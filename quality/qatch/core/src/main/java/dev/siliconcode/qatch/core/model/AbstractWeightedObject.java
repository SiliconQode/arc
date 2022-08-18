package dev.siliconcode.qatch.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Base class for the shared functionality of objects that use weights in their calculations
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public abstract class AbstractWeightedObject implements WeightedObject {

  /** The vector holding the weights for the evaluation of the characteristic. */
  @XStreamAlias("weights")
  @Getter
  @Setter
  protected Weights weights;

  /** Constructs a new AbstractWeightedObject with an empty set of weights */
  public AbstractWeightedObject() {
    this(new Weights());
  }

  /**
   * Constructs a new AbstractWeightedObject with the given set of weights
   *
   * @param weights Weights for this object
   */
  public AbstractWeightedObject(Weights weights) {
    this.weights = weights;
  }

  /** {@inheritDoc} */
  @Override
  public void addWeight(@NonNull Double weight) {
    weights.add(weight);
  }

  /** {@inheritDoc} */
  @Override
  public Double get(int index) {
    return weights.get(index);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isEmpty() {
    return weights.isEmpty();
  }

  /** {@inheritDoc} */
  @Override
  public Iterator<Double> iterator() {
    return weights.iterator();
  }

  /** {@inheritDoc} */
  @Override
  public Stream<Double> stream() {
    return weights.stream();
  }

  /** {@inheritDoc} */
  @Override
  public int size() {
    return weights.size();
  }

  /** {@inheritDoc} */
  @Override
  public Double[] toArray() {
    return weights.toArray();
  }

  /**
   * Returns a String representation of the weights of the characteristic
   *
   * @return String representation of the weights
   */
  @Override
  public String toString() {
    return weights.toString();
  }
}
