package dev.siliconcode.qatch.service.lang.processors

import dev.siliconcode.qatch.service.lang.CodeProcessor

import java.nio.file.Path

/**
 * Correctly generates interprettable JavaScript code from the code passed to the service
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
class JsCodeProcessor implements CodeProcessor {

    /** {@inheritDoc} */
    @Override
    String processSingleFileProject(String pathString, String content) {
        return null
    }

    /** {@inheritDoc} */
    @Override
    String getStandardImports() {
        return null
    }

    /** {@inheritDoc} */
    @Override
    String getPackageOrDefault(Path path) {
        return null
    }
}
