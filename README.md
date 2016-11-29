# TantrixSevenTilesPuzzleResolver - Java
This is a small application for solving [Tantrix] (http://tantrix.com/) Seven tiles puzzles that I have put together to learn functional programming using Java 8, [Javaslang] (http://www.javaslang.io/) and [Javactic] (https://github.com/javactic/javactic).

Previously I've made a Scala-based implementation that can be found [here] (https://github.com/jorander/TantrixSevenTilesPuzzleResolver).

## Build it
`mvn clean install`

## Run it
`java -jar ./target/TantrixSevenTilesPuzzleResolverJava-*.jar 1 8 7 56 11 17 18`

The number arguments are number identifiers for Tantrix tiles, try other numbers (1-56) to get solutions for different sets of tiles. If you enter non-integers, numbers above 56 (there are only 56 tiles in Tantrix) or more or fewer arguments (the puzzle uses exactly seven different tiles) you get an error message.