// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended;

import net.runelite.api.Perspective;

public final class Vertex
{
    private final int x;
    private final int y;
    private final int z;
    
    public Vertex rotate(int orientation) {
        orientation = (orientation + 1024) % 2048;
        if (orientation == 0) {
            return this;
        }
        final int sin = Perspective.SINE[orientation];
        final int cos = Perspective.COSINE[orientation];
        return new Vertex(this.x * cos + this.z * sin >> 16, this.y, this.z * cos - this.x * sin >> 16);
    }
    
    public Vertex(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Vertex)) {
            return false;
        }
        final Vertex other = (Vertex)o;
        return this.getX() == other.getX() && this.getY() == other.getY() && this.getZ() == other.getZ();
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getZ();
        return result;
    }
    
    @Override
    public String toString() {
        return "Vertex(x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ")";
    }
}
