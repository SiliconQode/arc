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

import dev.siliconcode.arc.datamodel.File
import dev.siliconcode.arc.datamodel.Literal
import dev.siliconcode.arc.datamodel.Type
import dev.siliconcode.arc.disharmonies.injector.transform.source.AddMember

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveLiteral extends AddMember {

    Type from
    Type to
    Literal literal
    File toFile

    DeleteLiteral deleteLiteral
    AddLiteral addLiteral

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    MoveLiteral(File file, Type from, File toFile, Type to, Literal literal) {
        super(file)
        this.from = from
        this.to = to
        this.literal = literal
        this.toFile = toFile
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setup() {
        deleteLiteral = new DeleteLiteral(file, from, literal)
        addLiteral = new AddLiteral(toFile, to, literal)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void buildContent() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void injectContent() {
        deleteLiteral.execute()
        addLiteral.execute()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void updateModel() {}
}
