package dev.siliconcode.qatch.calibration.io;

import dev.siliconcode.qatch.core.model.WeightedObject;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Base class for various exporters
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public abstract class AbstractExporter {

  /** name of the root element for xml output */
  protected String rootName;

  /**
   * Constructs a new exporter, with the given rootName
   *
   * @param rootName The name of the root element, cannot be null
   */
  public AbstractExporter(@NonNull String rootName) {
    this.rootName = rootName;
  }

  /**
   * This method exports the JDOM Tree representation of the Tqi object created by the
   * createJDOMRepresentation() method into an XML file to the desired path.
   *
   * <p>Basically:
   *
   * <p>1. It calls the createJDOMRepresentation() in order to receive the DOM tree representation
   * of the desired Tqi object. 2. Stores the DOM tree representation into an XML file in to the
   * desired path.
   *
   * @param obj Object to export, cannot be null
   * @param xmlPath Path to the xml file to export the data to, cannot be null or empty
   */
  public void exportToXML(@NonNull Object obj, @NonNull String xmlPath) {
    if (xmlPath.isEmpty()) throw new IllegalArgumentException("XML Path cannot be null");

    try {
      // Call the method to get the root element of this document
      Element root = createJDOMRepresentation(obj);

      // Create an XML Outputter
      XMLOutputter outputter = new XMLOutputter();

      // Set the format of the outputted XML File
      Format format = Format.getPrettyFormat();
      outputter.setFormat(format);

      // Output the XML File to standard output and the desired file
      FileWriter filew = new FileWriter(xmlPath);
      outputter.output(root, filew);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * This method returns the DOM Tree representation of a weighted node of the Quality Model's XML
   * file.
   *
   * <p>This method is used for the construction of the Quality Model's XML file, together with the
   * DOM Tree representation of the Properties and the Characteristics of the Quality Model.
   *
   * @param obj The object containing the weights, cannot be null
   * @return An Element object that correspond to the DOM Tree representation of the weighted
   *     object.
   */
  protected Element createJDOMRepresentation(@NonNull Object obj) {
    // Create an empty "root" element
    Element root = new Element(rootName);
    root.setName(rootName);

    // Create a node (element) containing the weights of the tqi object
    processContent(obj, root);

    // Return the "root" element
    return root;
  }

  /**
   * Processes content of the provided object, and then adds this content to the provided root element
   *
   * @param obj Object to process
   * @param root Root element to add the content data to
   */
  protected abstract void processContent(@NonNull Object obj, @NonNull Element root);

  /**
   * Constructs the weight data for the provided object
   *
   * @param obj object
   * @return The element representing the weight data
   */
  public Element createWeightData(Object obj) {
    if (obj instanceof WeightedObject weightedObject) {
      Element weights = new Element("weights");
      for (int i = 0; i < weightedObject.getWeights().size(); i++) {

        // Create a weight Element
        Element t = new Element("weight");

        // Set the appropriate value of the weight
        t.setText(String.valueOf(weightedObject.getWeights().get(i)));

        // Attach the current weight element to the element named "weights"
        weights.addContent(t);
      }
      return weights;
    }

    return null;
  }

  /**
   * This method exports the desired Tqi object into JSON format to the desired path.
   *
   * @param obj The object to export, cannot be null
   * @param jsonPath path of the json file to export the data to, cannot be null or empty
   */
  public void exportToJSON(@NonNull Object obj, @NonNull String jsonPath) {
    if (jsonPath.isEmpty()) throw new IllegalArgumentException("JSON Path cannot be null");

    // Create a Gson json parser
    Gson gson = new GsonBuilder()
            .serializeSpecialFloatingPointValues()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    // Parse your obj into a json String representation
    String json = gson.toJson(obj);

    try {
      FileWriter writer2 = new FileWriter(jsonPath);
      writer2.write(json);
      writer2.close();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * Constructs attribute data for the provided object
   * @param obj Object, cannot be null
   * @return Element representing the attribute data
   */
  protected abstract Element createAttributeData(@NonNull Object obj);
}
