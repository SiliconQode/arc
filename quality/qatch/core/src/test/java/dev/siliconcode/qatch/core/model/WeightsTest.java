package dev.siliconcode.qatch.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WeightsTest {

  private Weights weightsUnderTest;

  @BeforeEach
  void setUp() throws Exception {
    weightsUnderTest = new Weights();
  }

  @Test
  void testAdd1() {
    // Setup
    // Run the test
    weightsUnderTest.add(0.0);

    // Verify the results
    assertFalse(weightsUnderTest.isEmpty());
  }

  @Test
  void testAdd1_NPE() {
    assertThrows(NullPointerException.class, () -> weightsUnderTest.add(null));
  }

  @Test
  void testAdd2() {
    // Setup
    // Run the test
    weightsUnderTest.add(0, 0.0);

    // Verify the results
    assertFalse(weightsUnderTest.isEmpty());
  }

  void testAdd2_NPE() {
    assertThrows(NullPointerException.class, () -> weightsUnderTest.add(0, null));
  }

  @Test
  void testContains() {
    // Setup
    // Run the test
    final boolean result = weightsUnderTest.contains(0.0);

    // Verify the results
    assertFalse(result);
  }

  @Test
  void testGet() {
    // Setup
    weightsUnderTest.add(0.0);

    // Run the test
    final Double result = weightsUnderTest.get(0);

    // Verify the results
    assertEquals(0.0, result, 0.0001);
  }

  @Test
  void testIsEmpty() {
    // Setup
    // Run the test
    final boolean result = weightsUnderTest.isEmpty();

    // Verify the results
    assertTrue(result);
    weightsUnderTest.add(0.0);
    assertFalse(weightsUnderTest.isEmpty());
  }

  @Test
  void testIterator() {
    // Setup
    weightsUnderTest.add(0.0);

    // Run the test
    final Iterator<Double> result = weightsUnderTest.iterator();

    // Verify the results
    assertTrue(result.hasNext());
    assertNotNull(result.next());
  }

  @Test
  void testStream() {
    // Setup
    weightsUnderTest.add(0.0);

    // Run the test
    final Stream<Double> result = weightsUnderTest.stream();

    // Verify the results
    assertEquals(1, result.count());
  }

  @Test
  void testRemove1() {
    // Setup
    weightsUnderTest.add(0.0);

    // Run the test
    assertFalse(weightsUnderTest.isEmpty());
    weightsUnderTest.remove(0);

    // Verify the results
    assertTrue(weightsUnderTest.isEmpty());
  }

  @Test
  void testRemove2() {
    // Setup
    weightsUnderTest.add(0.0);

    // Run the test
    weightsUnderTest.remove(0.0);

    // Verify the results
    assertTrue(weightsUnderTest.isEmpty());
  }

  void testRemove2_NPE() {
    assertThrows(NullPointerException.class, () -> weightsUnderTest.remove(null));
  }

  @Test
  void testSize() {
    // Setup

    // Run the test
    final int result = weightsUnderTest.size();

    // Verify the results
    assertEquals(0, result);
    weightsUnderTest.add(0.0);
    assertEquals(1, weightsUnderTest.size());
  }

  @Test
  void testToArray() {
    // Setup
    weightsUnderTest.add(0.0);

    // Run the test
    final Double[] result = weightsUnderTest.toArray();

    // Verify the results
    assertArrayEquals(new Double[] {0.0}, result);
  }

  @Test
  void testToString() {
    // Setup
    // Run the test
    final String result = weightsUnderTest.toString();

    // Verify the results
    assertEquals("[]", result);
  }
}
