package dev.siliconcode.qatch.service.lang.processors

import dev.siliconcode.qatch.service.lang.CodeProcessor
import dev.siliconcode.qatch.service.lang.LanguageProvider
import groovy.transform.CompileStatic
import groovy.transform.NullCheck

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Correctly generates compilable Java code from the code passed to the service
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@CompileStatic
@NullCheck
class JavaCodeProcessor implements CodeProcessor {

    final LanguageProvider provider
    String projectName

    JavaCodeProcessor(LanguageProvider provider, String projectName) {
        this.provider = provider
        this.projectName = projectName
    }

    /** {@inheritdoc} */
    @Override
    String processSingleFileProject(String pathString, String content) {
        Path path = Paths.get(pathString)
        String className = path.getFileName().toString().replace(".java", "")

        content = """\
                  package ${getPackageOrDefault(path)};

                  ${getStandardImports()}

                  public class ${className} {
                  ${content.indent(2)};
                  }
                  """

        return content
    }

    /** {@inheritdoc} */
    @Override
    String getStandardImports() {
        String[] lines = JavaCodeProcessor.class.getResourceAsStream("/standard_imports/java.txt").readLines();
        return lines.join("\n");
    }

    /** {@inheritdoc} */
    @Override
    String getPackageOrDefault(Path path) {
        if (path.isEmpty())
            return projectName
        else
            return path.getParent().toString().replace("/", ".")
    }
}
