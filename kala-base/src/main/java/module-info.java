module kala.base {
    requires static org.jetbrains.annotations;

    exports kala;
    exports kala.annotations;
    exports kala.comparator;
    exports kala.comparator.primitive;
    exports kala.control;
    exports kala.control.primitive;
    exports kala.concurrent;
    exports kala.function;
    exports kala.io;
    exports kala.tuple;
    exports kala.tuple.primitive;
    exports kala.collection.base;
    exports kala.collection.base.primitive;
    exports kala.collection.factory;
    exports kala.collection.factory.primitive;
    exports kala.value;
    exports kala.value.primitive;
    exports kala.reflect;
    exports kala.range;
    exports kala.range.primitive;

    exports kala.internal to kala.collection, kala.collection.primitive, kala.jackson;
}