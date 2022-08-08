// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.resource;

import net.runelite.api.Player;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.function.Predicate;
import java.util.HashMap;
import java.util.Map;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.game.ItemManager;
import com.gauntletextended.GauntletExtendedConfig;
import com.gauntletextended.GauntletExtendedPlugin;
import javax.inject.Inject;
import net.runelite.api.Client;
import java.util.regex.Pattern;
import javax.inject.Singleton;

@Singleton
public class ResourceManager
{
    private static final int NORMAL_GAUNTLET_REGION_ID = 7512;
    private static final int CORRUPTED_GAUNTLET_REGION_ID = 7768;
    private static final String MESSAGE_UNTRADEABLE_DROP = "Untradeable drop: ";
    private static final Pattern PATTERN_RESOURCE_DROP;
    @Inject
    private Client client;
    @Inject
    private GauntletExtendedPlugin plugin;
    @Inject
    private GauntletExtendedConfig config;
    @Inject
    private ItemManager itemManager;
    @Inject
    private InfoBoxManager infoBoxManager;
    private final Map<Resource, ResourceCounter> resources;
    private Region region;
    private String prefix;
    
    public ResourceManager() {
        this.resources = new HashMap<Resource, ResourceCounter>();
        this.region = Region.UNKNOWN;
    }
    
    public void init() {
        this.prefix = (this.isLootVarbitSet() ? "Untradeable drop: " : this.getNamedDropMessage());
        this.region = this.getRegion();
        this.createCustomCounters();
    }
    
    public void reset() {
        this.prefix = null;
        this.region = Region.UNKNOWN;
        this.resources.clear();
        this.infoBoxManager.removeIf((Predicate)ResourceCounter.class::isInstance);
    }
    
    public void parseChatMessage(String chatMessage) {
        if (this.config.resourceTracker() == GauntletExtendedConfig.ResourceFilter.OFF || this.region == Region.UNKNOWN || this.prefix == null) {
            return;
        }
        chatMessage = Text.removeTags(chatMessage);
        if (chatMessage.startsWith(this.prefix)) {
            chatMessage = chatMessage.replace(this.prefix, "");
            this.processNpcResource(chatMessage);
        }
        else {
            this.processSkillResource(chatMessage);
        }
    }
    
    private void processNpcResource(final String chatMessage) {
        final Matcher matcher = ResourceManager.PATTERN_RESOURCE_DROP.matcher(chatMessage);
        if (!matcher.matches()) {
            return;
        }
        final String itemName = matcher.group("name");
        if (itemName == null) {
            return;
        }
        final Resource resource = Resource.fromName(itemName, this.region == Region.CORRUPTED);
        if (resource == null || (this.config.resourceTracker() == GauntletExtendedConfig.ResourceFilter.CUSTOM && !this.resources.containsKey(resource)) || (this.config.resourceTracker() == GauntletExtendedConfig.ResourceFilter.BASIC && isNonBasicResource(resource))) {
            return;
        }
        final String quantity = matcher.group("quantity");
        final int itemCount = (quantity != null) ? Integer.parseInt(quantity) : 1;
        this.processResource(resource, itemCount);
    }
    
    private void processSkillResource(final String chatMessage) {
        final Map<Resource, Integer> mapping = Resource.fromPattern(chatMessage, this.region == Region.CORRUPTED);
        if (mapping == null) {
            return;
        }
        final Resource resource = mapping.keySet().iterator().next();
        if (this.config.resourceTracker() == GauntletExtendedConfig.ResourceFilter.CUSTOM && !this.resources.containsKey(resource)) {
            return;
        }
        final int itemCount = mapping.get(resource);
        this.processResource(resource, itemCount);
    }
    
    private void processResource(final Resource resource, final int itemCount) {
        if (!this.resources.containsKey(resource)) {
            this.initResource(resource, itemCount);
        }
        else {
            final ResourceCounter counter = this.resources.get(resource);
            if (this.config.resourceTracker() == GauntletExtendedConfig.ResourceFilter.CUSTOM) {
                counter.decrementCount(itemCount);
            }
            else {
                counter.incrementCount(itemCount);
            }
        }
    }
    
    private void initResource(final Resource resource, final int itemCount) {
        final ResourceCounter counter = new ResourceCounter(this.plugin, resource, (BufferedImage)this.itemManager.getImage(resource.getItemId()), itemCount);
        this.resources.put(resource, counter);
        this.infoBoxManager.addInfoBox((InfoBox)counter);
    }
    
    private void createCustomCounters() {
        if (this.config.resourceTracker() != GauntletExtendedConfig.ResourceFilter.CUSTOM || this.region == Region.UNKNOWN) {
            return;
        }
        final int ore = this.config.resourceOre();
        final int bark = this.config.resourceBark();
        final int tirinum = this.config.resourceTirinum();
        final int grym = this.config.resourceGrym();
        final int frame = this.config.resourceFrame();
        final int fish = this.config.resourcePaddlefish();
        final int shard = this.config.resourceShard();
        final boolean bowstring = this.config.resourceBowstring();
        final boolean spike = this.config.resourceSpike();
        final boolean orb = this.config.resourceOrb();
        final boolean corrupted = this.region == Region.CORRUPTED;
        if (ore > 0) {
            this.initResource(corrupted ? Resource.CORRUPTED_ORE : Resource.CRYSTAL_ORE, ore);
        }
        if (bark > 0) {
            this.initResource(corrupted ? Resource.CORRUPTED_PHREN_BARK : Resource.PHREN_BARK, bark);
        }
        if (tirinum > 0) {
            this.initResource(corrupted ? Resource.CORRUPTED_LINUM_TIRINUM : Resource.LINUM_TIRINUM, tirinum);
        }
        if (grym > 0) {
            this.initResource(corrupted ? Resource.CORRUPTED_GRYM_LEAF : Resource.GRYM_LEAF, grym);
        }
        if (frame > 0) {
            this.initResource(corrupted ? Resource.CORRUPTED_WEAPON_FRAME : Resource.WEAPON_FRAME, frame);
        }
        if (fish > 0) {
            this.initResource(Resource.RAW_PADDLEFISH, fish);
        }
        if (shard > 0) {
            this.initResource(corrupted ? Resource.CORRUPTED_SHARDS : Resource.CRYSTAL_SHARDS, shard);
        }
        if (bowstring) {
            this.initResource(corrupted ? Resource.CORRUPTED_BOWSTRING : Resource.CRYSTALLINE_BOWSTRING, 1);
        }
        if (spike) {
            this.initResource(corrupted ? Resource.CORRUPTED_SPIKE : Resource.CRYSTAL_SPIKE, 1);
        }
        if (orb) {
            this.initResource(corrupted ? Resource.CORRUPTED_ORB : Resource.CRYSTAL_ORB, 1);
        }
    }
    
    private String getNamedDropMessage() {
        final Player player = this.client.getLocalPlayer();
        if (player == null) {
            return null;
        }
        return player.getName() + " received a drop: ";
    }
    
    private boolean isLootVarbitSet() {
        return this.client.getVarbitValue(5399) == 1 && this.client.getVarbitValue(5402) == 1;
    }
    
    private Region getRegion() {
        final int regionId = this.client.getMapRegions()[0];
        if (regionId == 7768) {
            return Region.CORRUPTED;
        }
        if (regionId == 7512) {
            return Region.NORMAL;
        }
        return Region.UNKNOWN;
    }
    
    private static boolean isNonBasicResource(final Resource resource) {
        switch (resource) {
            case TELEPORT_CRYSTAL:
            case CORRUPTED_TELEPORT_CRYSTAL:
            case WEAPON_FRAME:
            case CORRUPTED_WEAPON_FRAME:
            case CRYSTALLINE_BOWSTRING:
            case CORRUPTED_BOWSTRING:
            case CRYSTAL_SPIKE:
            case CORRUPTED_SPIKE:
            case CRYSTAL_ORB:
            case CORRUPTED_ORB: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    static {
        PATTERN_RESOURCE_DROP = Pattern.compile("((?<quantity>\\d+) x )?(?<name>.+)");
    }
    
    private enum Region
    {
        UNKNOWN, 
        NORMAL, 
        CORRUPTED;
    }
}
