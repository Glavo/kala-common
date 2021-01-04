module org.glavo.kala.collection {
    requires static org.jetbrains.annotations;

    requires transitive org.glavo.kala.base;

    exports org.glavo.kala.collection;
    exports org.glavo.kala.collection.mutable;
    exports org.glavo.kala.collection.immutable;
}