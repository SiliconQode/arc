package dev.siliconcode.qatch.core.eval;

/**
 * This class contains attributes that are at method level
 *
 * <p>Typically, this class represents a method in the field of Quality Evaluation, I.e. it
 * represents a method and contains all its quality attributes that can be used to characterize the
 * quality of the method (e.g LOC, Cyclomatic Compexity etc.)
 *
 * @author Isaac Griffith
 * @version 2.0.0
 *
 * @param methodName Name of the method
 * @param cyclComplexity Cyclomatic Complexity of the evaluated method
 * @param loc Lines of Code of the evaluated method
 */
public record MethodLevelAttributes(String methodName, double cyclComplexity, int loc) {}
