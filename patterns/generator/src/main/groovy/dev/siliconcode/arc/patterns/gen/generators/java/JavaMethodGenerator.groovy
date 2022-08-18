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

import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.patterns.gen.cue.Cue
import dev.siliconcode.arc.patterns.gen.cue.MethodCue
import dev.siliconcode.arc.patterns.gen.generators.MethodGenerator
import dev.siliconcode.arc.patterns.gen.generators.pb.RBML2DataModelManager
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class JavaMethodGenerator extends MethodGenerator {

    JavaMethodGenerator() {
    }

    @Override
    String generate() {
        log.info("Generating Method")
        if (!params.method && !params.field)
            throw new IllegalArgumentException("Method and Field cannot be null")

        Method method = (Method) params.method
        Field field = (Field) params.field

        String roleName
        if (method) {
            method.refresh()
            Project proj = method.getParentProject()
            proj.refresh()
            roleName = ctx.projRbmlMap[proj.getProjectKey()]?.getRole(method)?.name
        }
        else {
            field.refresh()
            Project proj = field.getParentProject()
            proj.refresh()
            roleName = ctx.projRbmlMap[proj.getProjectKey()]?.getRole(field)?.name
        }

        Cue cue = (Cue) params.cue

        if (method && roleName && params.parentCue && ((Cue) params.parentCue).hasCueForRole(roleName, method)) {
            return ""
        }

        String output = ""

        if (method)
            output += generate(method)
        else if (field)
            output += generate(field)

        log.info("Done generating method")
        output
    }

    String generate(Method method) {
        String output

        switch (method) {
            case Constructor:
                output = createConstructor(method)
                break
            case Destructor:
                output = createDestructor()
                break
            default:
                output = createMethod(method)
                break
        }

        output
    }

    String generate(Field field) {
        if (!field)
            throw new IllegalArgumentException("Field cannot be null")

        if ((ctx.rbmlManager.getRelName(field) || ctx.rbmlManager.getRole(field)) &&
                !((params.parentCue as Cue)?.hasCueForRole(ctx.rbmlManager.getRole(field)?.name, field) ||
                (params.parentCue as Cue)?.hasCueForRole(ctx.rbmlManager.getRelName(field), field))) {
            return ""
        }

        String line = """\
            ${createAccessor(field)}"""

        if (!field.hasModifier("FINAL")) {
            line += """
                ${createMutator(field)}"""
        }
        line
    }

    private String createMethod(Method method) {
        String name = method.name
        String body = getMethodBody(method)
        String paramList = getParamList(method)
        String modifiers = getModifiers(method)
        modifiers = modifiers ? modifiers + " " : ""
        String type = method.type.typeName
        String comment = getComment(method)
        String access = method.accessibility.toString().toLowerCase()
        access = access != "" ? access + " " : ""

        String excepts = ""
        if (method.exceptions) {
            excepts = " throws "
            def exceptList = []
            method.exceptions.each {
                exceptList << it.getTypeRef().typeName
            }
            excepts += exceptList.join(", ")
        }

        if (method.getParentTypes()) {
            if (method.getParentTypes().first().getType() == Type.INTERFACE) {
                """
            $comment
            $type $name($paramList)$excepts;"""
            } else {
                if (body != ";")
                    excepts += " "
                """
            $comment
            $access$modifiers$type $name($paramList)$excepts$body"""
            }
        }  else {
            if (body != ";")
                excepts += " "
            """
            $comment
            $access$modifiers$type $name($paramList)$excepts$body"""
        }
    }

    private String getComment(Method method) {
        String params = ""
        String ret = ""
        String excepts = ""
        method.params.each {
            params += """
             * @param ${it.name}"""
        }
        if (method.type.typeName != "void") {
            ret = """
             * @return"""
        }
        method.exceptions.each {
            excepts += """
             * @throws ${it.getTypeRef().typeName}"""
        }
        String comment = """/**
             *$params${ret ?: ""}$excepts
             */"""
//        if (method.isOverriding()) {
//            comment += """
//            @Override"""
//        }
        comment
    }

    private String getParamList(Method method) {
        def list = []
        if (method.params) {
            method.params.each { p ->
                def modList = []
                p.modifiers.each {
                    modList << it.name.toLowerCase()
                }
                String item = ""
                if (modList)
                    item = modList.join(" ") + " "
                list << "$item${p.type.typeName} ${p.name}"
            }
            list.join(", ")
        } else {
            ""
        }
    }

    private String getModifiers(Method method) {
        def list = []
        if (method.modifiers) {
            method.modifiers.each { m ->
                list << m.name.toLowerCase()
            }
            list.join(" ")
        } else {
            ""
        }
    }

    private String createConstructor(Method method) {
        String name = method.name

        String body = getMethodBody(method)
        String paramList = ""
        String comment = """/**
             * Construct a new $name instance
             */"""
        String access = method.accessibility.toString().toLowerCase()

        """
            $comment
            $access $name($paramList) $body"""
    }

    private String getMethodBody(method) {
        if (method.isAbstract())
            ";"
        else {
            """{
            }"""
        }
    }

    private String createDestructor() {
        """
            /**
             *
             */
            @Override
            protected void finalize() throws Throwable {
            }"""
    }

    private String createMutator(Field field) {
        String name = field.name
        String capName = name.capitalize()
        String type = field.type.typeName
        """
            /**
             * @param $name the new value for $name
             */
            public ${field.hasModifier("STATIC") ? 'static ' : ''}void set$capName($type $name) {
                this.$name = $name;
            }"""
    }

    private String createAccessor(Field field) {
        String name = field.name
        String capName = name.capitalize()
        String type = field.type.typeName
        if (type != "boolean") {
            """
            /**
             * @return the value of $name
             */
            public ${field.hasModifier("STATIC") ? 'static ' : ''}$type get$capName() {
                return $name;
            }"""
        } else {
            """
            /**
             * @return the value of $name
             */
            public ${field.hasModifier("STATIC") ? 'static ' : ''}$type is$capName() {
                return $name;
            }"""
        }
    }
}
