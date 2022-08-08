// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.overlay;

import com.google.common.base.Strings;
import java.awt.Font;
import net.runelite.api.Point;
import java.awt.Polygon;
import net.runelite.api.widgets.Widget;
import java.awt.Shape;
import com.gauntletextended.InterfaceTab;
import java.awt.Rectangle;
import java.awt.Color;
import com.gauntletextended.Prayer;
import net.runelite.api.Client;
import java.awt.Graphics2D;

public class OverlayUtil
{
    public static Rectangle renderPrayerOverlay(final Graphics2D graphics, final Client client, final Prayer prayer, final Color color) {
        final Widget widget = client.getWidget(prayer.getWidgetInfo());
        if (widget == null || client.getVarcIntValue(171) != InterfaceTab.PRAYER.getId()) {
            return null;
        }
        final Rectangle bounds = widget.getBounds();
        net.runelite.client.ui.overlay.OverlayUtil.renderPolygon(graphics, (Shape)rectangleToPolygon(bounds), color);
        return bounds;
    }
    
    private static Polygon rectangleToPolygon(final Rectangle rect) {
        final int[] xpoints = { rect.x, rect.x + rect.width, rect.x + rect.width, rect.x };
        final int[] ypoints = { rect.y, rect.y, rect.y + rect.height, rect.y + rect.height };
        return new Polygon(xpoints, ypoints, 4);
    }
    
    public static void renderTextLocation(final Graphics2D graphics, final String txtString, final int fontSize, final int fontStyle, final Color fontColor, final Point canvasPoint, final boolean shadows, final int yOffset) {
        graphics.setFont(new Font("Arial", fontStyle, fontSize));
        if (canvasPoint != null) {
            final Point canvasCenterPoint = new Point(canvasPoint.getX(), canvasPoint.getY() + yOffset);
            final Point canvasCenterPoint_shadow = new Point(canvasPoint.getX() + 1, canvasPoint.getY() + 1 + yOffset);
            if (shadows) {
                renderTextLocation(graphics, canvasCenterPoint_shadow, txtString, Color.BLACK);
            }
            renderTextLocation(graphics, canvasCenterPoint, txtString, fontColor);
        }
    }
    
    public static void renderTextLocation(final Graphics2D graphics, final Point txtLoc, final String text, final Color color) {
        if (Strings.isNullOrEmpty(text)) {
            return;
        }
        final int x = txtLoc.getX();
        final int y = txtLoc.getY();
        graphics.setColor(Color.BLACK);
        graphics.drawString(text, x + 1, y + 1);
        graphics.setColor(color);
        graphics.drawString(text, x, y);
    }
}
