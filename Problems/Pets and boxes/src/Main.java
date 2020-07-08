class Box<T extends Animal> {
    private java.util.List<T> items = new java.util.ArrayList<>();

    public void add(T item) {
        items.add(item);
    }
}

class Animal {

}