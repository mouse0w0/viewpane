package com.github.mouse0w0.viewpane;

import com.github.mouse0w0.viewpane.geometry.DividerType;
import com.github.mouse0w0.viewpane.geometry.EightPos;
import com.github.mouse0w0.viewpane.skin.ViewPaneSkin;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class ViewPane extends Control {

    public ViewPane() {
        getStyleClass().setAll("view-pane");

        getViewGroups().addListener(new ListChangeListener<ViewGroup>() {
            @Override
            public void onChanged(Change<? extends ViewGroup> c) {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        for (ViewGroup viewGroup : c.getRemoved()) {
                            viewGroup.setViewPane(null);
                            EightPos pos = viewGroup.getPos();
                            if (getViewGroupImpl(pos) == viewGroup) {
                                cacheViewGroups[pos.ordinal()] = null;
                            }
                        }
                    }
                    if (c.wasAdded()) {
                        for (ViewGroup viewGroup : c.getAddedSubList()) {
                            viewGroup.setViewPane(ViewPane.this);
                            EightPos pos = viewGroup.getPos();
                            ViewGroup oldViewGroup = getViewGroupImpl(pos);
                            if (oldViewGroup != null) {
                                getViewGroups().remove(oldViewGroup);
                            }
                            cacheViewGroups[pos.ordinal()] = viewGroup;
                        }
                    }
                }
            }
        });
    }

    private final ViewGroup[] cacheViewGroups = new ViewGroup[8];
    private final ObservableList<ViewGroup> viewGroups = FXCollections.observableArrayList();

    public final ObservableList<ViewGroup> getViewGroups() {
        return viewGroups;
    }

    public final ViewGroup getViewGroup(EightPos pos) {
        ViewGroup viewGroup = getViewGroupImpl(pos);
        if (viewGroup == null) {
            viewGroup = new ViewGroup(pos);
            getViewGroups().add(viewGroup);
        }
        return viewGroup;
    }

    private ViewGroup getViewGroupImpl(EightPos pos) {
        return cacheViewGroups[pos.ordinal()];
    }

    private ObjectProperty<Node> content;

    public final ObjectProperty<Node> contentProperty() {
        if (content == null) {
            content = new SimpleObjectProperty<>(this, "content");
        }
        return content;
    }

    public final Node getContent() {
        return content.get();
    }

    public final void setContent(Node content) {
        contentProperty().set(content);
    }

    private final Divider[] cacheDividers = new Divider[8];
    private final ObservableList<Divider> dividers = FXCollections.observableArrayList();
    private final ObservableList<Divider> unmodifiableDividers = FXCollections.unmodifiableObservableList(dividers);

    public final ObservableList<Divider> getDividers() {
        return unmodifiableDividers;
    }

    public final Divider getDivider(DividerType type) {
        Divider divider = cacheDividers[type.ordinal()];
        if (divider == null) {
            divider = new Divider(type);
            cacheDividers[type.ordinal()] = divider;
            dividers.add(divider);
        }
        return divider;
    }

    public final void setDividerPosition(DividerType type, double position) {
        getDivider(type).setPosition(position);
    }

    @Override
    public String getUserAgentStylesheet() {
        return ViewPane.class.getResource("ViewPane.css").toExternalForm();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ViewPaneSkin(this);
    }

    public static class Divider {
        private final DividerType type;

        public Divider(DividerType type) {
            this.type = type;
        }

        public DividerType getType() {
            return type;
        }

        private DoubleProperty position;

        public final void setPosition(double value) {
            positionProperty().set(value);
        }

        public final double getPosition() {
            return position == null ? computePrefPosition() : position.get();
        }

        public final DoubleProperty positionProperty() {
            if (position == null) {
                position = new SimpleDoubleProperty(this, "position", computePrefPosition());
            }
            return position;
        }

        private double computePrefPosition() {
            return type.isPrimary() ? (type.getSide() == Side.TOP || type.getSide() == Side.LEFT ? 0.2 : 0.8) : 0.5;
        }
    }
}
