package kala.collection.internal.tree;

import kala.internal.ComparableUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class BTree16<K> {
    static final int M = 16;
    static final int T = M / 2;
    static final int MAX_KEY_NUMBER = M - 1;
    static final int MIN_KEY_NUMBER = T - 1;

    private int size;
    private Node<K> root;

    protected int compare(K k0, K k1) {
        return ComparableUtils.compare(k0, k1);
    }

    protected int binarySearch(Node<K> node, K value) {
        int n = node.n;

        int low = 0;
        int high = n - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            K midVal = node.getKey(mid);

            int c = compare(midVal, value);

            if (c < 0) {
                low = mid + 1;
            } else if (c > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }

        return -(low + 1);
    }

    public void forEachKey(Consumer<? super K> action) {
        if (root == null) return;
        forEachKey(root, action);
    }

    protected void forEachKey(@NotNull Node<K> node, Consumer<? super K> action) {
        int n = node.n;

        int i;
        if (node.isLeaf()) {
            for (i = 0; i < n; i++) {
                action.accept(node.getKey(i));
            }
        } else {
            forEachKey(node.getNode(0), action);
            for (i = 0; i < n; i++) {
                action.accept(node.getKey(i));
                forEachKey(node.getNode(i + 1), action);
            }
        }
    }

    protected Node<K> search(Node<K> node, K value) {
        while (true) {
            int idx = binarySearch(node, value);

            if (idx >= 0) {
                return node;
            }

            if (node.isLeaf()) {
                return null;
            }

            node = node.getNode(-(idx + 1));
        }
    }

    public void insert(K key) {
        if (root == null) { // root is full
            Node<K> node = new Node<>();
            node.k0 = key;
            node.n = 1;
            root = node;
        } else if (root.n == MAX_KEY_NUMBER) {
            Node<K> node = new Node<>();
            node.n0 = root;
            splitChild(node, root, 0);

            insertNonFull(node, key);
            root = node;
        } else {
            insertNonFull(root, key);
        }

        size++;
    }

    protected boolean insertNonFull(Node<K> node, K key) {
        int idx = -(binarySearch(node, key) + 1);

        if (node.isLeaf()) {
            node.insertKey(idx, key);
            node.n++;
        } else {
            Node<K> child = node.getNode(idx);

            if (child.n == MAX_KEY_NUMBER) {
                splitChild(node, child, idx);
                if (compare(key, node.getKey(idx)) > 0) {
                    idx++;
                }
            }

            insertNonFull(node.getNode(idx), key);
        }

        return true;
    }

    protected void splitChild(Node<K> parent, Node<K> child, int idx) {
        Node<K> newChild = new Node<>();

        newChild.n = MIN_KEY_NUMBER;

        for (int i = 0; i < MIN_KEY_NUMBER; i++) {
            newChild.setKey(i, child.getKey(i + T));
            child.setKey(i + T, null);
        }

        if (!child.isLeaf()) {
            for (int i = 0; i < T; i++) {
                newChild.setNode(i, child.getNode(i + T));
                child.setNode(i + T, null);
            }
        }

        child.n = MIN_KEY_NUMBER;

        parent.insertKey(idx, child.getKey(MIN_KEY_NUMBER));
        parent.insertNode(idx + 1, newChild);
        parent.n++;
    }

    /*
    protected void delete(K key) {
        delete(root, key);

        if (root.n == 0) {
            root = null;
        }

        size--;
    }

    protected void delete(Node<K> node, K key) {
        int idx = binarySearch(node, key);

        if (idx >= 0) {
            if (node.isLeaf()) {
                node.removeKey(idx);
            } else {
                Node<K> predecessorChild = node.getNode(idx);
                if (predecessorChild.n > MIN_KEY_NUMBER) {
                    K successor = deleteSuccessor(predecessorChild);
                    node.setKey(idx, successor);
                } else {
                    Node<K> successorChild = node.getNode(idx + 1);
                    if (successorChild.n >= T) {
                        K successor = deleteSuccessor(predecessorChild);
                        node.setKey(idx, successor);
                    } else {
                        predecessorChild.setKey(predecessorChild.n++, node.getKey(idx));

                        int i;
                        for (i = 0; i < successorChild.n; i++) {
                            predecessorChild.setKey(predecessorChild.n + i + 1, successorChild.getKey(i));
                            predecessorChild.setNode(predecessorChild.n + i + 2, successorChild.getNode(i));
                        }
                    }
                }

            }

            return;
        }

        // TODO
    }

    private K deletePredecessor(Node<K> node) {
        while (!node.isLeaf()) {
            node = node.n0;
        }

        final int newN = node.n - 1;

        K res = node.getKey(newN);
        node.setKey(newN, null);
        node.n = (byte) newN;
        return res;
    }

    private K deleteSuccessor(Node<K> node) {
        while (!node.isLeaf()) {
            node = node.n0;
        }

        final int newN = node.n - 1;

        K res = node.getKey(0);
        for (int i = 0; i < newN; i++) {
            node.setKey(i, node.getKey(i + 1));
        }
        node.setKey(newN, null);

        node.n = (byte) newN;
        return res;
    }
     */
    public static final class Node<K> {
        byte n;

        // K parent;

        /*
         * Store elements compactly inside this class, reducing indirection and memory usage.
         */

        K k0, k1, k2, k3, k4, k5, k6, k7, k8, k9, k10, k11, k12, k13, k14;
        Node<K> n0, n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15;

        public Node() {
        }

        K getKey(int idx) {
            switch (idx) {
                case 0:
                    return k0;
                case 1:
                    return k1;
                case 2:
                    return k2;
                case 3:
                    return k3;
                case 4:
                    return k4;
                case 5:
                    return k5;
                case 6:
                    return k6;
                case 7:
                    return k7;
                case 8:
                    return k8;
                case 9:
                    return k9;
                case 10:
                    return k10;
                case 11:
                    return k11;
                case 12:
                    return k12;
                case 13:
                    return k13;
                case 14:
                    return k14;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        void setKey(int idx, K key) {
            switch (idx) {
                case 0:
                    k0 = key;
                    break;
                case 1:
                    k1 = key;
                    break;
                case 2:
                    k2 = key;
                    break;
                case 3:
                    k3 = key;
                    break;
                case 4:
                    k4 = key;
                    break;
                case 5:
                    k5 = key;
                    break;
                case 6:
                    k6 = key;
                    break;
                case 7:
                    k7 = key;
                    break;
                case 8:
                    k8 = key;
                    break;
                case 9:
                    k9 = key;
                    break;
                case 10:
                    k10 = key;
                    break;
                case 11:
                    k11 = key;
                    break;
                case 12:
                    k12 = key;
                    break;
                case 13:
                    k13 = key;
                    break;
                case 14:
                    k14 = key;
                    break;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        void insertKey(int idx, K key) {
            for (int i = MAX_KEY_NUMBER - 1; i > idx; i--) {
                setKey(i, getKey(i - 1));
            }
            setKey(idx, key);
        }

        void removeKey(int idx) {
            setKey(idx, null);
            for (int i = idx; i < MAX_KEY_NUMBER - 1; i++) {
                setKey(idx, getKey(i + 1));
            }

            setKey(MAX_KEY_NUMBER - 1, null);
        }

        Node<K> getNode(int idx) {
            switch (idx) {
                case 0:
                    return n0;
                case 1:
                    return n1;
                case 2:
                    return n2;
                case 3:
                    return n3;
                case 4:
                    return n4;
                case 5:
                    return n5;
                case 6:
                    return n6;
                case 7:
                    return n7;
                case 8:
                    return n8;
                case 9:
                    return n9;
                case 10:
                    return n10;
                case 11:
                    return n11;
                case 12:
                    return n12;
                case 13:
                    return n13;
                case 14:
                    return n14;
                case 15:
                    return n15;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        void setNode(int idx, Node<K> node) {
            switch (idx) {
                case 0:
                    n0 = node;
                    break;
                case 1:
                    n1 = node;
                    break;
                case 2:
                    n2 = node;
                    break;
                case 3:
                    n3 = node;
                    break;
                case 4:
                    n4 = node;
                    break;
                case 5:
                    n5 = node;
                    break;
                case 6:
                    n6 = node;
                    break;
                case 7:
                    n7 = node;
                    break;
                case 8:
                    n8 = node;
                    break;
                case 9:
                    n9 = node;
                    break;
                case 10:
                    n10 = node;
                    break;
                case 11:
                    n11 = node;
                    break;
                case 12:
                    n12 = node;
                    break;
                case 13:
                    n13 = node;
                    break;
                case 14:
                    n14 = node;
                    break;
                case 15:
                    n15 = node;
                    break;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        void insertNode(int idx, Node<K> node) {
            for (int i = M - 1; i > idx; i--) {
                setNode(i, getNode(i - 1));
            }

            setNode(idx, node);
        }

        public boolean isLeaf() {
            return n0 == null;
        }

        // For Debug
        @Override
        public String toString() {
            StringBuilder res = new StringBuilder();
            res.append("{");
            res.append("\"n\": ").append(n).append(',');

            res.append("\"keys\": {");
            res.append("\"k0\"").append(": ").append(k0);
            for (int i = 1; i < MAX_KEY_NUMBER; i++) {
                res.append(",\"k").append(i).append("\": ").append(getKey(i));
            }
            res.append("}");

            res.append(',');

            res.append("\"nodes\": {");
            res.append("\"n0\"").append(": ").append(n0);
            for (int i = 1; i <= MAX_KEY_NUMBER; i++) {
                res.append(",\"n").append(i).append("\": ").append(getNode(i));
            }
            res.append("}");

            res.append("}");
            return res.toString();
        }
    }
}
