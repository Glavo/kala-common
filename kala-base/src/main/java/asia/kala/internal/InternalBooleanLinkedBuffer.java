package asia.kala.internal;

import asia.kala.iterator.AbstractBooleanIterator;
import asia.kala.iterator.BooleanIterator;
import org.jetbrains.annotations.ApiStatus;

import java.util.NoSuchElementException;

@ApiStatus.Internal
public class InternalBooleanLinkedBuffer {
    static final class Node {
        final boolean head;
        Node tail;

        public Node(boolean head, Node tail) {
            this.head = head;
            this.tail = tail;
        }

        public final BooleanIterator iterator() {
            return new Itr(this);
        }

        static final class Itr extends AbstractBooleanIterator {
            private Node list;

            Itr(Node list) {
                this.list = list;
            }


            @Override
            public final boolean hasNext() {
                return list != null;
            }

            @Override
            public final boolean nextBoolean() {
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

    public final void append(boolean value) {
        Node n = new Node(value, null);
        if (first == null) {
            first = n;
        } else {
            last.tail = n;
        }
        last = n;
        ++len;
    }

    public boolean[] toArray() {
        boolean[] arr = new boolean[len];

        int i = 0;
        Node node = this.first;
        while (node != null) {
            arr[i++] = node.head;
            node = node.tail;
        }
        return arr;
    }

    public final BooleanIterator iterator() {
        return first == null ? BooleanIterator.empty() : first.iterator();
    }
}
