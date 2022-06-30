// Java program to implement solution of producer
// consumer problem.

import java.util.LinkedList;

public class PC_Using_Sync {
    public static void main(String[] args) throws InterruptedException {
        // Object of a class that has both produce() and consume() methods
        final PC pc = new PC();

        // Producer thread
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pc.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Consumer thread
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pc.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start both threads
        t1.start();
        t2.start();

        // edge case : t1 finishes before t2
        t1.join();
        t2.join();
    }

    // list : prod adds items to list and consumer removes items
    public static class PC {
        // Shared buffer
        LinkedList<Integer> list = new LinkedList<>();
        int capacity = 2; // size of list is 2

        // work by producer
        public void produce() throws InterruptedException {
            int value = 0;
            while (true) {
                synchronized (this) {
                    // producer thread waits while list is full
                    while (list.size() == capacity) {
                        wait(); // lock
                    }
                    // produce message and add item to list and increment the count (value)
                    System.out.println("Producer produced-" + value);
                    list.add(value++);

                    // notifies the consumer thread so it can start consuming
                    notify(); // unlock

                    // sleep to watch the process
                    Thread.sleep(1000);
                }
            }
        }

        // work by consumer
        public void consume() throws InterruptedException {
            while (true) {
                synchronized (this) {
                    // consumer thread waits while list is empty
                    while (list.size() == 0) {
                        wait();
                    }

                    // to retrieve the first job in the list
                    int val = list.removeFirst();
                    System.out.println("Consumer consumed-" + val);

                    // wake up producer thread so it can start producing again.
                    notify();

                    // sleep to watch the process
                    Thread.sleep(1000);
                }
            }
        }
    }
}