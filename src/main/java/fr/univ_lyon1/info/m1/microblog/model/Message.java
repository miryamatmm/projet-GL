package fr.univ_lyon1.info.m1.microblog.model;

/**
 * Message and its own data.
 */
public class Message {
    private final int idMessage; // unique et commence à 0
    private final String content;

    /**
     * Build a Message object from it's (String) content.
     */
    public Message(final int idMessage, final String content) {
        this.idMessage = idMessage;
        this.content = content;
    }

    /**
     * Get the id's Message.
     */
    public int getIdMessage() {
        return idMessage;
    }

    /**
     * Get the Message's content.
     */
    public String getContent() {
        return content;
    }
}
