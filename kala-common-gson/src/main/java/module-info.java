module kala.common.gson {
    requires com.google.gson;
    requires static kala.base;
    requires static kala.collection;

    exports kala.gson.base;
    exports kala.gson.collection;
}