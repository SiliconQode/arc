/*
 * The MIT License (MIT)
 *
 * Empirilytics Disharmony Injector
 * Copyright (c) 2017-2021 Empirilytics
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
package dev.siliconcode.arc.disharmonies.injector.transform.source.member

import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.disharmonies.injector.transform.model.member.AddParamModelTransform
import dev.siliconcode.arc.disharmonies.injector.transform.source.BasicSourceTransform
import groovy.transform.builder.Builder
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class AddMethodParameter extends BasicSourceTransform {

    Type type
    Method method
    Parameter param

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    @Builder(buildMethodName = "create")
    AddMethodParameter(File file, Type type, Method method, Parameter param) {
        super(file)
        this.type = type
        this.method = method
        this.param = param
    }

    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())
        lines = ops.readLines()

        start = method.getStart() - 1
        end = method.getEnd() - 1
        log.info "Method: ${this.method.name}"
        log.info "Method start: $start"
        log.info "Method end: $end"

        if (start == end)
            text = lines[start]
        else
            text = lines[start..(end - 1)].join("\n")
    }

    @Override
    void buildContent() {
        def matcher = text =~ /${method.getName()}\s*\(.*\)/
        if (matcher.find()) {
            String innerContent = matcher[0]
            def newContent = innerContent.replace(")", "${genParamText()})")

            text = text.replace(innerContent, newContent)
        }
    }

    @Override
    void injectContent() {
        def textLines = text.split("\n")

        for (int i = 0; i < textLines.size(); i++)
            lines[start + i] = textLines[i]

        ops.text = lines.join("\n")
    }

    @Override
    void updateModel() {
        updateImports()

        if (method.isAbstract()) {
            if (type.getType() == Type.INTERFACE) {
                type.getRealizedBy().each {
                    addParamToImplementingMethods(it, method.getName(), param)
                }
            } else {
                type.getGeneralizes().each {
                    addParamToImplementingMethods(it, method.getName(), param)
                }
            }
        }
    }

    def addParamToImplementingMethods(Type type, String methodName, Parameter param) {
        if (type.hasMethodWithName(methodName)) {
            Method method = type.getMethodWithName(methodName)
            AddParamModelTransform apmt = new AddParamModelTransform(method, param.getName(), param.getType().getType(type.getParentProject().getProjectKey()), (Modifier[]) param.getModifiers().toArray())
            apmt.execute()
        }

        if (type.getType() == Type.INTERFACE) {
            type.getRealizedBy().each {
                addParamToImplementingMethods(it, methodName, param)
            }
        } else {
            type.getGeneralizes().each {
                addParamToImplementingMethods(it, methodName, param)
            }
        }
    }

    private String genParamText() {
        StringBuilder builder = new StringBuilder()

        if (method.getParams().size() > 1)
            builder << ", "
        param.getModifiers().each {
            builder << "$it "
        }
        builder << "${param.getType().getTypeName()} "
        builder << param.getName()

        builder.toString()
    }
}
