package kala.collection.internal.hash;

public abstract class ChampNode<N extends ChampNode<N>> {
    public abstract boolean hasNodes();

    public abstract int nodeArity();

    public abstract N getNode(int index);

    public abstract boolean hasPayload();

    public abstract int payloadArity();

    public abstract Object getPayload();

    public abstract int getHash(int index);

    public abstract int cachedJavaKeySetHashCode();
}
