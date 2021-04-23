package sample;

import java.sql.*;

public class DB {
    private final String HOST = "localhost";
    private final String PORT = "3306";
    private final String DB_NAME = "itproger_java";
    private final String LOGIN = "root";
    private final String PASS = "root";

    private Connection dbConn = null;

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return dbConn;
    }

    public void isConnected() throws SQLException, ClassNotFoundException {
        dbConn = getDbConnection();
        System.out.println(dbConn.isValid(1000));
    }
    public boolean regUser(String login, String email, String password) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO `users` (`login`, `email`, `password`) VALUE(?, ?, ?)";

        Statement statement = getDbConnection().createStatement();
        ResultSet resultSet= statement.executeQuery("SELECT * FROM `users` WHERE `login`='"+ login + "' LIMIT 1");
        if(resultSet.next())
            return false;

        PreparedStatement prSt = getDbConnection().prepareStatement(sql);

        prSt.setString(1, login);
        prSt.setString(2, email);
        prSt.setString(3, password);
        prSt.executeUpdate();
        return true;

    }
    public boolean authUser(String login, String password) throws SQLException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();
        String sql = "SELECT * FROM `users` WHERE `login`='"+ login + "' AND`password`= '" + password +"' LIMIT 1";
        ResultSet resultSet= statement.executeQuery(sql);
        return resultSet.next();

    }

    public ResultSet getArticles() throws SQLException, ClassNotFoundException{
        String sql = "SELECT `title`, `intro` FROM `articles`";
        Statement statement = getDbConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;

    }
    public void addArticle(String intro, String title, String text) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO `articles` (`title`, `intro`, `text`, `views`) VALUE(?, ?, ?, ?)";

        PreparedStatement prSt = getDbConnection().prepareStatement(sql);

        prSt.setString(1, title);
        prSt.setString(2, intro);
        prSt.setString(3, text);
        prSt.setInt(4,15);

        prSt.executeUpdate();


    }

}
