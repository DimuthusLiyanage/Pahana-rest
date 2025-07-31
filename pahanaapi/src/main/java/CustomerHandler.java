import com.sun.net.httpserver.*;
import java.io.*;
import java.sql.*;

public class CustomerHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            String body = reader.readLine(); // Simplified for demo
            String[] parts = body.split("&");

            String accNum = parts[0].split("=")[1];
            String name = parts[1].split("=")[1];
            String address = parts[2].split("=")[1];
            String phone = parts[3].split("=")[1];
            int units = Integer.parseInt(parts[4].split("=")[1]);

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO customers(account_number, name, address, phone, units_consumed) VALUES (?, ?, ?, ?, ?)");
                stmt.setString(1, accNum);
                stmt.setString(2, name);
                stmt.setString(3, address);
                stmt.setString(4, phone);
                stmt.setInt(5, units);
                stmt.executeUpdate();

                String response = "Customer added successfully";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
