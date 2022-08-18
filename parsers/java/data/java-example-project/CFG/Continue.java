public class Continue {

  public void methodContinue1() {
    int i = 0;
    while (true) {
      if (i < 10) {
        i++;
        continue;
      } else
        i--;
    }
  }
}
