package kala.collection.internal.hash;

@SuppressWarnings("unchecked")
public abstract class ChampIteratorBase<N extends ChampNode<N>> {
    public static final int HashCodeLength = 32;

    public static final int BitPartitionSize = 5;

    public static final int BitPartitionMask = (1 << BitPartitionSize) - 1;

    public static final int MaxDepth = (int) Math.ceil((double) HashCodeLength / BitPartitionSize);

    public static final int BranchingFactor = 1 << BitPartitionSize;

    protected int currentValueCursor;
    protected int currentValueLength;
    protected N currentValueNode;

    private int currentStackLevel = -1;
    private int[] nodeCursorsAndLengths;
    private N[] nodes;

    public ChampIteratorBase(N rootNode) {
        if (rootNode.hasNodes()) {
            pushNode(rootNode);
        }

        if (rootNode.hasPayload()) {
            setupPayloadNode(rootNode);
        }
    }

    private void initNodes() {
        if (nodeCursorsAndLengths == null) {
            nodeCursorsAndLengths = new int[MaxDepth * 2];
            nodes = (N[]) new ChampNode<?>[MaxDepth];
        }
    }

    private void setupPayloadNode(N node) {
        currentValueNode = node;
        currentValueCursor = 0;
        currentValueLength = node.payloadArity();
    }

    private void pushNode(N node) {
        initNodes();
        currentStackLevel++;

        int cursorIndex = currentStackLevel * 2;
        int lengthIndex = currentStackLevel * 2 + 1;

        nodes[currentStackLevel] = node;
        nodeCursorsAndLengths[cursorIndex] = 0;
        nodeCursorsAndLengths[lengthIndex] = node.nodeArity();
    }

    private void popNode() {
        currentStackLevel = currentStackLevel - 1;
    }

    private boolean searchNextValueNode() {
        while (currentStackLevel >= 0) {
            int cursorIndex = currentStackLevel * 2;
            int lengthIndex = currentStackLevel * 2 + 1;

            int nodeCursor = nodeCursorsAndLengths[cursorIndex];
            int nodeLength = nodeCursorsAndLengths[lengthIndex];

            if (nodeCursor < nodeLength) {
                nodeCursorsAndLengths[cursorIndex]++;

                N nextNode = nodes[currentStackLevel].getNode(nodeCursor);

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

    public final boolean hasNext() {
        return (currentValueCursor < currentValueLength) || searchNextValueNode();
    }
}
