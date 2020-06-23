package com.westeroscraft.nodes;

import java.util.Collections;
import java.util.List;


import java.util.ArrayList;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.PaintConverter;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

@SuppressWarnings("restriction")
public class ToggleSwitch extends Control {
    
    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));

    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    public ToggleSwitch() {
    	this.getStyleClass().add("toggleswitch");
    	StackPane container = new StackPane();
    	
        Rectangle background = new Rectangle(50, 20);
        background.getStyleClass().addAll("toggleswitch", "background");
        background.setArcWidth(5);
        background.setArcHeight(5);
        background.setStrokeWidth(1);

        Rectangle trigger = new Rectangle(25, 20);
        trigger.getStyleClass().addAll("toggleswitch", "trigger");
        trigger.setArcWidth(5);
        trigger.setArcHeight(5);
        trigger.setFill(Color.WHITE);
        trigger.setStroke(Color.LIGHTGRAY);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(2);
        trigger.setEffect(shadow);

        translateAnimation.setNode(trigger);
        fillAnimation.setShape(background);

        container.getChildren().addAll(background, trigger);
        container.setAlignment(Pos.CENTER_LEFT);
        
        getChildren().add(container);

        switchedOnProperty().addListener((obs, oldState, newState) -> {
            boolean isOn = newState.booleanValue();
            translateAnimation.setToX(isOn ? 50 - 25 : 0);
            fillAnimation.setFromValue(isOn ? Color.GRAY : (Color) backgroundFillOnProperty().getValue());
            fillAnimation.setToValue(isOn ? (Color) backgroundFillOnProperty().get() : Color.GRAY);

            animation.play();
        });

        setOnMouseClicked(event -> {
            switchedOn.set(!switchedOn.get());
        });
    }
    
    @Override public Skin<?> createDefaultSkin() {
    	return new ToggleSwitchSkin(this);
    }
    
    
    /*******************************************************************************
     *                                                                             *
     * CSS Property Binding                                                        *
     *                                                                             *
     ******************************************************************************/
    
    private static final List<CssMetaData<? extends Styleable, ?>> cssMetaDataList; 
	static {
		final List<CssMetaData<? extends Styleable, ?>> temp = 
				new ArrayList<CssMetaData<? extends Styleable, ?>>(Control.getClassCssMetaData());
		Collections.addAll(temp, 
				ToggleSwitch.SWITCHED_ON, 
				ToggleSwitch.BACKGROUND_FILL_ON);
		cssMetaDataList = Collections.unmodifiableList(temp);
	}

	public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
		return cssMetaDataList;
	}
	 
	@Override
	public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
		return getClassCssMetaData();
	}
	
    private BooleanProperty switchedOn;
    public final BooleanProperty switchedOnProperty(){
    	if(switchedOn == null){
    		switchedOn = new StyleableBooleanProperty(false) {

				@Override
				public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
					return ToggleSwitch.SWITCHED_ON;
				}

				@Override
				public Object getBean() {
					return ToggleSwitch.this;
				}

				@Override
				public String getName() {
					return "switchedOn";
				}
    			
    		};
    	}
    	return switchedOn;
    }
    private static final CssMetaData<ToggleSwitch, Boolean> SWITCHED_ON =
    		new CssMetaData<ToggleSwitch, Boolean>("-wc-switched-on", StyleConverter.getBooleanConverter(), false) {
    	
    	@Override
    	public boolean isSettable(ToggleSwitch n){
    		return n.switchedOn == null || !n.switchedOn.isBound();
    	}
    	
    	@Override
    	public StyleableProperty<Boolean> getStyleableProperty(ToggleSwitch n){
    		return (StyleableProperty<Boolean>)(WritableValue<Boolean>) n.switchedOn;
    	}
    };
    
    public final void setSwitchedOn(boolean value) {
        switchedOnProperty().set(value);
    }
    
    private ObjectProperty<Paint> backgroundFillOn;
    public final ObjectProperty<Paint> backgroundFillOnProperty() {
    	if(backgroundFillOn == null){
    		backgroundFillOn = new StyleableObjectProperty<Paint>(Color.web("#a02d2a")) {

				@Override
				public CssMetaData<? extends Styleable, Paint> getCssMetaData() {
					return ToggleSwitch.BACKGROUND_FILL_ON;
				}

				@Override
				public Object getBean() {
					return ToggleSwitch.class;
				}

				@Override
				public String getName() {
					return "backgroundFillOn";
				}
    			
    		};
    	}
    	return backgroundFillOn;
    }
    private static final CssMetaData<ToggleSwitch, Paint> BACKGROUND_FILL_ON =
    		new CssMetaData<ToggleSwitch, Paint>("-wc-background-fill-on",
    		PaintConverter.getInstance(), Color.LIGHTGRAY) {
    			
    	@Override
    	public boolean isSettable(ToggleSwitch n){
    		return n.backgroundFillOn == null || !n.backgroundFillOn.isBound();
    	}

		@Override
		public StyleableProperty<Paint> getStyleableProperty(ToggleSwitch n) {
			return (StyleableProperty<Paint>)(WritableValue<Paint>)n.backgroundFillOnProperty();
		}
    };
    
    public final void setBackgroundFillOn(Paint value) {
        backgroundFillOnProperty().set(value);
    }
    
    
}
