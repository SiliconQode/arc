package dev.siliconcode.arc.metrics.ckjm.core;

/**
 *
 * @author marian
 * @version 2.5.0
 */
public interface ICountingProperities
{
    /** Return true if the measurements should include calls to the Java JDK into account */
    public boolean isJdkIncluded();

    /** Return true if the measurements should include all classes */
    public boolean includeAll();
}
