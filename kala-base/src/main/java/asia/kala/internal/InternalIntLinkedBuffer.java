package asia.kala.internal;

import asia.kala.iterator.AbstractIntIterator;
import asia.kala.iterator.IntIterator;
import org.jetbrains.annotations.ApiStatus;

import java.util.NoSuchElementException;

@ApiStatus.Internal
public class InternalIntLinkedBuffer {
    static final class Node {
        final int head;
        Node tail;

        public Node(int head, Node tail) {
            this.head = head;
            this.tail = tail;
        }

        public final IntIterator iterator() {
            return new Itr(this);
        }

        static final class Itr extends AbstractIntIterator {
            private Node list;

            Itr(Node list) {
                this.list = list;
            }


            @Override
            public final boolean hasNext() {
                return list != null;
            }

            @Override
            public final int nextInt() {
                final Node list = this.list;

                if (list == null) {
                    throw new NoSuchElementException();
                }

                this.list = list.tail;
                return list.head;
            }

        }
    }


    Node first = null;
    Node last = null;

    int len = 0;

    public final void append(int value) {
        Node n = new Node(value, null);
        if (first == null) {
            first = n;
        } else {
            last.tail = n;
        }
        last = n;
        ++len;
    }

    public int[] toArray() {
        int[] arr = new int[len];

        int i = 0;
        Node node = this.first;
        while (node != null) {
            arr[i++] = node.head;
            node = node.tail;
        }
        return arr;
    }

    public final IntIterator iterator() {
        return first == null ? IntIterator.empty() : first.iterator();
    }
}
