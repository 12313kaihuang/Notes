package chapter4;

public class MyRunnable2 implements Runnable {

    @Override
    public void run() {
        try {
            for(int i =0;i<Integer.MAX_VALUE/50;i++){
                String newStr = new String();
                Math.random();
                Math.random();
                Math.random();
                Math.random();
                Math.random();
                Math.random();
                if (Thread.currentThread().isInterrupted() == true) {
                    System.out.println("任务没有完成，就中断了");
                    throw new InterruptedException();
                }
            }
            System.out.println("任务成功完成！");
        } catch (InterruptedException e) {
            System.out.println("进入catch中断了任务");
            e.printStackTrace();
        }
    }
}
