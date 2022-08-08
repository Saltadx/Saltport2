// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.resource;

import java.util.regex.Matcher;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

enum Resource
{
    TELEPORT_CRYSTAL("Teleport crystal", 23904, (String)null, false), 
    CORRUPTED_TELEPORT_CRYSTAL("Corrupted teleport crystal", 23858, (String)null, true), 
    WEAPON_FRAME("Weapon frame", 23871, (String)null, false), 
    CORRUPTED_WEAPON_FRAME("Weapon frame", 23834, (String)null, true), 
    CRYSTALLINE_BOWSTRING("Crystalline bowstring", 23869, (String)null, false), 
    CORRUPTED_BOWSTRING("Corrupted bowstring", 23832, (String)null, true), 
    CRYSTAL_SPIKE("Crystal spike", 23868, (String)null, false), 
    CORRUPTED_SPIKE("Corrupted spike", 23831, (String)null, true), 
    CRYSTAL_ORB("Crystal orb", 23870, (String)null, false), 
    CORRUPTED_ORB("Corrupted orb", 23833, (String)null, true), 
    RAW_PADDLEFISH("Raw paddlefish", 23872, "You manage to catch a fish\\.", false), 
    CRYSTAL_SHARDS("Crystal shards", 23866, "You find (\\d+) crystal shards\\.", false), 
    CORRUPTED_SHARDS("Corrupted shards", 23824, "You find (\\d+) corrupted shards\\.", true), 
    CRYSTAL_ORE("Crystal ore", 23877, "You manage to mine some ore\\.", false), 
    CORRUPTED_ORE("Corrupted ore", 23837, "You manage to mine some ore\\.", true), 
    PHREN_BARK("Phren bark", 23878, "You get some bark\\.", false), 
    CORRUPTED_PHREN_BARK("Phren bark", 23838, "You get some bark\\.", true), 
    LINUM_TIRINUM("Linum tirinum", 23876, "You pick some fibre from the plant\\.", false), 
    CORRUPTED_LINUM_TIRINUM("Linum tirinum", 23836, "You pick some fibre from the plant\\.", true), 
    GRYM_LEAF("Grym leaf", 23875, "You pick a herb from the roots\\.", false), 
    CORRUPTED_GRYM_LEAF("Grym leaf", 23835, "You pick a herb from the roots\\.", true);
    
    private final String name;
    private final Pattern pattern;
    private final int itemId;
    private final boolean corrupted;
    
    private Resource(final String name, final int itemId, final String pattern, final boolean corrupted) {
        this.name = name;
        this.itemId = itemId;
        this.corrupted = corrupted;
        this.pattern = ((pattern != null) ? Pattern.compile(pattern) : null);
    }
    
    static Resource fromName(final String name, final boolean corrupted) {
        for (final Resource resource : values()) {
            if (resource.name.equals(name) && (corrupted == resource.corrupted || resource == Resource.RAW_PADDLEFISH)) {
                return resource;
            }
        }
        return null;
    }
    
    static Map<Resource, Integer> fromPattern(final String pattern, final boolean corrupted) {
        for (final Resource resource : values()) {
            if (resource.pattern != null) {
                if (corrupted == resource.corrupted || resource == Resource.RAW_PADDLEFISH) {
                    final Matcher matcher = resource.pattern.matcher(pattern);
                    if (matcher.matches()) {
                        final int itemCount = (matcher.groupCount() == 1) ? Integer.parseInt(matcher.group(1)) : 1;
                        return Collections.singletonMap(resource, itemCount);
                    }
                }
            }
        }
        return null;
    }
    
    Pattern getPattern() {
        return this.pattern;
    }
    
    int getItemId() {
        return this.itemId;
    }
    
    boolean isCorrupted() {
        return this.corrupted;
    }
}
