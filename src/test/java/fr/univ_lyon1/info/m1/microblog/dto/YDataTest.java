package fr.univ_lyon1.info.m1.microblog.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the YData class.
 */
public class YDataTest {

    private YData yData;

    /**
     * Setup method to initialize a YData object before each test.
     */
    @BeforeEach
    public void setUp() {
        yData = new YData();
    }

    /**
     * Test the default state of the YData object.
     */
    @Test
    public void testDefaultState() {
        assertThat(yData.getUsers(), is(notNullValue()));
        assertThat(yData.getUsers(), is(empty()));
        assertThat(yData.getMessages(), is(notNullValue()));
        assertThat(yData.getMessages(), is(empty()));
        assertThat(yData.getCurrentUser(), is(nullValue()));
    }

    /**
     * Test setting and getting the list of users.
     */
    @Test
    public void testSetAndGetUsers() {
        List<UserDTO> users = new ArrayList<>();
        users.add(new UserDTO());
        users.add(new UserDTO());

        yData.setUsers(users);

        assertThat(yData.getUsers(), is(notNullValue()));
        assertThat(yData.getUsers(), hasSize(2));
        assertThat(yData.getUsers(), is(users));
    }

    /**
     * Test setting and getting the list of messages.
     */
    @Test
    public void testSetAndGetMessages() {
        List<MessageDTO> messages = new ArrayList<>();
        messages.add(new MessageDTO());
        messages.add(new MessageDTO());

        yData.setMessages(messages);

        assertThat(yData.getMessages(), is(notNullValue()));
        assertThat(yData.getMessages(), hasSize(2));
        assertThat(yData.getMessages(), is(messages));
    }

    /**
     * Test setting null to users list.
     */
    @Test
    public void testSetUsersToNull() {
        yData.setUsers(null);
        assertThat(yData.getUsers(), is(nullValue()));
    }

    /**
     * Test setting null to messages list.
     */
    @Test
    public void testSetMessagesToNull() {
        yData.setMessages(null);
        assertThat(yData.getMessages(), is(nullValue()));
    }

    /**
     * Test setting and getting the current user.
     */
    @Test
    public void testSetAndGetCurrentUser() {
        assertThat(yData.getCurrentUser(), is(nullValue()));

        String userTag = "miryam";
        yData.setCurrentUser(userTag);

        assertThat(yData.getCurrentUser(), is(userTag));

        String anotherUserTag = "mariam";
        yData.setCurrentUser(anotherUserTag);

        assertThat(yData.getCurrentUser(), is(anotherUserTag));
    }

}

