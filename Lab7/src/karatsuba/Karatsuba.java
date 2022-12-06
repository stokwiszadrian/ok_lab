package karatsuba;

import java.math.BigInteger;

public class Karatsuba {

    public static String[] strCopy(long index, String string) {
        String	first = "",
                last = "";
        long actualIndex = string.length() - index;
        for (int i = 0; i<actualIndex; i++) {
            first+=string.charAt(i);
        }
        for (int i = (int)actualIndex; i<string.length(); i++) {
            last+=string.charAt(i);
        }
        return new String[] {first, last};
    }
    public static long calc(long num1, long num2) {
        if (num1 < 10 || num2 < 10) {
            return num1 * num2;
        }

        String num1str = Long.toString(num1);
        String num2str = Long.toString(num2);

        int m = Math.max(num1str.length(), num2str.length()) ;
        int m2 = m / 2;

        long high1 = Long.parseLong(strCopy(m2, num1str)[0]);
        long low1 = Long.parseLong(strCopy(m2, num1str)[1]);
        long high2 = Long.parseLong(strCopy(m2, num2str)[0]);
        long low2 = Long.parseLong(strCopy(m2, num2str)[1]);


        long z0 = calc(low1, low2);
        long z2 = calc(high1, high2);
        long z1 = calc((low1 + high1), (low2 + high2)) - z2 - z0;

        return (long) ((z2 * Math.pow(10, 2 * m2)) + (z1 * Math.pow(10, m2)) + z0);
    }
}

