module kala.collection {
    requires static org.jetbrains.annotations;

    requires transitive kala.base;

    exports kala.collection;
    exports kala.collection.mutable;
    exports kala.collection.immutable;
    exports kala.collection.internal;
    exports kala.collection.internal.tree;
}