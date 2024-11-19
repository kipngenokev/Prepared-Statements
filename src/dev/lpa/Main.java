package dev.lpa;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    private static String ARTIST_INSERT =
            "INSERT INTO music.artists (artist_name) VALUES (?)";
    private static String ALBUM_INSERT =
            "INSERT INTO music.albums (artist_id, album_name) VALUES (?, ?)";
    private static String SONG_INSERT =
            "INSERT INTO music.songs (album_id, track_number, song_title) "+
                    "VALUES (? ,? ,? )";

    public static void main(String[] args) {
        var dataSource = new MysqlDataSource();

        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("music");

        try {
            dataSource.setContinueBatchOnError(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try(Connection connection = dataSource.getConnection(
                System.getenv("MYSQLUSER"),
                System.getenv("MYSQLPASS"));
        ) {
            String sql = "SELECT *FROM music.albumview WHERE artist_name =?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,"Elf");
            ResultSet resultSet = ps.executeQuery();
            printRecords(resultSet);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean printRecords(ResultSet resultSet) throws SQLException{
        boolean foundData=false;
        var meta = resultSet.getMetaData();

        System.out.println("==============");

        for(int i=1;i<= meta.getColumnCount();i++) {
            System.out.printf("%-15s",meta.getColumnName(i));
        }
        System.out.println();
        while(resultSet.next()) {
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                System.out.printf("%-15s", resultSet.getString(i));
            }
            System.out.println();
            foundData = true;
        }
        return foundData;
    }

    private static int addArtist(PreparedStatement ps, Connection conn,
                                 String artistName) throws SQLException{
        int artistId = -1;
        ps.setString(1,artistName);
        int insertCount =ps.executeUpdate();
        if(insertCount > 0) {
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()) {
                artistId = generatedKeys.getInt(1);
                System.out.println("Auto-incremented ID:"+artistId);
            }
        }
        return artistId;
    }
    private static int addAlbum(PreparedStatement ps, Connection conn,String artistId
                                 String albumName) throws SQLException{
        int artistId = -1;
        ps.setString(1,artistName);
        int insertCount =ps.executeUpdate();
        if(insertCount > 0) {
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()) {
                artistId = generatedKeys.getInt(1);
                System.out.println("Auto-incremented ID:"+artistId);
            }
        }
        return artistId;
    }
}
