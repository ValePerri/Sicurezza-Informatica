import java.util.*;

public class MapUtil {
    public static <K, V extends Comparable<? super V>> HashMap<K, V> 
        sortByValue(HashMap<K, V> map, int sel) {
        List<HashMap.Entry<K, V>> list = new LinkedList<HashMap.Entry<K, V>>(map.entrySet());
        Collections.sort( list, new Comparator<HashMap.Entry<K, V>>() {
            public int compare(HashMap.Entry<K, V> o1, HashMap.Entry<K, V> o2) {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        });
        int count = 0;
        HashMap<K, V> result = new LinkedHashMap<K, V>();
        for (HashMap.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
            count++;
            if(count == sel)
            		break;
        }
        return result;
    }
}
