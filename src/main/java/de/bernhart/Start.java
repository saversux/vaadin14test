package de.bernhart;

import com.vaadin.cdi.annotation.UIScoped;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.router.Route;
import de.bernhart.events.ChatEvent;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * The main view contains a button and a click listener.
 */
@UIScoped
@Push
@Route("")
@PageTitle("Juliens Home")
@PreserveOnRefresh
@PWA(name = "Juliens Home", shortName = "Juliens Home")
public class Start extends VerticalLayout implements RouterLayout {
    private TextArea chatArea;
    private TextField typeArea;

    @Inject
    private Event<ChatEvent> chatEvent;

    public Start() {
        this.setSizeFull();

        chatArea = new TextArea("Julien Chat");
        chatArea.setHeight("50%");
        chatArea.setWidth("50%");
        chatArea.setReadOnly(true);

        typeArea = new TextField();
        typeArea.setWidth("50%");

        Button button = new Button("Click me", event -> sendMessage());
        button.addClickShortcut(Key.ENTER);
        setHorizontalComponentAlignment(Alignment.CENTER, button, chatArea, typeArea);
        add(chatArea, typeArea, button);
    }

    private void sendMessage() {
        if (!typeArea.isEmpty()) {
            chatEvent.fire(new ChatEvent(typeArea.getValue()));
            typeArea.clear();
        }
    }

    private void receiveMessage(@Observes ChatEvent chatEvent) {
        UI.getCurrent().access(() -> chatArea.setValue(chatArea.getValue() + "\n" + chatEvent.getMessage()));
    }
}
