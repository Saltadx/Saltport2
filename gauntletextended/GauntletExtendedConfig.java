// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended;

import net.runelite.client.config.Alpha;
import java.awt.Color;
import net.runelite.client.config.Range;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.Config;

@ConfigGroup("gauntlet")
public interface GauntletExtendedConfig extends Config
{
    @ConfigSection(name = "Resources", description = "Resources section.", position = 0)
    public static final String resourcesSection = "Resources";
    @ConfigSection(name = "Utilities", description = "Utilities section.", position = 1)
    public static final String utilitiesSection = "Utilities";
    @ConfigSection(name = "Npcs", description = "Other npcs section.", position = 2)
    public static final String npcSection = "Npcs";
    @ConfigSection(name = "Hunllef", description = "Hunllef section.", position = 3)
    public static final String hunllefSection = "Hunllef";
    @ConfigSection(name = "Projectiles", description = "Projectiles section.", position = 4)
    public static final String projectilesSection = "Projectiles";
    @ConfigSection(name = "Tornadoes", description = "Tornadoes section.", position = 5)
    public static final String tornadoesSection = "Tornadoes";
    @ConfigSection(name = "Player", description = "Player section.", position = 6)
    public static final String playerSection = "Player";
    @ConfigSection(name = "Timer", description = "Timer section.", position = 7)
    public static final String timerSection = "Timer";
    @ConfigSection(name = "Other", description = "Other section.", position = 8)
    public static final String otherSection = "Other";
    
    @ConfigItem(name = "Overlay resource icon and tile", description = "Overlay resources with a respective icon and tile outline.", position = 0, keyName = "resourceOverlay", section = "Resources")
    default boolean resourceOverlay() {
        return false;
    }
    
    @Range(min = 12, max = 64)
    @ConfigItem(name = "Icon size", description = "Change the size of the resource icons.", position = 1, keyName = "resourceIconSize", section = "Resources")
    @Units("pt")
    default int resourceIconSize() {
        return 18;
    }
    
    @Range(min = 1, max = 8)
    @ConfigItem(name = "Tile outline width", description = "Change the width of the resource tile outline.", position = 2, keyName = "resourceTileOutlineWidth", section = "Resources")
    @Units("pt")
    default int resourceTileOutlineWidth() {
        return 1;
    }
    
    @Alpha
    @ConfigItem(name = "Tile outline color", description = "Change the tile outline color of resources.", position = 3, keyName = "resourceTileOutlineColor", section = "Resources")
    default Color resourceTileOutlineColor() {
        return Color.YELLOW;
    }
    
    @Alpha
    @ConfigItem(name = "Tile fill color", description = "Change the tile fill color of resources.", position = 4, keyName = "resourceTileFillColor", section = "Resources")
    default Color resourceTileFillColor() {
        return new Color(255, 255, 255, 50);
    }
    
    @ConfigItem(name = "Outline resources", description = "Outline resources with a colored outline.", position = 5, keyName = "resourceOutline", section = "Resources")
    default boolean resourceOutline() {
        return false;
    }
    
    @Range(min = 1, max = 8)
    @ConfigItem(name = "Outline width", description = "Change the width of the resource outline.", position = 6, keyName = "resourceOutlineWidth", section = "Resources")
    @Units("pt")
    default int resourceOutlineWidth() {
        return 1;
    }
    
    @Alpha
    @ConfigItem(name = "Outline color", description = "Change the outline color of resources.", position = 7, keyName = "resourceOutlineColor", section = "Resources")
    default Color resourceOutlineColor() {
        return Color.YELLOW;
    }
    
    @ConfigItem(name = "Track resources", description = "Track resources in counter infoboxes.", position = 8, keyName = "resourceTracker")
    default ResourceFilter resourceTracker() {
        return ResourceFilter.OFF;
    }
    
    @ConfigItem(name = "Ore", description = "The desired number of ores to acquire.", position = 9, keyName = "resourceOre", section = "Resources")
    default int resourceOre() {
        return 3;
    }
    
    @ConfigItem(name = "Phren bark", description = "The desired number of phren barks to acquire.", position = 10, keyName = "resourceBark", section = "Resources")
    default int resourceBark() {
        return 3;
    }
    
    @ConfigItem(name = "Linum tirinum", description = "The desired number of linum tirinums to acquire.", position = 11, keyName = "resourceTirinum", section = "Resources")
    default int resourceTirinum() {
        return 3;
    }
    
    @ConfigItem(name = "Grym leaf", description = "The desired number of grym leaves to acquire.", position = 12, keyName = "resourceGrym", section = "Resources")
    default int resourceGrym() {
        return 2;
    }
    
    @ConfigItem(name = "Weapon frames", description = "The desired number of weapon frames to acquire.", position = 13, keyName = "resourceFrame", section = "Resources")
    default int resourceFrame() {
        return 2;
    }
    
    @ConfigItem(name = "Paddlefish", description = "The desired number of paddlefish to acquire.", position = 14, keyName = "resourcePaddlefish", section = "Resources")
    default int resourcePaddlefish() {
        return 20;
    }
    
    @ConfigItem(name = "Crystal shards", description = "The desired number of crystal shards to acquire.", position = 15, keyName = "resourceShard", section = "Resources")
    default int resourceShard() {
        return 320;
    }
    
    @ConfigItem(name = "Bowstring", description = "Whether or not to acquire the crystalline or corrupted bowstring.", position = 16, keyName = "resourceBowstring", section = "Resources")
    default boolean resourceBowstring() {
        return false;
    }
    
    @ConfigItem(name = "Spike", description = "Whether or not to acquire the crystal or corrupted spike.", position = 17, keyName = "resourceSpike", section = "Resources")
    default boolean resourceSpike() {
        return false;
    }
    
    @ConfigItem(name = "Orb", description = "Whether or not to acquire the crystal or corrupted orb.", position = 18, keyName = "resourceOrb", section = "Resources")
    default boolean resourceOrb() {
        return false;
    }
    
    @ConfigItem(name = "Outline starting room utilities", description = "Outline various utilities in the starting room.", position = 0, keyName = "utilitiesOutline", section = "Utilities")
    default boolean utilitiesOutline() {
        return false;
    }
    
    @Range(min = 2, max = 12)
    @ConfigItem(name = "Outline width", description = "Change the width of the utilities outline.", position = 1, keyName = "utilitiesOutlineWidth", section = "Utilities")
    @Units("pt")
    default int utilitiesOutlineWidth() {
        return 4;
    }
    
    @Alpha
    @ConfigItem(name = "Outline color", description = "Change the color of the utilities outline.", position = 2, keyName = "utilitiesOutlineColor", section = "Utilities")
    default Color utilitiesOutlineColor() {
        return Color.MAGENTA;
    }
    
    @ConfigItem(name = "Outline demi-bosses", description = "Overlay demi-bosses with a colored outline.", position = 0, keyName = "demibossOutline", section = "Npcs")
    default boolean demibossOutline() {
        return false;
    }
    
    @Range(min = 2, max = 12)
    @ConfigItem(name = "Outline width", description = "Change the width of the demi-boss outline.", position = 1, keyName = "demibossOutlineWidth", section = "Npcs")
    @Units("pt")
    default int demibossOutlineWidth() {
        return 4;
    }
    
    @ConfigItem(name = "Outline strong npcs", description = "Overlay strong npcs with a colored outline.", position = 2, keyName = "strongNpcOutline", section = "Npcs")
    default boolean strongNpcOutline() {
        return false;
    }
    
    @Range(min = 2, max = 12)
    @ConfigItem(name = "Outline width", description = "Change the width of the strong npcs outline.", position = 3, keyName = "strongNpcOutlineWidth", section = "Npcs")
    @Units("pt")
    default int strongNpcOutlineWidth() {
        return 2;
    }
    
    @Alpha
    @ConfigItem(name = "Outline color", description = "Change the outline color of strong npcs.", position = 4, keyName = "strongNpcOutlineColor", section = "Npcs")
    default Color strongNpcOutlineColor() {
        return Color.CYAN;
    }
    
    @ConfigItem(name = "Outline weak npcs", description = "Overlay weak npcs with a colored outline.", position = 5, keyName = "weakNpcOutline", section = "Npcs")
    default boolean weakNpcOutline() {
        return false;
    }
    
    @Range(min = 2, max = 12)
    @ConfigItem(name = "Outline width", description = "Change the width of the weak npcs outline.", position = 6, keyName = "weakNpcOutlineWidth", section = "Npcs")
    @Units("pt")
    default int weakNpcOutlineWidth() {
        return 2;
    }
    
    @Alpha
    @ConfigItem(name = "Outline color", description = "Change the outline color of weak npcs.", position = 7, keyName = "weakNpcOutlineColor", section = "Npcs")
    default Color weakNpcOutlineColor() {
        return Color.CYAN;
    }
    
    @ConfigItem(name = "Display counter on Hunllef", description = "Overlay the Hunllef with an attack and prayer counter.", position = 0, keyName = "hunllefOverlayAttackCounter", section = "Hunllef")
    default boolean hunllefOverlayAttackCounter() {
        return false;
    }
    
    @ConfigItem(name = "Counter font style", description = "Change the font style of the attack and prayer counter.", position = 1, keyName = "hunllefAttackCounterFontStyle", section = "Hunllef")
    default FontStyle hunllefAttackCounterFontStyle() {
        return FontStyle.BOLD;
    }
    
    @Range(min = 12, max = 64)
    @ConfigItem(name = "Counter font size", description = "Adjust the font size of the attack and prayer counter.", position = 2, keyName = "hunllefAttackCounterFontSize", section = "Hunllef")
    @Units("pt")
    default int hunllefAttackCounterFontSize() {
        return 22;
    }
    
    @ConfigItem(name = "Outline Hunllef on wrong prayer", description = "Outline the Hunllef when incorrectly praying against its current attack style.", position = 3, keyName = "hunllefOverlayWrongPrayerOutline", section = "Hunllef")
    default boolean hunllefOverlayWrongPrayerOutline() {
        return false;
    }
    
    @Range(min = 2, max = 12)
    @ConfigItem(name = "Outline width", description = "Change the width of the wrong prayer outline.", position = 4, keyName = "hunllefWrongPrayerOutlineWidth", section = "Hunllef")
    @Units("pt")
    default int hunllefWrongPrayerOutlineWidth() {
        return 4;
    }
    
    @ConfigItem(name = "Outline Hunllef tile", description = "Outline the Hunllef's tile.", position = 5, keyName = "hunllefOutlineTile", section = "Hunllef")
    default boolean hunllefOutlineTile() {
        return false;
    }
    
    @Range(min = 1, max = 8)
    @ConfigItem(name = "Tile outline width", description = "Change the width of the Hunllef's tile outline.", position = 6, keyName = "hunllefTileOutlineWidth", section = "Hunllef")
    @Units("pt")
    default int hunllefTileOutlineWidth() {
        return 1;
    }
    
    @Alpha
    @ConfigItem(name = "Tile outline color", description = "Change the outline color of the Hunllef's tile.", position = 7, keyName = "hunllefOutlineColor", section = "Hunllef")
    default Color hunllefOutlineColor() {
        return Color.WHITE;
    }
    
    @Alpha
    @ConfigItem(name = "Tile fill color", description = "Change the fill color of the Hunllef's tile.", position = 8, keyName = "hunllefFillColor", section = "Hunllef")
    default Color hunllefFillColor() {
        return new Color(255, 255, 255, 0);
    }
    
    @ConfigItem(name = "Overlay style icon on Hunllef", description = "Overlay a current attack style icon on the Hunllef.", position = 9, keyName = "hunllefOverlayAttackStyleIcon", section = "Hunllef")
    default boolean hunllefOverlayAttackStyleIcon() {
        return false;
    }
    
    @Range(min = 12, max = 64)
    @ConfigItem(name = "Icon size", description = "Change the size of the attack style icon.", position = 10, keyName = "hunllefAttackStyleIconSize", section = "Hunllef")
    @Units("pt")
    default int hunllefAttackStyleIconSize() {
        return 18;
    }
    
    @ConfigItem(name = "Play audio on prayer attack", description = "Play an in-game sound when the Hunllef is about to use its prayer attack.", position = 11, keyName = "hunllefPrayerAudio", section = "Hunllef")
    default boolean hunllefPrayerAudio() {
        return false;
    }
    
    @ConfigItem(name = "Outline projectiles", description = "Outline projectiles with a blue (magic) or green (range) color.", position = 0, keyName = "outlineProjectile", section = "Projectiles")
    default boolean outlineProjectile() {
        return false;
    }
    
    @ConfigItem(name = "Overlay projectile icons", description = "Overlay projectiles with their respective icon.", position = 1, keyName = "overlayProjectileIcon", section = "Projectiles")
    default boolean overlayProjectileIcon() {
        return false;
    }
    
    @Range(min = 12, max = 64)
    @ConfigItem(name = "Icon size", description = "Change the size of the projectile icons.", position = 2, keyName = "projectileIconSize", section = "Projectiles")
    @Units("pt")
    default int projectileIconSize() {
        return 18;
    }
    
    @ConfigItem(name = "Overlay tornado tick counter", description = "Overlay tornadoes with a tick counter.", position = 0, keyName = "tornadoTickCounter", section = "Tornadoes")
    default boolean tornadoTickCounter() {
        return false;
    }
    
    @ConfigItem(name = "Font style", description = "Bold/Italics/Plain", position = 1, keyName = "tornadoFontStyle", section = "Tornadoes")
    default FontStyle tornadoFontStyle() {
        return FontStyle.BOLD;
    }
    
    @ConfigItem(name = "Font shadow", description = "Toggle font shadow of the tornado tick counter.", position = 2, keyName = "tornadoFontShadow", section = "Tornadoes")
    default boolean tornadoFontShadow() {
        return true;
    }
    
    @Range(min = 12, max = 64)
    @ConfigItem(name = "Font size", description = "Adjust the font size of the tornado tick counter.", position = 3, keyName = "tornadoFontSize", section = "Tornadoes")
    @Units("pt")
    default int tornadoFontSize() {
        return 16;
    }
    
    @Alpha
    @ConfigItem(name = "Font color", description = "Color of the tornado tick counter font.", position = 4, keyName = "tornadoFontColor", section = "Tornadoes")
    default Color tornadoFontColor() {
        return Color.WHITE;
    }
    
    @ConfigItem(name = "Outline tornado tile", description = "Outline the tiles of tornadoes.", position = 5, keyName = "tornadoTileOutline", section = "Tornadoes")
    default boolean tornadoTileOutline() {
        return false;
    }
    
    @Range(min = 1, max = 8)
    @ConfigItem(name = "Tile outline width", description = "Change tile outline width of tornadoes.", position = 6, keyName = "tornadoTileOutlineWidth", section = "Tornadoes")
    @Units("pt")
    default int tornadoTileOutlineWidth() {
        return 1;
    }
    
    @Alpha
    @ConfigItem(name = "Tile outline color", description = "Color to outline the tile of a tornado.", position = 7, keyName = "tornadoOutlineColor", section = "Tornadoes")
    default Color tornadoOutlineColor() {
        return Color.YELLOW;
    }
    
    @Alpha
    @ConfigItem(name = "Tile fill color", description = "Color to fill the tile of a tornado.", position = 8, keyName = "tornadoFillColor", section = "Tornadoes")
    default Color tornadoFillColor() {
        return new Color(255, 255, 0, 50);
    }
    
    @ConfigItem(name = "Overlay prayer", description = "Overlay the correct prayer to use against the Hunllef's current attack style.", position = 0, keyName = "prayerOverlay")
    default PrayerHighlightMode prayerOverlay() {
        return PrayerHighlightMode.NONE;
    }
    
    @ConfigItem(name = "Flash on wrong attack style", description = "Flash the screen if you use the wrong attack style.", position = 1, keyName = "flashOnWrongAttack", section = "Player")
    default boolean flashOnWrongAttack() {
        return false;
    }
    
    @Range(min = 10, max = 50)
    @ConfigItem(name = "Flash duration", description = "Change the duration of the flash.", position = 2, keyName = "flashOnWrongAttackDuration", section = "Player")
    default int flashOnWrongAttackDuration() {
        return 25;
    }
    
    @Alpha
    @ConfigItem(name = "Flash color", description = "Color of the flash notification.", position = 3, keyName = "flashOnWrongAttackColor", section = "Player")
    default Color flashOnWrongAttackColor() {
        return new Color(255, 0, 0, 70);
    }
    
    @ConfigItem(name = "Flash on 5:1 method", description = "Flash the screen to weapon switch when using 5:1 method.", position = 4, keyName = "flashOn51Method", section = "Player")
    default boolean flashOn51Method() {
        return false;
    }
    
    @Range(min = 10, max = 50)
    @ConfigItem(name = "Flash duration", description = "Change the duration of the flash.", position = 5, keyName = "flashOn51MethodDuration", section = "Player")
    default int flashOn51MethodDuration() {
        return 25;
    }
    
    @Alpha
    @ConfigItem(name = "Flash color", description = "Color of the flash notification.", position = 6, keyName = "flashOn51MethodColor", section = "Player")
    default Color flashOn51MethodColor() {
        return new Color(255, 190, 0, 50);
    }
    
    @ConfigItem(position = 0, keyName = "timerOverlay", name = "Overlay timer", description = "Display an overlay that tracks your gauntlet time.", section = "Timer")
    default boolean timerOverlay() {
        return false;
    }
    
    @ConfigItem(position = 1, keyName = "timerChatMessage", name = "Chat timer", description = "Display a chat message on death with your gauntlet time.", section = "Timer")
    default boolean timerChatMessage() {
        return false;
    }
    
    @ConfigItem(name = "Render distance", description = "Set render distance of various overlays.", position = 0, keyName = "resourceRenderDistance", section = "Other")
    default RenderDistance resourceRenderDistance() {
        return RenderDistance.FAR;
    }
    
    @ConfigItem(name = "Disco mode", description = "Kill the Hunllef.", position = 1, keyName = "discoMode", section = "Other")
    default boolean discoMode() {
        return false;
    }
    
    public enum RenderDistance
    {
        SHORT("Short", 2350), 
        MEDIUM("Medium", 3525), 
        FAR("Far", 4700), 
        UNCAPPED("Uncapped", 0);
        
        private final String name;
        private final int distance;
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getDistance() {
            return this.distance;
        }
        
        private RenderDistance(final String name, final int distance) {
            this.name = name;
            this.distance = distance;
        }
    }
    
    public enum FontStyle
    {
        BOLD("Bold", 1), 
        ITALIC("Italic", 2), 
        PLAIN("Plain", 0);
        
        private final String name;
        private final int font;
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getFont() {
            return this.font;
        }
        
        private FontStyle(final String name, final int font) {
            this.name = name;
            this.font = font;
        }
    }
    
    public enum PrayerHighlightMode
    {
        WIDGET("Widget"), 
        BOX("Box"), 
        BOTH("Both"), 
        NONE("None");
        
        private final String name;
        
        @Override
        public String toString() {
            return this.name;
        }
        
        private PrayerHighlightMode(final String name) {
            this.name = name;
        }
    }
    
    public enum ResourceFilter
    {
        ALL, 
        BASIC, 
        CUSTOM, 
        OFF;
    }
}
