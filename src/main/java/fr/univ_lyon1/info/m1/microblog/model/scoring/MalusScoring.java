package fr.univ_lyon1.info.m1.microblog.model.scoring;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.MessageData;
import fr.univ_lyon1.info.m1.microblog.model.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Malus of messages.
 */
public class MalusScoring implements ScoringStrategies {
    // Liste des mots avec casse ignorée (tous convertis en minuscule)
    private final Set<String> malusWords =
            new HashSet<>(
                    Arrays.asList("Macron", "Israel", "50/50", "homme", "hommes", "mixité",
                                    "mariage", "ben laden", "pd", "vache", "grosse", "xu",
                                    "travail", "php", "voilée", "2.0", "fdp", "merde", "ptn",
                                    "pute", "ntm", "poupette", "#poupettekenza", "femme",
                                    "cuisine", "allan", "rn", "suicide"
                            ).stream()
                            .map(String::toLowerCase)
                            .collect(Collectors.toSet())
            );

    /**
     * Compute the score for all messages in messagesData.
     */
    @Override
    public int calculateScore(final Message message,
                              final MessageData messageData,
                              final User user) {
        int score = 0;


        String[] words = message.getContent().split("[^\\p{Alpha}]+");
        Set<String> wordSet = new HashSet<>(Arrays.stream(words)
                .map(String::toLowerCase)
                .collect(Collectors.toSet()));
        messageData.setWords(wordSet);

        // Calculer le score
        for (String word : wordSet) {
            if (malusWords.contains(word)) {
                score -= 2; // Appliquer un malus si le mot est dans malusWords
            }
        }

        return score;
    }

    @Override
    public String toString() {
        return "Par éthique";
    }

}
