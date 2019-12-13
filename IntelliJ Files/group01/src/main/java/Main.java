import dao.*;
import entity.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String args[]) {
        String credFile = "./src/main/java/credential.xml";
        Properties properties = new Properties();
        FileInputStream fis;

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

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void print_options(boolean isManager) {
        if (isManager) {
            System.out.println("Welcome, manager!");
            System.out.println("1. Make a reservation\n2. Cancel a booking\n3. Change a booking\n4. Get Room or Reservation information\n5. Logout\n6. Get Monthly Revenues\n7. Quit");
        } else {
            System.out.println("1. Make a reservation\n2. Cancel a booking\n3. Change a booking\n4. Get Room or Reservation information\n5. Logout\n6. Quit");
        }
    }

    private static void main_loop(CreditCardDaoImpl creditCardDao, CustomerDaoImpl customerDao,
                                  ReservationDaoImpl reservationDao, RoomDaoImpl roomDao) {
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        int command;
        int managerID = 900;

        int custId = login(sc, customerDao);
        System.out.println("\nThanks for logging in!");
        System.out.println("Welcome to the 365-Reservation System! Please choose an option:");
        while (run) {
            System.out.println("\nOptions:");
            print_options((custId == managerID));
            command = Integer.parseInt(sc.nextLine());
            switch (command) {
                case 1: // call make reservation submenu
                    reserveRoom(sc, roomDao, reservationDao, creditCardDao, custId);
                    continue;
                case 2: // call cancel reservation submenu
                    cancelRoom(sc, reservationDao, custId);
                    continue;
                case 3: // change a reservation submenu
                    changeReservation(sc, reservationDao, roomDao, custId);
                    continue;
                case 4: // call get room info submenu
                    getRoomInformations(sc, roomDao, reservationDao, custId);
                    continue;
                case 5: // logout
                    custId = login(sc, customerDao);
                    continue;
                case 6: // quit or monthly revenue if manager
                    if (custId == managerID) {
                        System.out.println("Fetching monthly revenue...");
                        getMonthlyRevenue(reservationDao);
                    } else {
                        System.out.println("Exiting...");
                        run = false;
                    }
                    continue;
                case 7: // Quit always
                    System.out.println("Exiting...");
                    run = false;
                    continue;
                default:
                    System.out.print("Unsupported command. Exiting...");
                    run = false;
            }
        }
    }

    private static int login(Scanner sc, CustomerDaoImpl customerDao) {
        System.out.println("Enter customer id for login (999 if new)");
        int id = Integer.parseInt(sc.nextLine());
        Customer cus;
        if (id == 900) {
            System.out.println("Logging in as Manager.");
            return id;
        } else {
            cus = customerDao.getById(id);
        }
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
        System.out.println("Popularity\tCODE\t\tName\t\t\t\t\tBed Type\tBeds\tMaxOcc\tBase Price\tDecor");
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
        System.out.println("Cancelled reservation:\n" + cancelled);
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
        if (maxCheckOutExt == null) {
            maxCheckOutExt = "9999-12-31";
        }
        System.out.println("Max check out extension: " + maxCheckOutExt);

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

        if (reservationDao.update(reservation)) {
            System.out.println("Reservation successfully updated\n" + reservation);
        } else {
            System.out.println("Reservation was not updated because your card was declined.");
        }
    }

    private static Set<Room> getSetDifference(Set<Room> allRooms, Set<Room> availableRooms) {
        Set<Room> setDiff = new HashSet<>();
        if (availableRooms == null || availableRooms.size() <= 0 || allRooms == null || allRooms.size() <= 0) {
            System.out.println("Sorry, there are no available rooms.");
            return null;
        } else {
            for (Room room : allRooms) {
                String code = room.getCode();
                boolean free = false;
                for (Room freeRoom : availableRooms) {
                    if (code.equals(freeRoom.getCode())) {
                        free = true;
                    }
                }
                if (!free) {
                    setDiff.add(room);
                }
            }
            return setDiff;
        }
    }

    private static void getRoomInformations(Scanner sc, RoomDaoImpl roomDao, ReservationDaoImpl reservationDao, int custId) {
        System.out.println("You're trying to get some room info!\nOptions:");

        System.out.println("1. Get All Rooms\n2. Get Your Reservations");
        int choice = Integer.parseInt(sc.nextLine());

        switch (choice) {
            case 1:
                String formatString = "yyyy-MM-dd";
                SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);
                Date current_date = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(current_date);
                c.add(Calendar.DATE, 1);
                Date next_date = c.getTime();
                Set<Room> allRooms = roomDao.getAll();
                if (allRooms == null || allRooms.size() == 0) {
                    System.out.println("Sorry no rooms were found.\n");
                    return;
                }

                Set<Room> available = roomDao.getAvailableRooms(dateFormat.format(current_date), dateFormat.format(next_date));
                Set<Room> unavailable = getSetDifference(allRooms, available);

                if (available.size() > 0) {
                    System.out.println("Available Rooms:");
                    String title = "Popularity\tCODE\t\tName\t\t\t\t\tBed Type\tBeds\tMaxOcc\tBase Price\tDecor\n";
                    System.out.print(title);
                    for (Room room : available) {
                        System.out.println(room);
                    }
                }

                if (unavailable != null && unavailable.size() > 0) {
                    System.out.println("Unavailable Rooms:");
                    String title = "Popularity\tCODE\t\tName\t\t\t\t\tBed Type\tBeds\tMaxOcc\tBase Price\tDecor\t\tNext Available Date\n";
                    System.out.print(title);
                    for (Room room : unavailable) {
                        System.out.println(room + "\t" + reservationDao.getCheckOutDateForCode(room.getCode(), dateFormat.format(current_date)));
                    }
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

    private static void displayRevenues(String roomCode, List<MonthlyRevenue> revenues) {
        int total = 0;
        HashMap<Integer, Integer> monthsMap = new HashMap<>();
        for (MonthlyRevenue rev : revenues) {
            monthsMap.put(rev.getMonth(), rev.getRevenue());
        }
        HashMap<Integer, Integer> sorted = new HashMap<>(monthsMap);
        String resultString = "";
        for (Integer month : sorted.keySet()) {
            total += sorted.get(month);
            resultString = resultString.concat(String.format("\t%-5s", sorted.get(month).toString()));
        }
        System.out.println(roomCode + ": " + resultString + String.format("\t%-5d", total));
    }

    private static void getMonthlyRevenue(ReservationDaoImpl reservationDao) {
        Set<MonthlyRevenue> revenues = reservationDao.getRevenue();
        HashMap<String, List<MonthlyRevenue>> revMap = new HashMap<>();
        for (MonthlyRevenue month : revenues) {
            String room = month.getRoom();
            if (!revMap.containsKey(room)) {
                List<MonthlyRevenue> list = new ArrayList<>();
                list.add(month);

                revMap.put(room, list);
            } else {
                revMap.get(room).add(month);
            }
        }
        System.out.println("Room\tJAN\t\tFEB\t\tMAR\t\tAPR\t\tMAY\t\tJUN\t\tJUL\t\tAUG\t\tSEP\t\tOCT\t\tNOV\t\tDEC\t\tTOTAL");
        for (String room : revMap.keySet()) {
            List<MonthlyRevenue> revs = revMap.get(room);
            displayRevenues(room, revs);
        }
    }
}

