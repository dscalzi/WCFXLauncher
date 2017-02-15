package com.westeroscraft.nodes;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.control.skin.ContextMenuContent.MenuItemContainer;
import com.sun.javafx.util.WeakReferenceQueue;
import com.westeroscraft.logging.LoggerUtil;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Worker.State;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

/**
 * This class is a wrapper for all of the components needed
 * to make the web browser on the front of the launcher functional.
 * 
 * Unfortunately some of JFX's WebView functionality is locked in
 * private API so this class makes use of some unsafe hack methods
 * and reflection to gain access to private fields.
 * 
 * @author Daniel D. Scalzi
 *
 */
@SuppressWarnings("restriction")
public class WCEngine {

	private final WebEngine engine;
	private final WebView view;
	private final ProgressBar progressBar;
	private final Pane container;
	
	private String javascript;
	private URI hrefCache;
	
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
		this.bindContextMenu();
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
			if (newState == State.RUNNING)
				view.getScene().lookup("#browser_container").setStyle("-fx-border-width: 0 0 3 3;");
			if (newState == State.SUCCEEDED)		
				view.getScene().lookup("#browser_container").setStyle("-fx-border-width: 3 0 3 3;");
	    });
	}
	
	/*
	 * Use our custom content menu instead.
	 */
	private void bindContextMenu(){
		//Listener for right clicks on links.
		engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			if(newState == State.SUCCEEDED){
				EventListener listener = ev -> {
					String domEventType = ev.getType();
					if(domEventType.equals("contextmenu")) {
						Element el = (Element)ev.getTarget();
						if(!el.getNodeName().equalsIgnoreCase("a"))
							if(el.getParentNode().getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
								el = (Element)el.getParentNode();
						URI loc = URI.create(engine.getLocation());
						String domain = null;
						try {
							domain = loc.toURL().getProtocol() + "://" + loc.getHost();
						} catch (MalformedURLException e) {
							//Shouldn't happen..
							LoggerUtil.getLogger("Launcher").severe("Current url is malformed, unable to proccess it.");
							e.printStackTrace();
						}
						String uri = el.getAttribute("href");
						hrefCache = URI.create((uri.startsWith("/") ? domain : (uri.startsWith("#") ? loc.toString() : "")) +  uri);
					} 
	            };
	
	            Document doc = view.getEngine().getDocument();
	            NodeList nodeList = doc.getElementsByTagName("a");
	            for (int i = 0; i < nodeList.getLength(); i++)
	                ((EventTarget) nodeList.item(i)).addEventListener("contextmenu", listener, false);
			}
		});
		view.setOnContextMenuRequested(cme -> {
			try {
				getPopupWindow();
			} catch (Throwable t){
				t.printStackTrace();
			}
		});
	}
	
	/**
	 * Load our custom JavaScript file once the page has finished loading. This allows
	 * us to modify the styles of loaded pages to have them fit nicer onto the smaller
	 * display space provided by the launcher.
	 */
	private void setupJavaScript(){
		engine.setJavaScriptEnabled(true);
		try(InputStream is = getClass().getClassLoader().getResourceAsStream("com/westeroscraft/resources/scripts/web.js")){
			javascript = loadLocalScript(is);
			engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
				if (newState == State.SUCCEEDED) {
					engine.executeScript(javascript);
				}
			});
		} catch (IOException e) {
			LoggerUtil.getLogger("Launcher").severe("Unable to locate javascript resource for web engine.");
			e.printStackTrace();
		}
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
			LoggerUtil.getLogger("Launcher").severe("Unable to load javascript file from InputStream.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Hack to modify the context menu of a WebView. Uses reflection
	 * and private API methods so this is not API-safe and may break
	 * on different java versions.
	 * 
	 * Works as of JDK 8 update 121.
	 * 
	 * @return Popup window object that is used as the context menu.
	 */
	@SuppressWarnings("unchecked")
	private PopupWindow getPopupWindow() {
		WeakReferenceQueue<Window> wQ = new WeakReferenceQueue<Window>();
		Iterator<Window> windows;
		try {
			Field f = Window.class.getDeclaredField("windowQueue");
			f.setAccessible(true);
			
			windows = (Iterator<Window>)((com.sun.javafx.util.WeakReferenceQueue<Window>)f.get(wQ)).iterator();
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
			LoggerUtil.getLogger("Launcher").severe("Unable to access field windowQueue as of java version " + 
				System.getProperty("java.version") + ". Report this to the developers:");
			e1.printStackTrace();
			return null;
		}

	    while (windows.hasNext()) {
	        final Window window = windows.next();

	        if (window instanceof ContextMenu) {
	            if(window.getScene()!=null && window.getScene().getRoot()!=null){ 
	                Parent root = window.getScene().getRoot();

	                // access to context menu content
	                if(root.getChildrenUnmodifiable().size()>0){
	                    Node popup = root.getChildrenUnmodifiable().get(0);
	                    if(popup.lookup(".context-menu")!=null){
	                        Node bridge = popup.lookup(".context-menu");
	                        ContextMenuContent cmc= (ContextMenuContent)((Parent)bridge).getChildrenUnmodifiable().get(0);

	                        VBox itemsContainer = cmc.getItemsContainer();
	                        
	                        for(int i=0; i<itemsContainer.getChildren().size(); ++i){
	                        	if(itemsContainer.getChildren().get(i) instanceof MenuItemContainer){
		                        	MenuItemContainer item = (MenuItemContainer) itemsContainer.getChildren().get(i);
		                            if(item.getItem().getText().equals("Open Link in New Window")){
		                            	itemsContainer.getChildren().remove(i);
		                            	MenuItem newBrowser = new MenuItem("Open Link in Default Browser");
		                            	newBrowser.setOnAction(e -> {
		                            		try {
												Desktop.getDesktop().browse(hrefCache);
											} catch (IOException e1) {
												LoggerUtil.getLogger("Launcher").severe("Unable to open " + hrefCache.toString() +
														" in default browser.");
												
												e1.printStackTrace();
											}
		                            	});
		                            	itemsContainer.getChildren().add(i, cmc.new MenuItemContainer(newBrowser));
		                            }
	                        	}
	                        }
	                        
	                        // adding new item:
	                        MenuItem openPageInBrowser = new MenuItem("Open Page in Default Browser");
	                        openPageInBrowser.setOnAction(e -> {
	                        	try {
									Desktop.getDesktop().browse(URI.create(engine.getLocation()));
								} catch (IOException e1) {
									LoggerUtil.getLogger("Launcher").severe("Unable to open " + engine.getLocation() +
											" in default browser.");
									e1.printStackTrace();
								}
	                        });
	                        
	                        cmc.getItemsContainer().getChildren().addAll(getPreparedSeparator(), cmc.new MenuItemContainer(openPageInBrowser));
	                        
	                        return (PopupWindow)window;
	                    }
	                }
	            }
	            return null;
	        }
	    }
	    return null;
	}
	
	/**
	 * Prepare a separator to be inserted into private api
	 * context menu. This preparation method was taken from
	 * {@link com.sun.javafx.scene.control.skin.ContextMenuContent#updateVisualItems()}
	 * 
	 * @return
	 */
	private Node getPreparedSeparator(){
		SeparatorMenuItem sep = new SeparatorMenuItem();
        sep.setMnemonicParsing(false);
        Node node = ((CustomMenuItem) sep).getContent();
        node.visibleProperty().bind(sep.visibleProperty());
        node.getProperties().put(MenuItem.class, sep);
        return node;
	}
	
}
