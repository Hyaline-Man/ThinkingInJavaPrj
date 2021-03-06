package com.zh.concurrent.join;

/**
 * Created by zh on 2016/12/24.
 */
class Sleeper extends Thread {
    private int duration;
    public Sleeper(String name, int sleepTime) {
        super(name);
        duration = sleepTime;
        start();
    }
    public void run(){
        try{
            sleep(duration);
        } catch (InterruptedException e){
            // 抛出中断异常时jvm会将中断状态清楚，所以这里始终打印false
            System.out.println(getName() + " was interrupted. isInterrupted(): " + isInterrupted());
        }
        System.out.println(getName() + " has awakened");
    }
}

class Joiner extends Thread {
    private Sleeper sleeper;
    public Joiner(String name, Sleeper sleeper){
        super(name);
        this.sleeper = sleeper;
        start();
    }
    public void run(){
        try{
            sleeper.join();
        } catch (InterruptedException e){
            System.out.println("Interrupted");
        }
        System.out.println(getName() + " join completed");
    }
}
public class Joining{
    public static void main(String[] args){
        Sleeper sleepy = new Sleeper("Sleepy", 1500), grumpy = new Sleeper("Grumpy", 1500);
        Joiner dopey = new Joiner("Dopey", sleepy),doc = new Joiner("Doc", grumpy);
        grumpy.interrupt();
    }
}

