package kala.collection.primitive.internal.tree;

import kala.function.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.function.*;

public abstract class ${Type}RedBlackTree<N extends ${Type}RedBlackTree.Node<N>> implements Serializable {
    private static final long serialVersionUID = 3036340578028981301L;

    protected static final boolean RED = true;
    protected static final boolean BLACK = false;

    protected transient N root;
    protected transient int size;

    protected ${Type}RedBlackTree() {
    }

    protected final @Nullable N getNode(${PrimitiveType} key) {
        N n = this.root;
        while (n != null) {
            int c = ${WrapperType}.compare(key, n.key);
            if (c < 0) {
                n = n.left;
            } else if (c > 0) {
                n = n.right;
            } else {
                return n;
            }
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

    protected static boolean colorOf(Node<?> p) {
        return p == null ? ${Type}RedBlackTree.BLACK : p.color;
    }

    protected static <N extends Node<N>> N parentOrNull(N p) {
        return p == null ? null : p.parent;
    }

    protected static void setColor(Node<?> p, boolean c) {
        if (p != null) {
            p.color = c;
        }
    }

    protected static <N extends Node<N>> N leftOrNull(@Nullable N p) {
        return p == null ? null : p.left;
    }

    protected static <N extends Node<N>> N rightOrNull(@Nullable N p) {
        return p == null ? null : p.right;
    }

    protected static <N extends Node<N>> N minNode(@NotNull N node) {
        N left;
        while ((left = node.left) != null) {
            node = left;
        }
        return node;
    }

    protected static <N extends Node<N>> N maxNode(@NotNull N node) {
        N right;
        while ((right = node.right) != null) {
            node = right;
        }
        return node;
    }

    protected static <N extends Node<N>> N successor(@NotNull N node) {
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

    protected static <N extends Node<N>> N predecessor(@NotNull N node) {
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
        x.color = ${Type}RedBlackTree.RED;

        while (x != null && x != root && x.parent.color == ${Type}RedBlackTree.RED) {
            if (parentOrNull(x) == leftOrNull(parentOrNull(parentOrNull(x)))) {
                N y = rightOrNull(parentOrNull(parentOrNull(x)));
                if (colorOf(y) == ${Type}RedBlackTree.RED) {
                    setColor(parentOrNull(x), ${Type}RedBlackTree.BLACK);
                    setColor(y, ${Type}RedBlackTree.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), ${Type}RedBlackTree.RED);
                    x = parentOrNull(parentOrNull(x));
                } else {
                    if (x == rightOrNull(parentOrNull(x))) {
                        x = parentOrNull(x);
                        rotateLeft(x);
                    }
                    setColor(parentOrNull(x), ${Type}RedBlackTree.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), ${Type}RedBlackTree.RED);
                    rotateRight(parentOrNull(parentOrNull(x)));
                }
            } else {
                N y = leftOrNull(parentOrNull(parentOrNull(x)));
                if (colorOf(y) == ${Type}RedBlackTree.RED) {
                    setColor(parentOrNull(x), ${Type}RedBlackTree.BLACK);
                    setColor(y, ${Type}RedBlackTree.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), ${Type}RedBlackTree.RED);
                    x = parentOrNull(parentOrNull(x));
                } else {
                    if (x == leftOrNull(parentOrNull(x))) {
                        x = parentOrNull(x);
                        rotateRight(x);
                    }
                    setColor(parentOrNull(x), ${Type}RedBlackTree.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), ${Type}RedBlackTree.RED);
                    rotateLeft(parentOrNull(parentOrNull(x)));
                }
            }
        }
        root.color = ${Type}RedBlackTree.BLACK;
    }

    protected final void fixAfterDelete(N x) {
        while (x != root && colorOf(x) == ${Type}RedBlackTree.BLACK) {
            if (x == leftOrNull(parentOrNull(x))) {
                N sib = rightOrNull(parentOrNull(x));
                if (colorOf(sib) == ${Type}RedBlackTree.RED) {
                    setColor(sib, ${Type}RedBlackTree.BLACK);
                    setColor(parentOrNull(x), ${Type}RedBlackTree.RED);
                    rotateLeft(parentOrNull(x));
                    sib = rightOrNull(parentOrNull(x));
                }

                if (colorOf(leftOrNull(sib)) == ${Type}RedBlackTree.BLACK &&
                        colorOf(rightOrNull(sib)) == ${Type}RedBlackTree.BLACK) {
                    setColor(sib, ${Type}RedBlackTree.RED);
                    x = parentOrNull(x);
                } else {
                    if (colorOf(rightOrNull(sib)) == ${Type}RedBlackTree.BLACK) {
                        setColor(leftOrNull(sib), ${Type}RedBlackTree.BLACK);
                        setColor(sib, ${Type}RedBlackTree.RED);
                        rotateRight(sib);
                        sib = rightOrNull(parentOrNull(x));
                    }
                    setColor(sib, colorOf(parentOrNull(x)));
                    setColor(parentOrNull(x), ${Type}RedBlackTree.BLACK);
                    setColor(rightOrNull(sib), ${Type}RedBlackTree.BLACK);
                    rotateLeft(parentOrNull(x));
                    x = root;
                }
            } else {
                N sib = leftOrNull(parentOrNull(x));

                if (colorOf(sib) == ${Type}RedBlackTree.RED) {
                    setColor(sib, ${Type}RedBlackTree.BLACK);
                    setColor(parentOrNull(x), ${Type}RedBlackTree.RED);
                    rotateRight(parentOrNull(x));
                    sib = leftOrNull(parentOrNull(x));
                }

                if (colorOf(rightOrNull(sib)) == ${Type}RedBlackTree.BLACK &&
                        colorOf(leftOrNull(sib)) == ${Type}RedBlackTree.BLACK) {
                    setColor(sib, ${Type}RedBlackTree.RED);
                    x = parentOrNull(x);
                } else {
                    if (colorOf(leftOrNull(sib)) == ${Type}RedBlackTree.BLACK) {
                        setColor(rightOrNull(sib), ${Type}RedBlackTree.BLACK);
                        setColor(sib, ${Type}RedBlackTree.RED);
                        rotateLeft(sib);
                        sib = leftOrNull(parentOrNull(x));
                    }
                    setColor(sib, colorOf(parentOrNull(x)));
                    setColor(parentOrNull(x), ${Type}RedBlackTree.BLACK);
                    setColor(leftOrNull(sib), ${Type}RedBlackTree.BLACK);
                    rotateRight(parentOrNull(x));
                    x = root;
                }
            }
        }

        setColor(x, ${Type}RedBlackTree.BLACK);
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

            if (node.color == ${Type}RedBlackTree.BLACK) {
                fixAfterDelete(replacement);
            }
        } else if (node.parent == null) {
            root = null;
        } else {
            if (node.color == ${Type}RedBlackTree.BLACK) {
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

    protected final void forEachKey0(@NotNull ${Type}Consumer consumer) {
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

    public int knownSize() {
        return size;
    }

    public final void clear() {
        root = null;
        size = 0;
    }

    protected static class Node<N extends Node<N>> {
        public boolean color = BLACK;
        public ${PrimitiveType} key;

        public N left;
        public N right;
        public N parent;

        public Node(${PrimitiveType} key, N parent) {
            this.key = key;
            this.parent = parent;
        }

        public void copyValuesFrom(N other) {
            this.key = other.key;
        }
    }
}
