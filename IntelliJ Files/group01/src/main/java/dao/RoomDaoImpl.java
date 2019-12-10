package dao;

import entity.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class RoomDaoImpl implements Dao<Room> {
    private Connection conn;

    public RoomDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Boolean insert(Room obj) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = this.conn.prepareStatement(
                    "INSERT INTO Rooms (Name, Num_Beds, BedType, MaxOCC, BasePrice, Decor) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, obj.getRoomName());
            preparedStatement.setInt(2, obj.getMaxOccupancy());
            preparedStatement.setString(3, obj.getBedType());
            preparedStatement.setInt(4, obj.getMaxOccupancy());
            preparedStatement.setInt(5, obj.getBasePrice());
            preparedStatement.setString(6, obj.getDecor());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public Boolean update(Room obj) {
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(
                    "UPDATE Rooms SET Name=?, Num_Beds=?, BedType=?, MaxOcc=?, " +
                            "BasePrice=?, Decor=?, WHERE CODE=?");
            preparedStatement.setString(1, obj.getRoomName());
            preparedStatement.setInt(2, obj.getBeds());
            preparedStatement.setString(3, obj.getBedType());
            preparedStatement.setInt(4, obj.getMaxOccupancy());
            preparedStatement.setInt(5, obj.getBasePrice());
            preparedStatement.setString(6, obj.getDecor());
            preparedStatement.setString(7, obj.getCode());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean delete(Room obj) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = this.conn.prepareStatement(
                    "DELETE FROM Rooms WHERE CODE = ?");
            preparedStatement.setString(1, obj.getCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public Room getById(int id) {
        return null;
    }

    public Room getByCode(String code) {
        Room room = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("SELECT * FROM Rooms WHERE CODE = ?");
            preparedStatement.setString(1, code);
            resultSet = preparedStatement.executeQuery();
            Set<Room> rooms = unpackResultSet(resultSet);
            room = (Room) rooms.toArray()[0];
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (preparedStatement == null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return room;
    }

    public int getNumOccupantsOfRoom(String code) {
        Room room = this.getByCode(code);
        if (room == null) {
            return -1;
        }
        return room.getMaxOccupancy();
    }

    @Override
    public Set<Room> getAll() {
        Set<Room> rooms = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("SELECT * FROM Rooms");
            resultSet = preparedStatement.executeQuery();
            rooms = unpackResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rooms;
    }


    private Set<Room> unpackResultSet(ResultSet rs) throws SQLException {
        Set<Room> reservations = new HashSet<Room>();

        while (rs.next()) {
            Room reservation = new Room(
                    rs.getString("CODE"),
                    rs.getString("Name"),
                    rs.getInt("Num_Beds"),
                    rs.getString("BedType"),
                    rs.getInt("MaxOcc"),
                    rs.getInt("BasePrice"),
                    rs.getString("Decor"));
            reservations.add(reservation);
        }
        return reservations;
    }

}