package dev.siliconcode.qatch.projectimport;

/**
 * A record representing a candidate code submission
 *
 * @param id the unique identifier for this submission
 * @param name the candidates name
 * @param score the submissions score
 * @param lang the language the submission was written in
 * @param source the actual source code of the submission
 * @param role the role for which the candidate applied
 * @param rejectionReason the reason why the candidate was rejected
 * @param stage the current stage the candidate is on
 * @param goodCode a boolean representing the quality of the candidates code
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public record Submission(String id,
                         String name,
                         int score,
                         String lang,
                         String source,
                         String role,
                         String rejectionReason,
                         String stage,
                         boolean goodCode) {
}
