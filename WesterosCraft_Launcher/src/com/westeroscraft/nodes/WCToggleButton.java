package com.westeroscraft.nodes;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

/**
 * Custom version of a ToggleButton which is used for the main ToolBar of the launcher.
 * This class adds a new state to the ToggleButton to fix CSS styles and ensure that
 * only one toggle button is selected at a time.
 * 
 * In this class, the focused state refers to the button be active and selected. Note that
 * this word is used also by the JavaFX API. Take care in distinguishing the two.
 * 
 * @author Daniel D. Scalzi
 *
 */
public class WCToggleButton extends ToggleButton implements EventHandler<Event>{
	
	private static PseudoClass CURRENT_PSEUDO_CLASS = PseudoClass.getPseudoClass("current");
	private static Map<ToggleGroup, WCToggleButton> focused;
	
	static {
		focused = new HashMap<ToggleGroup, WCToggleButton>();
	}
	
	public enum WCToggleButtonState {
		FOCUSED(),
		IDLE();
	}
	
	private WCToggleButtonState state;
	
	/**
	 * Construct a new WCToggleButton.
	 * 
	 * @param group The ToggleGroup that this button will belong to.
	 */
	public WCToggleButton(@NamedArg("toggleGroup")ToggleGroup group){
		this(group, "");
	}
	
	/**
	 * Construct a new WCToggleButton.
	 * 
	 * @param group The ToggleGroup that this button will belong to.
	 * @param text A text string for its label.
	 */
	public WCToggleButton(ToggleGroup group, String text){
		this(group, text, null);
	}
	
	/**
	 * Construct a new WCToggleButton.
	 * 
	 * @param group The ToggleGroup that this button will belong to.
	 * @param text A text string for its label.
	 * @param graphic The icon for its label.
	 */
	public WCToggleButton(ToggleGroup group, String text, Node graphic){
		super(text, graphic);
		if(!focused.containsKey(group)) focused.put(group, null);
		this.setToggleGroup(group);
		this.addEventHandler(Event.ANY, this);
	}
	
	private void setState(WCToggleButtonState state){
		this.state = state;
	}
	
	/**
	 * Set the focus of the ToggleGroup onto the specified WCToggleButton.
	 * This will ensure that only one button has focus at a time.
	 * 
	 * @param target The WCToggleButton to shift focus to.
	 * @return The result of the operation.
	 */
	public static boolean setFocus(WCToggleButton target){
		ToggleGroup g = target.getToggleGroup();
		
		if(WCToggleButton.focused.get(g) != null){
			WCToggleButton.focused.get(g).setState(WCToggleButtonState.IDLE);
			WCToggleButton.focused.get(g).currentProperty().set(false);
		}
		
		target.setSelected(true);
		WCToggleButton.focused.replace(g, target);
		target.setState(WCToggleButtonState.FOCUSED);
		target.currentProperty().set(true);
		
		return true;
	}
	
	/**
	 * Check to see if this WCToggleButton is currently focused.
	 * 
	 * @return True if this WCToggleButton is focused, else false.
	 */
	public boolean isCurrent() {
	       return current.get();
	}
	
	/**
	 * Boolean property used to determine a WCToggleButton is
	 * currently focused.
	 */
	public BooleanProperty currentProperty() {
		return current;
	}

	private BooleanProperty current = new BooleanPropertyBase(false) {

		@Override 
		protected void invalidated() {
			pseudoClassStateChanged(CURRENT_PSEUDO_CLASS, get());
		}

		@Override 
		public Object getBean() {
			return WCToggleButton.this;
		}

		@Override 
		public String getName() {
			return "magic";
		}
	};
	
	/**
	 * General event handler which redirects to sub-handlers.
	 * This is to ensure that this handler is checked first.
	 */
	@Override
	public void handle(Event event) {
		EventType<? extends Event> t = event.getEventType();
		if(t == ActionEvent.ACTION)
			this.handleActionEvent((ActionEvent)event);
		else if(t == MouseEvent.MOUSE_ENTERED)
			this.handleMouseEnteredEvent((MouseEvent)event);
		
	}
	
	/**
	 * General Handler
	 * 
	 * If the button is focused, consume the event. If not,
	 * set focus to the button.
	 */
	private void handleActionEvent(ActionEvent event){
		if(this.state == WCToggleButtonState.FOCUSED){
			event.consume();
			return;
		}
		WCToggleButton.setFocus(this);
	}
	
	/** 
	 * General Handler
	 * 
	 * Ensure that jfx focus is only on button that's hovered over.
	 */
	private void handleMouseEnteredEvent(MouseEvent event){
		for(Toggle t : this.getToggleGroup().getToggles()){
			if(t instanceof WCToggleButton){
				if(((WCToggleButton) t).isFocused()){
					focused.get(this.getToggleGroup()).requestFocus();
					break;
				}
			}
		}
	}
	
	
}
