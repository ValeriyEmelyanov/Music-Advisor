package advisor.veiw;

import java.util.List;

public class ConsoleView {
    private static final ConsoleView INSTANCE = new ConsoleView();

    private ConsoleView() {
    }

    public static ConsoleView getInstance() {
        return INSTANCE;
    }

    public void showList(List<?> list) {
        list.forEach(System.out::println);
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }
}
