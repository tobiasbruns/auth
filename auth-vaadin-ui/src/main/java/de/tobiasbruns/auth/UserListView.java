package de.tobiasbruns.auth;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Created on 22.09.2016.
 * 
 * @author Tobias Bruns
 */
@SpringView(name = UserListView.VIEW_NAME)
public class UserListView extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "";

	@PostConstruct
	void init() {
		addComponent(new Label("This is the default view"));
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// the view is constructed in the init() method()
	}
}
