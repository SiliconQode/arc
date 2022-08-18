package dev.siliconcode.arc.metrics.ckjm.core.utils;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author mjureczk
 * @version 2.5.0
 */
@Log4j2
public class MethodCoupling implements Comparable<MethodCoupling>{
    private String mClassA;
    private String mClassB;
    private String mMethodA;
    private String mMethodB;


    public MethodCoupling( String classA, String methodA, String classB, String methodB) {
        mClassA = classA;
        mMethodA = methodA;
        mClassB = classB;
        mMethodB = methodB;

        if( classA.equals(classB) && methodA.equals(methodB) ){
            log.error("Method "+classA+"."+methodA+" is coupled to itself!", new RuntimeException() );

        }
        else if( classA.equals(classB) ){
            log.error("Coupling within methods in the same class ("+classA+"): "+methodA+" is coupled to "+methodB+"!", new RuntimeException() );
        }

    }


    @Override
    public String toString(){
        return mClassA+"."+mMethodA+" is coupled to "+mClassB+"."+mMethodB;
    }

    public int compareTo(MethodCoupling mc) {
        int res=0;

        res = mClassA.compareTo( mc.getClassB() );
        res += mClassB.compareTo( mc.getClassA() );
        res += mMethodA.compareTo( mc.getMethodB() );
        res += mMethodB.compareTo( mc.getMethodA() );

        if( res == 0 ){
            return res;
        }

        res = mClassA.compareTo( mc.getClassA() );
        res += mClassB.compareTo( mc.getClassB() );
        res += mMethodA.compareTo( mc.getMethodA() );
        res += mMethodB.compareTo( mc.getMethodB() );

        return res;
    }

    /**
     * @return the mClassA
     */
    public String getClassA() {
        return mClassA;
    }

    /**
     * @return the mClassB
     */
    public String getClassB() {
        return mClassB;
    }

    /**
     * @return the mMethodA
     */
    public String getMethodA() {
        return mMethodA;
    }

    /**
     * @return the mMethodB
     */
    public String getMethodB() {
        return mMethodB;
    }



}
