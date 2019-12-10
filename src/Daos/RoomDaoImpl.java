package Daos;

import Daos.Dao;
import Models.Room;

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
    public Room getById(int id) {
        Room room = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("SELECT * FROM Rooms WHERE CODE = ?");
            preparedStatement.setInt(1, id);
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
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return room;
    }
    
    private BigDecimal getPopScore(String code) {
        BigDecimal popScore = new BigDecimal(0.00);
        String today = "2010-09-05";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = this.conn.prepareStatement("SELECT round((SUM(DATEDIFF(CheckOut, CheckIn)))/180, 2) AS popScore FROM Reservations LEFT JOIN Rooms ON Reservations.RoomCode = Rooms.CODE WHERE CheckIn >= DATE(? - INTERVAL 180 DAY) AND CheckOut <= ? AND Rooms.CODE = ?");
            preparedStatement.setString(1, today);
            preparedStatement.setString(2, today);
            preparedStatement.setString(3, code);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                popScore = popScore.add(resultSet.getBigDecimal("popScore"));
            }
            preparedStatement = this.conn.prepareStatement("SELECT round(DATEDIFF(?, CheckIn)/180, 2) AS popScore FROM Reservations LEFT JOIN Rooms ON Reservations.RoomCode = Rooms.CODE WHERE CheckIn < ? AND CheckOut > ? AND Rooms.CODE = ?");
            preparedStatement.setString(1, today);
            preparedStatement.setString(2, today);
            preparedStatement.setString(3, today);
            preparedStatement.setString(4, code);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                popScore = popScore.add(resultSet.getBigDecimal("popScore"));
            }
            preparedStatement = this.conn.prepareStatement("SELECT round(DATEDIFF(CheckOut, DATE(? - INTERVAL 180 DAY))/180, 2) AS popScore FROM Reservations LEFT JOIN Rooms ON Reservations.RoomCode = Rooms.CODE WHERE CheckOut > DATE(? - INTERVAL 180 DAY) AND CheckIn < DATE(? - INTERVAL 180 DAY) AND Rooms.CODE = ?");
            preparedStatement.setString(1, today);
            preparedStatement.setString(2, today);
            preparedStatement.setString(3, today);
            preparedStatement.setString(4, code);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                popScore = popScore.add(resultSet.getBigDecimal("popScore"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if (resultSet != null)
                    resultSet.close();
            } catch(SQLException e){
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
    
    public Set<Room> getByDecor(String decor) {
        Set<Room> rooms = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("Select * FROM Rooms WHERE decor = ?");
            preparedStatement.setString(1, decor);
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

    public Set<Room> getByBedType(String bedType) {
        Set<Room> rooms = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("Select * FROM Rooms WHERE bedType = ?");
            preparedStatement.setString(1, bedType);
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

    public Set<Room> getByPriceRange(Integer min, Integer max) {
        Set<Room> rooms = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("Select * FROM Rooms WHERE basePrice >= ? AND basePrice <= ?");
            preparedStatement.setInt(1, min);
            preparedStatement.setInt(1, max);
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

    public Set<Room> getByOccupancy(Integer occupants) {
        Set<Room> rooms = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("Select * FROM Rooms WHERE maxOcc >= ?");
            preparedStatement.setInt(1, occupants);
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

    public Set<Room> getByNumBeds(Integer numBeds) {
        Set<Room> rooms = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("Select * FROM Rooms WHERE Num_Beds = ?");
            preparedStatement.setInt(1, numBeds);
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

    @Override
    public Boolean insert(Room obj) {
        return null;
    }

    @Override
    public Boolean update(Room obj) {
        return null;
    }

    @Override
    public Boolean delete(Room obj) {
        return null;
    }

    @Override
    public Set<Room> unpackResultSet(ResultSet rs) throws SQLException {
        return null;
    }

    public String getMaxNumberOfOccupants(String roomName) {
        Room foundRoom = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("SELECT * FROM Rooms WHERE roomName = ?");
            preparedStatement.setString(1, roomName);
            resultSet = preparedStatement.executeQuery();
            Set<Room> rooms = unpackResultSet(resultSet);
            foundRoom = (Room) rooms.toArray()[0];
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
        return room;
    }

}
