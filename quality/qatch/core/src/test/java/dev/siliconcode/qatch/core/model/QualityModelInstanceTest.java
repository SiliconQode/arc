package dev.siliconcode.qatch.core.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QualityModelInstanceTest {

  private QualityModel model;

  private QualityModelInstance qualityModelInstanceUnderTest;

  @BeforeEach
  void setUp() throws Exception {
    PropertySet props = new PropertySet();
    props.addProperty(new Property("name", "desc", new Metric("wmc", "tool")));
    model = new QualityModel("name", props, new CharacteristicSet(), new Tqi());
    qualityModelInstanceUnderTest = model.getInstance();
  }

  @AfterEach
  void tearDown() throws Exception {}

  @ParameterizedTest
  @CsvSource({"0.5,0.5,0.75,0.75", "0.5,1.0,1.5,1.5", "0,1,1.5,1.5"})
  void testEvaluateProjectCharacteristics(
      double peval1, double peval2, double expected1, double expected2) {
    // Setup
    Measure measure = new Metric("wmc", "tool");
    Measure measure2 = new Metric("amc", "tool");
    Property prop = new Property(measure);
    Property prop2 = new Property(measure2);
    PropertySet pset = new PropertySet();
    pset.addProperty(prop);
    pset.addProperty(prop2);
    InstanceData data = new InstanceData();
    model.setProperties(pset);
    data.getPropertyEvals().put(prop, peval1);
    data.getPropertyEvals().put(prop2, peval2);
    qualityModelInstanceUnderTest.setData(data);
    final CharacteristicSet characteristicSet = new CharacteristicSet();
    final Weights weights = new Weights();
    weights.add(1.0);
    weights.add(0.5);
    final Weights weights2 = new Weights();
    weights2.add(0.5);
    weights2.add(1.0);
    final Characteristic char1 = new Characteristic("name", "standard", "description", weights);
    final Characteristic char2 = new Characteristic("name2", "standard", "description2", weights2);
    characteristicSet.setCharacteristics(List.of(char1, char2));
    model.setCharacteristics(characteristicSet);

    // Run the test
    qualityModelInstanceUnderTest.evaluateProjectCharacteristics();
    final double results1 = data.getCharacteristicEvals().get(char1);
    final double results2 = data.getCharacteristicEvals().get(char2);

    // Verify the results
    assertEquals(expected1, results1, 0.0001);
    assertEquals(expected2, results2, 0.0001);
  }

  @ParameterizedTest
  @CsvSource({
    "1.0,1.0,0.0,0.0",
    "0.0,0.0,1.0,1.0",
    "0.25,0.25,1.0,1.0",
    "0.5,0.5,0.5,0.5",
    "0.75,0.75,0.0,0.0",
    "0.25,0.75,0.0,0.0",
    "0.75,0.25,1.0,1.0"
  })
  void testEvaluateProjectProperties(
      double norm1, double norm2, double expected1, double expected2) {
    // Setup
    Measure measure = new Metric("wmc", "tool");
    Measure measure2 = new Metric("amc", "tool");
    Property prop1 = new Property(measure);
    prop1.setPositive(true);
    Property prop2 = new Property(measure2);
    prop2.setPositive(false);
    Thresholds thresh1 = new Thresholds(3);
    thresh1.addAll(0.25, 0.5, 0.75);
    prop1.setThresholds(thresh1);
    Thresholds thresh2 = new Thresholds(3);
    thresh2.addAll(0.25, 0.5, 0.75);
    prop2.setThresholds(thresh2);
    PropertySet pset = new PropertySet();
    pset.addProperty(prop1);
    pset.addProperty(prop2);
    InstanceData data = new InstanceData();
    data.getMeasureNormValues().put(measure, norm1);
    data.getMeasureNormValues().put(measure2, norm2);
    model.setProperties(pset);
    qualityModelInstanceUnderTest.setData(data);

    // Run the test
    qualityModelInstanceUnderTest.evaluateProjectProperties();
    final double results1 = data.getPropertyEvals().get(prop1);
    final double results2 = data.getPropertyEvals().get(prop2);

    // Verify the results
    assertEquals(expected1, results1, 0.0001);
    assertEquals(expected2, results2, 0.0001);
  }

  @ParameterizedTest
  @CsvSource(
      value = {
        "true,0.2,0",
        "true,0.25,0",
        "true,0.3,0.1",
        "true,0.5,0.5",
        "true,0.65,0.8",
        "true,0.75,1",
        "true,0.85,1",
        "false,0.2,1",
        "false,0.25,1",
        "false,0.3,0.9",
        "false,0.5,0.5",
        "false,0.65,0.2",
        "false,0.75,0",
        "false,0.85,0"
      })
  void testEvaluatePropertyMeasure(boolean pos, double norm, double expected) {
    // Setup
    Measure measure = new Metric("wmc", "tool");
    Property prop = new Property(measure);
    prop.setPositive(pos);
    Thresholds thresh = new Thresholds(3);
    thresh.addAll(0.25, 0.5, 0.75);
    prop.setThresholds(thresh);
    PropertySet pset = new PropertySet();
    pset.addProperty(prop);
    InstanceData data = new InstanceData();
    data.getMeasureNormValues().put(measure, norm);
    model.setProperties(pset);
    qualityModelInstanceUnderTest.setData(data);

    // Run the test
    final double result = qualityModelInstanceUnderTest.evaluatePropertyMeasure(prop);

    // Verify the results
    assertEquals(expected, result, 0.0001);
  }

  @ParameterizedTest
  @CsvSource({
    "100,100,1000,1000,0.1,0.1",
    "1000,1000,1000,1000,1,1",
    "1000,1000,100,100,10,10",
    "10,10,0,0,NaN,NaN",
    "0,0,10,10,0,0",
  })
  void testNormalizeMeasures(
      double value, double value2, double norm, double norm2, double expected, double expected2) {
    // Setup
    Measure measure = new Metric("wmc", "tool");
    Measure measure2 = new Metric("amc", "tool");
    Property prop = new Property(measure);
    Property prop2 = new Property(measure2);
    PropertySet pset = new PropertySet();
    pset.addProperty(prop);
    pset.addProperty(prop2);
    InstanceData data = new InstanceData();
    data.getMeasureValues().put(measure, value);
    data.getMeasureNorms().put(measure, norm);
    data.getMeasureValues().put(measure2, value2);
    data.getMeasureNorms().put(measure2, norm2);
    model.setProperties(pset);
    qualityModelInstanceUnderTest.setData(data);

    // Run the test
    qualityModelInstanceUnderTest.normalizeMeasures();
    final double result = qualityModelInstanceUnderTest.getData().getNormValue(measure);
    final double result2 = qualityModelInstanceUnderTest.getData().getNormValue(measure2);

    // Verify the results
    assertEquals(expected, result, 0.0001);
    assertEquals(expected2, result2, 0.0001);
  }

  @ParameterizedTest
  @CsvSource(value = {"1.0,0.5,0.5,0.5,0.75", "0.0,0.0,1.0,1.0,0.0"})
  void testCalculateTQI(
      double weight1, double weight2, double eval1, double eval2, double expected) {
    // Setup
    // Configure QualityModel.getTqi(...).
    final Weights weights = new Weights();
    weights.add(weight1);
    weights.add(weight2);
    final Tqi tqi = new Tqi(weights);
    model.setTqi(tqi);

    // Configure QualityModel.getCharacteristics(...).
    final Weights weights1 = new Weights();
    Characteristic ch = new Characteristic("name", "standard", "description", weights1);
    Characteristic ch2 = new Characteristic("name2", "standard", "description", weights1);
    model.getCharacteristics().setCharacteristics(List.of(ch, ch2));

    InstanceData data = qualityModelInstanceUnderTest.getData();
    data.addCharacteristicEval(ch, eval1);
    data.addCharacteristicEval(ch2, eval2);

    // Run the test
    qualityModelInstanceUnderTest.calculateTQI();

    // Verify the results
    assertEquals(expected, qualityModelInstanceUnderTest.getData().tqiEval, 0.0001);
  }

  @Test
  void testGetName() {
    // Setup

    // Run the test
    final String result = qualityModelInstanceUnderTest.getName();

    // Verify the results
    assertEquals("name", result);
  }

  @Test
  void testSetName() {
    // Setup
    // Run the test
    qualityModelInstanceUnderTest.setName("other");

    // Verify the results
    assertEquals("other", model.getName());
  }

  @Test
  void testGetProperties() {
    // Setup
    // Run the test
    final PropertySet result = qualityModelInstanceUnderTest.getProperties();

    // Verify the results
    assertEquals(1, result.size());
  }

  @Test
  void testSetProperties() {
    // Setup
    final PropertySet properties = new PropertySet();

    // Run the test
    qualityModelInstanceUnderTest.setProperties(properties);

    // Verify the results
    assertTrue(qualityModelInstanceUnderTest.getProperties().isEmpty());
  }

  @Test
  void testSetProperties_NPE() {
    assertThrows(
        NullPointerException.class, () -> qualityModelInstanceUnderTest.setProperties(null));
  }

  @Test
  void testGetCharacteristics() {
    // Setup
    // Configure QualityModel.getCharacteristics(...).
    final CharacteristicSet characteristicSet = new CharacteristicSet();
    final Weights weights = new Weights();
    model
        .getCharacteristics()
        .getCharacteristics()
        .addAll(List.of(new Characteristic("name", "standard", "description", weights)));

    // Run the test
    final CharacteristicSet result = qualityModelInstanceUnderTest.getCharacteristics();

    // Verify the results
    assertFalse(qualityModelInstanceUnderTest.getCharacteristics().isEmpty());
  }

  @Test
  void testSetCharacteristics() {
    // Setup
    final CharacteristicSet characteristics = new CharacteristicSet();
    final Weights weights = new Weights();
    characteristics.setCharacteristics(
        List.of(new Characteristic("name", "standard", "description", weights)));

    // Run the test
    qualityModelInstanceUnderTest.setCharacteristics(characteristics);

    // Verify the results
    assertFalse(qualityModelInstanceUnderTest.getCharacteristics().isEmpty());
  }

  @Test
  void testSetCharacteristics_NPE() {
    assertThrows(
        NullPointerException.class, () -> qualityModelInstanceUnderTest.setCharacteristics(null));
  }

  @Test
  void testGetTqi() {
    // Setup
    // Run the test
    final Tqi result = qualityModelInstanceUnderTest.getTqi();

    // Verify the results
    assertNotNull(result);
  }

  @Test
  void testSetTqi() {
    // Setup
    final Weights weights = new Weights();
    weights.add(1.0);
    final Tqi tqi = new Tqi(weights);

    // Run the test
    qualityModelInstanceUnderTest.setTqi(tqi);

    // Verify the results
    assertNotNull(qualityModelInstanceUnderTest.getTqi());
    assertFalse(qualityModelInstanceUnderTest.getTqi().isEmpty());
  }

  @Test
  void testGetTqiEval() {
    // Setup
    // Run the test
    final double result = qualityModelInstanceUnderTest.getTqiEval();

    // Verify the results
    assertEquals(0.0, result, 0.0001);
  }

  @ParameterizedTest
  @ValueSource(doubles = {-1.0, 0.0, 1.0, 10.0, 100.0})
  void testSetPropertyMeasureNormalizer(double value) {
    // Setup
    final Measure meas = new Metric("wmc", "tool");
    final Property prop = new Property(meas);

    // Run the test
    qualityModelInstanceUnderTest.setPropertyMeasureNormalizer(prop, value);

    // Verify the results
    assertEquals(
        value, qualityModelInstanceUnderTest.getData().getMeasureNorms().get(meas), 0.0001);
  }

  @Test
  void testSetPropertyMeasureValue() {
    // Setup
    final Property property = new Property("name", "desc", new Metric("wmc", "tool"));

    // Run the test
    qualityModelInstanceUnderTest.setPropertyMeasureValue(property, 1.0);

    // Verify the results
    assertEquals(1.0, qualityModelInstanceUnderTest.getPropertyMeasureValue(property), 0.0001);
  }

  @Test
  void testGetPropertyMeasureValue_Unknown() {
    // Setup
    final Property property = new Property(new Metric("wmc", "tool"));

    // Run the test
    qualityModelInstanceUnderTest.setPropertyMeasureValue(property, 1.0);

    // Verify the results
    assertEquals(
        0.0, qualityModelInstanceUnderTest.getPropertyMeasureValue(new Property()), 0.0001);
  }

  @Test
  void testSetPropertyMeasureValue_NPE() {
    assertThrows(
        NullPointerException.class,
        () -> qualityModelInstanceUnderTest.setPropertyMeasureValue(null, 0.0));
  }

  @ParameterizedTest
  @CsvSource({
    "100,1000,0.1",
    "1000,1000,1",
    "1000,100,10",
    "10,0,NaN",
    "0,10,0",
  })
  void testNormalizeMeasure(double value, double norm, double expected) {
    // Setup
    Measure measure = new Metric("wmc", "tool");
    Property prop = new Property(measure);
    InstanceData data = new InstanceData();
    data.getMeasureValues().put(measure, value);
    data.getMeasureNorms().put(measure, norm);
    qualityModelInstanceUnderTest.setData(data);

    // Run the test
    final double result = qualityModelInstanceUnderTest.normalizeMeasure(prop);

    // Verify the results
    assertEquals(expected, result, 0.0001);
  }

  @Test
  void testNormalizeMeasure_NPE() {
    assertThrows(
        NullPointerException.class, () -> qualityModelInstanceUnderTest.normalizeMeasure(null));
  }

  @Test
  void testRoundDown4() {
    assertEquals(1.0554, QualityModelInstance.roundDown4(1.05549), 0.0001);
  }

  @ParameterizedTest
  @CsvSource(value = {"0,0.85,0.65,true", "0,0.5,0.5,true", "0,0.5,0.6,false"})
  void testCheckThreshold(int index, double threshold, double norm, boolean expected) {
    // Setup
    Measure measure = mock(Metric.class);
    Property prop = mock(Property.class);
    InstanceData data = mock(InstanceData.class);
    qualityModelInstanceUnderTest.setData(data);
    Mockito.when(prop.getThreshold(index)).thenReturn(threshold);
    Mockito.when(data.getNormValue(measure)).thenReturn(norm);

    // Run the test
    boolean result = qualityModelInstanceUnderTest.checkThreshold(measure, prop, index);

    // Verify the results
    assertEquals(expected, result);
  }

  @ParameterizedTest
  @CsvSource(
      value = {
        "0.25,0.5,0.75,0.2,0",
        "0.25,0.5,0.75,0.25,0",
        "0.25,0.5,0.75,0.3,0.1",
        "0.25,0.5,0.75,0.5,0.5",
        "0.25,0.5,0.75,0.65,0.8",
        "0.25,0.5,0.75,0.75,1",
        "0.25,0.5,0.75,0.85,1"
      })
  void testCalculatePositiveEffect(
      double threshold0, double threshold1, double threshold2, double norm, double expected) {
    // Setup
    Measure measure = mock(Metric.class);
    Property prop = mock(Property.class);
    InstanceData data = mock(InstanceData.class);
    qualityModelInstanceUnderTest.setData(data);
    Mockito.when(prop.getThreshold(0)).thenReturn(threshold0);
    Mockito.when(prop.getThreshold(1)).thenReturn(threshold1);
    Mockito.when(prop.getThreshold(2)).thenReturn(threshold2);
    Mockito.when(data.getNormValue(measure)).thenReturn(norm);

    // Run the test
    double results = qualityModelInstanceUnderTest.calculatePositiveEffect(measure, prop);

    // Verify the results
    assertEquals(expected, results, 0.0001);
  }

  @ParameterizedTest
  @CsvSource(
      value = {
        "0.25,0.5,0.75,0.2,1",
        "0.25,0.5,0.75,0.25,1",
        "0.25,0.5,0.75,0.3,0.9",
        "0.25,0.5,0.75,0.5,0.5",
        "0.25,0.5,0.75,0.65,0.2",
        "0.25,0.5,0.75,0.75,0",
        "0.25,0.5,0.75,0.85,0"
      })
  void testCalculateNegativeEffect(
      double threshold0, double threshold1, double threshold2, double norm, double expected) {
    // Setup
    Measure measure = mock(Metric.class);
    Property prop = mock(Property.class);
    InstanceData data = mock(InstanceData.class);
    qualityModelInstanceUnderTest.setData(data);
    when(prop.getThreshold(0)).thenReturn(threshold0);
    when(prop.getThreshold(1)).thenReturn(threshold1);
    when(prop.getThreshold(2)).thenReturn(threshold2);
    when(data.getNormValue(measure)).thenReturn(norm);

    // Run the test
    double results = qualityModelInstanceUnderTest.calculateNegativeEffect(measure, prop);

    // Verify the results
    assertEquals(expected, results, 0.0001);
  }

  @Test
  void testGettersSetters() {
    QualityModel newModel = new QualityModel("new name");

    assertEquals(model, qualityModelInstanceUnderTest.getParent());

    qualityModelInstanceUnderTest.setParent(newModel);

    assertEquals(newModel, qualityModelInstanceUnderTest.getParent());
  }
}
