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
package dev.siliconcode.arc.patterns.gen.generators.pb.tree

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RandomBranch extends AbstractTreeGenerator {

    @Override
    Tree generate() {
        //Node inter = createInterfaceRoot()
        //Node root = branch(0, this.size, inter)
        Node root = branch(0, this.size, null)
        new Tree(root: root)
    }

    private Node branch(int depth, int size, Node parent) {
        Node node = new Node(parent: parent)
        if (depth == 0) {
//            node.value = parent ? randomSelect(nonterms) : root
            node.value = root
            generateChildren(size, node, depth)
        } else if (selectArityLessThan(size) <= 0) {
            node.value = randomSelect(terms)
        } else {
            generateChildren(size, node, depth)
        }

        node
    }

    private void generateChildren(int size, Node node, int depth) {
        int arity = selectArityLessThan(size)
        int children = arity

        if (children > 0) {
            if (node.value != root)
                node.value = randomSelect(nonterms)
            for (int i = 0; i < children; i++) {
                node.children << branch(depth + 1, (int) Math.floor((double) size / arity), node)
            }
        } else {
            node.value = randomSelect(terms)
        }
    }
}
