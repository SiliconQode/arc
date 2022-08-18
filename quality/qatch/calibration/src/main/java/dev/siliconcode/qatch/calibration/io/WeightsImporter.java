package dev.siliconcode.qatch.calibration.io;

import dev.siliconcode.qatch.calibration.RInvoker;
import dev.siliconcode.qatch.core.model.Characteristic;
import dev.siliconcode.qatch.core.model.CharacteristicSet;
import dev.siliconcode.qatch.core.model.Tqi;
import com.google.gson.*;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class is responsible for importing the weights, that are calculated by R, into the desired
 * Characteristic and Tqi objects of the quality model.
 *
 * <p>The files containing the values of the weights are searched inside R working directory.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class WeightsImporter {

  /**
   * A method for importing the weights found in the "weights.json" file, which is exported by R
   * analysis.
   *
   * <p>Precondition: This method should be executed only after the invocation of R analysis and
   * supposing that the json file is created.
   *
   * @param tqi Tqi object into which weights are to be imported, cannot be null
   * @param characteristics Characteristics object into which weights are to be imported, cannot be
   *     null
   */
  public void importWeights(@NonNull Tqi tqi, @NonNull CharacteristicSet characteristics) {
    try (BufferedReader br =
        new BufferedReader(new FileReader(RInvoker.R_WORK_DIR + File.separator + "weights.json"))) {
      // Create a BufferedReader in order to load the json file where the weights are stored

      // Create a Gson json Parser
      Gson gson = new Gson();

      // Parse the json into an  object of type Object
      Object obj = gson.fromJson(br, Object.class);
      String s = obj.toString();
      gsonParser(s, tqi, characteristics);

    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * This method implements a custom json parser that is used in order to load the weights of the
   * TQI and the characteristics of the Quality Model and store them in the appropriate objects.
   *
   * <p>The order in which the weights are placed inside the json file doesn't matter because this
   * method searches for the appropriate node. If the node isn't found inside the json file, a
   * NullPointerException is thrown!!
   *
   * <p>TODO: Check the case where there a certain Characteristic cannot be found inside the
   * predefined json file - Avoid NullPointerException
   *
   * @param jsonLine Json to be parsed, cannot be null
   * @param tqi The tqi object to updated, cannot be null
   * @param characteristics The characteristics set to be updated, cannot be null
   */
  public void gsonParser(
      @NonNull String jsonLine, @NonNull Tqi tqi, @NonNull CharacteristicSet characteristics) {
    // Get a JsonElement by using a jsonParser
    JsonElement jelement = JsonParser.parseString(jsonLine);

    // Typically, this json file is an object of arrays
    JsonObject jobject = jelement.getAsJsonObject();

    // Get the TQI weights
    JsonArray tqiArray = jobject.get("TQI").getAsJsonArray();

    for (int i = 0; i < tqiArray.size(); i++) {
      // Get the weights of the Tqi object
      tqi.addWeight(tqiArray.get(i).getAsDouble());
    }

    // Search for each characteristic in the json file and get its weights
    Characteristic characteristic;
    JsonArray charArray;
    for (int i = 0; i < characteristics.size(); i++) {

      // Get the current characteristic
      characteristic = characteristics.get(i);

      // Get the equivalent json array element
      charArray = jobject.get(characteristic.getName()).getAsJsonArray();

      // Get the weights of the current characteristic
      for (int j = 0; j < charArray.size(); j++) {
        characteristic.addWeight(charArray.get(j).getAsDouble());
      }
    }
  }
}
