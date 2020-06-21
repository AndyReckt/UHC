package net.saikatsune.uhc.manager;

import net.saikatsune.uhc.Game;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;

public class DatabaseManager {

    private final Game game = Game.getInstance();

    private Connection connection;

    private final String host = game.getConfig().getString("MYSQL.HOST"),
                         database = game.getConfig().getString("MYSQL.DATABASE"),
                         username = game.getConfig().getString("MYSQL.USERNAME"),
                         password = game.getConfig().getString("MYSQL.PASSWORD");

    private final int port = game.getConfig().getInt("MYSQL.PORT");

    public LinkedList<String> top10KillPlayers = new LinkedList<>();
    public LinkedList<String> top10DeathPlayers = new LinkedList<>();
    public LinkedList<String> top10WinPlayers = new LinkedList<>();

    public LinkedHashMap<String, Integer> topKillsHash = new LinkedHashMap<>();

    public void connectToDatabase() throws ClassNotFoundException, SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" +
                    this.port + "/" + this.database + "?autoReconnect=true", this.username, this.password);
        }
    }

    public void disconnectFromDatabase() throws SQLException {
        if(!connection.isClosed()) connection.close();
    }

    public void checkConnection() {
        try {
            if (this.connection == null || !this.connection.isValid(10) || this.connection.isClosed()) connectToDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        this.checkConnection();

        Statement statement = connection.createStatement();

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS STATS(USERNAME VARCHAR(100), UUID VARCHAR(100), " +
                "KILLS VARCHAR(100), DEATHS VARCHAR(100), WINS VARCHAR(100))");
    }

    public boolean isPlayerRegistered(OfflinePlayer player) {
        this.checkConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM STATS WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void registerPlayer(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(game, () -> {
            this.checkConnection();

            try {
                if(!this.isPlayerRegistered(player)) {
                    PreparedStatement statement = connection.prepareStatement("INSERT INTO STATS(USERNAME, UUID, KILLS, DEATHS, WINS) VALUE (?,?,?,?,?)");
                    statement.setString(1, player.getName());
                    statement.setString(2, player.getUniqueId().toString());
                    statement.setInt(3, 0);
                    statement.setInt(4, 0);
                    statement.setInt(5, 0);

                    statement.executeUpdate();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    public int getKills(OfflinePlayer player) {
        this.checkConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM STATS WHERE UUID=?");

            statement.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return resultSet.getInt("KILLS");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getDeaths(OfflinePlayer player) {
        this.checkConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM STATS WHERE UUID=?");

            statement.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return resultSet.getInt("DEATHS");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getWins(OfflinePlayer player) {
        this.checkConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM STATS WHERE UUID=?");

            statement.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return resultSet.getInt("WINS");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void addWins(OfflinePlayer player, int wins) {
        this.checkConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE STATS SET WINS=? WHERE UUID=?");

            statement.setInt(1, getWins(player) + wins);
            statement.setString(2, player.getUniqueId().toString());

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addKills(Player player, int kills) {
        this.checkConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE STATS SET KILLS=? WHERE UUID=?");

            statement.setInt(1, getKills(player) + kills);
            statement.setString(2, player.getUniqueId().toString());

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addDeaths(OfflinePlayer player, int deaths) {
        this.checkConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE STATS SET DEATHS=? WHERE UUID=?");

            statement.setInt(1, getDeaths(player) + deaths);
            statement.setString(2, player.getUniqueId().toString());

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removeDeaths(Player player, int deaths) {
        this.checkConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE STATS SET DEATHS=? WHERE UUID=?");

            statement.setInt(1, getDeaths(player) - deaths);
            statement.setString(2, player.getUniqueId().toString());

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void getTop10Kills() {
        this.checkConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM STATS ORDER BY KILLS DESC LIMIT 10");

            ResultSet resultSet = statement.executeQuery();

            for (int i = 0; i < 10; i++) {
                if(resultSet.next()) {
                    top10KillPlayers.add(resultSet.getString("USERNAME"));
                } else {
                    top10KillPlayers.add("Not available.");
                }
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void getTop10Deaths() {
        this.checkConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM STATS ORDER BY DEATHS DESC LIMIT 0, 10");

            ResultSet resultSet = statement.executeQuery();

            for (int i = 0; i < 10; i++) {
                if(resultSet.next()) {
                    top10DeathPlayers.add(resultSet.getString("USERNAME"));
                } else {
                    top10DeathPlayers.add("Not available.");
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void getTop10Wins() {
        this.checkConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM STATS ORDER BY WINS DESC LIMIT 0, 10");

            ResultSet resultSet = statement.executeQuery();

            for (int i = 0; i < 10; i++) {
                if(resultSet.next()) {
                    top10WinPlayers.add(resultSet.getString("USERNAME"));
                } else {
                    top10WinPlayers.add("Not available.");
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public List<String> getTop10KillPlayers() {
        return top10KillPlayers;
    }

    public List<String> getTop10DeathPlayers() {
        return top10DeathPlayers;
    }

    public List<String> getTop10WinPlayers() {
        return top10WinPlayers;
    }

    public LinkedHashMap<String, Integer> getTopKillsHash() {
        return topKillsHash;
    }
}
