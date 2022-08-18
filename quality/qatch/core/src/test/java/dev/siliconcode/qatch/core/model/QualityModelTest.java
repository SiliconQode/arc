package dev.siliconcode.qatch.core.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.MockitoAnnotations.openMocks;

class QualityModelTest {

  private QualityModel qualityModelUnderTest;

  private AutoCloseable mockitoCloseable;

  @BeforeEach
  void setUp() {
    mockitoCloseable = openMocks(this);
    qualityModelUnderTest =
        new QualityModel("name", new PropertySet(), new CharacteristicSet(), new Tqi());
  }

  @AfterEach
  void tearDown() throws Exception {
    mockitoCloseable.close();
  }

  @Test
  void testGetInstance() {
    // Setup
    assertNotNull(qualityModelUnderTest);

    // Run the test
    final QualityModelInstance result = qualityModelUnderTest.getInstance();

    // Verify the results
    assertNotNull(result);
  }
}
