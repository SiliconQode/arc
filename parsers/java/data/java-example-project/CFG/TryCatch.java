import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class TryCatch {

  public void methodTryCatch1() {
    try {
      ;
    } catch (RuntimeException e) {

    }
  }

  public void methodTryCatch2() {
    try (BufferedReader br = new BufferedReader(new FileReader("file"))) {
      ;
    } catch (IOException e) {
      ;
    }
  }

  public void methodTryCatch3() {
    try {
      ;
    } catch (IOException e) {
      ;
    } finally {
      ;
    }
  }
}
