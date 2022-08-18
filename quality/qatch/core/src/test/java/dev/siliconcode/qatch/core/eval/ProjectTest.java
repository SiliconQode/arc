package dev.siliconcode.qatch.core.eval;

import dev.siliconcode.qatch.core.model.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

  private QualityModel model;

  private Project projectUnderTest;

  @BeforeEach
  void setUp() {
    model = new QualityModel("name");
    projectUnderTest = new Project("name", model);
  }

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  void testGetProperties() {
    // Setup
    // Run the test
    final PropertySet result = projectUnderTest.getProperties();

    // Verify the results
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testSetProperties() {
    // Setup
    final PropertySet properties = new PropertySet();

    // Run the test
    projectUnderTest.setProperties(properties);

    // Verify the results
    assertEquals(properties, projectUnderTest.getProperties());
  }

  @Test
  void testSetProperties_NPE() {
    assertThrows(NullPointerException.class, () -> projectUnderTest.setProperties(null));
  }

  @Test
  void testGetCharacteristics() {
    // Setup
    // Run the test
    final CharacteristicSet result = projectUnderTest.getCharacteristics();

    // Verify the results
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testSetCharacteristics() {
    // Setup
    final CharacteristicSet characteristics = new CharacteristicSet();
    final Weights weights = new Weights();
    characteristics.setCharacteristics(
        List.of(new Characteristic("name", "standard", "description", weights)));

    // Run the test
    projectUnderTest.setCharacteristics(characteristics);

    // Verify the results
    assertEquals(characteristics, projectUnderTest.getCharacteristics());
  }

  @Test
  void testSetCharacteristics_NPE() {
    assertThrows(NullPointerException.class, () -> projectUnderTest.setCharacteristics(null));
  }

  @Test
  void testGetTqi() {
    // Setup
    // Run the test
    final Tqi result = projectUnderTest.getTqi();

    // Verify the results
    assertNotNull(result);
  }

  @Test
  void testSetTqi() {
    // Setup
    final Weights weights = new Weights();
    final Tqi tqi = new Tqi(weights);

    // Run the test
    projectUnderTest.setTqi(tqi);

    // Verify the results
    assertEquals(tqi, projectUnderTest.getTqi());
  }

  @Test
  void testSetTqi_NPE() {
    assertThrows(NullPointerException.class, () -> projectUnderTest.setTqi(null));
  }

  @Test
  void testAddIssueSet() {
    // Setup
    final IssueSet issueSet = new IssueSet();
    issueSet.setPropertyName("propertyName");
    issueSet.setIssues(
        List.of(
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
                "classPath")));
    issueSet.setFileName("fileName");
    issueSet.setFilePath("filePath");

    // Run the test
    projectUnderTest.addIssueSet(issueSet);

    // Verify the results
    assertNotNull(projectUnderTest.getIssues());
    assertEquals(1, projectUnderTest.size());
  }

  @Test
  void testIsEmpty() {
    // Setup

    // Run the test
    final boolean result = projectUnderTest.isEmpty();

    // Verify the results
    assertTrue(result);
  }

  @Test
  void testIssueSetIterator() {
    // Setup
    final IssueSet issueSet = new IssueSet();
    issueSet.setPropertyName("propertyName");
    issueSet.setIssues(
        List.of(
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
                "classPath")));
    issueSet.setFileName("fileName");
    issueSet.setFilePath("filePath");
    projectUnderTest.addIssueSet(issueSet);

    // Run the test
    final Iterator<IssueSet> result = projectUnderTest.issueSetIterator();

    // Verify the results
    assertTrue(result.hasNext());
    assertNotNull(result.next());
  }

  @Test
  void testIssuesSetStream() {
    // Setup
    final IssueSet issueSet = new IssueSet();
    issueSet.setPropertyName("propertyName");
    issueSet.setIssues(
        List.of(
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
                "classPath")));
    issueSet.setFileName("fileName");
    issueSet.setFilePath("filePath");
    projectUnderTest.addIssueSet(issueSet);

    // Run the test
    final Stream<IssueSet> result = projectUnderTest.issuesSetStream();

    // Verify the results
    assertEquals(1, result.count());
  }

  @Test
  void testSize() {
    // Setup
    final IssueSet issueSet = new IssueSet();
    issueSet.setPropertyName("propertyName");
    issueSet.setIssues(
        List.of(
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
                "classPath")));
    issueSet.setFileName("fileName");
    issueSet.setFilePath("filePath");
    projectUnderTest.addIssueSet(issueSet);

    // Run the test
    final int result = projectUnderTest.size();

    // Verify the results
    assertEquals(1, result);
  }

  @Test
  void testToArray() {
    // Setup
    final IssueSet issueSet = new IssueSet();
    issueSet.setPropertyName("propertyName");
    issueSet.setIssues(
        List.of(
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
                "classPath")));
    issueSet.setFileName("fileName");
    issueSet.setFilePath("filePath");
    projectUnderTest.addIssueSet(issueSet);
    IssueSet[] expected = {issueSet};

    // Run the test
    final IssueSet[] result = projectUnderTest.toArray();

    // Verify the results
    assertArrayEquals(expected, result);
  }

  @Test
  void testToString() {
    // Setup
    // Run the test
    final String result = projectUnderTest.toString();

    // Verify the results
    assertEquals("[]", result);
  }

  @Test
  void testClearIssuesAndMetrics() {
    // Setup
    MetricSet set = new MetricSet();
    set.addMetrics(new Metrics(""));
    List<IssueSet> list = Lists.newArrayList();
    list.add(new IssueSet());
    projectUnderTest.setMetrics(set);
    projectUnderTest.setIssues(list);

    // Run the test
    assertFalse(projectUnderTest.getMetrics().isEmpty());
    assertFalse(projectUnderTest.getIssues().isEmpty());
    projectUnderTest.clearIssuesAndMetrics();

    // Verify the results
    assertTrue(projectUnderTest.getIssues().isEmpty());
    assertTrue(projectUnderTest.getMetrics().isEmpty());
  }

  @Test
  void testEvaluateProjectProperty() {
    // Setup
    Property prop = new Property("name", "desc", new Metric("wmc", "tool"));
    QualityModelInstance inst = mock(QualityModelInstance.class);
    QualityModel model = mock(QualityModel.class);
    when(inst.evaluateProjectProperty(prop)).thenReturn(1.0);
    when(model.getCharacteristics()).thenReturn(new CharacteristicSet());
    when(model.getProperties()).thenReturn(new PropertySet());
    when(model.getInstance()).thenReturn(inst);

    projectUnderTest.setModel(inst);

    // Run the test
    double result = projectUnderTest.evaluateProjectProperty(prop);

    // Verify the results
    assertEquals(1.0, result, 0.0001);
  }

  @Test
  void testEvaluateProjectProperty_NPE() {
    assertThrows(NullPointerException.class, () -> projectUnderTest.evaluateProjectProperty(null));
  }

  @Test
  void testEvaluateProjectCharacteristic() {
    // Setup
    Characteristic ch = new Characteristic("name", "standard", "desc");
    QualityModelInstance inst = mock(QualityModelInstance.class);
    QualityModel model = mock(QualityModel.class);
    when(inst.evaluateProjectCharacteristic(ch)).thenReturn(1.0);
    when(model.getCharacteristics()).thenReturn(new CharacteristicSet());
    when(model.getProperties()).thenReturn(new PropertySet());
    when(model.getInstance()).thenReturn(inst);

    projectUnderTest.setModel(inst);

    // Run the test
    double result = projectUnderTest.evaluateProjectCharacteristic(ch);

    // Verify the results
    assertEquals(1.0, result, 0.0001);
  }

  @Test
  void testEvaluateProjectCharacteristic_NPE() {
    assertThrows(NullPointerException.class, () -> projectUnderTest.evaluateProjectCharacteristic(null));
  }

  @Test
  void testGetTqiEval() {
    // Setup
    // Run the test
    final double result = projectUnderTest.getTqiEval();

    // Verify the results
    assertEquals(0.0, result, 0.0001);
  }

  @Test
  void testSetPropertyMeasureNormalizer_NPE() {
    assertThrows(
        NullPointerException.class, () -> projectUnderTest.setPropertyMeasureNormalizer(null, 0.0));
  }

  @Test
  void testSetPropertyMeasureValue_NPE() {
    assertThrows(
        NullPointerException.class, () -> projectUnderTest.setPropertyMeasureValue(null, 0.0));
  }

  @Test
  void testSort() {
    // Setup
    Project proj1 = mock(Project.class);
    when(proj1.getTqiEval()).thenReturn(1.0);
    Project proj2 = mock(Project.class);
    when(proj2.getTqiEval()).thenReturn(0.0);

    final List<Project> projects = Lists.newArrayList(proj1, proj2);

    // Run the test
    Project.sort("eval", projects);

    // Verify the results
    assertEquals(proj2, projects.get(0));
    assertEquals(proj1, projects.get(1));
  }

  @Test
  void testSort2() {
    // Setup
    Project proj1 = mock(Project.class);
    Project proj2 = mock(Project.class);

    final List<Project> projects = Lists.newArrayList(proj1, proj2);

    // Run the test
    Project.sort("other", projects);

    // Verify the results
    assertEquals(proj1, projects.get(0));
    assertEquals(proj2, projects.get(1));
  }

  @Test
  void testSort_NPE() {
    assertThrows(NullPointerException.class, () -> Project.sort("", null));
  }

  @Test
  void testSort_NPE2() {
    assertThrows(NullPointerException.class, () -> Project.sort(null, Lists.newArrayList()));
  }

  @Test
  void testAttributesGettersAndSetters() {
    // Setup
    MetricSet metrics = mock(MetricSet.class);
    projectUnderTest.setName("new name");
    projectUnderTest.setPath("new path");
    projectUnderTest.setIssues(Lists.newArrayList());
    projectUnderTest.setMetrics(metrics);

    assertEquals("new name", projectUnderTest.getName());
    assertEquals("new path", projectUnderTest.getPath());
    assertTrue(projectUnderTest.getIssues().isEmpty());
    assertEquals(metrics, projectUnderTest.getMetrics());
  }

  @Test
  void testOtherConstructors() {
    // Run the test
    projectUnderTest = new Project(model);

    // Verify the Results
    assertNotNull(projectUnderTest);
    assertNull(projectUnderTest.getName());
  }
}
