package dev.siliconcode.qatch.service.data;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DBException;

import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * A class which manages db connection and operations for use with ActiveJDBC. This class is
 * implemented as a singleton.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Slf4j
public class DbManager {

  //  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  //  private boolean open;
  private DbCredentials credentials;
  private List<String> tables = ImmutableList.of("results", "projects");

  /** Private constructor * */
  private DbManager() {}

  /**
   * Internal class which maintains the singleton instance. Uses a lazy-loading and thread-safe
   * approach
   */
  private static final class InstanceHolder {
    private static final DbManager INSTANCE = new DbManager();
  }

  /**
   * Provides access to the actual singleton instance.
   *
   * @return The singleton instance
   */
  public static DbManager instance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * Opens the connection to the database, only allows one thread access to this operation at any
   * time.
   */
  public void open() {
    //    lock.writeLock().lock();
    //    if (open) {
    //      return;
    //    }
    try {
      Base.open(credentials.driver(), credentials.url(), credentials.user(), credentials.pass());
      //      open = true;
    } catch (DBException ex) {
      log.error(ex.getMessage());
    }
  }

  /** Closes the current connection to the database, if open. */
  public void close() {
    //    if (!open) return;
    try {
      Base.close();
      //      if (lock.writeLock().tryLock()) lock.writeLock().unlock();
    } catch (DBException ex) {
      log.error(ex.getMessage());
    }
  }

  /** Loads the database credentials from the internally held databse.properties file */
  public void loadCredentials() {
    if (credentials != null) return;

    Properties prop = new Properties();
    InputStream is = DbManager.class.getResourceAsStream("/database.properties");
    if (is != null) {
      try {
        prop.load(new InputStreamReader(is));
      } catch (IOException ex) {
        log.error(ex.getMessage());
      }
    }
    try {
      if (is != null) is.close();
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }

    credentials =
        new DbCredentials(
            prop.getProperty("development.type"),
            prop.getProperty("development.driver"),
            prop.getProperty("development.url"),
            prop.getProperty("development.username"),
            prop.getProperty("development.password"));
  }

  /** Determines if the database is whole, and if not will reset the database. */
  public void checkDatabaseAndCreateIfMissing() {
    AtomicBoolean missing = new AtomicBoolean(false);
    try (Connection conn =
        DriverManager.getConnection(credentials.url(), credentials.user(), credentials.pass())) {

      DatabaseMetaData metaData = conn.getMetaData();
      for (String table : tables) {
        ResultSet rs = metaData.getTables(null, null, table, null);
        if (!rs.next()) missing.set(true);
      }
    } catch (SQLException ex) {
      log.error(ex.getMessage());
    }

    // if (missing.get()) resetDatabase();
  }

  /**
   * Code to actually reset the database to a brand-new instance. NOTE: this will drop all existing
   * data.
   */
  void resetDatabase() {
    log.info("Resetting the database to empty");

    if (credentials.type() == "sqlite") {
      String filename = credentials.url().split(":")[2];
      new File(filename).delete();
    } else {
      try (Connection conn =
          DriverManager.getConnection(credentials.url(), credentials.user(), credentials.pass())) {
        Statement sql = conn.createStatement();
        sql.execute("SET FOREIGN_KEY_CHECKS = 0");
        for (String table : tables) {
          sql.execute(String.format("drop table if exists %s;", table));
        }
        sql.execute("SET FOREIGN_KEY_CHECKS = 1");
      } catch (SQLException ex) {
        log.error(ex.getMessage());
      }
    }

    try (Connection conn =
        DriverManager.getConnection(credentials.url(), credentials.user(), credentials.pass())) {
      String resource = "/db/create_" + credentials.type().toLowerCase() + ".sql";
      InputStream is = DbManager.class.getResourceAsStream(resource);

      String text = "";
      if (is != null) {
        text = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining());
      }

      Statement sql = conn.createStatement();
      String[] inst = text.split(";");

      for (String s : inst) {
        // we ensure that there is no spaces before or after the request string
        // in order to not execute empty statements
        if (!s.trim().isEmpty()) {
          sql.execute(s);
        }
      }
    } catch (SQLException ex) {
      log.error(ex.getMessage());
    }

    log.info("Database reset and ready for fresh data.");
  }
}
