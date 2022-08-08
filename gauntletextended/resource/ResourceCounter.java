// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.resource;

import net.runelite.client.ui.overlay.infobox.InfoBoxPriority;
import net.runelite.client.plugins.Plugin;
import java.awt.image.BufferedImage;
import com.gauntletextended.GauntletExtendedPlugin;
import net.runelite.client.ui.overlay.infobox.Counter;

class ResourceCounter extends Counter
{
    private final Resource resource;
    private int count;
    private String text;
    
    ResourceCounter(final GauntletExtendedPlugin plugin, final Resource resource, final BufferedImage bufferedImage, final int count) {
        super(bufferedImage, (Plugin)plugin, count);
        this.resource = resource;
        this.count = count;
        this.text = String.valueOf(count);
        this.setPriority(getPriority(resource));
    }
    
    public String getText() {
        return this.text;
    }
    
    public void incrementCount(final int count) {
        this.count += Math.max(0, count);
        this.text = String.valueOf(this.count);
    }
    
    public void decrementCount(final int count) {
        this.count -= Math.max(0, count);
        this.text = String.valueOf(this.count);
    }
    
    private static InfoBoxPriority getPriority(final Resource resource) {
        switch (resource) {
            case CRYSTAL_ORE:
            case CORRUPTED_ORE:
            case PHREN_BARK:
            case CORRUPTED_PHREN_BARK:
            case LINUM_TIRINUM:
            case CORRUPTED_LINUM_TIRINUM: {
                return InfoBoxPriority.HIGH;
            }
            case GRYM_LEAF:
            case CORRUPTED_GRYM_LEAF: {
                return InfoBoxPriority.MED;
            }
            case CRYSTAL_SHARDS:
            case CORRUPTED_SHARDS:
            case RAW_PADDLEFISH: {
                return InfoBoxPriority.NONE;
            }
            default: {
                return InfoBoxPriority.LOW;
            }
        }
    }
}
