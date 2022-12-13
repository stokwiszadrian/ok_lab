package karatsuba;

import java.math.BigInteger;

public class Karatsuba {

    public static String[] strCopy(long index, String string) {
        StringBuilder first = new StringBuilder();
        StringBuilder last = new StringBuilder();
        long actualIndex = string.length() - index;
        for (int i = 0; i<actualIndex; i++) {
            first.append(string.charAt(i));
        }
        for (int i = (int)actualIndex; i<string.length(); i++) {
            last.append(string.charAt(i));
        }
        return new String[] {first.toString(), last.toString()};
    }
    public static BigInteger calc(BigInteger num1, BigInteger num2) {
        if (num1.compareTo(BigInteger.valueOf(10)) < 0 || num2.compareTo(BigInteger.valueOf(10)) < 0) {
            return num1.multiply(num2);
        }

        String num1str = num1.toString();
        String num2str = num2.toString();

        int m = Math.max(num1str.length(), num2str.length()) ;
        int m2 = m / 2;
        System.out.println(num1 + "  " + num2);
        System.out.println("DIVISIONS");
        System.out.println(strCopy(m2, num1str)[0] + "  " + strCopy(m2, num1str)[1] + "  " + strCopy(m2, num2str)[0] + "  " + strCopy(m2, num2str)[1]);
        BigInteger high1 = new BigInteger(strCopy(m2, num1str)[0]);
        BigInteger low1 = new BigInteger(strCopy(m2, num1str)[1]);
        BigInteger high2 = new BigInteger(strCopy(m2, num2str)[0]);
        BigInteger low2 = new BigInteger(strCopy(m2, num2str)[1]);


        BigInteger z0 = calc(low1, low2);
        BigInteger z2 = calc(high1, high2);
        BigInteger z1 = calc(low1.add(high1), low2.add(high2)).subtract(z2).subtract(z0);

        BigInteger z2multi = BigInteger.valueOf((long)Math.pow(10, 2 * m2));
        BigInteger z1multi = BigInteger.valueOf((long) Math.pow(10, m2));

        return ((z2.multiply(z2multi)).add(z1.multiply(z1multi))).add(z0);
    }
}

