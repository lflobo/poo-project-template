package pt.ipb.poo.projeto.data;

import com.mysql.cj.jdbc.MysqlDataSource;
import pt.ipb.poo.projeto.database.Carro;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DataManager {

    private DataSource database;

    public DataManager(String hostname, String database, String username, String password) {
        MysqlDataSource dataSource = new MysqlDataSource();
        String url = String.format("jdbc:mysql://%s:3306/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", hostname, database);
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        this.database = dataSource;
    }

    /**
     * Extrai a informação de um resultSet resultante de um query à tabela CARRO e contrói um
     * objeto do tipo carro com os dados
     *
     * @param resultSet O resultSet
     * @return O carro
     */
    public Carro resultSetToCarro(ResultSet resultSet) throws SQLException {
        Carro carro = new Carro();
        carro.setNumChassis(resultSet.getInt("num_chassis"));
        carro.setMarca(resultSet.getString("marca"));
        carro.setModelo(resultSet.getString("modelo"));
        carro.setCc(resultSet.getInt("cc"));
        carro.setTara(resultSet.getInt("tara"));
        return carro;
    }

    /**
     * Obter um carro usando a chave primária (numChassis)
     *
     * @param numChassis O número do chassis
     * @return O carro (se existir)
     */
    public Carro obterCarro(Integer numChassis) {
        try (Connection con = database.getConnection()) {
            String query = "SELECT num_chassis, marca, modelo, cc, tara FROM carro WHERE num_chassis = ?";
            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setInt(1, numChassis);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) // se foi encontrado um registo
                        return resultSetToCarro(resultSet);
                    else
                        return null; // não foi encontrado
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
            return null; // sem efeito
        }
    }

    /**
     * Procura na tabela CARRO todos os carros com a marca passada como argumento
     *
     * @param marca A marca de carros a procurar
     * @return A lista de carros da marca
     */
    public List<Carro> listaCarrosPorMarca(String marca) {
        try (Connection con = database.getConnection()) {
            String query = "SELECT num_chassis, marca, modelo, cc, tara FROM carro WHERE marca = ?";
            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setString(1, marca);
                try (ResultSet result = statement.executeQuery()) {
                    List<Carro> testList = new ArrayList<>();
                    while (result.next()) {
                        testList.add(resultSetToCarro(result));
                    }
                    return testList;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
            return null; // sem efeito
        }
    }


    /**
     * @return A lista de todos os carros
     */
    public List<Carro> listaCarros() {
        try (Connection con = database.getConnection()) {
            String query = "SELECT num_chassis, marca, modelo, cc, tara FROM carro";
            try (PreparedStatement statement = con.prepareStatement(query)) {
                try (ResultSet result = statement.executeQuery()) {
                    List<Carro> testList = new ArrayList<>();
                    while (result.next()) {
                        testList.add(resultSetToCarro(result));
                    }
                    return testList;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
            return null; // sem efeito
        }
    }


    /**
     * Cria um carro
     *
     * @param carro O objeto do tipo carro contendo os dados para preencher a tabela
     * @note O valor numChassis é preenchido com o valor gerado (auto increment) da base de dados
     */
    public void criarCarro(Carro carro) {
        try (Connection con = database.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement("INSERT INTO carro(marca, modelo, cc, tara) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, carro.getMarca());
                statement.setString(2, carro.getModelo());
                statement.setInt(3, carro.getCc());
                statement.setInt(4, carro.getTara());
                statement.executeUpdate(); // executeUpdate deve ser usado em vez de execute quando vamos alterar alguma coisa na BD
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        carro.setNumChassis(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Permite apagar um carro com base no número do chassis
     *
     * @param numChassis O número do chassis
     * @return O número de registos modificados
     */
    public int apagarCarro(Integer numChassis) {
        try (Connection con = database.getConnection()) {
            String query = "DELETE FROM carro WHERE num_chassis = ?";
            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setInt(1, numChassis);
                return statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
            return 0;
        }
    }

    /**
     * Atualizar um carro
     *
     * @param carro O carro a atualizar
     * @note Não permite alterar o numChassis que serve de chave para o update
     */
    public void atualizarModelo(Carro carro) {
        try (Connection con = database.getConnection()) {
            String query = "UPDATE carro SET marca=?, modelo=?, cc=?, tara=? WHERE num_chassis = ?";
            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setString(1, carro.getMarca());
                statement.setString(2, carro.getModelo());
                statement.setInt(3, carro.getCc());
                statement.setInt(4, carro.getTara());
                statement.setInt(5, carro.getNumChassis());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
