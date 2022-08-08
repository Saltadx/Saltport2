// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.entity;

import net.runelite.api.Prayer;
import java.awt.Color;
import net.runelite.client.util.ImageUtil;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import java.awt.image.BufferedImage;
import net.runelite.api.NPC;

public class Hunllef
{
    private static final int ATTACK_TICK_SPEED = 6;
    private static final int MAX_ATTACK_COUNT = 4;
    private static final int MAX_PLAYER_ATTACK_COUNT = 6;
    private final NPC npc;
    private final BufferedImage originalMagicIcon;
    private final BufferedImage originalRangeIcon;
    private BufferedImage magicIcon;
    private BufferedImage rangeIcon;
    private AttackPhase attackPhase;
    private int attackCount;
    private int playerAttackCount;
    private int ticksUntilNextAttack;
    private int iconSize;
    
    public Hunllef(final NPC npc, final SkillIconManager skillIconManager, final int iconSize) {
        this.npc = npc;
        this.originalMagicIcon = skillIconManager.getSkillImage(Skill.MAGIC);
        this.originalRangeIcon = skillIconManager.getSkillImage(Skill.RANGED);
        this.iconSize = iconSize;
        this.attackCount = 4;
        this.playerAttackCount = 6;
        this.ticksUntilNextAttack = 0;
        this.attackPhase = AttackPhase.RANGE;
    }
    
    public void decrementTicksUntilNextAttack() {
        if (this.ticksUntilNextAttack > 0) {
            --this.ticksUntilNextAttack;
        }
    }
    
    public void updatePlayerAttackCount() {
        final int playerAttackCount = this.playerAttackCount - 1;
        this.playerAttackCount = playerAttackCount;
        if (playerAttackCount <= 0) {
            this.playerAttackCount = 6;
        }
    }
    
    public void updateAttackCount() {
        this.ticksUntilNextAttack = 6;
        final int attackCount = this.attackCount - 1;
        this.attackCount = attackCount;
        if (attackCount <= 0) {
            this.attackPhase = ((this.attackPhase == AttackPhase.RANGE) ? AttackPhase.MAGIC : AttackPhase.RANGE);
            this.attackCount = 4;
        }
    }
    
    public void setIconSize(final int iconSize) {
        this.iconSize = iconSize;
        this.magicIcon = ImageUtil.resizeImage(this.originalMagicIcon, iconSize, iconSize);
        this.rangeIcon = ImageUtil.resizeImage(this.originalRangeIcon, iconSize, iconSize);
    }
    
    public BufferedImage getIcon() {
        switch (this.attackPhase) {
            case MAGIC: {
                if (this.magicIcon == null) {
                    this.magicIcon = ImageUtil.resizeImage(this.originalMagicIcon, this.iconSize, this.iconSize);
                }
                return this.magicIcon;
            }
            case RANGE: {
                if (this.rangeIcon == null) {
                    this.rangeIcon = ImageUtil.resizeImage(this.originalRangeIcon, this.iconSize, this.iconSize);
                }
                return this.rangeIcon;
            }
            default: {
                throw new IllegalStateException("Unexpected boss attack phase: " + this.attackPhase);
            }
        }
    }
    
    public NPC getNpc() {
        return this.npc;
    }
    
    public AttackPhase getAttackPhase() {
        return this.attackPhase;
    }
    
    public int getAttackCount() {
        return this.attackCount;
    }
    
    public int getPlayerAttackCount() {
        return this.playerAttackCount;
    }
    
    public int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }
    
    public enum AttackPhase
    {
        MAGIC(Color.CYAN, Prayer.PROTECT_FROM_MAGIC), 
        RANGE(Color.GREEN, Prayer.PROTECT_FROM_MISSILES);
        
        private final Color color;
        private final Prayer prayer;
        
        private AttackPhase(final Color color, final Prayer prayer) {
            this.color = color;
            this.prayer = prayer;
        }
        
        public Color getColor() {
            return this.color;
        }
        
        public Prayer getPrayer() {
            return this.prayer;
        }
    }
}
