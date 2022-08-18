package dev.siliconcode.qatch.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing a finding type of measure which provides data from issues collected by a
 * particular tool
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@XStreamAlias("finding")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Finding extends Measure {

  /** Path to the rule set */
  @XStreamAlias("rulesetPath")
  @XStreamAsAttribute
  @Getter
  @Setter
  @EqualsAndHashCode.Include
  private String rulesetPath;

  /** Constructs a new Finding Object with a null rulesetPath and null tool */
  public Finding() {
    this(null, null);
  }

  /**
   * Constructs a new Finding object with the given rulesetPath and tool name
   *
   * @param rulesetPath Path to the ruleset used by the named tool
   * @param tool Tool used to evaluate this finding
   */
  public Finding(String rulesetPath, String tool) {
    super(tool);
    this.rulesetPath = rulesetPath;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isMetric() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isFinding() {
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("Finding RuleSet: %s, Tool: %s", getRulesetPath(), getTool());
  }
}
