package fr.univ_lyon1.info.m1.microblog.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.MessageData;
import fr.univ_lyon1.info.m1.microblog.model.User;
import fr.univ_lyon1.info.m1.microblog.model.Y;
import fr.univ_lyon1.info.m1.microblog.model.scoring.ScoringStrategies;
import fr.univ_lyon1.info.m1.microblog.view.JfxView;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


/**
 * Main class of the Controller (GUI) of the application.
 */
public class Controller {
    private final Y model;
    private final List<JfxView> views = new ArrayList<>();

    /**
     * Main Controller of the application.
     */
    public Controller(final Y model, final Stage stage) {
        this.model = model;
        // la vue par défaut
        this.addView(stage, 600, 600);
    }

    /**
     * Get the model (for ControllerTest only).
     */
    public Y getModel() {
        return model;
    }

    /**
     * Get the list of the views (for ControllerTest only).
     */
    public List<JfxView> getViews() {
        return views;
    }

    /**
     * Add view.
     */
    public void addView(final Stage stage, final int width, final int height) {
        this.views.add(new JfxView(stage, width, height));
        views.get(views.size() - 1).setController(this);
    }

    /**
     * Init view.
     */
    public void initView() {
        for (JfxView view : views) {
            view.createUserSelectionPane(model.getUsers());
        }
        // parce qu'on a déjà chargé des données, on met à jour
        if (model.getCurrentUser() != null) {
            applyStrategy(model.getCurrentStrategyUser().get(model.getCurrentUser()));
        }
        refreshView(model.getUsers());
    }

    /**
     * Handle bookmarks button.
     */
    public void handlerBookmarkButton(final MessageData d,
                                    final Message m,
                                    final Button bookButton,
                                    final User u) {
        model.bookmarkMessage(d, m, u);
        bookButton.setText(bookmarkText(d));
        refreshView(model.getUsers());
    }

    /**
     * Set the bookmark text.
     */
    public String bookmarkText(final MessageData d) {
        boolean bookmarked = d.isBookmarked();
        if (bookmarked) {
            return "⭐";
        } else {
            return "Click to bookmark";
        }
    }

    /**
     * Handle delete button.
     */
    public void deleteMessage(final Message m) {
        model.deleteMessage(m);
        refreshView(model.getUsers());
    }


    /**
     * Publish a message.
     */
    public void publish(final TextArea t, final User u) {
        int size = model.getUserMessageMap(u).size();
        String messageContent = t.getText();

        model.addMessage(new Message(size, messageContent), u);
        refreshView(model.getUsers());
    }

    /**
     * Set the Scoring strategy.
     */
    public void setScoringStrategy(final ScoringStrategies s) {
        if (s == null) {
            model.resetToDefaultScoring();
        } else {
            model.removeScoringSystem();
            model.setScoringSystem(s);
        }
        model.sortMessages();
        refreshView(model.getUsers());
    }

    /**
     * Filter the messages that can be seen by a user.
     */
    public Map<Message, MessageData> filterMessages(final Map<Message, MessageData> userMessages) {
        Map<Message, MessageData> filteredMessages = new LinkedHashMap<>();
        for (Map.Entry<Message, MessageData> entry : userMessages.entrySet()) {
            Message message = entry.getKey();
            MessageData messageData = entry.getValue();
            if (model.canBeSeen(messageData)) {
                filteredMessages.put(message, messageData);
            }
        }
        return filteredMessages;
    }


    /**
     * Refresh the view.
     */
    public void refreshView(final Collection<User> usersList) {
        User currentUser = model.getCurrentUser();
        if (currentUser != null) {
            for (JfxView view : views) {
                view.setupUserFeedHeader(currentUser.getUserTag());
                Map<Message, MessageData> userMessages = model.getUserMessageMap(currentUser);
                Map<Message, MessageData> filteredMessages = filterMessages(userMessages);
                for (Map.Entry<Message, MessageData> entry : filteredMessages.entrySet()) {
                    Message m = entry.getKey();
                    MessageData d = entry.getValue();
                    view.addMessageToFeed(m, d, currentUser);
                }
                view.showUserFeed(currentUser);
            }
        } else {
            for (JfxView view : views) {
                view.createUserSelectionPane(usersList);
            }
        }
    }

    /**
     * Select the current user.
     */
    public void selectUser(final User user) {
        model.setCurrentUser(user);
        applyStrategy(model.getCurrentStrategyUser().get(model.getCurrentUser()));
        refreshView(model.getUsers());
    }

    /**
     * Back to the users List.
     */
    public void backToUsersList() {
        model.setCurrentUser(null);
        refreshView(model.getUsers());
    }

    /**
     * Apply the strategy of sorting/scoring selected.
     */
    public void applyStrategy(final String selectedOption) {
        switch (selectedOption) {
            case "Par ordre chronologique":
                model.applyChronologicalStrategy();
                break;

            case "Messages les plus pertinents récents":
                model.applyRecentRelevantMessagesStrategy();
                break;

            case "Messages les plus pertinents":
                model.applyRelevantMessagesStrategy();
                break;

            default: break;
        }
        // attention : replace ça veut dire qu'il faut que l'user existe
        model.getCurrentStrategyUser().replace(model.getCurrentUser(), selectedOption);
        model.sortMessages();
        refreshView(model.getUsers());
    }
}
