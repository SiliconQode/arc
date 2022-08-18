package dev.siliconcode.qatch.service.lang.providers;

import dev.siliconcode.qatch.service.data.Project;
import lombok.NonNull;
import org.apache.commons.lang3.NotImplementedException;

/**
 * A Language Provider for the C# Languages
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class CSharpProvider extends AbstractLanguageProvider {

  /** Constructs a new CSharpProvider */
  public CSharpProvider() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public void initComponents(@NonNull Project project) {
    throw new NotImplementedException("Operation not yet implemented.");
  }
}
