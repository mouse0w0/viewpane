package com.github.mouse0w0.viewpane;

import com.github.mouse0w0.viewpane.geometry.EightPos;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewPaneTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewPane pane = new ViewPane();

        ViewTab tab1 = new ViewTab("First");
        ViewTab tab2 = new ViewTab("Second");
        ViewTab tab3 = new ViewTab("Third");

        ViewTab tab5 = new ViewTab("LEFT_BOTTOM");
        tab5.setPos(EightPos.LEFT_BOTTOM);

        ViewTab tab6 = new ViewTab("TOP_LEFT");
        tab6.setPos(EightPos.TOP_LEFT);

        ViewTab tab8 = new ViewTab("TOP_RIGHT");
        tab8.setPos(EightPos.TOP_RIGHT);

        ViewTab tab9 = new ViewTab("BOTTOM_LEFT");
        tab9.setPos(EightPos.BOTTOM_LEFT);

        ViewTab tab10 = new ViewTab("BOTTOM_RIGHT");
        tab10.setPos(EightPos.BOTTOM_RIGHT);

        ViewTab tab11 = new ViewTab("RIGHT_TOP");
        tab11.setPos(EightPos.RIGHT_TOP);

        ViewTab tab12 = new ViewTab("RIGHT_BOTTOM");
        tab12.setPos(EightPos.RIGHT_BOTTOM);

        pane.getTabs().addAll(tab1, tab2, tab3, tab5, tab6, tab8, tab9, tab10, tab11, tab12);

        primaryStage.setTitle("ViewPane");
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }
}
