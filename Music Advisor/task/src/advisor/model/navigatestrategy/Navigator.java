package advisor.model.navigatestrategy;

import advisor.model.Page;

import java.io.IOException;

public interface Navigator {
    void setNext(String next);
    void setPrevious(String previous);
    Page<?> next() throws IOException, InterruptedException;
    Page<?> prev() throws IOException, InterruptedException;
    boolean hasNext();
    boolean hasPrev();
}
