package com.github.mouse0w0.viewpane.skin;

import com.github.mouse0w0.viewpane.ViewGroup;
import com.github.mouse0w0.viewpane.ViewPane;
import com.github.mouse0w0.viewpane.ViewTab;
import com.github.mouse0w0.viewpane.geometry.EightPos;
import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;

public class ViewPaneSkin extends SkinBase<ViewPane> {
    private final SideBarArea sideBarArea;
    private final DivisionArea divisionArea;

    public ViewPaneSkin(ViewPane control) {
        super(control);

        sideBarArea = new SideBarArea();
        getChildren().add(sideBarArea);

        divisionArea = new DivisionArea();
        sideBarArea.setCenter(divisionArea);

        getViewGroups().forEach(this::createTabButtonBar);
        getViewGroups().addListener(new ListChangeListener<ViewGroup>() {
            @Override
            public void onChanged(Change<? extends ViewGroup> c) {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        for (ViewGroup viewGroup : c.getRemoved()) {
                            createTabButtonBar(viewGroup);
                        }
                    }
                    if (c.wasAdded()) {
                        for (ViewGroup viewGroup : c.getAddedSubList()) {
                            removeTabButtonBar(viewGroup);
                        }
                    }
                }
            }
        });
    }

    public ObservableList<ViewGroup> getViewGroups() {
        return getSkinnable().getViewGroups();
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return snappedLeftInset() + snapSize(sideBarArea.prefWidth(-1)) + snappedRightInset();
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return snappedTopInset() + snapSize(sideBarArea.prefHeight(-1)) + snappedBottomInset();
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        sideBarArea.resize(contentWidth, contentHeight);
        sideBarArea.relocate(contentX, contentY);
    }

    private void createTabButtonBar(ViewGroup viewGroup) {
        EightPos pos = viewGroup.getPos();
        SideBar sideBar = getSideBar(pos.getPrimary());

        Side secondary = pos.getSecondary();
        if (secondary == Side.TOP || secondary == Side.LEFT) {
            TabButtonBar tabButtonBar = sideBar.getTopLeftBar();
            if (tabButtonBar == null || tabButtonBar.getViewGroup() != viewGroup) {
                tabButtonBar = new TabButtonBar(viewGroup);
                sideBar.setTopLeftBar(tabButtonBar);
            }
        } else {
            TabButtonBar tabButtonBar = sideBar.getBottomRightBar();
            if (tabButtonBar == null || tabButtonBar.getViewGroup() != viewGroup) {
                tabButtonBar = new TabButtonBar(viewGroup);
                sideBar.setBottomRightBar(tabButtonBar);
            }
        }
    }

    private void removeTabButtonBar(ViewGroup viewGroup) {
        EightPos pos = viewGroup.getPos();
        SideBar sideBar = getSideBar(pos.getPrimary());

        Side secondary = pos.getSecondary();
        if (secondary == Side.TOP || secondary == Side.LEFT) {
            sideBar.setTopLeftBar(null);
        } else {
            sideBar.setTopLeftBar(null);
        }
    }

    private SideBar getSideBar(Side side) {
        SideBar sideBar = sideBarArea.getSideBar(side);
        if (sideBar == null) {
            sideBar = new SideBar(side);
            if (side == Side.LEFT)
                sideBarArea.setLeft(sideBar);
            else if (side == Side.RIGHT)
                sideBarArea.setRight(sideBar);
            else if (side == Side.BOTTOM)
                sideBarArea.setBottom(sideBar);
            else
                sideBarArea.setTop(sideBar);
        }
        return sideBar;
    }

    static class SideBarArea extends Region {
        private SideBar top;
        private SideBar left;
        private SideBar bottom;
        private SideBar right;
        private Node center;

        public SideBarArea() {
            getStyleClass().setAll("side-bar-area");
        }

        public SideBar getSideBar(Side side) {
            switch (side) {
                case TOP:
                    return top;
                case LEFT:
                    return left;
                case BOTTOM:
                    return bottom;
                case RIGHT:
                    return right;
                default:
                    throw new NullPointerException();
            }
        }

        public void setTop(SideBar top) {
            if (this.top != null) getChildren().remove(this.top);
            this.top = top;
            if (top != null) getChildren().add(top);
        }

        public void setLeft(SideBar left) {
            if (this.left != null) getChildren().remove(this.left);
            this.left = left;
            if (left != null) getChildren().add(left);
        }

        public void setBottom(SideBar bottom) {
            if (this.bottom != null) getChildren().remove(this.bottom);
            this.bottom = bottom;
            if (bottom != null) getChildren().add(bottom);
        }

        public void setRight(SideBar right) {
            if (this.right != null) getChildren().remove(this.right);
            this.right = right;
            if (right != null) getChildren().add(right);
        }

        public void setCenter(Node center) {
            if (this.center != null) getChildren().remove(this.center);
            this.center = center;
            if (center != null) getChildren().add(center);
        }

        @Override
        protected double computePrefWidth(double height) {
            return snappedLeftInset() +
                    computeChildPrefHeight(left) +
                    max(computeChildPrefWidth(top), computeChildPrefWidth(bottom), computeChildPrefWidth(center)) +
                    computeChildPrefHeight(right) +
                    snappedRightInset();
        }

        @Override
        protected double computePrefHeight(double width) {
            return snappedTopInset() +
                    computeChildPrefHeight(top) +
                    max(computeChildPrefWidth(left), computeChildPrefWidth(right), computeChildPrefHeight(center)) +
                    computeChildPrefHeight(bottom) +
                    snappedBottomInset();
        }

        private double computeChildPrefWidth(Node child) {
            return child != null && child.isManaged() ? snapSize(child.prefWidth(-1)) : 0;
        }

        private double computeChildPrefHeight(Node child) {
            return child != null && child.isManaged() ? snapSize(child.prefHeight(-1)) : 0;
        }

        private static double max(double a, double b, double c) {
            return Math.max(Math.max(a, b), c);
        }

        @Override
        protected void layoutChildren() {
            double topOffset = snappedTopInset();
            double leftOffset = snappedLeftInset();
            double bottomOffset = snappedBottomInset();
            double rightOffset = snappedRightInset();
            double width = getWidth();
            double height = getHeight();
            double contentWidth = width - leftOffset - rightOffset;
            double contentHeight = height - topOffset - bottomOffset;

            boolean hasTop = top != null && top.isManaged();
            boolean hasLeft = left != null && left.isManaged();
            boolean hasBottom = bottom != null && bottom.isManaged();
            boolean hasRight = right != null && right.isManaged();
            boolean hasCenter = center != null && center.isManaged();

            double leftWidth = hasLeft ? snapSize(left.prefHeight(-1)) : 0;
            double rightWidth = hasRight ? snapSize(right.prefHeight(-1)) : 0;
            double topHeight = hasTop ? snapSize(top.prefHeight(-1)) : 0;
            double bottomHeight = hasBottom ? snapSize(bottom.prefHeight(-1)) : 0;

            double centerX = leftOffset + leftWidth;
            double centerY = topOffset + topHeight;
            double centerWidth = contentWidth - leftWidth - rightWidth;
            double centerHeight = contentHeight - topHeight - bottomHeight;

            if (hasTop) {
                top.resize(centerWidth, topHeight);
                top.relocate(centerX, topOffset);
            }

            if (hasLeft) {
                // Swap width and height because the bar is rotated 90 degrees.
                left.resize(centerHeight, leftWidth);
                double rotateOffset = (centerHeight - leftWidth) / 2;
                left.relocate(leftOffset - rotateOffset, centerY + rotateOffset);
            }

            if (hasRight) {
                // Swap width and height because the bar is rotated 90 degrees.
                right.resize(centerHeight, rightWidth);
                double rotateOffset = (centerHeight - rightWidth) / 2;
                right.relocate(leftOffset + contentWidth - rightWidth - rotateOffset, centerY + rotateOffset);
            }

            if (hasBottom) {
                bottom.resize(centerWidth, bottomHeight);
                bottom.relocate(leftOffset + leftWidth, topOffset + contentHeight - bottomHeight);
            }

            if (hasCenter) {
                center.resize(centerWidth, centerHeight);
                center.relocate(centerX, centerY);
            }
        }
    }

    static class SideBar extends Region {
        private static final PseudoClass[] SIDE_PSEUDO_CLASSES = {
                PseudoClass.getPseudoClass("top"),
                PseudoClass.getPseudoClass("bottom"),
                PseudoClass.getPseudoClass("left"),
                PseudoClass.getPseudoClass("right")};

        private final Side side;

        private TabButtonBar topLeftBar;
        private TabButtonBar bottomRightBar;

        public TabButtonBar getTopLeftBar() {
            return topLeftBar;
        }

        public void setTopLeftBar(TabButtonBar topLeftBar) {
            if (this.topLeftBar != null) getChildren().remove(this.topLeftBar);
            this.topLeftBar = topLeftBar;
            if (topLeftBar != null) getChildren().add(topLeftBar);
        }

        public TabButtonBar getBottomRightBar() {
            return bottomRightBar;
        }

        public void setBottomRightBar(TabButtonBar bottomRightBar) {
            if (this.bottomRightBar != null) getChildren().remove(this.bottomRightBar);
            this.bottomRightBar = bottomRightBar;
            if (bottomRightBar != null) getChildren().add(bottomRightBar);
        }

        SideBar(Side side) {
            this.side = side;

            setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);

            getStyleClass().setAll("side-bar");
            pseudoClassStateChanged(SIDE_PSEUDO_CLASSES[side.ordinal()], true);

            getChildren().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    if (getChildren().isEmpty()) {
                        setManaged(false);
                        setVisible(false);
                    }
                }
            });

            if (side.isVertical()) setRotate(90);
        }

        public Side getSide() {
            return side;
        }

        @Override
        protected double computePrefWidth(double height) {
            double left = snappedLeftInset();
            double right = snappedRightInset();
            double width = 0;
            for (Node child : getChildren()) {
                if (child.isManaged()) {
                    width += snapSize(child.prefWidth(-1));
                }
            }
            return left + width + right;
        }

        @Override
        protected double computePrefHeight(double width) {
            double top = snappedTopInset();
            double bottom = snappedBottomInset();
            double height = 0;
            for (Node child : getChildren()) {
                if (child.isManaged()) {
                    double childHeight = snapSize(child.prefHeight(-1));
                    if (childHeight > height) height = childHeight;
                }
            }
            return top + height + bottom;
        }

        @Override
        protected void layoutChildren() {
            double width = getWidth();
            double height = getHeight();
            double left = snappedLeftInset();
            double right = snappedRightInset();
            double top = snappedTopInset();
            double bottom = snappedBottomInset();
            double contentWidth = width - left - right;
            double contentHeight = height - top - bottom;

            if (topLeftBar != null && topLeftBar.isManaged()) {
                layoutInArea(topLeftBar, left, top, contentWidth, contentHeight, 0, HPos.LEFT, VPos.CENTER);
                double childWidth = snapSize(topLeftBar.prefWidth(-1));
                left += childWidth;
                contentWidth -= childWidth;
            }

            if (bottomRightBar != null && bottomRightBar.isManaged()) {
                layoutInArea(bottomRightBar, left, top, contentWidth, contentHeight, 0, HPos.RIGHT, VPos.CENTER);
            }
        }
    }

    static class TabButtonBar extends Region {

        private final ViewGroup viewGroup;

        public TabButtonBar(ViewGroup viewGroup) {
            this.viewGroup = viewGroup;

            getStyleClass().setAll("tab-button-bar");

            viewGroup.getTabs().forEach(this::addViewTab);
            viewGroup.getTabs().addListener(new ListChangeListener<ViewTab>() {
                @Override
                public void onChanged(Change<? extends ViewTab> c) {
                    while (c.next()) {
                        if (c.wasRemoved()) {
                            for (ViewTab viewTab : c.getRemoved()) {
                                removeViewTab(viewTab);
                            }
                        }
                        if (c.wasAdded()) {
                            for (ViewTab viewTab : c.getAddedSubList()) {
                                addViewTab(viewTab);
                            }
                        }
                    }
                }
            });
        }

        public ViewGroup getViewGroup() {
            return viewGroup;
        }

        public void addViewTab(ViewTab viewTab) {
            getChildren().add(new TabButton(viewTab));
        }

        public void removeViewTab(ViewTab viewTab) {
            getChildren().removeIf(node -> ((TabButton) node).getTab() == viewTab);
        }

        @Override
        protected double computePrefWidth(double height) {
            double left = snappedLeftInset();
            double right = snappedRightInset();
            double width = 0;
            for (Node child : getChildren()) {
                if (child.isManaged()) {
                    width += snapSize(child.prefWidth(-1));
                }
            }
            return left + width + right;
        }

        @Override
        protected double computePrefHeight(double width) {
            double top = snappedTopInset();
            double bottom = snappedBottomInset();
            double height = 0;
            for (Node child : getChildren()) {
                if (child.isManaged()) {
                    double childHeight = snapSize(child.prefHeight(-1));
                    if (childHeight > height) height = childHeight;
                }
            }
            return top + height + bottom;
        }

        @Override
        protected void layoutChildren() {
            double width = getWidth();
            double height = getHeight();
            double left = snappedLeftInset();
            double right = snappedRightInset();
            double top = snappedTopInset();
            double bottom = snappedBottomInset();
            double contentWidth = width - left - right;
            double contentHeight = height - top - bottom;

            Side secondary = viewGroup.getPos().getSecondary();

            if (secondary == Side.TOP || secondary == Side.LEFT) {// 自左向右布局
                for (Node child : getChildren()) {
                    if (child.isManaged()) {
                        layoutInArea(child, left, top, contentWidth, contentHeight, 0, HPos.LEFT, VPos.CENTER);
                        double childWidth = snapSize(child.prefWidth(-1));
                        left += childWidth;
                        contentWidth -= childWidth;
                    }
                }
            } else {// 自右向左布局
                for (Node child : getChildren()) {
                    if (child.isManaged()) {
                        layoutInArea(child, left, top, contentWidth, contentHeight, 0, HPos.RIGHT, VPos.CENTER);
                        contentWidth -= snapSize(child.prefWidth(-1));
                    }
                }
            }
        }
    }

    static class DivisionArea extends Region {
        @Override
        protected double computePrefWidth(double height) {
            return super.computePrefWidth(height);
        }

        @Override
        protected double computePrefHeight(double width) {
            return super.computePrefHeight(width);
        }

        @Override
        protected void layoutChildren() {
            super.layoutChildren();
        }
    }

    static class ContentDivider extends Region {
        private final ViewPane.Divider divider;

        public ContentDivider(ViewPane.Divider divider) {
            this.divider = divider;
        }
    }

    static class TabButton extends ButtonBase {
        private final ViewTab tab;

        private final InvalidationListener graphicsInvalidationListener = observable -> updateGraphic();
        private final InvalidationListener viewGroupInvalidationListener = observable -> updatePos();

        public TabButton(ViewTab tab) {
            this.tab = tab;

            getStyleClass().setAll("tab-button");

            textProperty().bind(tab.textProperty());

            tab.graphicProperty().addListener(new WeakInvalidationListener(graphicsInvalidationListener));
            tab.viewGroupProperty().addListener(new WeakInvalidationListener(viewGroupInvalidationListener));
            updatePos();
            updateGraphic();
        }

        public ViewTab getTab() {
            return tab;
        }

        public EightPos getPos() {
            ViewGroup viewGroup = tab.getViewGroup();
            assert viewGroup != null;
            return viewGroup.getPos();
        }

        private void updatePos() {
            if (getPos().getPrimary() == Side.LEFT) {
                setRotate(180);
            } else {
                setRotate(0);
            }
            updateGraphic();
        }

        private void updateGraphic() {
            Node graphic = tab.getGraphic();
            setGraphic(graphic);

            if (graphic == null) return;
            if (getPos().getPrimary() == Side.LEFT) {
                graphic.setRotate(90);
                setTextAlignment(TextAlignment.RIGHT);
            } else {
                graphic.setRotate(0);
                setTextAlignment(TextAlignment.LEFT);
            }
        }

        @Override
        public void fire() {
            if (!isDisabled()) {
                tab.setSelected(!tab.isSelected());
                fireEvent(new ActionEvent());
            }
        }

        @Override
        protected Skin<?> createDefaultSkin() {
            return new TabButtonSkin(this);
        }
    }

    static class TabButtonSkin extends LabeledSkinBase<TabButton, ButtonBehavior<TabButton>> {

        public TabButtonSkin(TabButton labeled) {
            super(labeled, new ButtonBehavior<>(labeled));
        }
    }
}
