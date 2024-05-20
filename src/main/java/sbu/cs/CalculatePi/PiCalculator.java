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
            BigDecimal kasr;

            for(int i = 0; i < Math.pow(2,7); i++) {
                kasr = new BigDecimal("1");
                BigDecimal b1 = new BigDecimal("16");
                BigDecimal b2 = new BigDecimal("2");
                b2 = b2.pow(7);
                b2 = b2.multiply(new BigDecimal(Integer.toString(k)), mc);
                b2 = b2.add(new BigDecimal(Integer.toString(i)));
                b1 = b1.pow(Integer.parseInt(b2.toString()));
                kasr = kasr.divide(b1, mc);

                BigDecimal kasr1 = new BigDecimal("4");
                kasr1 = kasr1.divide(b2.multiply(new BigDecimal("8")).add(new BigDecimal("1")), mc);
                BigDecimal kasr2 = new BigDecimal("2");
                kasr2 = kasr2.divide(b2.multiply(new BigDecimal("8")).add(new BigDecimal("4")), mc);
                BigDecimal kasr3 = new BigDecimal("1");
                kasr3 = kasr3.divide(b2.multiply(new BigDecimal("8")).add(new BigDecimal("5")), mc);
                BigDecimal kasr4 = new BigDecimal("1");
                kasr4 = kasr4.divide(b2.multiply(new BigDecimal("8")).add(new BigDecimal("6")), mc);

                BigDecimal sum_4_kasr = new BigDecimal("0");
                sum_4_kasr = sum_4_kasr.add(kasr1);
                sum_4_kasr = sum_4_kasr.subtract(kasr2);
                sum_4_kasr = sum_4_kasr.subtract(kasr3);
                sum_4_kasr = sum_4_kasr.subtract(kasr4);

                ans = ans.add(sum_4_kasr.multiply(kasr, mc));
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
