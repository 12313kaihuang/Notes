package chapter4;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Run4_1 {

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() +
                    " run! " + System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        //7核心线程8最大线程，超时时间5秒，队列为LinkedBlockingQueue，默认队列长度无限
        ThreadPoolExecutor executor = new ThreadPoolExecutor(7, 8,
                5, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        executor.execute(runnable); //1
        executor.execute(runnable); //2
        executor.execute(runnable); //3
        executor.execute(runnable); //4
        executor.execute(runnable); //5
        executor.execute(runnable); //6
        executor.execute(runnable); //7
        executor.execute(runnable); //8
        executor.execute(runnable); //9
        Thread.sleep(300);
        System.out.println("A:" + executor.getCorePoolSize());
        System.out.println("A:" + executor.getPoolSize());
        System.out.println("A:" + executor.getQueue().size());
        Thread.sleep(10000);
        System.out.println("B:" + executor.getCorePoolSize());
        System.out.println("B:" + executor.getPoolSize());
        System.out.println("B:" + executor.getQueue().size());
        //注意到这里后程序并未结束，因为还有线程在等待任务 todo
    }
}
