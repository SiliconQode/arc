package dev.siliconcode.qatch.calibration;

import dev.siliconcode.qatch.core.model.Characteristic;
import dev.siliconcode.qatch.core.model.CharacteristicSet;
import dev.siliconcode.qatch.core.model.PropertySet;
import dev.siliconcode.qatch.core.model.Weights;
import org.junit.jupiter.api.Test;

import java.util.List;

class ComparisonMatricesCreatorTest {

  @Test
  void testGenerateCompMatrices() {
    // Setup
    final CharacteristicSet characteristics = new CharacteristicSet();
    final Weights weights = new Weights();
    characteristics.setCharacteristics(
        List.of(new Characteristic("name", "standard", "description", weights)));

    final PropertySet properties = new PropertySet();

    // Run the test
    ComparisonMatricesCreator.generateCompMatrices(characteristics, properties, false);

    // Verify the results
  }

  @Test
  void testGenerateTQICompMatrix() {
    // Setup
    final CharacteristicSet characteristics = new CharacteristicSet();
    final Weights weights = new Weights();
    characteristics.setCharacteristics(
        List.of(new Characteristic("name", "standard", "description", weights)));

    // Run the test
    ComparisonMatricesCreator.generateTQICompMatrix(characteristics);

    // Verify the results
  }

  @Test
  void testGenerateCharacteristicsCompMatrices() {
    // Setup
    final CharacteristicSet characteristics = new CharacteristicSet();
    final Weights weights = new Weights();
    characteristics.setCharacteristics(
        List.of(new Characteristic("name", "standard", "description", weights)));

    final PropertySet properties = new PropertySet();

    // Run the test
    ComparisonMatricesCreator.generateCharacteristicsCompMatrices(characteristics, properties);

    // Verify the results
  }
}
