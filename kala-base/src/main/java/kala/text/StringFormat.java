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
package kala.text;

import org.jetbrains.annotations.ApiStatus;

/**
 * Flexible and high-performance string interpolator.
 *
 * @see <a href="https://github.com/Glavo/kala-common/blob/main/docs/zh/StringFormat.md">Document (Chinese)</a>
 */
@ApiStatus.Experimental
public final class StringFormat {

    private final StringFormatFactory factory;
    private final String format;

    StringFormat(StringFormatFactory factory, String format) {
        this.factory = factory;
        this.format = format;
    }

    public static String format(String format, Object... arguments) {
        return StringFormatFactory.getDefault().format(format, arguments);
    }

    public StringFormatFactory getFactory() {
        return factory;
    }

    public String format(Object... arguments) {
        return format(new StringBuilder(), arguments).toString();
    }

    public StringBuilder format(StringBuilder out, Object... arguments) {
        return factory.format(out, format, arguments); // TODO: opt
    }
}
