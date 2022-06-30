package RW;
public class Reader extends Thread {
    private static int readers = 0; // number of readers
    private int number;
    private Database database;

    public Reader(Database database) {
        this.database = database;
        this.number = Reader.readers++;
    }

     // Reads.
    public void run() {
        while (true) {
            final int DELAY = 5000;
            try {
                Thread.sleep((int) (Math.random() * DELAY));
            } catch (InterruptedException e) {
            }
            this.database.read(this.number);
        }
    }
}
