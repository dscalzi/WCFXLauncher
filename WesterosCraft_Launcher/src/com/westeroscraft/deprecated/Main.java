package com.westeroscraft.deprecated;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Early version of the launcher intended as a proof-of-concept.
 * This class remains only as a record and its use is not
 * recommended.
 * 
 * @author Daniel D. Scalzi
 *
 */
@Deprecated
public class Main extends Application{

	boolean firstLoad = true;
	
	public static void main(String[] args){
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//primaryStage.initStyle(StageStyle.UNDECORATED);
		
		VBox layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		
		final WebView browser = new WebView();
		final WebEngine webEngine = browser.getEngine();
		final ProgressBar progressBar = new ProgressBar(0);
		progressBar.setId("load-bar");
		
		progressBar.progressProperty().bind(webEngine.getLoadWorker().progressProperty().add(.20D));
		progressBar.prefWidthProperty().bind(layout.widthProperty());
		//progressBar.
		
		webEngine.setJavaScriptEnabled(true);
		webEngine.getLoadWorker().stateProperty().addListener(
		        new ChangeListener<State>() {
		            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, State oldState, State newState) {
		                if (newState == State.SUCCEEDED) {
		                	if(firstLoad){
		                		webEngine.executeScript("window.scrollTo(0,80)");
		                		firstLoad = false;
		                		primaryStage.show();
		                	}
		                }
		            }
		        });
		webEngine.load("http://westeroscraft.com/servernews");
		browser.setZoom(.58);
		
		layout.getChildren().addAll(progressBar, browser);
		
		Scene scene = new Scene(layout);
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
				
		primaryStage.setScene(scene);
		primaryStage.setHeight(500);
		primaryStage.setWidth(850);
		primaryStage.show();
	}
	
}
