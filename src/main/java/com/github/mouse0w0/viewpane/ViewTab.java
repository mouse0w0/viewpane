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

    @Deprecated
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

    private BooleanProperty selected;

    public final BooleanProperty selectedProperty() {
        if (selected == null) {
            selected = new SimpleBooleanProperty(this, "selected");
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
