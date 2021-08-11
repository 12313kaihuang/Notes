package chapter4;

public class MyRunnable1 implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() +
                    " run! " + System.currentTimeMillis());
            Thread.sleep(4000);
            System.out.println(Thread.currentThread().getName() +
                    " end! " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
