package dev.siliconcode.qatch.projectimport;

/**
 * A record representing the quality values of a given submission
 *
 * @param submissionId id of the submission to which these results belong
 * @param maintainability the maintainability value of the submission
 * @param reliability the reliability value of the submission
 * @param security the security value of the submission
 * @param efficiency the efficiency value of the submission
 * @param tqi the total quality value of the submission
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public record Results(String submissionId, double maintainability, double reliability, double security, double efficiency, double tqi) {}
