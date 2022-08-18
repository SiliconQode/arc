package dev.siliconcode.qatch.service.eval;

import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * A Simple class to extract the pertinent quality results from a Qatch analysis results file.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Slf4j
public class DataExtractor {

  /**
   * Extracts the quality analysis results from the provided path
   *
   * @param path The path to extract the results from, cannot be null and must be a valid path
   * @return A map comprising the results, or an empty Map if the path does not represent a file, or
   *     the file is not readable
   */
  public Map<String, BigDecimal> parseResults(@NonNull String path) {
    if (!validatePath(path)) return Maps.newHashMap();

    Map<String, BigDecimal> values = Maps.newHashMap();
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      final JsonParser parser = Json.createParser(br);
      String key = null;
      String value = null;

      boolean outerCh = false;
      boolean innerCh = false;
      boolean characteristic = false;
      boolean characteristicValue = false;
      String charName = "";
      BigDecimal charVal = null;
      boolean tqi = false;
      BigDecimal tqiVal = null;

      while (parser.hasNext()) {
        final JsonParser.Event event = parser.next();
        switch (event) {
          case KEY_NAME:
            key = parser.getString();
            if (key.equals("characteristics") && !outerCh) outerCh = true;
            else if (key.equals("characteristics") && outerCh) innerCh = true;
            else if (outerCh && innerCh) {
              if (key.equals("name")) {
                characteristic = true;
                characteristicValue = false;
              } else if (key.equals("eval")) {
                characteristic = false;
                characteristicValue = true;
              } else {
                characteristic = false;
                characteristicValue = false;
              }
            } else if (key.equals("tqi")) {
              tqi = true;
            }
            break;
          case VALUE_STRING:
            String str = parser.getString();
            if (characteristic) charName = str;
            break;
          case VALUE_NUMBER:
            BigDecimal number = parser.getBigDecimal();
            if (characteristicValue) values.put(charName, number);
            else if (tqi) values.put("tqi", number);
            break;
        }
      }
      parser.close();
    } catch (IOException ex) {
      log.error("Error: could not read path: " + path);
    }
    return values;
  }

  /**
   * Validates the given path.
   *
   * @param path Path string to validate
   * @return True only if the file exists, is a regular file (not a directory), and is readable.
   */
  private boolean validatePath(String path) {
    Path check = Paths.get(path);
    return Files.exists(check) && Files.isRegularFile(check) && Files.isReadable(check);
  }
}
