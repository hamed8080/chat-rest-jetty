package config;

import application.Main;
import chatSdk.dataTransferObject.chat.ChatState;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hamed hosseini on 4/29/2023.
 */
public class MyResponseHandler {
    public static boolean checkSocketIsNotOpen(HttpServletResponse resp) {
        if (Main.chatManager.chat.getState() != ChatState.ChatReady) {
            try {
                PrintWriter writer;
                writer = resp.getWriter();
                writer.write("Chat is not ready");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }
}
