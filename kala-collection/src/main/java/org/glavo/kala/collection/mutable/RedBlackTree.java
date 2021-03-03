package org.glavo.kala.collection.mutable;

import org.glavo.kala.comparator.Comparators;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

abstract class RedBlackTree<A, N extends RedBlackTree.TreeNode<A, N>> {
    static final boolean RED = true;
    static final boolean BLACK = false;

    final Comparator<? super A> comparator;

    transient N root;
    transient int size;

    RedBlackTree(Comparator<? super A> comparator) {
        this.comparator = comparator == null ? Comparators.naturalOrder() : comparator;
    }

    @SuppressWarnings("unchecked")
    @Nullable N getNode(Object key) {
        try {
            final Comparator<? super A> comparator = this.comparator;
            N n = this.root;
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
        } catch (ClassCastException ignored) {
        }
        return null;
    }

    @Nullable N firstNode() {
        N node = root;
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Nullable N lastNode() {
        N node = root;
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }


    static boolean colorOf(TreeNode<?, ?> p) {
        return p == null ? RedBlackTree.BLACK : p.color;
    }

    static <T extends TreeNode<?, T>> T parentOrNull(T p) {
        return p == null ? null : p.parent;
    }

    static void setColor(TreeNode<?, ?> p, boolean c) {
        if (p != null) {
            p.color = c;
        }
    }

    static <T extends TreeNode<?, T>> T leftOrNull(T p) {
        return (p == null) ? null : p.left;
    }

    static <T extends TreeNode<?, T>> T rightOrNull(T p) {
        return (p == null) ? null : p.right;
    }

    static <T extends TreeNode<?, T>> T minNode(T node) {
        if (node == null) {
            return null;
        }
        return minNodeNonNull(node);
    }

    static <T extends TreeNode<?, T>> T minNodeNonNull(@NotNull T node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    static <T extends TreeNode<?, T>> T maxNode(T node) {
        if (node == null) {
            return null;
        }
        return maxNodeNonNull(node);
    }

    static <T extends TreeNode<?, T>> T maxNodeNonNull(@NotNull T node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    static <T extends TreeNode<?, T>> T successor(T node) {
        if (node == null) {
            return null;
        }
        if (node.right != null) {
            return minNodeNonNull(node.right);
        }
        T n = node.parent;
        T ch = node;
        while (n != null && ch == n.right) {
            ch = n;
            n = n.parent;
        }
        return n;
    }

    static <T extends TreeNode<?, T>> T predecessor(T node) {
        if (node == null) {
            return null;
        }
        if (node.left != null) {
            return maxNodeNonNull(node.left);
        }
        T p = node.parent;
        T ch = node;
        while (p != null && ch == p.left) {
            ch = p;
            p = p.parent;
        }
        return p;
    }

    void rotateLeft(N node) {
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

    void rotateRight(N node) {
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

    void fixAfterInsert(N x) {
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

    void fixAfterDelete(N x) {
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

    static class TreeNode<A, T extends TreeNode<A, T>> {
        A key;

        T left;
        T right;
        T parent;

        boolean color = BLACK;

        TreeNode(A key, T parent) {
            this.key = key;
            this.parent = parent;
        }


    }
}
