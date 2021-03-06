package org.foo;

abstract class A {

  boolean cond, cond2;

  private void methodA() throws ExA {
    if (cond) throw new ExA();
  }

  private void methodAB() throws ExA, ExB {
    if (cond) throw new ExA();
    else if (cond2) throw new ExB();
  }

  void test() {
    Object o = null; // flow@normal {{Implies 'o' is null.}}
    try {
      methodA();
    } catch (ExA e) {

    }
    o.toString(); // Noncompliant [[flows=normal]]  flow@normal {{'o' is dereferenced.}}
  }

  void test_multiple_ex_flows() {
    Object o = null; // flow@ex1,ex2 {{Implies 'o' is null.}}
    try {
      methodAB();  // flow@ex1 {{'ExA' is thrown.}} flow@ex2 {{'ExB' is thrown.}}
      o = new Object();
    } catch (ExA e) { // flow@ex1 {{'ExA' is caught.}}

    } catch (ExB e) { // flow@ex2 {{'ExB' is caught.}}

    }
    o.toString(); // Noncompliant [[flows=ex1,ex2]]  flow@ex1,ex2 {{'o' is dereferenced.}}
  }

  abstract void noBehavior() throws ExA, ExB;

  void test_method_with_no_behavior() {
    Object o = null; // flow@nb1,nb2 {{Implies 'o' is null.}}
    try {
      noBehavior();  // flow@nb1 {{'ExA' is thrown.}} flow@nb2 {{'ExB' is thrown.}}
      o = new Object();
    } catch (ExA e) { // flow@nb1 {{'ExA' is caught.}}

    } catch (ExB e) { // flow@nb2 {{'ExB' is caught.}}

    }
    o.toString(); // Noncompliant [[flows=nb1,nb2]]  flow@nb1,nb2 {{'o' is dereferenced.}}
  }

  void foo() {
    A a = null; // flow@single_flow
    try {
      doSomething(); // equivalent flow using exception thrown here discarded
    } catch (MyException e) {
      log(e.getMessage());
    }
    a.call(); // Noncompliant [[flows=single_flow]] flow@single_flow
  }

  abstract void doSomething() throws MyException;
  abstract void log(String s);
  abstract void call();

  class ExA extends Exception {}
  class ExB extends Exception {}
  class MyException extends Exception {}
}
