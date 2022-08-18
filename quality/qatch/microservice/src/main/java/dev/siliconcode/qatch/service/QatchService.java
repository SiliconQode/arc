package dev.siliconcode.qatch.service;

import dev.siliconcode.qatch.service.data.DbManager;
import dev.siliconcode.qatch.service.handlers.EvaluateHandler;
import dev.siliconcode.qatch.service.handlers.EvaluationHandler;
import dev.siliconcode.qatch.service.handlers.StatusHandler;
import dev.siliconcode.qatch.service.lang.providers.ProviderLoader;
import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi.Style;
import picocli.CommandLine.Help.ColorScheme;
import picocli.CommandLine.Option;

/**
 * Main class which sets up and executes the Qatch Service
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Command(
    name = "qservice",
    sortOptions = false,
    headerHeading = "@|bold,underline Usage|@:%n%n",
    synopsisHeading = "%n",
    descriptionHeading = "%n@|bold,underline Description|@:%n%n",
    parameterListHeading = "%n@|bold,underline Parameters|@:%n%n",
    optionListHeading = "%n@|bold,underline Options|@:%n",
    header = "Microservice wrapper for the Qatch Quality Evaluation System.",
    description = "blah blah blah...",
    footerHeading = "%n",
    footer = "Copyright (c) 2022 Empirilytics")
@Slf4j
public class QatchService implements Runnable {

  private static final String ANSI_CYAN = "\u001B[36m";
  private static final String ANSI_BLUE = "\u001B[34m";
  private static final String ANSI_YELLOW = "\u001B[33m";
  private static final String ANSI_RESET = "\u001B[0m";

  private static final ColorScheme colorScheme =
      new ColorScheme.Builder()
          .commands(Style.bold, Style.underline)
          .options(Style.fg_yellow)
          .parameters(Style.fg_yellow)
          .optionParams(Style.italic)
          .errors(Style.fg_red, Style.bold)
          .stackTraces(Style.italic)
          .applySystemProperties()
          .build();

  @Option(
      names = {"-h", "--help"},
      usageHelp = true,
      description = "show the help message and exit")
  @Getter
  private boolean helpRequested = false;

  @Option(
      names = {"-p", "--port"},
      description = "The port on which the server listens",
      paramLabel = "PORT")
  @Getter
  private int port = 8000;

  /** Constructs the qatch service and sets up the database */
  public QatchService() {}

  /**
   * Configures the components to setup the api documentation
   *
   * @return The api documentation plugin
   */
  public OpenApiPlugin getConfiguredOpenApiPlugin() {
    Info info = new Info().version("2.0.0").description("Qatch API");
    OpenApiOptions options =
        new OpenApiOptions(info)
            .activateAnnotationScanningFor("dev.siliconcode.qatch.service")
            .path("/swaqger-docs") // endpoint for OpenAPI json
            .swagger(new SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
            .reDoc(new ReDocOptions("/redoc")) // endpoint for redoc
            .defaultDocumentation(
                doc -> {
                  doc.json("500", ErrorResponse.class);
                  doc.json("503", ErrorResponse.class);
                });
    return new OpenApiPlugin(options);
  }

  /**
   * Entry point for the application
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    new CommandLine(new QatchService()).setColorScheme(colorScheme).execute(args);
  }

  /** {@inheritDoc} */
  @Override
  public void run() {
    System.out.println(
        ANSI_BLUE
            + "            ____        __       __   _____                 _"
            + ANSI_RESET
            + "\n"
            + ANSI_BLUE
            + "           / __ \\____ _/ /______/ /_ / ___/___  ______   __(_)_______"
            + ANSI_RESET
            + "\n"
            + ANSI_BLUE
            + "          / / / / __ `/ __/ ___/ __ \\\\__ \\/ _ \\/ ___/ | / / / ___/ _ \\"
            + ANSI_RESET
            + "\n"
            + ANSI_BLUE
            + "         / /_/ / /_/ / /_/ /__/ / / /__/ /  __/ /   | |/ / / /__/  __/"
            + ANSI_RESET
            + "\n"
            + ANSI_BLUE
            + "         \\___\\_\\__,_/\\__/\\___/_/ /_/____/\\___/_/    |___/_/\\___/\\___/"
            + ANSI_RESET);
    System.out.println();
    System.out.println(
        "                         " + ANSI_YELLOW + "https://empirilytics.com" + ANSI_RESET);
    System.out.println();
    System.out.println();

    if (isHelpRequested()) System.exit(0);
    else {
      try {
        DbManager.instance().loadCredentials();
        DbManager.instance().checkDatabaseAndCreateIfMissing();

        ServerContext context = ServerContext.instance();
        context.loadConfig();

        QueuedThreadPool pool = new QueuedThreadPool(200, 8, 60000);

        ProviderLoader.instance();

        Javalin app =
            Javalin.create(
                config -> {
                  config.server(() -> new Server(pool));
                  config.registerPlugin(getConfiguredOpenApiPlugin());
                  config.defaultContentType = "application/json";
                });

        app.get("api/status", new StatusHandler(context));
        app.get("api/evaluation/{id}", new EvaluationHandler(context));
        app.post("api/evaluate", new EvaluateHandler(context));

        app.start(getPort());
      } catch (Exception ex) {
        log.error(ex.getMessage());
      }
    }
  }
}
