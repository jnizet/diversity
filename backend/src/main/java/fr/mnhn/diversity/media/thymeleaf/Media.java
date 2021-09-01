package fr.mnhn.diversity.media.thymeleaf;

import fr.mnhn.diversity.model.Element;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A utility class exposed as an expression object to thymeleaf templates by {@link MediaDialect}
 * @author Arnaud Monteils
 */

public class Media {
    public String getContentType(Object content) {
      return ((HashMap<String, Object>)content).keySet().iterator().next();
    }

    /** Used to compare order of media element with their key */
    Comparator<HashMap<String, HashMap<String, Element>>> mediaElementComparator = new Comparator<HashMap<String, HashMap<String, Element>>>() {
        @Override
        public int compare(HashMap<String, HashMap<String, Element>> i1, HashMap<String, HashMap<String, Element>> i2) {
            return Integer.compareUnsigned(
                    Integer.parseInt(i1.values().iterator().next().values().iterator().next().getKey().split("\\.")[2]),
                    Integer.parseInt(i2.values().iterator().next().values().iterator().next().getKey().split("\\.")[2])
            );
        }
    };

    /** Used to distinct by a specific key */
    public static <T> Predicate<T> distinctByKey(
        Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /** This funtion do a distinct and sort all elements */
    public ArrayList<HashMap<String, HashMap<String, Element>>> orderMediaList(ArrayList<HashMap<String, HashMap<String, Element>>> content) {
        return content.stream().filter(distinctByKey(e -> e.values().iterator().next().values().iterator().next().getKey())).sorted(mediaElementComparator).collect(Collectors.toCollection(ArrayList::new));
    }
}

