// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.overlay;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.api.Prayer;
import net.runelite.api.NPC;
import com.gauntletextended.entity.Hunllef;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayPosition;
import java.awt.Rectangle;
import java.awt.Dimension;
import net.runelite.client.plugins.Plugin;
import java.awt.image.BufferedImage;
import net.runelite.client.ui.overlay.components.InfoBoxComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.game.SpriteManager;
import com.gauntletextended.GauntletExtendedConfig;
import com.gauntletextended.GauntletExtendedPlugin;
import net.runelite.api.Client;
import java.awt.Color;
import javax.inject.Singleton;

@Singleton
public class OverlayPrayerBox extends Overlay
{
    private static final Color NOT_ACTIVATED_BACKGROUND_COLOR;
    private final Client client;
    private final GauntletExtendedPlugin plugin;
    private final GauntletExtendedConfig config;
    private final SpriteManager spriteManager;
    private final PanelComponent panelComponent;
    private final InfoBoxComponent infoBoxComponent;
    private BufferedImage spriteProtectFromMagic;
    private BufferedImage spriteProtectFromRange;
    
    @Inject
    OverlayPrayerBox(final Client client, final GauntletExtendedPlugin plugin, final GauntletExtendedConfig config, final SpriteManager spriteManager) {
        super(plugin);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.spriteManager = spriteManager;
        this.panelComponent = new PanelComponent();
        (this.infoBoxComponent = new InfoBoxComponent()).setColor(Color.WHITE);
        this.infoBoxComponent.setPreferredSize(new Dimension(40, 40));
        this.panelComponent.getChildren().add(this.infoBoxComponent);
        this.panelComponent.setPreferredSize(new Dimension(40, 40));
        this.panelComponent.setBorder(new Rectangle(0, 0, 0, 0));
        this.setPosition(OverlayPosition.BOTTOM_RIGHT);
        this.setPriority(OverlayPriority.HIGH);
        this.determineLayer();
    }
    
    public Dimension render(final Graphics2D graphics) {
        final GauntletExtendedConfig.PrayerHighlightMode prayerHighlightMode = this.config.prayerOverlay();
        if (prayerHighlightMode == GauntletExtendedConfig.PrayerHighlightMode.NONE || prayerHighlightMode == GauntletExtendedConfig.PrayerHighlightMode.WIDGET) {
            return null;
        }
        final Hunllef hunllef = this.plugin.getHunllef();
        if (hunllef == null) {
            return null;
        }
        final NPC npc = hunllef.getNpc();
        if (npc == null || npc.isDead()) {
            return null;
        }
        final Prayer prayer = hunllef.getAttackPhase().getPrayer();
        this.infoBoxComponent.setImage(this.getPrayerSprite(prayer));
        this.infoBoxComponent.setBackgroundColor(this.client.isPrayerActive(prayer) ? ComponentConstants.STANDARD_BACKGROUND_COLOR : OverlayPrayerBox.NOT_ACTIVATED_BACKGROUND_COLOR);
        return this.panelComponent.render(graphics);
    }
    
    @Override
    public void determineLayer() {
        this.setLayer(OverlayLayer.UNDER_WIDGETS);
    }
    
    private BufferedImage getPrayerSprite(final Prayer prayer) {
        switch (prayer) {
            case PROTECT_FROM_MAGIC: {
                if (this.spriteProtectFromMagic == null) {
                    this.spriteProtectFromMagic = scaleSprite(this.spriteManager.getSprite(127, 0));
                }
                return this.spriteProtectFromMagic;
            }
            case PROTECT_FROM_MISSILES: {
                if (this.spriteProtectFromRange == null) {
                    this.spriteProtectFromRange = scaleSprite(this.spriteManager.getSprite(128, 0));
                }
                return this.spriteProtectFromRange;
            }
            default: {
                throw new IllegalStateException("Unexpected boss attack phase prayer: " + prayer);
            }
        }
    }
    
    private static BufferedImage scaleSprite(final BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return null;
        }
        final double width = bufferedImage.getWidth(null);
        final double height = bufferedImage.getHeight(null);
        final double size = 36.0;
        final double scalex = 36.0 / width;
        final double scaley = 36.0 / height;
        final double scale = Math.min(scalex, scaley);
        final int newWidth = (int)(width * scale);
        final int newHeight = (int)(height * scale);
        final BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, 2);
        final Graphics g = scaledImage.createGraphics();
        g.drawImage(bufferedImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return scaledImage;
    }
    
    static {
        NOT_ACTIVATED_BACKGROUND_COLOR = new Color(150, 0, 0, 150);
    }
}
