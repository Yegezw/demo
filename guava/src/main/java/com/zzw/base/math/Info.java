package com.zzw.base.math;

public class Info
{
    /*
     * IntMath
     * LongMath
     * BigIntegerMath
     * DoubleMath
     */

    /*
     * IntMath                    LongMath
     * IntMath.checkedAdd         LongMath.checkedAdd
     * IntMath.checkedSubtract    LongMath.checkedSubtract
     * IntMath.checkedMultiply    LongMath.checkedMultiply
     * IntMath.checkedPow         LongMath.checkedPow
     */

    /*
     * RoundingMode
     * DOWN          向零方向舍入(去尾法)
     * UP            远离零方向舍入
     * FLOOR         向负无限大方向舍入
     * CEILING       向正无限大方向舍入
     * UNNECESSARY   不需要舍入，如果用此模式进行舍入，应直接抛出 ArithmeticException
     * HALF_UP       向最近的整数舍入，其中 x.5 远离零方向舍入
     * HALF_DOWN     向最近的整数舍入，其中 x.5 向零方向舍入
     * HALF_EVEN     向最近的整数舍入，其中 x.5 向相邻的偶数舍入
     *
     * IntMath                           LongMath                            BigIntegerMath
     * divide(int, int, RoundingMode)    divide(long, long, RoundingMode)    divide(BigInteger, BigInteger, RoundingMode)    除法
     * log2(int, RoundingMode)           log2(long, RoundingMode)            log2(BigInteger, RoundingMode)                  2  为底的对数
     * log10(int, RoundingMode)          log10(long, RoundingMode)           log10(BigInteger, RoundingMode)                 10 为底的对数
     * sqrt(int, RoundingMode)           sqrt(long, RoundingMode)            sqrt(BigInteger, RoundingMode)                  平方根
     * gcd(int, int)                     gcd(long, long)                     BigInteger.gcd(BigInteger)                      最大公约数
     * mod(int, int)                     mod(long, long)                     BigInteger.mod(BigInteger)                      取模
     * pow(int, int)                     pow(long, int)                      BigInteger.pow(int)                             取幂
     * isPowerOfTwo(int)                 isPowerOfTwo(long)                  isPowerOfTwo(BigInteger)                        是否 2 的幂
     * factorial(int)                    factorial(int)                      factorial(int)                                  阶乘
     * binomial(int, int)                binomial(int, int)                  binomial(int, int)                              二项式系数
     * BigInteger 的最大公约数、取模、取幂运算由 JDK 提供，阶乘和二项式系数的运算结果如果溢出，则返回 MAX_VALUE
     */

    /*
     * JDK 比较彻底地涵盖了浮点数运算，但 Guava 在 DoubleMath 类中也提供了一些有用的方法
     * isMathematicalInteger(double)              判断该浮点数是不是一个整数
     * roundToInt(double, RoundingMode)           舍入为int，对无限小数、溢出抛出异常
     * roundToLong(double, RoundingMode)          舍入为long，对无限小数、溢出抛出异常
     * roundToBigInteger(double, RoundingMode)    舍入为BigInteger，对无限小数抛出异常
     * log2(double, RoundingMode)                 2 的浮点对数，并且舍入为 int，比 JDK 的 Math.log(double) 更快
     */
}
