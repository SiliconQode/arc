package dev.siliconcode.arc.metrics.ckjm.core.io;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

/**
 * @author Isaac Griffith
 * @version 2.5.0
 */
public class OutputHandlerFactory {

    private OutputHandlerFactory() {}

    private static class FactoryHelper {
      private static final OutputHandlerFactory INSTANCE = new OutputHandlerFactory();
    }

    public static OutputHandlerFactory instance() {
      return FactoryHelper.INSTANCE;
    }

    public CkjmOutputHandler getConsoleHandler(boolean usesStandardIO, OutputType type) {
      if (usesStandardIO) {
        PrintStream ps = new PrintStream(System.out);
        switch(type) {
          case XML:
            return new PrintXmlResults(ps);
          case JSON:
            return new PrintJsonResults(ps);
          default:
            return new PrintPlainResults(ps);
        }
      } else {
        return new NullOutputHandler();
      }
    }

    public CkjmOutputHandler getFileHandler(String fileLoc, OutputType type) {
      if (fileLoc != null) {
        try {
          Path file = Paths.get(fileLoc);
          Files.deleteIfExists(file);
          Files.createDirectories(file.getParent());
          Files.createFile(file);

          PrintWriter pw = new PrintWriter(Files.newOutputStream(file));

          switch(type) {
            case XML:
              return new WriteXmlResults(pw);
            case JSON:
              return new WriteJsonResults(pw);
            default:
              return new WritePlainResults(pw);
          }
        } catch (IOException ex) {
          return new NullOutputHandler();
        }
      } else {
        return new NullOutputHandler();
      }
    }
}
