/*
 * The MIT License (MIT)
 *
 * Empirilytics Parsers
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
package dev.siliconcode.arc.parsers

import com.google.common.collect.Sets
import dev.siliconcode.arc.datamodel.*
import dev.siliconcode.arc.datamodel.util.DBCredentials
import dev.siliconcode.arc.datamodel.util.DBManager

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AssociationExtractor {

    Project project

    DBCredentials credentials

    AssociationExtractor(Project proj, DBCredentials credentials) {
        this.project = proj
        this.credentials = credentials
    }

    void extractAssociations() {
        Set<Type> types = getTypes()

        types.each { Type type ->
            processType(type)
        }
    }

    protected void processType(Type type) {
        handleTypeAssociation(type)
    }

    protected Set<Type> getTypes() {
        Set<Type> types = Sets.newConcurrentHashSet()
        DBManager.instance.open(credentials)
        types.addAll(project.getAllTypes())
        DBManager.instance.close()

        return types
    }

    protected void handleTypeAssociation(Type type) {
        DBManager.instance.open(credentials)
        List<Field> fields = type.getFields()

        fields.each { Field f ->
            if (f.getType() != null && f.getType().getReference() != null)
                createAssociation(type, f.getType())
        }
        DBManager.instance.close()
    }

    protected void createAssociation(Type type, TypeRef ref) {
        Reference reference = ref.getReference()
        if (reference && ref.getType() == TypeRefType.Type) {
            Type dep = ref.getType(project.getProjectKey())
            if (dep != null)
                type.associatedTo(dep)
        }
    }
}
