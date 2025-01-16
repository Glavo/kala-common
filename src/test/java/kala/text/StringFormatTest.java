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

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public final class StringFormatTest {

    @Test
    void test() {
        assertEquals("", StringFormat.format(""));
        assertEquals("", StringFormat.format("", "str0"));

        assertEquals("str0", StringFormat.format("str0"));
        assertEquals("str0", StringFormat.format("{}", "str0"));
        assertEquals("null", StringFormat.format("{}", new Object[]{null}));
        assertEquals(" str0 ", StringFormat.format(" {} ", "str0"));


        assertEquals("str0 : str1", StringFormat.format("{} : {}", "str0", "str1"));
        assertEquals("str0 : str1", StringFormat.format("{0} : {1}", "str0", "str1"));
        assertEquals("str0 : str0", StringFormat.format("{0} : {0}", "str0", "str1"));
        assertEquals("str1 : str0", StringFormat.format("{1} : {0}", "str0", "str1"));
        assertEquals("str1 : str0 : str1 : str0", StringFormat.format("{1} : {} : {} : {0}", "str0", "str1"));

        assertEquals("", StringFormat.format("{''}"));
        assertEquals("{", StringFormat.format("{'{'}"));
        assertEquals("}", StringFormat.format("{'}'}"));
        assertEquals("{}", StringFormat.format("{'{}'}"));
        assertEquals("{0} str0", StringFormat.format("{'{0}'} {}", "str0"));

        assertEquals("str0", StringFormat.format("{:}", "str0"));
        assertEquals("str0", StringFormat.format("{::}", "str0"));
        assertEquals("str0", StringFormat.format("{0::}", "str0"));
        assertEquals("str1", StringFormat.format("{1::}", "str0", "str1"));
        assertEquals("str0 : str1", StringFormat.format("{} : {::}", "str0", "str1"));

        assertEquals("[]", StringFormat.format("{:array}", (Object) new int[] {}));
        assertEquals("[0, 1, 2]", StringFormat.format("{:array}", (Object) new int[] {0, 1, 2}));

        assertThrows(StringFormatException.class, () -> StringFormat.format("{"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("str0 {"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("str0{str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{1}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{2}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{foo}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{'"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{'}"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{'foo"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{'foo}"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:any:}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{::any}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:array:any}", (Object) new int[0]));
    }

    @Test
    void substringTest() {
        assertEquals("str0", StringFormat.format("{:substring}", "str0"));
        assertEquals("str0", StringFormat.format("{:substring:}", "str0"));
        assertEquals("str0", StringFormat.format("{:substring:,}", "str0"));
        assertEquals("str0", StringFormat.format("{:substring:0,}", "str0"));
        assertEquals("str0", StringFormat.format("{:substring:0,4}", "str0"));

        assertEquals("tr0", StringFormat.format("{:substring:1}", "str0"));
        assertEquals("tr0", StringFormat.format("{:substring:1,}", "str0"));

        assertEquals("str", StringFormat.format("{:substring:,3}", "str0"));
        assertEquals("str", StringFormat.format("{:substring:0,3}", "str0"));

        assertEquals("tr", StringFormat.format("{:substring:1,3}", "str0"));
        assertEquals("", StringFormat.format("{:substring:0,0}", "str0"));
        assertEquals("", StringFormat.format("{:substring:1,1}", "str0"));
        assertEquals("", StringFormat.format("{:substring:2,2}", "str0"));

        assertThrows(StringFormatException.class, () -> StringFormat.format("{:substring: }", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:substring:zzz}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:substring:, }", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:substring:zzz,}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:substring:zzz,zzz}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:substring:-1}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:substring:-1,}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:substring:,-1}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:substring:-1,-1}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:substring:5}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:substring:1,0}", "str0"));
    }
}
