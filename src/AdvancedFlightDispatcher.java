import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AdvancedFlightDispatcher {
    private static final String URL = "jdbc:mysql://localhost:3306/FlightDB?allowLoadLocalInfile=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jerusalem&useSSL=false";
    private static final String USER = "root";

    public static void updateMultipleDelays(Connection conn, List<Integer> arrivalIds, String newStatus) throws SQLException {
        String sql = "UPDATE Arrivals SET delay_status = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);

            for (Integer id : arrivalIds) {
                pstmt.setInt(2, id);
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            int totalAffected = 0;
            for (int count : results) {
                if (count >= 0) {
                    totalAffected += count;
                }
            }
            System.out.println("Batch update completed. Total rows affected: " + totalAffected);
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Please enter your MySQL root password: ");
            String password = scanner.nextLine();

            try (Connection conn = DriverManager.getConnection(URL, USER, password)) {
                List<Integer> targetIds = Arrays.asList(1, 3, 5);
                updateMultipleDelays(conn, targetIds, "Landed - Final");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}