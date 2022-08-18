package dev.siliconcode.qatch.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FindingTest {

  private Finding findingUnderTest;

  @BeforeEach
  void setUp() {
    findingUnderTest = new Finding("rulesetPath", "tool");
  }

  @Test
  void testIsMetric() {
    assertFalse(findingUnderTest.isMetric());
  }

  @Test
  void testIsFinding() {
    assertTrue(findingUnderTest.isFinding());
  }

  @Test
  void testGetterSetter() {
    findingUnderTest.setRulesetPath("new path");

    assertEquals("new path", findingUnderTest.getRulesetPath());
  }
}
