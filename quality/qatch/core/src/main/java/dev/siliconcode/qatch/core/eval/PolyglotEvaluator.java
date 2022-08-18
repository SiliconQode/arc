package dev.siliconcode.qatch.core.eval;

import dev.siliconcode.qatch.core.model.Characteristic;
import dev.siliconcode.qatch.core.model.Property;
import dev.siliconcode.qatch.core.model.QualityModel;
import dev.siliconcode.qatch.core.model.QualityModelInstance;
import dev.siliconcode.qatch.core.util.QualityModelIO;
import lombok.NonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An evaluator for a multi-lingual, or polyglot, project. The details of this are still being worked out
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class PolyglotEvaluator {

    private Map<String, Double> languagePercentages;
    private Map<Characteristic, Double> charEvals;
    private Map<Property, Double> propEvals;

    /**
     * Evaluates a project using the universal quality model on the given path
     *
     * @param project Project to be evaluated, cannot be null
     * @param universalQmPath Path to univeral quality model, cannot be null
     * @return An instance of the Universal Quality model with the appropriate evaluation values
     */
    public QualityModelInstance evaluateProject(@NonNull Project project, @NonNull Path universalQmPath) {
        // 0. Load Universal Model
        QualityModel universal = QualityModelIO.importModel(universalQmPath);
        // 1. Get List of Quality Model Instances
//        List<QualityModelInstance> instances = project.getQmInstances();
        // 2. Construct a map of <Language, PercentLOC> -> x_i
        initMaps();
        // 3. For each Property in the universal model
//        universal.getProperties().iterator().forEachRemaining((prop) -> {
//            AtomicReference<Double> total = new AtomicReference<>((double) 0);
//            instances.forEach((inst) -> {
//                var val = inst.normalizeMeasure(inst.findProperty(prop.getName()));
//                total.updateAndGet(v -> v + val * languagePercentages.get(inst.getLanguage()));
//            });
//            propEvals.put(prop, total.get());
//        });
        QualityModelInstance inst = universal.getInstance();
//        inst.setNormPropertyValues(propVals);
        inst.calculateTQI();
        return inst;
    }

    private void initMaps() {}
}
