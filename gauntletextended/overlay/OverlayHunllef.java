// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.overlay;

import net.runelite.api.Model;
import java.util.List;
import net.runelite.api.model.Jarvis;
import java.util.ArrayList;
import com.gauntletextended.Vertex;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.Projectile;
import net.runelite.api.NPCComposition;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.gauntletextended.entity.Missile;
import java.awt.image.ImageObserver;
import java.awt.Image;
import net.runelite.api.Point;
import java.awt.Polygon;
import java.util.Iterator;
import java.awt.Shape;
import net.runelite.api.Perspective;
import com.gauntletextended.entity.Tornado;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.api.NPC;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.plugins.Plugin;
import com.gauntletextended.entity.Hunllef;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import com.gauntletextended.GauntletExtendedConfig;
import com.gauntletextended.GauntletExtendedPlugin;
import net.runelite.api.Client;
import java.awt.Color;
import javax.inject.Singleton;

@Singleton
public class OverlayHunllef extends Overlay
{
    private static final Color[] COLORS;
    private static final int COLOR_DURATION = 10;
    private final Client client;
    private final GauntletExtendedPlugin plugin;
    private final GauntletExtendedConfig config;
    private final ModelOutlineRenderer modelOutlineRenderer;
    private Hunllef hunllef;
    private int timeout;
    private int idx;
    
    @Inject
    private OverlayHunllef(final Client client, final GauntletExtendedPlugin plugin, final GauntletExtendedConfig config, final ModelOutlineRenderer modelOutlineRenderer) {
        super(plugin);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.determineLayer();
    }
    
    public Dimension render(final Graphics2D graphics2D) {
        this.hunllef = this.plugin.getHunllef();
        if (this.hunllef == null) {
            return null;
        }
        final NPC npc = this.hunllef.getNpc();
        if (npc == null) {
            return null;
        }
        if (npc.isDead()) {
            this.renderDiscoMode();
            return null;
        }
        this.renderTornadoes(graphics2D);
        this.renderProjectile(graphics2D);
        this.renderHunllefWrongPrayerOutline();
        this.renderHunllefAttackCounter(graphics2D);
        this.renderHunllefAttackStyleIcon(graphics2D);
        this.renderHunllefTile(graphics2D);
        this.renderFlashOnWrongAttack(graphics2D);
        this.renderFlashOn51Method(graphics2D);
        return null;
    }
    
    @Override
    public void determineLayer() {
        this.setLayer(OverlayLayer.UNDER_WIDGETS);
    }
    
    private void renderTornadoes(final Graphics2D graphics2D) {
        if ((!this.config.tornadoTickCounter() && !this.config.tornadoTileOutline()) || this.plugin.getTornadoes().isEmpty()) {
            return;
        }
        for (final Tornado tornado : this.plugin.getTornadoes()) {
            final int timeLeft = tornado.getTimeLeft();
            if (timeLeft < 0) {
                continue;
            }
            final NPC npc = tornado.getNpc();
            if (this.config.tornadoTileOutline()) {
                final Polygon polygon = Perspective.getCanvasTilePoly(this.client, npc.getLocalLocation());
                if (polygon == null) {
                    continue;
                }
                Overlay.drawOutlineAndFill(graphics2D, this.config.tornadoOutlineColor(), this.config.tornadoFillColor(), (float)this.config.tornadoTileOutlineWidth(), polygon);
            }
            if (!this.config.tornadoTickCounter()) {
                continue;
            }
            final String ticksLeftStr = String.valueOf(timeLeft);
            final Point point = npc.getCanvasTextLocation(graphics2D, ticksLeftStr, 0);
            if (point == null) {
                return;
            }
            OverlayUtil.renderTextLocation(graphics2D, ticksLeftStr, this.config.tornadoFontSize(), this.config.tornadoFontStyle().getFont(), this.config.tornadoFontColor(), point, this.config.tornadoFontShadow(), 0);
        }
    }
    
    private void renderProjectile(final Graphics2D graphics2D) {
        if ((!this.config.outlineProjectile() && !this.config.overlayProjectileIcon()) || this.plugin.getMissile() == null) {
            return;
        }
        final Missile missile = this.plugin.getMissile();
        final Polygon polygon = getProjectilePolygon(this.client, missile.getProjectile());
        if (polygon == null) {
            return;
        }
        if (this.config.outlineProjectile()) {
            final Color originalColor = graphics2D.getColor();
            graphics2D.setColor(missile.getOutlineColor());
            graphics2D.draw(polygon);
            graphics2D.setColor(missile.getFillColor());
            graphics2D.fill(polygon);
            graphics2D.setColor(originalColor);
        }
        if (this.config.overlayProjectileIcon()) {
            final BufferedImage icon = missile.getIcon();
            final Rectangle bounds = polygon.getBounds();
            final int x = (int)bounds.getCenterX() - icon.getWidth() / 2;
            final int y = (int)bounds.getCenterY() - icon.getHeight() / 2;
            graphics2D.drawImage(icon, x, y, null);
        }
    }
    
    private void renderHunllefWrongPrayerOutline() {
        if (!this.config.hunllefOverlayWrongPrayerOutline()) {
            return;
        }
        final Hunllef.AttackPhase phase = this.hunllef.getAttackPhase();
        if (this.client.isPrayerActive(phase.getPrayer())) {
            return;
        }
        this.modelOutlineRenderer.drawOutline(this.hunllef.getNpc(), this.config.hunllefWrongPrayerOutlineWidth(), phase.getColor(), 0);
    }
    
    private void renderHunllefAttackCounter(final Graphics2D graphics2D) {
        if (!this.config.hunllefOverlayAttackCounter()) {
            return;
        }
        final NPC npc = this.hunllef.getNpc();
        final String text = String.format("%d | %d", this.hunllef.getAttackCount(), this.hunllef.getPlayerAttackCount());
        final Point point = npc.getCanvasTextLocation(graphics2D, text, 0);
        if (point == null) {
            return;
        }
        final Font originalFont = graphics2D.getFont();
        graphics2D.setFont(new Font("SansSerif", this.config.hunllefAttackCounterFontStyle().getFont(), this.config.hunllefAttackCounterFontSize()));
        OverlayUtil.renderTextLocation(graphics2D, point, text, this.hunllef.getAttackPhase().getColor());
        graphics2D.setFont(originalFont);
    }
    
    private void renderHunllefAttackStyleIcon(final Graphics2D graphics2D) {
        if (!this.config.hunllefOverlayAttackStyleIcon()) {
            return;
        }
        final NPC npc = this.hunllef.getNpc();
        final BufferedImage icon = this.hunllef.getIcon();
        final Point point = Perspective.getCanvasImageLocation(this.client, npc.getLocalLocation(), icon, npc.getLogicalHeight() - 100);
        if (point == null) {
            return;
        }
        graphics2D.drawImage(icon, point.getX(), point.getY(), null);
    }
    
    private void renderHunllefTile(final Graphics2D graphics2D) {
        if (!this.config.hunllefOutlineTile()) {
            return;
        }
        final NPC npc = this.hunllef.getNpc();
        final NPCComposition npcComposition = npc.getComposition();
        if (npcComposition == null) {
            return;
        }
        final Polygon polygon = Perspective.getCanvasTileAreaPoly(this.client, npc.getLocalLocation(), npcComposition.getSize());
        if (polygon == null) {
            return;
        }
        Overlay.drawOutlineAndFill(graphics2D, this.config.hunllefOutlineColor(), this.config.hunllefFillColor(), (float)this.config.hunllefTileOutlineWidth(), polygon);
    }
    
    private void renderFlashOnWrongAttack(final Graphics2D graphics2D) {
        if (!this.config.flashOnWrongAttack() || !this.plugin.isWrongAttackStyle()) {
            return;
        }
        final Color originalColor = graphics2D.getColor();
        graphics2D.setColor(this.config.flashOnWrongAttackColor());
        graphics2D.fill(this.client.getCanvas().getBounds());
        graphics2D.setColor(originalColor);
        if (++this.timeout >= this.config.flashOnWrongAttackDuration()) {
            this.timeout = 0;
            this.plugin.setWrongAttackStyle(false);
        }
    }
    
    private void renderFlashOn51Method(final Graphics2D graphics2D) {
        if (!this.config.flashOn51Method() || !this.plugin.isSwitchWeapon()) {
            return;
        }
        final Color originalColor = graphics2D.getColor();
        graphics2D.setColor(this.config.flashOn51MethodColor());
        graphics2D.fill(this.client.getCanvas().getBounds());
        graphics2D.setColor(originalColor);
        if (++this.timeout >= this.config.flashOn51MethodDuration()) {
            this.timeout = 0;
            this.plugin.setSwitchWeapon(false);
        }
    }
    
    private void renderDiscoMode() {
        if (!this.config.discoMode()) {
            return;
        }
        if (++this.timeout > 10) {
            this.timeout = 0;
            this.idx = ((this.idx >= OverlayHunllef.COLORS.length - 1) ? 0 : (this.idx + 1));
        }
        this.modelOutlineRenderer.drawOutline(this.hunllef.getNpc(), 6, OverlayHunllef.COLORS[this.idx], 4);
    }
    
    private static Polygon getProjectilePolygon(final Client client, final Projectile projectile) {
        if (projectile == null || projectile.getModel() == null) {
            return null;
        }
        final Model model = projectile.getModel();
        final LocalPoint localPoint = new LocalPoint((int)projectile.getX(), (int)projectile.getY());
        final int tileHeight = Perspective.getTileHeight(client, localPoint, client.getPlane());
        double angle = Math.atan(projectile.getVelocityY() / projectile.getVelocityX());
        angle = Math.toDegrees(angle) + ((projectile.getVelocityX() < 0.0) ? 180 : 0);
        angle = ((angle < 0.0) ? (angle + 360.0) : angle);
        angle = 360.0 - angle - 90.0;
        double ori = angle * 5.688888888888889;
        ori = ((ori < 0.0) ? (ori + 2048.0) : ori);
        final int orientation = (int)Math.round(ori);
        final List<Vertex> vertices = getVertices(model);
        for (int i = 0; i < vertices.size(); ++i) {
            vertices.set(i, vertices.get(i).rotate(orientation));
        }
        final List<Point> list = new ArrayList<Point>();
        for (final Vertex vertex : vertices) {
            final Point point = Perspective.localToCanvas(client, localPoint.getX() - vertex.getX(), localPoint.getY() - vertex.getZ(), tileHeight + vertex.getY() + (int)projectile.getZ());
            if (point == null) {
                continue;
            }
            list.add(point);
        }
        final List<Point> convexHull = (List<Point>)Jarvis.convexHull((List)list);
        if (convexHull == null) {
            return null;
        }
        final Polygon polygon = new Polygon();
        for (final Point point2 : convexHull) {
            polygon.addPoint(point2.getX(), point2.getY());
        }
        return polygon;
    }
    
    public static List<Vertex> getVertices(final Model model) {
        final int[] verticesX = model.getVerticesX();
        final int[] verticesY = model.getVerticesY();
        final int[] verticesZ = model.getVerticesZ();
        final List<Vertex> vertices = new ArrayList<Vertex>(model.getVerticesCount());
        for (int i = 0; i < model.getVerticesCount(); ++i) {
            final Vertex v = new Vertex(verticesX[i], verticesY[i], verticesZ[i]);
            vertices.add(v);
        }
        return vertices;
    }
    
    static {
        COLORS = new Color[] { Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.WHITE, Color.CYAN, Color.MAGENTA, Color.PINK, Color.YELLOW, Color.DARK_GRAY, Color.LIGHT_GRAY };
    }
}
