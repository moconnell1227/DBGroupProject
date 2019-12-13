package dao;

import entity.Room;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;

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

    private BigDecimal getPopScore(String code) {
        BigDecimal popScore = new BigDecimal(0.00);

        LocalDate current_date = LocalDate.now(ZoneId.of("America/Montreal"));

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("SELECT round((SUM(DATEDIFF(CheckOut, CheckIn)))/180, 2) AS popScore FROM Reservations LEFT JOIN Rooms ON Reservations.RoomCode = Rooms.CODE WHERE CheckIn >= DATE(? - INTERVAL 180 DAY) AND CheckOut <= ? AND Rooms.CODE = ?");
            preparedStatement.setDate(1, Date.valueOf(current_date));
            preparedStatement.setDate(2, Date.valueOf(current_date));
            preparedStatement.setString(3, code);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                popScore = popScore.add(resultSet.getBigDecimal("popScore"));
            }
            preparedStatement = this.conn.prepareStatement("SELECT round(DATEDIFF(?, CheckIn)/180, 2) AS popScore FROM Reservations LEFT JOIN Rooms ON Reservations.RoomCode = Rooms.CODE WHERE CheckIn < ? AND CheckOut > ? AND Rooms.CODE = ?");
            preparedStatement.setDate(1, Date.valueOf(current_date));
            preparedStatement.setDate(2, Date.valueOf(current_date));
            preparedStatement.setDate(3, Date.valueOf(current_date));
            preparedStatement.setString(4, code);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                popScore = popScore.add(resultSet.getBigDecimal("popScore"));
            }
            preparedStatement = this.conn.prepareStatement("SELECT round(DATEDIFF(CheckOut, DATE(? - INTERVAL 180 DAY))/180, 2) AS popScore FROM Reservations LEFT JOIN Rooms ON Reservations.RoomCode = Rooms.CODE WHERE CheckOut > DATE(? - INTERVAL 180 DAY) AND CheckIn < DATE(? - INTERVAL 180 DAY) AND Rooms.CODE = ?");
            preparedStatement.setDate(1, Date.valueOf(current_date));
            preparedStatement.setDate(2, Date.valueOf(current_date));
            preparedStatement.setDate(3, Date.valueOf(current_date));
            preparedStatement.setString(4, code);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                popScore = popScore.add(resultSet.getBigDecimal("popScore"));
            }
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
        return popScore;
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

    public Set<Room> getAvailableRooms(String checkin, String checkout, int minOcc, String type, String decor, int maxPrice) {
        Set<Room> availableRooms = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("select * from Rooms where CODE not in (" +
                    "select RoomCode from Reservations where (CheckIn <= ? and CheckOut > ?)" +
                    " or (CheckIn between ? and ?) or (CheckOut between ? and ?))" +
                    " and MaxOcc >= ? and BedType = ? and Decor = ? and BasePrice <= ?");
            preparedStatement.setDate(1, Date.valueOf(checkin));
            preparedStatement.setDate(2, Date.valueOf(checkout));
            preparedStatement.setDate(3, Date.valueOf(checkin));
            preparedStatement.setDate(4, Date.valueOf(checkout));
            preparedStatement.setDate(5, Date.valueOf(checkin));
            preparedStatement.setDate(6, Date.valueOf(checkout));
            preparedStatement.setInt(7, minOcc);
            preparedStatement.setString(8, type);
            preparedStatement.setString(9, decor);
            preparedStatement.setInt(10, maxPrice);

            resultSet = preparedStatement.executeQuery();
            availableRooms = unpackResultSet(resultSet);
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
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return availableRooms;
    }
    public Set<Room> getAvailableRooms(String checkin, String checkout) {
        Set<Room> availableRooms = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("select * from Rooms where CODE not in (" +
                    "select RoomCode from Reservations where (CheckIn <= ? and CheckOut > ?)" +
                    " or (CheckIn between ? and ?) or (CheckOut between ? and ?))");
            preparedStatement.setDate(1, Date.valueOf(checkin));
            preparedStatement.setDate(2, Date.valueOf(checkout));
            preparedStatement.setDate(3, Date.valueOf(checkin));
            preparedStatement.setDate(4, Date.valueOf(checkout));
            preparedStatement.setDate(5, Date.valueOf(checkin));
            preparedStatement.setDate(6, Date.valueOf(checkout));

            resultSet = preparedStatement.executeQuery();
            availableRooms = unpackResultSet(resultSet);
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
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return availableRooms;
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
            reservation.setPopScore(this.getPopScore(reservation.getCode()));
            reservations.add(reservation);
        }
        return reservations;
    }

}