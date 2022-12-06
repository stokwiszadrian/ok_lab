package karatsuba;

import java.math.BigInteger;

public class Karatsuba {

    public Karatsuba () {
    }

    public BigInteger calc(BigInteger num1, BigInteger num2) {
        String num1str = num1.toString();
        String num2str = num1.toString();
        if ( num1.compareTo(BigInteger.valueOf(10)) == -1 || num2.compareTo(BigInteger.valueOf(10)) == -1) {
            return num1.multiply(num2);
        }
        int m = Math.min(num1str.length(), num2str.length());
        int m2 = Math.floorDiv(m, 2);

        String high1 = num1str.substring(0, m2);
        String low1 = num1str.substring(m2);
        String high2 = num2str.substring(0, m2);
        String low2 = num2str.substring(m2);

        BigInteger z0 = calc()
    }
}
