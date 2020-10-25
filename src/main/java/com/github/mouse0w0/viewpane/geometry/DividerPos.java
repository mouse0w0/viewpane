package com.github.mouse0w0.viewpane.geometry;

import javafx.geometry.Orientation;
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
    private final Orientation orientation;

    DividerPos(Side side, boolean primary) {
        this.side = side;
        this.primary = primary;
        if (side == Side.TOP || side == Side.BOTTOM) {
            orientation = primary ? Orientation.HORIZONTAL : Orientation.VERTICAL;
        } else {
            orientation = primary ? Orientation.VERTICAL : Orientation.HORIZONTAL;
        }
    }

    public Side getSide() {
        return side;
    }

    public boolean isPrimary() {
        return primary;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
