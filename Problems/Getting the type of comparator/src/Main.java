import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Class to work with
 */
class ComparatorInspector {

    /**
     * Return Type variable that corresponds to the type parameterizing Comparator.   
     *
     * @param clazz {@link Class} object, should be non null
     * @return {@link Type} object or null if Comparable does not have type parameter
     */
    public static <T extends Comparable<?>> Type getComparatorType(Class<T> clazz) {
        Type[] types = clazz.getGenericInterfaces();
        if (types.length == 0) {
            return null;
        }

        for (Type type : types) {
            if (type instanceof ParameterizedType  && ((ParameterizedType) type).getRawType() == Comparable.class) {
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                if (actualTypeArguments.length == 0) {
                    return null;
                }
                return actualTypeArguments[0];
            }
        }

        return null;
    }

}