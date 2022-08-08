// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended;

import java.util.Collections;
import java.util.List;
import java.awt.Color;

public class TableRow
{
    Color rowColor;
    TableAlignment rowAlignment;
    List<TableElement> elements;
    
    private static List<TableElement> $default$elements() {
        return Collections.emptyList();
    }
    
    TableRow(final Color rowColor, final TableAlignment rowAlignment, final List<TableElement> elements) {
        this.rowColor = rowColor;
        this.rowAlignment = rowAlignment;
        this.elements = elements;
    }
    
    public static TableRowBuilder builder() {
        return new TableRowBuilder();
    }
    
    public Color getRowColor() {
        return this.rowColor;
    }
    
    public TableAlignment getRowAlignment() {
        return this.rowAlignment;
    }
    
    public List<TableElement> getElements() {
        return this.elements;
    }
    
    public void setRowColor(final Color rowColor) {
        this.rowColor = rowColor;
    }
    
    public void setRowAlignment(final TableAlignment rowAlignment) {
        this.rowAlignment = rowAlignment;
    }
    
    public void setElements(final List<TableElement> elements) {
        this.elements = elements;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TableRow)) {
            return false;
        }
        final TableRow other = (TableRow)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$rowColor = this.getRowColor();
        final Object other$rowColor = other.getRowColor();
        Label_0065: {
            if (this$rowColor == null) {
                if (other$rowColor == null) {
                    break Label_0065;
                }
            }
            else if (this$rowColor.equals(other$rowColor)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$rowAlignment = this.getRowAlignment();
        final Object other$rowAlignment = other.getRowAlignment();
        Label_0102: {
            if (this$rowAlignment == null) {
                if (other$rowAlignment == null) {
                    break Label_0102;
                }
            }
            else if (this$rowAlignment.equals(other$rowAlignment)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$elements = this.getElements();
        final Object other$elements = other.getElements();
        if (this$elements == null) {
            if (other$elements == null) {
                return true;
            }
        }
        else if (this$elements.equals(other$elements)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TableRow;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $rowColor = this.getRowColor();
        result = result * 59 + (($rowColor == null) ? 43 : $rowColor.hashCode());
        final Object $rowAlignment = this.getRowAlignment();
        result = result * 59 + (($rowAlignment == null) ? 43 : $rowAlignment.hashCode());
        final Object $elements = this.getElements();
        result = result * 59 + (($elements == null) ? 43 : $elements.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "TableRow(rowColor=" + this.getRowColor() + ", rowAlignment=" + this.getRowAlignment() + ", elements=" + this.getElements() + ")";
    }
    
    public static class TableRowBuilder
    {
        private Color rowColor;
        private TableAlignment rowAlignment;
        private boolean elements$set;
        private List<TableElement> elements$value;
        
        TableRowBuilder() {
        }
        
        public TableRowBuilder rowColor(final Color rowColor) {
            this.rowColor = rowColor;
            return this;
        }
        
        public TableRowBuilder rowAlignment(final TableAlignment rowAlignment) {
            this.rowAlignment = rowAlignment;
            return this;
        }
        
        public TableRowBuilder elements(final List<TableElement> elements) {
            this.elements$value = elements;
            this.elements$set = true;
            return this;
        }
        
        public TableRow build() {
            List<TableElement> elements$value = this.elements$value;
            if (!this.elements$set) {
                elements$value = $default$elements();
            }
            return new TableRow(this.rowColor, this.rowAlignment, elements$value);
        }
        
        @Override
        public String toString() {
            return "TableRow.TableRowBuilder(rowColor=" + this.rowColor + ", rowAlignment=" + this.rowAlignment + ", elements$value=" + this.elements$value + ")";
        }
    }
}
