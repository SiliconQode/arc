package dev.siliconcode.qatch.core.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class PropertyTest {

  @Mock private Measure mockMeasure;

  private Property propertyUnderTest;

  private AutoCloseable mockitoCloseable;

  @BeforeEach
  void setUp() {
    mockitoCloseable = openMocks(this);
    propertyUnderTest = new Property(mockMeasure);
  }

  @AfterEach
  void tearDown() throws Exception {
    mockitoCloseable.close();
  }

  @Test
  void testIsFinding() {
    // Setup
    when(mockMeasure.isFinding()).thenReturn(false);

    // Run the test
    final boolean result = propertyUnderTest.isFinding();

    // Verify the results
    assertFalse(result);
  }

  @Test
  void testGetThreshold() {
    // Setup
    propertyUnderTest.addAll(0.0, 1.0);
    // Run the test
    final double result = propertyUnderTest.getThreshold(0);
    final double result2 = propertyUnderTest.getThreshold(1);

    // Verify the results
    assertEquals(0.0, result, 0.0001);
    assertEquals(1.0, result2, 0.0001);
  }

  @Test
  void testAdd() {
    // Setup
    // Run the test
    propertyUnderTest.add(0.0);

    // Verify the results
    assertEquals(1, propertyUnderTest.thresholdsSize());
  }

  @Test
  void testAdd_NPE() {
    assertThrows(NullPointerException.class, () -> propertyUnderTest.add(null));
  }

  @Test
  void testAddAll() {
    // Setup
    // Run the test
    propertyUnderTest.addAll(0.0, 1.0, 2.0);

    // Verify the results
    assertEquals(3, propertyUnderTest.thresholdsSize());
  }

  @Test
  void testAddAll_NPE() {
    assertThrows(NullPointerException.class, () -> propertyUnderTest.addAll((Double[]) null));
  }

  @Test
  void testClearThresholds() {
    // Setup
    propertyUnderTest.addAll(0.0, 1.0, 2.0);
    // Run the test
    assertNotEquals(0, propertyUnderTest.thresholdsSize());
    propertyUnderTest.clearThresholds();

    // Verify the results
    assertEquals(0, propertyUnderTest.thresholdsSize());
  }

  @Test
  void testThresholdsSize() {
    // Setup
    // Run the test
    final int result = propertyUnderTest.thresholdsSize();

    // Verify the results
    assertEquals(0, result);
  }

  @Test
  void testGettersSetters() {
    propertyUnderTest.setName("new name");
    propertyUnderTest.setDescription("new description");
    propertyUnderTest.setPositive(false);
    propertyUnderTest.setMeasure(new Metric("other", "other"));
    propertyUnderTest.setThresholds(new Thresholds(3));
    propertyUnderTest.setProfile(null);

    assertEquals("new name", propertyUnderTest.getName());
    assertEquals("new description", propertyUnderTest.getDescription());
    assertFalse(propertyUnderTest.isPositive());
    assertNotNull(propertyUnderTest.getMeasure());
    assertNotNull(propertyUnderTest.getThresholds());
    assertNull(propertyUnderTest.getProfile());
  }
}
