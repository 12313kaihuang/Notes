package chapter4;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test2 {

    public static void main(String[] args) {

        MyRunnable1 runnable1 = new MyRunnable1();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(7, 10,
                0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        pool.execute(runnable1);
        System.out.println("main end!");
        //程序没有结束  因为使用有一个核心线程在等待任务 todo
    }
}
