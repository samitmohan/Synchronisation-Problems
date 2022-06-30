package PC;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PC_Using_Mutex {

    public static void main(String[] args) {
        // shared object (global)
        ProdCon sharedObject = new ProdCon();

        // creating producer and consumer threads
        Producer p = new Producer(sharedObject);
        Consumer c = new Consumer(sharedObject);

        // start
        p.start();
        c.start();
    }
}

class ProdCon {
    // producer consumer problem data
    private static final int CAPACITY = 10;
    private final Queue queue = new LinkedList<>();
    private final Random theRandom = new Random();

    // lock and condition variables
    private final Lock aLock = new ReentrantLock();
    private final Condition bufferNotFull = aLock.newCondition();
    private final Condition bufferNotEmpty = aLock.newCondition();

    // producer
    public void put() throws InterruptedException {
        aLock.lock();
        try {
            while (queue.size() == CAPACITY) {
                System.out.println(Thread.currentThread().getName() + " : Buffer is full, waiting");
                bufferNotEmpty.await();
            }

            int number = theRandom.nextInt();
            boolean isAdded = queue.offer(number);
            if (isAdded) {
                System.out.println(Thread.currentThread().getName() + " added " + number + " into queue ");
                // signal consumer thread that, buffer has element now
                System.out.println(Thread.currentThread().getName() + " : Signalling that buffer is no more empty now");
                bufferNotFull.signalAll();
            }
        } finally {
            aLock.unlock();
        }
    }

    // consumer
    public void get() throws InterruptedException {
        aLock.lock();
        try {
            while (queue.size() == 0) {
                System.out.println(Thread.currentThread().getName() + " : Buffer is empty, waiting");
                bufferNotFull.await();
            }

            Integer value = (Integer) queue.poll();
            if (value != null) {
                System.out.println(Thread.currentThread().getName() + " consumed " + value + "from queue");
                // signal producer thread that, buffer may be empty now
                System.out.println(Thread.currentThread().getName() + " : Signalling that buffer may be empty now");
                bufferNotEmpty.signalAll();
            }
        } finally {
            aLock.unlock();
        }
    }
}

// producer main class
class Producer extends Thread {
    ProdCon pc;
    public Producer(ProdCon sharedObject) {
        super("Producer");
        this.pc = sharedObject;
    }

    @Override
    public void run() {
        try {
            pc.put();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// consumer main class
class Consumer extends Thread {
    ProdCon pc;
    public Consumer(ProdCon sharedObject) {
        super("Consumer");
        this.pc = sharedObject;
    }

    @Override
    public void run() {
        try {
            pc.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}