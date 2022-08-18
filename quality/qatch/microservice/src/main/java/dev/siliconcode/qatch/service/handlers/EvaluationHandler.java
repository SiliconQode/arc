package dev.siliconcode.qatch.service.handlers;

import dev.siliconcode.qatch.service.ServerContext;
import dev.siliconcode.qatch.service.data.DbManager;
import dev.siliconcode.qatch.service.data.Project;
import dev.siliconcode.qatch.service.data.Results;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A Handler for the endpoint which returns the status/results of a given evaluation
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Slf4j
public class EvaluationHandler extends BaseGetHandler {

  /**
   * Constructs a new Evaluation Handler with the given ServerContext
   *
   * @param context The ServerContext
   */
  public EvaluationHandler(@NonNull ServerContext context) {
    super(context);
  }

  /** {@inheritDoc} */
  @OpenApi(
      summary = "Retrieve Evaluation Results",
      operationId = "getEvaluation",
      path = "/api/evaluation/{id}",
      method = HttpMethod.GET,
      tags = {"Evalution"},
      responses = {
        @OpenApiResponse(
            status = "200",
            content = {@OpenApiContent(from = Results.class)})
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
    String id = params.get("id");

    DbManager.instance().open();
    Project proj = Project.findFirst("submissionId = ?", id);
    Results res = Results.findFirst("submissionId = ?", id);

    String json = "";
    if (res != null) {
      json = res.toJson(true);
    } else {
      if (proj != null) {
        json = "\"Evaluation has not completed\"";
      } else {
        json = "\"No such project with id: " + id + "\"";
      }
    }
    DbManager.instance().close();

    return json;
  }
}
