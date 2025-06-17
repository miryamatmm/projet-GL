package fr.univ_lyon1.info.m1.microblog.model.sorting;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.MessageData;
import fr.univ_lyon1.info.m1.microblog.model.User;

import java.util.Comparator;
import java.util.Map;

/**
 * Interface of all sorting strategies.
 */
public interface SortingStrategies {
        /**
         * Function to get the comparator for the sorting.
         */
        Comparator<Map.Entry<Message, MessageData>> getComparator(User user);
}
