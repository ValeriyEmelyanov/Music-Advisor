package advisor.model.navigatestrategy;

import advisor.model.AdvisorHttpClient;

public abstract class AbstractNavigator implements Navigator {
    protected final AdvisorHttpClient ADVISOR_HTTP_CLIENT = AdvisorHttpClient.getInstance();
    protected String next;
    protected String previous;

    public AbstractNavigator(String next, String previous) {
        this.next = next;
        this.previous = previous;
    }

    @Override
    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public void setPrevious(String previous) {
        this.previous = previous;
    }

    @Override
    public boolean hasNext() {
        return next != null && !next.isEmpty();
    }

    @Override
    public boolean hasPrev() {
        return previous != null && !previous.isEmpty();
    }
}
