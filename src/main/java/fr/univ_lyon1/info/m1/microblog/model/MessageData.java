package fr.univ_lyon1.info.m1.microblog.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Piece of data associated to a message for a particular user.
 */
public class MessageData {
    /**
     * Represents the data associated with a message.
     */
    private Date dateCreation = new Date();
    private boolean isBookmarked = false;
    private int score = -1;
    private Set<String> words = new HashSet<>();
    // les utilisateurs qui ont bookmark ce message
    private final Set<User> bookmarkedUsers = new HashSet<>();

    private User user;

    /**
     * Construct a MessageData Object.
     */
    public MessageData() { }

    /**
     * Get the User.
     */
    public User getUser() {
        return user;
    }

    /**
     * Construct a MessageData Object with a User.
     */
    public MessageData(final User user) {
        this.user = user;
    }

    /**
     * Get the Date.
     */
    public Date getDate() { 
        return dateCreation; 
    }

    /**
     * Returns the set of words associated with the message.
     *
     * @return the set of words
     */
    public Set<String> getWords() {
        return words;
    }

    /**
     * Sets the date of creation associated with the message.
     */
    public void setDateCreation(final Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * Sets the set of words associated with the message.
     *
     * @param words the set of words
     */
    public void setWords(final Set<String> words) {
        this.words = words;
    }

    /**
     * Returns the score of the message.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of the message.
     *
     * @param score the score
     */
    public void setScore(final int score) {
        this.score = score;
    }

    /**
     * Returns whether the message is bookmarked or not.
     *
     * @return true if the message is bookmarked, false otherwise
     */
    public boolean isBookmarked() {
        return isBookmarked;
    }

    /**
     * Sets whether the message is bookmarked or not.
     *
     * @param bookmarked true if the message is bookmarked, false otherwise
     */
    public void setBookmarked(final boolean bookmarked) {
        this.isBookmarked = bookmarked;
    }

        /**
     * Returns the set of users who have bookmarked this message.
     *
     * @return the set of users
     */
    public Set<User> getBookmarkedUsers() {
        return bookmarkedUsers;
    }

    /**
     * Adds a user to the list of users who have bookmarked this message.
     *
     * @param user the user to add
     */
    public void addBookmarkedUser(final User user) {
        bookmarkedUsers.add(user);
    }

    /**
     * Removes a user from the list of users who have bookmarked this message.
     *
     * @param user the user to remove
     */
    public void removeBookmarkedUser(final User user) {
        bookmarkedUsers.remove(user);
        // met à jour l'état de bookmark si aucune autre personne n'a bookmarké
        isBookmarked = !bookmarkedUsers.isEmpty();
    }

    /**
     * Compare two Messages metadata. Suitable for sorting.
     */
    public int compare(final MessageData rightData) {
        int scoreLeft = getScore();
        boolean isBookmarkedLeft = isBookmarked();

        int scoreRight = rightData.getScore();
        boolean isBookmarkedRight = rightData.isBookmarked();

        // on traite dans un premier temps le fait qu'il soit bookmarké ou pas

        if (isBookmarkedLeft && !isBookmarkedRight) {
            return 1;
        } else if (!isBookmarkedLeft && isBookmarkedRight) {
            return -1;
        }

        // sinon on traite juste avec le score

        if (scoreLeft < scoreRight) {
            return -1;
        } else if (scoreLeft == scoreRight) {
            return 0;
        } else if (scoreLeft > scoreRight) {
            return 1;
        }
        throw new AssertionError("Case not covered in comparision");
    }
}
