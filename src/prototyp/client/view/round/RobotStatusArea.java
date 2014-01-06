package prototyp.client.view.round;

import prototyp.client.presenter.chat.ChatPresenter;
import prototyp.client.util.Button;
import prototyp.client.util.VerticalStack;

import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.SectionStack;

/**
 * Oberklasse für PlayerStatusArea und
 * WatcherStatusArea
 * @author timo
 * @version 1.0
 *
 */
public abstract class RobotStatusArea extends VerticalStack{
	/**
	 * Default
	 */
	public RobotStatusArea() {
		
	}
	/**
	 * Von VerticalStack
	 * @param membersMaging
	 * @param padding
	 */
	public RobotStatusArea(int membersMaging, int padding) {
		super(membersMaging, padding);
	}
	
	/*
	 * Abstrakte Methoden:
	 */		
	public abstract SectionStack getSectionStack();
	
	public abstract VerticalStack getOthersStateArea();
	
	public abstract ListGrid getOthersStateGrid();
	
	public abstract ListGrid getOthersReadyGrid();
	
	public abstract void setChatPresenter(ChatPresenter chatPresenter);
	
	public abstract Button getEndGameButton();
	
	public abstract CheckboxItem getSoundOn();
	
	public abstract Slider getSoundSlider();
}
