package fr.univ_lyon1.info.m1.microblog.model.scoring;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.MessageData;
import fr.univ_lyon1.info.m1.microblog.model.User;


/**
 * Scoring of messages based on bookmarks.
 */
public class BookmarkScoring implements ScoringStrategies {
    @Override
    public int calculateScore(final Message message, 
                              final MessageData messageData, 
                              final User user) {
        int score = 0;
        for (String word : messageData.getWords()) {
            if (user.getBookmarkedWords().contains(word)) {
                score++;
            }
        }
        return score;
    }

}
