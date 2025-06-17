package fr.univ_lyon1.info.m1.microblog.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for the Y class.
 * This class holds lists of users and messages.
 */
public class YData {
    private List<UserDTO> users = new ArrayList<>();
    private List<MessageDTO> messages = new ArrayList<>();
    private String currentUser;

    /**
     * Gets the list of users.
     *
     * @return List of users
     */
    public List<UserDTO> getUsers() {
        return users;
    }

    /**
     * Sets the list of users.
     *
     * @param users List of users
     */
    public void setUsers(final List<UserDTO> users) {
        this.users = users;
    }

    /**
     * Gets the list of messages.
     *
     * @return List of messages
     */
    public List<MessageDTO> getMessages() {
        return messages;
    }

    /**
     * Sets the list of messages.
     *
     * @param messages List of messages
     */
    public void setMessages(final List<MessageDTO> messages) {
        this.messages = messages;
    }

    /**
     * Gets the current user logged in the application.
     *
     * @return the current user
     */
    public String getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user logged in the application.
     *
     * @param currentUser the userTag of the current user
     */
    public void setCurrentUser(final String currentUser) {
        this.currentUser = currentUser;
    }
}
