package dev.siliconcode.qatch.core.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class represents a set of characteristics that are used for the evaluation of a project.
 *
 * <p>Typically, it is a Vector of Characteristic objects. It is used in order to load all the
 * information for the characteristics found in the Quality Model's XML file.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@XStreamAlias("characteristics")
public class CharacteristicSet {

  /** The set of the quality model's characteristics */
  @Getter
  @Setter
  @XStreamImplicit
  @XStreamAlias("characteristic")
  private List<Characteristic> characteristics;

  /** Constructs a new empty CharacteristicsSets */
  public CharacteristicSet() {
    this.characteristics = new ArrayList<>();
  }

  /**
   * Retrieves the characteristic at the given index, if the index is within range (0, size] where
   * size is the number of characteristics
   *
   * @param index Index of the characteristic
   * @return Characteristic at index
   */
  public Characteristic get(int index) {
    return characteristics.get(index);
  }

  /**
   * Checks if the set of characteristics is currently empty
   *
   * @return true if empty, false otherwise
   */
  public boolean isEmpty() {
    return characteristics.isEmpty();
  }

  /**
   * Retrieves an iterator over the set of characteristics
   *
   * @return Iterator
   */
  public Iterator<Characteristic> iterator() {
    return characteristics.iterator();
  }

  /**
   * Retrieves a stream over the set of characteristics
   *
   * @return Stream
   */
  public Stream<Characteristic> stream() {
    return characteristics.stream();
  }

  /**
   * Retrieves the current size of the set of charactersitics
   *
   * @return Size of characteristics
   */
  public int size() {
    return characteristics.size();
  }

  /**
   * Retrieves an array representation of the characteristics (for efficient processing)
   *
   * @return Array representation
   */
  public Characteristic[] toArray() {
    return characteristics.toArray(new Characteristic[0]);
  }

  /**
   * Retrieves a string representation of the characteristics
   *
   * @return String representation
   */
  public String toString() {
    return characteristics.toString();
  }
}
