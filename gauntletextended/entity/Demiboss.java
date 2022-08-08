// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.entity;

import com.google.common.collect.ImmutableSet;
import java.awt.Color;
import java.util.Set;
import net.runelite.api.NPC;

public class Demiboss
{
    private final NPC npc;
    private final Type type;
    
    public Demiboss(final NPC npc) {
        this.npc = npc;
        this.type = Type.fromId(npc.getId());
    }
    
    public NPC getNpc() {
        return this.npc;
    }
    
    public Type getType() {
        return this.type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Demiboss)) {
            return false;
        }
        final Demiboss other = (Demiboss)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$npc = this.getNpc();
        final Object other$npc = other.getNpc();
        if (this$npc == null) {
            if (other$npc == null) {
                return true;
            }
        }
        else if (this$npc.equals(other$npc)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Demiboss;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $npc = this.getNpc();
        result = result * 59 + (($npc == null) ? 43 : $npc.hashCode());
        return result;
    }
    
    public enum Type
    {
        BEAR((Set<Integer>)ImmutableSet.of((Object)9032, (Object)9046), Color.RED), 
        DARK_BEAST((Set<Integer>)ImmutableSet.of((Object)9034, (Object)9048), Color.GREEN), 
        DRAGON((Set<Integer>)ImmutableSet.of((Object)9033, (Object)9047), Color.BLUE);
        
        private final Set<Integer> ids;
        private final Color outlineColor;
        
        static Type fromId(final int id) {
            for (final Type type : values()) {
                if (type.ids.contains(id)) {
                    return type;
                }
            }
            return null;
        }
        
        private Type(final Set<Integer> ids, final Color outlineColor) {
            this.ids = ids;
            this.outlineColor = outlineColor;
        }
        
        public Color getOutlineColor() {
            return this.outlineColor;
        }
    }
}
