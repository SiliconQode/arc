package dev.siliconcode.qatch.service.handlers;

import dev.siliconcode.qatch.service.ServerContext;
import dev.siliconcode.qatch.service.data.Content;
import io.javalin.http.Handler;
import lombok.NonNull;

import java.util.Map;

/**
 * Abstract base POST handler
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public abstract class BasePostHandler implements Handler {

  /** The server context instance */
  protected ServerContext context;

  /**
   * Constructs a new Base POST handler with the given ServerContext
   *
   * @param context The ServerContext
   */
  public BasePostHandler(@NonNull ServerContext context) {
    this.context = context;
  }

  /**
   * Method to process the data passed via the POST call
   *
   * @param data Map representing the data to be processed
   */
  public abstract void processData(@NonNull Content data);

  /**
   * The message object representing the data to be returned to the caller, when everything goes
   * correctly
   *
   * @return Message object.
   */
  public abstract Object getMessage();

  /**
   * The message to be returned if the handler encounters an error.
   *
   * @return The error message
   */
  public abstract String getErrorMessage();
}
