/**
 * The MIT License (MIT)
 *
 * Empirilytics Arc Framework
 * Copyright (c) 2015-2021 Empirilytics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.siliconcode.arc.experimenter.impl.quality.quamoco;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.graph.Network;
import dev.siliconcode.arc.datamodel.Measure;
import dev.siliconcode.arc.datamodel.MetricRepository;
import dev.siliconcode.arc.experimenter.ArcContext;
import dev.siliconcode.arc.experimenter.FlowPhase;
import dev.siliconcode.arc.experimenter.anot.Phase;
import dev.siliconcode.arc.experimenter.command.SecondaryAnalysisCommand;
import dev.siliconcode.arc.experimenter.impl.metrics.MetricsConstants;
import dev.siliconcode.arc.quality.quamoco.distiller.ModelDistiller;
import dev.siliconcode.arc.quality.quamoco.distiller.ModelManager;
import dev.siliconcode.arc.quality.quamoco.distiller.QuamocoContext;
import dev.siliconcode.arc.quality.quamoco.graph.edge.Edge;
import dev.siliconcode.arc.quality.quamoco.graph.node.*;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
@Phase(FlowPhase.SECONDARY_ANALYSIS)
public class QuamocoCommand extends SecondaryAnalysisCommand {

    ArcContext context;
    Network<Node, Edge> graph;

    public QuamocoCommand() {
        super(QuamocoConstants.QUAMOCO_CMD_NAME);
    }

    @Override
    public void execute(ArcContext context) {
        log.info("Executing Quamoco Analysis");
        // preliminaries
        this.context = context;
        loadConfig();

        QuamocoContext.instance().setProject(context.getProject());
        QuamocoContext.instance().setMetricRepoKey(MetricsConstants.METRICS_REPO_KEY);

        context.open();

        // Build graph
        graph = buildGraph();

        // Connect Values
        //connectValues(); Not needed as these are all normalization measures and handled by the extents classes

        // Connect Issues
        connectFindings();

        // Execute Quamoco
        executeQuamoco();

        // Store Results
        storeResults();

        context.close();

        log.info("Finished Quamoco Analysis");
    }

    private Network<Node, Edge> buildGraph() {
        log.info("Creating Quamoco Processing Graph");
        String baseDir = context.getArcProperty(QuamocoConstants.QM_HOME_PROP_KEY);

        String lang = context.getLanguage();

        String[] qmFiles = getQMFiles(lang.toLowerCase());
        for (int i = 0; i < qmFiles.length; i++) {
            System.out.println("QMFile[" + i + "]: " + qmFiles[i]);
            System.out.println("baseDir: " + baseDir);
            qmFiles[i] = Paths.get(baseDir, qmFiles[i]).toAbsolutePath().toString();
            System.out.println("QMFile[" + i + "]: " + qmFiles[i]);
        }
        ModelDistiller md = new ModelDistiller(new ModelManager());
        md.readInQualityModels(qmFiles);
        md.buildGraph();

        log.info("Quamoco Processing Graph Created");
        return md.getGraph();
    }

//    private void connectValues() {
//        log.info("Connecting Values to Quamoco Processing Graph");
//
//        graph.nodes().forEach(node -> {
//            if (node instanceof ValueNode) {
//                ValueNode vn = (ValueNode) node;
//
//                MetricsRegistrar reg = MetricsRegistrar.instances.get(QuamocoContext.instance().getMetricRepoKey())
//                String handle = reg.getHandle(vn.getMetric());
//                String repo = QuamocoContext.instance().getMetricRepoKey();
//
//                List<Double> values = Measure.getAllClassValues(context.getProject(), repo, handle);
//                values.forEach(vn::addValue);
//            }
//        });
//
//        log.info("Values connected to Quamoco Processing Graph");
//    }

    private void connectFindings() {
        log.info("Connecting Findings to Quamoco Processing Graph");
        graph.nodes().forEach(node -> {
            if (node instanceof FindingNode) {
                FindingNode fn = (FindingNode) node;

                String rule = fn.getRuleName();
                String repo = fn.getToolName();

                List<dev.siliconcode.arc.datamodel.Finding> findings = context.getProject().getFindings(rule);

                findings.forEach(v -> {
                    fn.addFinding(createFinding(v));
                });
            }
        });
        log.info("Findings connected to Quamoco Processing Graph");
    }

    private void executeQuamoco() {
        log.info("Excecuting Quamoco Analysis Engine");
        String root = context.getArcProperty(QuamocoProperties.QUAMOCO_METRICS_ROOT);

        FactorNode rootNode = null;
        for (Node n : graph.nodes()) {
            if (n instanceof FactorNode) {
                if (n.getName().equals(root)) {
                    rootNode = (FactorNode) n;
                    break;
                }
            }
        }

        if (rootNode != null) {
            rootNode.getValue();
        }
        log.info("Quamoco Analysis Engine Finished");
    }

    private void storeResults() {
        log.info("Storing Quamoco Results");
        Map<String, FactorNode> map = Maps.newHashMap();
        List<String> keys = getQualityAspects();

        for (Node n : graph.nodes()) {
            if (n instanceof FactorNode && keys.contains(n.getName())) {
                Measure.of(QuamocoConstants.QUAMOCO_REPO_KEY + ":" + n.getName())
                        .on(context.getProject())
                        .withValue(n.getValue());
            }
        }
    }

    private Finding createFinding(dev.siliconcode.arc.datamodel.Finding finding) {
        log.info("Ref Key: " + finding.getReferences().get(0).getRefKey());
        log.info("Ref Type: " + finding.getReferences().get(0).getType());
        return new ComponentFinding(finding.getReferences().get(0).getReferencedComponent(context.getProject()), finding.getParentRule().getKey(), finding.getParentRule().getName());
    }

    private void loadConfig() {
        log.info("Loading Quamoco Configuration");
        try(FileInputStream fis = new FileInputStream(new File(QuamocoConstants.QUAMOCO_LANG_MODELS_FILE))) {
            Properties props = new Properties();
            props.load(fis);

            props.forEach((key, value) -> context.addArcProperty((String) key, (String) value));
        } catch (Exception e) {
            log.atError().withThrowable(e).log(e.getMessage());
        }
        try(FileInputStream fis = new FileInputStream(new File(QuamocoConstants.QUAMOCO_METRICS_FILE))) {
            Properties props = new Properties();
            props.load(fis);

            props.forEach((key, value) -> context.addArcProperty((String) key, (String) value));
        } catch (Exception e) {
            log.atError().withThrowable(e).log(e.getMessage());
        }
        log.info("Finished Loading Quamoco Configuration");
    }

    private String[] getQMFiles(String lang) {
        String models = context.getArcProperty(String.format(QuamocoProperties.QUAMOCO_LANG_MODELS, lang));
        String[] files = models.split(",");
        for (int i = 0; i < files.length; i++)
            files[i] = files[i] + ".qm";

        return files;
    }

    private List<String> getQualityAspects() {
        MetricRepository repo = MetricRepository.findFirst("repoKey = ?", QuamocoConstants.QUAMOCO_REPO_KEY);
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        repo.getMetrics().forEach(metric -> builder.add(metric.getName()));
        return builder.build();
    }
}
