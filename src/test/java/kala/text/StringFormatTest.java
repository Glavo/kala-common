package kala.text;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StringFormatTest {
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
        assertThrows(StringFormatException.class, () -> StringFormat.format("{::any}", "str0"));
        assertThrows(StringFormatException.class, () -> StringFormat.format("{:array:any}", (Object) new int[0]));
    }
}
