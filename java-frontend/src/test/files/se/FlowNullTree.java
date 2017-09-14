abstract class A {

  void test(Object o, boolean b2) {
    try {
      try {
        f();
      } finally {
        if (b2) {
          o = null;
          g();
          o = new Object();
        }
      }
      h();
    } finally {
      o.toString(); // Noncompliant
    }
  }

  abstract void f();
  abstract void g();
  abstract void h();
}

