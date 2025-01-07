/*
 * Copyright 2024 Glavo
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
package kala.collection.internal.tree;

import kala.internal.ComparableUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.function.Consumer;

public abstract class RedBlackTree<A, N extends RedBlackTree.Node<A, N>> implements Serializable {
    @Serial
    private static final long serialVersionUID = 3036340578028981301L;

    protected static final boolean RED = true;
    protected static final boolean BLACK = false;

    protected final @Nullable Comparator<? super A> comparator;

    protected transient N root;
    protected transient int size;

    protected RedBlackTree(@Nullable Comparator<? super A> comparator) {
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    protected final @Nullable N getNode(Object key) {
        try {
            N n = this.root;

            if (comparator == null) {
                while (n != null) {
                    int c = ComparableUtils.compare(key, n.key);
                    if (c < 0) {
                        n = n.left;
                    } else if (c > 0) {
                        n = n.right;
                    } else {
                        return n;
                    }
                }
            } else {
                while (n != null) {
                    int c = comparator.compare((A) key, n.key);
                    if (c < 0) {
                        n = n.left;
                    } else if (c > 0) {
                        n = n.right;
                    } else {
                        return n;
                    }
                }
            }
        } catch (ClassCastException | NullPointerException ignored) {
        }
        return null;
    }

    protected final @Nullable N firstNode() {
        N node = root;
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    protected final @Nullable N lastNode() {
        N node = root;
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    protected static boolean colorOf(Node<?, ?> p) {
        return p == null ? RedBlackTree.BLACK : p.color;
    }

    protected static <N extends Node<?, N>> N parentOrNull(N p) {
        return p == null ? null : p.parent;
    }

    protected static void setColor(Node<?, ?> p, boolean c) {
        if (p != null) {
            p.color = c;
        }
    }

    protected static <N extends Node<?, N>> N leftOrNull(@Nullable N p) {
        return p == null ? null : p.left;
    }

    protected static <N extends Node<?, N>> N rightOrNull(@Nullable N p) {
        return p == null ? null : p.right;
    }

    protected static <N extends Node<?, N>> N minNode(@NotNull N node) {
        N left;
        while ((left = node.left) != null) {
            node = left;
        }
        return node;
    }

    protected static <N extends Node<?, N>> N maxNode(@NotNull N node) {
        N right;
        while ((right = node.right) != null) {
            node = right;
        }
        return node;
    }

    protected static <N extends Node<?, N>> N successor(@NotNull N node) {
        if (node.right != null) {
            return minNode(node.right);
        }
        N n = node.parent;
        N ch = node;
        while (n != null && ch == n.right) {
            ch = n;
            n = n.parent;
        }
        return n;
    }

    protected static <N extends Node<?, N>> N predecessor(@NotNull N node) {
        if (node.left != null) {
            return maxNode(node.left);
        }
        N p = node.parent;
        N ch = node;
        while (p != null && ch == p.left) {
            ch = p;
            p = p.parent;
        }
        return p;
    }

    protected final void rotateLeft(N node) {
        if (node == null) {
            return;
        }
        N r = node.right;
        node.right = r.left;
        if (r.left != null) {
            r.left.parent = node;
        }
        r.parent = node.parent;
        if (node.parent == null) {
            root = r;
        } else if (node.parent.left == node) {
            node.parent.left = r;
        } else {
            node.parent.right = r;
        }
        r.left = node;
        node.parent = r;
    }

    protected final void rotateRight(N node) {
        if (node == null) {
            return;
        }
        N l = node.left;
        node.left = l.right;
        if (l.right != null) {
            l.right.parent = node;
        }
        l.parent = node.parent;
        if (node.parent == null) {
            root = l;
        } else if (node.parent.right == node) {
            node.parent.right = l;
        } else {
            node.parent.left = l;
        }
        l.right = node;
        node.parent = l;
    }

    protected final void fixAfterInsert(N x) {
        x.color = RedBlackTree.RED;

        while (x != null && x != root && x.parent.color == RedBlackTree.RED) {
            if (parentOrNull(x) == leftOrNull(parentOrNull(parentOrNull(x)))) {
                N y = rightOrNull(parentOrNull(parentOrNull(x)));
                if (colorOf(y) == RedBlackTree.RED) {
                    setColor(parentOrNull(x), RedBlackTree.BLACK);
                    setColor(y, RedBlackTree.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), RedBlackTree.RED);
                    x = parentOrNull(parentOrNull(x));
                } else {
                    if (x == rightOrNull(parentOrNull(x))) {
                        x = parentOrNull(x);
                        rotateLeft(x);
                    }
                    setColor(parentOrNull(x), RedBlackTree.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), RedBlackTree.RED);
                    rotateRight(parentOrNull(parentOrNull(x)));
                }
            } else {
                N y = leftOrNull(parentOrNull(parentOrNull(x)));
                if (colorOf(y) == RedBlackTree.RED) {
                    setColor(parentOrNull(x), RedBlackTree.BLACK);
                    setColor(y, RedBlackTree.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), RedBlackTree.RED);
                    x = parentOrNull(parentOrNull(x));
                } else {
                    if (x == leftOrNull(parentOrNull(x))) {
                        x = parentOrNull(x);
                        rotateRight(x);
                    }
                    setColor(parentOrNull(x), RedBlackTree.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), RedBlackTree.RED);
                    rotateLeft(parentOrNull(parentOrNull(x)));
                }
            }
        }
        root.color = RedBlackTree.BLACK;
    }

    protected final void fixAfterDelete(N x) {
        while (x != root && colorOf(x) == RedBlackTree.BLACK) {
            if (x == leftOrNull(parentOrNull(x))) {
                N sib = rightOrNull(parentOrNull(x));
                if (colorOf(sib) == RedBlackTree.RED) {
                    setColor(sib, RedBlackTree.BLACK);
                    setColor(parentOrNull(x), RedBlackTree.RED);
                    rotateLeft(parentOrNull(x));
                    sib = rightOrNull(parentOrNull(x));
                }

                if (colorOf(leftOrNull(sib)) == RedBlackTree.BLACK &&
                        colorOf(rightOrNull(sib)) == RedBlackTree.BLACK) {
                    setColor(sib, RedBlackTree.RED);
                    x = parentOrNull(x);
                } else {
                    if (colorOf(rightOrNull(sib)) == RedBlackTree.BLACK) {
                        setColor(leftOrNull(sib), RedBlackTree.BLACK);
                        setColor(sib, RedBlackTree.RED);
                        rotateRight(sib);
                        sib = rightOrNull(parentOrNull(x));
                    }
                    setColor(sib, colorOf(parentOrNull(x)));
                    setColor(parentOrNull(x), RedBlackTree.BLACK);
                    setColor(rightOrNull(sib), RedBlackTree.BLACK);
                    rotateLeft(parentOrNull(x));
                    x = root;
                }
            } else {
                N sib = leftOrNull(parentOrNull(x));

                if (colorOf(sib) == RedBlackTree.RED) {
                    setColor(sib, RedBlackTree.BLACK);
                    setColor(parentOrNull(x), RedBlackTree.RED);
                    rotateRight(parentOrNull(x));
                    sib = leftOrNull(parentOrNull(x));
                }

                if (colorOf(rightOrNull(sib)) == RedBlackTree.BLACK &&
                        colorOf(leftOrNull(sib)) == RedBlackTree.BLACK) {
                    setColor(sib, RedBlackTree.RED);
                    x = parentOrNull(x);
                } else {
                    if (colorOf(leftOrNull(sib)) == RedBlackTree.BLACK) {
                        setColor(rightOrNull(sib), RedBlackTree.BLACK);
                        setColor(sib, RedBlackTree.RED);
                        rotateLeft(sib);
                        sib = leftOrNull(parentOrNull(x));
                    }
                    setColor(sib, colorOf(parentOrNull(x)));
                    setColor(parentOrNull(x), RedBlackTree.BLACK);
                    setColor(leftOrNull(sib), RedBlackTree.BLACK);
                    rotateRight(parentOrNull(x));
                    x = root;
                }
            }
        }

        setColor(x, RedBlackTree.BLACK);
    }

    protected final void remove0(N node) {
        if (node.left != null && node.right != null) {
            N s = successor(node);
            node.copyValuesFrom(s);
            node = s;
        }

        N replacement = node.left != null ? node.left : node.right;

        if (replacement != null) {
            replacement.parent = node.parent;
            if (node.parent == null) {
                root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }
            node.left = node.right = node.parent = null;

            if (node.color == RedBlackTree.BLACK) {
                fixAfterDelete(replacement);
            }
        } else if (node.parent == null) {
            root = null;
        } else {
            if (node.color == RedBlackTree.BLACK) {
                fixAfterDelete(node);
            }

            if (node.parent != null) {
                if (node == node.parent.left) {
                    node.parent.left = null;
                } else if (node == node.parent.right) {
                    node.parent.right = null;
                }
                node.parent = null;
            }
        }
        size--;
    }

    protected final void forEachKey0(@NotNull Consumer<? super A> consumer) {
        N node = this.firstNode();
        while (node != null) {
            consumer.accept(node.key);

            N n;
            if (node.right != null) {
                n = node.right;
                while (n.left != null) {
                    n = n.left;
                }
            } else {
                n = node.parent;
                N c = node;

                while (n != null && c == n.right) {
                    c = n;
                    n = n.parent;
                }
            }
            node = n;
        }
    }

    public final boolean isEmpty() {
        return size == 0;
    }

    public final int size() {
        return size;
    }

    public final void clear() {
        root = null;
        size = 0;
    }

    public final Comparator<? super A> comparator() {
        return this.comparator;
    }

    protected static class Node<A, N extends Node<A, N>> {
        public boolean color = BLACK;
        public A key;

        public N left;
        public N right;
        public N parent;

        public Node(A key, N parent) {
            this.key = key;
            this.parent = parent;
        }

        public void copyValuesFrom(N other) {
            this.key = other.key;
        }
    }
}
