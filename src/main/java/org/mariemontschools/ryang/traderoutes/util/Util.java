package org.mariemontschools.ryang.traderoutes.util;

import java.awt.*;
import java.util.Random;

public class Util {
    private static final Random random = new Random(1);

    public static final Color randomColor() {
        return new Color(random.nextInt() & 0xFFFFFF);
    }

    public static String aggressiveTrim(String string) {
        return string.replace(" ", "");
    }
}
