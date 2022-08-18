public enum Enums {
  X,
  Y,
  Z
}

public enum Enums2 {
  X("X"),
  Y("Y"),
  Z("Z");

  private String name;

  public Enums2(String name) {

  }

  public String getName() {
    return name;
  }
}
