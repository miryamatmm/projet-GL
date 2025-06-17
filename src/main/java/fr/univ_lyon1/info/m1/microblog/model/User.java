package fr.univ_lyon1.info.m1.microblog.model;

import java.util.ArrayList;
import java.util.List;


/**
 * User of the application.
 */
public class User {
    private final String userTag; // sert d'id pour le User
    private final List<String> bookmarkedWords; // Ensemble des mots bookmarkés par l'utilisateur

    /**
     * Default constructor for User.
     * @param userTag must be a unique identifier.
     */
    public User(final String userTag) {
        this.userTag = userTag;
        this.bookmarkedWords = new ArrayList<>(); // Initialisation de l'ensemble
    }

    /**
     * Get the User's usertag.
     */
    public String getUserTag() {
        return userTag;
    }

    /**
     * Add a word to the user's bookmarked words.
     */
    public void addBookmarkedWord(final String word) {
        bookmarkedWords.add(word);
    }

    /**
     * Remove a word from the user's bookmarked words.
     */
    public void removeBookmarkedWord(final String word) {
        bookmarkedWords.remove(word);
    }

    /**
     * Get the set of bookmarked words for the user.
     */
    public List<String> getBookmarkedWords() {
        return bookmarkedWords;
    }
}
