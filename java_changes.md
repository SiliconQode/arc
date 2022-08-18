Java 8
---
Java Optionals
Java Predicates
Java Functors
Lambdas
Collections & Streams

Java 9
---
Java HttpClient
Java Modules
JShell
try-with-resources improvements
Interfaces have private methods
Streams
  - takeWhile
  - dropWhile
  - iterate
Collections Helpers
  - List.of
  - Set.of
  - Map.of
  
Java 10
---
Local Variable type inference: var-keyword

Java 11
---
Strings & Files
  String
    .isBlank()
    .lines();
    .strip();
  Files
    .writeString()
    .createTempFile()
    .readString()
You can run source files without compiling
Type Inference (var) for lambda params
  (var firstname, var lastname) -> firstname + lastname
Other
  - HttpClient finalized
  - No-Op Garbage Colllector
  - FlightRecorder
  - Nashorn JS Engine deprecated

Java 12
---

Java 13
---
New Switch (without fall through) - Preview
  boolean result = switch (status) {
      case SUBSCRIBER -> true;
      case FREE_TRIAL -> false;
      default -> throw new IllegalArgumentException("something is murky");
  };
Multiline Strings - Preview
  """
  blah blah blah
  blah blah blah
  """;
  
Java 14
---
New Switch
   int numLetters = switch (day) {
      case MONDAY, FRIDAY, SUNDAY -> 6;
      case TUESDAY                -> 7;
      default      -> {
         String s = day.toString();
         int result = s.length();
         yield result;
      }
   };
Helpful NullPointerExceptions

Java 15
---
Text-Blocks / Multiline Strings
Nashorn JS Engine Removed
Z Garbage Collector

Java 16
---
Pattern Matching for Instanceof
  - Instead of
     if (obj instanceof String) {
        String s = (String) obj;
        // do work
     }
  - Use this now
     if (obj instnaceof String s) {
        // do work with s now
     }
Unix Domain Sockets
   socket.connect(UnixDomainSocketAddress.of(
        "/var/run/postgresql/.s.PGSQL.5432"));     
Records
  - Instead of
     final class Point {
         public final int x;
         public final int y;
         
         public Point(int x, int y) {
             this.x = x;
             this.y = y;
         }
     }
  - You can do this
     record Point(int x, int y) {}

Pattern Matching for switch - Preview
    public String test(Object obj) {
        return switch(obj) {
            case Integer i -> "An integer";
            case String s -> "A string";
            case Cat c -> "A Cat";
            default -> "I don't know what it is";
        };

    }
Sealed Classes
    public abstract sealed class Shape
        permits Circle, Rectangle, Square {...}
        
    Only classes allowed to subclass Shape are Circle, Rectangle, Square
