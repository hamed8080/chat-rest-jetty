package servlets;

import application.Main;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hamed hosseini on 4/29/2023.
 */
public class ConnectionStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reconnectValue = request.getParameter("reconnect");
        boolean reconnect = false;
        if (reconnectValue != null) {
            reconnect = reconnectValue.equals("1");
        }

        String statusValue = request.getParameter("status");
        if (statusValue != null) {
            String sdk = statusValue.toLowerCase();
            if (sdk.equals("chat")) {
                writeToResponse(Main.chatManager.chat.getState().toString(), response);
            } else if (sdk.equals("async")) {
                writeToResponse(Main.chatManager.chat.getAsyncState().toString(), response);
            }
        } else if (reconnect) {
            Main.chatManager.chat.forceReconnect();
        }
    }

    private void writeToResponse(String message, HttpServletResponse response) {
        try {
            PrintWriter writer;
            response.addHeader("content-type", "application/text");
            writer = response.getWriter();
            writer.write(new Gson().toJson(message));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
