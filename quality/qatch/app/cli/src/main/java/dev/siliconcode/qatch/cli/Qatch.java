package dev.siliconcode.qatch.cli;

import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;

/**
 * Main entry point class for the Qatch Evaluation System
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class Qatch {

  private static final String ANSI_CYAN = "\u001B[36m";
  private static final String ANSI_BLUE = "\u001B[34m";
  private static final String ANSI_YELLOW = "\u001B[33m";
  private static final String ANSI_RESET = "\u001B[0m";

  /**
   * The entry point method from the command line
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    QatchContext context = new QatchContext();
    CommandLine cmdLine = new CommandLine(context);
    CommandLine.populateCommand(context, args);

    if (context.isHelpRequested() || context.isVersionRequested()) {
      if (context.isHelpRequested())
        CommandLine.usage(context, System.out);
      if (context.isVersionRequested())
        cmdLine.printVersionHelp(System.out);
      System.exit(0);
    }

    context.run();

    Qatch qatch = new Qatch();
    qatch.exec(context);
  }

  /**
   * Execute the Qatch analysis
   *
   * @param context global execution context
   */
  public void exec(QatchContext context) {
    System.out.println(
            ANSI_BLUE + "                       ____        __       __" + ANSI_RESET + "\n" +
            ANSI_BLUE + "                      / __ \\____ _/ /______/ /_" + ANSI_RESET + "\n" +
            ANSI_BLUE + "                     / / / / __ `/ __/ ___/ __ \\" + ANSI_RESET + "\n" +
            ANSI_BLUE + "                    / /_/ / /_/ / /_/ /__/ / / /" + ANSI_RESET + "\n" +
            ANSI_BLUE + "                    \\___\\_\\__,_/\\__/\\___/_/ /_/" + ANSI_RESET + "\n");
    System.out.println(ANSI_YELLOW + "                   http://siliconcode.dev/qatch" + ANSI_RESET + "\n\n");

    try {
      Runner runner;
      if (context.isCalibration() || context.isWeightsElicitation()) {
        runner = new BenchmarkRunner(context);
      } else {
        if (context.isMultiProject()) {
          runner = new MultiProjectRunner(context);
        } else {
          runner = new SingleProjectRunner(context);
        }
      }
      runner.run();
    } catch (Exception e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }
  }
}
