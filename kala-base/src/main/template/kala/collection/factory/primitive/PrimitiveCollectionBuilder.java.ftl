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
package kala.collection.factory.primitive;

import kala.annotations.ReplaceWith;
import kala.collection.base.primitive.${Type}Growable;
import kala.collection.factory.CollectionBuilder;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface ${Type}CollectionBuilder<R> extends CollectionBuilder<${WrapperType}, R>, ${Type}Growable {

    @Override
    void plusAssign(${PrimitiveType} value);

    @Override
    R build();

    @Override
    @Deprecated
    @ReplaceWith("plusAssign(${PrimitiveType})")
    default void plusAssign(@NotNull ${WrapperType} value) {
        plusAssign(value.${PrimitiveType}Value());
    }
}
