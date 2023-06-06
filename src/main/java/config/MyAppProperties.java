package config;


import application.Main;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by hamed hosseini on 4/29/2023.
 */

public class MyAppProperties {
    private String platformHost;
    private String socketAddress;
    private String token;
    private String ssoHost;
    private String fileServer;
    private String serverName;
    private String socketServerName;
    private String queueServer;
    private String queuePort;
    private String queueInput;
    private String queueOutput;
    private String queueUserName;
    private String queuePassword;
    private Boolean isSocket = false;
    private Boolean isLoggable = false;
    private String appId = "PodChat";
    private Long chatId;
    private Long maxReconnectCount = 5L;
    private Long reconnectInterval = 5000L;
    private Long asyncCheckConnectionLastMessageInterval = 5000L;
    private int serverPort = 9090;

    public String getPlatformHost() {
        return platformHost;
    }

    public void setPlatformHost(String platformHost) {
        this.platformHost = platformHost;
    }

    public String getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(String socketAddress) {
        this.socketAddress = socketAddress;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSsoHost() {
        return ssoHost;
    }

    public void setSsoHost(String ssoHost) {
        this.ssoHost = ssoHost;
    }

    public String getFileServer() {
        return fileServer;
    }

    public void setFileServer(String fileServer) {
        this.fileServer = fileServer;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getSocketServerName() {
        return socketServerName;
    }

    public void setSocketServerName(String socketServerName) {
        this.socketServerName = socketServerName;
    }

    public String getQueueServer() {
        return queueServer;
    }

    public void setQueueServer(String queueServer) {
        this.queueServer = queueServer;
    }

    public String getQueuePort() {
        return queuePort;
    }

    public void setQueuePort(String queuePort) {
        this.queuePort = queuePort;
    }

    public String getQueueInput() {
        return queueInput;
    }

    public void setQueueInput(String queueInput) {
        this.queueInput = queueInput;
    }

    public String getQueueOutput() {
        return queueOutput;
    }

    public void setQueueOutput(String queueOutput) {
        this.queueOutput = queueOutput;
    }

    public String getQueueUserName() {
        return queueUserName;
    }

    public void setQueueUserName(String queueUserName) {
        this.queueUserName = queueUserName;
    }

    public String getQueuePassword() {
        return queuePassword;
    }

    public void setQueuePassword(String queuePassword) {
        this.queuePassword = queuePassword;
    }

    public Boolean getIsSocket() {
        return isSocket;
    }

    public void setIsSocket(Boolean socket) {
        isSocket = socket;
    }

    public Boolean getSocket() {
        return isSocket;
    }

    public Boolean getIsLoggable() {
        return isLoggable;
    }

    public void setIsLoggable(Boolean loggable) {
        isLoggable = loggable;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getMaxReconnectCount() {
        return maxReconnectCount;
    }

    public void setMaxReconnectCount(Long maxReconnectCount) {
        this.maxReconnectCount = maxReconnectCount;
    }

    public Long getReconnectInterval() {
        return reconnectInterval;
    }

    public void setReconnectInterval(Long reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

    public Long getAsyncCheckConnectionLastMessageInterval() {
        return asyncCheckConnectionLastMessageInterval;
    }

    public void setAsyncCheckConnectionLastMessageInterval(Long asyncCheckConnectionLastMessageInterval) {
        this.asyncCheckConnectionLastMessageInterval = asyncCheckConnectionLastMessageInterval;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public static MyAppProperties loadResourceConfig() throws IOException, NullPointerException, InvalidPathException {
        InputStream inputStream = Main.class.getResourceAsStream("/config.json");
        byte[] resourceFileBytes = new byte[0];
        if (inputStream != null) {
            resourceFileBytes = IOUtils.toByteArray(inputStream);
        }
        String fileContent = new String(resourceFileBytes);
        return new Gson().fromJson(fileContent, MyAppProperties.class);
    }

    public static MyAppProperties loadExternalConfig(String jsonConfigFilePath) throws IOException, URISyntaxException, InvalidPathException {
//        Path jarDirPath = Paths.get(Main.class.getProtectionDomain()
//                .getCodeSource()
//                .getLocation()
//                .toURI()).getParent();
//        Path path = Paths.get(jarDirPath.toUri().resolve( "config.json"));
        Path path = Paths.get(jsonConfigFilePath);
        if (!Files.exists(path)) { return null;}
        byte[] externalConfigContent = Files.readAllBytes(path);
        String fileContent = new String(externalConfigContent);
        System.out.println("Loading config file from external.");
        return new Gson().fromJson(fileContent, MyAppProperties.class);
    }
}
