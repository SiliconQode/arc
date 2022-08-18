public class C {

  private E e;
  private D d;

  // toString
  public String toString() {
    return "E [d=" + d.toString() + ", e=" + e.toString() + "]";
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
