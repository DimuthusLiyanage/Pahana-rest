import com.sun.net.httpserver.*;
import java.io.*;
import java.sql.*;
import java.net.URI;
import java.util.*;

public class ItemHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";

        try (Connection conn = DBConnection.getConnection()) {
            if (method.equalsIgnoreCase("POST")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
                String[] parts = reader.readLine().split("&");
                String name = parts[0].split("=")[1];
                double price = Double.parseDouble(parts[1].split("=")[1]);
                int stock = Integer.parseInt(parts[2].split("=")[1]);

                PreparedStatement stmt = conn.prepareStatement("INSERT INTO items(name, price, stock) VALUES (?, ?, ?)");
                stmt.setString(1, name);
                stmt.setDouble(2, price);
                stmt.setInt(3, stock);
                stmt.executeUpdate();

                response = "Item added successfully";

            } else if (method.equalsIgnoreCase("PUT")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
                String[] parts = reader.readLine().split("&");
                int id = Integer.parseInt(parts[0].split("=")[1]);
                String name = parts[1].split("=")[1];
                double price = Double.parseDouble(parts[2].split("=")[1]);
                int stock = Integer.parseInt(parts[3].split("=")[1]);

                PreparedStatement stmt = conn.prepareStatement("UPDATE items SET name=?, price=?, stock=? WHERE id=?");
                stmt.setString(1, name);
                stmt.setDouble(2, price);
                stmt.setInt(3, stock);
                stmt.setInt(4, id);
                stmt.executeUpdate();

                response = "Item updated successfully";

            } else if (method.equalsIgnoreCase("DELETE")) {
                URI uri = exchange.getRequestURI();
                Map<String, String> params = parseQuery(uri.getQuery());
                int id = Integer.parseInt(params.get("id"));

                PreparedStatement stmt = conn.prepareStatement("DELETE FROM items WHERE id=?");
                stmt.setInt(1, id);
                stmt.executeUpdate();

                response = "Item deleted successfully";

            } else if (method.equalsIgnoreCase("GET")) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM items");

                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("ID: ").append(rs.getInt("id"))
                      .append(", Name: ").append(rs.getString("name"))
                      .append(", Price: ").append(rs.getDouble("price"))
                      .append(", Stock: ").append(rs.getInt("stock"))
                      .append("\n");
                }

                response = sb.toString();
            } else {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            exchange.sendResponseHeaders(200, response.getBytes().length);
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
