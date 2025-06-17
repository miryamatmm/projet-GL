package fr.univ_lyon1.info.m1.microblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import fr.univ_lyon1.info.m1.microblog.dto.MessageDTO;
import fr.univ_lyon1.info.m1.microblog.dto.UserDTO;
import fr.univ_lyon1.info.m1.microblog.model.scoring.BookmarkScoring;
import fr.univ_lyon1.info.m1.microblog.model.scoring.DateScoring;
import fr.univ_lyon1.info.m1.microblog.model.scoring.MalusScoring;
import fr.univ_lyon1.info.m1.microblog.model.scoring.ScoringStrategies;
import fr.univ_lyon1.info.m1.microblog.model.sorting.DateSortingStrategy;
import fr.univ_lyon1.info.m1.microblog.model.sorting.ScoreSortingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.univ_lyon1.info.m1.microblog.dto.YData;

/**
 * Unit tests for the Y class.
 */
class YTest {

    private Y model;
    private User user1;
    private User user2;
    private Message m1;
    private Message m2;

    @BeforeEach
    void setUp() {
        model = new Y();

        model.removeScoringSystem();

        model.getScoringSystem().addRule(new BookmarkScoring());
        model.getScoringSystem().addRule(new MalusScoring());

        model.createUser("user1");
        model.createUser("user2");

        user1 = model.getUsers().stream().filter(u ->
                u.getUserTag().equals("user1")).findFirst().orElse(null);
        user2 = model.getUsers().stream().filter(u ->
                u.getUserTag().equals("user2")).findFirst().orElse(null);

        m1 = new Message(0, "Macron's policy update");
        m2 = new Message(1, "Hello, World!");

        model.addMessage(m1, user1);
        model.addMessage(m2, user2);
    }

    @Test
    void testConstructor() {
        Y model = new Y();

        assertThat(model.getUsers().size(), is(0));

        List<ScoringStrategies> rules = model.getScoringSystem().getRules();
        assertThat(rules.size(), is(0));
    }

    @Test
    void testSetAndGetCurrentUser() {
        assertThat(model.getCurrentUser(), is(nullValue()));

        model.setCurrentUser(user1);
        assertThat(model.getCurrentUser(), is(user1));

        model.setCurrentUser(user2);
        assertThat(model.getCurrentUser(), is(user2));
    }

    @Test
    void testSetCurrentUserToNull() {
        model.setCurrentUser(user1);
        assertThat(model.getCurrentUser(), is(user1));

        model.setCurrentUser(null);
        assertThat(model.getCurrentUser(), is(nullValue()));
    }

    @Test
    void testResetToDefaultScoring() {
        model.setScoringSystem(new MalusScoring());

        model.resetToDefaultScoring();

        assertThat(model.getScoringSystem().getRules(), hasSize(2));
        assertThat(model.getScoringSystem().getRules().get(0), instanceOf(BookmarkScoring.class));
        assertThat(model.getScoringSystem().getRules().get(1), instanceOf(MalusScoring.class));
    }


    @Test
    void testCreateUser() {
        assertThat(model.getUsers().size(), is(2));
        assertThat(user1.getUserTag(), is("user1"));
        assertThat(user2.getUserTag(), is("user2"));
    }

    @Test
    void testCreateUserWithExistingTag() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            model.createUser("user1");
        });

        assertThat(exception.getMessage(), is("User with tag 'user1' already exists."));
    }


    @Test
    void testAddMessage() {
        Map<Message, MessageData> user1Messages = model.getUserMessageMap(user1);
        Map<Message, MessageData> user2Messages = model.getUserMessageMap(user2);

        // Vérifie la présence des messages dans chaque UserMap
        assertThat(user1Messages.containsKey(m1), is(true));
        assertThat(user2Messages.containsKey(m1), is(true));
        assertThat(user1Messages.containsKey(m2), is(true));
        assertThat(user2Messages.containsKey(m2), is(true));
    }

    @Test
    void testBookmarkMessage() {
        // test avant
        assertThat(model.getUserMessageMap(user2).get(m1).isBookmarked(), is(false));

        model.bookmarkMessage(model.getUserMessageMap(user2).get(m1), m1, user2);

        // test après
        assertThat(model.getUserMessageMap(user2).get(m1).isBookmarked(), is(true));
    }

    @Test
    void testDeleteMessage() {
        model.deleteMessage(m1);

        assertThat(model.getUserMessageMap(user1).containsKey(m1), is(false));
        assertThat(model.getUserMessageMap(user2).containsKey(m1), is(false));
    }

    @Test
    void testCanBeSeen() {
        // seuil = 0
        MessageData messageData1 = model.getUserMessageMap(user1).get(m1);
        MessageData messageData2 = model.getUserMessageMap(user1).get(m2);

        assertThat(model.canBeSeen(messageData1), is(false)); // score : -1
        assertThat(model.canBeSeen(messageData2), is(true)); // score : 1
    }

    @Test
    void testResetUserMessages() {
        // avant
        assertThat(model.getUserMessageMap(user1).size(), is(2));
        assertThat(model.getUserMessageMap(user2).size(), is(2));

        model.setUserMessageMap(user1, new LinkedHashMap<>());
        model.setUserMessageMap(user2, new LinkedHashMap<>());

        // après
        assertThat(model.getUserMessageMap(user1).size(), is(0));
        assertThat(model.getUserMessageMap(user2).size(), is(0));
    }

    @Test
    void testSortMessages() {
        model.sortMessages();

        assertThat(new ArrayList<>(model.getUserMessageMap(user1).keySet()).get(0), is(m2)); // 1
        assertThat(new ArrayList<>(model.getUserMessageMap(user1).keySet()).get(1), is(m1)); // -1

        assertThat(new ArrayList<>(model.getUserMessageMap(user2).keySet()).get(0), is(m2)); // 1
        assertThat(new ArrayList<>(model.getUserMessageMap(user2).keySet()).get(1), is(m1)); // -1
    }


    @Test
    void testFindUserByTag() {
        User foundUser = model.findUserByTag("user1");
        assertThat(foundUser.getUserTag(), is("user1"));

        User nonExistentUser = model.findUserByTag("nonexistent");
        assertThat(nonExistentUser, is(nullValue()));
    }


    @Test
    void testImportExportData() {
        YData data = new YData();

        // Création d'un utilisateur "mimi"
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setUsertag("mimi");
        data.getUsers().add(userDTO1);

        // Création d'un message
        MessageDTO messageDTO = new MessageDTO(0, "mimi", "Test message",
                System.currentTimeMillis(), new ArrayList<>());
        data.getMessages().add(messageDTO);

        data.setCurrentUser("mimi");

        System.out.println(model.getUsers());

        // qd on import ça clear ce qu'il y avait avant
        model.importData(data);

        assertThat(model.getUsers().size(), is(1));
        assertThat(model.getUserMessageMap(model.findUserByTag("mimi")).size(), is(1));
        assertThat(model.getCurrentUser(), is(model.findUserByTag("mimi")));

        YData exportedData = model.exportData();

        assertThat(exportedData.getUsers().size(), is(1));
        assertThat(exportedData.getMessages().size(), is(1));

        MessageDTO exportedMessage = exportedData.getMessages().get(0);
        assertThat(exportedMessage.getContent(), is("Test message"));
        assertThat(exportedMessage.getUsertag(), is("mimi"));

        assertThat(exportedData.getCurrentUser(), is("mimi"));
    }

    @Test
    public void testChronologicalStrategy() {
        model.applyChronologicalStrategy();

        assertThat(model.getSortingStrategy(), instanceOf(DateSortingStrategy.class));
        assertThat(model.getScoringSystem().getRules(), is(empty()));
    }

    @Test
    public void testRecentRelevantMessagesStrategy() {
        model.applyRecentRelevantMessagesStrategy();

        assertThat(model.getSortingStrategy(), instanceOf(ScoreSortingStrategy.class));
        assertThat(model.getScoringSystem().getRules(), hasItem(instanceOf(DateScoring.class)));
        assertThat(model.getScoringSystem().getRules(), hasItem(instanceOf(MalusScoring.class)));
        assertThat(model.getScoringSystem().getRules(), hasItem(instanceOf(BookmarkScoring.class)));
    }

    @Test
    public void testRelevantMessagesStrategy() {
        model.applyRelevantMessagesStrategy();

        assertThat(model.getSortingStrategy(), instanceOf(ScoreSortingStrategy.class));
        assertThat(model.getScoringSystem().getRules(), hasItem(instanceOf(MalusScoring.class)));
        assertThat(model.getScoringSystem().getRules(), hasItem(instanceOf(BookmarkScoring.class)));
    }

}
