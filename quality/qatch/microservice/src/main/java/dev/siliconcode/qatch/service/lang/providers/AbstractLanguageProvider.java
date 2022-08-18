package dev.siliconcode.qatch.service.lang.providers;

import dev.siliconcode.qatch.service.lang.CodeProcessor;
import dev.siliconcode.qatch.service.lang.FileBuilder;
import dev.siliconcode.qatch.service.lang.LanguageProvider;
import dev.siliconcode.qatch.service.lang.ProjectBuilder;
import lombok.Getter;

/** Abstract class containing the base attributes for all language providers */
public abstract class AbstractLanguageProvider implements LanguageProvider {

  /** The FileBuilder for this Language Provider */
  @Getter protected FileBuilder fileBuilder;

  /** The ProjectBuilder for this Language Provider */
  @Getter protected ProjectBuilder projectBuilder;

  /** The CodeProcessor for this Language Provider */
  @Getter protected CodeProcessor codeProcessor;

  /** Basic constructor */
  public AbstractLanguageProvider() {}
}
