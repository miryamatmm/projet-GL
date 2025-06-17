package fr.univ_lyon1.info.m1.microblog.model.scoring;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.MessageData;
import fr.univ_lyon1.info.m1.microblog.model.User;

/**
 * Interface of all scoring strategies.
 */
public interface ScoringStrategies {
        /** 
         * Compute the score for all messages in messagesData. 
         */
        int calculateScore(Message message, MessageData messageData, User user);

        @Override
        String toString();
}
