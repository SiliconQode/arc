package dev.siliconcode.qatch.analyzers.java;

import dev.siliconcode.qatch.analyzers.Aggregator;
import dev.siliconcode.qatch.core.eval.MetricSet;
import dev.siliconcode.qatch.core.eval.Metrics;
import dev.siliconcode.qatch.core.eval.Project;
import dev.siliconcode.qatch.core.model.Metric;
import dev.siliconcode.qatch.core.model.Property;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * Responsible for calculating the value of the properties that are quantified
 * by the CKJM static analysis tool.
 * <p>
 * Typically:
 * <ul>
 * 	<li>It calculates the weighted sum of each metric against all the classes of
 *      the project (class's loc is used as weight)</li>
 *  <li>It keeps only the values of the properties that we are interested in.</li>
 * </ul>
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class CKJMAggregator implements Aggregator {

  /** {@inheritDoc} */
  @Override
  public void aggregate(@NonNull Project project) {
    //Get the MetricSet of the project
    MetricSet metricSet = project.getMetrics();

    //Initialize the appropriate counters
    double totalLoc = 0;
    double[] metr = new double[18];

    //For each Metrics object (i.e. class) do...
    for(int i = 0; i < metricSet.size(); i++){
      //Get the metrics of the i-th class of the Project
      Metrics metrics = metricSet.get(i);

      //Aggregate all the metrics
      double loc = metrics.get("Loc");
      totalLoc += loc;

      metr[0]  += metrics.get("Wmc")  * loc;
      metr[1]  += metrics.get("Dit")  * loc;
      metr[2]  += metrics.get("Noc")  * loc;
      metr[3]  += metrics.get("Cbo")  * loc;
      metr[4]  += metrics.get("Rfc")  * loc;
      metr[5]  += metrics.get("Lcom") * loc;
      metr[6]  += metrics.get("Ca")   * loc;
      metr[7]  += metrics.get("Ce")   * loc;
      metr[8]  += metrics.get("Npm")  * loc;
      metr[9]  += metrics.get("Lcom3")* loc;
      metr[10] += metrics.get("Loc");
      metr[11] += metrics.get("Dam")  * loc;
      metr[12] += metrics.get("Moa")  * loc;
      metr[13] += metrics.get("Mfa")  * loc;
      metr[14] += metrics.get("Cam")  * loc;
      metr[15] += metrics.get("Ic")   * loc;
      metr[16] += metrics.get("Cbm")  * loc;
      metr[17] += metrics.get("Amc")  * loc;
      // TODO: Decide what to do with the Cyclomatic Complexity metric
      // metr[18] += metrics.getCc() * loc;
    }

    // Find the property, set its normalizer to the totalLoc and set its value if it is a CKJM property
    for(int i = 0; i < project.getProperties().size(); i++){
      //Get the current property
      Property property = project.getProperties().get(i);

      //Set the property's field normalizer to the totalLOC of the project
      project.setPropertyMeasureNormalizer(property, totalLoc);

      //Check if this property is quantified by the CKJM tool
      if(property.getMeasure().getTool().equalsIgnoreCase("ckjm")) {
        Metric measure = (Metric) property.getMeasure();

        // Get the index of the metr array that corresponds to this metric
        int index = 0;
        index = findIndex(measure.getMetricName());

        // Set the value of the property to the appropriate value of the metr array
        project.setPropertyMeasureValue(property, metr[index]);
      }
    }
  }

  /**
   * This method is responsible for finding the index of the array
   * that corresponds to the desirable measureName
   *
   * @param measureName Name of the metric to find the index of, cannot be null
   * @return index of the metric with the given name
   */
  public int findIndex(@NonNull String measureName){
    int index = switch (measureName) {
      case "WMC" -> 0;
      case "DIT" -> 1;
      case "NOC" -> 2;
      case "CBO" -> 3;
      case "RFC" -> 4;
      case "LCOM" -> 5;
      case "Ca" -> 6;
      case "Ce" -> 7;
      case "NPM" -> 8;
      case "LCOM3" -> 9;
      case "LOC" -> 10;
      case "DAM" -> 11;
      case "MOA" -> 12;
      case "MFA" -> 13;
      case "CAM" -> 14;
      case "IC" -> 15;
      case "CBM" -> 16;
      case "AMC" -> 17;
      case "CC" -> 18;
      default -> -1;
    };

    if (index == -1)
      log.info("Not a valid name!!");

    return index;
  }
}
