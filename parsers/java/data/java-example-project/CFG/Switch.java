public class Switch {

  public void methodSwitch1() {
      String emote = "happy";
      String emoticon = "/-s";
      switch (emote) {
         case "happy":
            emoticon = ":-)";
            break;
         default:
            emoticon = ":-?";
            break;
      }
      System.out.println(emoticon);
  }
}
