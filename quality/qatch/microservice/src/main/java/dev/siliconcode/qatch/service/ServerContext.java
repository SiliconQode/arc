package dev.siliconcode.qatch.service;

import lombok.Getter;

/**
 * A single point of data control for use across the service. Implemented as a Singleton.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class ServerContext {

  @Getter private final Config config;

  /** Private constructor of the singleton, sets up an empty config * */
  private ServerContext() {
    config = new Config();
  }

  /** Internal class which holds the singleton instance. Design to be lazy loaded and thread safe */
  private static final class InstanceHolder {
    private static final ServerContext INSTANCE = new ServerContext();
  }

  /**
   * Method to obtain the singleton instance
   *
   * @return The singleton instance
   */
  public static ServerContext instance() {
    return InstanceHolder.INSTANCE;
  }

  /** Loads the config for the service */
  public void loadConfig() {
    config.readConfig();
  }
}
