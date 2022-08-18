public class Labeled {

  public void methodLabeledBreak() {
    int counter = 0;
    start: {
      for (int i = 0; i <= 10; i++) {
        for (int j = 0; j <= 10; j++) {
           if (i == 5)
              break start;
        }
        counter++;
      }
    }
  }

  public void methodLabeledContinue() {
    start: {
       for (int i = 0; i < 5; i++) {
         System.out.println();
         for (int j = 0; j < 10; j++) {
            System.out.print("#");
            if (j >= i)
               continue start;
         }
         System.out.println("This will never be printed");
       }
     }
   }
}
