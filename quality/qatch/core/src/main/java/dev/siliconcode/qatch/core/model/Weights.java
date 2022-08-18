package dev.siliconcode.qatch.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * A simple data class for managing weights information
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@XStreamAlias("weights")
public class Weights {

  @XStreamImplicit
  @XStreamAlias("weight")
  private final List<Double> weights;

  /** Constructs a new Weights object */
  public Weights() {
    weights = new ArrayList<>();
  }

  /**
   * Adds the provided value to the weights set
   *
   * @param weight New weight value to add, cannot be null
   */
  public void add(@NonNull Double weight) {
    weights.add(weight);
  }

  /**
   * Adds the provided weight at the provided index
   *
   * @param index Index for the new weight
   * @param weight The weight to add, cannot be null
   */
  public void add(int index, @NonNull Double weight) {
    weights.add(index, weight);
  }

  /**
   * Determines if the weights set contains the given value
   *
   * @param weight Value to check, cannot be null
   * @return true if the weights contain the value, false otherwise
   */
  public boolean contains(@NonNull Double weight) {
    return weights.contains(weight);
  }

  /**
   * Retrieves the weight at the given index, if the index is in range (0, size] where size is the
   * number of weights
   *
   * @param index index of the weight to retrieve
   * @return The value of the weight at the provided index
   */
  public Double get(int index) {
    return weights.get(index);
  }

  /**
   * Checks if the current weight set is empty
   *
   * @return true if there are no weights, false otherwise
   */
  public boolean isEmpty() {
    return weights.isEmpty();
  }

  /**
   * Retrieves an iterator over the set of weights
   *
   * @return Iterator
   */
  public Iterator<Double> iterator() {
    return weights.iterator();
  }

  /**
   * Retrieves a processing stream over the set of weights
   *
   * @return Stream
   */
  public Stream<Double> stream() {
    return weights.stream();
  }

  /**
   * Removes the weight at the given index, if the index is in range (0, size], where size is the
   * current number of weights
   *
   * @param index Index of the weight to remove
   */
  public void remove(int index) {
    weights.remove(index);
  }

  /**
   * Removes the first occurrence of the weight with the given value
   *
   * @param weight Value of the weight to be removed, cannot be null
   */
  public void remove(@NonNull Double weight) {
    weights.remove(weight);
  }

  /**
   * The current number of weights contained
   *
   * @return Number of weights
   */
  public int size() {
    return weights.size();
  }

  /**
   * Retrieves an array representation of the set of weights
   *
   * @return Array of weights
   */
  public Double[] toArray() {
    return weights.toArray(new Double[0]);
  }

  /**
   * Retrieves a string representation of the set of weights
   *
   * @return String representation
   */
  public String toString() {
    return weights.toString();
  }
}
