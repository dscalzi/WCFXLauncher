package com.westeroscraft;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonParser;
import com.westeroscraft.logging.LauncherOutputStream;
import com.westeroscraft.logging.LoggerUtil;
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
	
	@FXML private WCToggleButton tbbtn_javasettings;
	
	@FXML private ImageView seal_imageview;
	
	
	private Stage w;
	private StackPane popup_launcher;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		this.setupBrowser();
		this.prepareFullLogScene();
		this.setupLog();
		WCToggleButton.setFocus(tbbtn_javasettings);
	}
	
	private void setupBrowser(){
		engine.getEngine().setUserDataDirectory(new File(LauncherExecutor.getDataPath() + "/local/webview"));
		WCToggleButton.setFocus(tbbtn_news);
		webview_main.setZoom(.53);
	}
	
	@FXML
	private void handleNewsAction(ActionEvent e){
		engine.loadPage("http://westeroscraft.com/servernews");
	}
	@FXML
	private void handleMapAction(ActionEvent e){
		engine.loadPage("http://mc.westeroscraft.com/");
	}
	@FXML
	private void handleModsAction(ActionEvent e){
		
	}
	@FXML
	private void handleFaqAction(ActionEvent e){
		engine.loadPage("http://westeroscraft.com/guide");
	}
	@FXML
	private void handleSettingsAction(ActionEvent e){
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
	@FXML
	private void handleHastebinAction(ActionEvent e){
		String hastebinurl = hastebin(launcher_log.getText());
		if(hastebinurl == null) return;
		try {
			Desktop.getDesktop().browse(URI.create(hastebinurl));
		} catch (IOException e1) {
			LoggerUtil.getLogger("Launcher").severe("Unable to open hastebin link: " + hastebinurl);
			e1.printStackTrace();
		}
	}
	public static String hastebin(String data) {

		try {
			URL url = new URL("https://hastebin.com/documents");
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection(); 
			
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Content-Type", "text/plain");
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(data.getBytes());
			wr.flush();
			wr.close();
			
			try(InputStream in = con.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));){
				
				int responseCode = con.getResponseCode();
				
				String response = "";
		        String line = null;
		        while((line = reader.readLine()) != null) {
		        	response = response + line;
		        }
		        JsonParser parser = new JsonParser();
		        String code = parser.parse(response).getAsJsonObject().get("key").getAsString();
		        String hburl = "https://hastebin.com/" + code;
		        LoggerUtil.getLogger("Launcher").info("Hastebin result:\n" + 
		        		"Response code: " + responseCode + ".\n" +
		        		"Hastebin URL: " + hburl);
		        
				return hburl;
			}
			
		} catch (MalformedURLException e) {
			LoggerUtil.getLogger("Launcher").severe("Malformed URL when trying to contact hastebin:");
			e.printStackTrace();
		} catch (ProtocolException e) {
			LoggerUtil.getLogger("Launcher").severe("Invalid protocol provided when attempting to contact hastebin:");
			e.printStackTrace();
		} catch (IOException e) {
			LoggerUtil.getLogger("Launcher").severe("Error occurred while contacting hastebin:");
			e.printStackTrace();
		}
		return null;
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
