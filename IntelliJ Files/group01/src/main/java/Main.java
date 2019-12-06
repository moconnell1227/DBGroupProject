import dao.Dao;
import dao.DaoManager;
import dao.ConnectionFactory;
import entity.Customer;

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
            Dao<Customer> customerDao = daoManager.getCustomerDao();
            Set<Customer> customers = customerDao.getAll();
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
