package com.github.mouse0w0.viewpane.skin;

import com.github.mouse0w0.viewpane.ViewPane;
import com.github.mouse0w0.viewpane.ViewTab;
import com.github.mouse0w0.viewpane.geometry.EightPos;
import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.FXCollections;
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
    private final TabButtonArea tabButtonArea;
    private final DivisionArea divisionArea;

    public ViewPaneSkin(ViewPane control) {
        super(control);

        tabButtonArea = new TabButtonArea();
        getChildren().add(tabButtonArea);

        divisionArea = new DivisionArea();
        tabButtonArea.setCenter(divisionArea);

        getTabs().forEach(TabButton::new);
        getTabs().addListener(new ListChangeListener<ViewTab>() {
            @Override
            public void onChanged(Change<? extends ViewTab> c) {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        for (ViewTab viewTab : c.getRemoved()) {
                            removeTabButton(viewTab);
                        }
                    }
                    if (c.wasAdded()) {
                        for (ViewTab viewTab : c.getAddedSubList()) {
                            addTabButton(viewTab);
                        }
                    }
                }
            }
        });
    }

    private ObservableList<ViewTab> getTabs() {
        return getSkinnable().getTabs();
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return snappedLeftInset() + snapSize(tabButtonArea.prefWidth(-1)) + snappedRightInset();
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return snappedTopInset() + snapSize(tabButtonArea.prefHeight(-1)) + snappedBottomInset();
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        tabButtonArea.resize(contentWidth, contentHeight);
        tabButtonArea.relocate(contentX, contentY);
    }

    private TabButtonBar getTabButtonBar(Side side) {
        TabButtonBar tabButtonBar = tabButtonArea.getTabButtonBar(side);
        if (tabButtonBar != null) return tabButtonBar;

        tabButtonBar = new TabButtonBar(side);
        if (side == Side.LEFT)
            tabButtonArea.setLeft(tabButtonBar);
        else if (side == Side.RIGHT)
            tabButtonArea.setRight(tabButtonBar);
        else if (side == Side.BOTTOM)
            tabButtonArea.setBottom(tabButtonBar);
        else
            tabButtonArea.setTop(tabButtonBar);

        return tabButtonBar;
    }

    private ObservableList<TabButton> getTabButtons(EightPos pos) {
        TabButtonBar tabButtonBar = getTabButtonBar(pos.getPrimary());
        Side secondary = pos.getSecondary();
        return secondary == Side.TOP || secondary == Side.LEFT ?
                tabButtonBar.getTopLeftButtons() : tabButtonBar.getBottomRightButtons();
    }

    private TabButton getTabButton(ViewTab tab) {
        for (TabButton tabButton : getTabButtons(tab.getPos())) {
            if (tabButton.getTab() == tab) return tabButton;
        }
        return null;
    }

    private void addTabButton(ViewTab tab) {
        new TabButton(tab);
    }

    private void removeTabButton(ViewTab tab) {
        getTabButtons(tab.getPos()).removeIf(tabButton -> tabButton.getTab() == tab);
    }

    static class TabButtonArea extends Region {
        private TabButtonBar top;
        private TabButtonBar left;
        private TabButtonBar bottom;
        private TabButtonBar right;
        private Node center;

        public TabButtonArea() {
            getStyleClass().setAll("tab-button-area");
        }

        public TabButtonBar getTabButtonBar(Side side) {
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

        public void setTop(TabButtonBar top) {
            if (this.top != null) getChildren().remove(this.top);
            this.top = top;
            if (top != null) getChildren().add(top);
        }

        public void setLeft(TabButtonBar left) {
            if (this.left != null) getChildren().remove(this.left);
            this.left = left;
            if (left != null) getChildren().add(left);
        }

        public void setBottom(TabButtonBar bottom) {
            if (this.bottom != null) getChildren().remove(this.bottom);
            this.bottom = bottom;
            if (bottom != null) getChildren().add(bottom);
        }

        public void setRight(TabButtonBar right) {
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

    static class TabButtonBar extends Region {
        private static final PseudoClass[] SIDE_PSEUDO_CLASSES = {
                PseudoClass.getPseudoClass("top"),
                PseudoClass.getPseudoClass("bottom"),
                PseudoClass.getPseudoClass("left"),
                PseudoClass.getPseudoClass("right")};

        private final Side side;

        private final ObservableList<TabButton> topLeftButtons = FXCollections.observableArrayList();
        private final ObservableList<TabButton> bottomRightButtons = FXCollections.observableArrayList();

        private final ListChangeListener<TabButton> listChangeListener = new ListChangeListener<TabButton>() {
            @Override
            public void onChanged(Change<? extends TabButton> c) {
                while (c.next()) {
                    ObservableList<Node> children = getChildren();
                    if (c.wasAdded()) children.addAll(c.getAddedSubList());
                    if (c.wasRemoved()) children.removeAll(c.getRemoved());
                }
            }
        };

        TabButtonBar(Side side) {
            this.side = side;

            setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);

            getStyleClass().setAll("tab-button-bar");
            pseudoClassStateChanged(SIDE_PSEUDO_CLASSES[side.ordinal()], true);

            topLeftButtons.addListener(listChangeListener);
            bottomRightButtons.addListener(listChangeListener);

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

        public ObservableList<TabButton> getTopLeftButtons() {
            return topLeftButtons;
        }

        public ObservableList<TabButton> getBottomRightButtons() {
            return bottomRightButtons;
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

            // 自左向右布局
            for (TabButton child : getTopLeftButtons()) {
                if (child.isManaged()) {
                    layoutInArea(child, left, top, contentWidth, contentHeight, 0, HPos.LEFT, VPos.CENTER);
                    double childWidth = snapSize(child.prefWidth(-1));
                    left += childWidth;
                    contentWidth -= childWidth;
                }
            }
            // 自右向左布局
            for (TabButton child : getBottomRightButtons()) {
                if (child.isManaged()) {
                    layoutInArea(child, left, top, contentWidth, contentHeight, 0, HPos.RIGHT, VPos.CENTER);
                    contentWidth -= snapSize(child.prefWidth(-1));
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

    class TabButton extends ButtonBase {
        private final ViewTab tab;

        private final InvalidationListener graphicsInvalidationListener = observable -> updateGraphic();
        private final InvalidationListener posInvalidationListener = observable -> updatePos();

        public TabButton(ViewTab tab) {
            this.tab = tab;

            getStyleClass().setAll("tab-button");

            textProperty().bind(tab.textProperty());

            tab.graphicProperty().addListener(new WeakInvalidationListener(graphicsInvalidationListener));
            tab.posProperty().addListener(new WeakInvalidationListener(posInvalidationListener));
            updatePos();
            updateGraphic();
        }

        public ViewTab getTab() {
            return tab;
        }

        public EightPos getPos() {
            return tab.getPos();
        }

        private void updatePos() {
            EightPos pos = getPos();
            getTabButtons(pos).add(this);

            if (pos.getPrimary() == Side.LEFT) {
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
