package dev.siliconcode.qatch.service;

import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Class to hold and load configuration data for the Qatch Evaluation Service
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class Config {

  @Getter private String runnerBinPath;
  @Getter private String projectPath;
  @Getter private String qmPath;
  @Getter private String analysisPath;
  @Getter private String outputPath;
  @Getter private String gradleHome;
  @Getter private String mvnHome;

  /** Method to read in the configuration data from the stored file */
  public void readConfig() {
    String qatchHome = System.getenv("QATCH_HOME");
    String qservHome = System.getenv("QSERVICE_HOME");

    Yaml yaml = new Yaml();
    try (InputStream inputStream =
        Files.newInputStream(Paths.get(System.getenv("QSERVICE_HOME"), "conf", "qservice.yml"))) {
      Map<String, Object> obj = yaml.load(inputStream);
      runnerBinPath = ((String) obj.get("QatchBinPath")).replace("$QATCH_HOME", qatchHome);
      projectPath = ((String) obj.get("ProjectPath")).replace("$QSERVICE_HOME", qservHome);
      qmPath = ((String) obj.get("QmPath")).replace("$QATCH_HOME", qatchHome);
      analysisPath = ((String) obj.get("AnalysisPath")).replace("$QSERVICE_HOME", qservHome);
      outputPath = ((String) obj.get("OutputPath")).replace("$QSERVICE_HOME", qservHome);
      mvnHome = ((String) obj.get("MavenHome")).replace("$MAVEN_HOME", System.getenv("MAVEN_HOME"));
      gradleHome = ((String) obj.get("GradleHome")).replace("$GRADLE_HOME", System.getenv("GRADLE_HOME"));
      gradleHome = gradleHome.replace("~", System.getProperty("user.dir"));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
