import com.sun.net.httpserver.*;
import java.io.*;
import java.sql.*;

public class AuthHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        String body = reader.readLine(); // Expecting format: username=abc&password=123
        String[] parts = body.split("&");

        String username = parts[0].split("=")[1];
        String password = parts[1].split("=")[1];

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            String response = rs.next() ? "Login success" : "Invalid credentials";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
