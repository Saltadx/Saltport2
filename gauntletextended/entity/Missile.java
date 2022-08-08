// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.entity;

import net.runelite.api.Skill;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.game.SkillIconManager;
import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.api.Projectile;

public class Missile
{
    private static final int FILL_ALPHA = 100;
    private final Projectile projectile;
    private final BufferedImage originalIcon;
    private final Color outlineColor;
    private final Color fillColor;
    private BufferedImage icon;
    private int iconSize;
    
    public Missile(final Projectile projectile, final SkillIconManager skillIconManager, final int iconSize) {
        this.projectile = projectile;
        this.iconSize = iconSize;
        this.originalIcon = getOriginalIcon(skillIconManager, projectile.getId());
        this.outlineColor = getOutlineColor(projectile.getId());
        this.fillColor = new Color(this.outlineColor.getRed(), this.outlineColor.getGreen(), this.outlineColor.getBlue(), 100);
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
    
    private static Color getOutlineColor(final int projectileId) {
        switch (projectileId) {
            case 1707:
            case 1708: {
                return Color.CYAN;
            }
            case 1711:
            case 1712: {
                return Color.GREEN;
            }
            case 1713:
            case 1714: {
                return Color.MAGENTA;
            }
            default: {
                throw new IllegalArgumentException("Unsupported gauntlet projectile id: " + projectileId);
            }
        }
    }
    
    private static BufferedImage getOriginalIcon(final SkillIconManager skillIconManager, final int projectileId) {
        switch (projectileId) {
            case 1707:
            case 1708: {
                return skillIconManager.getSkillImage(Skill.MAGIC);
            }
            case 1711:
            case 1712: {
                return skillIconManager.getSkillImage(Skill.RANGED);
            }
            case 1713:
            case 1714: {
                return skillIconManager.getSkillImage(Skill.PRAYER);
            }
            default: {
                throw new IllegalArgumentException("Unsupported gauntlet projectile id: " + projectileId);
            }
        }
    }
    
    public Projectile getProjectile() {
        return this.projectile;
    }
    
    public Color getOutlineColor() {
        return this.outlineColor;
    }
    
    public Color getFillColor() {
        return this.fillColor;
    }
}
