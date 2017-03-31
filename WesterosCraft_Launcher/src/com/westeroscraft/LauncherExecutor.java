package com.westeroscraft;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
//import com.sun.javafx.application.LauncherImpl;
import com.westeroscraft.lib.borderless.BorderlessScene;
import com.westeroscraft.logging.LoggerUtil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LauncherExecutor extends Application{
	
	public static void main(String[] args){
		//LauncherImpl.launchApplication(LauncherExecutor.class, LauncherPreloader.class, args);
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		
		VBox root;
		
		try(InputStream fxmlStream = getClass().getResourceAsStream("LauncherLayout.fxml")){
			root = (VBox) loader.load(fxmlStream);
		}
		
		LoggerUtil.getLogger("Launcher").info("Loading..");
		
		try{
			throw new Exception("This is a test exception");
		} catch(Throwable t){
			LoggerUtil.getLogger("Launcher").log(Level.SEVERE, t.getMessage(), t);
			t.printStackTrace();
		}
		
		BorderlessScene scene = new BorderlessScene(primaryStage, root);
		scene.getStylesheets().add(getClass().getResource("resources/styles/core.css").toExternalForm());
		scene.setMoveControl(root.lookup("#windowFrameAdapter"));
		
		((WebView)root.lookup("#webview_main")).getEngine().load("http://westeroscraft.com/servernews");
		
		primaryStage.getIcons().add(((ImageView)root.lookup("#seal_imageview")).getImage());
		
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setHeight(500);
		primaryStage.setWidth(925);
		primaryStage.setMinWidth(575);
		primaryStage.show();
		System.gc();
	}
	
	/**
	 * Utility method which will retrieve the window pointer of
	 * the specified window.
	 * 
	 * @param window The JFX window to lookup.
	 * @return The window pointer
	 */
	@SuppressWarnings("unused")
	private static Long getWindowPointer(javafx.stage.Window window) {
		Long retval = null;
		try {
			Method m = window.getClass().getMethod("impl_getPeer");
			final Object tkStage = m.invoke( window );
			m = tkStage.getClass().getDeclaredMethod("getPlatformWindow");
			m.setAccessible(true);
			final Object platformWindow = m.invoke( tkStage );
			m = platformWindow.getClass().getMethod("getNativeHandle");
			retval = (Long)m.invoke(platformWindow);
		} catch (Throwable t) {
			System.err.println("Error getting Window Pointer");
		}
		return retval;
	}
	
	public static Path getDataPath(){
		return Paths.get(System.getProperty("user.home"), "AppData", "Roaming", ".westeroscraft");
	}
	
}
