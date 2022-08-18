package dev.siliconcode.qatch.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;

/**
 * This class encapsulates all the appropriate information that describe a Quality Model that can be
 * used for the evaluation of a single project or a benchmark of projects.
 *
 * <p>Typically, it is used in order to load the PropertySet and the CharacteristicSet of the XML
 * file that describes the quality model and assign their values to the project (or projects) that
 * we want to evaluate.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@XStreamAlias("qualityModel")
public class QualityModel {

  /** The name of the QM found in the XML file */
  @XStreamAlias("name")
  @XStreamAsAttribute
  @Getter
  @Setter
  private String name;

  /** The configuration for the calculation of the TQI of a project */
  @XStreamAlias("tqi")
  @Getter
  @Setter
  private Tqi tqi;

  /** The CharacteristicSet containing all the characteristics of the Quality Model */
  @Getter @Setter private CharacteristicSet characteristics;

  /** The PropertySet containing all the properties of the Quality Model */
  @Getter @Setter private PropertySet properties;

  /**
   * Constructs a new unnamed quality model with an empty property set, an empty characteristics
   * set, and an empty tqi
   */
  public QualityModel() {
    this("");
  }

  /**
   * Constructs a new Quality model with the give name, an empty property set, an empty
   * characteristics set, and an empty Tqi
   *
   * @param name Name of the quality model
   */
  public QualityModel(String name) {
    this(name, new PropertySet(), new CharacteristicSet(), new Tqi());
  }

  /**
   * Constructs a new Quality Model with the given name, set of properties, set of characteristics,
   * and tqi
   *
   * @param name Name of the quailty model
   * @param properties quality model properties set
   * @param characteristics quality model characteristics set
   * @param tqi quality model tqi
   */
  public QualityModel(
      String name, PropertySet properties, CharacteristicSet characteristics, Tqi tqi) {
    this.name = name;
    this.properties = properties;
    this.characteristics = characteristics;
    this.tqi = tqi;
  }

  /**
   * Returns a quality model instance for use in the evaulation of a project, such an instance
   * provides the capability to divide project specific information (such as evaluation resuls) from
   * the model itself. This allows multiple projects to share the same model.
   *
   * @return An evaluation instance of this quality model
   */
  public QualityModelInstance getInstance() {
    return new QualityModelInstance(this);
  }
}
