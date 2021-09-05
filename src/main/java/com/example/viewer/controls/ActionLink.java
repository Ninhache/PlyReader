package com.example.viewer.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;

public class ActionLink extends Hyperlink {
    public ActionLink(String text) {
        super(text);
    }

    public ActionLink(String text, Node graphic) {
        super(text, graphic);
    }

    public ActionLink(String text, EventHandler<ActionEvent> onClick) {
        this(text, onClick, null);
    }

    public ActionLink(String text, EventHandler<ActionEvent> onClick, Node graphic) {
        super(text, graphic);
        setOnAction(onClick);
    }
}

