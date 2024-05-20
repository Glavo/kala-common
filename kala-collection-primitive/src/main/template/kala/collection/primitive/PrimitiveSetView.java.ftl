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
package kala.collection.primitive;

import kala.collection.primitive.internal.view.${Type}SetViews;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.*;

public interface ${Type}SetView extends ${Type}SetLike, ${Type}CollectionView, PrimitiveSetView<${WrapperType}> {

    @Override
    default @NotNull String className() {
        return "${Type}SetView";
    }

    @Override
    default @NotNull ${Type}SetView view() {
        return this;
    }

    @Override
    default @NotNull ${Type}SetView filter(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new ${Type}SetViews.Filter(this, predicate);
    }

    @Override
    default @NotNull ${Type}SetView filterNot(@NotNull ${Type}Predicate predicate) {
        return filter(predicate.negate());
    }
}
