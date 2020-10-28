package com.github.mouse0w0.viewpane;

import com.github.mouse0w0.viewpane.geometry.EightPos;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class ViewPaneTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewPane pane = new ViewPane();

        ViewGroup leftTop = pane.getViewGroup(EightPos.LEFT_TOP);
        ViewTab explorer = new ViewTab("Explorer");
        TreeView<String> treeView = new TreeView<>();

        TreeItem<String> root = new TreeItem<>();
        root.getChildren().addAll(new TreeItem<>("Hello"), new TreeItem<>("World"));
        treeView.setRoot(root);

        leftTop.getTabs().addAll(explorer);
        explorer.setContent(treeView);

        ViewGroup leftBottom = pane.getViewGroup(EightPos.LEFT_BOTTOM);
        leftBottom.getTabs().addAll(new ViewTab("LEFT_BOTTOM"));

        ViewGroup topLeft = pane.getViewGroup(EightPos.TOP_LEFT);
        topLeft.getTabs().addAll(new ViewTab("TOP_LEFT"));

        ViewGroup topRight = pane.getViewGroup(EightPos.TOP_RIGHT);
        topRight.getTabs().addAll(new ViewTab("TOP_RIGHT"));

        ViewGroup bottomLeft = pane.getViewGroup(EightPos.BOTTOM_LEFT);
        bottomLeft.getTabs().addAll(new ViewTab("BOTTOM_LEFT"));

        ViewGroup bottomRight = pane.getViewGroup(EightPos.BOTTOM_RIGHT);
        bottomRight.getTabs().addAll(new ViewTab("BOTTOM_RIGHT"));

        ViewGroup rightTop = pane.getViewGroup(EightPos.RIGHT_TOP);
        rightTop.getTabs().addAll(new ViewTab("RIGHT_TOP"));

        ViewGroup rightBottom = pane.getViewGroup(EightPos.RIGHT_BOTTOM);
        rightBottom.getTabs().addAll(new ViewTab("RIGHT_BOTTOM"));

        primaryStage.setTitle("ViewPane");
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }
}
