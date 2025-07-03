package classic;

public class Singleton {

    private static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        Singleton instance1 = getInstance();
        Singleton instance2 = getInstance();
        System.out.println(instance1 == instance2);
    }
}
