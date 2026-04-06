package com.mevchu.mods;

import java.util.ArrayDeque;
import java.util.Deque;

public class CpsMod {

    private final Deque<Long> leftClicks  = new ArrayDeque<>();
    private final Deque<Long> rightClicks = new ArrayDeque<>();

    public void onLeftClick()  { leftClicks.addLast(System.currentTimeMillis()); }
    public void onRightClick() { rightClicks.addLast(System.currentTimeMillis()); }

    public void onTick() {
        long cutoff = System.currentTimeMillis() - 1000L;
        while (!leftClicks.isEmpty()  && leftClicks.peekFirst()  < cutoff) leftClicks.pollFirst();
        while (!rightClicks.isEmpty() && rightClicks.peekFirst() < cutoff) rightClicks.pollFirst();
    }

    public int getLeftCps()  { return leftClicks.size(); }
    public int getRightCps() { return rightClicks.size(); }
}
