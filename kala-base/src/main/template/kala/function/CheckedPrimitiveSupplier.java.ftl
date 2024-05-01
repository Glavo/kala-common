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
package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
<#if IsSpecialized || Type == "Boolean">

import java.util.function.${Type}Supplier;
</#if>

@FunctionalInterface
public interface Checked${Type}Supplier<Ex extends Throwable> extends ${Type}Supplier {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <Ex extends Throwable> Checked${Type}Supplier<Ex> of(Checked${Type}Supplier<? extends Ex> consumer) {
        return (Checked${Type}Supplier<Ex>) consumer;
    }

    ${PrimitiveType} getAs${Type}Checked();

    default ${PrimitiveType} getAs${Type}() {
        try {
            return getAs${Type}Checked();
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }

        throw new AssertionError();
    }
}
