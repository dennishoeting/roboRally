package prototyp.client.util;

import com.smartgwt.client.widgets.Label;

public class StandardLabel extends Label {
	public StandardLabel() {
		super();
	}

	public StandardLabel(String content) {
		super(content);
		this.setStyleName("standardLabel");
		setAutoFit(true);
		setWrap(false);
	}
}
