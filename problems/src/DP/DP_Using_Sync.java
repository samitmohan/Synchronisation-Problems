// Hence, to avoid a deadlock situation we need to make sure that the circular wait condition is broken.
class Philosopher implements Runnable {
    // forks
    private Object lf;
    private Object rf;

    public Philosopher(Object lf, Object rf) {
        this.lf = lf;
        this.rf = rf;
    }

    private void work(String job) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " " + job);
        Thread.sleep(((int) (Math.random() * 100))); // sleep for 1 ms
    }

    @Override
    public void run() {
        // we need to lock it so that no two Philosopher threads acquire it at the same time.
        try {
            while (true) {
                // thinking
                work(System.nanoTime() + " : Thinking");
                synchronized (lf) {
                    work(System.nanoTime() + " : Picked up left fork");
                    synchronized (rf) {
                        // eating (picked up both forks)
                        work(System.nanoTime() + " : Eating (picked up right fork also)");
                        work(System.nanoTime() + " : Put down right fork");
                    }

                    // thinking again
                    work(System.nanoTime() + " : Put down left fork, thinking again.");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
}

class DiningPhilosopher {
    public static void main(String[] args) throws Exception {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] forks = new Object[philosophers.length]; //  generic Java objects

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Object(); // initialise fork for the philosopher
        }

        for (int i = 0; i < philosophers.length; i++) {
            Object lf = forks[i];
            Object rf = forks[(i + 1) % forks.length];
            // solution -> avoiding deadlock (make sure the last philosopher picks up right fork and then left)
            if (i == philosophers.length - 1) {
                // last philosopher picks up right fork first.
                philosophers[i] = new Philosopher(rf, lf);
            } else {
                philosophers[i] = new Philosopher(lf, rf);
            }
            Thread t1 = new Thread(philosophers[i], "Philosopher" + (i + 1));
            t1.start();
        }
    }
}