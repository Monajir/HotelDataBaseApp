import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/hotel_db";
        String username = "root";
        String password = "****"; // put password here

        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement statement = con.createStatement();

            while(true) {
                System.out.println();
                System.out.println("Choose one of the following functions : ");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get room number");
                System.out.println("4. Update reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");

                Scanner scanner = new Scanner(System.in);
                int function = scanner.nextInt();

                switch (function) {
                    case 1:
                            ReserveRoom(statement, scanner);
                            break;
                    case 2:
                            viewReservation(statement);
                            break;
                    case 3:
                            getRoomNumber(statement, scanner);
                            break;
                    case 4:
                            updateReservation(statement, scanner);
                            break;
                    case 5:
                            deleteReservation(statement, scanner);
                            break;
                    case 0:
                            // exit()
                            System.out.println("Thank you for using our app.");
                            scanner.close();
                            return;
                    default :
                        System.out.println("Invalid Choice!!");
                }
            }
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
//        catch(InterruptedException e) {
//            throw new RuntimeException(e);
//        }

    }

    public static void ReserveRoom(Statement statement, Scanner scanner) {
        try{
            String guest_name;
            int room_number;
            String contact_number;

            System.out.println("Enter guest name: ");
            guest_name = scanner.next();
            scanner.nextLine();

            System.out.println("Enter room number: ");
            room_number = scanner.nextInt();

            System.out.println("Enter contact number: ");
            contact_number = scanner.next();

            String sql = "INSERT INTO reservation (guest_name, room_number, contact_number) " +
                        "VALUES ('" + guest_name + "', " + room_number + ", '" + contact_number + "');";

            try{
                int affectedRows = statement.executeUpdate(sql);
                if(affectedRows > 0) {
                    System.out.println("Reservation Successful!");
                }else {
                    System.out.println("Reservation failed");
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void viewReservation(Statement statement) {
        String sql = "Select * from reservation;";
        try{
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Current Reservations : ");
            while(resultSet.next()) {
                int reservation_id = resultSet.getInt("reservation_id");
                String guest_name = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contact_number = resultSet.getString("contact_number");
                String reservation_date = resultSet.getString("reservation_date");

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservation_id, guest_name, roomNumber, contact_number, reservation_date);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    private static void getRoomNumber(Statement statement, Scanner scanner) {
        System.out.println("Enter reservation ID : ");
        int reservation_id = scanner.nextInt();
        System.out.println("Enter guest name : ");
        String guestName = scanner.next();

        String sql = "Select room_number from reservation " +
                "where reservation_id = " + reservation_id +
                " and guest_name = '" + guestName + "';";
        try{
                ResultSet resultSet = statement.executeQuery(sql);
                if(resultSet.next()) {
                    int room_number = resultSet.getInt("room_number");
                    System.out.println("Room number for reservation ID: " + reservation_id +
                            " and Guest " + guestName + " is " + room_number);
                }else {
                    System.out.println("Reservation not found for the given ID and guest name. ");
                }

        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateReservation(Statement statement, Scanner scanner) {
        try{
            System.out.println("Enter reservation ID to update : ");
            int reservation_id = scanner.nextInt();
            scanner.nextLine();

            if(!reservationExist(statement, reservation_id)) {
                System.out.println("Reservation not found!!");
                return;
            }
            System.out.println("Enter new guest name : ");
            String newGuest_name = scanner.nextLine();
            System.out.print("Enter new room number : ");
            int room_number = scanner.nextInt();
            System.out.println("Enter new Contact Number : ");
            String contact_number = scanner.next();

            String sql = "UPDATE reservation " +
                    "SET guest_name = '" + newGuest_name + "', room_number = " + room_number + ",  contact_number = '" + contact_number + "' " +
                    "WHERE reservation_id = " + reservation_id;

            try{
                int affectedRows = statement.executeUpdate(sql);

                if(affectedRows > 0){
                    System.out.println("Reservation successfully updated.");
                }else {
                    System.out.println("Reservation update failed");
                }
            }catch(SQLException e) {
                System.out.println(e.getMessage());
            }

        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteReservation(Statement statement, Scanner scanner) {
        try{
            System.out.println("Enter reservation ID to delete: ");
            int reservation_id = scanner.nextInt();

            if(!reservationExist(statement, reservation_id)){
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservation where reservation_id = " + reservation_id;

            int affectedRows = statement.executeUpdate(sql);
            if(affectedRows > 0) {
                System.out.println("Deleted successfully.");
            }else {
                System.out.println("Reservation deletion failed.");
            }

        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean reservationExist(Statement statement, int reservation_id) {
        try{
            String sql = "SELECT reservation_id from reservation where reservation_id = " + reservation_id + ";";

            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next(); // Return a boolean
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}