// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.resource;

import com.google.common.base.CharMatcher;

class JagexPrintableCharMatcher extends CharMatcher
{
    public boolean matches(final char c) {
        return (c >= ' ' && c <= '~') || c == '\u0080' || (c >= ' ' && c <= '\u00ff');
    }
}
