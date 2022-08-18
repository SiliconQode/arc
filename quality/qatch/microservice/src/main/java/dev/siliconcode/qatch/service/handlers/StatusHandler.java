package dev.siliconcode.qatch.service.handlers;

import dev.siliconcode.qatch.service.ServerContext;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import lombok.NonNull;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A Basic Service Status (heartbeat) call.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class StatusHandler extends BaseGetHandler {

  /**
   * constructs a new StatusHandler with the given ServerContext
   *
   * @param context Server context
   */
  public StatusHandler(@NonNull ServerContext context) {
    super(context);
  }

  /** {@inheritDoc} */
  @OpenApi(
      summary = "Checks the status of the service",
      operationId = "status",
      path = "/api/status",
      method = HttpMethod.GET,
      tags = {"Status"},
      responses = {
        @OpenApiResponse(
            status = "200",
            content = {@OpenApiContent(from = String.class)})
      })
  @Override
  public void handle(@NotNull Context ctx) {
    params = ctx.pathParamMap();
    ctx.res.setStatus(HttpStatus.OK_200);
    ctx.json(getMessage());
  }

  /** {@inheritDoc} */
  @Override
  public Object getMessage() {
    return "Good to Go!";
  }
}
