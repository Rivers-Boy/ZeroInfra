package thread;

public class printABC {

    private static int num;
    private static final Object LOCK = new Object();

    private static void printAns(int targetNum) {
        for (int i = 0; i < 10; i++) {  // 单次打印去掉循环即可
            synchronized (LOCK) {
                while (num % 3 != targetNum) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                num++;
                System.out.println(Thread.currentThread().getName());
                LOCK.notifyAll();
            }
        }

    }

    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                printAns(0);
            }
        }, "A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                printAns(1);
            }
        }, "B").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                printAns(2);
            }
        }, "C").start();
    }
}
