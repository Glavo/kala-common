package asia.kala.io;

import asia.kala.annotations.StaticClass;

@StaticClass
public final class AnsiColors {

    /**
     * Foreground color for ANSI black
     */
    public static final String BLACK = "\u001b[30m";

    /**
     * Foreground color for ANSI red
     */
    public static final String RED = "\u001b[31m";

    /**
     * Foreground color for ANSI green
     */
    public static final String GREEN = "\u001b[32m";

    /**
     * Foreground color for ANSI yellow
     */
    public static final String YELLOW = "\u001b[33m";

    /**
     * Foreground color for ANSI blue
     */
    public static final String BLUE = "\u001b[34m";

    /**
     * Foreground color for ANSI magenta
     */
    public static final String MAGENTA = "\u001b[35m";

    /**
     * Foreground color for ANSI cyan
     */
    public static final String CYAN = "\u001b[36m";

    /**
     * Foreground color for ANSI white
     */
    public static final String WHITE = "\u001b[37m";

    /**
     * Background color for ANSI black
     */
    public static final String BLACK_B = "\u001b[40m";

    /**
     * Background color for ANSI red
     */
    public static final String RED_B = "\u001b[41m";

    /**
     * Background color for ANSI green
     */
    public static final String GREEN_B = "\u001b[42m";

    /**
     * Background color for ANSI yellow
     */
    public static final String YELLOW_B = "\u001b[43m";

    /**
     * Background color for ANSI blue
     */
    public static final String BLUE_B = "\u001b[44m";

    /**
     * Background color for ANSI magenta
     */
    public static final String MAGENTA_B = "\u001b[45m";

    /**
     * Background color for ANSI cyan
     */
    public static final String CYAN_B = "\u001b[46m";

    /**
     * Background color for ANSI white
     */
    public static final String WHITE_B = "\u001b[47m";

    /**
     * Reset ANSI styles
     */
    public static final String RESET = "\u001b[0m";
    /**
     * ANSI bold
     */
    public static final String BOLD = "\u001b[1m";
    /**
     * ANSI underlines
     */
    public static final String UNDERLINED = "\u001b[4m";

    /**
     * ANSI blink
     */
    public static final String BLINK = "\u001b[5m";

    /**
     * ANSI reversed
     */
    public static final String REVERSED = "\u001b[7m";

    /**
     * ANSI invisible
     */
    public static final String INVISIBLE = "\u001b[8m";

    private AnsiColors() {
    }
}
