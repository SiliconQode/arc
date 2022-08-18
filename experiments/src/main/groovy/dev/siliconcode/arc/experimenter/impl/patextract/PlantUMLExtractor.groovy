/*
 * The MIT License (MIT)
 *
 * Empirilytics Arc Framework
 * Copyright (c) 2015-2021 Empirilytics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.siliconcode.arc.experimenter.impl.patextract


import dev.siliconcode.arc.datamodel.Type

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
class PlantUMLExtractor {

    Set<Type> types
    Set<Type> related

    PlantUMLExtractor(Set<Type> types, Set<Type> related) {
        this.types = types
        this.related = related
    }

    String generateClassDiagram() {
        StringBuilder builder = new StringBuilder()

        builder << "@startuml\n"
        types.each {
            String val = plantUmlForType(it, true)
            if (val) builder << "${val}\n"
        }
        related.each {
            String val = plantUmlForType(it, false)
            if (val) builder << "${val}\n"
        }
        generateRelationships(builder)
        builder << "@enduml"
    }

    private void generateRelationships(StringBuilder builder) {
        types.each {first ->
            first.getGeneralizedBy().each { second -> builder << "${first.getFullName()} --|> ${second.getFullName()}\n" }
            first.getGeneralizes().each { second -> builder << "${first.getFullName()} <|-- ${second.getFullName()}\n" }
            first.getAssociatedFrom().each { second -> builder << "${first.getFullName()} <-- ${second.getFullName()}\n" }
            first.getAssociatedTo().each { second -> builder << "${first.getFullName()} --> ${second.getFullName()}\n" }
            first.getComposedFrom().each { second -> builder << "${first.getFullName()} <--* ${second.getFullName()}\n" }
            first.getComposedTo().each { second -> builder << "${first.getFullName()} *--> ${second.getFullName()}\n" }
            first.getAggregatedFrom().each { second -> builder << "${first.getFullName()} <--o ${second.getFullName()}\n" }
            first.getAggregatedTo().each { second -> builder << "${first.getFullName()} o--> ${second.getFullName()}\n" }
            first.getUseFrom().each { second -> builder << "${first.getFullName()} <.. ${second.getFullName()}\n" }
            first.getUseTo().each { second -> builder << "${first.getFullName()} ..> ${second.getFullName()}\n" }
            first.getDependencyFrom().each { second -> builder << "${first.getFullName()} <.. ${second.getFullName()}\n" }
            first.getDependencyTo().each { second -> builder << "${first.getFullName()} ..> ${second.getFullName()}\n" }
            first.getRealizedBy().each { second -> builder << "${first.getFullName()} ..|> ${second.getFullName()}\n" }
            first.getRealizes().each { second -> builder << "${first.getFullName()} <|.. ${second.getFullName()}\n" }
        }
    }

    private String plantUmlForType(Type type, boolean patternType) {
        String style = patternType ? "#aliceblue ##[bold]blue" : ""

        switch (type.getType()) {
            case Type.CLASS:
                if (type.isAbstract()) return "abstract class ${type.getFullName()}  ${style}"
                else return "class ${type.getFullName()}  ${style}"
            case Type.INTERFACE:
                return "interface ${type.getFullName()}  ${style}"
            case Type.ENUM:
                return "enum ${type.getFullName()}  ${style}"
            case Type.ANNOTATION:
                return "annotation ${type.getFullName()}  ${style}"
            default:
                return null
        }
    }
}
