package fr.univ_lyon1.info.m1.microblog.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for the Message class.
 */
public class MessageDTO {
    private int id;
    private String usertag;
    private String content;
    private List<String> bookmarkedByUsers = new ArrayList<>();
    private long creationTimestamp;

    /**
     * Default constructor required by Jackson.
     */
    public MessageDTO() {
        // Default constructor for Jackson
    }

    /**
     * Constructor to initialize MessageDTO with specified values.
     *
     * @param id                 the message ID
     * @param usertag            the usertag of the message creator
     * @param content            the content of the message
     * @param creationTimestamp  the creation timestamp of the message
     * @param bookmarkedByUsers  the list of users who bookmarked the message
     */
    public MessageDTO(final int id, final String usertag, final String content,
                      final long creationTimestamp, final List<String> bookmarkedByUsers) {
        this.id = id;
        this.usertag = usertag;
        this.content = content;
        this.creationTimestamp = creationTimestamp;
        if (bookmarkedByUsers != null) {
            this.bookmarkedByUsers = bookmarkedByUsers;
        }
    }

    /**
     * Gets the message ID.
     *
     * @return the message ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the message ID.
     *
     * @param id the message ID
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Gets the usertag.
     *
     * @return the usertag of the message creator
     */
    public String getUsertag() {
        return usertag;
    }

    /**
     * Sets the usertag.
     *
     * @param usertag the usertag of the message creator
     */
    public void setUsertag(final String usertag) {
        this.usertag = usertag;
    }

    /**
     * Gets the message content.
     *
     * @return the content of the message
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the message content.
     *
     * @param content the content of the message
     */
    public void setContent(final String content) {
        this.content = content;
    }

    /**
     * Gets the list of users who bookmarked this message.
     *
     * @return the list of bookmarked users
     */
    public List<String> getBookmarkedByUsers() {
        return bookmarkedByUsers;
    }

    /**
     * Sets the list of users who bookmarked this message.
     *
     * @param bookmarkedByUsers the list of users who bookmarked the message
     */
    public void setBookmarkedByUsers(final List<String> bookmarkedByUsers) {
        this.bookmarkedByUsers = bookmarkedByUsers;
    }

    /**
     * Gets the creation timestamp of the message.
     *
     * @return the creation timestamp
     */
    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    /**
     * Sets the creation timestamp of the message.
     *
     * @param creationTimestamp the creation timestamp
     */
    public void setCreationTimestamp(final long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}
