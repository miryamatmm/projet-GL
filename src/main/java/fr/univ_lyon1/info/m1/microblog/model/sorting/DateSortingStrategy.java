package fr.univ_lyon1.info.m1.microblog.model.sorting;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.MessageData;
import fr.univ_lyon1.info.m1.microblog.model.User;

import java.util.Comparator;
import java.util.Map;

/**
 * Sorting of messages based on the date.
 */
public class DateSortingStrategy implements SortingStrategies {
    /**
     * Get the comparator of messages based on the date.
     */
    @Override
    public Comparator<Map.Entry<Message, MessageData>> getComparator(final User user) {
        // comparateur pour avoir les récents en haut
        return (entry1, entry2) ->
                entry2.getValue().getDate().compareTo(entry1.getValue().getDate());
    }
}
