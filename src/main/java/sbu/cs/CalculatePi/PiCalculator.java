package sbu.cs.CalculatePi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PiCalculator{

    public static class Calculate_segment implements Runnable{

        private int k;
        public BigDecimal ans;
        private MathContext mc;
        public Calculate_segment(int k, int floating_point){
            this.k = k;
            mc = new MathContext(floating_point);
        }
        @Override
        public void run() {
            ans = new BigDecimal("0");
            BigDecimal taghsim;

            for(int i = 2; i < Math.pow(2,15); i+=2) {
                taghsim = new BigDecimal("4");
                taghsim = taghsim.divide(new BigDecimal(Integer.toString((int) ((Math.pow(2,16) * k + i)
                        * (Math.pow(2,16) * k + i+1) * (Math.pow(2,16) * k + i+2)))), mc);
                if((i/2)%2 == 1)
                    ans = ans.add(taghsim);
                else
                    ans = ans.subtract(taghsim);
            }

        }

    }

    public String calculate(int floatingPoint)
    {
        long startTime = System.currentTimeMillis();

        MathContext mc = new MathContext(floatingPoint + 2);
        BigDecimal sum = new BigDecimal("0");


        Calculate_segment c1 = new Calculate_segment(0, floatingPoint + 2);
        Calculate_segment c2 = new Calculate_segment(1, floatingPoint + 2);
        Calculate_segment c3 = new Calculate_segment(2, floatingPoint + 2);
        Calculate_segment c4 = new Calculate_segment(3, floatingPoint + 2);
        Calculate_segment c5 = new Calculate_segment(4, floatingPoint + 2);
        Calculate_segment c6 = new Calculate_segment(5, floatingPoint + 2);
        Calculate_segment c7 = new Calculate_segment(6, floatingPoint + 2);
        Calculate_segment c8 = new Calculate_segment(7, floatingPoint + 2);


        ExecutorService pool = Executors.newFixedThreadPool(4);

        pool.execute(c1);
        pool.execute(c2);
        pool.execute(c3);
        pool.execute(c4);
        pool.execute(c5);
        pool.execute(c6);
        pool.execute(c7);
        pool.execute(c8);

        pool.shutdown();
        try {
            if (!pool.awaitTermination(3, TimeUnit.MINUTES)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(3, TimeUnit.MINUTES))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }

        sum = sum.add(c1.ans);
        sum = sum.add(c2.ans);
        sum = sum.add(c3.ans);
        sum = sum.add(c4.ans);
        sum = sum.add(c5.ans);
        sum = sum.add(c6.ans);
        sum = sum.add(c7.ans);
        sum = sum.add(c8.ans);
        sum = sum.add(new BigDecimal("3"));
        sum = sum.setScale(floatingPoint, RoundingMode.FLOOR);

        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);


        return sum.toString();
    }

    public static synchronized void add_to_sum(BigDecimal value){

    }

    public static void main(String[] args) {
        PiCalculator p = new PiCalculator();
        System.out.println(p.calculate(1000));
    }

}
