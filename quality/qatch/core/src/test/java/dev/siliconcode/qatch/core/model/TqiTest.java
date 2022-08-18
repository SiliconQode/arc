package dev.siliconcode.qatch.core.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TqiTest {

  private Tqi tqiUnderTest;

  @BeforeEach
  void setUp() throws Exception {
    tqiUnderTest = new Tqi(new Weights());
  }

  @AfterEach
  void tearDown() throws Exception {}

  @ParameterizedTest
  @CsvSource({"0,0,0", "0.25,0.25,0.25", "0.5,0.5,0.5", "0.75,0.75,0.75", "1.0,1.0,1.0"})
  void testCalculateTQI(double value1, double value2, double expected) {
    // Setup
    final CharacteristicSet characteristics = new CharacteristicSet();
    final Weights weights = new Weights();
    Characteristic one = new Characteristic("one", "standard", "description", weights);
    Characteristic two = new Characteristic("two", "standard", "description", weights);
    characteristics.setCharacteristics(List.of(one, two));

    final Weights tqiWeights = new Weights();
    tqiWeights.add(0.5);
    tqiWeights.add(0.5);
    final Map<Characteristic, Double> charEvals =
        Map.ofEntries(Map.entry(one, value1), Map.entry(two, value2));
    tqiUnderTest.setWeights(tqiWeights);

    // Run the test
    final double result = tqiUnderTest.calculateTQI(characteristics, charEvals);

    // Verify the results
    assertEquals(expected, result, 0.0001);
  }

  @Test
  void testGettersSetters() {
    tqiUnderTest.setWeights(new Weights());

    assertTrue(tqiUnderTest.getWeights().isEmpty());
  }

  @Test
  void testAddWeight_NPE() {
    assertThrows(NullPointerException.class, () -> tqiUnderTest.addWeight(null));
  }

  @Test
  void testGet() {
    tqiUnderTest.addWeight(0.0);

    assertEquals(0.0, tqiUnderTest.get(0), 0.0001);
  }

  @Test
  void testIterator() {
    tqiUnderTest.addWeight(0.0);

    Iterator<Double> result = tqiUnderTest.iterator();

    assertTrue(result.hasNext());
    assertNotNull(result.next());
  }

  @Test
  void testStream() {
    tqiUnderTest.addWeight(0.0);

    Stream<Double> result = tqiUnderTest.stream();

    assertEquals(1, result.count());
  }

  @Test
  void testSize() {
    tqiUnderTest.addWeight(0.0);

    assertEquals(1, tqiUnderTest.size());
  }

  @Test
  void testToArray() {
    Double[] array = {0.0d};

    tqiUnderTest.addWeight(0.0);

    assertArrayEquals(array, tqiUnderTest.toArray());
  }

  @Test
  void testToString() {
    assertEquals("[]", tqiUnderTest.toString());
  }
}
