import java.util.ArrayList;

/**
 * Class to work with
 */
class Violator {

    public static List<Box<? extends Bakery>> defraud() {
        Box box = new Box();
        box.put(new Paper());
        List<Box<? extends Bakery>> list = new ArrayList();
        list.add(box);
        return list;
    }

}
