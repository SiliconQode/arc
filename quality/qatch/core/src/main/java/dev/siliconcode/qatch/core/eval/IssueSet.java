package dev.siliconcode.qatch.core.eval;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

/**
 * A class that represents a set of violations of a certain file (i.e. class) or a whole project in
 * general.
 *
 * <p>For the benchmarking we do not pay attention to each class separately, but to the total number
 * of the violations for the whole project.
 *
 * <p>Typically it is a vector of issues. An IssueSet holds all the violations (i.e. issues) of a
 * certain Property (i.e Rule Set) of the total Project.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
public class IssueSet {

  @Getter @Setter private String propertyName;
  /** An IssueSet is just a Vector of Issue objects */
  @Getter @Setter private List<Issue> issues;

  @Getter @Setter private String fileName;
  @Getter @Setter private String filePath;

  /** Constructs a new empty IssueSet */
  public IssueSet() {
    issues = Lists.newArrayList();
  }

  /**
   * Adds the provided issue to the issue set
   *
   * @param issue Issue to add, cannot be null
   */
  public void addIssue(@NonNull Issue issue) {
    issues.add(issue);
  }

  /**
   * Retrieves the issue at the provide index
   *
   * @param index Index of issue, must be in range (0, size] where size is the current number of
   *     issues
   * @return Issue at the provided index
   */
  public Issue get(int index) {
    return issues.get(index);
  }

  /**
   * Tests if the current issue set is empty
   *
   * @return true if no issues present, false otherwise
   */
  public boolean isEmpty() {
    return issues.isEmpty();
  }

  /**
   * Creates an iterator over the current issues in the set.
   *
   * @return Iterator
   */
  public Iterator<Issue> iterator() {
    return issues.iterator();
  }

  /**
   * Creates a Stream over the current issues in the set.
   *
   * @return Stream
   */
  public Stream<Issue> stream() {
    return issues.stream();
  }

  /**
   * @return Current number of issues
   */
  public int size() {
    return issues.size();
  }

  /**
   * @return Array representation of the issues
   */
  public Issue[] toArray() {
    return issues.toArray(new Issue[0]);
  }

  /**
   * @return String representation of the issues
   */
  public String toString() {
    return issues.toString();
  }
}
