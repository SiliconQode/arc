package dev.siliconcode.qatch.core.model;

import lombok.NonNull;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * An interface necessary for handling objects that have weights attached
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public interface WeightedObject {

  /**
   * @return The weights object associated with this
   */
  Weights getWeights();

  /**
   * Convenience delegator which adds the given weight to the set of weighs
   *
   * @param weight Weight to be added, cannot be null
   */
  void addWeight(@NonNull Double weight);

  /**
   * Retrieves the weight at the given index, if such an index exists, otherwise it throws an
   * IndexOutOfBoundsException
   *
   * @param index Index of the weight
   * @return Weight at that index
   */
  Double get(int index);

  /**
   * Checks if the weights are empty
   *
   * @return true if no weights have been added, false otherwise
   */
  boolean isEmpty();

  /**
   * Constructs and iterator for the weights for iterative processing
   *
   * @return Iterator
   */
  Iterator<Double> iterator();

  /**
   * Constructs a stream for the weights for a more functional approach to iteration
   *
   * @return Stream
   */
  Stream<Double> stream();

  /**
   * Retrieves the current number of weights that have been added
   *
   * @return Number of weights
   */
  int size();

  /**
   * Converts the weights into an array for efficient processing
   *
   * @return Array of weights
   */
  Double[] toArray();
}
