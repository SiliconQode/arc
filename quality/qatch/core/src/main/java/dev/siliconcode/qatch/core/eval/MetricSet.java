package dev.siliconcode.qatch.core.eval;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class represents a set of metrics found in a result.xml file of CKJM.
 *
 * <p>Typically, it is a Vector of Metrics objects. Each object is a bundle of metrics for a
 * specific class of the project.
 *
 * <p>It is the equivalent of IssueSet. However, here we pay attention on the level of the analysis.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class MetricSet {
  /*
   * There is no need of setting a Name. It is a collection of
   * all the metrics available  for each class of a project.
   */
  @Getter @Setter List<Metrics> metricSet;

  /** Constructs a new empty MetricSet object */
  public MetricSet() {
    metricSet = Lists.newArrayList();
  }

  /**
   * Adds the provided metrics to the metric set
   *
   * @param metrics Merics to be added, cannot be null
   */
  public void addMetrics(@NonNull Metrics metrics) {
    metricSet.add(metrics);
  }

  /**
   * Retrieves the Metrics at the provided index
   *
   * @param index index of metrics, must be in range (0, size], where size is the current number of
   *     held metrics
   * @return Metrics object at index
   */
  public Metrics get(int index) {
    return metricSet.get(index);
  }

  /**
   * Tests if the set of Metrics is empty
   *
   * @return true if empty, false otherwise
   */
  public boolean isEmpty() {
    return metricSet.isEmpty();
  }

  /**
   * Creates an iterator over the set of metrics objects
   *
   * @return Iterator
   */
  public Iterator<Metrics> iterator() {
    return metricSet.iterator();
  }

  /**
   * Creates a Stream over the set of metrics objects
   *
   * @return Stream
   */
  public Stream<Metrics> stream() {
    return metricSet.stream();
  }

  /**
   * @return Current number of metrics
   */
  public int size() {
    return metricSet.size();
  }

  /**
   * @return Array representation of Metrics
   */
  public Metrics[] toArray() {
    return metricSet.toArray(new Metrics[0]);
  }

  /**
   * @return String representation of metrics
   */
  public String toString() {
    return metricSet.toString();
  }

  /** Removes all items from the metricSet */
  public void clear() {
    metricSet.clear();
  }
}
