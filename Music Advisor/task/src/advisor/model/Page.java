package advisor.model;

import java.util.List;

public class Page<T> {
    private final List<T> list;
    private final int limit;
    private final int offset;
    private final int total;
    private final String next;
    private final String previous;

    public Page(List<T> list, int limit, int offset, int total, String next, String previous) {
        this.list = list;
        this.limit = limit;
        this.offset = offset;
        this.total = total;
        this.next = next;
        this.previous = previous;
    }

    public List<T> getList() {
        return list;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getTotal() {
        return total;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }
}
