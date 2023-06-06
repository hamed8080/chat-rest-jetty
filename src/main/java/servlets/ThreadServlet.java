package servlets;

import application.Main;
import chatSdk.chat.listeners.ThreadListener;
import chatSdk.dataTransferObject.ChatResponse;
import chatSdk.dataTransferObject.thread.inPut.Conversation;
import chatSdk.dataTransferObject.thread.outPut.GetThreadRequest;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

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
public class ThreadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int offset = Integer.parseInt(request.getParameter("offset"));
        int count = Integer.parseInt(request.getParameter("count"));
        getThreadsAsync(request, response, count, offset);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] bytes = IOUtils.toByteArray(request.getInputStream());
        String jsonContent = new String(bytes);
        GetThreadRequest msg = new Gson().fromJson(jsonContent, GetThreadRequest.class);
        getThreadsAsync(request, response, msg.getCount(), msg.getOffset());
    }

    private void getThreadsAsync(HttpServletRequest request, HttpServletResponse response, int count, int offset) throws ServletException, IOException {
        final AsyncContext asyncContext = request.startAsync();
        if (checkSocketIsNotOpen(response)) return;
        GetThreadRequest getThreadRequest = new GetThreadRequest.Builder()
                .setCount(count)
                .setOffset(offset)
                .build();
        String uniqueId = Main.chatManager.chat.getThreads(getThreadRequest);
        asyncContext.start(() -> Main.chatManager.listenerMaps.put(uniqueId, new ThreadListener() {
            @Override
            public void onGetThread(ChatResponse<Conversation[]> threads) {
                try {
                    PrintWriter writer;
                    response.addHeader("content-type", "application/json");
                    writer = response.getWriter();
                    writer.write(new Gson().toJson(threads));
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
