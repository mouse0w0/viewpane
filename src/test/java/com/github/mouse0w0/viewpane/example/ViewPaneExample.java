package com.github.mouse0w0.viewpane.example;

import com.github.mouse0w0.viewpane.ViewPane;
import com.github.mouse0w0.viewpane.ViewTab;
import com.github.mouse0w0.viewpane.geometry.EightPos;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ViewPaneExample extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewPane viewPane = new ViewPane();
        viewPane.setPrefSize(1280, 720);

        initLeftTop(viewPane);
        initLeftBottom(viewPane);
        initBottomLeft(viewPane);
        initBottomRight(viewPane);
        initRightTop(viewPane);
        initCenter(viewPane);

        primaryStage.setTitle("ViewPane");
        primaryStage.setScene(new Scene(viewPane));
        primaryStage.show();
    }

    private static void initLeftTop(ViewPane viewPane) {
        TreeItem<String> geometry = new TreeItem<>("geometry");
        geometry.getChildren().setAll(new TreeItem<>("EightPos"));

        TreeItem<String> skin = new TreeItem<>("skin");
        skin.getChildren().setAll(new TreeItem<>("LayoutHelper"), new TreeItem<>("StyleHelper"),
                new TreeItem<>("ViewPaneSkin"));

        TreeItem<String> root = new TreeItem<>("com.github.mouse0w0.viewpane");
        root.getChildren().setAll(geometry, skin,
                new TreeItem<>("DividerType"), new TreeItem<>("ViewGroup"),
                new TreeItem<>("ViewPane"), new TreeItem<>("ViewTab"));

        TreeView<String> treeView = new TreeView<>();
        treeView.setRoot(root);

        ViewTab explorer = new ViewTab("Explorer");
        explorer.setContent(treeView);

        viewPane.getViewGroup(EightPos.LEFT_TOP).getTabs().addAll(explorer);
    }

    private static void initLeftBottom(ViewPane viewPane) {
        TreeItem<String> divisionArea = new TreeItem<>("DivisionArea");
        divisionArea.getChildren().setAll(new TreeItem<>("ContentArea"), new TreeItem<>("ContentDivider"));

        TreeItem<String> tabButtonBar = new TreeItem<>("TabButtonBar");
        tabButtonBar.getChildren().setAll(new TreeItem<>("TabButton"));

        TreeItem<String> sideBar = new TreeItem<>("SideBar");
        sideBar.getChildren().setAll(tabButtonBar, divisionArea);

        TreeItem<String> sideBarArea = new TreeItem<>("SideBarArea");
        sideBarArea.getChildren().setAll(sideBar);

        TreeItem<String> root = new TreeItem<>("ViewPane");
        root.getChildren().setAll(sideBarArea);

        TreeView<String> treeView = new TreeView<>();
        treeView.setRoot(root);

        ViewTab structure = new ViewTab("Structure");
        structure.setContent(treeView);

        viewPane.getViewGroup(EightPos.LEFT_BOTTOM).getTabs().addAll(structure);
    }

    private static void initBottomLeft(ViewPane viewPane) {
        TextArea textArea = new TextArea();
        textArea.setText("C:\\Program Files\\jdk8\\bin\\java.exe ...\n" +
                "\n" +
                "Process finished with exit code 0\n");

        ViewTab run = new ViewTab("Run");
        run.setContent(textArea);

        viewPane.getViewGroup(EightPos.BOTTOM_LEFT).getTabs().setAll(run,
                new ViewTab("Problems"), new ViewTab("Git"),
                new ViewTab("Build"), new ViewTab("Terminal"));
    }

    private static void initBottomRight(ViewPane viewPane) {
        viewPane.getViewGroup(EightPos.BOTTOM_RIGHT).getTabs().setAll(new ViewTab("Messages"));
    }

    private static void initRightTop(ViewPane viewPane) {
        TreeItem<String> tasks = new TreeItem<>("Tasks");
        tasks.getChildren().setAll(new TreeItem<>("build"), new TreeItem<>("build setup"),
                new TreeItem<>("documentation"), new TreeItem<>("help"), new TreeItem<>("other"),
                new TreeItem<>("verification"));

        TreeItem<String> dependencies = new TreeItem<>("Dependencies");
        dependencies.getChildren().setAll(new TreeItem<>("testCompileClasspath"), new TreeItem<>("testRuntimeClasspath"));

        TreeItem<String> viewpane = new TreeItem<>("viewpane");
        viewpane.getChildren().setAll(tasks, dependencies);

        TreeView<String> treeView = new TreeView<>();
        treeView.setRoot(viewpane);

        ViewTab gradle = new ViewTab("Gradle");
        gradle.setContent(treeView);

        viewPane.getViewGroup(EightPos.RIGHT_TOP).getTabs().setAll(gradle,
                new ViewTab("Maven"), new ViewTab("Ant"));
    }

    private static void initCenter(ViewPane viewPane) {
        TextArea textArea = new TextArea("public class HelloViewPane {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello ViewPane\");\n" +
                "    }\n" +
                "}");

        Tab tab = new Tab("HelloViewPane");
        tab.setContent(textArea);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().setAll(tab);

        viewPane.setCenter(tabPane);
    }


}
