package dev.siliconcode.qatch.core.model;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

class PropertySetTest {

  @Mock private PropertySet mockP;

  private PropertySet propertySetUnderTest;

  private AutoCloseable mockitoCloseable;

  @BeforeEach
  void setUp() {
    mockitoCloseable = openMocks(this);
    propertySetUnderTest = new PropertySet();
  }

  @AfterEach
  void tearDown() throws Exception {
    mockitoCloseable.close();
  }

  @Test
  void testAddProperty() {
    // Setup
    final Property p = new Property(null);

    // Run the test
    propertySetUnderTest.addProperty(p);

    // Verify the results
    assertEquals(1, propertySetUnderTest.size());
  }

  @Test
  void testAddProperty_NPE() {
    assertThrows(NullPointerException.class, () -> propertySetUnderTest.addProperty(null));
  }

  @Test
  void testGet() {
    // Setup
    propertySetUnderTest.addProperty(new Property());
    // Run the test
    final Property result = propertySetUnderTest.get(0);

    // Verify the results
    assertNotNull(result);
  }

  @Test
  void testIsEmpty() {
    // Setup
    // Run the test
    final boolean result = propertySetUnderTest.isEmpty();

    // Verify the results
    assertTrue(result);
    propertySetUnderTest.addProperty(new Property());
    assertFalse(propertySetUnderTest.isEmpty());
  }

  @Test
  void testIterator() {
    // Setup
    propertySetUnderTest.addProperty(new Property());

    // Run the test
    final Iterator<Property> result = propertySetUnderTest.iterator();

    // Verify the results
    assertTrue(result.hasNext());
    assertNotNull(result.next());
  }

  @Test
  void testStream() {
    // Setup
    propertySetUnderTest.addProperty(new Property());
    // Run the test
    final Stream<Property> result = propertySetUnderTest.stream();

    // Verify the results
    assertEquals(1, result.count());
  }

  @Test
  void testSize() {
    // Setup
    // Run the test
    final int result = propertySetUnderTest.size();

    // Verify the results
    assertEquals(0, result);
  }

  @Test
  void testToArray() {
    // Setup
    // Run the test
    final Property[] result = propertySetUnderTest.toArray();

    // Verify the results
    assertEquals(0, result.length);
  }

  @Test
  void testToString() {
    // Setup
    // Run the test
    final String result = propertySetUnderTest.toString();

    // Verify the results
    assertEquals("[]", result);
  }

  @Test
  void testGettersSetters() {
    propertySetUnderTest.setProperties(Lists.newArrayList());

    assertTrue(propertySetUnderTest.getProperties().isEmpty());
  }
}
