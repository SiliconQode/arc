package dev.siliconcode.qatch.core.eval;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class IssueSetTest {

  private IssueSet issueSetUnderTest;

  @BeforeEach
  void setUp() {
    issueSetUnderTest = new IssueSet();
  }

  @Test
  void testAddIssue() {
    // Setup
    final Issue issue =
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

    // Run the test
    issueSetUnderTest.addIssue(issue);

    assertFalse(issueSetUnderTest.isEmpty());
  }

  @Test
  void testAddIssue_NPE() {
    assertThrows(NullPointerException.class, () -> issueSetUnderTest.addIssue(null));
  }

  @Test
  void testGet() {
    // Setup
    final Issue expectedResult =
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
    issueSetUnderTest.addIssue(expectedResult);

    // Run the test
    final Issue result = issueSetUnderTest.get(0);

    // Verify the results
    assertEquals(expectedResult, result);
  }

  @Test
  void testIsEmpty() {
    // Setup
    // Run the test
    final boolean result = issueSetUnderTest.isEmpty();

    // Verify the results
    assertTrue(result);
    issueSetUnderTest.addIssue(new Issue(
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
                    "classPath"));
    assertFalse(issueSetUnderTest.isEmpty());
  }

  @Test
  void testIterator() {
    // Setup
    issueSetUnderTest.addIssue(new Issue(
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
                    "classPath"));
    // Run the test
    final Iterator<Issue> result = issueSetUnderTest.iterator();

    // Verify the results
    assertTrue(result.hasNext());
    assertNotNull(result.next());
  }

  @Test
  void testStream() {
    // Setup
    issueSetUnderTest.addIssue(new Issue(
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
            "classPath"));

    // Run the test
    final Stream<Issue> result = issueSetUnderTest.stream();

    // Verify the results
    assertEquals(1, result.count());
  }

  @Test
  void testSize() {
    // Setup
    issueSetUnderTest.addIssue(new Issue(
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
            "classPath"));

    // Run the test
    final int result = issueSetUnderTest.size();

    // Verify the results
    assertEquals(1, result);
  }

  @Test
  void testToArray() {
    // Setup
    final Issue[] expectedResult =
        new Issue[] {
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
              "classPath")
        };
    issueSetUnderTest.addIssue(expectedResult[0]);

    // Run the test
    final Issue[] result = issueSetUnderTest.toArray();

    // Verify the results
    assertArrayEquals(expectedResult, result);
  }

  @Test
  void testToString() {
    // Setup
    // Run the test
    final String result = issueSetUnderTest.toString();

    // Verify the results
    assertEquals("[]", result);
  }
}
