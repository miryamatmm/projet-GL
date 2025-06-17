package fr.univ_lyon1.info.m1.microblog.model.scoring;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.MessageData;
import fr.univ_lyon1.info.m1.microblog.model.User;

/** 
 * Scoring based on the date. 
*/
public class DateScoring implements ScoringStrategies {

    @Override
    public int calculateScore(final Message message, 
                              final MessageData messageData, 
                              final User user) {
        long currentTime = System.currentTimeMillis(); 
        long messageTime = messageData.getDate().getTime(); // la date en ms
        long timeSinceCreation = currentTime - messageTime;

        // les messages postés il y a
        // moins de 7 jours auront un bonus d'un point, 
        // ceux postés il y a moins de 24
        // heures un bonus supplémentaire d'un point.

        int score = 0;
        if (timeSinceCreation <= 86400000) { //  (24h * 60min * 60sec * 1000ms)
            score = 2;
        } else if (timeSinceCreation <= 604800000) { // (7j * 24h * 60min * 60sec * 1000ms)
            score = 1; 
        }
        return score;
    }

    @Override
    public String toString() {
        return "Par ordre chronologique";
    }

}
