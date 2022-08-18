package dev.siliconcode.qatch.calibration.io;

import dev.siliconcode.qatch.core.eval.Project;
import dev.siliconcode.qatch.core.model.Characteristic;
import dev.siliconcode.qatch.core.model.Property;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * A Class that exports the results of an evaluation
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class EvaluationResultsExporter extends AbstractExporter {

  /** Constructs a new exporter, with the given rootName */
  public EvaluationResultsExporter() {
    super("evaluation");
  }

  /**
   * This method is used in order to export the results of the projects' evaluation to XLS format.
   *
   * @param projects Set of benchmark project cannot be null
   * @param path path to which to export, cannot be null or empty
   * @param includeName Include Name?
   * @param prop_norm Normalize properties?
   * @param prop_eval Evaluate properties?
   * @param char_eval Evaluate characteristics?
   * @param tqi Evaluate TQI?
   */
  public void exportPropValuesAndTqiToXls(
      @NonNull List<Project> projects,
      @NonNull String path,
      boolean includeName,
      boolean prop_norm,
      boolean prop_eval,
      boolean char_eval,
      boolean tqi) {

    if (path.isEmpty()) throw new IllegalArgumentException("path cannot be null");

    // Set the path where the xls file will be stored and the name of the xls file
    // TODO: Use RInvoker.R_WORK_DIR instead
    String filename = path;

    /*
     * Step 1: Create the appropriate spreadsheet.
     */

    // Create an empty workbook
    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("Evaluation Results");
    HSSFRow rowhead = sheet.createRow((short) 0);

    // Check if the names of the projects should be included in the XLS file (user defined)
    int start = 0;
    if (includeName) {
      rowhead.createCell(0).setCellValue("Project_Name");
      start = 1;
    }

    // Create the header of the xls file
    if (prop_norm) {
      for (int i = 0; i < projects.get(0).getProperties().size(); i++) {

        // Get the i-th property
        Property p = projects.get(0).getProperties().get(i);

        // Set the name of the i-th column to the name of this Property
        rowhead.createCell(start).setCellValue(p.getName());
        start++;
      }
    }

    // Check if the the properties' quality scores should be included in the xls file
    if (prop_eval) {

      for (int i = 0; i < projects.get(0).getProperties().size(); i++) {

        // Get the i-th property
        Property p = projects.get(0).getProperties().get(i);

        // Set the name of the i-th column to the name of this Property
        rowhead.createCell(start).setCellValue(p.getName() + "_Eval");
        start++;
      }
    }

    // Check if the the characteristics' quality scores should be included in the xls file
    if (char_eval) {

      for (int i = 0; i < projects.get(0).getCharacteristics().size(); i++) {

        // Get the i-th property
        Characteristic c = projects.get(0).getCharacteristics().get(i);

        // Set the name of the i-th column to the name of this Characteristic
        rowhead.createCell(start).setCellValue(c.getName() + "_Eval");
        start++;
      }
    }

    // Check if the the tqi of each project should be included in the xls file
    if (tqi) {
      rowhead.createCell(start).setCellValue("TQI");
    }

    /*
     * Step 2: Fill the spreadsheet with the appropriate values.
     */

    // Fill the xls file with the normalized values of each project's properties
    int index = 0;
    Iterator<Project> iterator = projects.iterator();
    // Iterate through all the projects
    while (iterator.hasNext()) {

      // Check if the project's name should be included in the xls file
      if (!includeName) {
        start = 0;
      } else {
        start = 1;
      }

      // Get the current project
      Project project = iterator.next();

      // Create a new row in the xls file
      index++;
      HSSFRow row = sheet.createRow((short) index);

      // Check if the names of the projects should be included in the XLS file (user defined)
      if (includeName) {
        row.createCell(0).setCellValue(project.getName());
      }

      // Iterate thorough all the properties of the project and store their values into the xls file
      if (prop_norm) {
        for (int i = 0; i < project.getProperties().size(); i++) {

          // Get the current property
          Property p = project.getProperties().get(i);

          // Receive the normalized value of this property
          double normValue = project.normalizeMeasure(p);

          // Store it in the appropriate column of the xls file
          row.createCell(start).setCellValue(normValue);
          start++;
        }
      }

      // Properties Evaluation
      if (prop_eval) {

        for (int i = 0; i < project.getProperties().size(); i++) {

          // Get the current property
          Property p = project.getProperties().get(i);

          // Receive the normalized value of this property
          final double eval = project.evaluateProjectProperty(p);

          // Store it in the appropriate column of the csv file
          row.createCell(start).setCellValue(eval);
          start++;
        }
      }

      // Characteristics Evaluation
      if (char_eval) {

        for (int i = 0; i < project.getCharacteristics().size(); i++) {

          // Receive the normalized value of this property
          double eval = project.evaluateProjectCharacteristic(project.getCharacteristics().get(i));

          // Store it in the appropriate column of the csv file
          row.createCell(start).setCellValue(eval);
          start++;
        }
      }

      // Add the TQI of the project
      if (tqi) {
        row.createCell(start).setCellValue(project.getTqiEval());
      }
    }

    /*
     * Step 3: Save the spreadsheet to the appropriate file
     */
    // Export the XLS file to the desired path
    FileOutputStream fileOut = null;
    try {
      fileOut = new FileOutputStream(filename);
      workbook.write(fileOut);
      fileOut.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  /** {@inheritDoc} */
  @Override
  protected void processContent(@NotNull Object obj, @NotNull Element root) {}

  /** {@inheritDoc} */
  @Override
  protected Element createAttributeData(@NotNull Object obj) {
    return null;
  }
}
