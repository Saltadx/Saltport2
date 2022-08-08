// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import net.runelite.api.Tile;
import net.runelite.api.HeadIcon;
import net.runelite.api.Actor;
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.api.GameState;
import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;
import java.util.Arrays;
import java.util.HashSet;
import net.runelite.api.Projectile;
import com.gauntletextended.entity.Hunllef;
import com.gauntletextended.entity.Missile;
import java.util.List;
import net.runelite.api.NPC;
import com.gauntletextended.entity.Demiboss;
import com.gauntletextended.entity.Tornado;
import net.runelite.api.GameObject;
import com.gauntletextended.entity.Resource;
import com.gauntletextended.overlay.Overlay;
import com.google.common.collect.ImmutableSet;
import com.gauntletextended.overlay.OverlayPrayerBox;
import com.gauntletextended.overlay.OverlayPrayerWidget;
import com.gauntletextended.overlay.OverlayHunllef;
import com.gauntletextended.overlay.OverlayGauntlet;
import com.gauntletextended.overlay.OverlayTimer;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.game.SkillIconManager;
import com.gauntletextended.resource.ResourceManager;
import net.runelite.client.callback.ClientThread;
import javax.inject.Inject;
import net.runelite.api.Client;
import java.util.Set;
import javax.inject.Singleton;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.Plugin;

@PluginDescriptor(name = "Gauntlet Extended", enabledByDefault = false, description = "All-in-one plugin for the Gauntlet.", tags = { "gauntlet" })
@Singleton
public class GauntletExtendedPlugin extends Plugin
{
    public static final int ONEHAND_SLASH_AXE_ANIMATION = 395;
    public static final int ONEHAND_CRUSH_PICKAXE_ANIMATION = 400;
    public static final int ONEHAND_CRUSH_AXE_ANIMATION = 401;
    public static final int UNARMED_PUNCH_ANIMATION = 422;
    public static final int UNARMED_KICK_ANIMATION = 423;
    public static final int BOW_ATTACK_ANIMATION = 426;
    public static final int ONEHAND_STAB_HALBERD_ANIMATION = 428;
    public static final int ONEHAND_SLASH_HALBERD_ANIMATION = 440;
    public static final int ONEHAND_SLASH_SWORD_ANIMATION = 390;
    public static final int ONEHAND_STAB_SWORD_ANIMATION = 386;
    public static final int HIGH_LEVEL_MAGIC_ATTACK = 1167;
    public static final int HUNLEFF_TORNADO = 8418;
    private static final Set<Integer> MELEE_ANIM_IDS;
    private static final Set<Integer> ATTACK_ANIM_IDS;
    private static final Set<Integer> PROJECTILE_MAGIC_IDS;
    private static final Set<Integer> PROJECTILE_RANGE_IDS;
    private static final Set<Integer> PROJECTILE_PRAYER_IDS;
    private static final Set<Integer> PROJECTILE_IDS;
    private static final Set<Integer> HUNLLEF_IDS;
    private static final Set<Integer> TORNADO_IDS;
    private static final Set<Integer> DEMIBOSS_IDS;
    private static final Set<Integer> STRONG_NPC_IDS;
    private static final Set<Integer> WEAK_NPC_IDS;
    private static final Set<Integer> RESOURCE_IDS;
    private static final Set<Integer> UTILITY_IDS;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private GauntletExtendedConfig config;
    @Inject
    private ResourceManager resourceManager;
    @Inject
    private SkillIconManager skillIconManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private OverlayTimer overlayTimer;
    @Inject
    private OverlayGauntlet overlayGauntlet;
    @Inject
    private OverlayHunllef overlayHunllef;
    @Inject
    private OverlayPrayerWidget overlayPrayerWidget;
    @Inject
    private OverlayPrayerBox overlayPrayerBox;
    private ImmutableSet<Overlay> overlays;
    private final Set<Resource> resources;
    private final Set<GameObject> utilities;
    private final Set<Tornado> tornadoes;
    private final Set<Demiboss> demibosses;
    private final Set<NPC> strongNpcs;
    private final Set<NPC> weakNpcs;
    private final List<Set<?>> entitySets;
    private Missile missile;
    private Hunllef hunllef;
    private boolean wrongAttackStyle;
    private boolean switchWeapon;
    private Projectile lastProjectile;
    private boolean inGauntlet;
    private boolean inHunllef;
    private boolean firstProjectile;
    
    public GauntletExtendedPlugin() {
        this.resources = new HashSet<Resource>();
        this.utilities = new HashSet<GameObject>();
        this.tornadoes = new HashSet<Tornado>();
        this.demibosses = new HashSet<Demiboss>();
        this.strongNpcs = new HashSet<NPC>();
        this.weakNpcs = new HashSet<NPC>();
        this.entitySets = Arrays.asList(this.resources, this.utilities, this.tornadoes, this.demibosses, this.strongNpcs, this.weakNpcs);
        this.firstProjectile = true;
    }
    
    @Provides
    GauntletExtendedConfig getConfig(final ConfigManager configManager) {
        return (GauntletExtendedConfig)configManager.getConfig((Class)GauntletExtendedConfig.class);
    }
    
    protected void startUp() {
        if (this.overlays == null) {
            this.overlays = (ImmutableSet<Overlay>)ImmutableSet.of((Object)this.overlayTimer, (Object)this.overlayGauntlet, (Object)this.overlayHunllef, (Object)this.overlayPrayerWidget, (Object)this.overlayPrayerBox);
        }
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.clientThread.invoke(this::pluginEnabled);
        }
    }
    
    protected void shutDown() {
        this.overlays.forEach(o -> this.overlayManager.remove((net.runelite.client.ui.overlay.Overlay)o));
        this.inGauntlet = false;
        this.inHunllef = false;
        this.hunllef = null;
        this.missile = null;
        this.wrongAttackStyle = false;
        this.switchWeapon = false;
        this.overlayTimer.reset();
        this.resourceManager.reset();
        this.entitySets.forEach(Set::clear);
    }
    
    @Subscribe
    private void onConfigChanged(final ConfigChanged event) {
        if (!event.getGroup().equals("gauntlet")) {
            return;
        }
        final String key = event.getKey();
        switch (key) {
            case "resourceIconSize": {
                if (!this.resources.isEmpty()) {
                    this.resources.forEach(r -> r.setIconSize(this.config.resourceIconSize()));
                    break;
                }
                break;
            }
            case "resourceTracker": {
                if (this.inGauntlet && !this.inHunllef) {
                    this.resourceManager.reset();
                    this.resourceManager.init();
                    break;
                }
                break;
            }
            case "projectileIconSize": {
                if (this.missile != null) {
                    this.missile.setIconSize(this.config.projectileIconSize());
                    break;
                }
                break;
            }
            case "hunllefAttackStyleIconSize": {
                if (this.hunllef != null) {
                    this.hunllef.setIconSize(this.config.hunllefAttackStyleIconSize());
                    break;
                }
                break;
            }
            case "mirrorMode": {
                this.overlays.forEach(overlay -> {
                    overlay.determineLayer();
                    if (this.overlayManager.anyMatch(o -> o == overlay)) {
                        this.overlayManager.remove((net.runelite.client.ui.overlay.Overlay)overlay);
                        this.overlayManager.add((net.runelite.client.ui.overlay.Overlay)overlay);
                    }
                    return;
                });
                break;
            }
        }
    }
    
    @Subscribe
    private void onVarbitChanged(final VarbitChanged event) {
        if (this.isHunllefVarbitSet()) {
            if (!this.inHunllef) {
                this.initHunllef();
            }
        }
        else if (this.isGauntletVarbitSet()) {
            if (!this.inGauntlet) {
                this.initGauntlet();
            }
        }
        else if (this.inGauntlet || this.inHunllef) {
            this.shutDown();
        }
    }
    
    @Subscribe
    private void onGameTick(final GameTick event) {
        if (this.hunllef == null) {
            return;
        }
        this.hunllef.decrementTicksUntilNextAttack();
        if (this.missile != null && this.missile.getProjectile().getRemainingCycles() <= 0) {
            this.missile = null;
        }
        if (!this.tornadoes.isEmpty()) {
            this.tornadoes.forEach(Tornado::updateTimeLeft);
        }
    }
    
    @Subscribe
    private void onGameStateChanged(final GameStateChanged event) {
        switch (event.getGameState()) {
            case LOADING: {
                this.resources.clear();
                this.utilities.clear();
                break;
            }
            case LOGIN_SCREEN:
            case HOPPING: {
                this.shutDown();
                break;
            }
        }
    }
    
    @Subscribe
    private void onWidgetLoaded(final WidgetLoaded event) {
        if (event.getGroupId() == 637) {
            this.overlayTimer.setGauntletStart();
            this.resourceManager.init();
        }
    }
    
    @Subscribe
    private void onGameObjectSpawned(final GameObjectSpawned event) {
        final GameObject gameObject = event.getGameObject();
        final int id = gameObject.getId();
        if (GauntletExtendedPlugin.RESOURCE_IDS.contains(id)) {
            this.resources.add(new Resource(gameObject, this.skillIconManager, this.config.resourceIconSize()));
        }
        else if (GauntletExtendedPlugin.UTILITY_IDS.contains(id)) {
            this.utilities.add(gameObject);
        }
    }
    
    @Subscribe
    private void onGameObjectDespawned(final GameObjectDespawned event) {
        final GameObject gameObject = event.getGameObject();
        final int id = gameObject.getId();
        if (GauntletExtendedPlugin.RESOURCE_IDS.contains(gameObject.getId())) {
            this.resources.removeIf(o -> o.getGameObject() == gameObject);
        }
        else if (GauntletExtendedPlugin.UTILITY_IDS.contains(id)) {
            this.utilities.remove(gameObject);
        }
    }
    
    @Subscribe
    private void onNpcSpawned(final NpcSpawned event) {
        final NPC npc = event.getNpc();
        final int id = npc.getId();
        if (GauntletExtendedPlugin.HUNLLEF_IDS.contains(id)) {
            this.hunllef = new Hunllef(npc, this.skillIconManager, this.config.hunllefAttackStyleIconSize());
        }
        else if (GauntletExtendedPlugin.TORNADO_IDS.contains(id)) {
            this.tornadoes.add(new Tornado(npc));
        }
        else if (GauntletExtendedPlugin.DEMIBOSS_IDS.contains(id)) {
            this.demibosses.add(new Demiboss(npc));
        }
        else if (GauntletExtendedPlugin.STRONG_NPC_IDS.contains(id)) {
            this.strongNpcs.add(npc);
        }
        else if (GauntletExtendedPlugin.WEAK_NPC_IDS.contains(id)) {
            this.weakNpcs.add(npc);
        }
    }
    
    @Subscribe
    private void onNpcDespawned(final NpcDespawned event) {
        final NPC npc = event.getNpc();
        final int id = npc.getId();
        if (GauntletExtendedPlugin.HUNLLEF_IDS.contains(id)) {
            this.hunllef = null;
        }
        else if (GauntletExtendedPlugin.TORNADO_IDS.contains(id)) {
            this.tornadoes.removeIf(t -> t.getNpc() == npc);
        }
        else if (GauntletExtendedPlugin.DEMIBOSS_IDS.contains(id)) {
            this.demibosses.removeIf(d -> d.getNpc() == npc);
        }
        else if (GauntletExtendedPlugin.STRONG_NPC_IDS.contains(id)) {
            this.strongNpcs.remove(npc);
        }
        else if (GauntletExtendedPlugin.WEAK_NPC_IDS.contains(id)) {
            this.weakNpcs.remove(npc);
        }
    }
    
    @Subscribe
    private void onProjectileMoved(final ProjectileMoved event) {
        if (this.hunllef == null) {
            return;
        }
        final Projectile projectile = event.getProjectile();
        if (this.firstProjectile) {
            this.lastProjectile = projectile;
            this.firstProjectile = false;
        }
        if (this.lastProjectile.getStartCycle() == projectile.getStartCycle() && !this.firstProjectile) {
            return;
        }
        this.lastProjectile = projectile;
        final int id = projectile.getId();
        if (!GauntletExtendedPlugin.PROJECTILE_IDS.contains(id)) {
            return;
        }
        this.missile = new Missile(projectile, this.skillIconManager, this.config.projectileIconSize());
        this.hunllef.updateAttackCount();
        if (GauntletExtendedPlugin.PROJECTILE_PRAYER_IDS.contains(id) && this.config.hunllefPrayerAudio()) {
            this.client.playSoundEffect(227);
        }
    }
    
    @Subscribe
    private void onChatMessage(final ChatMessage event) {
        final ChatMessageType type = event.getType();
        if (type == ChatMessageType.SPAM || type == ChatMessageType.GAMEMESSAGE) {
            this.resourceManager.parseChatMessage(event.getMessage());
        }
    }
    
    @Subscribe
    private void onActorDeath(final ActorDeath event) {
        if (event.getActor() != this.client.getLocalPlayer()) {
            return;
        }
        this.overlayTimer.onPlayerDeath();
    }
    
    @Subscribe
    private void onAnimationChanged(final AnimationChanged event) {
        if (!this.isHunllefVarbitSet() || this.hunllef == null) {
            return;
        }
        final Actor actor = event.getActor();
        final int animationId = actor.getAnimation();
        if (actor instanceof Player) {
            if (!GauntletExtendedPlugin.ATTACK_ANIM_IDS.contains(animationId)) {
                return;
            }
            final boolean validAttack = this.isAttackAnimationValid(animationId);
            if (validAttack) {
                this.wrongAttackStyle = false;
                this.hunllef.updatePlayerAttackCount();
                if (this.hunllef.getPlayerAttackCount() == 1) {
                    this.switchWeapon = true;
                }
            }
            else {
                this.wrongAttackStyle = true;
            }
        }
        else if (actor instanceof NPC && animationId == 8418) {
            this.hunllef.updateAttackCount();
        }
    }
    
    private boolean isAttackAnimationValid(final int animationId) {
        final HeadIcon headIcon = this.hunllef.getNpc().getComposition().getOverheadIcon();
        if (headIcon == null) {
            return true;
        }
        switch (headIcon) {
            case MELEE: {
                if (GauntletExtendedPlugin.MELEE_ANIM_IDS.contains(animationId)) {
                    return false;
                }
                break;
            }
            case RANGED: {
                if (animationId == 426) {
                    return false;
                }
                break;
            }
            case MAGIC: {
                if (animationId == 1167) {
                    return false;
                }
                break;
            }
        }
        return true;
    }
    
    private void pluginEnabled() {
        if (this.isGauntletVarbitSet()) {
            this.overlayTimer.setGauntletStart();
            this.resourceManager.init();
            this.addSpawnedEntities();
            this.initGauntlet();
        }
        if (this.isHunllefVarbitSet()) {
            this.initHunllef();
        }
    }
    
    private void addSpawnedEntities() {
        for (final GameObject gameObject : this.getGameObjects(this.client)) {
            final GameObjectSpawned gameObjectSpawned = new GameObjectSpawned();
            gameObjectSpawned.setTile((Tile)null);
            gameObjectSpawned.setGameObject(gameObject);
            this.onGameObjectSpawned(gameObjectSpawned);
        }
        for (final NPC npc : this.client.getNpcs()) {
            this.onNpcSpawned(new NpcSpawned(npc));
        }
    }
    
    private Collection<GameObject> getGameObjects(final Client client) {
        final Collection<GameObject> objects = new ArrayList<GameObject>();
        for (final Tile[][] array : client.getScene().getTiles()) {
            final Tile[][] sceneTiles = array;
            for (final Tile[] array2 : array) {
                final Tile[] tiles = array2;
                for (final Tile tile : array2) {
                    if (tile != null) {
                        final GameObject[] object = tile.getGameObjects();
                        objects.addAll(Arrays.asList(object));
                    }
                }
            }
        }
        return objects;
    }
    
    private void initGauntlet() {
        this.inGauntlet = true;
        this.overlayManager.add((net.runelite.client.ui.overlay.Overlay)this.overlayTimer);
        this.overlayManager.add((net.runelite.client.ui.overlay.Overlay)this.overlayGauntlet);
    }
    
    private void initHunllef() {
        this.inHunllef = true;
        this.overlayTimer.setHunllefStart();
        this.resourceManager.reset();
        this.overlayManager.remove((net.runelite.client.ui.overlay.Overlay)this.overlayGauntlet);
        this.overlayManager.add((net.runelite.client.ui.overlay.Overlay)this.overlayHunllef);
        this.overlayManager.add((net.runelite.client.ui.overlay.Overlay)this.overlayPrayerWidget);
        this.overlayManager.add((net.runelite.client.ui.overlay.Overlay)this.overlayPrayerBox);
    }
    
    private boolean isGauntletVarbitSet() {
        return this.client.getVarbitValue(9178) == 1;
    }
    
    private boolean isHunllefVarbitSet() {
        return this.client.getVarbitValue(9177) == 1;
    }
    
    public Set<Resource> getResources() {
        return this.resources;
    }
    
    public Set<GameObject> getUtilities() {
        return this.utilities;
    }
    
    public Set<Tornado> getTornadoes() {
        return this.tornadoes;
    }
    
    public Set<Demiboss> getDemibosses() {
        return this.demibosses;
    }
    
    public Set<NPC> getStrongNpcs() {
        return this.strongNpcs;
    }
    
    public Set<NPC> getWeakNpcs() {
        return this.weakNpcs;
    }
    
    public Missile getMissile() {
        return this.missile;
    }
    
    public Hunllef getHunllef() {
        return this.hunllef;
    }
    
    public boolean isWrongAttackStyle() {
        return this.wrongAttackStyle;
    }
    
    public void setWrongAttackStyle(final boolean wrongAttackStyle) {
        this.wrongAttackStyle = wrongAttackStyle;
    }
    
    public boolean isSwitchWeapon() {
        return this.switchWeapon;
    }
    
    public void setSwitchWeapon(final boolean switchWeapon) {
        this.switchWeapon = switchWeapon;
    }
    
    public Projectile getLastProjectile() {
        return this.lastProjectile;
    }
    
    public void setLastProjectile(final Projectile lastProjectile) {
        this.lastProjectile = lastProjectile;
    }
    
    public boolean isFirstProjectile() {
        return this.firstProjectile;
    }
    
    public void setFirstProjectile(final boolean firstProjectile) {
        this.firstProjectile = firstProjectile;
    }
    
    static {
        MELEE_ANIM_IDS = (Set)ImmutableSet.of((Object)386, (Object)390, (Object)395, (Object)400, (Object)401, (Object)422, (Object[])new Integer[] { 423, 428, 440 });
        (ATTACK_ANIM_IDS = new HashSet<Integer>()).addAll(GauntletExtendedPlugin.MELEE_ANIM_IDS);
        GauntletExtendedPlugin.ATTACK_ANIM_IDS.add(426);
        GauntletExtendedPlugin.ATTACK_ANIM_IDS.add(1167);
        PROJECTILE_MAGIC_IDS = (Set)ImmutableSet.of((Object)1707, (Object)1708);
        PROJECTILE_RANGE_IDS = (Set)ImmutableSet.of((Object)1711, (Object)1712);
        PROJECTILE_PRAYER_IDS = (Set)ImmutableSet.of((Object)1713, (Object)1714);
        (PROJECTILE_IDS = new HashSet<Integer>()).addAll(GauntletExtendedPlugin.PROJECTILE_MAGIC_IDS);
        GauntletExtendedPlugin.PROJECTILE_IDS.addAll(GauntletExtendedPlugin.PROJECTILE_RANGE_IDS);
        GauntletExtendedPlugin.PROJECTILE_IDS.addAll(GauntletExtendedPlugin.PROJECTILE_PRAYER_IDS);
        HUNLLEF_IDS = (Set)ImmutableSet.of((Object)9021, (Object)9022, (Object)9023, (Object)9024, (Object)9035, (Object)9036, (Object[])new Integer[] { 9037, 9038 });
        TORNADO_IDS = (Set)ImmutableSet.of((Object)9025, (Object)9039);
        DEMIBOSS_IDS = (Set)ImmutableSet.of((Object)9032, (Object)9046, (Object)9034, (Object)9048, (Object)9033, (Object)9047, (Object[])new Integer[0]);
        STRONG_NPC_IDS = (Set)ImmutableSet.of((Object)9030, (Object)9044, (Object)9029, (Object)9043, (Object)9031, (Object)9045, (Object[])new Integer[0]);
        WEAK_NPC_IDS = (Set)ImmutableSet.of((Object)9028, (Object)9042, (Object)9026, (Object)9040, (Object)9027, (Object)9041, (Object[])new Integer[0]);
        RESOURCE_IDS = (Set)ImmutableSet.of((Object)36064, (Object)35967, (Object)35969, (Object)36066, (Object)36068, (Object)35971, (Object[])new Integer[] { 35973, 36070, 35975, 36072 });
        UTILITY_IDS = (Set)ImmutableSet.of((Object)35966, (Object)36063, (Object)35980, (Object)36077, (Object)35981, (Object)36078, (Object[])new Integer[0]);
    }
}
