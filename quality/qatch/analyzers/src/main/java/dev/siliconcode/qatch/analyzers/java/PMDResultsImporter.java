package dev.siliconcode.qatch.analyzers.java;

import dev.siliconcode.qatch.analyzers.Filename;
import dev.siliconcode.qatch.analyzers.IssuesImporter;
import dev.siliconcode.qatch.core.eval.Issue;
import dev.siliconcode.qatch.core.eval.IssueSet;
import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Responsible for importing all the violations of a certain XML file, containing
 * the results of a static analysis exported by the PMD tool.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class PMDResultsImporter implements IssuesImporter {

  /**
   * This method is used to parse all the issues (i.e. violations) of a single result XML PMD file,
   * that corresponds to a certain property, into a single IssueSet object.
   *
   * @param path The path of the results file to be imported, cannot be null
   */
  @Override
  public IssueSet parseIssues(@NonNull String path) {

    // The IssueSet object used to store all the violations of this property
    IssueSet tempIssues = new IssueSet();

    try {

      // Import the desired xml file with the violations and create the tree representation
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(new File(path));
      Element root = (Element) doc.getRootElement();

      // Create a list of all the "files" evaluated by the PMD
      List<Element> fileList = root.getChildren();

      // Create the IssueSet that will be returned
      IssueSet issues = new IssueSet();

      // Set the property name of the IssueSet

      Filename propName = new Filename(path, File.separatorChar, '.');
      issues.setPropertyName(propName.filename());
      System.out.println("Parsing: " + propName.filename());

      // Create an empty list in order to store temporary the violations of each file
      List<Element> list = null;

      // Iterate through the list of files
      for (Element el : fileList) {

        // Get the violations of the current file
        list = el.getChildren();

        /*
         * Iterate through the list of violation for this certain
         * file and store them in the appropriate objects
         */
        for (Element viol : list) {
          Issue issue =
              new Issue(
                  viol.getAttributeValue("rule"),
                  viol.getAttributeValue("ruleset"),
                  viol.getAttributeValue("package"),
                  viol.getText(),
                  viol.getAttributeValue("externalInfoUrl"),
                  Integer.parseInt(viol.getAttributeValue("priority")),
                  Integer.parseInt(viol.getAttributeValue("beginline")),
                  Integer.parseInt(viol.getAttributeValue("endline")),
                  Integer.parseInt(viol.getAttributeValue("begincolumn")),
                  Integer.parseInt(viol.getAttributeValue("endcolumn")),
                  el.getAttributeValue("name"));

          // Add the issue to the IssueSet
          issues.addIssue(issue);
        }
      }

      tempIssues = issues;

    } catch (JDOMException | IOException e) {
      log.error(e.getMessage());
    }

    // Return the IssueSet
    return tempIssues;
  }

  /**
   * Parses each issue from the issue set of each ruleset, one per file, in the provided path
   *
   * @param path Path to the results from a particular ruleset, cannot e null
   * @return Set of issue sets for each ruleset
   */
  // TODO: Check if we should follow this approach - If not remove this method
  public Vector<IssueSet> parseIssuesPerFile(@NonNull String path) {

    Vector<IssueSet> tempIssues = new Vector<>();

    try {

      // The OS Separator should be defined for Filename class
      // TODO: Find a better way
      char separator;
      if (System.getProperty("os.name").contains("Windows")) {
        separator = '\\';
      } else {
        separator = '/';
      }

      /* Import the desired xml file with the violations and create the tree representation */
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(new File(path));
      Element root = (Element) doc.getRootElement();

      /* Create a list of all the "files" evaluated by the PMD */
      List<Element> fileList = root.getChildren();

      /* Create an empty list in order to store temporary the violation nodes of each file node */
      List<Element> list = null;

      /* Create the IssueSet to store the issues of the current file*/
      // IssueSet issues = new IssueSet();

      /* Create an empty Issue Object */
      // Issue issue = new Issue();

      /* Iterate through the list of files */
      for (Element el : fileList) {
        /* Create the IssueSet to store the issues of the current file*/
        IssueSet issues = new IssueSet();
        /* Save the name and the absolute path of the file */
        Filename fileName = new Filename(el.getAttributeValue("name"), separator, '.');
        issues.setFileName(fileName.filename());
        issues.setFilePath(fileName.path());

        Filename propName = new Filename(path, separator, '.');
        issues.setPropertyName(propName.filename());

        /* Get the violations of the current file */
        list = el.getChildren();

        /* Iterate through the list of violation for this certain file and store them in the appropriate objects */
        for (Element viol : list) {
          Issue issue =
              new Issue(
                  viol.getAttributeValue("rule"),
                  viol.getAttributeValue("ruleset"),
                  viol.getAttributeValue("package"),
                  viol.getText(),
                  viol.getAttributeValue("externalInfoUrl"),
                  Integer.parseInt(viol.getAttributeValue("priority")),
                  Integer.parseInt(viol.getAttributeValue("beginline")),
                  Integer.parseInt(viol.getAttributeValue("endline")),
                  Integer.parseInt(viol.getAttributeValue("begincolumn")),
                  Integer.parseInt(viol.getAttributeValue("endcolumn")),
                  "");

          /* Add the issue to the IssueSet */
          issues.addIssue(issue);
        }
        /* Add the current IssueSet to the IssueSet Vector */
        tempIssues.add(issues);
      }

    } catch (JDOMException | IOException e) {
      log.error(e.getMessage());
    }

    /* Return the IssueSet */
    return tempIssues;
  }
}
