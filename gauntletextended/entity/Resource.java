// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.entity;

import net.runelite.api.Skill;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.game.SkillIconManager;
import java.awt.image.BufferedImage;
import net.runelite.api.GameObject;

public class Resource
{
    private final GameObject gameObject;
    private final BufferedImage originalIcon;
    private BufferedImage icon;
    private int iconSize;
    
    public Resource(final GameObject gameObject, final SkillIconManager skillIconManager, final int iconSize) {
        this.gameObject = gameObject;
        this.iconSize = iconSize;
        this.originalIcon = getOriginalIcon(skillIconManager, gameObject.getId());
    }
    
    public void setIconSize(final int iconSize) {
        this.iconSize = iconSize;
        this.icon = ImageUtil.resizeImage(this.originalIcon, iconSize, iconSize);
    }
    
    public BufferedImage getIcon() {
        if (this.icon == null) {
            this.icon = ImageUtil.resizeImage(this.originalIcon, this.iconSize, this.iconSize);
        }
        return this.icon;
    }
    
    private static BufferedImage getOriginalIcon(final SkillIconManager skillIconManager, final int objectId) {
        switch (objectId) {
            case 35967:
            case 36064: {
                return skillIconManager.getSkillImage(Skill.MINING);
            }
            case 35969:
            case 36066: {
                return skillIconManager.getSkillImage(Skill.WOODCUTTING);
            }
            case 35971:
            case 36068: {
                return skillIconManager.getSkillImage(Skill.FISHING);
            }
            case 35973:
            case 36070: {
                return skillIconManager.getSkillImage(Skill.HERBLORE);
            }
            case 35975:
            case 36072: {
                return skillIconManager.getSkillImage(Skill.FARMING);
            }
            default: {
                throw new IllegalArgumentException("Unsupported gauntlet resource gameobject id: " + objectId);
            }
        }
    }
    
    public GameObject getGameObject() {
        return this.gameObject;
    }
}
