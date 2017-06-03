package pt.ipb.poo.projeto.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    private String title;

    private List<String> options = new ArrayList<>();

    public Menu(String title) {
        this.title = title;

    }

    public void addOption(String option) {
        options.add(option);
    }

    public int print() {
        System.out.println("********************");
        System.out.println(" " + title);
        System.out.println("********************");
        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);
            // 1. Bla
            System.out.println((i + 1) + ". " + option);
        }
        System.out.println("********************");

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        try {
            System.out.print("Escolha uma opção: ");
            String line = reader.readLine();
            int option = Integer.parseInt(line);
            if (option > options.size()) {
                System.out.println("Opção inválida: " + option);
                return print();
            } else {
                return option;
            }
        } catch (IOException e) {
            System.out.println("Erro de opção: " + e.getMessage());
            return print();
        } catch (NumberFormatException e) {
            System.out.println("A opção não é um número");
            return print();
        }
    }

}
