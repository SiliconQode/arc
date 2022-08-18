package dev.siliconcode.qatch.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InstanceDataTest {

  private InstanceData instanceDataUnderTest;

  @BeforeEach
  void setUp() {
    instanceDataUnderTest = new InstanceData();
    instanceDataUnderTest.tqiEval = 0.0;
  }

  @Test
  void testInit() {
    // Setup
    final CharacteristicSet characteristicSet = new CharacteristicSet();
    final Weights weights = new Weights();
    characteristicSet.setCharacteristics(
        List.of(new Characteristic("name", "standard", "description", weights)));
    final Weights weights1 = new Weights();
    final PropertySet properties = new PropertySet();
    properties.addProperty(new Property("prop1", "desc1", new Metric()));
    properties.addProperty(new Property("prop2", "desc2", new Finding()));
    final QualityModel model =
        new QualityModel("name", properties, characteristicSet, new Tqi(weights1));

    // Run the test
    instanceDataUnderTest.init(model);

    // Verify the results
    assertFalse(instanceDataUnderTest.getCharacteristicEvals().isEmpty());
    assertFalse(instanceDataUnderTest.getPropertyEvals().isEmpty());
    assertFalse(instanceDataUnderTest.getPropertyMeasures().isEmpty());
  }

  @Test
  void testInit_NPE() {
    assertThrows(NullPointerException.class, () -> instanceDataUnderTest.init(null));
  }

  @Test
  void testAddCharacteristicEval() {
    // Setup
    final Weights weights = new Weights();
    final Characteristic characteristic =
        new Characteristic("name", "standard", "description", weights);

    // Run the test
    instanceDataUnderTest.addCharacteristicEval(characteristic, 0.0);

    // Verify the results
  }

  @Test
  void testAddCharacteristicEval_NPE() {
    assertThrows(
        NullPointerException.class, () -> instanceDataUnderTest.addCharacteristicEval(null, 0.0));
  }

  @Test
  void testAddPropertyEval() {
    // Setup
    final Property property = new Property(null);

    // Run the test
    instanceDataUnderTest.addPropertyEval(property, 0.0);

    // Verify the results
  }

  @Test
  void testAddPropertyEval_NPE() {
    assertThrows(
        NullPointerException.class, () -> instanceDataUnderTest.addPropertyEval(null, 0.0));
  }

  @Test
  void testCalculateNormValue() {
    // Setup
    final Measure measure = new Metric("Wmc", "tool");
    instanceDataUnderTest.getMeasureValues().put(measure, 10.0);
    instanceDataUnderTest.getMeasureNorms().put(measure, 100.0);

    // Run the test
    instanceDataUnderTest.calculateNormValue(measure);
    final double result = instanceDataUnderTest.getNormValue(measure);

    // Verify the results
    assertEquals(0.1, result, 0.0001);
  }

  @Test
  void testCalculateNormValue_NPE() {
    assertThrows(NullPointerException.class, () -> instanceDataUnderTest.calculateNormValue(null));
  }

  @Test
  void testGetNormValue() {
    // Setup
    final Measure measure = new Metric("Wmc", "tool");
    instanceDataUnderTest.getMeasureNormValues().put(measure, 10.0);

    // Run the test
    final double result = instanceDataUnderTest.getNormValue(measure);

    // Verify the results
    assertEquals(10, result, 0.0001);
  }

  @Test
  void testGetNormValue_NPE() {
    assertThrows(NullPointerException.class, () -> instanceDataUnderTest.getNormValue(null));
  }
}
