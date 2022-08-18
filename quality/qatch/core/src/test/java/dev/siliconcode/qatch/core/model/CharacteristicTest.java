package dev.siliconcode.qatch.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CharacteristicTest {

  @Mock private Weights mockWeights;

  private Characteristic characteristicUnderTest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    characteristicUnderTest = new Characteristic("name", "standard", "description", mockWeights);
  }

  @Test
  void testEvaluate() {
    // Setup
    final PropertySet properties = new PropertySet();
    final Map<Property, Double> propEvals = Map.ofEntries(Map.entry(new Property(null), 0.0));
    when(mockWeights.size()).thenReturn(0);
    when(mockWeights.get(0)).thenReturn(0.0);

    // Run the test
    final double result = characteristicUnderTest.evaluate(properties, propEvals);

    // Verify the results
    assertEquals(0.0, result, 0.0001);
  }

  @Test
  void testEvaluate_data() {
    // Setup
    final PropertySet properties = new PropertySet();
    properties.addProperty(new Property("prop1", "desc1", null));
    properties.addProperty(new Property("prop2", "desc2", null));
    final Map<Property, Double> propEvals =
        Map.ofEntries(Map.entry(properties.get(0), .25), Map.entry(properties.get(1), 0.5));
    when(mockWeights.size()).thenReturn(2);
    when(mockWeights.get(0)).thenReturn(1.0);
    when(mockWeights.get(1)).thenReturn(1.0);

    // Run the test
    final double result = characteristicUnderTest.evaluate(properties, propEvals);

    // Verify the results
    assertEquals(.75, result, 0.0001);
  }

  @Test
  void testEvaluate_NPE() {
    // Setup
    final Map<Property, Double> propEvals = Map.ofEntries(Map.entry(new Property(null), 0.0));
    when(mockWeights.size()).thenReturn(0);
    when(mockWeights.get(0)).thenReturn(0.0);

    // Run the test
    assertThrows(
        NullPointerException.class, () -> characteristicUnderTest.evaluate(null, propEvals));
  }

  @Test
  void testEvaluate_NPE2() {
    // Setup
    final PropertySet properties = new PropertySet();

    // Run the test
    assertThrows(
        NullPointerException.class, () -> characteristicUnderTest.evaluate(properties, null));
  }

  @Test
  void testGettersSetters() {
    characteristicUnderTest.setName("other name");
    characteristicUnderTest.setDescription("other desc");
    characteristicUnderTest.setStandard("other standard");

    assertEquals("other name", characteristicUnderTest.getName());
    assertEquals("other desc", characteristicUnderTest.getDescription());
    assertEquals("other standard", characteristicUnderTest.getStandard());
  }
}
