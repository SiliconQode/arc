/**
 * The MIT License (MIT)
 *
 * Empirilytics Java Parser
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
package dev.siliconcode.arc.parsers.java;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FileTree {

    static class Node {
        @Getter @Setter
        String data;
        @Getter @Setter
        List<Node> children;

        public Node(String data) {
            this.data = data;
            this.children = Lists.newArrayList();
        }

        public void addChild(Node child) {
            if (child != null)
                children.add(child);
        }

        public boolean hasChild(String data) {
            for (Node child : children) {
                if (child.getData().equals(data))
                    return true;
            }
            return false;
        }

        public Node getChild(String data) {
            for (Node child : children) {
                if (child.getData().equals(data))
                    return child;
            }

            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node node = (Node) o;
            return getData().equals(node.getData());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getData());
        }
    }

    private Node root;

    public FileTree() {
        root = new Node("");
    }

    public void addPath(String path) {
        String[] components = path.split(File.separator);

        Node current = root;
        for (String comp : components) {
            if (current.hasChild(comp)) {
                current = current.getChild(comp);
            } else {
                Node temp = new Node(comp);
                current.addChild(temp);
                current = temp;
            }
        }
    }

    public String[] findPaths(Set<String> rootNamespaces) {
        Set<String> result = Sets.newHashSet();

        for (Node child : root.getChildren()) {
            findPathsRec(child, "", result, rootNamespaces);
        }

        return result.toArray(new String[0]);
    }

    private void findPathsRec(Node current, String currPath, Set<String> result, Set<String> rootNamespaces) {
        if (rootNamespaces.contains(current.getData())) {
            result.add(currPath);
        } else {
            currPath = currPath + current.getData() + File.separator;
            for (Node child : current.getChildren()) {
                findPathsRec(child, currPath, result, rootNamespaces);
            }
        }
    }
}
