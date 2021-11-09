package kala.internal;

public class BreakHole implements Runnable {
    public boolean isBroken = false;

    @Override
    public void run() {
        isBroken = true;
    }
}
