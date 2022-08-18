package dev.siliconcode.qatch.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CharacteristicSetTest {

  private CharacteristicSet characteristicSetUnderTest;

  @BeforeEach
  void setUp() {
    characteristicSetUnderTest = new CharacteristicSet();
  }

  @Test
  void testGet() {
    // Setup
    // Run the test
    characteristicSetUnderTest.getCharacteristics().add(new Characteristic());
    final Characteristic result = characteristicSetUnderTest.get(0);

    // Verify the results
    assertNotNull(result);
  }

  @Test
  void testIsEmpty() {
    // Setup
    // Run the test
    final boolean result = characteristicSetUnderTest.isEmpty();

    // Verify the results
    assertTrue(result);
  }

  @Test
  void testIterator() {
    // Setup
    // Run the test
    characteristicSetUnderTest.getCharacteristics().add(new Characteristic());
    final Iterator<Characteristic> result = characteristicSetUnderTest.iterator();

    // Verify the results
    assertTrue(result.hasNext());
    assertNotNull(result.next());
  }

  @Test
  void testStream() {
    // Setup
    // Run the test
    characteristicSetUnderTest.getCharacteristics().add(new Characteristic());
    final Stream<Characteristic> result = characteristicSetUnderTest.stream();

    // Verify the results
    assertEquals(1, result.count());
  }

  @Test
  void testSize() {
    // Setup
    // Run the test
    int result = characteristicSetUnderTest.size();

    // Verify the results
    assertEquals(0, result);
    characteristicSetUnderTest.getCharacteristics().add(new Characteristic());
    result = characteristicSetUnderTest.size();
    assertEquals(1, result);
  }

  @Test
  void testToArray() {
    // Setup
    characteristicSetUnderTest.getCharacteristics().add(new Characteristic());
    // Run the test
    final Characteristic[] result = characteristicSetUnderTest.toArray();

    // Verify the results
    assertEquals(1, result.length);
  }

  @Test
  void testToString() {
    // Setup
    // Run the test
    final String result = characteristicSetUnderTest.toString();

    // Verify the results
    assertEquals("[]", result);
  }
}
