package com.westeroscraft.deprecated;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.westeroscraft.lib.borderless.BorderlessScene;
import com.westeroscraft.logging.LauncherOutputStream;
import com.westeroscraft.nodes.CloseButton;
import com.westeroscraft.nodes.DiminishButton;
import com.westeroscraft.nodes.RestoreButton;
import com.westeroscraft.nodes.WCEngine;
import com.westeroscraft.nodes.WCToggleButton;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

/**
 * Early version of the launcher written using the JavaFX API.
 * This class remains only as a record and its use is not
 * recommended.
 * 
 * @author Daniel D. Scalzi
 */
@Deprecated
public class MainLauncher extends Application{
	
	public static void main(String[] args){
		launch(args);
	}
	
	WCEngine engine;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//Startup log
		TextArea logArea = new TextArea();
		LauncherOutputStream log = new LauncherOutputStream(logArea);
		PrintStream ps = new PrintStream(log, true);
		System.setOut(ps);
		System.setErr(ps);
		
		for(int i=0; i<100; ++i) System.out.println("Launcher loading..");
		
		//Final
		final Image seal = new Image(getClass().getResource("../resources/images/WesterosCraft.png").toExternalForm());
		//final Font font = Font.loadFont(getClass().getResource("resources/fonts/ringbearer.ttf").toExternalForm(), 60);
		
		//Wide-scope
		HBox windowFrameAdapter;
		ToggleGroup tg;
		
		/* This acts as the mock scene */
		VBox shell = new VBox();
		{
			shell.setId("shell");
			
			/* Header that contains the three standard buttons */
			windowFrameAdapter = new HBox(new DiminishButton(25,40), new RestoreButton(25,40), new CloseButton(25,40));
			{
				windowFrameAdapter.setId("window-frame-adapter");
				windowFrameAdapter.setMinHeight(25);
				windowFrameAdapter.setPrefWidth(900);
				windowFrameAdapter.setAlignment(Pos.TOP_RIGHT);
			}
			
			/* Entire scene excluding the Standard Menu */
			VBox innerShell = new VBox();
			{
				innerShell.setId("inner-shell");
				VBox.setVgrow(innerShell, Priority.ALWAYS);
			
				/* Launcher Header */
				VBox headerContainer = new VBox();
				{
					headerContainer.setId("header-container");
					headerContainer.setAlignment(Pos.CENTER);
					
					StackPane headerMainLine = new StackPane();
					{
						headerMainLine.setId("header-main-line");
						headerMainLine.setAlignment(Pos.CENTER);
						
						Pane sealPane = new Pane();{
							sealPane.setId("seal-pane");
							ImageView sealIV = new ImageView();
							{
								sealIV.setId("seal-imageview");
								sealIV.setImage(seal);
								sealIV.setPreserveRatio(true);
								sealIV.setFitHeight(75);
							}
							sealPane.getChildren().add(sealIV);
						}
						
						HBox headerTextContainer = new HBox();
						{
							headerTextContainer.setId("header-text-container");
							headerTextContainer.setAlignment(Pos.CENTER);
							
							Text headerText = new Text("WesterosCraft");
							{
								headerText.setId("header-text");
							}
							
							headerTextContainer.getChildren().add(headerText);
						}
						
						headerMainLine.getChildren().addAll(sealPane, headerTextContainer);
					}
					
					StackPane headerTrimPane = new StackPane();
					{
						headerTrimPane.setId("header-trim-pane");
						//TODO Maybe move rectangle styles to stylesheet.
						Rectangle bar = new Rectangle(900, 5, Color.web("a02d2a"));
						headerTrimPane.getChildren().add(bar);
					}
					
					headerContainer.getChildren().addAll(headerMainLine, headerTrimPane);
				}
				
				/* Launcher Body */
				StackPane bodyContainer = new StackPane();
				{
					bodyContainer.setId("body-container");
					
					HBox contentContainer = new HBox();
					{
						contentContainer.setId("content-container");
						contentContainer.setAlignment(Pos.TOP_RIGHT);
						
						VBox leftContainer = new VBox();
						{
							leftContainer.setId("left-container");
							leftContainer.setMinWidth(275);
							leftContainer.setAlignment(Pos.TOP_CENTER);
							
							StackPane welcomeContainer = new StackPane();
							{
								welcomeContainer.setId("welcome-container");
								
								Text welcomeText = new Text("Welcome to WesterosCraft!");
								{
									welcomeText.setId("welcome-text");
								}
								welcomeContainer.getChildren().add(welcomeText);
							}
							
							HBox loginArea = new HBox();
							{
								loginArea.setId("login-area");
								loginArea.setAlignment(Pos.CENTER);
								
								VBox leftLoginContainer = new VBox();
								{
									leftLoginContainer.setId("left-login-container");
									leftLoginContainer.setAlignment(Pos.TOP_CENTER);
									
									StackPane avatarContainer = new StackPane();
									{
										avatarContainer.setId("avatar-container");
										Image avatar = new Image("https://minotar.net/avatar/TheAventine.png");
										ImageView avatarIV = new ImageView(avatar);
										{
											avatarIV.setPreserveRatio(true);
											avatarIV.setFitHeight(85);
										}
										avatarContainer.getChildren().add(avatarIV);
									}
									leftLoginContainer.getChildren().add(avatarContainer);
								}
								
								VBox rightLoginContainer = new VBox();
								{
									rightLoginContainer.setId("right-login-container");
									
									Text usernameText = new Text("Username");{
										usernameText.setId("username-text");
									}
									
									TextField usernameTextField = new TextField();
									{
										usernameTextField.setId("username-textfield");
									}
									
									Text passwordText = new Text("Password");{
										passwordText.setId("password-text");
									}
									
									PasswordField passwordTextField = new PasswordField();
									{
										passwordTextField.setId("password-textfield");
									}
									
									StackPane loginContainer = new StackPane();
									{
										loginContainer.setId("login-container");
										loginContainer.setAlignment(Pos.CENTER_RIGHT);
										
										Button loginButton = new Button("Login");
										{
											loginButton.setId("login-button");
										}
										
										loginContainer.getChildren().add(loginButton);
									}
									
									rightLoginContainer.getChildren().addAll(usernameText, usernameTextField, passwordText, passwordTextField, loginContainer);
									
								}
								
								loginArea.getChildren().addAll(leftLoginContainer, rightLoginContainer);
							}
							
							VBox serverSelectContainer = new VBox();
							{
								serverSelectContainer.setId("server-select-container");
								serverSelectContainer.setAlignment(Pos.CENTER);
								
								ToggleGroup serverTG = new ToggleGroup();
								
								ToggleButton mainServer = new ToggleButton("main server 99/100");
								{
									mainServer.setId("main-server-button");
									mainServer.getStyleClass().add("server-select-btn");
									mainServer.setToggleGroup(serverTG);
									mainServer.setSelected(true);
								}
								ToggleButton testServer = new ToggleButton("test server 3/100");
								{
									testServer.setId("test-server-button");
									testServer.getStyleClass().add("server-select-btn");
									testServer.setToggleGroup(serverTG);
								}
								
								serverSelectContainer.getChildren().addAll(mainServer, testServer);
							}
							
							HBox buttonDock = new HBox();
							{
								buttonDock.setId("button-dock");
								buttonDock.setAlignment(Pos.CENTER);
								
								Button settingsButton = new Button("Settings");
								{
									settingsButton.setId("settings-button");
								}
								
								Button updateButton = new Button("Update");
								{
									updateButton.setId("update-button");
								}
								
								buttonDock.getChildren().addAll(settingsButton, updateButton);
							}
							
							StackPane launchButtonContainer = new StackPane();
							{
								launchButtonContainer.setId("launch-button-container");
								Button launchButton = new Button("LAUNCH");
								{
									launchButton.setId("launch-button");
								}
								launchButtonContainer.getChildren().add(launchButton);
							}
							
							leftContainer.getChildren().addAll(welcomeContainer, loginArea, serverSelectContainer, buttonDock, launchButtonContainer);
						}
						HBox.setHgrow(leftContainer, Priority.SOMETIMES);
						
						VBox rightContainer = new VBox();
						{
							rightContainer.setId("right-container");
							
							rightContainer.setAlignment(Pos.TOP_LEFT);
							
							ProgressBar progressBar;
							StackPane progressBarPane = new StackPane();
							{
								progressBarPane.setId("progress-bar-pane");
								progressBarPane.setAlignment(Pos.CENTER_RIGHT);
								
								progressBar = new ProgressBar();
								{
									progressBar.setId("progressbar-main");
									progressBar.prefWidthProperty().bind(progressBarPane.widthProperty());
								}
								
								progressBarPane.getChildren().add(progressBar);
							}
							
							StackPane browserPane = new StackPane();
							{
								browserPane.setId("browser-pane");
								
								StackPane browserContainer = new StackPane();
								{
									browserContainer.setId("browser_container");
									
									final WebView browser = new WebView();
									{
										browser.setId("webview-main");
										browser.setZoom(.53);
									}
									
									engine = new WCEngine(browser, progressBar, browserPane);
									browserContainer.getChildren().addAll(browser);
								}
								browserPane.getChildren().add(browserContainer);
							}
							
							StackPane logPane = new StackPane();
							{
								logPane.setId("log-pane");
								
								StackPane logContainer = new StackPane();
								{
									logContainer.setId("log-container");
									
									logArea.setWrapText(true);
									logArea.setEditable(false);
									logArea.prefWidthProperty().bind(logPane.widthProperty());
									
									logContainer.getChildren().addAll(logArea);
								}
								logPane.getChildren().add(logContainer);
								logPane.setVisible(false);
								logPane.setManaged(false);
							}
							
							ToolBar tb = new ToolBar();
							{
								tb.setId("right-main-toolbar");
								Map<String, Pair<String, EventHandler<ActionEvent>>> names = new LinkedHashMap<String, Pair<String, EventHandler<ActionEvent>>>();
								names.put("news", new Pair<String, EventHandler<ActionEvent>>("tbbtn-news", e -> {
									if(!((WCToggleButton)e.getSource()).isCurrent())
										logPane.setVisible(false);
										logPane.setManaged(false);
										browserPane.setVisible(true);
										browserPane.setManaged(true);
										engine.loadPage("http://westeroscraft.com/servernews");
								}));
								names.put("map", new Pair<String, EventHandler<ActionEvent>>("tbbtn-map", e -> {
									if(!((WCToggleButton)e.getSource()).isCurrent())
										logPane.setVisible(false);
										logPane.setManaged(false);
										browserPane.setVisible(true);
										browserPane.setManaged(true);
										engine.loadPage("http://westeroscraft.com/map");
								}));
								names.put("mods", new Pair<String, EventHandler<ActionEvent>>("tbbtn-mods", e -> {
									if(!((WCToggleButton)e.getSource()).isCurrent()){
										//Do something
									}
								}));
								names.put("faq", new Pair<String, EventHandler<ActionEvent>>("tbbtn-faq", e -> {
									if(!((WCToggleButton)e.getSource()).isCurrent()){
										logPane.setVisible(false);
										logPane.setManaged(false);
										browserPane.setVisible(true);
										browserPane.setManaged(true);
										engine.loadPage("http://westeroscraft.com/guide");
									}
								}));
								names.put("log", new Pair<String, EventHandler<ActionEvent>>("tbbtn-log", e -> {
									if(!((WCToggleButton)e.getSource()).isCurrent()){
										browserPane.setVisible(false);
										browserPane.setManaged(false);
										logPane.setVisible(true);
										logPane.setManaged(true);
									}
								}));
								names.put("refresh", new Pair<String, EventHandler<ActionEvent>>("tbbtn-refresh", e -> {
									if(!((WCToggleButton)e.getSource()).isCurrent()){
										//Do something
									}
								}));
								
								
								tg = new ToggleGroup();
								List<WCToggleButton> tbBtns = new ArrayList<WCToggleButton>();
								
								for(Map.Entry<String, Pair<String, EventHandler<ActionEvent>>> entry : names.entrySet()){
									WCToggleButton b = new WCToggleButton(tg, entry.getKey());
									b.setId(entry.getValue().getKey());
									b.getStyleClass().add("tbbtn");
									b.setOnAction(entry.getValue().getValue());
									b.setPrefHeight(35);
									b.setPrefWidth(90);
									tbBtns.add(b);
								}
								
								if(tbBtns.size() > 0)
									WCToggleButton.setFocus(tbBtns.get(0));
								
								tb.getItems().addAll(tbBtns);
							}
							
							rightContainer.getChildren().addAll(tb, progressBarPane, browserPane, logPane);
						}
						
						contentContainer.getChildren().addAll(leftContainer, rightContainer);
					}
					
					bodyContainer.getChildren().add(contentContainer);
				}
				
				innerShell.getChildren().addAll(headerContainer, bodyContainer);
			}
			
			shell.getChildren().addAll(windowFrameAdapter, innerShell);
			
		}
		
		BorderlessScene scene = new BorderlessScene(primaryStage, shell);
		scene.setMoveControl(windowFrameAdapter);
		scene.getStylesheets().add(getClass().getResource("launcherlight.css").toExternalForm());
		
		engine.loadPage("http://westeroscraft.com/servernews");
		
		primaryStage.getIcons().add(seal);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setHeight(500);
		primaryStage.setWidth(925);
		primaryStage.setMinWidth(575);
		primaryStage.show();
	}

}
