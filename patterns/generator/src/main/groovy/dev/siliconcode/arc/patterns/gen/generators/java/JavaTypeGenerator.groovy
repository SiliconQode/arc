/*
 * MIT License
 *
 * Empirilytics Design Pattern Generator
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
package dev.siliconcode.arc.patterns.gen.generators.java

import com.google.common.collect.Sets
import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.patterns.gen.cue.Cue
import dev.siliconcode.arc.patterns.gen.cue.CueManager
import dev.siliconcode.arc.patterns.gen.cue.CueParams
import dev.siliconcode.arc.patterns.gen.generators.TypeGenerator
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class JavaTypeGenerator extends TypeGenerator {

    @Override
    String generate() {
        log.info("Generating Type")
        Type type = (Type) params.type

        String roleName = findRoleName(type)
        Cue cue = CueManager.getInstance().getCurrent()

        String output = ""

        if (roleName && cue.hasCueForRole(roleName, type)) {
            Cue other = (Cue) cue.getCueForRole(roleName, type)
            output = fromCue(other, type)
        } else {
            switch (type.getType()) {
                case Type.CLASS:
                    output = createTemplate("class", type)
                    break
                case Type.ENUM:
                    output = createTemplate("enum", type)
                    break
                case Type.INTERFACE:
                    output = createTemplate("interface", type)
                    break
            }
        }

        log.info("Done generating type")
        output
    }

    private String fromCue(Cue cue, Type type) {
        String kind = ""
        switch(type.getType()) {
            case Type.CLASS:
                kind = "class"
                break
            case Type.INTERFACE:
                kind = "interface"
                break
            case Type.ENUM:
                kind = "enum"
                break
        }

        CueParams params = new CueParams()
        params.setParam("literals", genLiterals(type))
        params.setParam("typedef", createTypeDef(kind, type, type.name))
        params.setParam("fields", genFields(cue, type))
        params.setParam("methods", genMethods(cue, type))
        params.setParam("InstName", type.name)
        params.setParam("ClassComment", typeComment())

        type.refresh()
        Project proj = type.getParentProject()
        proj.refresh()
        cue.compile(type, params, ctx.projRbmlMap[proj.getProjectKey()])
    }

    private String createTemplate(String kind, Type type) {
        String name = type.name

        """\
        /**
        ${typeComment()}
         */
        ${createTypeDef(kind, type, name)} {${getContent(type)}

        }
        """
    }

    private String createTypeDef(String kind, Type type, String name) {
        "${access(type)}${modifiers(type)}$kind ${name}${extendsList(type)}${implementsList(type)}"
    }

    private String getContent(Type type) {
        return """${
            genLiterals(type)
        }${genFields(null, type)}${genMethods(null, type)}"""
    }

    private String typeComment() {
        """ * Generated Class
         *
         * @author Isaac Griffith
         * @version 1.0"""
    }

    private String access(Type type) {
        String access = type.accessibility.toString().toLowerCase().replaceAll(/_/, " ") + " "
        if (access == "default " || access == " ")
            access = ""

        access
    }

    private String modifiers(Type type) {
        String content = ""

        if (type.modifiers) {
            content += type.modifiers.collect { it.name.toLowerCase() }.join(" ")
            content += " "
        }
        if (content.contains("abstract ") && type.getType() == Type.INTERFACE)
            content = content.replace("abstract ", "")

        content
    }

    private String extendsList(Type type) {
        String content = ""
        if (type.getType() == Type.ENUM)
            return content
        if (type.getGeneralizedBy()) {
            content += " extends "
            if (type.getType() == Type.INTERFACE) {
                List<String> names = []
                type.getGeneralizedBy().each {
                    names << it.name
                }
                content += names.join(", ")
            } else
                content += type.getGeneralizedBy().first().name
        }

        content
    }

    private String implementsList(Type type) {
        String content = ""
        if (type.getType() == Type.INTERFACE)
            return content
        if (type.realizes) {
            content += " implements "
            Set<String> set = Sets.newTreeSet()
            set.addAll(type.getRealizes().collect { it.name })
            content += set.join(", ")
        }

        content
    }

    private String genLiterals(Type type) {
        String content = ""

        if (type.getType() == Type.ENUM) {
            if (type.literals)
                content += "\n"
            type.literals.each { Literal l ->
                content += "\n        "
                ctx.literalGen.init(parent: type, literal: l)
                content += ctx.literalGen.generate()
                content += ","
            }
        }

        int index = content.lastIndexOf(",")
        if (index >= 0)
            content = content.substring(0, index) + ";"

        content
    }

    private String genFields(Cue cue, Type type) {
        String content = ""

        if (!type.getFields().isEmpty()) {
            content += "\n"
            type.getFields().each { Field f ->
                content += "\n            "
                ctx.fieldGen.init(field: f, type: type, parentCue: cue, cue: cue?.getCueForRole(ctx.rbmlManager.getRole(f)?.name, f))
                content += ctx.fieldGen.generate()
            }
        }

        content
    }

    protected String genMethods(Cue cue, Type type) {
        String content = ""

        if (type.getType() != Type.INTERFACE) {
            type.fields.each { Field f ->
                Cue childCue = (Cue) cue?.getCueForRole(ctx.rbmlManager.getRole(f)?.name, f)
                if (!childCue) {
                    childCue = (Cue) cue?.getCueForRole(ctx.rbmlManager.getRelName(f), f)
                }

                if (!childCue) {
                    ctx.methodGen.init(field: f, parent: type, parentCue: cue, cue: childCue)
                    content += "\n    "
                    content += ctx.methodGen.generate()
                }
            }
        }

        type.methods.each { Method m ->
            ctx.methodGen.init(method: m, parent: type, parentCue: cue, cue: cue?.getCueForRole(ctx.rbmlManager.getRole(m)?.name, m))
            content += "\n    "
            content += ctx.methodGen.generate()
        }

        content
    }
}
