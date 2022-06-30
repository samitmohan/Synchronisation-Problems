package RW;
public class Writer extends Thread {
    private static int writers = 0; // number of writers
    private int number;
    private Database database;

    public Writer(Database database) {
        this.database = database;
        this.number = Writer.writers++;
    }

    // Writes.
    public void run() {
        while (true) {
            final int DELAY = 5000;
            try {
                Thread.sleep((int) (Math.random() * DELAY));
            } catch (InterruptedException e) {
            }
            this.database.write(this.number);
        }
    }
}