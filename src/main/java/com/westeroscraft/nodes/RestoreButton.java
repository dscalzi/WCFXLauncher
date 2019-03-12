package com.westeroscraft.nodes;

import com.westeroscraft.lib.borderless.BorderlessScene;

import javafx.beans.NamedArg;
import javafx.scene.control.Button;

public class RestoreButton extends Button{

	private static String styleClass = "restorebutton";
	
	private static char maximizedIcon = (char)9633;
	private static char restoreDownIcon = (char)8863;
	
	public RestoreButton(){
		super(Character.toString(maximizedIcon));
		this.getStyleClass().add(styleClass);
		this.setOnAction(event -> {
			BorderlessScene bscene = (BorderlessScene)((Button)event.getSource()).getScene();
			boolean state = bscene.isMaximized();
			bscene.maximize();
			if(state)
				this.setText(Character.toString(maximizedIcon));
			else
				this.setText(Character.toString(restoreDownIcon));
		});
	}
	
	public RestoreButton(@NamedArg("height")double height, @NamedArg("width")double width){
		this();
		this.setMinHeight(height);
		this.setMinWidth(width);
		this.setMaxHeight(height);
		this.setMaxWidth(width);
	}
	
}
