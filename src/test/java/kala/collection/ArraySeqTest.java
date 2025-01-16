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
package kala.collection;

import kala.collection.factory.CollectionFactory;

public final class ArraySeqTest implements SeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ? extends ArraySeq<E>> factory() {
        return ArraySeq.factory();
    }

    static final class ViewTest implements SeqViewTestTemplate {

        @Override
        public <E> SeqView<E> of(E... elements) {
            return ArraySeq.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(E[] elements) {
            return ArraySeq.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(Iterable<? extends E> elements) {
            return ArraySeq.<E>from(elements).view();
        }
    }
}
