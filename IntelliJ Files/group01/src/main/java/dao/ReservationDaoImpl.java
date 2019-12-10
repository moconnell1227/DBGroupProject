package dao;

import entity.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

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
                    "INSERT INTO Reservations (CheckIn, CheckOut, Rate, " +
                            "NumOcc, RoomCode, CustomerId, CardNum) VALUES (?,?,?,?,?,?,?)"
            );
            preparedStatement.setString(1, obj.getCheckIn());
            preparedStatement.setString(2, obj.getCheckOut());
            preparedStatement.setFloat(3, obj.getRate());
            preparedStatement.setInt(4, obj.getNumOcc());
            preparedStatement.setString(5, obj.getRoomCode());
            preparedStatement.setInt(6, obj.getCustomerId());
            preparedStatement.setInt(7, obj.getCardNum());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
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
            preparedStatement.setInt(5, obj.getRID());
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
            PreparedStatement prepareStatement = this.conn.prepareStatement(
                    "DELETE FROM Reservations WHERE rID=?"
            );
            prepareStatement.setInt(1, obj.getRID());
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


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
