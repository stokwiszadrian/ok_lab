package karatsuba;

import java.math.BigInteger;

public class Karatsuba {

    public Karatsuba() {
    }

    // Takes two integers and returns the maximum of them
    public static int max(int x, int y) {
        return (x > y) ? x : y;
    }

    public static String[] strCopy(long index, String string) {
        String first = "",
                last = "";
        long actualIndex = string.length() - index;
        for (int i = 0; i < actualIndex; i++) {
            first += string.charAt(i);
        }
        for (int i = (int) actualIndex; i < string.length(); i++) {
            last += string.charAt(i);
        }
        return new String[]{first, last};
    }
    public static long power(long x, long y) {
        if (y == 0)
            return 1;
        else {
            long answer = 1;
            for (int i = 1; i <= y; i++) {
                answer *= x;
            }
            return answer;
        }
    }

    public static long karatsuba(long x, long y) {
        // Base case: single digit multiplication
        if (x < 10 || y < 10) {
            return x * y;
        }
        // Recursive case: Decompose the problem by splitting the integers and applying the algorithm on the parts.
        else {
            // Convert the numbers to strings so we can compute the # of digits of each number.
            // Note: We could also use floor(log10(n) + 1) to compute the #digits, but remember that we need to split the numbers too.
            String xString = Integer.toString((int) x);
            String yString = Integer.toString((int) y);
            // Local variables
            long m = max(xString.length(), yString.length()), // the maximum # of digits
                    m2 = m / 2, // the middle; if the number is odd, it will floor the fraction
                    high1 = Integer.parseInt(strCopy(m2, xString)[0]), // the most significant digits. this is the scalar multiplier for b^m2
                    low1 = Integer.parseInt(strCopy(m2, xString)[1]), // the least significant digits. this is what is added on to high1*b^m2
                    high2 = Integer.parseInt(strCopy(m2, yString)[0]), // same for y
                    low2 = Integer.parseInt(strCopy(m2, yString)[1]), // same for y
                    // Three recursive calls
                    z0 = karatsuba(low1, low2), // z0 = x0y0
                    z2 = karatsuba(high1, high2), // z2 = x1y1
                    z1 = karatsuba((low1 + high1), (low2 + high2)) - z2 - z0; // z1 = (x0 + y1)*(x1 + y0) - z2 - z0, courtesy of Karatsuba

            return (z2 * power(10, 2 * m2) + (z1 * power(10, m2)) + z0);
        }
    }

    public long calc(long num1, long num2) {
        if (num1 < 10 || num2 < 10) {
            return num1 * num2;
        }

        String num1str = Long.toString(num1);
        String num2str = Long.toString(num2);

        int m = Math.max(num1str.length(), num2str.length());
        int m2 = m / 2;


        System.out.println("NUM STRINGS: " + num1 + ":" + num1str + "  " + num2 + ":" + num2str);

        long high1 = Long.parseLong(num1str.substring(0, m2));
        long low1 = Long.parseLong(num1str.substring(m2));
        long high2 = Long.parseLong(num2str.substring(0, m2));
        long low2 = Long.parseLong(num2str.substring(m2));

        long z0 = calc(low1, low2);
        long z2 = calc(high1, high2);
        long z1 = calc((low1 + high1), (low2 + high2)) - z2 - z0;
        System.out.println("X: " + z0 + ", Y: " + z1 + ", Z: " + z2);

        return (long) ((z2 * Math.pow(10, 2 * m2)) + (z1 * Math.pow(10, m2)) + z0);
    }
//    public BigInteger calc(BigInteger num1, BigInteger num2) {
//        String num1str = num1.toString();
//        String num2str = num2.toString();
//        if (num1.compareTo(BigInteger.valueOf(10)) < 0 || num2.compareTo(BigInteger.valueOf(10)) < 0) {
//            return num1.multiply(num2);
//        }
//        int m = Math.max(num1str.length(), num2str.length()) + 1;
//        int m2 = Math.floorDiv(m, 2);
//
//
//        BigInteger high1 = new BigInteger(num1str.substring(0, m2));
//        BigInteger low1 = new BigInteger(num1str.substring(m2));
//        BigInteger high2 = new BigInteger(num2str.substring(0, m2));
//        BigInteger low2 = new BigInteger(num2str.substring(m2));
//
//        System.out.println(num1str);
//        System.out.println(high1 + "" + low1 + "\n");
//        System.out.println(num2str);
//        System.out.println(high2 + "" + low2);
//        System.out.println("END");
//
//        BigInteger z0 = calc(low1, low2);
//        BigInteger z1 = calc(low1.add(high1), low2.add(high2));
//        BigInteger z2 = calc(high1, high2);
//        System.out.println("X: " + z0 + ", Y: " + z1 + ", Z: " + z2);
//
//        z2.shiftLeft()
//
//        return z2.multiply(BigInteger.valueOf((long) Math.pow(10, m2 * 2)))
//                .add((z1.subtract(z2).subtract(z0)).multiply(BigInteger.valueOf((long) Math.pow(10, m2))))
//                .add(z0);

//        String num1str = num1.toString();
//        String num2str = num2.toString();
//        if ( num1.compareTo(BigInteger.valueOf(10)) < 0 || num2.compareTo(BigInteger.valueOf(10)) < 0) {
//            return num1.multiply(num2);
//        }
//
//        int n = Math.max(num1str.length(), num2str.length());
//        int m = n / 2;
//
//        BigInteger high1 = new BigInteger(num1str.substring(0, m));
//        BigInteger low1 = new BigInteger(num1str.substring(m));
//        BigInteger high2 = new BigInteger(num2str.substring(0, m));
//        BigInteger low2 = new BigInteger(num2str.substring(m));
//
//        System.out.println(num1str);
//        System.out.println(high1 + "" + low1 + "\n");
//        System.out.println(num2str);
//        System.out.println(high2 + "" + low2);
//        System.out.println("END");
//
//        BigInteger X = calc(high1, high2);
//        BigInteger Y = calc(low1, low2);
//        BigInteger Z = calc(low1.add(high1), low2.add(high2)).subtract(X).subtract(Y);
//        System.out.println("X: " + X + ", Y: " + Y + ", Z: " + Z);
//
//        return X.multiply(BigInteger.valueOf((long) Math.pow(10, 2 * m)))
//                .add(Z.multiply(BigInteger.valueOf((long) Math.pow(10, m))))
//                .add(Y);
}

