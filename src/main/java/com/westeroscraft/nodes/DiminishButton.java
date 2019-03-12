package com.westeroscraft.nodes;

import com.westeroscraft.lib.borderless.BorderlessScene;

import javafx.beans.NamedArg;
import javafx.scene.control.Button;

public class DiminishButton extends Button{

private static String styleClass = "diminishbutton";
	
	public DiminishButton(){
		super("_");
		this.getStyleClass().add(styleClass);
		this.setOnAction(event -> {
			BorderlessScene bscene = (BorderlessScene)((Button)event.getSource()).getScene();
            bscene.minimize();
		});
	}

	public DiminishButton(@NamedArg("height")double height, @NamedArg("width")double width){
		this();
		this.setMinHeight(height);
		this.setMinWidth(width);
		this.setMaxHeight(height);
		this.setMaxWidth(width);
	}
	
}
