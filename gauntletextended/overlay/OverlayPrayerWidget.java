// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.overlay;

import net.runelite.client.ui.overlay.OverlayLayer;
import java.awt.Rectangle;
import net.runelite.api.NPC;
import com.gauntletextended.entity.Hunllef;
import net.runelite.api.Point;
import java.awt.Color;
import com.gauntletextended.Prayer;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.plugins.Plugin;
import com.gauntletextended.GauntletExtendedConfig;
import com.gauntletextended.GauntletExtendedPlugin;
import net.runelite.api.Client;
import javax.inject.Singleton;

@Singleton
public class OverlayPrayerWidget extends Overlay
{
    private final Client client;
    private final GauntletExtendedPlugin plugin;
    private final GauntletExtendedConfig config;
    
    @Inject
    OverlayPrayerWidget(final Client client, final GauntletExtendedPlugin plugin, final GauntletExtendedConfig config) {
        super(plugin);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.determineLayer();
    }
    
    public Dimension render(final Graphics2D graphics2D) {
        final GauntletExtendedConfig.PrayerHighlightMode prayerHighlightMode = this.config.prayerOverlay();
        if (prayerHighlightMode == GauntletExtendedConfig.PrayerHighlightMode.NONE || prayerHighlightMode == GauntletExtendedConfig.PrayerHighlightMode.BOX) {
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
        final Hunllef.AttackPhase phase = hunllef.getAttackPhase();
        final Prayer prayer = Prayer.valueOf(phase.getPrayer().name());
        final Color phaseColor = phase.getColor();
        final Rectangle rectangle = OverlayUtil.renderPrayerOverlay(graphics2D, this.client, prayer, phaseColor);
        if (rectangle == null) {
            return null;
        }
        final int ticksUntilAttack = hunllef.getTicksUntilNextAttack();
        final String text = String.valueOf(ticksUntilAttack);
        final int fontSize = 16;
        final int fontStyle = 1;
        final Color fontColor = (ticksUntilAttack == 1) ? Color.WHITE : phaseColor;
        final int x = (int)(rectangle.getX() + rectangle.getWidth() / 2.0);
        final int y = (int)(rectangle.getY() + rectangle.getHeight() / 2.0);
        final Point point = new Point(x, y);
        final Point canvasPoint = new Point(point.getX() - 3, point.getY() + 6);
        OverlayUtil.renderTextLocation(graphics2D, text, 16, 1, fontColor, canvasPoint, true, 0);
        return null;
    }
    
    @Override
    public void determineLayer() {
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
    }
}
