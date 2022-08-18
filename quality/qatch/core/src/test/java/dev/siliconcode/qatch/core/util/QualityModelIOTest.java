package dev.siliconcode.qatch.core.util;

import dev.siliconcode.qatch.core.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QualityModelIOTest {

  @Test
  void testExportModel() throws IOException {
    // Setup
    final CharacteristicSet characteristicSet = new CharacteristicSet();
    final PropertySet propertySet = new PropertySet();
    propertySet.addProperty(new Property("name", "description", new Metric("name", "tool")));
    propertySet.addProperty(new Property("name", "description", new Finding("ruleset", "tool")));
    final Weights weights = new Weights();
    characteristicSet.setCharacteristics(
        List.of(new Characteristic("name", "standard", "description", weights)));
    final Weights weights1 = new Weights();
    final QualityModel qm =
        new QualityModel("name", propertySet, characteristicSet, new Tqi(weights1));

    // Run the test
    QualityModelIO.exportModel(qm, Paths.get("test_data/filename.xml"));

    // Verify the results
    String text = String.join("\n", Files.readAllLines(Paths.get("test_data/filename.xml")));
    assertEquals("""
        <?xml version="1.0" encoding="UTF-8" ?>
                        
        <qualityModel name="name">
          <tqi>
            <weights/>
          </tqi>
          <characteristics>
            <characteristic name="name" standard="standard" description="description">
              <weights/>
            </characteristic>
          </characteristics>
          <properties>
            <property name="name" description="description" positiveImpact="false">
              <measure class="metric" tool="tool" metricName="name"/>
              <thresholds/>
            </property>
            <property name="name" description="description" positiveImpact="false">
              <measure class="finding" tool="tool" rulesetPath="ruleset"/>
              <thresholds/>
            </property>
          </properties>
        </qualityModel>""",
        text);
  }

  @Test
  void testImportModel() {
    // Setup
    // Run the test
    final QualityModel result = QualityModelIO.importModel(Paths.get("test_data/filename.xml"));

    // Verify the results
    assertNotNull(result);
    assertEquals("name", result.getName());
    assertNotNull(result.getProperties());
    assertFalse(result.getProperties().isEmpty());
    assertFalse(result.getCharacteristics().isEmpty());
    assertNotNull(result.getTqi());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        //"test_data/filename_unreadable.xml",
        "test_data/unreadable/filename.xml",
        "test_data/"
      })
  void testImportModel_ThrowsIllegalArgumentException(String path) {
    // Setup
    // Run the test
    assertThrows(IllegalArgumentException.class, () -> QualityModelIO.importModel(Paths.get(path)));
  }

  @Test
  void testImportModel_NullPath() {
    assertThrows(NullPointerException.class, () -> QualityModelIO.importModel(null));
  }
}
