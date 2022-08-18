public class F {

  private A a;
  private B b;

  // toString
  public String toString() {
    return "F [a=" + a.toString() + ", b=" + b.toString() + "]";
  }

  // Method 1
  public void method1() {
    for (int i = 0; i < 10; i++)
      System.out.println(i);
  }

  // Method 2
  public void method2() {
    int i = 0;
    while (i < 10) {
      System.out.println(i++);
    }
  }
}
