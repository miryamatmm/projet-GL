package fr.univ_lyon1.info.m1.microblog.controller;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.MessageData;
import fr.univ_lyon1.info.m1.microblog.model.User;
import fr.univ_lyon1.info.m1.microblog.model.Y;
import fr.univ_lyon1.info.m1.microblog.model.scoring.BookmarkScoring;
import fr.univ_lyon1.info.m1.microblog.model.scoring.ScoringSystem;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Controller test class for the microblogging application.
 */
@DisabledIfEnvironmentVariable(named = "CI", matches = "true")
public class ControllerTest extends ApplicationTest {

    private Controller controller;
    private Stage stage;
    private Y model;
    private User user;
    private Message message;

    /**
     * Initialize the graphical components and the stage for testing.
     *
     * @param stage the stage to display
     */
    @Override
    public void start(final Stage stage) {
        this.stage = stage;

        Platform.runLater(() -> {
            try {
                VBox root = new VBox();
                TextArea textArea = new TextArea();
                Button publishButton = new Button("Publish");
                publishButton.setOnAction(e -> controller.publish(textArea, user));
                root.getChildren().addAll(textArea, publishButton);

                stage.setScene(new Scene(root, 400, 400));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        sleep(); // Attente pour s'assurer que JavaFX a terminé l'initialisation.
    }

    /**
     * Set up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        Platform.runLater(() -> {
            model = new Y();
            controller = new Controller(model, stage);
            model.createUser("miryamlastar");
            user = model.findUserByTag("miryamlastar");
            message = new Message(0, "Test message");
            model.addMessage(message, user);
            model.getCurrentStrategyUser().put(user, null);
        });
        sleep();
    }

    /**
     * Tear down the test environment after each test.
     */
    @AfterEach
    public void tearDown() {
        Platform.runLater(() -> stage.close());
    }

    /**
     * Test the controller constructor.
     */
    @Test
    public void testControllerConstructor() {
        assertThat(controller, is(notNullValue()));
        assertThat(controller.getModel(), is(sameInstance(model)));
        assertThat(stage.getScene(), is(notNullValue()));
        assertThat(stage.getScene().getWidth(), is(equalTo(600.0)));
        assertThat(stage.getScene().getHeight(), is(equalTo(600.0)));
    }

    /**
     * Test getting model from Controller.
     */
    @Test
    public void testGetModel() {
        Platform.runLater(() -> {
            Y modelFromController = controller.getModel();
            assertThat(modelFromController, is(sameInstance(model)));
        });
        sleep();
    }


    /**
     * Test adding a new view to the controller.
     */
    @Test
    public void testAddView() {
        Platform.runLater(() -> {
            controller.addView(stage, 400, 400);
            assertThat(controller.getViews().size(), is(2)); // Default view + new view
        });
        sleep();
    }

    /**
     * Test publishing a message.
     */
    @Test
    public void testPublishMessage() {
        Platform.runLater(() -> {
            TextArea textArea = new TextArea("Hello, world!");
            controller.publish(textArea, user);
            assertThat(model.getUserMessageMap(user).size(), is(2));

            assertThat(model.getUserMessageMap(user).keySet().iterator().next().getContent(),
                    is("Test message"));

            Iterator<Message> iterator = model.getUserMessageMap(user).keySet().iterator();
            iterator.next();
            assertThat(iterator.next().getContent(), is("Hello, world!"));
        });
        sleep();
    }

    /**
     * Test deleting a message.
     */
    @Test
    public void testDeleteMessage() {
        Platform.runLater(() -> {
            model.addMessage(message, user);
            int initialMessageCount = model.getUserMessageMap(user).size();
            controller.deleteMessage(message);
            assertThat(model.getUserMessageMap(user).size(), is(initialMessageCount - 1));
        });
        sleep();
    }

    /**
     * Test handling bookmark button click.
     */
    @Test
    public void testHandlerBookmarkButton() {
        Platform.runLater(() -> {
            user = model.findUserByTag("miryamlastar");
            model.addMessage(message, user);
            MessageData messageData = model.getUserMessageMap(user).get(message);
            Button bookmarkButton = new Button("Click to bookmark");
            controller.handlerBookmarkButton(messageData, message, bookmarkButton, user);
            assertThat(messageData.isBookmarked(), is(true));
            assertThat(bookmarkButton.getText(), is("⭐"));
        });
        sleep();
    }

    /**
     * Test the bookmarkText method for both bookmarked and non-bookmarked messages.
     */
    @Test
    public void testBookmarkText() {
        Platform.runLater(() -> {
            MessageData messageData = model.getUserMessageMap(user).get(message);
            String result = controller.bookmarkText(messageData);
            assertThat(result, is("Click to bookmark"));

            messageData.setBookmarked(true);

            result = controller.bookmarkText(messageData);
            assertThat(result, is("⭐"));
        });
        sleep();
    }


    /**
     * Test setting the scoring strategy.
     */
    @Test
    public void testSetScoringStrategy() {
        Platform.runLater(() -> {
            BookmarkScoring scoringStrategy = new BookmarkScoring();
            controller.setScoringStrategy(scoringStrategy);
            ScoringSystem scoringSystem = model.getScoringSystem();
            assertThat(scoringSystem.getRules(), hasItem(scoringStrategy));
        });
        sleep();
    }

    /**
     * Test setting the scoring strategy with null.
     */
    @Test
    public void testSetScoringStrategyWithNull() {
        Platform.runLater(() -> {
            controller.setScoringStrategy(null);
            assertThat(model.getScoringSystem(), is(notNullValue()));
            assertThat(model.getScoringSystem().getRules().size(), is(greaterThan(0)));
        });
        sleep();
    }

    /**
     * Test apply strategy.
     */
    @Test
    public void testApplyStrategy() {
        Platform.runLater(() -> {
            // given
            model.setCurrentUser(user);

            controller.applyStrategy("Par ordre chronologique");
            assertThat(model.getCurrentStrategyUser().get(model.getCurrentUser()),
                    is("Par ordre chronologique"));

            controller.applyStrategy("Messages les plus pertinents récents");
            assertThat(model.getCurrentStrategyUser().get(model.getCurrentUser()),
                    is("Messages les plus pertinents récents"));

            controller.applyStrategy("Messages les plus pertinents");
            assertThat(model.getCurrentStrategyUser().get(model.getCurrentUser()),
                    is("Messages les plus pertinents"));
        });
        sleep();
    }

    /**
     * Sleep method for synchronization in tests.
     */
    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
