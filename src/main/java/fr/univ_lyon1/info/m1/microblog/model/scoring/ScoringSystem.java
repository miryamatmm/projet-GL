package fr.univ_lyon1.info.m1.microblog.model.scoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.MessageData;
import fr.univ_lyon1.info.m1.microblog.model.User;

/**
 * Every strategy used in the program.
 */
public class ScoringSystem {
        private final List<ScoringStrategies> rules = new ArrayList<>();
        
        /**
         * Add rule.
         */
        public void addRule(final ScoringStrategies rule) {
                rules.add(rule);
        }

        /**
         * Clear rules.
         */
        public void clearRules() {
                rules.clear();
        }

        /**
         * Get rules.
         */
        public List<ScoringStrategies> getRules() {
                return rules;
        }
        
        
        /** 
         * Calculate total score of all scoring strategies. 
         */
        public void calculateTotalScore(final Map<Message, MessageData> messagesData,
                                        final User user) {
                messagesData.values().forEach(data -> data.setScore(1));
                Set<Message> messages = messagesData.keySet();
                messages.forEach((Message m) -> {
                        MessageData d = messagesData.get(m);
                        // on initialise à 1 tout les messages pour qu'ils s'affichent tous 
                        // par défaut après c'est selon les preferences de tri
                        int score = 1; 
                        score += rules.stream()
                                        .mapToInt(rule -> rule.calculateScore(m, d, user))
                                        .sum();
                        d.setScore(score);
                        });
        }
}
