package karatsuba;

import java.math.BigInteger;

public class Karatsuba {

    public static BigInteger calc(BigInteger x, BigInteger y) {

        // Użycie domyślnego mnożenia dla mniejszych liczb
        int N = Math.max(x.bitLength(), y.bitLength());
        if (N <= 20) return x.multiply(y);

        // Liczba bitów podzielona na 2, zaokrąglona
        N = (N / 2) + (N % 2);

        // x = low1 + 2^N high1,   y = low2 + 2^N high2
        BigInteger high1 = x.shiftRight(N);
        BigInteger low1 = x.subtract(high1.shiftLeft(N));
        BigInteger high2 = y.shiftRight(N);
        BigInteger low2 = y.subtract(high2.shiftLeft(N));

        // Obliczenia rekursywne
        BigInteger ac    = calc(low1, low2);
        BigInteger bd    = calc(high1, high2);
        BigInteger abcd  = calc(low1.add(high1), low2.add(high2));

        return ac.add(abcd.subtract(ac).subtract(bd).shiftLeft(N)).add(bd.shiftLeft(2*N));
    }
}

