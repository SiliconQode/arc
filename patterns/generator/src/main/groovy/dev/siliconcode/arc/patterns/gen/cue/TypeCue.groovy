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
package dev.siliconcode.arc.patterns.gen.cue


import dev.siliconcode.arc.datamodel.Component
import dev.siliconcode.arc.datamodel.Field
import dev.siliconcode.arc.datamodel.Method
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.patterns.gen.generators.pb.RBML2DataModelManager
import dev.siliconcode.arc.patterns.rbml.model.Classifier
import dev.siliconcode.arc.patterns.rbml.model.Role
import groovy.transform.TupleConstructor

@TupleConstructor(includeSuperProperties = true, includeSuperFields = true, excludes = ["fieldCues", "methodCues"])
class TypeCue extends CueContainer {

    Map<String, FieldCue> fieldCues
    Map<String, MethodCue> methodCues

    @Override
    def getDelimString() {
        return (/(?ms)start_type: ${name}.*?end_type: ${name}/)
    }

    @Override
    def getReplacement() {
        return "[[type: ${name}]]"
    }

    @Override
    def getCueForRole(String roleName, Component c) {
        Cue retVal = null
        if (c instanceof Type && name == roleName) {
            retVal = this
        }
        else {
            for (child in children) {
                if (child.value.hasCueForRole(roleName, c)) {
                    retVal = child.value.getCueForRole(roleName, c)
                    break
                }
            }
        }
        return retVal
    }

    @Override
    def hasCueForRole(String roleName, Component t) {
        boolean retVal = false
        if (t instanceof Type) {
            retVal = name == roleName
        }
        else {
            for (child in children) {
                if (child.value.hasCueForRole(roleName, t)) {
                    retVal = true
                    break
                }
            }
        }
        return retVal
    }

    @Override
    String content(String text, Component comp, CueParams params, RBML2DataModelManager manager) {
        Type type = (Type) comp
        Classifier compRole = (Classifier) manager.getRole((Type) comp)
        compRole.behFeats.each { role ->
            String combined = ""
            Cue cue = null
            def comps = manager.getComponentsByRole(role).findAll { ((Method) it).parentType == type }
            comps.each { meth ->
                cue = getCueForRole(role.name, (Component) meth)
                if (cue && (meth as Method).getParentType() == type) {
                    combined += cue.compile(meth, params, manager) + "\n    "
                }
            }
            if (cue) {
                combined = combined.trim()
                text = text.replaceAll(cue.replacement, combined)
            }
        }

        compRole.structFeats.each { role ->
            String combined = ""
            Cue cue = null
            def comps = manager.getComponentsByRole(role).findAll {((Field) it).parentType == type }
            comps.each {fld ->
                cue = getCueForRole(role.name, (Component) fld)
                if (cue && (fld as Field).getParentType() == type) {
                    combined += cue.compile(fld, params, manager) + "\n    "
                }
            }
            if (cue) {
                combined = combined.trim()
                text = text.replaceAll(cue.replacement, combined)
            }
        }

        Map<Cue, String> combinedMap = [:]
        type.getFields().each {
            String roleName = manager.getRole(it)
            if (!roleName)
                roleName = manager.getRelName(it)
            Cue cue = getCueForRole(roleName, it)
            if (cue) {
                String combined = cue.compile(it, params, manager) + "\n    "
                if (combinedMap[cue])
                    combinedMap[cue] += combined
                else
                    combinedMap[cue] = combined
            }
        }
        combinedMap.each {Cue cue, String combined ->
            combined = combined.trim()
            text = text.replaceAll(cue.replacement, combined)
        }

        text
    }
}
