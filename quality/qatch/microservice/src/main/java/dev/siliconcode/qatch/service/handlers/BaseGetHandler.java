package dev.siliconcode.qatch.service.handlers;

import dev.siliconcode.qatch.service.ServerContext;
import com.google.common.collect.Maps;
import io.javalin.http.Handler;
import lombok.NonNull;

import java.util.Map;

/**
 * Abstract basic Get Handler
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public abstract class BaseGetHandler implements Handler {
  /** The server context instance */
  protected ServerContext context;
  /** A map of the params passed to the handler */
  protected Map<String, String> params;

  /**
   * Constructs a new basic with an empty param list and the given ServerContext
   *
   * @param context The ServerContext
   */
  public BaseGetHandler(@NonNull ServerContext context) {
    this.params = Maps.newHashMap();
    this.context = context;
  }

  /**
   * Return the object representing the message to be returned to the caller.
   *
   * @return Message object.
   */
  public abstract Object getMessage();
}
