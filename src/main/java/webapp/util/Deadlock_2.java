package webapp.util;

public class Deadlock_2 {
    public static void main(String[] args) {
        final Object o1 = new Object();
        final Object o2 = new Object();

        Thread thread0 = new Thread(()->{
            synchronized (o1){
                System.out.println("lock o1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                synchronized (o2){
                    System.out.println("lock o2");
                }
            }
        });

        Thread thread1 = new Thread(()->{
            synchronized (o2){
                System.out.println("lock o2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                synchronized (o1){
                    System.out.println("lock o1");
                }
            }
        });
        thread0.start();
        thread1.start();
    }
}
