import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;

public class HelpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String helpText = "Pahana Edu System Help:\n- Use /login for login\n- Use /customer to manage customers\n- Use /item to manage items\n- Use /bill to get bill";
        exchange.sendResponseHeaders(200, helpText.length());
        OutputStream os = exchange.getResponseBody();
        os.write(helpText.getBytes());
        os.close();
    }
}
