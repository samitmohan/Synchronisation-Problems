package DP;
import java.util.concurrent.Semaphore;
class Philosopher extends Thread {
    private int num;
    static Semaphore left = new Semaphore(1);
    static Semaphore right = new Semaphore(1);


    public Philosopher(int num) {
        this.num = num;
    }

    private void randPause() throws InterruptedException {
        sleep(1000 + (int)(Math.random()*4000));
    }
    public void run () {
        for (int meal = 0; meal < 3; meal++) {
            eat();
            System.out.println(num + " finished meal " + meal);
        }
        System.out.println(num + " all done");
    }

    public void eat() {
        try {
            System.out.println(num + " thinking ");
            randPause();
            System.out.println(num + " hungry ");
            left.acquire();
            System.out.println(num + " left fork picked");
            randPause();
            System.out.println(num + " going for right fork");
            right.acquire();
            System.out.println(num + " right fork; eating");
            randPause();
            System.out.println(num + " finished eating; putting down forks");
            right.release();
            left.release();
        }
        catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}

public class DP_Using_Mutex {
    public static void main(String[] args) {
        Thread t1 = new Thread();

    }
}