package org.glavo.kala.collection.immutable;

import org.glavo.kala.tuple.Tuple;
import org.glavo.kala.tuple.Tuple2;
import org.glavo.kala.collection.CollectionTestTemplate;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SuppressWarnings({"unchecked", "rawtypes"})
public interface ImmutableCollectionTestTemplate extends CollectionTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends ImmutableCollection<? extends E>> factory();


}
