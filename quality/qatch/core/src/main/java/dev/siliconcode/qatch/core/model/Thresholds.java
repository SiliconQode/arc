package dev.siliconcode.qatch.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Class to manage thresholds for a set of properties
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@XStreamAlias("thresholds")
public class Thresholds {

  @XStreamImplicit
  @XStreamAlias("weight")
  private final List<Double> thresholds;

  /**
   * Constructs a new thresholds object, set with the maximum size provided
   *
   * @param max Maximum number of thresholds
   */
  public Thresholds(int max) {
    thresholds = new ArrayList<>(max);
  }

  /**
   * Adds the given weight to the thresholds
   *
   * @param weight Weight (threshold) to be added, cannot be null
   */
  public void add(@NonNull Double weight) {
    thresholds.add(weight);
  }

  /**
   * Adds the given weight at the provided index to the thresholds
   *
   * @param index Index at which to add the weight
   * @param weight Weight to be added, cannot be null
   */
  public void add(int index, @NonNull Double weight) {
    thresholds.add(index, weight);
  }

  /**
   * Tests whether the given weight is contained in the thresholds
   *
   * @param weight Weight to check, cannot be null
   * @return true if the provided weight is contained, false otherwise
   */
  public boolean contains(@NonNull Double weight) {
    return thresholds.contains(weight);
  }

  /**
   * Retrieves the weight at the given index, will throw an IndexOutOfBoundsException if the index
   * is less than zero or greater than or equal to the size of thresholds
   *
   * @param index Index of the weight to retrieve
   * @return If the index is in bounds, then returns the threshold at that index, otherwise throws
   *     an exception
   */
  public Double get(int index) {
    return thresholds.get(index);
  }

  /**
   * Checks if the thresholds are empty
   *
   * @return true if no thresholds have been added, false otherwise
   */
  public boolean isEmpty() {
    return thresholds.isEmpty();
  }

  /**
   * Retrieves an iterator object allowing iterative processing of the thresholds
   *
   * @return Iterator
   */
  public Iterator<Double> iterator() {
    return thresholds.iterator();
  }

  /**
   * Retrieves a stream object allowsing streaming processing of the thresholds
   *
   * @return Stream
   */
  public Stream<Double> stream() {
    return thresholds.stream();
  }

  /**
   * Removes the weight at the provided index, if such an index exists, otherwise it throws an
   * IndexOutOfBoundsException
   *
   * @param index index of the weight to be removed
   */
  public void remove(int index) {
    thresholds.remove(index);
  }

  /**
   * Removes the first occurrence of the provided weight, if no such weight exists nothing occurs
   *
   * @param weight The weight to be removed, cannot be null
   */
  public void remove(@NonNull Double weight) {
    thresholds.remove(weight);
  }

  /**
   * The number of thresholds currently contained
   *
   * @return Number of thresholds
   */
  public int size() {
    return thresholds.size();
  }

  /**
   * Converts the throshold container to an array object
   *
   * @return Array representation of the thresholds
   */
  public Double[] toArray() {
    return thresholds.toArray(new Double[0]);
  }

  /**
   * Produces a string representation of the thresholds
   *
   * @return Thresholds string
   */
  @Override
  public String toString() {
    return thresholds.toString();
  }

  /** Removes all currently held thresholds */
  public void clear() {
    thresholds.clear();
  }

  /**
   * Adds all provided values to the thresholds in the order provided
   *
   * @param values Thresholds to add, cannot be null
   */
  public void addAll(@NonNull Double... values) {
    thresholds.addAll(List.of(values));
  }
}
