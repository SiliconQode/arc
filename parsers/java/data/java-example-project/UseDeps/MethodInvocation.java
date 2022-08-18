public class MethodInvocation {

  public void testMethodKnown(A a) {
    a.toString();
  }

  public void testMethodUnknown(String s) {
    s.toString();
  }
}
