package com.zzw.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <a href="https://arthas.aliyun.com/doc/quick-start.html">Arthas 快速入门</a>
 */
public class MathGame
{

    private static final Random random = new Random();

    private int illegalArgumentCount = 0;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws InterruptedException
    {
        MathGame game = new MathGame();
        while (true)
        {
            game.run();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public void run()
    {
        try
        {
            int           number       = random.nextInt() / 10000;
            List<Integer> primeFactors = primeFactors(number);
            print(number, primeFactors);
        }
        catch (Exception e)
        {
            System.out.println(
                    String.format("illegalArgumentCount:%3d, ", illegalArgumentCount) + e.getMessage()
            );
        }
    }

    public static void print(int number, List<Integer> primeFactors)
    {
        StringBuffer sb = new StringBuffer(number + " = ");
        for (int factor : primeFactors)
        {
            sb.append(factor).append('*');
        }
        if (sb.charAt(sb.length() - 1) == '*')
        {
            sb.deleteCharAt(sb.length() - 1);
        }
        System.out.println(sb);
    }

    public List<Integer> primeFactors(int number)
    {
        if (number < 2)
        {
            illegalArgumentCount++;
            throw new IllegalArgumentException("number is: " + number + ", need >= 2");
        }

        List<Integer> result = new ArrayList<>();
        int           i      = 2;
        while (i <= number)
        {
            if (number % i == 0)
            {
                result.add(i);
                number /= i;
                i = 2;
            }
            else i++;
        }

        return result;
    }
}
