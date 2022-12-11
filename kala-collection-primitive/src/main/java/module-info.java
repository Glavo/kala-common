module kala.collection.primitive {
    requires static org.jetbrains.annotations;
    requires transitive kala.collection;

    exports kala.collection.primitive;
    exports kala.collection.immutable.primitive;
    exports kala.collection.mutable.primitive;
}