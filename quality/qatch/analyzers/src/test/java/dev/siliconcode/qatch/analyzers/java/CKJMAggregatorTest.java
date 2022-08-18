package dev.siliconcode.qatch.analyzers.java;

import dev.siliconcode.qatch.core.eval.Project;
import dev.siliconcode.qatch.core.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CKJMAggregatorTest {

  private CKJMAggregator ckjmAggregatorUnderTest;

  @BeforeEach
  void setUp() {
    ckjmAggregatorUnderTest = new CKJMAggregator();
  }

  @Test
  void testAggregate() {
    // Setup
    final CharacteristicSet characteristicSet = new CharacteristicSet();
    final Weights weights = new Weights();
    characteristicSet.setCharacteristics(
        List.of(new Characteristic("name", "standard", "description", weights)));
    final Weights weights1 = new Weights();
    final Project project =
        new Project(
            "name",
            new QualityModel("name", new PropertySet(), characteristicSet, new Tqi(weights1)));

    // Run the test
    ckjmAggregatorUnderTest.aggregate(project);

    // Verify the results
  }

  @Test
  void testFindIndex() {
    assertEquals(0, ckjmAggregatorUnderTest.findIndex("measureName"));
  }
}
