// https://github.com/skywind3000/avlmini
package kala.collection.internal.tree;

import kala.internal.ComparableUtils;

import java.util.Comparator;

public class AVLTree<K, N extends AVLTree.Node<K, N>> {
    protected int size;
    protected N root;

    protected Comparator<? super K> comparator;

    protected int leftHeight(N node) {
        return node.left != null ? node.left.height : 0;
    }

    protected int rightHeight(N node) {
        return node.right != null ? node.right.height : 0;
    }

    protected N firstNode() {
        N node = root;
        if (node == null) return null;
        while (node.left != null)
            node = node.left;
        return node;
    }

    protected N lastNode() {
        N node = root;
        if (node == null) return null;
        while (node.right != null)
            node = node.right;
        return node;
    }

    protected N nextNode(N node) {
        if (node == null) return null;
        if (node.right != null) {
            node = node.right;
            while (node.left != null)
                node = node.left;
        } else {
            while (true) {
                N n = node;
                node = node.parent;
                if (node == null || node.left == n)
                    break;
            }
        }
        return node;
    }

    protected N prevNode(N node) {
        if (node == null) return null;
        if (node.left != null) {
            node = node.left;
            while (node.right != null) {
                node = node.right;
            }
        } else {
            while (true) {
                N n = node;
                node = node.parent;
                if (node == null || node.right == n)
                    break;
            }
        }
        return node;
    }

    private void nodeChildReplace(N oldNode, N newNode, N parent) {
        if (parent != null) {
            if (parent.left == oldNode)
                parent.left = newNode;
            else
                parent.right = newNode;
        } else {
            root = newNode;
        }
    }


    protected N findNode(K key) {
        N node = this.root;
        if (comparator == null) {
            while (node != null) {
                int c = ComparableUtils.compare(key, node.key);
                if (c == 0)
                    return node;
                else if (c < 0)
                    node = node.left;
                else
                    node = node.right;
            }
        } else {
            while (node != null) {
                int c = comparator.compare(key, node.key);
                if (c == 0)
                    return node;
                else if (c < 0)
                    node = node.left;
                else
                    node = node.right;
            }
        }

        return null;
    }

    public static class Node<K, N extends Node<K, N>> {
        int height;
        N left, right, parent;
        K key;
    }
}
