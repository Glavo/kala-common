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
package kala.collection.internal.hash;

public abstract class ChampNode<T extends ChampNode<T>> {

    public abstract boolean hasNodes();

    public abstract int nodeArity();

    public abstract T getNode(int index);

    public abstract boolean hasPayload();

    public abstract int payloadArity();

    public abstract Object getPayload(int index);

    public abstract int getHash(int index);

    public abstract int cachedJavaKeySetHashCode();
}
