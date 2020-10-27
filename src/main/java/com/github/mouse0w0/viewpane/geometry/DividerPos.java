package com.github.mouse0w0.viewpane.geometry;

import javafx.geometry.Side;

public enum DividerPos {
    TOP_PRIMARY(Side.TOP, true),
    TOP_SECONDARY(Side.TOP, false),
    LEFT_PRIMARY(Side.LEFT, true),
    LEFT_SECONDARY(Side.LEFT, false),
    BOTTOM_PRIMARY(Side.BOTTOM, true),
    BOTTOM_SECONDARY(Side.BOTTOM, false),
    RIGHT_PRIMARY(Side.RIGHT, true),
    RIGHT_SECONDARY(Side.RIGHT, false);

    private final Side side;
    private final boolean primary;

    DividerPos(Side side, boolean primary) {
        this.side = side;
        this.primary = primary;
    }

    public Side getSide() {
        return side;
    }

    public boolean isPrimary() {
        return primary;
    }
}
