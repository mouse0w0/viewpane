package com.github.mouse0w0.viewpane;

import com.github.mouse0w0.viewpane.geometry.DividerPos;
import com.github.mouse0w0.viewpane.skin.ViewPaneSkin;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class ViewPane extends Control {

    public ViewPane() {
        getStyleClass().setAll("view-pane");

        getTabs().addListener(new ListChangeListener<ViewTab>() {
            @Override
            public void onChanged(Change<? extends ViewTab> c) {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        for (ViewTab viewTab : c.getRemoved()) {
                            viewTab.setViewPane(null);
                        }
                    }
                    if (c.wasAdded()) {
                        for (ViewTab viewTab : c.getAddedSubList()) {
                            viewTab.setViewPane(ViewPane.this);
                        }
                    }
                }
            }
        });
    }

    private final ObservableList<ViewTab> tabs = FXCollections.observableArrayList();

    public ObservableList<ViewTab> getTabs() {
        return tabs;
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
