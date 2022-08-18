package dev.siliconcode.arc.metrics.ckjm.core;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

/**
 * ClassVisitor, that counts lines of code in a class.
 * @author marian
 * @version 2.5.0
 */
public class LocClassVisitor extends AbstractClassVisitor {



    public LocClassVisitor( IClassMetricsContainer classMap){
        super(classMap);
    }


    @Override
    protected void visitJavaClass_body(JavaClass jc) {
        String t;

        mClassMetrics.addLoc( jc.getFields().length );
        Method[] methods=jc.getMethods();
        mClassMetrics.addLoc( methods.length );

        for( Method m : methods ){
            m.accept( this );
        }
    }

    @Override
    public void visitMethod(Method meth){
        MethodGen mg = new MethodGen(meth, mClassName, mPoolGen);
        InstructionList il = mg.getInstructionList();

        if( il != null ){
            int instr = il.getLength();
            mClassMetrics.addLoc(instr);
        }
    }
}
