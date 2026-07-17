package org.example;
class Resource {
    private final String name;

    public Resource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class DeadlockExample implements Runnable {
    private Resource resource1;
    private Resource resource2;

    public DeadlockExample(Resource resource1, Resource resource2) {
        this.resource1 = resource1;
        this.resource2 = resource2;
    }


    @Override
    public void run() {
        for(int i=0; i < 5; i++) {
                synchronized (resource1) {
                    System.out.println(Thread.currentThread().getName() + " locked " + resource1.getName());
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                synchronized (resource2) {
                    System.out.println(Thread.currentThread().getName() + " locked " + resource2.getName());
                }

        }
    }
//    @Override
//    public void run() {
//        for(int i=0; i < 5; i++) {
//            synchronized (resource1) {
//                System.out.println(Thread.currentThread().getName() + " locked " + resource1.getName());
//
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                }
//
//                synchronized (resource2) {
//                    System.out.println(Thread.currentThread().getName() + " locked " + resource2.getName());
//                }
//            }
//        }
//    }

    public static void main(String[] args) {
        Resource resA = new Resource("Resource A");
        Resource resB = new Resource("Resource B");

        Thread thread1 = new Thread(new DeadlockExample(resA, resB), "Thread-1");
        Thread thread2 = new Thread(new DeadlockExample(resB, resA), "Thread-2");

        thread1.start();
        thread2.start();
    }
}
