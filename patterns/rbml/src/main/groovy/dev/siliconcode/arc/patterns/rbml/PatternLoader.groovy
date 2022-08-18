/*
 * The MIT License (MIT)
 *
 * Empirilytics RBML DSL
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
package dev.siliconcode.arc.patterns.rbml

import com.google.common.collect.ImmutableList
import dev.siliconcode.arc.patterns.rbml.io.SpecificationReader
import dev.siliconcode.arc.patterns.rbml.model.SPS
import org.yaml.snakeyaml.Yaml

/**
 * A Singleton class used to load patterns contained in this
 * module into an SPS for use by other components of the system
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class PatternLoader {

    /**
     * A Simple mapping of the pattern names to their yaml files
     */
    private final Map<String, String> patterns = [
            'abstract factory' : '/rbmldef/abstract_factory.yml',
            'adapter' : '/rbmldef/adapter.yml',
            'bridge' : '/rbmldef/bridge.yml',
            'builder' : '/rbmldef/builder.yml',
            'chain of responsibility' : '/rbmldef/chain_of_responsibility.yml',
            'command' : '/rbmldef/command.yml',
            'composite' : '/rbmldef/composite.yml',
            'decorator' : '/rbmldef/decorator.yml',
            'facade' : '/rbmldef/facade.yml',
            'factory method' : '/rbmldef/factory_method.yml',
            'flyweight' : '/rbmldef/flyweight.yml',
            'interpreter' : '/rbmldef/interpreter.yml',
            'iterator' : '/rbmldef/iterator.yml',
            'mediator' : '/rbmldef/mediator.yml',
            'memento' : '/rbmldef/memento.yml',
            'observer' : '/rbmldef/observer.yml',
            'prototype' : '/rbmldef/prototype.yml',
            'proxy' : '/rbmldef/proxy.yml',
            'singleton' : '/rbmldef/singleton.yml',
            'state' : '/rbmldef/state.yml',
            'strategy' : '/rbmldef/strategy.yml',
            'template method' : '/rbmldef/template_method.yml',
            'visitor' : '/rbmldef/visitor.yml'
    ]

    /**
     * Loads the pattern from the yaml file into an SPS via
     * the Specification reader.
     *
     * @param pattern Pattern whose SPS is requested
     * @return The SPS for the named pattern
     *
     * @throws IllegalArgumentException when the provided pattern name is either
     * null or empty, or when it is not a known pattern name.
     */
    SPS loadPattern(String pattern) {
        if (!pattern)
            throw new IllegalArgumentException("Pattern name cannot be null or empty")
        if (!patterns[pattern])
            return null

        String text = getClass().getResource(patterns[pattern]).text
        Yaml yaml = new Yaml()
        SpecificationReader reader = new SpecificationReader()

        def map = yaml.load(text)
        reader.processSPS(map)
        reader.sps
    }

    /**
     * Returns an immutable list of the known names of patterns that may be loaded
     * @return Immutable list of pattern names
     */
    def patterns() {
        ImmutableList.builder().addAll(patterns.keySet()).build()
    }
}
