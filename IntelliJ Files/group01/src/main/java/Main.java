import dao.Dao;
import dao.DaoManager;
import dao.ConnectionFactory;
import dao.RoomDaoImpl;
import entity.CreditCard;
import entity.Customer;
import entity.Reservation;
import entity.Room;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

public class Main {
    public static void main(String args[]) {
        String credFile = "./src/main/java/credential.xml";
        Properties properties = new Properties();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(credFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            properties.loadFromXML(fis);
            DaoManager daoManager = new DaoManager(new ConnectionFactory(
                    properties.getProperty("driver"),
                    properties.getProperty("url"),
                    properties.getProperty("user"),
                    properties.getProperty("pass")
            ));
            RoomDaoImpl reservationDao = (RoomDaoImpl) daoManager.getRoomDao();
            Set<Room> reservations = reservationDao.getAvailableRooms("2010-01-01", "2010-01-10", 2);
            for (Room reservation : reservations) {
                System.out.println(reservation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
