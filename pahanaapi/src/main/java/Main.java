import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/login", new AuthHandler());
        server.createContext("/customer", new CustomerHandler());
        server.createContext("/item", new ItemHandler());
        server.createContext("/bill", new BillHandler());
        server.createContext("/help", new HelpHandler());

        server.setExecutor(null); // default
        System.out.println("Server started on port 8080");
        server.start();
    }
}
