package fr.univ_lyon1.info.m1.microblog.model;

import fr.univ_lyon1.info.m1.microblog.dto.MessageDTO;
import fr.univ_lyon1.info.m1.microblog.dto.UserDTO;
import fr.univ_lyon1.info.m1.microblog.dto.YData;

import fr.univ_lyon1.info.m1.microblog.model.scoring.BookmarkScoring;
import fr.univ_lyon1.info.m1.microblog.model.scoring.MalusScoring;
import fr.univ_lyon1.info.m1.microblog.model.scoring.ScoringStrategies;
import fr.univ_lyon1.info.m1.microblog.model.scoring.ScoringSystem;
import fr.univ_lyon1.info.m1.microblog.model.scoring.DateScoring;

import fr.univ_lyon1.info.m1.microblog.model.sorting.DateSortingStrategy;
import fr.univ_lyon1.info.m1.microblog.model.sorting.SortingStrategies;
import fr.univ_lyon1.info.m1.microblog.model.sorting.ScoreSortingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;


/**
 * Toplevel class for the Y microblogging application's model.
 */
public class Y {

    private final List<User> users = new ArrayList<>();
    private final Map<User, Map<Message, MessageData>> userMessageMap = new HashMap<>();
    private final ScoringSystem scoringSystem = new ScoringSystem();
    private SortingStrategies sortingStrategy = new ScoreSortingStrategy();
    private final Map<User, String> currentStrategyUser = new HashMap<>();
    private User currentUser;

    /**
     * Model constructor.
     */
    public Y() { }

    /**
     * Get the current user logged in the application.
     * * If nobody is logged, it's null
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Set the current user logged in the application.
     * If nobody is logged, it's null
     */
    public void setCurrentUser(final User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Get the User.
     */
    public Collection<User> getUsers() {
        return users;
    }

    /**
     * Get the User Message Map.
     */
    public Map<Message, MessageData> getUserMessageMap(final User u) {
        return userMessageMap.computeIfAbsent(u, k -> new LinkedHashMap<>());
    }

    /**
     * Set the User Message Map.
     */
    public void setUserMessageMap(final User u, final Map<Message, MessageData> messages) {
        userMessageMap.put(u, messages);
    }

    /**
     * Create a user and add it to the user's registry.
     * Ensures that userTag is unique.
     */
    public void createUser(final String userTag) {
        if (users.stream().anyMatch(user -> user.getUserTag().equals(userTag))) {
            throw new IllegalArgumentException("User with tag '" + userTag + "' already exists.");
        }

        User u = new User(userTag);
        users.add(u);
        userMessageMap.put(u, new LinkedHashMap<>());
    }

    /**
     * Finds a user by their usertag.
     */
    public User findUserByTag(final String usertag) {
        for (User user : users) {
            if (user.getUserTag().equals(usertag)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Add message m to all users.
     */
    public void addMessage(final Message m, final User u) {
        String[] words = m.getContent().toLowerCase().split("[^\\p{Alpha}]+");
        Set<String> wordSet = new HashSet<>(Arrays.asList(words));

        // Pour chaque utilisateur, ajoute le message à sa map
        for (User user : getUsers()) {
            Map<Message, MessageData> userMessages = getUserMessageMap(user);
            MessageData messageData = new MessageData(u);
            messageData.setWords(wordSet);
            userMessages.put(m, messageData);
        }

        sortMessages();
    }

    /**
     * Bookmark or unbookmark a message.
     */
    public void bookmarkMessage(final MessageData d, final Message m, final User u) {
        // on extrait les mots du message
        String[] words = m.getContent().split("[^\\p{Alpha}]+");
        Set<String> wordSet = new HashSet<>(Arrays.asList(words));

        d.setWords(wordSet);

        if (d.getBookmarkedUsers().contains(u)) {
            // si l'utilisateur a déjà bookmarké ce message
            for (String word : wordSet) {
                u.getBookmarkedWords().remove(word);
            }
            d.removeBookmarkedUser(u);
            d.setBookmarked(false);
        } else {
            d.addBookmarkedUser(u);
            d.setBookmarked(true);
            for (String word : wordSet) {
                u.getBookmarkedWords().add(word);
            }
        }

        // on met à jour la liste dans la map de chaque utilisateur (pr json)
        for (User user : getUsers()) {
            Map<Message, MessageData> userMessages = getUserMessageMap(user);
            MessageData userMessageData = userMessages.computeIfAbsent(m,
                    k -> new MessageData(d.getUser()));

            if (d.getBookmarkedUsers().contains(u)) {
                userMessageData.addBookmarkedUser(u);
            } else {
                userMessageData.removeBookmarkedUser(u);
            }
        }

        sortMessages();
    }

    /**
     * Delete a message.
     */
    public void deleteMessage(final Message m) {
        for (User u : users) {
            Map<Message, MessageData> userMessages = getUserMessageMap(u);
            MessageData messageData = userMessages.get(m);
            Set<String> wordsToRemove = messageData.getWords();

            // on supprime les mots des bookmarked words si le message a été bookmarké
            for (String word : wordsToRemove) {
                u.getBookmarkedWords().remove(word);
            }

            userMessages.remove(m);
        }

        sortMessages();
    }

    /**
     * Return true if the message should be displayed.
     */
    public boolean canBeSeen(final MessageData d) {
        int seuil = 0;
        return d.getScore() > seuil;
    }

    /**
     * Sort the Messages.
     */
    public void sortMessages() {
        for (User u : users) {
            Map<Message, MessageData> messageList = getUserMessageMap(u);
            LinkedHashMap<Message, MessageData> sortedMessageList = new LinkedHashMap<>();
            scoringSystem.calculateTotalScore(messageList, u);
            messageList.entrySet()
                    .stream()
                    .sorted(sortingStrategy.getComparator(u))
                    .forEach(e -> {
                        sortedMessageList.put(e.getKey(), e.getValue());
                    });
            setUserMessageMap(u, sortedMessageList);
        }
    }

    /**
     * Get the map of the current strategy.
     */
    public Map<User, String> getCurrentStrategyUser() {
        return currentStrategyUser;
    }

    /**
     * Set scoring strategies.
     */
    public void setScoringSystem(final ScoringStrategies s) {
        scoringSystem.addRule(s);
    }

    /**
     * Set the sorting strategy.
     */
    public void setSortingStrategy(final SortingStrategies sortingStrategy) {
        this.sortingStrategy = sortingStrategy;
    }

    /**
     * Get the sorting strategy.
     */
    public SortingStrategies getSortingStrategy() {
        return sortingStrategy;
    }

    /**
     * Remove all scoring strategies.
     */
    public void removeScoringSystem() {
        scoringSystem.clearRules();
    }

    /**
     * Get scoring strategies.
     */
    public ScoringSystem getScoringSystem() {
        return scoringSystem;
    }

    /**
     * Reset the scoring system to its default configuration.
     */
    public void resetToDefaultScoring() {
        scoringSystem.clearRules();
        scoringSystem.addRule(new BookmarkScoring());
        scoringSystem.addRule(new MalusScoring());
    }

    /**
     * Applies a sorting strategy based solely on chronological order.
     */
    public void applyChronologicalStrategy() {
        removeScoringSystem();
        setSortingStrategy(new DateSortingStrategy());
    }

    /**
     * Applies a sorting strategy that prioritizes the most relevant recent messages.
     */
    public void applyRecentRelevantMessagesStrategy() {
        removeScoringSystem();
        setSortingStrategy(new ScoreSortingStrategy());
        setScoringSystem(new DateScoring());
        setScoringSystem(new MalusScoring());
        setScoringSystem(new BookmarkScoring());
    }

    /**
     * Applies a sorting strategy that prioritizes the most relevant messages overall.
     */
    public void applyRelevantMessagesStrategy() {
        removeScoringSystem();
        setSortingStrategy(new ScoreSortingStrategy());
        setScoringSystem(new MalusScoring());
        setScoringSystem(new BookmarkScoring());
    }

    /**
     * Imports data from a YData object into the model.
     */
    public void importData(final YData data) {
        users.clear();
        userMessageMap.clear();

        // création des utilisateurs
        for (UserDTO userDTO : data.getUsers()) {
            createUser(userDTO.getUsertag());
            currentStrategyUser.put(findUserByTag(userDTO.getUsertag()), userDTO.getUserStrategy());
        }

        // création des messages
        for (MessageDTO messageDTO : data.getMessages()) {
            User creator = findUserByTag(messageDTO.getUsertag());
            if (creator != null) {

                Message message = new Message(messageDTO.getId(), messageDTO.getContent());
                addMessage(message, creator);

                // pour récupérer la date des messages
                for (User user : getUsers()) {
                    Map<Message, MessageData> userMessages = getUserMessageMap(user);
                    MessageData messageData = userMessages.get(message);

                    if (messageData != null) {
                        // Assigner la date de création du message
                        messageData.setDateCreation(new Date(messageDTO.getCreationTimestamp()));
                    }
                }

                // mettre à jour le bookmarkage pour chaque userMessageMap
                for (String bookmarkUserTag : messageDTO.getBookmarkedByUsers()) {
                    User bookmarkUser = findUserByTag(bookmarkUserTag);
                    if (bookmarkUser != null) {
                        Map<Message, MessageData> userMessages = getUserMessageMap(bookmarkUser);
                        MessageData messageData = userMessages.get(message);
                        if (messageData != null) {
                            messageData.setBookmarked(true);
                            messageData.addBookmarkedUser(bookmarkUser);
                        }
                    }
                }
            }
        }

        // récupération du currentUser
        setCurrentUser(findUserByTag(data.getCurrentUser()));
    }

    /**
     * Exports the model data to a YData object.
     */
    public YData exportData() {
        YData data = new YData();

        // création des utilisateurs dans les DTO
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsertag(user.getUserTag());
            userDTO.setUserStrategy(currentStrategyUser.get(findUserByTag(userDTO.getUsertag())));
            data.getUsers().add(userDTO);
        }

        // on récupère la map des messages QUE du premier utilisateur (si existant)
        if (!users.isEmpty()) {
            User firstUser = users.get(0);
            Map<Message, MessageData> messages = getUserMessageMap(firstUser);

            for (Map.Entry<Message, MessageData> messageEntry : messages.entrySet()) {
                Message message = messageEntry.getKey();
                MessageData messageData = messageEntry.getValue();

                List<String> bookmarkedByUsers = new ArrayList<>();
                for (User bookmarkedUser : messageData.getBookmarkedUsers()) {
                    bookmarkedByUsers.add(bookmarkedUser.getUserTag());
                }

                MessageDTO messageDTO = new MessageDTO(
                        message.getIdMessage(),
                        messageData.getUser().getUserTag(),
                        message.getContent(),
                        messageData.getDate().getTime(),
                        bookmarkedByUsers
                );
                data.getMessages().add(messageDTO);
            }
        }

        if (currentUser != null) {
            data.setCurrentUser(currentUser.getUserTag());
        } else {
            data.setCurrentUser(null);
        }

        return data;
    }

    /**
     * Set the User Message Map. (for debug)
     */
    public static void printMessageMap(final Map<Message, MessageData> messageMap) {
        // Vérifier si la Map est null
        if (messageMap == null) {
            System.out.println("La map est null.");
            return;
        }

        // Vérifier si la Map est vide
        if (messageMap.isEmpty()) {
            System.out.println("La map est vide.");
            return;
        }

        // Afficher le contenu de la Map
        System.out.println("Contenu de la Map :");
        for (Map.Entry<Message, MessageData> entry : messageMap.entrySet()) {
            Message key = entry.getKey();
            MessageData value = entry.getValue();
            System.out.println("Message: "
                    + key.getContent()
                    + " | Score: " + value.getScore()
                    + " | Bookmarked: " + value.isBookmarked());
        }
    }
}
