module kala.common.jackson {
    requires static com.fasterxml.jackson.databind;

    requires static kala.base;
    requires static kala.collection;

    exports kala.jackson;
}