package com.github.mouse0w0.viewpane;

import com.github.mouse0w0.viewpane.geometry.DividerPos;
import com.github.mouse0w0.viewpane.geometry.EightPos;
import com.github.mouse0w0.viewpane.skin.ViewPaneSkin;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
                            if (getViewGroupImpl(viewGroup.getPos()) == viewGroup) {
                                cacheViewGroups[viewGroup.getPos().ordinal()] = null;
                            }
                        }
                    }
                    if (c.wasAdded()) {
                        for (ViewGroup viewGroup : c.getAddedSubList()) {
                            viewGroup.setViewPane(ViewPane.this);
                            ViewGroup oldViewGroup = getViewGroupImpl(viewGroup.getPos());
                            if (oldViewGroup != null) {
                                getViewGroups().remove(oldViewGroup);
                            }
                            cacheViewGroups[viewGroup.getPos().ordinal()] = viewGroup;
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

    @Override
    public String getUserAgentStylesheet() {
        return ViewPane.class.getResource("ViewPane.css").toExternalForm();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ViewPaneSkin(this);
    }

    public static class Divider {
        private final DividerPos pos;

        public Divider(DividerPos pos) {
            this.pos = pos;
        }

        public DividerPos getPos() {
            return pos;
        }

        private DoubleProperty position;

        public final void setPosition(double value) {
            positionProperty().set(value);
        }

        public final double getPosition() {
            return position == null ? 0.5F : position.get();
        }

        public final DoubleProperty positionProperty() {
            if (position == null) {
                position = new SimpleDoubleProperty(this, "position", 0.5F);
            }
            return position;
        }
    }
}
