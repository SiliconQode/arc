package dev.siliconcode.qatch.core.eval;

import org.junit.jupiter.api.BeforeEach;

class IssueTest {

  private Issue issueUnderTest;

  @BeforeEach
  void setUp() {
    issueUnderTest =
        new Issue(
            "ruleName",
            "ruleSetName",
            "packageName",
            "description",
            "externalInfoUrl",
            0,
            0,
            0,
            0,
            0,
            "classPath");
  }
}
