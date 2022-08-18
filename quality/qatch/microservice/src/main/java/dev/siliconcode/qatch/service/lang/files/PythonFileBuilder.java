package dev.siliconcode.qatch.service.lang.files;

import dev.siliconcode.qatch.service.data.Project;
import dev.siliconcode.qatch.service.lang.FileBuilder;
import dev.siliconcode.qatch.service.lang.LanguageProvider;
import lombok.NonNull;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Constructs the file system from the input of the service
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class PythonFileBuilder extends FileBuilder {

  /**
   * Constructs a new PythonFileBuilder contained by the provided LanguageProvider
   *
   * @param provider The LanguageProvider containing this FileBuilder
   */
  public PythonFileBuilder(LanguageProvider provider) {
    super(provider);
  }

  /** {@inheritDoc} */
  @Override
  public void createFile(
      @NonNull Project project, @NonNull String fileName, @NonNull String content)
      throws IllegalArgumentException {
    throw new NotImplementedException("Operation not yet implemented");
  }

  /** {@inheritDoc} */
  @Override
  public void createFiles(
      @NonNull Project proj,
      @NonNull List<Map<String, String>> files,
      @NotNull @NonNull String[] dependencies) {
    throw new NotImplementedException("Operation not yet implemented");
  }
}
