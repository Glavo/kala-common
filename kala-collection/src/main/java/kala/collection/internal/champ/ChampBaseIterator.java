/*
 * Copyright 2025 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection.internal.champ;

import kala.collection.base.AbstractIterator;

import static kala.collection.internal.champ.ChampNode.MaxDepth;

@SuppressWarnings("unchecked")
public abstract class ChampBaseIterator<A, T extends ChampNode<T>> extends AbstractIterator<A> {

    protected int currentValueCursor = 0;
    protected int currentValueLength = 0;
    protected T currentValueNode;

    private int currentStackLevel = -1;
    private int[] nodeCursorsAndLengths;
    private T[] nodes;

    public ChampBaseIterator(T rootNode) {
        if (rootNode.hasNodes()) pushNode(rootNode);
        if (rootNode.hasPayload()) setupPayloadNode(rootNode);
    }

    private void initNodes() {
        if (nodeCursorsAndLengths == null) {
            nodeCursorsAndLengths = new int[MaxDepth * 2];
            nodes = (T[]) new ChampNode<?>[MaxDepth];
        }
    }

    private void setupPayloadNode(T node) {
        currentValueNode = node;
        currentValueCursor = 0;
        currentValueLength = node.payloadArity();
    }

    private void pushNode(T node) {
        initNodes();
        currentStackLevel = currentStackLevel + 1;

        int cursorIndex = currentStackLevel * 2;
        int lengthIndex = currentStackLevel * 2 + 1;

        nodes[currentStackLevel] = node;
        nodeCursorsAndLengths[cursorIndex] = 0;
        nodeCursorsAndLengths[lengthIndex] = node.nodeArity();
    }

    private void popNode() {
        currentStackLevel = currentStackLevel - 1;
    }

    /**
     * Searches for next node that contains payload values,
     * and pushes encountered sub-nodes on a stack for depth-first traversal.
     */
    private boolean searchNextValueNode() {
        while (currentStackLevel >= 0) {
            int cursorIndex = currentStackLevel * 2;
            int lengthIndex = currentStackLevel * 2 + 1;

            int nodeCursor = nodeCursorsAndLengths[cursorIndex];
            int nodeLength = nodeCursorsAndLengths[lengthIndex];

            if (nodeCursor < nodeLength) {
                nodeCursorsAndLengths[cursorIndex] += 1;

                var nextNode = nodes[currentStackLevel].getNode(nodeCursor);

                if (nextNode.hasNodes()) {
                    pushNode(nextNode);
                }
                if (nextNode.hasPayload()) {
                    setupPayloadNode(nextNode);
                    return true;
                }
            } else {
                popNode();
            }
        }

        return false;
    }

    @Override
    public final boolean hasNext() {
        return (currentValueCursor < currentValueLength) || searchNextValueNode();
    }
}
