package com.westeroscraft;

import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;
import com.westeroscraft.logging.LauncherOutputStream;
import com.westeroscraft.nodes.WCEngine;
import com.westeroscraft.nodes.WCToggleButton;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LauncherController implements Initializable{

	@FXML private WebView webview_main;
	@FXML private WCEngine engine;
	@FXML private WCToggleButton tbbtn_news;
	
	@FXML private TextArea launcher_log;
	@FXML private StackPane log_container;
	@FXML private ContextMenu log_contextmenu;
	@FXML private MenuItem log_menuitem_copy;
	@FXML private MenuItem log_menuitem_newwindow;
	
	@FXML private StackPane logpl_container;
	
	@FXML private ImageView seal_imageview;
	
	
	private Stage w;
	private StackPane popup_launcher;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		this.setupBrowser();
		this.prepareFullLogScene();
		this.setupLog();
	}
	
	private void setupBrowser(){
		WCToggleButton.setFocus(tbbtn_news);
		webview_main.setZoom(.53);
	}
	
	@FXML
	private void handleNewsAction(ActionEvent e){
		if(!((WCToggleButton)e.getSource()).isCurrent())
			engine.loadPage("http://westeroscraft.com/servernews");
	}
	@FXML
	private void handleMapAction(ActionEvent e){
		if(!((WCToggleButton)e.getSource()).isCurrent())
			engine.loadPage("http://westeroscraft.com/map");
	}
	@FXML
	private void handleModsAction(ActionEvent e){
		if(!((WCToggleButton)e.getSource()).isCurrent())
			System.out.println("Coming soon!");
	}
	@FXML
	private void handleFaqAction(ActionEvent e){
		if(!((WCToggleButton)e.getSource()).isCurrent())
			engine.loadPage("http://westeroscraft.com/guide");
	}
	@FXML
	private void handleRefreshAction(ActionEvent e){
		if(!((WCToggleButton)e.getSource()).isCurrent())
			System.out.println("Coming soon!");
	}
	
	@FXML
	private void handleDockLogAction(ActionEvent e){
		w.fireEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSE_REQUEST));
	}
	@FXML
	private void handleCopyAction(ActionEvent e){
		launcher_log.copy();
	}
	@FXML
	private void handleSelectAllAction(ActionEvent e){
		launcher_log.selectAll();
	}
	@FXML
	private void handleNewWindowAction(ActionEvent e){
		popup_launcher.setPrefHeight(log_container.getHeight());
		popup_launcher.setPrefWidth(log_container.getWidth());
		//Remove launcher log from the main launcher
		log_container.getChildren().remove(launcher_log);
		popup_launcher.getChildren().add(launcher_log);
		//Make the placeholder visible
		logpl_container.setVisible(true);
		logpl_container.setManaged(true);
		w.show();
		log_menuitem_newwindow.setVisible(false);
	}
	
	private void prepareFullLogScene(){
		w = new Stage();
		w.getIcons().add(seal_imageview.getImage());
		popup_launcher = new StackPane();
		Scene fullLog = new Scene(popup_launcher);
		fullLog.getStylesheets().add(getClass().getResource("resources/styles/core.css").toExternalForm());
		w.setScene(fullLog);
		w.setTitle("WesterosCraft Launcher Log");
		w.setOnCloseRequest(v -> {
			w.close();
			logpl_container.setVisible(false);
			logpl_container.setManaged(false);
			popup_launcher.getChildren().remove(launcher_log);
			log_container.getChildren().add(launcher_log);
			log_menuitem_newwindow.setVisible(true);
		});
	}
	
	private void setupLog(){
		BooleanBinding emptySelection = Bindings.createBooleanBinding(() -> launcher_log.getSelection().getLength() == 0, launcher_log.selectionProperty());
		
		log_menuitem_copy.disableProperty().bind(emptySelection);
		
		LauncherOutputStream log = new LauncherOutputStream(launcher_log);
		PrintStream ps = new PrintStream(log, true);
		System.setOut(ps);
		System.setErr(ps);
	}

}
