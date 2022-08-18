package dev.siliconcode.qatch.service.handlers;

import dev.siliconcode.qatch.service.eval.EvalRunner;
import dev.siliconcode.qatch.service.ServerContext;
import dev.siliconcode.qatch.service.data.Content;
import dev.siliconcode.qatch.service.data.DbManager;
import dev.siliconcode.qatch.service.data.Project;
import dev.siliconcode.qatch.service.lang.LanguageProvider;
import dev.siliconcode.qatch.service.lang.providers.ProviderLoader;
import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Handles the quality evaluation of a candidates submitted code Expected data format:
 *
 * <pre>{@code
 * {
 *   "submission_id": '<id>',
 *   "project": 'projectName',
 *   "language": 'language',
 *   "dependencies": [
 *     "",
 *     "",
 *     "",
 *     ""
 *   ]
 *   "files": [
 *     {
 *       "name": "file1.java",
 *       "contents": "..."
 *     },
 *     {
 *       "name": "file2.java",
 *       "contents": "..."
 *     }
 *   ]
 * }
 * }</pre>
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Slf4j
public class EvaluateHandler extends BasePostHandler {

  private String submissionId;

  /**
   * Constructs a new EvaluateHandler with the given ServerContext
   *
   * @param context The ServerContext
   */
  public EvaluateHandler(@NonNull ServerContext context) {
    super(context);
  }

  /** {@inheritDoc} */
  @OpenApi(
      summary = "Submit code for evaluation",
      operationId = "createEvaluation",
      path = "/api/evaluate",
      method = HttpMethod.POST,
      tags = {"Evaluate"},
      responses = {@OpenApiResponse(status = "200")})
  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    if (Objects.equals(ctx.contentType(), "application/json")) {
      Gson gson = new Gson();
      Content cont = gson.fromJson(ctx.body(), Content.class);

      processData(cont);

      ctx.json(getMessage());
    } else {
      ctx.json(getErrorMessage());
    }
  }

  /** {@inheritDoc} */
  @Override
  public void processData(@NotNull Content data) {
    // TODO Move all of this to a separate thread
    submissionId = data.getSubmission_id();
    String language = data.getLanguage();

    log.info("Constructing Files");
    List<Map<String, String>> files = data.getFiles();

    log.info("Creating Project");
    DbManager.instance().open();
    Project project; // = Project.findFirst("submissionId = ?", submissionId);
    // if (project == null) {
    project =
        Project.builder()
            .id(submissionId)
            .name(data.getProject())
            .path(
                Paths.get(
                        Paths.get(context.getConfig().getProjectPath()).toAbsolutePath().toString(),
                        data.getProject())
                    .toAbsolutePath()
                    .toString())
            .resultsPath(
                Paths.get(
                        Paths.get(context.getConfig().getOutputPath()).toAbsolutePath().toString(),
                        data.getProject())
                    .toAbsolutePath()
                    .toString())
            .language(data.getLanguage())
            .build();
    // }
    DbManager.instance().close();

    log.info("Creating Files");
    LanguageProvider provider = ProviderLoader.instance().getProvider(language);
    provider.initComponents(project);
    provider
        .getFileBuilder()
        .createFiles(project, files, data.getDependencies().toArray(new String[0]));

    log.info("Evaluating Project");
    EvalRunner runner = new EvalRunner();
    runner.execute(project);
  }

  /** {@inheritDoc} */
  @Override
  public Object getMessage() {
    return "Evaluation in progress";
  }

  /** {@inheritDoc} */
  @Override
  public String getErrorMessage() {
    return "Something went wrong";
  }
}
