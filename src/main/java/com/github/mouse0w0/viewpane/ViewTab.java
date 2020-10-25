package com.github.mouse0w0.viewpane;

import com.github.mouse0w0.viewpane.geometry.EightPos;
import javafx.beans.property.*;
import javafx.scene.Node;

public class ViewTab {

    public ViewTab() {
    }

    public ViewTab(String text) {
        setText(text);
    }

    private ReadOnlyObjectWrapper<ViewPane> viewPane;

    private ReadOnlyObjectWrapper<ViewPane> viewPanePropertyImpl() {
        if (viewPane == null) {
            viewPane = new ReadOnlyObjectWrapper<>(this, "viewPane");
        }
        return viewPane;
    }

    final void setViewPane(ViewPane viewPane) {
        viewPanePropertyImpl().set(viewPane);
    }

    public final ReadOnlyObjectProperty<ViewPane> viewPaneProperty() {
        return viewPanePropertyImpl().getReadOnlyProperty();
    }

    public final ViewPane getViewPane() {
        return viewPane == null ? null : viewPane.get();
    }

    private ObjectProperty<EightPos> pos;

    public ObjectProperty<EightPos> posProperty() {
        if (pos == null) {
            pos = new SimpleObjectProperty<>(this, "pos", EightPos.LEFT_TOP);
        }
        return pos;
    }

    public EightPos getPos() {
        return pos == null ? EightPos.LEFT_TOP : pos.get();
    }

    public void setPos(EightPos pos) {
        posProperty().set(pos);
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

    private ReadOnlyBooleanWrapper selected;

    private ReadOnlyBooleanWrapper selectedPropertyImpl() {
        if (selected == null) {
            selected = new ReadOnlyBooleanWrapper(this, "selected");
        }
        return selected;
    }

    public final ReadOnlyBooleanProperty selectedProperty() {
        return selectedPropertyImpl().getReadOnlyProperty();
    }

    public final boolean isSelected() {
        return selected != null && selected.get();
    }

    final void setSelected(boolean selected) {
        selectedPropertyImpl().set(selected);
    }
}
