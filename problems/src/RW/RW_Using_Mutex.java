import java.util.concurrent.Semaphore;

class RW_Using_Mutex {
    static Semaphore readLock = new Semaphore(1);
    static Semaphore writeLock = new Semaphore(1);
    // volatile : let the JVM know that a thread accessing the variable must always merge its own private copy of the variable with the master copy in the memory.
    // for thread safety.
    volatile static int readCount = 0;

    static class Read implements Runnable {
        @Override
        public void run() {
            try {
                // Lock
                readLock.acquire();
                readCount++; // increase count
                if (readCount == 1) {
                    writeLock.acquire();
                }
                // Unlock
                readLock.release();

                // Reading
                System.out.println("Thread " + Thread.currentThread().getName() + " reading");
                Thread.sleep(1500);
                System.out.println("Thread " + Thread.currentThread().getName() + " finished reading");

                // Releasing section
                readLock.acquire();
                readCount--;
                if (readCount == 0) {
                    writeLock.release();
                }
                readLock.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static class Write implements Runnable {
        @Override
        public void run() {
            try {
                // lock
                writeLock.acquire();
                System.out.println("Thread " + Thread.currentThread().getName() + " writing");
                Thread.sleep(2500);
                System.out.println("Thread " + Thread.currentThread().getName() + " finished writing");
                // unlock
                writeLock.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // 4 threads -> 3 readers, 1 writer.
        Read read = new Read();
        Write write = new Write();
        Thread t1 = new Thread(read);
        t1.setName("thread1");
        Thread t2 = new Thread(read);
        t2.setName("thread2");
        Thread t3 = new Thread(write);
        t3.setName("thread3");
        Thread t4 = new Thread(read);
        t4.setName("thread4");
        t1.start();
        t3.start();
        t2.start();
        t4.start();
    }
}