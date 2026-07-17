package org.example;

public class DeadLockDemo {

    static final Object LOCK_A = new Object();
    static final Object LOCK_B = new Object();

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            synchronized (LOCK_A) {
                System.out.println("Thread1 acquired LOCK_A");
                try { Thread.sleep(100); } catch (Exception e) {}

                synchronized (LOCK_B) {                          // waits for LOCK_B
                    System.out.println("Thread1 acquired LOCK_B");
                }
            }
        });

        Runnable thread2 = (() -> {
            synchronized (LOCK_B) {
                System.out.println("Thread2 acquired LOCK_B");
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }

                synchronized (LOCK_A) {                          // waits for LOCK_A
                    System.out.println("Thread2 acquired LOCK_A");
                }
            }
        });

        thread1.start();
        thread2.run();
        // Program hangs here — deadlock!

        System.out.println("Main thread finished");
    }

}
