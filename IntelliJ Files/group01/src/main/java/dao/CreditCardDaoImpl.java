package dao;

import entity.CreditCard;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CreditCardDaoImpl implements Dao<CreditCard> {

    private Connection conn;

    public CreditCardDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public CreditCard getById(int id) {
        CreditCard creditCard = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("SELECT * FROM CreditCards WHERE CardNum = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            Set<CreditCard> creditCards = unpackResultSet(resultSet);
            creditCard = (CreditCard) creditCards.toArray()[0];
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return creditCard;
    }

    public Set<CreditCard> getAllForCustomer(int cid) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("SELECT * FROM CreditCards WHERE OwnerID = ?");
            preparedStatement.setInt(1, cid);
            resultSet = preparedStatement.executeQuery();
            Set<CreditCard> creditCards = unpackResultSet(resultSet);
            if (creditCards.size() == 0) {
                return null;
            }
            return creditCards;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<CreditCard> getAll() {
        Set<CreditCard> creditCards = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.conn.prepareStatement("SELECT * FROM CreditCards");
            resultSet = preparedStatement.executeQuery();
            creditCards = unpackResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return creditCards;
    }

    @Override
    public Boolean insert(CreditCard obj) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = this.conn.prepareStatement(
                    "INSERT INTO CreditCards (Balance, CardLimit, OwnerID) VALUES (?, ?, ?)");
            preparedStatement.setFloat(1, obj.getBalance());
            preparedStatement.setFloat(2, obj.getCardLimit());
            preparedStatement.setInt(3, obj.getOwnerId());
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
    public Boolean update(CreditCard obj) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = this.conn.prepareStatement(
                    "UPDATE CreditCards SET Balance = ?, CardLimit = ?, OwnerID = ? WHERE CardNum = ?",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setFloat(1, obj.getBalance());
            preparedStatement.setFloat(2, obj.getCardLimit());
            preparedStatement.setInt(3, obj.getOwnerId());
            preparedStatement.setInt(4, obj.getCardNum());
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
    public Boolean delete(CreditCard obj) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = this.conn.prepareStatement(
                    "DELETE FROM CreditCards WHERE CardNum = ?");
            preparedStatement.setInt(1, obj.getCardNum());
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

    private Set<CreditCard> unpackResultSet(ResultSet rs) throws SQLException {
        Set<CreditCard> creditCards = new HashSet<CreditCard>();

        while (rs.next()) {
            CreditCard creditCard = new CreditCard(
                    rs.getInt("CardNum"),
                    rs.getFloat("Balance"),
                    rs.getFloat("CardLimit"),
                    rs.getInt("OwnerID"));
            creditCards.add(creditCard);
        }
        return creditCards;
    }
}
