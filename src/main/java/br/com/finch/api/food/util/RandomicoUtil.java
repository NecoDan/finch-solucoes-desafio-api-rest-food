package br.com.finch.api.food.util;

import java.math.BigDecimal;
import java.util.Random;

public class RandomicoUtil {

    private static int gerarValorRandomico() {
        int min = 1;
        int max = 5000;

        Random random = new Random();
        return min + random.nextInt(max);
    }

    public static Long gerarValorRandomicoLong() {
        return (long) gerarValorRandomico();
    }

    public static Integer gerarValorRandomicoInteger() {
        return gerarValorRandomico();
    }

    public static BigDecimal gerarValorRandomicoDecimal() {
        double leftLimit = 1D;
        double rightLimit = 1000D;

        return BigDecimal.valueOf(leftLimit + new Random().nextDouble() * (rightLimit - leftLimit)).setScale(2, BigDecimal.ROUND_UP);
    }
}
