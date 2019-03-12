package com.westeroscraft.nodes;

import javafx.beans.DefaultProperty;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

@DefaultProperty("image")
public class ProgressArea extends StackPane {
	
	private final ObjectProperty<Image> property;
	
	private ImageView iv;
	private ProgressBar pb;
	private Text text;
	
	public ProgressArea(){ this(null, null); }
	
	public ProgressArea(@NamedArg("text")String text){ this(text, null); }
	
	public ProgressArea(Image img){	this(null, img); }
	
	public ProgressArea(String text, Image img){
		this.getStyleClass().add("progressarea");
		this.iv = new ImageView(img);
		this.property = iv.imageProperty();
		this.pb = new ProgressBar();
		this.text = new Text(text);
		this.construct();
	}
	
	private void construct(){
		
		//Fix imageview
		iv.setPreserveRatio(true);
		iv.setFitHeight(30);
		
		
		//Pane for imageview
		StackPane left = new StackPane();
		left.getStyleClass().add("leftpane");
		left.getChildren().add(iv);
		
		//Main container
		HBox main = new HBox();
		main.getStyleClass().add("mainpane");
		main.setAlignment(Pos.CENTER);
		
		//Container for text and progressbar
		VBox right = new VBox();
		right.getStyleClass().add("rightpane");
		right.setAlignment(Pos.CENTER_LEFT);
		right.getChildren().addAll(text, pb);
		
		//Progressbar
		pb.prefWidthProperty().bind(right.widthProperty());
		
		main.getChildren().addAll(left, right);
		
		this.getChildren().add(main);
		
		HBox.setHgrow(this, Priority.ALWAYS);
		HBox.setHgrow(main, Priority.ALWAYS);
		
	}
	
	public final Image getImage(){
		return property.get();
	}
	
	public final void setImage(Image image){
		property.set(image);
	}
	
}
