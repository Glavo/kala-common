/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    exports kala.text;

    exports kala.internal to kala.collection, kala.collection.primitive;
}