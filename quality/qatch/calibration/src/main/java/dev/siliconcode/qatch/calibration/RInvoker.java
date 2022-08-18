package dev.siliconcode.qatch.calibration;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.file.*;
import java.util.List;

/**
 * Class used to invoke the R Statistical Language in order to process thresholds and weights.
 *
 * <p>TODO The goal is to remove the need for this by implementing R scripts as Classes
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class RInvoker {

  public static final String BASE_DIR = System.getProperty("user.dir"); // TODO Remove this
  public static final String R_WORK_DIR =
      new File(BASE_DIR + "/R_Working_Directory").getAbsolutePath(); // TODO Remove this
  public static final String R_THRES_SCRIPT =
      new File(R_WORK_DIR + "/thresholdsExtractor.R").getAbsolutePath(); // TODO Remove this
  public static final String R_AHP_SCRIPT =
      new File(R_WORK_DIR + "/ahpWeightElicitation.R").getAbsolutePath(); // TODO Remove this
  public static final String R_FAHP_SCRIPT =
      new File(R_WORK_DIR + "/fahpWeightElicitator.R").getAbsolutePath(); // TODO Remove this
  public static String weightsScript = R_AHP_SCRIPT; // TODO Remove this
  public static String R_BIN_PATH = ""; // TODO Remove this

  /** A method for executing a certain R script... */
  public void executeRScript(String rPath, String scriptPath, String args) {
    try {
      if (System.getProperty("os.name").contains("Windows")) {
        scriptPath = "\"" + scriptPath + "\"";
        args = "\"" + args + "\"";
      }

      Runtime.getRuntime().exec(rPath + "/RScript " + scriptPath + " " + args);
    } catch (IOException e) {
      log.info(e.getMessage());
    }
  }

  /** A method for executing the R script that calculates the thresholds of the properties. */
  public void executeRScriptForThresholds() {

    // TODO: Remove this prints
    log.info("* R_BIN_PATH= " + R_BIN_PATH);
    log.info("* R_AHP_SCRIPT= " + R_THRES_SCRIPT);
    log.info("* DIR= " + BASE_DIR);
    log.info("* ");

    // Invoke the appropriate R script for threshold extraction - Use the fixed paths
    executeRScript(RInvoker.R_BIN_PATH, RInvoker.R_THRES_SCRIPT, R_WORK_DIR);

    // Wait for the RScript.exe to finish the analysis by polling the directory for changes
    Path resultsPath = Paths.get(R_WORK_DIR);

    try {
      // Create a directory watcher that watches for certain events
      WatchService watcher = resultsPath.getFileSystem().newWatchService();
      resultsPath.register(
          watcher,
          StandardWatchEventKinds.ENTRY_CREATE,
          StandardWatchEventKinds.ENTRY_DELETE,
          StandardWatchEventKinds.ENTRY_MODIFY);
      WatchKey watchKey = watcher.take();

      // Poll the directory for certain events
      // Wake up the thread when a directory or a file is created, modified or deleted in the
      // desired directory
      List<WatchEvent<?>> events = watchKey.pollEvents();
      for (WatchEvent event : events) {
        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
          log.info("* Created: " + event.context().toString());
        }
        if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
          log.info("* Deleted: " + event.context().toString());
        }
        if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
          log.info("* Modified: " + event.context().toString());
        }
      }

    } catch (IOException | InterruptedException e) {
      log.error(e.getMessage());
    }
  }

  /** A method for executing the R script that calculates the weights of the model. */
  public void executeRScriptForWeightsElicitation() {

    // Invoke the appropriate R script for threshold extraction - Use the fixed paths
    // TODO: Remove this prints
    log.info("* R_BIN_PATH= " + R_BIN_PATH);
    log.info("* R_AHP_SCRIPT= " + weightsScript);
    log.info("* DIR= " + BASE_DIR);
    log.info("* ");

    executeRScript(RInvoker.R_BIN_PATH, RInvoker.weightsScript, BASE_DIR);

    // Wait for the RScript.exe to finish the analysis by polling the directory for changes
    Path resultsPath = Paths.get(R_WORK_DIR);

    try {
      // Create a directory watcher that watches for certain events
      WatchService watcher = resultsPath.getFileSystem().newWatchService();
      resultsPath.register(
          watcher,
          StandardWatchEventKinds.ENTRY_CREATE,
          StandardWatchEventKinds.ENTRY_DELETE,
          StandardWatchEventKinds.ENTRY_MODIFY);
      WatchKey watchKey = watcher.take();

      // Poll the directory for certain events
      // Wake up the thread when a directory or a file is created, modified or deleted in the
      // desired directory
      List<WatchEvent<?>> events = watchKey.pollEvents();
      for (WatchEvent event : events) {
        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
          log.info("* Created: " + event.context().toString());
        }
        if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
          log.info("* Deleted: " + event.context().toString());
        }
        if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
          log.info("* Modified: " + event.context().toString());
        }
      }

    } catch (IOException | InterruptedException e) {
      log.info(e.getMessage());
    }
  }

  /** A method that loads the path of the RScript executable */
  public static String loadRScriptExecutablePath() {

    String rPath = null;

    try {
      FileReader fw = new FileReader(new File(R_WORK_DIR + "/RScript_Path.txt").getAbsolutePath());
      BufferedReader bw = new BufferedReader(fw);

      rPath = bw.readLine();

      bw.close();
      fw.close();

      if (rPath.contains(" ")) {
        rPath = "\"" + rPath + "\"";
      }

    } catch (IOException e) {
      log.error(e.getMessage());
    }

    return rPath;
  }
}
