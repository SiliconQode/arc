package dev.siliconcode.qatch.analyzers.java;

import dev.siliconcode.qatch.analyzers.MetricsImporter;
import dev.siliconcode.qatch.core.eval.MethodLevelAttributes;
import dev.siliconcode.qatch.core.eval.MetricSet;
import dev.siliconcode.qatch.core.eval.Metrics;
import lombok.extern.log4j.Log4j2;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * This class is responsible for importing all the metrics that the ckjm tool calculates for a
 * certain project, into a MetricSet object.
 *
 * <p>Each object of the MetricSet contains all the metrics of a certain class of the whole project.
 * Typically, it contains all the data found between the tags &lt;class&gt; and &lt;/class&gt; of
 * the ckjmResults XML file.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class CKJMResultsImporter implements MetricsImporter {

  /**
   * The method that is used to parse the calculated metrics from the XML file containing the
   * results of the CKJM tool for the desired project.
   *
   * @param path The exact path to the ckjmResults.xml file, cannot be null
   */
  @Override
  public MetricSet parseMetrics(@NotNull String path) {

    // A MetricSet object for storing the calculated metrics
    MetricSet metricSet = new MetricSet();

    try {
      // Import the desired xml file with the ckjm results and create the tree representation
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(new File(path));
      Element root = (Element) doc.getRootElement();

      // Create a list of all the classes evaluated by the CKJM
      List<Element> classList = root.getChildren();

      // Iterate through the classes and parse their metrics
      for (Element el : classList) {
        List<Element> list = el.getChildren();
        Metrics metrics = new Metrics(list.get(0).getText());

        metrics.set("Wmc", Integer.parseInt(list.get(1).getText()));
        metrics.set("Dit", Integer.parseInt(list.get(2).getText()));
        metrics.set("Noc", Integer.parseInt(list.get(3).getText()));
        metrics.set("Cbo", Integer.parseInt(list.get(4).getText()));
        metrics.set("Rfc", Integer.parseInt(list.get(5).getText()));
        metrics.set("Lcom", Integer.parseInt(list.get(6).getText()));
        metrics.set("Ca", Integer.parseInt(list.get(7).getText()));
        metrics.set("Ce", Integer.parseInt(list.get(8).getText()));
        metrics.set("Npm", Integer.parseInt(list.get(9).getText()));
        metrics.set("Lcom3", Double.parseDouble(list.get(10).getText()));
        metrics.set("Loc", Integer.parseInt(list.get(11).getText()));
        metrics.set("Dam", Double.parseDouble(list.get(12).getText()));
        metrics.set("Moa", Integer.parseInt(list.get(13).getText()));
        metrics.set("Mfa", Double.parseDouble(list.get(14).getText()));
        metrics.set("Cam", Double.parseDouble(list.get(15).getText()));
        metrics.set("Ic", Integer.parseInt(list.get(16).getText()));
        metrics.set("Cbm", Integer.parseInt(list.get(17).getText()));
        metrics.set("Amc", Double.parseDouble(list.get(18).getText()));

        // Store the methods as well
        List<Element> methodList = list.get(19).getChildren();

        // Iterate through each method found in the XML file
        Vector<MethodLevelAttributes> methods = new Vector<>();
        for (Element methodNode : methodList) {

          // Create an object for each method
          MethodLevelAttributes method =
              new MethodLevelAttributes(
                  methodNode.getAttributeValue("name"), Integer.parseInt(methodNode.getText()), 0);
          methods.add(method);
        }

        // Add the metrics to the MetricSet
        metrics.setMethods(methods);
        metricSet.addMetrics(metrics);
      }

    } catch (JDOMException | IOException e) {
      log.error(e.getMessage());
    }

    // Return the MetricSet object containing the metrics of each class of the project under
    // evaluation
    return metricSet;
  }

  /** {@inheritDoc} */
  @Override
  public String getFileName() {
    return "ckjm.xml";
  }
}
