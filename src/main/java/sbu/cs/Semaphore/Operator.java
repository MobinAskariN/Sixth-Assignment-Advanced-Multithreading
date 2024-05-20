package sbu.cs.Semaphore;

import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;

public class Operator extends Thread {

    private Semaphore sem;

    public Operator(String name, Semaphore sem) {
        super(name);
        this.sem = sem;
    }
    @Override
    public void run() {
        try {
            sem.acquire();
            System.out.println(currentThread().getName() + " " + LocalDateTime.now());
            for (int i = 0; i < 10; i++)
            {
                Resource.accessResource();         // critical section - a Maximum of 2 operators can access the resource concurrently
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sem.release();
    }
}
