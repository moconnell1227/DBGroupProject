package dao;

import entity.MonthlyRevenue;
import entity.Reservation;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class ReservationDaoImpl implements Dao<Reservation> {
    private Connection conn;

    public ReservationDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Reservation getById(int id) {
        Reservation reservation = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("SELECT * FROM Reservations WHERE rID=?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            Set<Reservation> reservations = unpackResultSet(resultSet);
            reservation = (Reservation) reservations.toArray()[0];
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
        return reservation;
    }

    public void displayRevenues() {
        Set<MonthlyRevenue> revenues = getRevenue();
        int totalRev = getTotalRevenue(revenues);
        Map<String, int[]> roomRevenues = createRoomRevenues(revenues);
        for (Map.Entry<String, int[]> room : roomRevenues.entrySet()) {
            System.out.print("Room: " + room.getKey());
            for (int i = 1; i < 12; i++) {
                System.out.print(", Month: " + i + ", Revenue: " + room.getValue()[i - 1]);
            }
            System.out.println(", Total Revenue: " + totalRev);
        }
    }

    public Map<String, int[]> createRoomRevenues(Set<MonthlyRevenue> revenues) {
        Map<String, int[]> roomRevenues = new HashMap<String, int[]>();

        for (MonthlyRevenue r : revenues) {
            if (roomRevenues.containsKey(r.getRoom())) {
                roomRevenues.get(r.getRoom())[r.getMonth() - 1] = r.getRevenue();
            } else {
                roomRevenues.put(r.getRoom(), new int[12]);
                roomRevenues.get(r.getRoom())[r.getMonth() - 1] = r.getRevenue();
            }
        }
        return roomRevenues;
    }

    public int getTotalRevenue(Set<MonthlyRevenue> revenues) {
        int total = 0;
        for (MonthlyRevenue r : revenues) {
            total += r.getRevenue();
        }
        return total;
    }

    public Set<MonthlyRevenue> getRevenue() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<MonthlyRevenue> revenues = null;
        try {
            preparedStatement = this.conn.prepareStatement("SELECT RoomCode, month(CheckIn) as month, " +
                    "count(RoomCode) as c, " +
                    "round(sum(datediff(checkout, checkin)*rate), 2) as r " +
                    "from Reservations " +
                    "group by RoomCode, month " +
                    "order by RoomCode, month");
            resultSet = preparedStatement.executeQuery();
            revenues = unpackResultSetRevenue(resultSet);
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
        return revenues;
    }


    @Override
    public Set<Reservation> getAll() {
        Set<Reservation> reservations = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("SELECT * FROM Reservations");
            resultSet = preparedStatement.executeQuery();
            reservations = unpackResultSet(resultSet);
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
        return reservations;
    }

    @Override
    public Boolean insert(Reservation obj) {
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(
                    "INSERT INTO Reservations (CheckIn, CheckOut, Rate, NumOcc, RoomCode, CustomerId, CardNum) VALUES (?,?,?,?,?,?,?)"
            );
            preparedStatement.setString(1, obj.getCheckIn());
            preparedStatement.setString(2, obj.getCheckOut());
            preparedStatement.setFloat(3, obj.getRate());
            preparedStatement.setInt(4, obj.getNumOcc());
            preparedStatement.setString(5, obj.getRoomCode());
            preparedStatement.setInt(6, obj.getCustomerId());
            preparedStatement.setInt(7, obj.getCardNum());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Boolean update(Reservation obj) {
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(
                    "UPDATE Reservation SET CheckIn=?, CheckOut=?, Rate=?, NumOcc=?, " +
                            "RoomCode=?, CustomerId=?, CardNum=? WHERE rID=?");
            preparedStatement.setString(1, obj.getCheckIn());
            preparedStatement.setString(2, obj.getCheckOut());
            preparedStatement.setFloat(3, obj.getRate());
            preparedStatement.setInt(4, obj.getNumOcc());
            preparedStatement.setString(5, obj.getRoomCode());
            preparedStatement.setInt(5, obj.getCustomerId());
            preparedStatement.setInt(5, obj.getCardNum());
            preparedStatement.setInt(5, obj.getrID());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean delete(Reservation obj) {
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(
                    "DELETE FROM Reservations WHERE rID=?"
            );
            preparedStatement.setInt(1, obj.getrID());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Set<Reservation> unpackResultSet(ResultSet rs) throws SQLException {
        Set<Reservation> reservations = new HashSet<Reservation>();

        while (rs.next()) {
            Reservation reservation = new Reservation(
                    rs.getInt("rID"),
                    rs.getString("CheckIn"),
                    rs.getString("CheckOut"),
                    rs.getFloat("Rate"),
                    rs.getInt("NumOcc"),
                    rs.getString("RoomCode"),
                    rs.getInt("CustomerId"),
                    rs.getInt("CardNum"));
            reservations.add(reservation);
        }
        return reservations;
    }

    private Set<MonthlyRevenue> unpackResultSetRevenue(ResultSet rs) throws SQLException {
        Set<MonthlyRevenue> revenues = new HashSet<MonthlyRevenue>();

        while (rs.next()) {
            MonthlyRevenue monthlyRevenue = new MonthlyRevenue(
                    rs.getInt("month"),
                    rs.getString("RoomCode"),
                    rs.getInt("r")
            );
            revenues.add(monthlyRevenue);
        }
        return revenues;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
