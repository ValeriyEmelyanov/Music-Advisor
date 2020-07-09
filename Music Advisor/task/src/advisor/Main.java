package advisor;

import advisor.controller.AdvisorController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> argsList = new ArrayList<>();
        for (String arg : args) {
            argsList.addAll(Arrays.asList(arg.split("[\\u00A0\\s]+")));
        }
        if (argsList.size() % 2 != 0) {
            throw new IllegalArgumentException("Wrong argument number!");
        }

        AdvisorController controller = new AdvisorController();
        for (int i = 0; i < argsList.size(); i += 2) {
            if ("-access".equals(argsList.get(i))) {
                controller.setServerPath(argsList.get(i + 1));
            }
            if ("-resource".equals(argsList.get(i))) {
                controller.setResourcePath(argsList.get(i + 1));
            }
        }
        controller.run();
    }

}
