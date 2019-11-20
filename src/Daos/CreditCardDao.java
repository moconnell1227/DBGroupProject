package Daos;

import Models.CreditCard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class CreditCardDao implements Dao<CreditCard> {
    private Connection conn;

    public CreditCardDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public CreditCard getById(int id) {
        CreditCard creditCard = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        try {
            preparedStatement = this.conn.prepareStatement("SELECT * FROM Customer WHERE id=?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            Set<CreditCard> creditCards = unpackResultSet(resultSet);

            if (creditCards.size() > 1) {
                System.out.println("ERROR: MORE THAN ONE CC RETURNED FOR A CC NUM. SHOULDN'T HAPPEN!!");
                System.exit(1);
            }

            creditCard = (CreditCard) creditCards.toArray()[0];
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return creditCard;
    }

    @Override
    public Set<CreditCard> getAll() {
        Set<CreditCard> creditCards = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        try {
            preparedStatement = this.conn.prepareStatement("SELECT * FROM Customer");
            resultSet = preparedStatement.executeQuery();
            creditCards = unpackResultSet(resultSet);
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return creditCards;
    }

    @Override
    public Boolean insert(CreditCard creditCard) {
        PreparedStatement preparedStatement;

        try {
            preparedStatement = this.conn.prepareStatement("INSERT INTO CreditCards (Balance, CardLimit, OwnerID) "
                    + "VALUES (?, ?, ?)");
            preparedStatement.setFloat(1, creditCard.getBalance());
            preparedStatement.setFloat(1, creditCard.getCardLimit());
            preparedStatement.setInt(1, creditCard.getOwnerId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Boolean update(CreditCard creditCard) {
        PreparedStatement preparedStatement;

        try {
            preparedStatement = this.conn.prepareStatement(
                    "UPDATE CreditCards SET Balance=?, CardLimit=?, OwnerID=? WHERE CardNum=?");

            preparedStatement.setFloat(1, creditCard.getBalance());
            preparedStatement.setFloat(1, creditCard.getCardLimit());
            preparedStatement.setInt(1, creditCard.getOwnerId());
            preparedStatement.setInt(1, creditCard.getCardNum());

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Boolean delete(CreditCard creditCard) {
        PreparedStatement preparedStatement;

        try {
            preparedStatement = this.conn.prepareStatement("DELETE FROM CreditCards WHERE CardNum=?");
            preparedStatement.setInt(1, creditCard.getCardNum());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Set<CreditCard> unpackResultSet(ResultSet rs) throws SQLException {
        Set<CreditCard> creditCards = new HashSet<>();

        while (rs.next()) {
            creditCards.add(new CreditCard(
                    rs.getInt("CardNum"),
                    rs.getFloat("Balance"),
                    rs.getFloat("CardLimit"),
                    rs.getInt("OwnerID")
            ));
        }

        return creditCards;
    }
}
