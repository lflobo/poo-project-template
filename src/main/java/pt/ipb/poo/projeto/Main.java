package pt.ipb.poo.projeto;

import pt.ipb.poo.projeto.data.DataManager;
import pt.ipb.poo.projeto.database.Test;
import pt.ipb.poo.projeto.menu.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    private DataManager dataManager;

    public Main() {
        dataManager = new DataManager();

        Menu menu = new Menu("Gestão de Endereços");
        menu.addOption("Sair"); // 1
        menu.addOption("Lista de Test"); // 2
        menu.addOption("Pesquisar Test"); // 3

        int option = 0;
        do {
            option = menu.print();
            System.out.println("Você escolheu a opção " + option);
            switch (option) {
                case 2:
                    listaDeTest();
                    break;
                case 3:
                    searchDeTest();
                    break;
            }
        } while (option != 1);
    }

    private void searchDeTest() {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        try {
            System.out.print("Critério de Pesquisa: ");
            String search = reader.readLine();
            List<Test> testList = dataManager.searchTestList(search);
            for (Test test : testList) {
                System.out.println("test: id = " + test.getId() + ", text = " + test.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void listaDeTest() {
        List<Test> testList = dataManager.getTestList();
        for (Test test : testList) {
            System.out.println("test: id = " + test.getId() + ", text = " + test.getText());
        }
    }

    public static void main(String[] args) {
        new Main();
    }

}
