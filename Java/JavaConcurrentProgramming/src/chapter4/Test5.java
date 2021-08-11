package chapter4;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test5 {

    public static void main(String[] args) {
        //见MyRunnable2
        MyRunnable2 runnable2 = new MyRunnable2();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 10,
                0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        pool.execute(runnable2);
        pool.execute(runnable2);
        pool.execute(runnable2);
        //调用shutdownNow后，等待队列中的任务会被取消，返回值为被取消的任务列表 todo
        List<Runnable> runnables = pool.shutdownNow();
        System.out.println("pool shutdown, 取消任务数：" + runnables.size());
    }

}
