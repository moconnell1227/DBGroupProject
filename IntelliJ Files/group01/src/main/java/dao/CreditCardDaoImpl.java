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
      preparedStatement = this.conn.prepareStatement("SELECT * FROM CreditCard WHERE CardNum = ?");
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();
      Set<CreditCard> creditCards = unpackResultSet(resultSet);
      creditCard = (CreditCard) creditCards.toArray()[0];
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return creditCard;
  }

  @Override
  public Set<CreditCard> getAll() {
    Set<CreditCard> creditCards = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      preparedStatement = this.conn.prepareStatement("SELECT * FROM CreditCard");
      resultSet = preparedStatement.executeQuery();
      creditCards = unpackResultSet(resultSet);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return creditCards;
  }

  @Override
  public Integer insert(CreditCard obj) {
    Integer cardNumber = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      preparedStatement = this.conn.prepareStatement(
        "INSERT INTO CreditCard (CardNum, Balance, CardLimit, OwnerID) VALUES (?, ?, ?, ?)");
      preparedStatement.setInt(1, obj.getCardNum());
      preparedStatement.setFloat(2, obj.getCardLimit());
      preparedStatement.setFloat(3, obj.getBalance());
      preparedStatement.setInt(4, obj.getOwnerId());
      int numRows = preparedStatement.executeUpdate();
      if (numRows == 1) {
        // get generated id
        resultSet = preparedStatement.getGeneratedKeys();
        if(resultSet.next())
          cardNumber = resultSet.getInt(1);

      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally{
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
    return cardNumber;
  }

  @Override
  public Integer update(CreditCard obj) {
    Integer numRows = 0;
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = this.conn.prepareStatement(
        "UPDATE CreditCard SET Balance = ?, CardLimit = ?, OwnerID = ? WHERE CardNum = ?",
        Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setFloat(1, obj.getBalance());
      preparedStatement.setFloat(2, obj.getCardLimit());
      preparedStatement.setInt(3, obj.getOwnerId());
      preparedStatement.setInt(4, obj.getCardNum());
      numRows = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally{
      try {
        if (preparedStatement != null)
          preparedStatement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return numRows;
  }

  @Override
  public Integer delete(CreditCard obj) {
    Integer numRows = 0;
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = this.conn.prepareStatement(
        "DELETE FROM CreditCard WHERE CardNum = ?");
      preparedStatement.setInt(1, obj.getCardNum());
      numRows = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally{
      try {
        if (preparedStatement != null)
          preparedStatement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return numRows;
  }

  private Set<CreditCard> unpackResultSet(ResultSet rs) throws SQLException {
    Set<CreditCard> creditCards = new HashSet<CreditCard>();

    while(rs.next()) {
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
