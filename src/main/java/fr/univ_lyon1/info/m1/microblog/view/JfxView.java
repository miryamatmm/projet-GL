package fr.univ_lyon1.info.m1.microblog.view;

import java.text.SimpleDateFormat;

import java.util.Collection;

import fr.univ_lyon1.info.m1.microblog.controller.Controller;
import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.MessageData;
import fr.univ_lyon1.info.m1.microblog.model.User;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

import javafx.stage.Stage;
import javafx.geometry.Pos;

/**
 * Main class of the View (GUI) of the application.
 */
public class JfxView {
    private Controller controller;
    private final VBox root;
    private final VBox userMsgBox;

    /**
     * Main view of the application.
     */
    public JfxView(final Stage stage,
                   final int width, final int height) {
        stage.setTitle("Y Microblogging");

        root = new VBox(10);
        userMsgBox = new VBox(10);

        // Everything's ready: add it to the scene and display it
        final Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Set the Controller for the view.
     */
    public void setController(final Controller controller) {
        this.controller = controller;
    }

    /**
     * Create the main view with a list of users, allowing the selection of a user
     * to view their feed.
     */

    public void  createUserSelectionPane(final Collection<User> usersList) {
        root.getChildren().clear();

        // création d'un VBox pour contenir le titre et la liste des utilisateurs
        VBox userSelectionBox = new VBox(15);
        userSelectionBox.setAlignment(Pos.CENTER);
        userSelectionBox.setPrefWidth(Double.MAX_VALUE);
        userSelectionBox.setMaxWidth(Double.MAX_VALUE);

        Label chooseUserLabel = new Label("Login");
        chooseUserLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        chooseUserLabel.setAlignment(Pos.CENTER);
        userSelectionBox.getChildren().add(chooseUserLabel);

        // on affiche tous les users possibles
        for (User u : usersList) {
            Button userButton = new Button("@" + u.getUserTag());

            userButton.setStyle(USER_BUTTON_STYLE);

            userButton.setOnAction(e -> {
                controller.selectUser(u);
            });

            userSelectionBox.getChildren().add(userButton);
        }

        // on crée un ScrollPane et on ajoute la VBox dedans
        ScrollPane userSelectionPane = new ScrollPane(userSelectionBox);
        userSelectionPane.setFitToWidth(true);
        userSelectionPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        userSelectionPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        root.getChildren().add(userSelectionPane);
    }

    /**
     * Sets up the header and controls for the selected user's feed.
     * This includes the logout button, the feed title, and the sorting preferences selector.
     */
    public void setupUserFeedHeader(final String userTag) {
        userMsgBox.getChildren().clear();

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setSpacing(20);

        Label userHeader = new Label("Feed of @" + userTag);
        userHeader.setStyle(HEADER_STYLE);

        Button backButton = new Button("Logout");
        backButton.setStyle(BACKBUTTON_STYLE);
        backButton.setOnAction(e -> {
            controller.backToUsersList();
        });

        HBox strategyBox = new HBox();
        strategyBox.setAlignment(Pos.CENTER);
        strategyBox.setSpacing(20);

        Pane strategySelector = createScoringStrategySelector();

        headerBox.getChildren().add(userHeader);
        strategyBox.getChildren().add(strategySelector);
        userMsgBox.getChildren().addAll(backButton, headerBox, strategyBox);
    }

    /**
     * Adds a single message to the currently displayed user's feed.
     *
     * @param m The message to display.
     * @param d The metadata associated with the message (e.g., score, creation date).
     * @param u The user who created the message.
     */
    public void addMessageToFeed(final Message m, final MessageData d, final User u) {
        userMsgBox.setAlignment(Pos.CENTER_LEFT);
        userMsgBox.setFillWidth(true);

        VBox msgBox = createMessageWidget(m, d, u);
        userMsgBox.getChildren().add(msgBox);
    }

    /**
     * Displays the complete feed for the selected user,
     * including the input field for creating new messages.
     *
     * @param u The user whose feed is being displayed.
     */
    public void showUserFeed(final User u) {
        Pane textBox = createInputWidget(u);
        userMsgBox.getChildren().add(textBox);

        ScrollPane scrollPane = new ScrollPane(userMsgBox);
        scrollPane.setFitToWidth(true);

        StackPane centeredPane = new StackPane();
        centeredPane.getChildren().add(scrollPane);
        StackPane.setAlignment(scrollPane, Pos.CENTER);

        root.getChildren().clear();
        root.getChildren().add(centeredPane);
    }

    /**
     * Create the widget where appears messages.
    */
    public VBox createMessageWidget(
            final Message m,
            final MessageData d,
            final User u) {
        VBox msgBox = new VBox();

        String bookmarkText = controller.bookmarkText(d);
        Button bookButton = new Button(bookmarkText);
        bookButton.setOnAction(e -> { 
            controller.handlerBookmarkButton(d, m, bookButton, u);
        });

        Button deleteButton = new Button("X");
        deleteButton.setOnAction(e -> {
            controller.deleteMessage(m); 
        });

        HBox buttonBox = new HBox();

        // pour bien l'aligner 
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(bookButton, deleteButton);

        msgBox.getChildren().add(buttonBox);

        final Label label = new Label(m.getContent());
        msgBox.getChildren().add(label);

        final Label score = new Label("Score: " + d.getScore());
        score.setTextFill(Color.GRAY);
        msgBox.getChildren().add(score);

        final Label userTag = new Label("@" + d.getUser().getUserTag());
        userTag.setTextFill(Color.DEEPPINK);
        msgBox.getChildren().add(userTag);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'at' HH'h'mm");
        final Label date = new Label("Creation date: " + sdf.format(d.getDate()));
        date.setTextFill(Color.HOTPINK);
        msgBox.getChildren().add(date);

        msgBox.setStyle(MSG_STYLE);
        return msgBox;
    }

    /**
     * Create the ComboBox to select the strategy.
     */
    public Pane createScoringStrategySelector() {
        HBox strategyBox = new HBox(10);
        strategyBox.setStyle(STRATEGY_BOX_STYLE);
        strategyBox.setAlignment(Pos.CENTER);

        ComboBox<String> c = new ComboBox<>();
        c.getItems().addAll(
                "Par ordre chronologique", // DateSorting
                "Messages les plus pertinents récents", // ScoreSorting + DS + MS  + BS
                "Messages les plus pertinents" // ScoreSorting + MalusScoring + BookmarkScoring
        );

        c.setPromptText("Préférences de tri");
        c.setStyle(COMBOBOX_STYLE);

        c.setOnAction(e -> {
            String selectedOption = c.getValue();
            if (selectedOption != null) {
                controller.applyStrategy(selectedOption);
            }
        });

        strategyBox.getChildren().add(c);
        return strategyBox;
    }


    /**
     * Create input widget.
    */
    private Pane createInputWidget(final User u) {
        final Pane input = new HBox();
        TextArea t = new TextArea();
        t.setPrefWidth(400); // minimum
        t.setMaxWidth(500);  // maximum
        HBox.setHgrow(t, Priority.ALWAYS); // si j'ai la place
        t.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && e.isControlDown()) {
                controller.publish(t, u);
                t.clear();
            }
        });
        Button s = new Button("Publish");
        s.setOnAction(e -> {
            controller.publish(t, u);
            t.clear();
        });

        input.getChildren().addAll(t, s);
        return input;
    }

    // un peu de style
    static final String MSG_STYLE = "-fx-border-color: #FFC1D9; "
            + "-fx-border-width: 1.5px; "
            + "-fx-border-radius: 15px; "
            + "-fx-background-radius: 15px; "
            + "-fx-padding: 12px; "
            + "-fx-margin: 8px; "
            + "-fx-font-size: 14px; "
            + "-fx-text-fill: #B56576;";

    static final String USER_BUTTON_STYLE = "-fx-font-size: 16px; "
            + "-fx-padding: 15px 20px; "
            + "-fx-background-color: #FCE4EC; "
            + "-fx-border-color: #FFC1D9; "
            + "-fx-border-width: 2px; "
            + "-fx-border-radius: 8px; "
            + "-fx-background-radius: 8px; "
            + "-fx-text-fill: #9A1750; "
            + "-fx-pref-width: 200px; "
            + "-fx-pref-height: 40px;"
            + "-fx-effect: dropshadow(three-pass-box, rgba(255,182,193,0.5), 10, 0.3, 0, 4);";

    static final String HEADER_STYLE = "-fx-font-size: 20px; "
            + "-fx-padding: 12px 20px; "
            + "-fx-background-color: #FCE4EC; "
            + "-fx-text-fill: #9A1750; "
            + "-fx-font-family: 'Georgia', serif; "
            + "-fx-border-color: #FFC1D9; "
            + "-fx-border-width: 1.5px; "
            + "-fx-border-radius: 10px;";

    static final String BACKBUTTON_STYLE = "-fx-font-size: 16px; "
            + "-fx-padding: 10px 15px; "
            + "-fx-background-color: #FCE4EC; "
            + "-fx-border-color: #FFC1D9; "
            + "-fx-border-width: 1.5px; "
            + "-fx-border-radius: 6px; "
            + "-fx-background-radius: 6px; "
            + "-fx-text-fill: #9A1750; "
            + "-fx-font-family: 'Georgia', serif; ";


    static final String STRATEGY_BOX_STYLE = "-fx-border-color: #FFC1D9; "
            + "-fx-border-width: 1px; "
            + "-fx-border-radius: 10px; "
            + "-fx-background-color: #FCE4EC; "
            + "-fx-padding: 5px; "
            + "-fx-spacing: 0px; ";

    static final String COMBOBOX_STYLE = "-fx-font-size: 14px; "
            + "-fx-padding: 4px 5px; "
            + "-fx-background-color: #FFFFFF; "
            + "-fx-border-color: #FFC1D9; "
            + "-fx-border-width: 1.5px; "
            + "-fx-border-radius: 8px; "
            + "-fx-background-radius: 8px; "
            + "-fx-text-fill: #B56576; "
            + "-fx-prompt-text-fill: #B56576;";

}
