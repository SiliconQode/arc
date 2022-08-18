package dev.siliconcode.qatch.service.lang.processors

import dev.siliconcode.qatch.service.lang.CodeProcessor

import java.nio.file.Path

/**
 * Correctly generates compilable C# code from code passed to the service
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
class CSharpCodeProcessor implements CodeProcessor {

    /** {@inheritdoc} */
    @Override
    String processSingleFileProject(String pathString, String content) {
        return null
    }

    /** {@inheritdoc} */
    @Override
    String getStandardImports() {
        return null
    }

    /** {@inheritdoc} */
    @Override
    String getPackageOrDefault(Path path) {
        return null
    }
}
