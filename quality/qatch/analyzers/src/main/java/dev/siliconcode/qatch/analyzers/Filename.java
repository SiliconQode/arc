package dev.siliconcode.qatch.analyzers;

/**
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
public class Filename {
  private String fullPath;
  private char pathSeparator, extensionSeparator;

  /**
   * Constructs a new filename representation
   *
   * @param str Path to the file
   * @param sep Separator character
   * @param ext Extension character
   */
  public Filename(String str, char sep, char ext) {
    fullPath = str;
    pathSeparator = sep;
    extensionSeparator = ext;
  }

  /**
   * Gets the extension of the filename
   *
   * @return The filename's extension
   */
  public String extension() {
    int dot = fullPath.lastIndexOf(extensionSeparator);
    return fullPath.substring(dot + 1);
  }

  /**
   * Returns the filename without its extension
   *
   * @return The filename sans extension
   */
  public String filename() {
    int dot = fullPath.lastIndexOf(extensionSeparator);
    int sep = fullPath.lastIndexOf(pathSeparator);
    return fullPath.substring(sep + 1, dot);
  }

  /**
   * The path of the file
   *
   * @return The path
   */
  public String path() {
    int sep = fullPath.lastIndexOf(pathSeparator);
    return fullPath.substring(0, sep);
  }
}
