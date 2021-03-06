package pt.ipb.poo.projeto;

import pt.ipb.poo.projeto.data.DataManager;
import pt.ipb.poo.projeto.database.Carro;
import pt.ipb.poo.projeto.database.Pessoa;
import pt.ipb.poo.projeto.menu.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    private DataManager dataManager;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        // Ligar à BD                 hostname,    database,   user,   pass
        dataManager = new DataManager("localhost", "trabalho", "trabalho", "1234");

        // Criar um menu com as opções
        // O número da opção obedece à ordem de 'addOption' começando por 1
        Menu menu = new Menu("Gestão de Endereços");
        menu.addOption("Sair");
        menu.addOption("Criar Carro");
        menu.addOption("Procurar Carros");
        menu.addOption("Mostrar Carro (por N.º Chassis)");
        menu.addOption("Listar Carros (por Marca)");
        menu.addOption("Apagar um Carro");
        menu.addOption("Obter Pessoa");
        menu.addOption("Procurar Pessoas (por nome)");
        menu.addOption("Pergunta m'uma coisa e mostra'mum sub-menu");

        // loop infinito
        while (true) {
            int opcao = menu.print();
            switch (opcao) {
                case 1: // Sair
                    System.out.println("Adeus!");
                    System.exit(0);
                    break;
                case 2:
                    criarCarro();
                    break;
                case 3:
                    procurarCarrosPorMarca();
                    break;
                case 4:
                    mostrarCarro();
                    break;
                case 5:
                    listarCarros();
                    break;
                case 6:
                    apagarCarro();
                    break;
                case 7:
                    obterPessoa();
                    break;
                case 8:
                    procurarPessoaPorNome();
                    break;
                case 9:
                    perguntaMumaCoisaEMostraMumSubMenu();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void perguntaMumaCoisaEMostraMumSubMenu() {
        String coisa = pedirTexto("Uma coisa");

        Menu subMenu = new Menu("Mum Sub Menu");
        subMenu.addOption("Opção 1");
        subMenu.addOption("Opção 2");
        subMenu.addOption("Opção 3");
        subMenu.addOption("Menu Anterior");

        while (true) {
            switch (subMenu.print()) {
                case 1:
                    System.out.println("Ouch 1!");
                    break;
                case 2:
                    System.out.println("Ouch 2!");
                    break;
                case 3:
                    System.out.println("Ouch 3!");
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Opção Inválida");
            }
        }
    }

    private void procurarPessoaPorNome() {
        String nome = pedirTexto("Introduza um Nome");
        List<Pessoa> pessoas = dataManager.obterPessoasPorNome(nome);
        if (pessoas.isEmpty()) {
            System.out.println("AVISO: A pesquisa não devolveu resultados");
        } else {
            // só entra se !pessoas.isEmpty()
            for (Pessoa pessoa : pessoas) {
                printPessoa(pessoa);
            }
        }
    }

    private void obterPessoa() {
        Integer pessoaId = pedirInteiro("ID da Pessoa");
        Pessoa pessoa = dataManager.obterPessoa(pessoaId);
        if (pessoa != null) {
            printPessoa(pessoa);
        } else {
            System.out.println("ERRO: A pessoa não existe");
        }
    }

    private void printPessoa(Pessoa pessoa) {
        System.out.println("===[ Informação do Carro ]=====================");
        System.out.println("= ID da Pessoa: " + pessoa.getPessoaId());
        System.out.println("= Nome: " + pessoa.getNome());
        System.out.println("= Grupo ID: " + pessoa.getGrupoId());
        System.out.println("= Notas: " + pessoa.getNotas());
        System.out.println("= Data Nasc.: " + pessoa.getDataNascimento());
        System.out.println("===============================================");
    }

    private void criarCarro() {
        // Pedir os parametros do carro ao utilizador
        String marca = pedirTexto("Marca");
        String modelo = pedirTexto("Modelo");
        Integer cc = pedirInteiro("CC");
        Integer tara = pedirInteiro("Tara");

        // Criar o objeto do tipo Carro e pedir ao utilizador os seus valores
        Carro carro = new Carro();
        carro.setMarca(marca);
        carro.setModelo(modelo);
        carro.setCc(cc);
        carro.setTara(tara);

        // Criar o carro na BD
        dataManager.criarCarro(carro);

        // Mostrar o carro criado
        System.out.println("--> Foi criado o carro: ");
        printCarro(carro);
    }

    private void procurarCarrosPorMarca() {
        String marca = pedirTexto("Marca");
        List<Carro> carros = dataManager.listaCarrosPorMarca(marca);
        System.out.println("--> Foram encontrados " + carros.size() + " carros:");
        for (Carro carro : carros) {
            printCarro(carro);
        }
    }

    private void mostrarCarro() {
        Integer numChassis = pedirInteiro("N.º Chassis");
        Carro carro = dataManager.obterCarro(numChassis);
        if (carro == null) {
            System.out.println("ERRO: Não foi encontrado nenhum carro com o chassis: " + numChassis);
        } else {
            printCarro(carro);
        }
    }

    private void listarCarros() {
        List<Carro> carros = dataManager.listaCarros();
        System.out.println("--> Foram encontrados " + carros.size() + " carros:");
        for (Carro carro : carros) {
            printCarro(carro);
        }
    }

    private void apagarCarro() {
        Integer numChassis = pedirInteiro("N.º Chassis");
        int numRows = dataManager.apagarCarro(numChassis);
        if (numRows == 0) {
            System.out.println("ERRO: não foi apagado nenhum carro");
        } else {
            System.out.println("O carro foi apagado");
        }
    }

    /**
     * Funções de Ajuda
     */
    private void printCarro(Carro carro) {
        System.out.println("===[ Informação do Carro ]=====================");
        System.out.println("= N.º Chassis: " + carro.getNumChassis());
        System.out.println("= Marca: " + carro.getMarca());
        System.out.println("= Modelo: " + carro.getModelo());
        System.out.println("= CC: " + carro.getCc());
        System.out.println("= Tara: " + carro.getTara());
        System.out.println("===============================================");
    }

    private String readInput() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    private String pedirTexto(String prompt) {
        System.out.print(prompt + ": ");
        String input = readInput();
        if ("".equals(input) || input == null) {
            System.out.println("ERRO: Tem que introduzir um texto!");
            return pedirTexto(prompt);
        } else {
            return input;
        }
    }

    private Integer pedirInteiro(String prompt) {
        System.out.print(prompt + ": ");
        String input = readInput();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("ERRO: O número é inválido " + input);
            return pedirInteiro(prompt);
        }
    }


}
