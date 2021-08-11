package chapter4;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test4 {

    public static void main(String[] args) {

        MyRunnable1 runnable1 = new MyRunnable1();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(7, 10,
                0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        pool.execute(runnable1);
        pool.execute(runnable1);
        pool.shutdown();
        pool.execute(runnable1); //会抛出RejectedExecutionException异常
        System.out.println("main end!");
        //程序会在任务执行完成后结束 todo

        test2();
    }

    private static void test2() {
        MyRunnable1 runnable1 = new MyRunnable1();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 10,
                0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        pool.execute(runnable1);
        pool.execute(runnable1);
        pool.shutdown();
        System.out.println("pool shutdown");
        //shutdown后，任务队列中等待的任务也会得到执行 todo
    }
}
