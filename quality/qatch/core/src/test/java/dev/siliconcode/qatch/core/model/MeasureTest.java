package dev.siliconcode.qatch.core.model;

import org.junit.jupiter.api.BeforeEach;

class MeasureTest {

  private Measure measureUnderTest;

  @BeforeEach
  void setUp() {
    measureUnderTest =
        new Measure("tool") {
          @Override
          public boolean isMetric() {
            return false;
          }

          @Override
          public boolean isFinding() {
            return false;
          }
        };
  }
}
