package com.github.mouse0w0.viewpane;

import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.control.SingleSelectionModel;

public class ViewTab {

    public ViewTab() {
    }

    public ViewTab(String text) {
        setText(text);
    }

    private ReadOnlyObjectWrapper<ViewGroup> viewGroup;

    private ReadOnlyObjectWrapper<ViewGroup> viewGroupPropertyImpl() {
        if (viewGroup == null) {
            viewGroup = new ReadOnlyObjectWrapper<>(this, "viewGroup");
        }
        return viewGroup;
    }

    final void setViewGroup(ViewGroup viewGroup) {
        viewGroupPropertyImpl().set(viewGroup);
    }

    public final ReadOnlyObjectProperty<ViewGroup> viewGroupProperty() {
        return viewGroupPropertyImpl().getReadOnlyProperty();
    }

    public final ViewGroup getViewGroup() {
        return viewGroup == null ? null : viewGroup.get();
    }

    private StringProperty text;

    public final StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty(this, "text");
        }
        return text;
    }

    public final String getText() {
        return text == null ? null : text.get();
    }

    public final void setText(String value) {
        textProperty().set(value);
    }

    private ObjectProperty<Node> graphic;

    public final ObjectProperty<Node> graphicProperty() {
        if (graphic == null) {
            graphic = new SimpleObjectProperty<>(this, "graphic");
        }
        return graphic;
    }

    public final Node getGraphic() {
        return graphic == null ? null : graphic.get();
    }

    public final void setGraphic(Node value) {
        graphicProperty().set(value);
    }

    private ObjectProperty<Node> content;

    public final ObjectProperty<Node> contentProperty() {
        if (content == null) {
            content = new SimpleObjectProperty<>(this, "content");
        }
        return content;
    }

    public final Node getContent() {
        return content == null ? null : content.get();
    }

    public final void setContent(Node value) {
        contentProperty().set(value);
    }

    private BooleanProperty selected;

    public final BooleanProperty selectedProperty() {
        if (selected == null) {
            selected = new SimpleBooleanProperty(this, "selected") {
                @Override
                protected void invalidated() {
                    ViewGroup viewGroup = getViewGroup();
                    if (viewGroup != null) {
                        SingleSelectionModel<ViewTab> selectionModel = viewGroup.getSelectionModel();
                        if (isSelected()) {
                            selectionModel.select(ViewTab.this);
                        } else if (selectionModel.getSelectedItem() == ViewTab.this) {
                            selectionModel.clearSelection();
                        }
                    }
                }
            };
        }
        return selected;
    }

    public final boolean isSelected() {
        return selected != null && selected.get();
    }

    public final void setSelected(boolean selected) {
        selectedProperty().set(selected);
    }
}
