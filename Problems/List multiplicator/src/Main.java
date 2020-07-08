import java.util.List;

/**
 * Class to modify
 */
class ListMultiplicator {

    /**
     * Multiplies original list provided number of times
     *
     * @param list list to multiply
     * @param n    times to multiply, should be zero or greater
     */
    public static void multiply(List<?> list, int n) {
        if (list == null || n < 0) {
            return;
        }

        multiplyHelper(list, n);
    }

    private static <T> void multiplyHelper(List<T> list, int n) {
        switch (n) {
            case 0:
                list.clear();
                return;
            case 1:
                return;
            default:
                final List<T> template = List.copyOf(list);
                for (int i = 1; i < n; i++) {
                    list.addAll(template);
                }
                break;
        }
    }
}