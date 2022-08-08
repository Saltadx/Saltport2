// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.overlay;

import net.runelite.api.ChatMessageType;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import java.awt.Color;
import net.runelite.client.ui.overlay.OverlayLayer;
import java.time.Instant;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayPosition;
import java.util.Arrays;
import com.gauntletextended.TableElement;
import com.gauntletextended.TableAlignment;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.plugins.Plugin;
import com.gauntletextended.GauntletExtendedPlugin;
import com.gauntletextended.TableRow;
import com.gauntletextended.TableComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.chat.ChatMessageManager;
import com.gauntletextended.GauntletExtendedConfig;
import javax.inject.Singleton;

@Singleton
public class OverlayTimer extends Overlay
{
    private final GauntletExtendedConfig config;
    private final ChatMessageManager chatMessageManager;
    private final PanelComponent panelComponent;
    private final TableComponent tableComponent;
    private final TableRow rowPrepTime;
    private final TableRow rowTotalTime;
    private long timeGauntletStart;
    private long timeHunllefStart;
    
    @Inject
    public OverlayTimer(final GauntletExtendedPlugin plugin, final GauntletExtendedConfig config, final ChatMessageManager chatMessageManager) {
        super(plugin);
        this.config = config;
        this.chatMessageManager = chatMessageManager;
        this.panelComponent = new PanelComponent();
        this.tableComponent = new TableComponent();
        this.panelComponent.getChildren().add(TitleComponent.builder().text("Gauntlet Timer").build());
        this.panelComponent.getChildren().add(this.tableComponent);
        this.tableComponent.setColumnAlignments(TableAlignment.LEFT, TableAlignment.RIGHT);
        this.rowPrepTime = TableRow.builder().elements(Arrays.asList(TableElement.builder().content("Prep Time:").build(), TableElement.builder().content("").build())).build();
        this.rowTotalTime = TableRow.builder().elements(Arrays.asList(TableElement.builder().content("Total Time:").build(), TableElement.builder().content("").build())).build();
        this.timeGauntletStart = -1L;
        this.timeHunllefStart = -1L;
        this.setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        this.setPriority(OverlayPriority.HIGH);
        this.determineLayer();
    }
    
    public Dimension render(final Graphics2D graphics2D) {
        if (!this.config.timerOverlay() || this.timeGauntletStart == -1L) {
            return null;
        }
        final TableRow tableRow = (this.timeHunllefStart == -1L) ? this.rowPrepTime : this.rowTotalTime;
        tableRow.getElements().get(1).setContent(calculateElapsedTime(Instant.now().getEpochSecond(), this.timeGauntletStart));
        return this.panelComponent.render(graphics2D);
    }
    
    @Override
    public void determineLayer() {
        this.setLayer(OverlayLayer.UNDER_WIDGETS);
    }
    
    public void reset() {
        this.timeGauntletStart = -1L;
        this.timeHunllefStart = -1L;
        this.rowPrepTime.getElements().get(1).setContent("");
        this.rowTotalTime.getElements().get(1).setContent("");
        this.tableComponent.getRows().clear();
    }
    
    public void setGauntletStart() {
        this.timeGauntletStart = Instant.now().getEpochSecond();
        this.rowPrepTime.setRowColor(Color.WHITE);
        this.tableComponent.setRows(this.rowPrepTime);
    }
    
    public void setHunllefStart() {
        this.timeHunllefStart = Instant.now().getEpochSecond();
        this.rowPrepTime.setRowColor(Color.LIGHT_GRAY);
        this.tableComponent.setRows(this.rowPrepTime);
        this.tableComponent.addRows(this.rowTotalTime);
    }
    
    public void onPlayerDeath() {
        if (!this.config.timerChatMessage()) {
            return;
        }
        this.printTime();
        this.reset();
    }
    
    private void printTime() {
        if (this.timeGauntletStart == -1L || this.timeHunllefStart == -1L) {
            return;
        }
        final long current = Instant.now().getEpochSecond();
        final String elapsedPrepTime = calculateElapsedTime(this.timeHunllefStart, this.timeGauntletStart);
        final String elapsedBossTime = calculateElapsedTime(current, this.timeHunllefStart);
        final String elapsedTotalTime = calculateElapsedTime(current, this.timeGauntletStart);
        final ChatMessageBuilder chatMessageBuilder = new ChatMessageBuilder().append(ChatColorType.NORMAL).append("Preparation time: ").append(ChatColorType.HIGHLIGHT).append(elapsedPrepTime).append(ChatColorType.NORMAL).append(". Hunllef kill time: ").append(ChatColorType.HIGHLIGHT).append(elapsedBossTime).append(ChatColorType.NORMAL).append(". Total time: ").append(ChatColorType.HIGHLIGHT).append(elapsedTotalTime + ".");
        this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(chatMessageBuilder.build()).build());
    }
    
    private static String calculateElapsedTime(final long end, final long start) {
        final long elapsed = end - start;
        final long minutes = elapsed % 3600L / 60L;
        final long seconds = elapsed % 60L;
        return String.format("%01d:%02d", minutes, seconds);
    }
}
