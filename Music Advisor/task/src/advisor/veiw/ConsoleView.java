package advisor.veiw;

import advisor.model.Page;
import advisor.model.entities.Playlist;

import java.util.List;

public class ConsoleView {
    private static final ConsoleView INSTANCE = new ConsoleView();

    private ConsoleView() {
    }

    public static ConsoleView getInstance() {
        return INSTANCE;
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }

    public void showPage(Page<?> page) {
        page.getList().forEach(System.out::println);
        if (page.getLimit() != 0) {
            System.out.println(
                    String.format("---PAGE %d OF %d---",
                            page.getOffset() / page.getLimit() + 1,
                            page.getTotal() / page.getLimit() + (page.getTotal() % page.getLimit() == 0 ? 0 : 1)
                    ));
        }
    }
}
