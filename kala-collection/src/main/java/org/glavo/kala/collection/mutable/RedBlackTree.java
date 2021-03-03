package org.glavo.kala.collection.mutable;

import org.jetbrains.annotations.NotNull;

final class RedBlackTree {
    static final boolean RED = true;
    static final boolean BLACK = false;

    static boolean colorOf(TreeNode<?> p) {
        return p == null ? RedBlackTree.BLACK : p.color;
    }

    static <T extends TreeNode<T>> T parentOrNull(T p) {
        return p == null ? null : p.parent;
    }

    static void setColor(TreeNode<?> p, boolean c) {
        if (p != null) {
            p.color = c;
        }
    }

    static <T extends TreeNode<T>> T leftOrNull(T p) {
        return (p == null) ? null : p.left;
    }

    static <T extends TreeNode<T>> T rightOrNull(T p) {
        return (p == null) ? null : p.right;
    }

    static <T extends TreeNode<T>> T minNode(T node) {
        if (node == null) {
            return null;
        }
        return minNodeNonNull(node);
    }

    static <T extends TreeNode<T>> T minNodeNonNull(@NotNull T node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    static <T extends TreeNode<T>> T maxNode(T node) {
        if (node == null) {
            return null;
        }
        return maxNodeNonNull(node);
    }

    static <T extends TreeNode<T>> T maxNodeNonNull(@NotNull T node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    static <T extends TreeNode<T>> T successor(T  node) {
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

    static <T extends TreeNode<T>> T predecessor(T node) {
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


    static class TreeNode<T extends TreeNode<T>> {
        T left;
        T right;
        T parent;

        boolean color = BLACK;

        TreeNode(T parent) {
            this.parent = parent;
        }
    }
}
