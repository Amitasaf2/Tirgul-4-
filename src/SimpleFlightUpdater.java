import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * SecureFlightUpdater (refactored SimpleFlightUpdater) updates flight arrival statuses
 * using parameterized JDBC queries to prevent SQL Injection attacks.
 *
 * <p><b>Security Audit &amp; Vulnerability Fix:</b></p>
 * <ul>
 *   <li><b>Previous Vulnerability:</b> The original implementation used {@link java.sql.Statement}
 *       with raw string concatenation ({@code "UPDATE Arrivals SET delay_status = '" + userInput + "' ..."}).
 *       An attacker supplying a payload such as {@code Landed' #} prematurely closed the SQL string literal
 *       and commented out the {@code WHERE} clause, corrupting all 299 rows in the table.</li>
 *   <li><b>Mitigation via PreparedStatement:</b> Refactoring to {@link java.sql.PreparedStatement}
 *       with a {@code ?} placeholder ensures the SQL query structure is pre-compiled by the database engine
 *       before user input is bound via {@code setString()}. Any special characters (such as quotes or
 *       comment symbols) are strictly treated as literal data values rather than executable SQL syntax.</li>
 * </ul>
 */
public class SimpleFlightUpdater {
    private static final String URL = "jdbc:mysql://localhost:3306/FlightDB?allowLoadLocalInfile=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jerusalem&useSSL=false";
    private static final String USER = "root";

    /**
     * Prompts the user for database credentials and a new delay status for flight LX354,
     * safely executing the update with a parameterized PreparedStatement.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        String sql = "UPDATE Arrivals SET delay_status = ? WHERE flight_number = ?";

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Please enter your MySQL root password: ");
            String password = scanner.nextLine();

            System.out.print("Enter new delay status for flight 'LX354': ");
            String userInput = scanner.nextLine();

            try (Connection conn = DriverManager.getConnection(URL, USER, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, userInput);
                pstmt.setString(2, "LX354");

                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Number of affected rows: " + rowsAffected);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}