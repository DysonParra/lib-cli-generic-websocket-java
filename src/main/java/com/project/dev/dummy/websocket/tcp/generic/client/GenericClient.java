/*
 * @fileoverview    {GenericClient}
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementación realizada.
 * @version 2.0     Documentación agregada.
 */
package com.project.dev.dummy.websocket.tcp.generic.client;

import com.project.dev.dummy.websocket.tcp.generic.exception.ListenerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO: Definición de {@code GenericClient}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class GenericClient implements Runnable {

    protected String name;
    protected String serverIpAddress;
    protected int serverPort;
    @Setter(AccessLevel.NONE)
    protected String clientIpAddress;
    @Setter(AccessLevel.NONE)
    protected int clientPort;
    protected long retryTime = 3000;
    protected long maxTries = -1;
    protected boolean autoRetryConnection = false;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected Socket serverSocket = null;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected BufferedReader socketInputStream = null;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected OutputStream socketOutputStream = null;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected Queue<String> messagesToSend;
    @Setter(AccessLevel.NONE)
    protected boolean connected = false;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected boolean connecting = false;
    @Setter(AccessLevel.NONE)
    protected boolean stopped = false;
    @Setter(AccessLevel.NONE)
    protected boolean killed = false;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected String lastMessage;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected String lastResponse;
    protected ClientMessageListener onMessageListener = null;
    protected ClientConnectionListener onConnectionListener = null;
    protected boolean processListenersOnOtherThread = false;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected final Object synchronizer = new Object();

    /**
     * TODO: Definición de {@code GenericClient}.
     *
     */
    public GenericClient() {
        this.messagesToSend = new LinkedList<>();
        this.clientIpAddress = "127.0.0.1";
        this.clientPort = 0;
    }

    /**
     * TODO: Definición de {@code GenericClient}.
     *
     * @param serverIpAddress
     */
    public GenericClient(String serverIpAddress) {
        this();
        if (serverIpAddress.equals("localhost"))
            this.serverIpAddress = "127.0.0.1";
        else
            this.serverIpAddress = serverIpAddress;
        updateName();
    }

    /**
     * TODO: Definición de {@code GenericClient}.
     *
     * @param serverIpAddress
     * @param serverPort
     */
    public GenericClient(String serverIpAddress, int serverPort) {
        this(serverIpAddress);
        this.serverPort = serverPort;
        updateName();
    }

    /**
     * TODO: Definición de {@code GenericClient}.
     *
     * @param serverIpAddress
     * @param serverPort
     * @param retryTime
     */
    public GenericClient(String serverIpAddress, int serverPort, int retryTime) {
        this(serverIpAddress, serverPort);
        this.retryTime = retryTime;
    }

    /**
     * TODO: Definición de {@code GenericClient}.
     *
     * @param serverIpAddress
     * @param serverPort
     * @param retryTime
     * @param maxTries
     */
    public GenericClient(String serverIpAddress, int serverPort, int retryTime, int maxTries) {
        this(serverIpAddress, serverPort, retryTime);
        this.maxTries = maxTries;
    }

    /**
     * TODO: Definición de {@code GenericClient}.
     *
     * @param serverIpAddress
     * @param serverPort
     * @param retryTime
     * @param maxTries
     * @param autoRetryConnection
     */
    public GenericClient(String serverIpAddress, int serverPort, int retryTime, int maxTries, boolean autoRetryConnection) {
        this(serverIpAddress, serverPort, retryTime, maxTries);
        this.autoRetryConnection = autoRetryConnection;
    }

    /**
     * TODO: Definición de {@code GenericClient}.
     *
     * @param serverIpAddress
     * @param serverPort
     * @param retryTime
     * @param maxTries
     * @param autoRetryConnection
     * @param processListenersOnOtherThread
     */
    public GenericClient(String serverIpAddress, int serverPort, int retryTime, int maxTries, boolean autoRetryConnection, boolean processListenersOnOtherThread) {
        this(serverIpAddress, serverPort, retryTime, maxTries, autoRetryConnection);
        this.processListenersOnOtherThread = processListenersOnOtherThread;
    }

    /**
     * TODO: Definición de {@code updateName}.
     *
     */
    private void updateName() {
        this.setName("Socket " + this.serverIpAddress + ":" + this.serverPort);
    }

    /**
     * TODO: Definición de {@code setServerIpAddress}.
     *
     * @param serverIpAddress
     */
    public void setServerIpAddress(String serverIpAddress) {
        this.serverIpAddress = serverIpAddress;
        updateName();
        this.stop();
    }

    /**
     * TODO: Definición de {@code setServerPort}.
     *
     * @param serverPort
     */
    public void setServerPort(int serverPort) {
        this.stop();
        this.serverPort = serverPort;
        updateName();
    }

    /**
     * TODO: Definición de {@code initialize}.
     *
     * @return
     */
    public boolean initialize() {
        boolean result = false;
        if (!this.connecting && !this.connected && !this.stopped) {
            this.connecting = true;
            this.connected = false;
            if (this.serverIpAddress != null && this.serverPort > 0) {
                this.serverSocket = connectToServer();
                if (serverSocket != null) {
                    try {
                        socketOutputStream = serverSocket.getOutputStream();
                        socketInputStream = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                        if (socketOutputStream != null) {
                            this.connected = true;
                            System.out.println("Can send and receive messages now in " + "'" + this.name + "' from/to server in " + this.serverIpAddress + ":" + this.serverPort);
                            this.clientIpAddress = this.serverSocket.getLocalAddress().getHostAddress();
                            this.clientPort = this.serverSocket.getLocalPort();
                            sendStackMessages();
                            result = true;
                        }
                        if (socketInputStream != null) {
                            synchronized (synchronizer) {
                                synchronizer.notifyAll();
                            }
                        }
                    } catch (IOException ex) {
                        System.out.println("Error getting Output stream of socket.");
                    }
                } else {
                    System.out.println("Could not connect to server in " + this.serverIpAddress + ":" + this.serverPort + " from '" + this.name + "', Max tries reached or client stopped?");
                }
            } else {
                System.out.println("Invalid ipAddres: " + this.serverIpAddress + " or server port: " + this.serverPort);
            }
            this.connecting = false;
            try {
                if (this.connected && this.onConnectionListener != null) {
                    if (processListenersOnOtherThread)
                        new Thread(() -> this.onConnectionListener.onConnectedClient(this, serverSocket)).start();
                    else
                        this.onConnectionListener.onConnectedClient(this, serverSocket);
                }
            } catch (Exception ex) {
                ListenerException listener = new ListenerException("Error proccessing method onConnectedClient", ex);
                listener.printStackTrace(System.out);
            }
        } else if (this.stopped) {
            System.out.println("Client is stopped start first.");
        } else if (this.connecting) {
            System.out.println("Another init task in process, try in other moment.");
        } else if (this.connected) {
            System.out.println("Client is connected, stop or edit ip or port first.");
        }
        return result;
    }

    /**
     * TODO: Definición de {@code connectToServer}.
     *
     * @return
     */
    private Socket connectToServer() {
        int tries = 0;
        Socket socket = null;
        do {
            try {
                tries++;
                System.out.println("Trying to connect client '" + this.name + "' to server in " + this.serverIpAddress + ":" + this.serverPort + " (Trie " + tries + ")");
                socket = new Socket();
                socket.connect(new InetSocketAddress(this.serverIpAddress, this.serverPort), 5000);
                System.out.println("Client " + "'" + this.name + "' is connected to server in " + this.serverIpAddress + ":" + this.serverPort);
                break;
            } catch (Exception e) {
                System.out.println(" Error connecting client '" + this.name + "' to server in " + this.serverIpAddress + ":" + this.serverPort);
                this.connected = false;
                try {
                    if (this.onConnectionListener != null) {
                        if (processListenersOnOtherThread) {
                            int triesAux = tries;
                            new Thread(() -> this.onConnectionListener.onErrorConnectingToServer(this, this.serverIpAddress, this.serverPort, triesAux)).start();
                        } else
                            this.onConnectionListener.onErrorConnectingToServer(this, this.serverIpAddress, this.serverPort, tries);
                    }
                } catch (Exception ex) {
                    ListenerException listener = new ListenerException("Error proccessing method onErrorConnectingToServer", ex);
                    listener.printStackTrace(System.out);
                }
                if (this.retryTime > 0) {
                    System.out.println(" Waiting " + this.retryTime / 1000.0 + " seconds for reconnect client '" + this.name + "'..." + "\n");
                    try {
                        synchronized (synchronizer) {
                            synchronizer.wait(this.retryTime);
                        }
                    } catch (InterruptedException ex) {
                        System.out.println("Error waiting to retry..." + "\n");
                    }
                } else {
                    socket = null;
                    break;
                }
            }
        } while (this.maxTries != tries && !this.stopped);
        if (stopped)
            socket = null;
        return socket;
    }

    /**
     * TODO: Definición de {@code init}.
     *
     */
    public void init() {
        new Thread(this::initialize).start();
    }

    /**
     * TODO: Definición de {@code start}.
     *
     * @return
     */
    public boolean start() {
        boolean result = false;
        if (this.stopped) {
            this.stopped = false;
            this.connected = false;
            System.out.println("Client " + "'" + this.name + "'" + " started");
            result = true;
        } else
            System.out.println("Cannot start client " + "'" + this.name + "'" + ", client is not stopped");
        return result;
    }

    /**
     * TODO: Definición de {@code stop}.
     *
     * @return
     */
    public boolean stop() {
        boolean result = false;
        if (!stopped) {
            this.stopped = true;
            this.connected = false;
            try {
                serverSocket.close();
            } catch (Exception e) {
            }
            synchronized (synchronizer) {
                synchronizer.notifyAll();
            }
            serverSocket = null;
            result = true;
            System.out.println("Client " + "'" + this.name + "'" + " stopped");
        } else
            System.out.println("Cannot stop, client " + "'" + this.name + "'" + " is stopped");
        return result;
    }

    /**
     * TODO: Definición de {@code killClient}.
     *
     */
    public void killClient() {
        this.killed = true;
        synchronized (synchronizer) {
            synchronizer.notifyAll();
        }
    }

    /**
     * TODO: Definición de {@code sendStackMessages}.
     *
     * @return
     */
    private boolean sendStackMessages() {
        boolean result = true;
        while (!messagesToSend.isEmpty()) {
            try {
                lastMessage = this.messagesToSend.peek();
                if (lastMessage != null) {
                    System.out.println("Sending message: " + "'" + name + "'" + " (" + clientIpAddress + ":" + clientPort + " to " + serverIpAddress + ":" + serverPort + ")" + " '" + lastMessage + "'" + " (" + messagesToSend.size() + " in queue)");
                    socketOutputStream.write((lastMessage + "\n").getBytes());
                    System.out.println("Message sent:    " + "'" + name + "'" + " (" + clientIpAddress + ":" + clientPort + " to " + serverIpAddress + ":" + serverPort + ")" + " '" + lastMessage + "'" + " (" + messagesToSend.size() + " in queue)");
                    this.messagesToSend.poll();
                    try {
                        if (this.onMessageListener != null) {
                            if (processListenersOnOtherThread)
                                new Thread(() -> this.onMessageListener.onMessage(this, serverSocket, lastMessage)).start();
                            else
                                this.onMessageListener.onMessage(this, serverSocket, lastMessage);
                        }
                    } catch (Exception ex) {
                        ListenerException listener = new ListenerException("Error proccessing method onMessage", ex);
                        listener.printStackTrace(System.out);
                    }
                } else {
                    System.out.println("Cannot send: " + "'" + name + "'" + " (" + clientIpAddress + ":" + clientPort + " to " + serverIpAddress + ":" + serverPort + ")" + " '" + lastMessage + "'" + " (" + messagesToSend.size() + " in queue)");
                    messagesToSend.peek();
                }
            } catch (Exception e) {
                System.out.println("Error sending message from " + "'" + this.name + "'" + " to " + this.serverIpAddress + ":" + this.serverPort);
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * TODO: Definición de {@code sendMessage}.
     *
     * @param message
     * @return
     */
    public boolean sendMessage(String message) {
        boolean result = false;
        this.messagesToSend.add(message);
        if (!this.connected || this.connecting || this.stopped) {
            System.out.println("Cannot send message now, message will send when client " + "'" + this.name + "'" + " is connected.");
        } else {
            if (sendStackMessages()) {
                result = true;
            } else {
                System.out.println("Possibly broken pipe in client " + "'" + this.name + "'" + ", init again.");
                System.out.println("Message will send when client " + "'" + this.name + "'" + " is connected.");
                this.connected = false;
                try {
                    this.socketInputStream.close();
                } catch (Exception e) {
                }
                this.socketInputStream = null;
            }
        }
        return result;
    }

    /**
     * TODO: Definición de {@code cleanQueueMessages}.
     *
     * @return
     */
    public boolean cleanQueueMessages() {
        this.messagesToSend.clear();
        return true;
    }

    /**
     * TODO: Definición de {@code run}.
     *
     */
    @Override
    public void run() {
        initialize();
        int nullMessages = 0;
        while (!killed) {
            if (this.socketInputStream == null) {
                if (autoRetryConnection)
                    this.init();
                try {
                    synchronized (synchronizer) {
                        synchronizer.wait();
                    }
                } catch (InterruptedException e) {
                }
            }
            try {
                try {
                    lastResponse = socketInputStream.readLine();
                } catch (IOException e) {
                    lastResponse = null;
                }
                //System.out.println("RESP: '" + lastResponse + "'");
                if (lastResponse == null) {
                    nullMessages++;
                    if (nullMessages == 10) {
                        socketInputStream = null;
                        nullMessages = 0;
                        this.connected = false;
                        System.out.println("Server " + "'" + this.name + "'" + " is possibly disconnected.");
                        try {
                            if (this.onConnectionListener != null) {
                                if (processListenersOnOtherThread)
                                    new Thread(() -> this.onConnectionListener.onDisconnectedClient(this, serverSocket)).start();
                                else
                                    this.onConnectionListener.onDisconnectedClient(this, serverSocket);
                            }
                        } catch (Exception ex) {
                            ListenerException listener = new ListenerException("Error proccessing method onDisconnectedClient", ex);
                            listener.printStackTrace(System.out);
                        }
                    }
                } else {
                    nullMessages = 0;
                    try {
                        if (this.onMessageListener != null) {
                            if (processListenersOnOtherThread)
                                new Thread(() -> this.onMessageListener.onResponse(this, serverSocket, lastResponse)).start();
                            else
                                this.onMessageListener.onResponse(this, serverSocket, lastResponse);
                        }
                    } catch (Exception ex) {
                        ListenerException listener = new ListenerException("Error proccessing method onResponse", ex);
                        listener.printStackTrace(System.out);
                    }
                }
            } catch (Exception e) {
            }
        }
        System.out.println("Client killed");
        try {
            serverSocket.close();
        } catch (Exception e) {
        }
    }

}
