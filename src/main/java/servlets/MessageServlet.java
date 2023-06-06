package servlets;

import application.Main;
import chatSdk.chat.listeners.MessageListener;
import chatSdk.dataTransferObject.ChatResponse;
import chatSdk.dataTransferObject.message.inPut.Message;
import chatSdk.dataTransferObject.message.outPut.SendMessageRequest;
import com.google.gson.Gson;
import servlets.dto.MessageRequest;
import sun.misc.IOUtils;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static config.MyResponseHandler.checkSocketIsNotOpen;

/**
 * Created by hamed hosseini on 4/29/2023.
 */
public class MessageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = request.getParameter("message");
        long threadId = Long.parseLong(request.getParameter("threadId"));
        sendAsyncMessage(request, response, message, threadId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] bytes = IOUtils.readAllBytes(request.getInputStream());
        String jsonContent = new String(bytes);
        MessageRequest msg = new Gson().fromJson(jsonContent, MessageRequest.class);
        sendAsyncMessage(request, response, msg.getMessage(), msg.getThreadId());
    }

    private void sendAsyncMessage(HttpServletRequest request, HttpServletResponse response, String message, long threadId) throws ServletException, IOException {
        final AsyncContext asyncContext = request.startAsync();
        if (checkSocketIsNotOpen(response)) return;
        SendMessageRequest sendMessageRequest = new SendMessageRequest.Builder()
                .setMessage(message)
                .setThreadId(threadId)
                .build();
        String uniqueId = Main.chatManager.chat.sendTextMessage(sendMessageRequest);
        asyncContext.start(() -> Main.chatManager.listenerMaps.put(uniqueId, new MessageListener() {
            @Override
            public void onNewMessage(ChatResponse<Message> messages) {
                try {
                    PrintWriter writer;
                    response.addHeader("content-type", "application/json");
                    writer = response.getWriter();
                    writer.write(new Gson().toJson(messages));
                    writer.close();
                    asyncContext.complete();
                } catch (IOException e) {
                    asyncContext.complete();
                    throw new RuntimeException(e);
                }
            }
        }));
    }
}
