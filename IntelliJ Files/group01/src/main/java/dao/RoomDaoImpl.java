package edu.calpoly.csc365.group01.dao;

import edu.calpoly.csc365.group01.entity.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class RoomDaoImpl implements Dao<Room> {
	private Connection conn;

	public RoomDaoImpl(Connection conn){
		this.conn = conn;
	}

	public Room getById(int id){
		Room room = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = this.conn.prepareStatement("SELECT * FROM Rooms WHERE CODE = ?");
			preparedStatement.setString(1, id);
			resultSet = preparedStatement.executeQuery();
			Set<Room> rooms = unpackResultSet(resultSet);
			room = (Room)rooms.toArray()[0];
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null)
					resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(preparedStatement == null)
					preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return room;
	}

	public Set<Room> getByDecor(String decor){
		Set<Room> rooms = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			preparedStatement = this.conn.prepareStatement("Select * FROM Rooms WHERE decor = ?");
			preparedStatement.setString(1, decor);
			resultSet = preparedStatement.executeQuery();
			rooms = unpackResultSet(resultSet);
		} catch(SQLException e){
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null)
					resultSet.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			try {
				if(preparedStatement != null){
					preparedStatement.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return rooms;
	}

	public Set<Room> getByBedType(String bedType){
		Set<Room> rooms = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			preparedStatement = this.conn.prepareStatement("Select * FROM Rooms WHERE bedType = ?");
			preparedStatement.setString(1, bedType);
			resultSet = preparedStatement.executeQuery();
			rooms = unpackResultSet(resultSet);
		} catch(SQLException e){
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null)
					resultSet.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			try {
				if(preparedStatement != null){
					preparedStatement.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return rooms;
	}

	public Set<Room> getByPriceRange(Integer min, Integer max){
		Set<Room> rooms = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sMin = Integer.toString(min);
		String sMax = Integer.toString(max);
		try{
			preparedStatement = this.conn.prepareStatement("Select * FROM Rooms WHERE basePrice >= ? AND basePrice <= ?");
			preparedStatement.setString(1, sMin);
			preparedStatement.setString(1, sMax);
			resultSet = preparedStatement.executeQuery();
			rooms = unpackResultSet(resultSet);
		} catch(SQLException e){
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null)
					resultSet.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			try {
				if(preparedStatement != null){
					preparedStatement.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return rooms;
	}

	public Set<Room> getByOccupancy(Integer occupants){
		Set<Room> rooms = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sOccupants = Integer.toString(occupants);
		try{
			preparedStatement = this.conn.prepareStatement("Select * FROM Rooms WHERE maxOcc >= ?");
			preparedStatement.setString(1, sOccupants);
			resultSet = preparedStatement.executeQuery();
			rooms = unpackResultSet(resultSet);
		} catch(SQLException e){
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null)
					resultSet.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			try {
				if(preparedStatement != null){
					preparedStatement.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return rooms;
	}

	public Set<Room> getByNumBeds(Integer numBeds){
		Set<Room> rooms = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sNumBeds = Integer.toString(numBeds);
		try{
			preparedStatement = this.conn.prepareStatement("Select * FROM Rooms WHERE Num_Beds = ?");
			preparedStatement.setString(1, sNumBeds);
			resultSet = preparedStatement.executeQuery();
			rooms = unpackResultSet(resultSet);
		} catch(SQLException e){
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null)
					resultSet.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			try {
				if(preparedStatement != null){
					preparedStatement.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return rooms;
	}
	
	public Set<Room> getAll() {
		Set<Room> rooms = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			preparedStatement = this.conn.prepareStatement("SELECT * FROM Rooms");
			resultSet = preparedStatement.executeQuery();
			rooms = unpackResultSet(resultSet);
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null)
					resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(preparedStatement != null){
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

		while(rs.next()) {
			Room reservation = new Room(
					rs.getString("roomID"),
					rs.getString("roomName"),
					rs.getInt("beds"),
					rs.getString("bedType"),
					rs.getInt("maxOccupancy"),
					rs.getFloat("basePrice"),
					rs.getString("decor"));
			reservations.add(reservation);
		}
		return reservations;
	}

}