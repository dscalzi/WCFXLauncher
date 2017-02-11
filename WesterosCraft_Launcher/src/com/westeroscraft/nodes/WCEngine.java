package com.westeroscraft.nodes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Worker.State;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * This class is a wrapper for all of the components needed
 * to make the web browser on the front of the launcher functional.
 * 
 * @author Daniel D. Scalzi
 *
 */
public class WCEngine {

	private final WebEngine engine;
	private final WebView view;
	private final ProgressBar progressBar;
	private final Pane container;
	
	private String javascript;
	
	/**
	 * Create a new WCEngine.
	 * 
	 * @param view The WebView that will be controlled by this engine.
	 * @param progressBar The ProgressBar for this engine.
	 * @param container Container that holds the WebView, used for visibility property binding.
	 */
	public WCEngine(@NamedArg("webView")WebView view, @NamedArg("progressBar")ProgressBar progressBar, @NamedArg("pane")Pane container){
		this.engine = view.getEngine();
		this.view = view;
		this.progressBar = progressBar;
		this.container = container;
		this.setupJavaScript();
		this.bindProgressBar();
	}
	
	/**
	 * Load a web page with the given url. The page will only load
	 * if it is not already loaded.
	 * 
	 * @param url The url of the web page.
	 */
	public void loadPage(String url){
		if(engine.getLocation() != null && engine.getLocation().equals(url)) return;
		Platform.runLater(() -> engine.load(url));
	}
	
	/**
	 * Get the direct WebEngine object that is wrapped
	 * by this object. 
	 * 
	 * @return The WebEngine wrapped by this object.
	 */
	public WebEngine getEngine(){
		return this.engine;
	}
	
	/**
	 * Method to setup and bind the ProgressBar of this WCEngine. This will ensure
	 * that the ProgressBar is only visible when a page is loading and the WebView is visible.
	 * This will also modify the style of the browser container when the ProgressBar is loading
	 * to have it fit into the design.
	 */
	private void bindProgressBar(){
		BooleanBinding overrideVisibility = Bindings.createBooleanBinding(() -> !(container.isVisible()) ? false : engine.getLoadWorker().getState() == State.RUNNING, container.visibleProperty(), engine.getLoadWorker().stateProperty());
		
		progressBar.visibleProperty().bind(overrideVisibility);
		progressBar.managedProperty().bind(overrideVisibility);
		progressBar.progressProperty().bind(engine.getLoadWorker().progressProperty());
		engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			if(view.getScene() == null) System.out.println("null");
			if (newState == State.RUNNING)
				view.getScene().lookup("#browser_container").setStyle("-fx-border-width: 0 0 3 3;");
			if (newState == State.SUCCEEDED) 
				view.getScene().lookup("#browser_container").setStyle("-fx-border-width: 3 0 3 3;");
	    });
	}
	
	/**
	 * Load our custom JavaScript file once the page has finished loading. This allows
	 * us to modify the stles of loaded pages to have them fit nicer onto the smaller
	 * display space provided by the launcher.
	 */
	private void setupJavaScript(){
		engine.setJavaScriptEnabled(true);
		try(InputStream is = getClass().getClassLoader().getResourceAsStream("com/westeroscraft/resources/scripts/web.js")){
			javascript = loadLocalScript(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			if (newState == State.SUCCEEDED) {
				engine.executeScript(javascript);
			}
		});
	}
	
	/**
	 * Load the JavaScript file into a String in order to be executed by the engine on page load.
	 * 
	 * @param path The path to the script file.
	 * @return A string representation of the JavaScript file.
	 */
	private String loadLocalScript(InputStream inputStream){
		try(ByteArrayOutputStream result = new ByteArrayOutputStream()){
			byte[] buffer = new byte[1024];
			int length;
			while((length = inputStream.read(buffer)) != -1) result.write(buffer, 0, length);
			return result.toString("UTF-8");
		} catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
}
