package dev.siliconcode.qatch.core.eval;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MethodLevelAttributesTest {

  private MethodLevelAttributes methodLevelAttributesUnderTest;

  @BeforeEach
  void setUp() {
    methodLevelAttributesUnderTest = new MethodLevelAttributes("methodName", 0, 0);
  }

  @Test
  public void testConstruct() {
    MethodLevelAttributes attr = new MethodLevelAttributes("method", 1, 100);

    assertEquals("method", attr.methodName());
    assertEquals(1, attr.cyclComplexity());
    assertEquals(100, attr.loc());
  }

}
