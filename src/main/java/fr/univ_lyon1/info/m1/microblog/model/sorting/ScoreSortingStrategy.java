package fr.univ_lyon1.info.m1.microblog.model.sorting;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.MessageData;
import fr.univ_lyon1.info.m1.microblog.model.User;

import java.util.Comparator;
import java.util.Map;

/**
 * Sorting of messages based on the score.
 */
public class ScoreSortingStrategy implements SortingStrategies {
    @Override
    public Comparator<Map.Entry<Message, MessageData>> getComparator(final User user) {
        return (left, right) ->
                Double.compare(right.getValue().getScore(), left.getValue().getScore());
    }
}


