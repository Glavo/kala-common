/*
 * Copyright 2025 Glavo
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
package kala.collection.internal.champ;

import kala.tuple.Tuple2;

public class ChampMapKeyValueTupleIterator<K, V> extends ChampBaseIterator<Tuple2<K, V>, ChampMapNode<K, V>> {

    public ChampMapKeyValueTupleIterator(ChampMapNode<K, V> rootNode) {
        super(rootNode);
    }

    @Override
    public Tuple2<K, V> next() {
        checkStatus();
        var payload = currentValueNode.getPayload(currentValueCursor);
        currentValueCursor += 1;
        return payload;
    }
}
