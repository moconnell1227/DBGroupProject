import dao.*;
import entity.CreditCard;
import entity.Customer;
import entity.Reservation;
import entity.Room;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

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

            Dao<Customer> dao_for_cust = daoManager.getCustomerDao();
            CustomerDaoImpl custDao = (CustomerDaoImpl) dao_for_cust;

            Dao<Reservation> dao_for_res = daoManager.getReservationDao();
            ReservationDaoImpl resDao = (ReservationDaoImpl) dao_for_res;


            Dao<Room> dao_for_room = daoManager.getRoomDao();
            RoomDaoImpl roomDao = (RoomDaoImpl) dao_for_room;

            main_loop(ccDao, custDao, resDao, roomDao);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void print_options() {
        System.out.println("1. Make a reservation\n2. Cancel a booking\n3. Change a booking\n4. Get Room or Reservation information\n5. Logout\n6. Quit");
    }

    private static void main_loop(CreditCardDaoImpl creditCardDao, CustomerDaoImpl customerDao,
                                  ReservationDaoImpl reservationDao, RoomDaoImpl roomDao) {
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        int command;
        int counter = 0;

        int custId = login(sc, customerDao);
        System.out.println("\nThanks for logging in!");
        System.out.println("Welcome to the 365-Reservation System! Please choose an option:");
        while (run) {
            System.out.println("\nOptions:");
            print_options();
            command = Integer.parseInt(sc.nextLine());
            switch (command) {
                case 1: // call make reservation submenu
                    reserveRoom(sc, roomDao, reservationDao, creditCardDao, custId);
                    counter = 0;
                    continue;
                case 2: // call cancel reservation submenu
                    cancelRoom(sc, reservationDao, custId);
                    counter = 0;
                    continue;
                case 3:
                    changeReservation(sc, reservationDao, roomDao, custId);
                    counter = 0;
                    continue;
                case 4: // call get room info submenu
                    getRoomInformations(sc, roomDao, reservationDao, custId);
                    counter = 0;
                    continue;
                case 5:
                    custId = login(sc, customerDao);
                    continue;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.print("Unsupported command. Exiting...");
                    run = false;
            }
            if (++counter > 100) {
                run = false;
            }

        }

    }

    private static int login(Scanner sc, CustomerDaoImpl customerDao) {
        System.out.println("Enter customer id for login (999 if new)");
        int id = Integer.parseInt(sc.nextLine());
        Customer cus = customerDao.getById(id);
        if (cus == null) {
            System.out.println("This customer does not exist, please provide a first and last name to create a new account");

            System.out.println("Enter first name");
            String first = sc.nextLine();

            System.out.println("Enter last name");
            String last = sc.nextLine();

            Customer newCustomer = new Customer(first, last);
            customerDao.insert(newCustomer);

            newCustomer = customerDao.getByFirstLast(first, last);
            System.out.println(newCustomer);
            return newCustomer.getId();
        } else {
            System.out.println(cus);
            return id;
        }
    }

    private static CreditCard getCreditCardForCustomer(int cid, CreditCardDaoImpl creditCardDao, Scanner sc) {
        Set<CreditCard> creditCards = creditCardDao.getAllForCustomer(cid);
        Map<Integer, CreditCard> cardMap = new HashMap<>();

        if (creditCards == null) {
            System.out.println("No known cards found... we just made one for you!\nNew card:");
            CreditCard newCard = new CreditCard(0, 10000, cid);
            creditCardDao.insert(newCard);
            newCard = (CreditCard) creditCardDao.getAllForCustomer(cid).toArray()[0];
            cardMap.put(newCard.getCardNum(), newCard);
            System.out.println(newCard);
        } else {
            System.out.println("Found cards:");
            for (CreditCard creditCard : creditCards) {
                System.out.println(creditCard);
                cardMap.put(creditCard.getCardNum(), creditCard);
            }
        }

        System.out.println("Select card to use");
        return cardMap.get(Integer.parseInt(sc.nextLine()));
    }

    private static void reserveRoom(Scanner sc, RoomDaoImpl roomDao, ReservationDaoImpl reservationDao,
                                    CreditCardDaoImpl creditCardDao, int custId) {
        System.out.println("You're trying to make a reservation!");
        System.out.println("Enter desired check in date (YYYY-MM-DD)");
        String checkin = sc.nextLine();

        System.out.println("Enter desired check out date (YYYY-MM-DD)");
        String checkout = sc.nextLine();

        System.out.println("Enter desired occupants");
        int occupants = Integer.parseInt(sc.nextLine());

        System.out.println("Enter the type of room [King, Queen, Double]");
        String type = sc.nextLine();

        System.out.println("Enter the type of decor [modern, traditional, rustic]");
        String decor = sc.nextLine();

        System.out.println("Enter the maximum price per day of the room");
        int maxPrice = Integer.parseInt(sc.nextLine());

        Set<Room> availableRooms = roomDao.getAvailableRooms(checkin, checkout, occupants, type, decor, maxPrice);
        if (availableRooms == null || availableRooms.size() == 0) {
            System.out.println("Sorry, no rooms are available with those specifications!\n");
            return;
        }

        System.out.println("\nAvailable Rooms:");
        Map<String, Room> roomMap = new HashMap<>();
        for (Room room : availableRooms) {
            System.out.println(room);
            roomMap.put(room.getCode(), room);
        }
        System.out.println();

        System.out.println("Enter desired room code");
        Room room = roomMap.get(sc.nextLine());

        System.out.println("Pulling known credit cards...");
        CreditCard creditCard = getCreditCardForCustomer(custId, creditCardDao, sc);

        Reservation reservation = new Reservation(checkin, checkout,
                (float) room.getBasePrice(), occupants, room.getCode(), custId, creditCard.getCardNum());

        boolean success = reservationDao.insert(reservation);
        if (success) {
            System.out.println("Congrats! You made a reservation\n" + reservation);
        } else {
            System.out.println("Sorry! Your card was declined :(((");
        }
    }

    private static void cancelRoom(Scanner sc, ReservationDaoImpl reservationDao, int custId) {
        System.out.println("You're trying to cancel a reservation!");
        Set<Reservation> reservations = reservationDao.getAllForCustomer(custId);
        Map<Integer, Reservation> reservationMap = new HashMap<>();

        if (reservations == null) {
            System.out.println("No reservations were found with this customer ID");
            return;
        }

        System.out.println("Found reservations:");
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
            reservationMap.put(reservation.getrID(), reservation);
        }

        System.out.println("\nEnter id to cancel");
        Reservation cancelled = reservationMap.get(Integer.parseInt(sc.nextLine()));
        reservationDao.delete(cancelled);

        System.out.println("Your reservation has been cancelled, credit card " + cancelled.getCardNum() + " has been refunded");
    }

    private static void changeReservation(Scanner sc, ReservationDaoImpl reservationDao, RoomDaoImpl roomDao, int custId) {
        Set<Reservation> reservations = reservationDao.getAllForCustomer(custId);
        if (reservations == null || reservations.size() == 0) {
            System.out.println("No reservations found for given customer");
            return;
        }

        Map<Integer, Reservation> reservationMap = new HashMap<>();
        System.out.println("Found reservations:");
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
            reservationMap.put(reservation.getrID(), reservation);
        }

        System.out.println("Enter ID of desired change of reservation");
        Reservation reservation = reservationMap.get(Integer.parseInt(sc.nextLine()));

        System.out.println("Current Details:\nCheck In: " + reservation.getCheckIn() + "\nCheck Out: " + reservation.getCheckOut() +
                "\nNumber of Occupants: " + reservation.getNumOcc() + "\n");

        String maxCheckInExt = reservationDao.getMaxCheckInChangeDate(reservation.getCheckIn(), reservation.getRoomCode());
        System.out.println("Max check in extension: " + maxCheckInExt);

        String maxCheckOutExt = reservationDao.getMaxCheckOutChangeDate(reservation.getCheckOut(), reservation.getRoomCode());
        System.out.println("Max check in extension: " + maxCheckOutExt);

        System.out.println("\nEnter new check in date:");
        String checkIn = sc.nextLine();

        System.out.println("\nEnter new check out date:");
        String checkOut = sc.nextLine();

        Room room = roomDao.getByCode(reservation.getRoomCode());
        System.out.println("\nEnter new occupant count [NOTE: OCCUPANCY MAY NOT EXCEED " + room.getMaxOccupancy() + "!!]:");
        int numOcc = Integer.parseInt(sc.nextLine());

        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(checkOut);
        reservation.setNumOcc(numOcc);

        reservationDao.update(reservation);
        System.out.println("Reservation successfully updated\n" + reservation);
    }

    private static void getRoomInformations(Scanner sc, RoomDaoImpl roomDao, ReservationDaoImpl reservationDao, int custId) {
        System.out.println("You're trying to get some room info!\nOptions:");

        System.out.println("1. Get All Rooms\n2. Get Your Reservations");
        int choice = Integer.parseInt(sc.nextLine());

        switch (choice) {
            case 1:
                Set<Room> rooms = roomDao.getAll();
                if (rooms == null || rooms.size() == 0) {
                    System.out.println("Sorry no rooms were found.\n");
                    return;
                }

                System.out.println("Rooms in the hotel:");
                for (Room room : rooms) {
                    System.out.println(room);
                }
                break;
            case 2:
                Set<Reservation> reservations = reservationDao.getAllForCustomer(custId);
                if (reservations == null || reservations.size() == 0) {
                    System.out.println("No reservations found for given customer");
                } else {
                    for (Reservation reservation : reservations) {
                        System.out.println(reservation);
                    }
                }
                break;
        }
    }
}

