package application;

import asyncSdk.AsyncConfig;
import chatSdk.chat.Chat;
import chatSdk.chat.ChatListener;
import chatSdk.chat.listeners.Listener;
import chatSdk.chat.listeners.MessageListener;
import chatSdk.chat.listeners.ThreadListener;
import chatSdk.dataTransferObject.ChatResponse;
import chatSdk.dataTransferObject.chat.ChatConfig;
import chatSdk.dataTransferObject.chat.ChatState;
import chatSdk.dataTransferObject.message.inPut.Message;
import chatSdk.dataTransferObject.system.outPut.ErrorOutPut;
import chatSdk.dataTransferObject.thread.inPut.Conversation;
import config.MyAppProperties;

import java.util.HashMap;

/**
 * Created by hamed hosseini on 4/29/2023.
 */
public class ChatManager implements ChatListener {
    private final MyAppProperties appProperties;
    public Chat chat;
    public HashMap<String, Listener> listenerMaps = new HashMap<>();

    public ChatManager(MyAppProperties appProperties) {
        this.appProperties = appProperties;
        init();
    }

    void init() {
        String serverName = appProperties.getIsSocket() ? appProperties.getSocketServerName() : appProperties.getServerName();
        AsyncConfig asyncConfig = AsyncConfig
                .builder()
                .isSocketProvider(appProperties.getIsSocket())
                .socketAddress(appProperties.getSocketAddress())
                .serverName(serverName)
                .queuePassword(appProperties.getQueuePassword())
                .queueUserName(appProperties.getQueueUserName())
                .queueInput(appProperties.getQueueInput())
                .queueOutput(appProperties.getQueueOutput())
                .queueServer(appProperties.getQueueServer())
                .queuePort(appProperties.getQueuePort())
                .isLoggable(appProperties.getIsLoggable())
                .appId(appProperties.getAppId())
                .checkConnectionLastMessageInterval(appProperties.getAsyncCheckConnectionLastMessageInterval())
                .maxReconnectCount(appProperties.getMaxReconnectCount())
                .reconnectInterval(appProperties.getReconnectInterval())
                .build();
        ChatConfig chatConfig = ChatConfig.builder()
                .asyncConfig(asyncConfig)
                .severName(serverName)
                .token(appProperties.getToken())
                .chatId(appProperties.getChatId())
                .fileServer(appProperties.getFileServer())
                .ssoHost(appProperties.getSsoHost())
                .platformHost(appProperties.getPlatformHost())
                .isLoggable(appProperties.getIsLoggable())
                .maxReconnectCount(appProperties.getMaxReconnectCount())
                .reconnectInterval(appProperties.getReconnectInterval())
                .ttl(appProperties.getTtl())
                .build();
        System.out.println("token is " + appProperties.getToken());
        try {
            chat = Chat.init(chatConfig, this);
            chat.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(String s, ErrorOutPut errorOutPut) {
        String uniqueId = errorOutPut.getUniqueId();
        Listener listener = listenerMaps.get(uniqueId);
        if(listener != null) {
            listenerMaps.remove(uniqueId);
        }
    }

    @Override
    public void onChatState(ChatState chatState) {
        if (chatState == ChatState.ChatReady) {
            System.out.println("Chat is ready.");
        } else {
            System.out.println("Chat state is " + chatState.toString());
        }
    }

    @Override
    public void OnLogEvent(String s) {

    }

    @Override
    public void onNewMessage(ChatResponse<Message> response) {
        ChatListener.super.onNewMessage(response);
        String uniqueId = response.getResult().getUniqueId();
        Listener listener = listenerMaps.get(uniqueId);
        if (listener instanceof MessageListener) {
            ((MessageListener) listener).onNewMessage(response);
            listenerMaps.remove(uniqueId);
        }
    }

    @Override
    public void onGetThread(ChatResponse<Conversation[]> response) {
        ChatListener.super.onGetThread(response);
        String uniqueId = response.getUniqueId();
        Listener listener = listenerMaps.get(uniqueId);
        if (listener instanceof ThreadListener) {
            ((ThreadListener) listener).onGetThread(response);
            listenerMaps.remove(uniqueId);
        }
    }
}
