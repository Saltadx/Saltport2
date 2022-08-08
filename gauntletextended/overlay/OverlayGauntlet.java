// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.overlay;

import net.runelite.api.NPC;
import com.gauntletextended.entity.Demiboss;
import java.awt.Polygon;
import net.runelite.api.GameObject;
import java.util.Iterator;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.TileObject;
import net.runelite.client.ui.overlay.OverlayUtil;
import java.awt.Shape;
import net.runelite.api.Perspective;
import com.gauntletextended.entity.Resource;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.client.ui.overlay.OverlayLayer;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.plugins.Plugin;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import com.gauntletextended.GauntletExtendedConfig;
import com.gauntletextended.GauntletExtendedPlugin;
import net.runelite.api.Client;
import javax.inject.Singleton;

@Singleton
public class OverlayGauntlet extends Overlay
{
    private final Client client;
    private final GauntletExtendedPlugin plugin;
    private final GauntletExtendedConfig config;
    private final ModelOutlineRenderer modelOutlineRenderer;
    private Player player;
    
    @Inject
    private OverlayGauntlet(final Client client, final GauntletExtendedPlugin plugin, final GauntletExtendedConfig config, final ModelOutlineRenderer modelOutlineRenderer) {
        super(plugin);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.determineLayer();
    }
    
    @Override
    public void determineLayer() {
        this.setLayer(OverlayLayer.UNDER_WIDGETS);
    }
    
    public Dimension render(final Graphics2D graphics2D) {
        this.player = this.client.getLocalPlayer();
        if (this.player == null) {
            return null;
        }
        this.renderResources(graphics2D);
        this.renderUtilities();
        this.renderDemibosses();
        this.renderStrongNpcs();
        this.renderWeakNpcs();
        return null;
    }
    
    private void renderResources(final Graphics2D graphics2D) {
        if ((!this.config.resourceOverlay() && !this.config.resourceOutline()) || this.plugin.getResources().isEmpty()) {
            return;
        }
        final LocalPoint localPointPlayer = this.player.getLocalLocation();
        for (final Resource resource : this.plugin.getResources()) {
            final GameObject gameObject = resource.getGameObject();
            final LocalPoint localPointGameObject = gameObject.getLocalLocation();
            if (this.isOutsideRenderDistance(localPointGameObject, localPointPlayer)) {
                continue;
            }
            if (this.config.resourceOverlay()) {
                final Polygon polygon = Perspective.getCanvasTilePoly(this.client, localPointGameObject);
                if (polygon == null) {
                    continue;
                }
                Overlay.drawOutlineAndFill(graphics2D, this.config.resourceTileOutlineColor(), this.config.resourceTileFillColor(), (float)this.config.resourceTileOutlineWidth(), polygon);
                OverlayUtil.renderImageLocation(this.client, graphics2D, localPointGameObject, resource.getIcon(), 0);
            }
            if (!this.config.resourceOutline()) {
                continue;
            }
            final Shape shape = gameObject.getConvexHull();
            if (shape == null) {
                continue;
            }
            this.modelOutlineRenderer.drawOutline((TileObject)gameObject, this.config.resourceOutlineWidth(), this.config.resourceOutlineColor(), 0);
        }
    }
    
    private void renderUtilities() {
        if (!this.config.utilitiesOutline() || this.plugin.getUtilities().isEmpty()) {
            return;
        }
        final LocalPoint localPointPlayer = this.player.getLocalLocation();
        for (final GameObject gameObject : this.plugin.getUtilities()) {
            if (this.isOutsideRenderDistance(gameObject.getLocalLocation(), localPointPlayer)) {
                continue;
            }
            final Shape shape = gameObject.getConvexHull();
            if (shape == null) {
                continue;
            }
            this.modelOutlineRenderer.drawOutline((TileObject)gameObject, this.config.utilitiesOutlineWidth(), this.config.utilitiesOutlineColor(), 0);
        }
    }
    
    private void renderDemibosses() {
        if (!this.config.demibossOutline() || this.plugin.getDemibosses().isEmpty()) {
            return;
        }
        final LocalPoint localPointPlayer = this.player.getLocalLocation();
        for (final Demiboss demiboss : this.plugin.getDemibosses()) {
            final NPC npc = demiboss.getNpc();
            final LocalPoint localPointNpc = npc.getLocalLocation();
            if (localPointNpc != null && !npc.isDead()) {
                if (this.isOutsideRenderDistance(localPointNpc, localPointPlayer)) {
                    continue;
                }
                this.modelOutlineRenderer.drawOutline(npc, this.config.demibossOutlineWidth(), demiboss.getType().getOutlineColor(), 0);
            }
        }
    }
    
    private void renderStrongNpcs() {
        if (!this.config.strongNpcOutline() || this.plugin.getStrongNpcs().isEmpty()) {
            return;
        }
        final LocalPoint localPointPLayer = this.player.getLocalLocation();
        for (final NPC npc : this.plugin.getStrongNpcs()) {
            final LocalPoint localPointNpc = npc.getLocalLocation();
            if (localPointNpc != null && !npc.isDead()) {
                if (this.isOutsideRenderDistance(localPointNpc, localPointPLayer)) {
                    continue;
                }
                this.modelOutlineRenderer.drawOutline(npc, this.config.strongNpcOutlineWidth(), this.config.strongNpcOutlineColor(), 0);
            }
        }
    }
    
    private void renderWeakNpcs() {
        if (!this.config.weakNpcOutline() || this.plugin.getWeakNpcs().isEmpty()) {
            return;
        }
        final LocalPoint localPointPlayer = this.player.getLocalLocation();
        for (final NPC npc : this.plugin.getWeakNpcs()) {
            final LocalPoint localPointNpc = npc.getLocalLocation();
            if (localPointNpc != null && !npc.isDead()) {
                if (this.isOutsideRenderDistance(localPointNpc, localPointPlayer)) {
                    continue;
                }
                this.modelOutlineRenderer.drawOutline(npc, this.config.weakNpcOutlineWidth(), this.config.weakNpcOutlineColor(), 0);
            }
        }
    }
    
    private boolean isOutsideRenderDistance(final LocalPoint localPoint, final LocalPoint playerLocation) {
        final int maxDistance = this.config.resourceRenderDistance().getDistance();
        return maxDistance != 0 && localPoint.distanceTo(playerLocation) >= maxDistance;
    }
}
