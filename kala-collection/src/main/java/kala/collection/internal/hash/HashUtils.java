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

public final class HashUtils {
    private HashUtils() {
    }

    public static final int sizeMapBucketBitSize = 5;
    public static final int sizeMapBucketSize = 1 << sizeMapBucketBitSize;

    public static final int defaultLoadFactor = 750;
    public static final int loadFactorDenum = 1000;

    public static int tableSizeFor(int capacity) {
        return Integer.min(Integer.highestOneBit(Integer.max(capacity - 1, 4)) * 2, 1 << 30);
    }

    public static int improveHash(int originalHash) {
        return originalHash ^ (originalHash >>> 16);
    }

    public static int unimproveHash(int improvedHash) {
        return improveHash(improvedHash);
    }

    public static int computeHash(Object o) {
        return o == null ? 0 : improveHash(o.hashCode());
    }
}
