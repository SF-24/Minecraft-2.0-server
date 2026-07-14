package net.mineshaft.util;

import java.util.Random;

public class RandomUtil {

    public static int range(Random rand, int lower, int higher) {
        return rand.nextInt(higher - lower + 1) + lower;
    }
}
