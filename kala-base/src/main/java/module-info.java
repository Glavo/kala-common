module org.glavo.kala.base {
    requires static org.jetbrains.annotations;

    exports org.glavo.kala;
    exports org.glavo.kala.annotations;
    exports org.glavo.kala.comparator;
    exports org.glavo.kala.comparator.primitive;
    exports org.glavo.kala.control;
    exports org.glavo.kala.control.primitive;
    exports org.glavo.kala.function;
    exports org.glavo.kala.io;
    exports org.glavo.kala.tuple;
    exports org.glavo.kala.tuple.primitive;
    exports org.glavo.kala.collection.base;
    exports org.glavo.kala.collection.base.primitive;
    exports org.glavo.kala.collection.factory;
    exports org.glavo.kala.value;
    exports org.glavo.kala.value.primitive;

    exports org.glavo.kala.internal to org.glavo.kala.collection;
}