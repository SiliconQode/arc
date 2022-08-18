package dev.siliconcode.qatch.core.util;

import dev.siliconcode.qatch.core.model.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

/**
 * A class for managing the import and export of a quality model to or from XML. This class is a
 * significant improvement over the original approach, and utilizes the XStream library.
 * Additionally, unlike the prior approaches, this will actually construct valid XML. However, we
 * may later wish to add in additional ability to export or import from JSON as well. Since we have
 * setup this to use XStream, that is merely a simple method call away.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class QualityModelIO {

  /**
   * Exports the given quality model to the file specified by the given path
   *
   * @param qm The quality model to export, cannot be null
   * @param path The path to export to, cannot be null
   * @throws IllegalArgumentException if the provided path is unreadable or a directory
   */
  public static void exportModel(@NonNull QualityModel qm, @NonNull Path path) {
    try {
      Files.deleteIfExists(path);
      Files.createFile(path);
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }

    XStream xstream = setupXStream();

    try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(path))) {
      out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      xstream.marshal(qm, new PrettyPrintWriter(out));
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }
  }

  /**
   * Imports a quality model from the provided path
   *
   * @param path The path to the file containing the quality model,
   * @return The quality model constructed from the provide file
   * @throws IllegalArgumentException if the provided path is unreadable or a directory
   */
  public static QualityModel importModel(@NonNull Path path) throws IllegalArgumentException {
    if (!Files.exists(path))
      throw new IllegalArgumentException("path must exist!");
    else if (!Files.isRegularFile(path))
      throw new IllegalArgumentException("path must not be a directory");
    else if (!Files.isReadable(path))
      throw new IllegalArgumentException("path must be readable");

    XStream xstream = setupXStream();

    QualityModel model = new QualityModel();
    try (Reader reader = Files.newBufferedReader(path)) {
      model = (QualityModel) xstream.fromXML(reader);
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }

    return model;
  }

  /**
   * Sets up the XStream object to convert a model into XML
   *
   * @return The XStream processor
   */
  private static XStream setupXStream() {
    XStream xstream = new XStream(new StaxDriver());
    xstream.addPermission(NoTypePermission.NONE);
    xstream.addPermission(NullPermission.NULL);
    xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
    xstream.allowTypeHierarchy(Collection.class);
    xstream.allowTypesByWildcard(new String[] {QualityModel.class.getPackage().getName() + ".*"});

    xstream.processAnnotations(PropertySet.class);
    xstream.processAnnotations(Property.class);
    xstream.processAnnotations(Characteristic.class);
    xstream.processAnnotations(CharacteristicSet.class);
    xstream.processAnnotations(Property.class);
    xstream.processAnnotations(QualityModel.class);
    xstream.processAnnotations(Measure.class);
    xstream.processAnnotations(Finding.class);
    xstream.processAnnotations(Metric.class);
    xstream.processAnnotations(Tqi.class);
    xstream.processAnnotations(Weights.class);
    xstream.addImplicitCollection(
        CharacteristicSet.class, "characteristics", "characteristic", Characteristic.class);
    xstream.addImplicitCollection(PropertySet.class, "properties", "property", Property.class);
    xstream.addImplicitCollection(Weights.class, "weights", "weight", Double.class);
    xstream.addImplicitCollection(Thresholds.class, "thresholds", "threshold", Double.class);
    return xstream;
  }
}
