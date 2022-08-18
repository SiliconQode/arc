package dev.siliconcode.arc.metrics.ckjm.core;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import com.google.common.collect.Sets;

/**
 *
 * @author mjureczk
 * @version 2.5.0
 */
public class CamClassVisitor extends AbstractClassVisitor{

    final private String mThis = "this";

    public CamClassVisitor(IClassMetricsContainer container) {
        super(container);
    }

    private Set<String> getArgsTypes(Method m, JavaClass jc, ConstantPoolGen poolGen){
        Set<String> result = Sets.newHashSet();
        if( ignore(m) ){
            return result;
        }
        if( !m.isStatic() ){
            result.add(mThis);
        }
        MethodGen mg = new MethodGen(m, jc.getClassName(), poolGen);
        Type[] args = mg.getArgumentTypes();
        for( Type t : args ){
            result.add( t.getSignature() );
        }
        return result;
    }

    protected ClassMetrics getClassMetrics(JavaClass jc){
        return mMetricsContainer.getMetrics(jc.getClassName());
    }

    /** Ignores static initilizers. */
    private boolean ignore(Method m) {
        return m.getName().compareTo("<clinit>") == 0;
    }

    @Override
    protected void visitJavaClass_body(JavaClass jc) {
        ConstantPoolGen poolGen = new ConstantPoolGen(jc.getConstantPool());
        Set<String> types = Sets.newHashSet();
        Method[] methods = jc.getMethods();
        double numerator=0;
        double denominator=0;

        for( Method m : methods ){ //collect data about method arguments
            types.addAll(getArgsTypes(m, jc, poolGen));
        }

        for( Method m : methods ){ //count the metric
            if( !ignore(m) ){
                numerator += getArgsTypes(m, jc, poolGen).size();
                denominator += types.size();
            }
        }

        if( denominator == 0 ){
            denominator = 1;
        }
        getClassMetrics(jc).setCam(numerator/denominator);
    }
}
