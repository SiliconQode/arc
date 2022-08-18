package dev.siliconcode.qatch.core.model;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Data class representing a set of properties
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@XStreamAlias("properties")
public class PropertySet {

  @Getter
  @Setter
  @XStreamImplicit
  @XStreamAlias("property")
  private List<Property> properties;

  /** Constructs a new empty PropertySet */
  public PropertySet() {
    this.properties = Lists.newArrayList();
  }

  /**
   * Adds the provide property to this property set
   *
   * @param p Property to be added, cannot be null
   */
  public void addProperty(@NonNull Property p) {
    this.properties.add(p);
  }

  /**
   * Retrieves the property at the provided index, if the provided index is in range (0, size] where
   * size is the number of current proprties held
   *
   * @param index Index of the property to retrieve
   * @return the retrieved property
   */
  public Property get(int index) {
    return this.properties.get(index);
  }

  /**
   * Checks if the current set of properties is empty
   *
   * @return true if no properties are currently held in the set, false otherwise
   */
  public boolean isEmpty() {
    return properties.isEmpty();
  }

  /**
   * Retrieves an iterator over the properties held
   *
   * @return Iterator
   */
  public Iterator<Property> iterator() {
    return this.properties.iterator();
  }

  /**
   * Retrieves a stream over the properties held
   *
   * @return Stream
   */
  public Stream<Property> stream() {
    return this.properties.stream();
  }

  /**
   * Retrieves the current size of the properties held
   *
   * @return Number of properties
   */
  public int size() {
    return properties.size();
  }

  /**
   * Retrieves an array representation of the properties for efficient processing
   *
   * @return Array Representation
   */
  public Property[] toArray() {
    return properties.toArray(new Property[0]);
  }

  /**
   * Retrieves a string representation of the properties
   *
   * @return String representation
   */
  public String toString() {
    return properties.toString();
  }
}
