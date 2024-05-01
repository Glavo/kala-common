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
package kala.value.primitive;

import java.io.Serial;
import java.io.Serializable;

public final class Volatile${Type}Var extends Abstract${Type}Value implements Mutable${Type}Value, Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    public volatile ${PrimitiveType} value;

    public Volatile${Type}Var() {
    }

    public Volatile${Type}Var(${PrimitiveType} value) {
        this.value = value;
    }

    @Override
    public ${PrimitiveType} get() {
        return value;
    }

    @Override
    public void set(${PrimitiveType} value) {
        this.value = value;
    }

<#if IsIntegral>
    public void increment() {
        value++;
    }

    public void decrement() {
        value--;
    }

</#if>
    @Override
    public String toString() {
        return "Volatile${Type}Var[" + value + "]";
    }
}
