package pt.ipb.poo.projeto.data;

import com.mysql.cj.jdbc.MysqlDataSource;
import pt.ipb.poo.projeto.database.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DataManager {

    private DataSource database;

    public DataManager() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/trabalho?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSource.setUser("root");
        dataSource.setPassword("1234");
        this.database = dataSource;
    }

    public List<Test> searchTestList(String search) {
        try (Connection con = database.getConnection()) {
            try (PreparedStatement statement =
                         con.prepareStatement("SELECT id, text FROM test WHERE text LIKE ?")) {
                statement.setString(1, "%" + search + "%");
                try (ResultSet result = statement.executeQuery()) {
                    List<Test> testList = new ArrayList<>();
                    while (result.next()) {
                        int id = result.getInt("id");
                        String text = result.getString("text");

                        Test test = new Test();
                        test.setId(id);
                        test.setText(text);

                        testList.add(test);
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


    public List<Test> getTestList() {
        try (Connection con = database.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement("SELECT id, text FROM test")) {
                try (ResultSet result = statement.executeQuery()) {
                    List<Test> testList = new ArrayList<>();
                    while (result.next()) {
                        int id = result.getInt("id");
                        String text = result.getString("text");

                        Test test = new Test();
                        test.setId(id);
                        test.setText(text);

                        testList.add(test);
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


    public void insert(int id) {
        try (Connection con = database.getConnection()) {
            try (PreparedStatement statement =
                         con.prepareStatement("INSERT INTO test(id) values(?)")) {
                statement.setInt(1, id);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
