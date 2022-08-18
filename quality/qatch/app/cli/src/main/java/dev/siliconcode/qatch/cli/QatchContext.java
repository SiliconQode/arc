package dev.siliconcode.qatch.cli;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.yaml.snakeyaml.Yaml;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Class which maintains the configuration context for the entire Qatch Evaluation system.
 * Additionally, it also provides the necessary logic for loading and maintaining the current
 * Language Provider
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@CommandLine.Command(
    name = "qatch",
    sortOptions = false,
    headerHeading = "@|bold,underline Usage|@:%n%n",
    synopsisHeading = "%n",
    descriptionHeading = "%n@|bold,underline Description|@:%n%n",
    parameterListHeading = "%n@|bold,underline Parameters|@:%n%n",
    optionListHeading = "%n@|bold,underline Options|@:%n",
    header = "The Qatch Quality Evaluation System.",
    description = "blah blah blah...",
    footerHeading = "%n",
    footer = "Copyright (c) 2022 Empirilytics",
    version = {"Qatch version 2.0.0", "Copyright (c) 2022 Empirilytics"},
    usageHelpAutoWidth = true,
    usageHelpWidth = 120,
    helpCommand = false,
    mixinStandardHelpOptions = false)
@Log4j2
public class QatchContext {

  @Option(
      names = {"-h", "--help"},
      usageHelp = true,
      description = "Display this message and exit")
  @Getter
  private boolean helpRequested = false;

  @Option(
      names = {"-i", "--inspect-res"},
      description = "Flag to keep the inspection results")
  @Getter
  @Setter
  private boolean includeInspectRes = false;

  @Option(
      names = {"-s", "--static"},
      description = "Flag to execute static analysis")
  @Getter
  @Setter
  private boolean staticAnalysis = false;

  @Option(
      names = {"-p", "--parallel"},
      description = "Flag to execute project analyses in parallel")
  @Getter
  @Setter
  private boolean parallelAnalysis = false;

  @Option(
      names = {"-k", "--keep-results"},
      description = "Flag to keep the analysis results")
  @Getter
  @Setter
  private boolean keepResults = false;

  @Option(
      names = {"-b", "--use-benchmark"},
      description = "Flag indicating to use benchmark results")
  @Getter
  @Setter
  private boolean useBenchmarksResultDir = false;

  @Option(
      names = {"-r", "--results"},
      paramLabel = "PATH",
      description = "Path to the analysis results directory",
      required = true)
  @Setter
  private String analysisResPath = null;

  @Option(
      names = {"-w", "--workspace"},
      paramLabel = "PATH",
      description = "Path to the workspace",
      required = true)
  @Setter
  private String workspacePath = null;

  @Option(
      names = {"-q", "--quality-model"},
      paramLabel = "PATH",
      description = "Path to the quality model to be used.")
  @Setter
  private String qmPath = null;

  @Option(
      names = {"-o", "--output"},
      paramLabel = "PATH",
      description = "Path to the output results directory",
      required = true)
  @Setter
  private String resPath = null;

  @Option(
      names = {"-l", "--language"},
      paramLabel = "LANG",
      description =
          "The language to evaluate. Available languages include: 'java' - The Java language, 'python' - The python language",
      required = true)
  @Getter
  @Setter
  private String language = null;

  @Option(
      names = {"-c", "--calibration"},
      description = "Conduct a benchmark calibration (threshold extraction) analysis")
  @Getter
  @Setter
  private boolean calibration = false;

  @Option(
      names = {"-e", "--weight-elicitation"},
      description = "Conduct a weights elicitation analysis")
  @Getter
  @Setter
  private boolean weightsElicitation = false;

  @Option(
      names = {"-d", "--model-derive"},
      description = "Conduct a general model derivation")
  @Getter
  @Setter
  private boolean modelDerivation = false;

  @Option(
      names = {"-m", "--multi-project"},
      description =
          "Flag indicating the analysis is for multiple projects. Thus subfolders in the workspace path will be considered separate projects, otherwise the workspace directory will be considered a single project.")
  @Getter
  @Setter
  private boolean multiProject = false;

  @Option(
      names = {"-u", "--use-prior-analysis"},
      description = "Flag indicating that a prior analysis will be used")
  @Getter
  @Setter
  private boolean usePriorAnalysis = false;

  @Option(
      names = {"-n", "--bench-repo"},
      paramLabel = "PATH",
      description = "Path to the benchmark repo")
  @Setter
  private String benchRepoPath = null;
  @Setter private String configPath = null;
  @Setter private String resultsPath = null;
  @Setter private String modelsPath = null;
  @Getter @Setter private LanguageProvider currentProvider;

  @Option(
      names = {"-x", "--bench-res-path"},
      paramLabel = "PATH",
      description = "Path to the benchmark results")
  @Setter
  private String benchmarkResPath = null;

  @Option(
      names = {"-v", "--version"},
      versionHelp = true,
      description = "Print version information and exit")
  @Getter
  boolean versionRequested = false;

  private Map<String, LanguageProvider> providers;
  private final List<String> languages = ImmutableList.of("java", "python", "cs-python");
  private Map<String, Map<String, String>> toolsConfig;

  /** Reads the configuration information from the conf directory */
  public void readConfig() {
    Yaml yaml = new Yaml();
    Path config = Paths.get(System.getenv("QATCH_HOME"), "conf");
    configPath = config.toAbsolutePath().toString();
    config = Paths.get(configPath, "qatch.yml");
    try (InputStream inputStream = Files.newInputStream(config)) {
      Map<String, Object> obj = yaml.load(inputStream);
      resultsPath =
          ((String) obj.get("ResultsPath")).replace("$QATCH_HOME", System.getenv("QATCH_HOME"));
      modelsPath = ((String) obj.get("Models")).replace("$QATCH_HOME", System.getenv("QATCH_HOME"));
      List<Map<String, String>> providerList = (List<Map<String, String>>) obj.get("Providers");
      toolsConfig = (Map<String, Map<String, String>>) obj.get("Tools");
      loadProviders(providerList);
    } catch (Exception ex) {
      log.error(ex.getMessage());
      ex.printStackTrace();
    }
  }

  /**
   * Loads the language provides from the list of strings, where each element in the list is a fully
   * specified class name of a language provider
   *
   * @param list List of Maps of language provider class names, cannot be null
   */
  public void loadProviders(@NonNull List<Map<String, String>> list) {
    providers = Maps.newHashMap();
    for (Map<String, String> map : list) {
      map.forEach((key, value) -> {
        log.info(String.format("Loading Language Provider: (%s) -> (%s)", key, value));
        try {
          Class<? extends LanguageProvider> cls =
            (Class<? extends LanguageProvider>) Class.forName(value);
          Method method = cls.getDeclaredMethod("instance");
          LanguageProvider provider = (LanguageProvider) method.invoke(null);
          providers.put(provider.getLanguage(), provider);
        } catch (Exception ex) {
          log.error(ex.getMessage());
        }
      });
    }
  }

  /** Initializes the provider for the selected language */
  public void initProvider() {
    currentProvider = providers.get(language);
    String home = System.getenv("QATCH_HOME");
    if (toolsConfig.get(language) != null) {
      toolsConfig.get(language).put("resultsPath", resultsPath);
      toolsConfig.get(language).put("configPath", configPath);
      toolsConfig.get(language).forEach((k, v) -> {
        if (v.contains("$QATCH_HOME"))
          toolsConfig.get(language).put(k, v.replace("$QATCH_HOME", home));
      });
      currentProvider.initialize(toolsConfig.get(language));
    }
  }

  /** Sets up the global execution path */
  public void run() {
    readConfig();

    if (!languages.contains(getLanguage())) {
      displayHelp("Unknown Language: " + language);
    } else {
      initProvider();
    }
    try {
      setWorkspacePath(findPath(getWorkspacePath(), false));
      setResPath(findOrCreatePath(getResPath()));
      setResultsPath(findOrCreatePath(getResultsPath()));
      setAnalysisResPath(findOrCreatePath(getAnalysisResPath()));
      setQmPath(findPath(getQmPath(), true));
      setBenchRepoPath(findPath(getBenchRepoPath(), false));
      setBenchmarkResPath(findOrCreatePath(getBenchmarkResPath()));
    } catch (Exception ex) {
      displayHelp(ex.getMessage());
    }
    setCalibration(isCalibration() || isModelDerivation());
    setWeightsElicitation(isWeightsElicitation() || isModelDerivation());
  }

  /**
   * Finds the path with the given location, and if the ifFile flag is true checks if the path is a
   * regular file or not
   *
   * @param loc The path to find
   * @param isFile Flag indicating whether to check as a regular file (true) or a directory (false)
   * @return The absolute path to the provided location
   * @throws Exception If the path exiss, but is not a regular file or directory (depending on the
   *     setting of the flag), or if the path does not exist.
   */
  private String findPath(String loc, boolean isFile) throws Exception {
    Path path = Paths.get(loc);
    path = path.toAbsolutePath();
    if (Files.exists(path)) {
      if (!Files.isDirectory(path) && !isFile) {
        throw new Exception(String.format("Path: %s exists, but is not a directory.", path));
      }
    } else {
      throw new Exception(String.format("Path: %s does not exist.", path));
    }

    return path.toString();
  }

  /**
   * Displays the help information, and if an error message is provided, that message is shown
   * preceeding the help information.
   *
   * @param error Error message
   */
  void displayHelp(String error) {
    if (error != null && !(error.isEmpty() || error.isBlank()))
      System.out.printf("Error: %s\n", error);
    CommandLine.usage(this, System.out);
  }

  /**
   * Finds or creates the path in the provided string
   *
   * @param loc Location to find or create, cannot be null or empty
   * @return The string of the absolute path that was found or created
   * @throws Exception If the provided location is null or empty, or if the path is not a directory
   *     or the parent directories could not be created
   */
  private String findOrCreatePath(@NonNull String loc) throws Exception {
    if (loc.isEmpty()) throw new IllegalArgumentException("Location cannot be null");
    Path path = Paths.get(loc);
    path = path.toAbsolutePath();
    if (Files.exists(path)) {
      if (!Files.isDirectory(path)) {
        throw new Exception(String.format("Path: %s exists, but is not a directory.", path));
      }
    } else {
      Files.createDirectories(path);
      if (!Files.exists(path))
        throw new Exception(String.format("Path: %s could not be created.", path));
    }

    return path.toString();
  }

  /** @return the normalized absolute path to the benchmark results directory */
  public String getBenchmarkResPath() {
    return Paths.get(benchmarkResPath).toAbsolutePath().normalize().toString();
  }

  /** @return the normalized absolute path to the benchmark repo (projects) directory */
  public String getBenchRepoPath() {
    return Paths.get(benchRepoPath).toAbsolutePath().normalize().toString();
  }

  /** @return the normalized absolute path to the configuration file */
  public String getConfigPath() {
    return Paths.get(configPath).toAbsolutePath().normalize().toString();
  }

  /** @return the normalized absolute path to the tool results directory */
  public String getResultsPath() {
    return Paths.get(resultsPath).toAbsolutePath().normalize().toString();
  }

  /** @return the normalized absolute path to the models directory */
  public String getModelsPath() {
    return Paths.get(modelsPath).toAbsolutePath().normalize().toString();
  }

  /** @return the normalized absolute path to the final results directory */
  public String getResPath() {
    return Paths.get(resPath).toAbsolutePath().normalize().toString();
  }

  /** @return the normalized absolute path to the project workspace directory */
  public String getWorkspacePath() {
    return Paths.get(workspacePath).toAbsolutePath().normalize().toString();
  }

  /** @return the normalized absolute path to the analysis results directory */
  public String getAnalysisResPath() {
    return Paths.get(analysisResPath).toAbsolutePath().normalize().toString();
  }

  /** @return the normalized absolute path to the current quality model */
  public String getQmPath(){
    return Paths.get(qmPath).toAbsolutePath().normalize().toString();
  }
}
