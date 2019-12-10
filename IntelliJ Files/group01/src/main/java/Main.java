import dao.*;
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


import java.util.Scanner;

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
            Dao<CreditCard> dao_for_card = daoManager.getCreditCardDao();
            CreditCardDaoImpl ccDao = (CreditCardDaoImpl) dao_for_card;
            Set<CreditCard> creditcards = ccDao.getAll();
            for (CreditCard card : creditcards) {
                System.out.println(card);
            }

            Dao<Customer> dao_for_cust = daoManager.getCustomerDao();
            CustomerDaoImpl custDao = (CustomerDaoImpl) dao_for_cust;
            Set<Customer> customers = custDao.getAll();
            for (Customer cust : customers) {
                System.out.println(cust);
            }

            Dao<Reservation> dao_for_res = daoManager.getReservationDao();
            ReservationDaoImpl resDao = (ReservationDaoImpl) dao_for_res;
            Set<Reservation> reservations = resDao.getAll();
            for (Reservation res : reservations) {
                System.out.println(res);
            }

            Dao<Room> dao_for_room = daoManager.getRoomDao();
            RoomDaoImpl roomDao = (RoomDaoImpl) dao_for_room;
            Set<Room> rooms = roomDao.getAll();
            for (Room r : rooms) {
                System.out.println(r);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // MAIN LOOP OF SYSTEM PROGRAM STARTS HERE
        main_loop();
    }

    private static void print_options() {
        System.out.println("1. Make a reservation\n2. Cancel a booking\n3. Get Room information\n");
    }

    private static void main_loop() {
        Scanner sc = new Scanner(System.in);
        boolean run = false;
        int command;
        int counter = 0;

        while (run) {
            System.out.println("Welcome to the 365-Reservation System! Please choose an option:");
            print_options();
            command = sc.nextInt();
            switch(command) {
                case 1: // call make reservation submenu
                    System.out.println("You're trying to make a reservation!");
                    counter=0;
                    continue;
                case 2: // call cancel reservation submenu
                    System.out.println("You're trying to cancel a reservation!");
                    counter=0;
                    continue;
                case 3: // call get room info submenu
                    System.out.println("You're trying to get some room info!");
                    counter=0;
                    continue;
                default: System.out.print("Unsupported command. Exiting...");
                    run=false;
            }
            if (++counter > 100) {
                run = false;
            }

        }

        return;
    }
}
