package dev.siliconcode.qatch.service;

import java.util.Map;

/**
 * Data class for containing information related to an ErrorResponse when processing calls for the Api documentation
 *
 * @param title Title for the response
 * @param status The status code
 * @param type The type of the error
 * @param defaults The defaults for the response
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public record ErrorResponse(
  String title,
  int status,
  String type,
  Map<String, String> defaults) {
}
