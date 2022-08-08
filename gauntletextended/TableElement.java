// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended;

import java.awt.Color;

public class TableElement
{
    TableAlignment alignment;
    Color color;
    String content;
    
    TableElement(final TableAlignment alignment, final Color color, final String content) {
        this.alignment = alignment;
        this.color = color;
        this.content = content;
    }
    
    public static TableElementBuilder builder() {
        return new TableElementBuilder();
    }
    
    public TableAlignment getAlignment() {
        return this.alignment;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public void setAlignment(final TableAlignment alignment) {
        this.alignment = alignment;
    }
    
    public void setColor(final Color color) {
        this.color = color;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TableElement)) {
            return false;
        }
        final TableElement other = (TableElement)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$alignment = this.getAlignment();
        final Object other$alignment = other.getAlignment();
        Label_0065: {
            if (this$alignment == null) {
                if (other$alignment == null) {
                    break Label_0065;
                }
            }
            else if (this$alignment.equals(other$alignment)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$color = this.getColor();
        final Object other$color = other.getColor();
        Label_0102: {
            if (this$color == null) {
                if (other$color == null) {
                    break Label_0102;
                }
            }
            else if (this$color.equals(other$color)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null) {
            if (other$content == null) {
                return true;
            }
        }
        else if (this$content.equals(other$content)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TableElement;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $alignment = this.getAlignment();
        result = result * 59 + (($alignment == null) ? 43 : $alignment.hashCode());
        final Object $color = this.getColor();
        result = result * 59 + (($color == null) ? 43 : $color.hashCode());
        final Object $content = this.getContent();
        result = result * 59 + (($content == null) ? 43 : $content.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "TableElement(alignment=" + this.getAlignment() + ", color=" + this.getColor() + ", content=" + this.getContent() + ")";
    }
    
    public static class TableElementBuilder
    {
        private TableAlignment alignment;
        private Color color;
        private String content;
        
        TableElementBuilder() {
        }
        
        public TableElementBuilder alignment(final TableAlignment alignment) {
            this.alignment = alignment;
            return this;
        }
        
        public TableElementBuilder color(final Color color) {
            this.color = color;
            return this;
        }
        
        public TableElementBuilder content(final String content) {
            this.content = content;
            return this;
        }
        
        public TableElement build() {
            return new TableElement(this.alignment, this.color, this.content);
        }
        
        @Override
        public String toString() {
            return "TableElement.TableElementBuilder(alignment=" + this.alignment + ", color=" + this.color + ", content=" + this.content + ")";
        }
    }
}
