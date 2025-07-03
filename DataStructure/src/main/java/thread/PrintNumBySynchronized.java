package thread;

public class PrintNumBySynchronized {

    private static int num;
    private static final Object LOCK = new Object();

    private static void printNum(int targetNum) {
        for (int i = 0; i < 10; i++) {
            synchronized (LOCK) {
                while (num % 3 != targetNum) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(num);
                num++;
                LOCK.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                printNum(0);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                printNum(1);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                printNum(2);
            }
        }).start();
    }
}
