《Java并发编程：核心方法与框架》读书笔记
-----------------------

时间关系，仅一个大纲，细节部分还是再进入书中或者Demo中温习吧

# 第4章 Executor和ThreadPoolExecutor的使用

JDK5中提供了线程池的支持，主要的作用是**支持高并发的访问处理**，并且可以**将线程对象进行复用**。

## Executor接口介绍

```java
//java.util.concurrent.Executor.java 
public interface Executor {

    void execute(Runnable command);
}
```

接口关系：**Executor**结构非常简洁，仅有一个方法；接口**ExecutorService**是Executor的子接口，在内部添加了比较多的方法；ExecutorService的唯一子实现类**AbstractExecutorService**；**ThreadPoolExecutor**又继承了AbstractExecutorService。

## 使用Executors工厂类创建线程池

ThreadPoolExecutor在使用上并不是那么方便，在实例化时需要传入很多个参数，还要考虑线程的并发数等与线程池运行效率有关的参数，所以官方建议使用**Executors**工厂类来创建线程池对象。

* newFixedThreadPool()
* newCachedThreadPool()
* newScheduledThreadPool()
* newSingleThreadExecutor()

## ThreadPoolExecutor的使用

### 构造方法的测试

ThreadPoolExecutor最常使用的构造方法是：

```java
//核心线程数，最大线程数，非核心线程保活时间，保活时间单位，任务队列
ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue)
```

#### 线程池执行任务逻辑：

* 当线程池中线程数**未达到核心线程数**时，会立即创建一个核心线程处理任务。
* 当线程池中线程数达到了核心线程数但未达到最大线程数时，会将任务放入任务队列中等待线程空闲。
* 当上述情况下，任务队列满时，会启动非核心线程执行任务。
* 若此时线程数以达最大要求，则根据所设置的rejectHandler策略拒绝任务。

有一些细节性的Demo可以通过代码查看



### 方法shutdown()和shutdownNow()与返回

* **shutdown()**的作用是使**当前未执行完的线程继续执行，而不再添加新的任务Task**。shutdown()方法不会阻塞，调用shutdown()方法后，主线程main就马上结束了，而**线程池会继续运行直到所有任务执行完才会停止**。如果不调用shutdown()方法，那么线程池会一直保持下去，以便随时执行被添加的新Task任务。

  当线程池调用shutdown()方法时，线程池的状态则立刻变成**SHUTDOWN**状态，此时不能再往线程池中添加任何任务，否则将会抛出RejectedExecutionException异常。但是，此时线程池不会立刻退出，直到线程池中的任务都已经处理完成，才会退出。

* **shutdownNow()**的作用是**中断所有的任务Task**，并且**抛出InterruptedException异常**，前提是在Runnable中使用if (Thread.currentThread().isInterrupted() == true)语句来判断当前线程的中断状态，而未执行的线程不再执行，也就是从执行队列中清除。如果没有if (Thread.currentThread().isInterrupted() == true)语句及抛出异常的代码，则池中正在运行的线程直到执行完毕，而未执行的线程不再执行，也从执行队列中清除。

  shutdownNow()方法是使线程池的状态立刻变成**STOP**状态，并**试图停止所有正在执行的线程**（如果有if判断则人为地抛出异常），不再处理还在池队列中等待的任务，当然，它**会返回那些未执行的任务**。

> 所以shutdownNow方法需要在任务中通过判断线程池状态与之配合，否则调用此方法的效果会跟shutdown一样

### 方法isShutdown()

作用是判断线程池是否已经关闭。
