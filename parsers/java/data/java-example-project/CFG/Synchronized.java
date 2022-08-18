public class Synchronized {

  public void methodSync1() {
    int i = 0;

    synchronized(this) {
      i += 1;
    }
  }
}
