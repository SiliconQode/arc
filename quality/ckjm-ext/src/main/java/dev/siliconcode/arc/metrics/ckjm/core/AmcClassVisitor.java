package dev.siliconcode.arc.metrics.ckjm.core;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.apache.bcel.classfile.JavaClass;

/**
 * The metric will be counted correctly only when the WMC and LOC metric have been already counted.
 *
 * @author mjureczk
 * @version 2.5.0
 */
@Log4j2
public class AmcClassVisitor extends AbstractClassVisitor {

    public AmcClassVisitor(IClassMetricsContainer container) {
        super(container);
    }

    @Override
    protected void visitJavaClass_body(JavaClass jc) {
        double numberOfFields = jc.getFields().length;
        double wmc = mClassMetrics.getWmc();
        double loc = mClassMetrics.getLoc();
        double result;

        if (wmc == 0) {
            result = 0;
        } else {
            loc = loc - numberOfFields - wmc;
            if (loc < 0) {
                log.error("The AMC metric probably is being counted before WMC or LOC!", new RuntimeException());
                result = 0;
            } else {
                result = loc / wmc;
            }
        }

        mClassMetrics.setAmc(result);
    }

}
