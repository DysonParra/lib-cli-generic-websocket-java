/*
 * @fileoverview    {GenericServer}
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementation done.
 * @version 2.0     Documentation added.
 */
package com.project.dev.dummy.websocket.tcp.generic.server;

import com.project.dev.dummy.websocket.tcp.generic.exception.ListenerException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO: Definición de {@code GenericServer}.
 *
 * @author Dyson Parra
 * @since 11
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class GenericServer implements Runnable {

    protected String name;
    protected int serverPort = 5000;
    @Setter(AccessLevel.NONE)
    protected List<Socket> clients;
    @Setter(AccessLevel.NONE)
    protected List<ThreadServer> servers;
    @Setter(AccessLevel.NONE)
    protected boolean started = false;
    @Setter(AccessLevel.NONE)
    protected boolean stopped = true;
    @Setter(AccessLevel.NONE)
    protected boolean killed = false;
    protected boolean validPort = true;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected ServerSocket serversocket = null;
    protected ServerMessageListener onMessageListener = null;
    protected ServerConnectionListener onConnectionListener = null;
    protected ServerScheduledMessage scheduledMessage;
    protected boolean processListenersOnOtherThread = false;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected final Object synchronizer = new Object();

    /**
     * TODO: Definición de {@code GenericServer}.
     *
     */
    public GenericServer() {
        clients = new ArrayList<>();
        servers = new ArrayList<>();
    }

    /**
     * TODO: Definición de {@code GenericServer}.
     *
     * @param serverPort
     */
    public GenericServer(int serverPort) {
        this();
        this.serverPort = serverPort;
        this.name = "sever:" + this.serverPort + "";
    }

    /**
     * TODO: Definición de {@code GenericServer}.
     *
     * @param serverPort
     * @param processListenersOnOtherThread
     */
    public GenericServer(int serverPort, boolean processListenersOnOtherThread) {
        this(serverPort);
        this.processListenersOnOtherThread = processListenersOnOtherThread;
    }

    /**
     * TODO: Definición de {@code setName}.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        this.servers.forEach(server -> server.setName(name));
    }

    /**
     * TODO: Definición de {@code setServerPort}.
     *
     * @param serverPort
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
        this.name = "sever:" + this.serverPort + "";
        if (!this.stopped)
            this.stop();
        if (this.scheduledMessage != null && this.scheduledMessage.isStarted())
            this.scheduledMessage.stopScheduling();
        this.clients.clear();
        servers.forEach(ThreadServer::kill);
        this.servers.clear();
        try {
            serversocket.close();
        } catch (IOException e) {
        }
    }

    /**
     * TODO: Definición de {@code setOnMessageListener}.
     *
     * @param onMessageListener
     */
    public void setOnMessageListener(ServerMessageListener onMessageListener) {
        this.onMessageListener = onMessageListener;
        this.servers.forEach(server -> server.setOnMessageListener(onMessageListener));
    }

    /**
     * TODO: Definición de {@code setOnConnectionListener}.
     *
     * @param onConnectionListener
     */
    public void setOnConnectionListener(ServerConnectionListener onConnectionListener) {
        this.onConnectionListener = onConnectionListener;
        this.servers.forEach(server -> server.setOnConnectionListener(onConnectionListener));
    }

    /**
     * TODO: Definición de {@code setScheduledMessage}.
     *
     * @param ScheduledMessage
     */
    public void setScheduledMessage(ServerScheduledMessage ScheduledMessage) {
        if (ScheduledMessage == null && this.scheduledMessage != null) {
            this.scheduledMessage.killScheduling();
            this.scheduledMessage = ScheduledMessage;
        } else {
            this.scheduledMessage = ScheduledMessage;
            if (this.scheduledMessage != null) {
                this.scheduledMessage.setGenericServer(this);
            }
        }
    }

    /**
     * TODO: Definición de {@code startScheduledMessages}.
     *
     * @return
     */
    public boolean startScheduledMessages() {
        boolean result = false;
        if (!killed) {
            if (!validPort)
                System.out.println("Error starting scheduled messages, server '" + name + "' is not initialized");
            else if (this.scheduledMessage != null) {
                if (this.scheduledMessage.isRun()) {
                    result = scheduledMessage.startScheduling();
                } else {
                    new Thread(scheduledMessage::run).start();
                    result = true;
                }
            } else
                System.out.println("Cannot start scheduled messages, No scheduled message specified.");
        } else
            System.out.println("Error executing operation, server is killed.");
        return result;
    }

    /**
     * TODO: Definición de {@code stopScheduledMessages}.
     *
     * @return
     */
    public boolean stopScheduledMessages() {
        boolean result = false;
        if (!killed) {
            if (this.scheduledMessage != null) {
                result = this.scheduledMessage.stopScheduling();
            } else
                System.out.println("Cannot stop scheduled messages, No scheduled message specified.");
        } else
            System.out.println("Error executing operation, server is killed.");
        return result;
    }

    /**
     * TODO: Definición de {@code init}.
     *
     */
    public void init() {
        if (!killed) {
            try {
                serversocket = new ServerSocket(this.serverPort);
                System.out.println("Server '" + this.name + "' initilized in port " + serversocket.getLocalPort());
                validPort = true;

                try {
                    synchronized (synchronizer) {
                        synchronizer.notifyAll();
                    }
                } catch (Exception e) {
                }
            } catch (Exception e) {
                System.out.println("Error initializing in port " + this.serverPort);
                validPort = false;
            }
        } else
            System.out.println("Error executing operation, server is killed.");
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
            this.started = false;
            System.out.println("Server started in " + "'" + this.name + "'");
            result = true;
        } else
            System.out.println("Cannot start, server is not stopped");
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
            this.started = false;
            result = true;
            System.out.println("Server stopped from server " + "'" + this.name + "'");
        } else
            System.out.println("Cannot stop, Server is stopped");
        return result;
    }

    /**
     * TODO: Definición de {@code killServer}.
     *
     */
    public void killServer() {
        this.killed = true;
        setScheduledMessage(null);
        this.clients.clear();
        this.servers.forEach(ThreadServer::kill);
        this.servers.clear();
        try {
            serversocket.close();
        } catch (IOException e) {
        }
        synchronized (synchronizer) {
            synchronizer.notifyAll();
        }
    }

    /**
     * TODO: Definición de {@code sendMessageToAllClients}.
     *
     * @param message
     * @param maxTries
     * @param notSendToThese
     */
    public void sendMessageToAllClients(String message, List<Socket> notSendToThese, int maxTries) {
        if (!killed) {
            System.out.println("Sending " + "'" + message + "'" + " to " + "All" + " from server " + "'" + this.name + "'");
            OutputStream socketOutputStream;
            if (this.clients.isEmpty())
                System.out.println("No connected clients to " + "'" + this.name + "'");
            for (Socket socket : this.clients) {
                for (int i = 1; i <= maxTries; i++) {
                    try {
                        if (notSendToThese == null || !notSendToThese.contains(socket)) {
                            System.out.println("Sending " + "'" + message + "'" + " to " + "'" + socket.getPort() + "'" + " from server " + "'" + this.name + "'" + " (Trie " + i + ")");
                            socketOutputStream = socket.getOutputStream();
                            socketOutputStream.write((message + "\n").getBytes());
                            System.out.println("Sent " + "'" + message + "'" + " to " + "'" + socket.getPort() + "'" + " from server " + "'" + this.name + "'");
                            break;
                        }
                    } catch (IOException ex) {
                        System.out.println("Error sending " + "'" + message + "'" + " to " + "'" + socket.getPort() + "'" + " from server " + "'" + this.name + "'" + " (Trie " + i + ")");
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        } else
            System.out.println("Cannot send messages, server is killed");
    }

    /**
     * TODO: Definición de {@code run}.
     *
     */
    @Override
    public void run() {
        if (!killed) {
            init();
            while (!killed) {
                try {
                    if (serversocket == null) {
                        try {
                            synchronized (synchronizer) {
                                System.out.println("Waiting to server '" + this.name + "' initialize");
                                synchronizer.wait();
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                    Socket socket = serversocket.accept();
                    System.out.println("Client connected to server '" + this.name + "' in port " + socket.getPort());
                    clients.add(socket);
                    ThreadServer server = new ThreadServer(this, socket, clients, name, processListenersOnOtherThread);
                    server.setOnMessageListener(onMessageListener);
                    server.setOnConnectionListener(onConnectionListener);
                    servers.add(server);
                    try {
                        if (onConnectionListener != null) {
                            if (processListenersOnOtherThread)
                                new Thread(() -> this.onConnectionListener.onConnectedClient(server)).start();
                            else
                                this.onConnectionListener.onConnectedClient(server);
                        }
                    } catch (Exception ex) {
                        ListenerException listener = new ListenerException("Error proccessing method onConnectedClient", ex);
                        listener.printStackTrace(System.out);
                    }
                    new Thread(server).start();
                } catch (Exception ex) {
                }
            }
            System.out.println("Server killed");
        } else
            System.out.println("Cannot start server, server is killed");
    }
}
