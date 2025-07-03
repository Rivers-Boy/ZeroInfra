package thread;

/**
 * join()方法：在A线程中调用了B线程的join()方法时，表示只有当B线程执行完毕时，A线程才能继续执行。
 */
public class PrintNumByJoin {

    private static int num;

    private static void printNum(Thread preThread) {
        if (preThread != null) {
            try {
                preThread.join();
                System.out.println(num);
                num++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread t1 = new Thread(() -> {
                printNum(null);
            });
            Thread t2 = new Thread(() -> {
                printNum(t1);
            });
            Thread t3 = new Thread(() -> {
                printNum(t2);
            });
            t1.start();
            t2.start();
            t3.start();
            Thread.sleep(10);
        }
    }
}
