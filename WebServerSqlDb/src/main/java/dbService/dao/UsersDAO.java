package dbService.dao;

import dbService.dataSets.UsersDataSet;
import dbService.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class UsersDAO {

    private Executor executor;

    public UsersDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public UsersDataSet getUserById(long id) throws SQLException {
        return executor.execQuery("select * from users where id=" + id, result -> {
            result.next();
            return new UsersDataSet(result.getLong(1), result.getString(2), result.getString(3));
        });
    }

    public List<UsersDataSet> getAllByName(String name) throws SQLException{
        List<UsersDataSet> dataSetList = new ArrayList<>();
        return executor.execQuery("select * from users where user_name='" + name + "'", resultSet -> {

            while (resultSet.next()) {
                dataSetList.add(new UsersDataSet(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3)));
            }
            return dataSetList;
        });
    }

    public long getUserId(String name) throws SQLException {
        return executor.execQuery("select * from users where user_name='" + name + "'", result -> {
            result.next();
            return result.getLong(1);
        });
    }

    public void insertUser(String name, String password) throws SQLException {
        if (getAllByName(name).stream().noneMatch(u -> u.getName().equals(name) && u.getPassword().equals(password))) {
            executor.execUpdate("insert into users (user_name, user_password) values ('" + name + "','" + password + "')");
        }
    }

    public void createTable() throws SQLException {
        executor.execUpdate("create table if not exists users (id bigint auto_increment, user_name varchar(256), user_password varchar(256), primary key (id))");
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("drop table users");
    }
}
