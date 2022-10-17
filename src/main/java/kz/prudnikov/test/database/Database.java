package kz.prudnikov.test.database;

import kz.prudnikov.test.objects.Player;

import java.sql.*;

public class Database {

    static String url;
    static Connection conn;
    static String sql;
    static Statement stmt;
    static PreparedStatement pstmt;
    static ResultSet rs;


    public static void createNewDatabase(String folderForDB ,String fileName) { // for creating new DB in your direction
        url = folderForDB + fileName;
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTables(String folderForDB, String fileName) {
        url = folderForDB + fileName;

         sql = "CREATE TABLE IF NOT EXISTS players (\n"
                + " id integer PRIMARY KEY,\n"
                + " playerName text NOT NULL,\n"
                + " gold INT,\n"
                + " clanName text\n"
                + ");";

        try{
             conn = DriverManager.getConnection(url);
             stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS clans (\n"
                + " id integer PRIMARY KEY,\n"
                + " clanName text NOT NULL,\n"
                + " gold INT\n"
                + ");";

        try{
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS logs (\n"
                + " id integer PRIMARY KEY,\n"
                + " playerName text NOT NULL,\n"
                + " time  text,\n"
                + " action text \n"
                + ");";

        try{
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void insertNewPlayers(String playerName, int gold, String clanName) {
        sql = "INSERT INTO players(playerName, gold, clanName) VALUES(?,?,?)";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, playerName);
            pstmt.setInt(2, gold);
            pstmt.setString(3, clanName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Created name= " + playerName + " gold= " + gold + " clan= " + clanName );
    }

    public static void insertNewClans(String clanName, int gold){
        sql = "INSERT INTO clans(clanName, gold) VALUES(?,?)";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, clanName);
            pstmt.setInt(2, gold);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(" clanName= " + clanName + " gold = " + gold);
    }

    public static Player checkClan(String playerName) {
        ResultSet rs;
        PreparedStatement pstmt;
        Player player = new Player();
        sql = "SELECT clanName, gold FROM players WHERE playerName = ?";

        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, playerName);
            rs = pstmt.executeQuery();
            while (rs.next()){
                player.setClanName(rs.getString("clanName"));
                player.setGold(rs.getInt("gold"));
            }
            pstmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return player;
    }

    public static void putGoldToClanTreasure(Player player, String name){     // players put gold for clan treasure
        sql = "UPDATE clans " +
                "SET gold = ((SELECT gold FROM clans WHERE clanName = ? ) + ? ) " +
                "WHERE clanName = ? ";
        String clanName = player.getClanName();
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, clanName);
            pstmt.setInt(2, (player.getGold()/5));
            pstmt.setString(3, clanName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println( "ERR clan= " + player.getClanName() + " ERRgold= " + player.getGold() + " ERRname= " + name);
            System.out.println(e.getMessage() + " err" );
        }

        sql = "UPDATE players " +
                "SET gold =  ? " +
                "WHERE playerName = ? ";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (player.getGold() - player.getGold()/5 ));
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("clan= " + player.getClanName() + " name= " + name + " gold= " + player.getGold());
    }

    public static void createLogOfActions(String player, String action){

        sql = "INSERT INTO logs(playerName, time, action) VALUES(?,?,?)";
        try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, player);
                pstmt.setString(2, String.valueOf(new Timestamp(System.currentTimeMillis())));
                pstmt.setString(3, action);
                pstmt.executeUpdate();
            } catch(SQLException e){
                System.out.println(e.getMessage());
            }
    }

    public static void putGoldAfterQuest(String playerName, int gold){  // players take gold after quest
        sql = "UPDATE players " +
                "SET gold =  ? " +
                "WHERE playerName = ? ";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, gold);
            pstmt.setString(2, playerName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(" player " + playerName + " add " + gold);
    }

    public static void clearDB(String folderForDB, String fileName){
        url = folderForDB + fileName;
        sql = "DELETE FROM players";
        try{
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            stmt.execute(sql);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        sql = "DELETE FROM clans";
        try{
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            stmt.execute(sql);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        sql = "DELETE FROM logs";
        try{
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            stmt.execute(sql);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

}
