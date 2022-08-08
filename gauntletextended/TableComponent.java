// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended;

import java.util.Arrays;
import lombok.NonNull;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import com.gauntletextended.resource.Text;
import java.util.Collection;
import net.runelite.client.ui.overlay.components.TextComponent;
import java.util.Iterator;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;

public class TableComponent implements LayoutableRenderableEntity
{
    private static final TableElement EMPTY_ELEMENT;
    private final List<TableElement> columns;
    private final List<TableRow> rows;
    private final Rectangle bounds;
    private TableAlignment defaultAlignment;
    private Color defaultColor;
    private Dimension gutter;
    private Point preferredLocation;
    private Dimension preferredSize;
    
    public TableComponent() {
        this.columns = new ArrayList<TableElement>();
        this.rows = new ArrayList<TableRow>();
        this.bounds = new Rectangle();
        this.defaultAlignment = TableAlignment.LEFT;
        this.defaultColor = Color.WHITE;
        this.gutter = new Dimension(3, 0);
        this.preferredLocation = new Point();
        this.preferredSize = new Dimension(129, 0);
    }
    
    public Dimension render(final Graphics2D graphics) {
        final FontMetrics metrics = graphics.getFontMetrics();
        final TableRow colRow = TableRow.builder().elements(this.columns).build();
        final int[] columnWidths = this.getColumnWidths(metrics, colRow);
        graphics.translate(this.preferredLocation.x, this.preferredLocation.y);
        int height = this.displayRow(graphics, colRow, 0, columnWidths, metrics);
        for (final TableRow row : this.rows) {
            height = this.displayRow(graphics, row, height, columnWidths, metrics);
        }
        graphics.translate(-this.preferredLocation.x, -this.preferredLocation.y);
        final Dimension dimension = new Dimension(this.preferredSize.width, height);
        this.bounds.setLocation(this.preferredLocation);
        this.bounds.setSize(dimension);
        return dimension;
    }
    
    private int displayRow(final Graphics2D graphics, final TableRow row, int height, final int[] columnWidths, final FontMetrics metrics) {
        int x = 0;
        final int startingRowHeight = height;
        final List<TableElement> elements = row.getElements();
        for (int i = 0; i < elements.size(); ++i) {
            int y = startingRowHeight;
            final TableElement cell = elements.get(i);
            final String content = cell.getContent();
            if (content != null) {
                final String[] lines = lineBreakText(content, columnWidths[i], metrics);
                final TableAlignment alignment = this.getCellAlignment(row, i);
                final Color color = this.getCellColor(row, i);
                for (final String line : lines) {
                    final int alignmentOffset = getAlignedPosition(line, alignment, columnWidths[i], metrics);
                    final TextComponent leftLineComponent = new TextComponent();
                    y += metrics.getHeight();
                    leftLineComponent.setPosition(new Point(x + alignmentOffset, y));
                    leftLineComponent.setText(line);
                    leftLineComponent.setColor(color);
                    leftLineComponent.render(graphics);
                }
                height = Math.max(height, y);
                x += columnWidths[i] + this.gutter.width;
            }
        }
        return height + this.gutter.height;
    }
    
    private int[] getColumnWidths(final FontMetrics metrics, final TableRow columnRow) {
        int numCols = this.columns.size();
        for (final TableRow r : this.rows) {
            numCols = Math.max(r.getElements().size(), numCols);
        }
        final int[] maxtextw = new int[numCols];
        final int[] maxwordw = new int[numCols];
        final boolean[] flex = new boolean[numCols];
        final boolean[] wrap = new boolean[numCols];
        final int[] finalcolw = new int[numCols];
        final List<TableRow> rows = new ArrayList<TableRow>(this.rows);
        rows.add(columnRow);
        for (final TableRow r2 : rows) {
            final List<TableElement> elements = r2.getElements();
            for (int col = 0; col < elements.size(); ++col) {
                final TableElement ele = elements.get(col);
                final String cell = ele.getContent();
                if (cell != null) {
                    final int cellWidth = getTextWidth(metrics, cell);
                    maxtextw[col] = Math.max(maxtextw[col], cellWidth);
                    for (final String word : cell.split(" ")) {
                        maxwordw[col] = Math.max(maxwordw[col], getTextWidth(metrics, word));
                    }
                    if (maxtextw[col] == cellWidth) {
                        wrap[col] = cell.contains(" ");
                    }
                }
            }
        }
        int left = this.preferredSize.width - (numCols - 1) * this.gutter.width;
        final double avg = left / numCols;
        int nflex = 0;
        for (int col2 = 0; col2 < numCols; ++col2) {
            final double maxNonFlexLimit = 1.5 * avg;
            flex[col2] = (maxtextw[col2] > maxNonFlexLimit);
            if (flex[col2]) {
                ++nflex;
            }
            else {
                finalcolw[col2] = maxtextw[col2];
                left -= finalcolw[col2];
            }
        }
        if (left < nflex * avg) {
            for (int col2 = 0; col2 < numCols; ++col2) {
                if (!flex[col2] && wrap[col2]) {
                    left += finalcolw[col2];
                    finalcolw[col2] = 0;
                    flex[col2] = true;
                    ++nflex;
                }
            }
        }
        int tot = 0;
        for (int col3 = 0; col3 < numCols; ++col3) {
            if (flex[col3]) {
                maxtextw[col3] = Math.min(maxtextw[col3], this.preferredSize.width);
                tot += maxtextw[col3];
            }
        }
        for (int col3 = 0; col3 < numCols; ++col3) {
            if (flex[col3]) {
                finalcolw[col3] = left * maxtextw[col3] / tot;
                finalcolw[col3] = Math.max(finalcolw[col3], maxwordw[col3]);
                left -= finalcolw[col3];
            }
        }
        final int extraPerCol = left / numCols;
        for (int col4 = 0; col4 < numCols; ++col4) {
            final int[] array = finalcolw;
            final int n = col4;
            array[n] += extraPerCol;
            left -= extraPerCol;
        }
        final int[] array2 = finalcolw;
        final int n2 = finalcolw.length - 1;
        array2[n2] += left;
        return finalcolw;
    }
    
    private static int getTextWidth(final FontMetrics metrics, final String cell) {
        return metrics.stringWidth(Text.removeTags(cell));
    }
    
    private static String[] lineBreakText(final String text, final int maxWidth, final FontMetrics metrics) {
        final String[] words = text.split(" ");
        if (words.length == 0) {
            return new String[0];
        }
        final StringBuilder wrapped = new StringBuilder(words[0]);
        int spaceLeft = maxWidth - getTextWidth(metrics, wrapped.toString());
        for (int i = 1; i < words.length; ++i) {
            final String word = words[i];
            final int wordLen = getTextWidth(metrics, word);
            final int spaceWidth = metrics.stringWidth(" ");
            if (wordLen + spaceWidth > spaceLeft) {
                wrapped.append("\n").append(word);
                spaceLeft = maxWidth - wordLen;
            }
            else {
                wrapped.append(" ").append(word);
                spaceLeft -= spaceWidth + wordLen;
            }
        }
        return wrapped.toString().split("\n");
    }
    
    public boolean isEmpty() {
        return this.columns.size() == 0 || this.rows.size() == 0;
    }
    
    private void ensureColumnSize(final int size) {
        while (size > this.columns.size()) {
            this.columns.add(TableElement.builder().build());
        }
    }
    
    private static int getAlignedPosition(final String str, final TableAlignment alignment, final int columnWidth, final FontMetrics metrics) {
        final int stringWidth = getTextWidth(metrics, str);
        int offset = 0;
        switch (alignment) {
            case CENTER: {
                offset = columnWidth / 2 - stringWidth / 2;
                break;
            }
            case RIGHT: {
                offset = columnWidth - stringWidth;
                break;
            }
        }
        return offset;
    }
    
    private Color getCellColor(final TableRow row, final int colIndex) {
        final List<TableElement> rowElements = row.getElements();
        final TableElement cell = (colIndex < rowElements.size()) ? rowElements.get(colIndex) : TableComponent.EMPTY_ELEMENT;
        final TableElement column = (colIndex < this.columns.size()) ? this.columns.get(colIndex) : TableComponent.EMPTY_ELEMENT;
        return firstNonNull(cell.getColor(), row.getRowColor(), column.getColor(), this.defaultColor);
    }
    
    private void setColumnAlignment(final int col, final TableAlignment alignment) {
        assert this.columns.size() > col;
        this.columns.get(col).setAlignment(alignment);
    }
    
    public void setColumnAlignments(@Nonnull final TableAlignment... alignments) {
        this.ensureColumnSize(alignments.length);
        for (int i = 0; i < alignments.length; ++i) {
            this.setColumnAlignment(i, alignments[i]);
        }
    }
    
    private TableAlignment getCellAlignment(final TableRow row, final int colIndex) {
        final List<TableElement> rowElements = row.getElements();
        final TableElement cell = (colIndex < rowElements.size()) ? rowElements.get(colIndex) : TableComponent.EMPTY_ELEMENT;
        final TableElement column = (colIndex < this.columns.size()) ? this.columns.get(colIndex) : TableComponent.EMPTY_ELEMENT;
        return firstNonNull(cell.getAlignment(), row.getRowAlignment(), column.getAlignment(), this.defaultAlignment);
    }
    
    @SafeVarargs
    private static <T> T firstNonNull(@Nullable final T... elements) {
        if (elements == null || elements.length == 0) {
            return null;
        }
        int i;
        T cur;
        for (i = 0, cur = elements[0]; cur == null && i < elements.length; cur = elements[i], ++i) {}
        return cur;
    }
    
    public void addRow(@Nonnull final String... cells) {
        final List<TableElement> elements = new ArrayList<TableElement>();
        for (final String cell : cells) {
            elements.add(TableElement.builder().content(cell).build());
        }
        final TableRow row = TableRow.builder().build();
        row.setElements(elements);
        this.rows.add(row);
    }
    
    private void addRows(@Nonnull final String[]... rows) {
        for (final String[] row : rows) {
            this.addRow(row);
        }
    }
    
    public void addRows(@NonNull final TableRow... rows) {
        if (rows == null) {
            throw new NullPointerException("rows is marked non-null but is null");
        }
        this.rows.addAll(Arrays.asList(rows));
    }
    
    public void setRows(@Nonnull final String[]... elements) {
        this.rows.clear();
        this.addRows(elements);
    }
    
    public void setRows(@Nonnull final TableRow... elements) {
        this.rows.clear();
        this.rows.addAll(Arrays.asList(elements));
    }
    
    private void addColumn(@Nonnull final String col) {
        this.columns.add(TableElement.builder().content(col).build());
    }
    
    public void addColumns(@NonNull final TableElement... columns) {
        if (columns == null) {
            throw new NullPointerException("columns is marked non-null but is null");
        }
        this.columns.addAll(Arrays.asList(columns));
    }
    
    public void setColumns(@Nonnull final TableElement... elements) {
        this.columns.clear();
        this.columns.addAll(Arrays.asList(elements));
    }
    
    public void setColumns(@Nonnull final String... columns) {
        this.columns.clear();
        for (final String col : columns) {
            this.addColumn(col);
        }
    }
    
    public void setDefaultAlignment(final TableAlignment defaultAlignment) {
        this.defaultAlignment = defaultAlignment;
    }
    
    public void setDefaultColor(final Color defaultColor) {
        this.defaultColor = defaultColor;
    }
    
    public void setGutter(final Dimension gutter) {
        this.gutter = gutter;
    }
    
    public void setPreferredLocation(final Point preferredLocation) {
        this.preferredLocation = preferredLocation;
    }
    
    public void setPreferredSize(final Dimension preferredSize) {
        this.preferredSize = preferredSize;
    }
    
    public List<TableElement> getColumns() {
        return this.columns;
    }
    
    public List<TableRow> getRows() {
        return this.rows;
    }
    
    public Rectangle getBounds() {
        return this.bounds;
    }
    
    static {
        EMPTY_ELEMENT = TableElement.builder().build();
    }
}
