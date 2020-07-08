class Shelf<T extends Book> {
    private T element;

    T getElement() {
        return element;
    }

    void setElement(T element) {
        this.element = element;
    }
}