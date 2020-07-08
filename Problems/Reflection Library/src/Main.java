import java.util.HashMap;
import java.util.Map;

class ReflectionLibrary {

    public static Class getPrimitiveClass(String name) {
        Map<String, Class> map = getStringClassMap();
        return map.get(name);
    }

    public static String getClassEncoding(String name) {
        String str = getStringEncodedStringMap().get(name);
        if (str == null) {
            return null;
        }
        return "[" + str;
    }

    public static String getObjectEncoding(Object object) {
        String name = object.getClass().getName();
        return "[[L" + name + ";";
    }

    private static Map<String, Class> getStringClassMap() {
        Class[] array = {
            byte.class,
            char.class,
            short.class,
            int.class,
            long.class,
            float.class,
            double.class,
            boolean.class,
            void.class};
        Map<String, Class> map = new HashMap<>();
        for (Class aClass : array) {
            map.put(aClass.getSimpleName(), aClass);
        }
        return map;
    }

    private static Map<String, String> getStringEncodedStringMap() {
        Map<String, String> map = new HashMap<>();
        map.put("byte", "B");
        map.put("char", "C");
        map.put("short", "S");
        map.put("int", "I");
        map.put("long", "J");
        map.put("float", "F");
        map.put("double", "D");
        map.put("boolean", "Z");
        return map;
    }
}