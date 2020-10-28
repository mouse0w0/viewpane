package com.github.mouse0w0.viewpane.skin;

import com.github.mouse0w0.viewpane.DividerType;
import com.github.mouse0w0.viewpane.geometry.EightPos;
import javafx.geometry.Orientation;
import javafx.geometry.Side;

class LayoutHelper {
    public static class Area {
        ViewPaneSkin.ContentArea peer;

        public boolean isManaged() {
            return peer != null && peer.isManaged();
        }

        public void update(double x, double y, double width, double height) {
            if (isManaged()) {
                peer.resizeRelocate(x, y, width, height);
            }
        }
    }

    public static class Divider {
        ViewPaneSkin.ContentDivider peer;

        private final DividerType type;

        public Divider(DividerType type) {
            this.type = type;
        }

        public boolean isManaged() {
            return peer != null && peer.isManaged();
        }

        public double getWidth() {
            if (isManaged()) {
                return peer.getDividerWidth();
            }
            return 0;
        }

        public double getPosition() {
            if (isManaged()) {
                return peer.getPosition();
            }
            return type.getSide() == Side.TOP || type.getSide() == Side.LEFT ? 0 : 1;
        }

        public void updateParentSize(double width, double height, double previousPos, double nextPos) {
            if (isManaged()) {
                peer.updateParentSize(width, height, previousPos, nextPos);
            }
        }

        public void update(double x, double y, double width, double height) {
            if (isManaged()) {
                peer.resizeRelocate(x, y, width, height);
            }
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

        void update(double x, double y, double width, double height, boolean snapToPixel) {
            topDivider.updateParentSize(width, height, 0, bottomDivider.getPosition());
            bottomDivider.updateParentSize(width, height, topDivider.getPosition(), 1);

            double topDividerHeight = topDivider.getWidth();
            double topDividerY = height * topDivider.getPosition();
            double topHeight = snapToPixel(topDividerY - topDividerHeight / 2, true);

            double bottomDividerHeight = bottomDivider.getWidth();
            double bottomDividerY = height * bottomDivider.getPosition();
            double bottomHeight = snapToPixel(height - bottomDividerY - bottomDividerHeight / 2, true);

            double centerHeight = height - topHeight - topDividerHeight - bottomHeight - bottomDividerHeight;

            top.update(x, y, width, topHeight, snapToPixel);
            y += topHeight;
            topDivider.update(x, y, width, topDividerHeight);
            y += topDividerHeight;
            center.update(x, y, width, centerHeight, snapToPixel);
            y += centerHeight;
            bottomDivider.update(x, y, width, bottomDividerHeight);
            y += bottomDividerHeight;
            bottom.update(x, y, width, bottomHeight, snapToPixel);
        }
    }

    private static final class HorizontalSplitter {
        private final Splitter left;
        private final Divider leftDivider;
        private final Area center;
        private final Divider rightDivider;
        private final Splitter right;

        public HorizontalSplitter(Splitter left, Divider leftDivider, Area center, Divider rightDivider, Splitter right) {
            this.left = left;
            this.leftDivider = leftDivider;
            this.center = center;
            this.rightDivider = rightDivider;
            this.right = right;
        }

        public void update(double x, double y, double width, double height, boolean snapToPixel) {
            leftDivider.updateParentSize(width, height, 0, rightDivider.getPosition());
            rightDivider.updateParentSize(width, height, leftDivider.getPosition(), 1);

            double leftDividerWidth = leftDivider.getWidth();
            double leftDividerX = width * leftDivider.getPosition();
            double leftWidth = snapToPixel(leftDividerX - leftDividerWidth / 2, true);

            double rightDividerWidth = rightDivider.getWidth();
            double rightDividerX = width * rightDivider.getPosition();
            double rightWidth = snapToPixel(width - rightDividerX - rightDividerWidth / 2, true);

            double centerWidth = width - leftWidth - leftDividerWidth - rightWidth - rightDividerWidth;

            left.update(x, y, leftWidth, height, snapToPixel);
            x += leftWidth;
            leftDivider.update(x, y, leftDividerWidth, height);
            x += leftDividerWidth;
            center.update(x, y, centerWidth, height);
            x += centerWidth;
            rightDivider.update(x, y, rightDividerWidth, height);
            x += rightDividerWidth;
            right.update(x, y, rightWidth, height, snapToPixel);
        }
    }

    private static final class Splitter {
        private final Area leftTop;
        private final Divider divider;
        private final Area rightBottom;
        private final Orientation orientation;

        public Splitter(Area leftTop, Divider divider, Area rightBottom, Orientation orientation) {
            this.leftTop = leftTop;
            this.divider = divider;
            this.rightBottom = rightBottom;
            this.orientation = orientation;
        }

        public boolean isManaged() {
            return leftTop.isManaged() || rightBottom.isManaged();
        }

        public void update(double x, double y, double width, double height, boolean snapToPixel) {
            divider.updateParentSize(width, height, 0, 1);

            if (leftTop.isManaged() && rightBottom.isManaged()) {
                double dividerWidth = divider.getWidth();
                if (orientation == Orientation.HORIZONTAL) {
                    double dividerX = width * divider.getPosition();
                    double leftWidth = snapToPixel(dividerX - dividerWidth / 2, snapToPixel);
                    leftTop.update(x, y, leftWidth, height);
                    divider.update(x + leftWidth, y, dividerWidth, height);
                    rightBottom.update(x + leftWidth + dividerWidth, y, width - leftWidth - dividerWidth, height);
                } else {
                    double dividerY = height * divider.getPosition();
                    double topHeight = snapToPixel(dividerY - dividerWidth / 2, snapToPixel);
                    leftTop.update(x, y, width, topHeight);
                    divider.update(x, y + topHeight, width, height);
                    rightBottom.update(x, y + topHeight + dividerWidth, width, height - topHeight - dividerWidth);
                }
            } else {
                leftTop.update(x, y, width, height);
                rightBottom.update(x, y, width, height);
            }
        }
    }

    private final VerticalSplitter root;

    final Area[] areas = new Area[8];
    final Area center = new Area();
    final Divider[] dividers = new Divider[8];

    public LayoutHelper() {
        Splitter top = new Splitter(
                createNode(EightPos.TOP_LEFT),
                createDivider(DividerType.TOP_SECONDARY),
                createNode(EightPos.TOP_RIGHT),
                Orientation.HORIZONTAL);

        Splitter left = new Splitter(
                createNode(EightPos.LEFT_TOP),
                createDivider(DividerType.LEFT_SECONDARY),
                createNode(EightPos.LEFT_BOTTOM),
                Orientation.VERTICAL);

        Splitter bottom = new Splitter(
                createNode(EightPos.BOTTOM_LEFT),
                createDivider(DividerType.BOTTOM_SECONDARY),
                createNode(EightPos.BOTTOM_RIGHT),
                Orientation.HORIZONTAL);

        Splitter right = new Splitter(
                createNode(EightPos.RIGHT_TOP),
                createDivider(DividerType.RIGHT_SECONDARY),
                createNode(EightPos.RIGHT_BOTTOM),
                Orientation.VERTICAL);

        HorizontalSplitter horizontal = new HorizontalSplitter(
                left,
                createDivider(DividerType.LEFT_PRIMARY),
                center,
                createDivider(DividerType.RIGHT_PRIMARY),
                right);

        VerticalSplitter vertical = new VerticalSplitter(
                top,
                createDivider(DividerType.TOP_PRIMARY),
                horizontal,
                createDivider(DividerType.BOTTOM_PRIMARY),
                bottom);

        root = vertical;
    }

    private Area createNode(EightPos pos) {
        return areas[pos.ordinal()] = new Area();
    }

    private Divider createDivider(DividerType type) {
        return dividers[type.ordinal()] = new Divider(type);
    }

    public void layout(double x, double y, double width, double height, boolean snapToPixel) {
        root.update(x, y, width, height, snapToPixel);
    }

    private static double snapToPixel(double value, boolean snapToPixel) {
        return snapToPixel ? Math.ceil(value) : value;
    }
}
