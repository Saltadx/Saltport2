// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.entity;

import net.runelite.api.NPC;

public class Tornado
{
    private static final int TICK_DURATION = 21;
    private int timeLeft;
    private final NPC npc;
    
    public void updateTimeLeft() {
        if (this.timeLeft >= 0) {
            --this.timeLeft;
        }
    }
    
    public Tornado(final NPC npc) {
        this.timeLeft = 21;
        this.npc = npc;
    }
    
    public int getTimeLeft() {
        return this.timeLeft;
    }
    
    public NPC getNpc() {
        return this.npc;
    }
}
