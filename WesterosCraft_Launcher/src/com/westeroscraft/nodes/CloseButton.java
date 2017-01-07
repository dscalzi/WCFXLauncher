package com.westeroscraft.nodes;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.scene.control.Button;

public class CloseButton extends Button{

	private static String styleClass = "closebutton";
	
	public CloseButton(){
		super("X");
		this.getStyleClass().add(styleClass);
		this.setOnAction(event -> {
			Platform.exit();
		});
	}
	
	public CloseButton(@NamedArg("height")double height, @NamedArg("width")double width){
		this();
		this.setMinHeight(height);
		this.setMinWidth(width);
		this.setMaxHeight(height);
		this.setMaxWidth(width);
	}
}
