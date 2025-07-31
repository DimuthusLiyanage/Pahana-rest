import com.sun.net.httpserver.*;
import java.io.*;
import java.sql.*;
import java.net.URI;
import java.util.*;

public class BillHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        URI uri = exchange.getRequestURI();
        Map<String, String> params = parseQuery(uri.getQuery());
        String idParam = params.get("id");

        if (idParam == null) {
            String response = "Missing customer ID";
            exchange.sendResponseHeaders(400, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        int customerId = Integer.parseInt(idParam);
        double unitRate = 10.0; // Static rate

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT units_consumed FROM customers WHERE id = ?");
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            String response;
            if (rs.next()) {
                int units = rs.getInt("units_consumed");
                double bill = units * unitRate;
                response = "Customer ID: " + customerId + " | Units: " + units + " | Total Bill: Rs. " + bill;
            } else {
                response = "Customer not found";
            }

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null) return result;

        for (String pair : query.split("&")) {
            String[] entry = pair.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }
        }
        return result;
    }
}
