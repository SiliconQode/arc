package dev.siliconcode.qatch.service.lang.providers;

import dev.siliconcode.qatch.service.ServerContext;
import dev.siliconcode.qatch.service.lang.builder.JavaBuilder;
import dev.siliconcode.qatch.service.data.Project;
import dev.siliconcode.qatch.service.lang.files.JavaFileBuilder;
import dev.siliconcode.qatch.service.lang.processors.JavaCodeProcessor;
import lombok.NonNull;

/**
 * Language provider for the Java language
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class JavaProvider extends AbstractLanguageProvider {

  /** Constructs a new JavaProvider */
  public JavaProvider() {
    super();
  }

  /** {@inheritDoc} */
  public void initComponents(@NonNull Project project) {
    ServerContext context = ServerContext.instance();
    codeProcessor = new JavaCodeProcessor(this, project.name());
    projectBuilder =
        new JavaBuilder(
            this,
            project.path(),
            context.getConfig().getMvnHome(),
            context.getConfig().getGradleHome());
    fileBuilder = new JavaFileBuilder(this);
  }
}
