package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.slf4j.*;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl extends Util implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDaoJDBCImpl.class);
    public UserDaoJDBCImpl() {

    }

    private final Connection connection = getConnection();

    public void createUsersTable() {

        try (Statement statement = connection.createStatement()) {
            statement.execute(" CREATE TABLE IF NOT EXISTS user (\n" +
                    " id BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    " name varchar(25),\n" +
                    " lastname varchar(25),\n" +
                    " age DOUBLE,\n" +
                    " PRIMARY KEY (id)\n" +
                    " ); ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate("DROP TABLE IF EXISTS user;");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {

        try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO user (name, lastname, age) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            log.info("User с именем — {} добавлен в базу данных", name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("DELETE FROM user where id = ?")){

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<User> getAllUsers() {
        List<User> allusers = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM user")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                allusers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allusers;
    }

    public void cleanUsersTable() {

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("TRUNCATE TABLE user;")){
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
