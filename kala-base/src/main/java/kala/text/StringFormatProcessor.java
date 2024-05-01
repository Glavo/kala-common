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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@FunctionalInterface
@ApiStatus.Experimental
public interface StringFormatProcessor {
    StringFormatProcessor DEFAULT = (factory, out, value, style) -> {
        if (!style.isEmpty()) {
            throw new StringFormatException("Unknown style: " + style);
        }
        out.append(value);
    };

    StringFormatProcessor PRINTF = (factory, out, value, style) -> out.append(String.format(factory.getLocale(), style, value));

    StringFormatProcessor ARRAY = (factory, out, value, style) -> {
        if (!style.isEmpty()) {
            throw new StringFormatException("Unknown style: " + style);
        }
        new StringAppender(out).append(value);
    };

    StringFormatProcessor DATE = (factory, out, value, style) -> {
        if (!(value instanceof Date)) {
            throw new StringFormatException(value + " is not date");
        }

        DateFormat format;

        switch (style) {
            case "":
            case "medium":
                format = DateFormat.getDateInstance(DateFormat.DEFAULT, factory.getLocale());
                break;
            case "short":
                format = DateFormat.getDateInstance(DateFormat.SHORT, factory.getLocale());
                break;
            case "long":
                format = DateFormat.getDateInstance(DateFormat.LONG, factory.getLocale());
                break;
            case "full":
                format = DateFormat.getDateInstance(DateFormat.FULL, factory.getLocale());
                break;
            default:
                format = new SimpleDateFormat(style, factory.getLocale());
                break;
        }

        out.append(format.format(value));
    };

    StringFormatProcessor UPPER = (factory, out, value, style) -> {
        Locale locale;
        if (style.isEmpty()) {
            locale = factory.getLocale();
        } else {
            locale = Locale.forLanguageTag(style);
        }

        out.append(value.toString().toUpperCase(locale));
    };

    StringFormatProcessor LOWER = (factory, out, value, style) -> {
        Locale locale;
        if (style.isEmpty()) {
            locale = factory.getLocale();
        } else {
            locale = Locale.forLanguageTag(style);
        }

        out.append(value.toString().toLowerCase(locale));
    };

    StringFormatProcessor SUBSTRING = (factory, out, value, style) -> {
        String str = value.toString();

        if (style.isEmpty()) {
            out.append(str);
            return;
        }

        int beginIndex;
        int endIndex;

        int comma = style.indexOf(',');
        if (comma < 0) {
            beginIndex = Integer.parseInt(style);
            endIndex = str.length();
        } else {
            beginIndex = comma == 0 ? 0 : Integer.parseInt(style.substring(0, comma));
            endIndex = comma == style.length() - 1 ? str.length() : Integer.parseInt(style.substring(comma + 1));
        }

        out.append(str, beginIndex, endIndex);
    };

    StringFormatProcessor TRIM = (factory, out, value, style) -> out.append(value.toString().trim());

    default boolean processNull() {
        return false;
    }

    void accept(StringFormatFactory factory, StringBuilder out, Object value, String style) throws StringFormatException;
}
