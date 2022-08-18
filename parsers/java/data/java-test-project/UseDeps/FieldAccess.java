public class FieldAccess {

  public void testMethodKnown() {
    A.name;
  }

  public void testMethodUnknown() {
    String.value;
  }
}
