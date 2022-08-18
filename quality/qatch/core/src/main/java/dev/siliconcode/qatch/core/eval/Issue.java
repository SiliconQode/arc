package dev.siliconcode.qatch.core.eval;

/**
 * A class that represents an issue (i.e. a violation of a rule).
 *
 * <p>Typically, it represents a &lt;violation&gt; entry of a PMD result XML file.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 *
 * @param ruleName Name of the rule which produces this issue
 * @param ruleSetName Name of the containing rule set
 * @param packageName Name of the package
 * @param description Description of this issue
 * @param externalInfoUrl URL for external information regarding this issue
 * @param priority Priority level of this issue
 * @param beginLine Start line of where this issue occurred
 * @param endLine End line of where this issue occurred
 * @param beginCol Start col of start line where this issue occurred
 * @param endCol End col of End line of where this issue occurred
 * @param classPath The path to the project's class that this violation belongs to
 */
public record Issue(String ruleName,
                    String ruleSetName,
                    String packageName,
                    String description,
                    String externalInfoUrl,
                    int priority,
                    int beginLine,
                    int endLine,
                    int beginCol,
                    int endCol,
                    String classPath) {}
