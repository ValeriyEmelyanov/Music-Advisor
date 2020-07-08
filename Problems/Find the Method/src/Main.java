class MethodFinder {

    public static String findMethod(String methodName, String[] classNames) {
        for (String s : classNames) {
            Class aClass = null;
            try {
                aClass = Class.forName(s);
            } catch (ClassNotFoundException e) {
                continue;
            }
            for (java.lang.reflect.Method method : aClass.getMethods()) {
                if (method.getName().equals(methodName)) {
                    return aClass.getName();
                }
            }
        }
        return null;
    }
}