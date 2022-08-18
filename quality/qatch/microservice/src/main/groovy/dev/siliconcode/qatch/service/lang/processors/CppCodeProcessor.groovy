package dev.siliconcode.qatch.service.lang.processors

import dev.siliconcode.qatch.service.lang.CodeProcessor

import java.nio.file.Path

/**
 * Correctly generates compilable C++ code from the code provided to the service
 *
 * @author Isaac Griffith
 * @version 1.0.0
 */
class CppCodeProcessor implements CodeProcessor {

    @Override
    String processSingleFileProject(String pathString, String content) {
        return null
    }

    @Override
    String getStandardImports() {
        return null
    }

    @Override
    String getPackageOrDefault(Path path) {
        return null
    }
}
