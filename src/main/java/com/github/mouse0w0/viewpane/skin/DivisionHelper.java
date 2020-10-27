package com.github.mouse0w0.viewpane.skin;

import com.github.mouse0w0.viewpane.geometry.DividerPos;
import com.github.mouse0w0.viewpane.geometry.EightPos;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;

class DivisionHelper {
    public static class Bound {
        double x, y, width, height;
        boolean enable;

        public void update(double x, double y, double width, double height, boolean snapToPixel) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    public static class Divider extends Bound {
        private final DoubleProperty grabberWidth = new SimpleDoubleProperty();
        private final DoubleProperty pos = new SimpleDoubleProperty(0.5);

        boolean enable;

        public DoubleProperty posProperty() {
            return pos;
        }

        public double getPos() {
            return pos.get();
        }
    }

    private static final class VerticalSplitter {
        private final Splitter top;
        private final Divider topDivider;
        private final HorizontalSplitter center;
        private final Divider bottomDivider;
        private final Splitter bottom;

        public VerticalSplitter(Splitter top, Divider topDivider, HorizontalSplitter center, Divider bottomDivider, Splitter bottom) {
            this.top = top;
            this.topDivider = topDivider;
            this.center = center;
            this.bottomDivider = bottomDivider;
            this.bottom = bottom;
        }

        void update(double minX, double minY, double maxX, double maxY, boolean snapToPixel) {
            double height = maxY - minY;
            double topDividerY = top.isEnable() ? snapToPixel(height * topDivider.getPos(), snapToPixel) : 0;
            double bottomDividerY = bottom.isEnable() ? snapToPixel(height * bottomDivider.getPos(), snapToPixel) : maxY;
            top.update(minX, minY, maxX, topDividerY, snapToPixel);
            center.update(minX, topDividerY, maxX, bottomDividerY, snapToPixel);
            bottom.update(minX, bottomDividerY, maxX, maxY, snapToPixel);
        }
    }

    private static final class HorizontalSplitter {
        private final Splitter left;
        private final Divider leftDivider;
        private final Bound center;
        private final Divider rightDivider;
        private final Splitter right;

        public HorizontalSplitter(Splitter left, Divider leftDivider, Bound center, Divider rightDivider, Splitter right) {
            this.left = left;
            this.leftDivider = leftDivider;
            this.center = center;
            this.rightDivider = rightDivider;
            this.right = right;
        }

        public void update(double minX, double minY, double maxX, double maxY, boolean snapToPixel) {
            double width = maxX - minX;
            double leftDividerX = left.isEnable() ? snapToPixel(width * leftDivider.getPos(), snapToPixel) : 0;
            double rightDividerX = right.isEnable() ? snapToPixel(width * rightDivider.getPos(), snapToPixel) : maxX;
            left.update(minX, minY, leftDividerX, maxY, snapToPixel);
            center.update(leftDividerX, minY, rightDividerX, maxY, snapToPixel);
            right.update(rightDividerX, minY, maxX, maxY, snapToPixel);
        }
    }

    private static final class Splitter {
        private final Bound leftTop;
        private final Divider divider;
        private final Bound rightBottom;
        private final Orientation orientation;

        public Splitter(Bound leftTop, Divider divider, Bound rightBottom, Orientation orientation) {
            this.leftTop = leftTop;
            this.divider = divider;
            this.rightBottom = rightBottom;
            this.orientation = orientation;
        }

        public boolean isEnable() {
            return leftTop.enable || rightBottom.enable;
        }

        public void update(double minX, double minY, double maxX, double maxY, boolean snapToPixel) {
            if (leftTop.enable && rightBottom.enable) {
                if (orientation == Orientation.HORIZONTAL) {
                    double width = maxX - minX;
                    double dividerX = snapToPixel(width * divider.getPos(), snapToPixel);
                    leftTop.update(minX, minY, dividerX, maxY, snapToPixel);
                    rightBottom.update(dividerX, minY, maxX, maxY, snapToPixel);
                } else {
                    double height = maxY - minY;
                    double dividerY = snapToPixel(height * divider.getPos(), snapToPixel);
                    leftTop.update(minX, minY, maxX, dividerY, snapToPixel);
                    rightBottom.update(maxX, dividerY, maxX, maxY, snapToPixel);
                }
            } else {
                leftTop.update(minX, minY, maxX, maxY, snapToPixel);
                rightBottom.update(minX, minY, maxX, maxY, snapToPixel);
            }
        }
    }

    private final VerticalSplitter root;

    final Bound[] bounds = new Bound[8];
    final Bound center = new Bound();
    final Divider[] dividers = new Divider[8];

    public DivisionHelper() {
        Splitter top = new Splitter(
                createNode(EightPos.TOP_LEFT),
                createDivider(DividerPos.TOP_SECONDARY),
                createNode(EightPos.TOP_RIGHT),
                Orientation.HORIZONTAL);

        Splitter left = new Splitter(
                createNode(EightPos.LEFT_TOP),
                createDivider(DividerPos.LEFT_SECONDARY),
                createNode(EightPos.LEFT_BOTTOM),
                Orientation.VERTICAL);

        Splitter bottom = new Splitter(
                createNode(EightPos.BOTTOM_LEFT),
                createDivider(DividerPos.BOTTOM_SECONDARY),
                createNode(EightPos.BOTTOM_RIGHT),
                Orientation.HORIZONTAL);

        Splitter right = new Splitter(
                createNode(EightPos.RIGHT_TOP),
                createDivider(DividerPos.RIGHT_SECONDARY),
                createNode(EightPos.RIGHT_BOTTOM),
                Orientation.VERTICAL);

        HorizontalSplitter horizontal = new HorizontalSplitter(
                left,
                createDivider(DividerPos.LEFT_PRIMARY),
                center,
                createDivider(DividerPos.RIGHT_PRIMARY),
                right);

        VerticalSplitter vertical = new VerticalSplitter(
                top,
                createDivider(DividerPos.TOP_PRIMARY),
                horizontal,
                createDivider(DividerPos.BOTTOM_PRIMARY),
                bottom);

        root = vertical;
    }

    private Bound createNode(EightPos pos) {
        return bounds[pos.ordinal()] = new Bound();
    }

    private Divider createDivider(DividerPos pos) {
        return dividers[pos.ordinal()] = new Divider();
    }

    public void update(double x, double y, double width, double height, boolean snapToPixel) {
        root.update(x, y, width, height, snapToPixel);
    }

    private static double snapToPixel(double value, boolean snapToPixel) {
        return snapToPixel ? Math.ceil(value) : value;
    }
}
