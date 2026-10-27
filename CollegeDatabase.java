import java.sql.*;
import java.util.Scanner;

public class CollegeDatabase {
    static final String URL = "jdbc:oracle:thin:@localhost:1521/FREEPDB1";
    static final String USER = "system";
    static final String PASSWORD = "Oracle123";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    static void insertStudent(Scanner sc) {
        String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Student ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Department: ");
            String department = sc.nextLine();

            System.out.print("Enter Year of Study: ");
            int year = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, department);
            ps.setInt(4, year);
            ps.setString(5, email);

            ps.executeUpdate();
            con.commit();

            System.out.println("Student registered successfully.");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Student ID already exists.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void displayStudents() {
        String sql = "SELECT * FROM students";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nID\tName\tDepartment\tYear\tEmail");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("student_id") + "\t" +
                    rs.getString("name") + "\t" +
                    rs.getString("department") + "\t" +
                    rs.getInt("year_of_study") + "\t" +
                    rs.getString("email")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void searchStudent(Scanner sc) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Student ID: ");
            int id = sc.nextInt();

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nStudent Found");
                    System.out.println("ID: " + rs.getInt("student_id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Department: " + rs.getString("department"));
                    System.out.println("Year: " + rs.getInt("year_of_study"));
                    System.out.println("Email: " + rs.getString("email"));
                } else {
                    System.out.println("Student not found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- College Database ---");
            System.out.println("1. Insert Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    insertStudent(sc);
                    break;
                case 2:
                    displayStudents();
                    break;
                case 3:
                    searchStudent(sc);
                    break;
                case 4:
                    System.out.println("Program ended.");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}