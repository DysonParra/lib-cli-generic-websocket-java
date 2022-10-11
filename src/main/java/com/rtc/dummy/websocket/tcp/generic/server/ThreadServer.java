/*
 * @fileoverview {ThreadServer} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {ThreadServer} fue realizada el 31/07/2022.
 * @Dev - La primera version de {ThreadServer} fue escrita por Dyson A. Parra T.
 */
package com.rtc.dummy.websocket.tcp.generic.server;

import com.rtc.dummy.websocket.tcp.generic.exception.ListenerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO: Definición de {@code ThreadServer}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class ThreadServer implements Runnable {

    protected String name;
    @Setter(AccessLevel.NONE)
    protected Socket sender;
    @Setter(AccessLevel.NONE)
    protected List<Socket> clients;
    @Setter(AccessLevel.NONE)
    protected GenericServer genericServer;
    protected ServerMessageListener onMessageListener = null;
    protected ServerConnectionListener onConnectionListener = null;
    protected boolean processListenersOnOtherThread = false;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected int nullMessages = 0;

    /**
     * TODO: Definición de {@code ThreadServer}.
     *
     * @param genericServer
     * @param sender
     * @param clients
     */
    public ThreadServer(GenericServer genericServer, Socket sender, List<Socket> clients) {
        this.genericServer = genericServer;
        this.sender = sender;
        this.clients = clients;
        if (sender != null)
            this.name = sender.getPort() + "";
    }

    /**
     * TODO: Definición de {@code ThreadServer}.
     *
     * @param genericServer
     * @param sender
     * @param clients
     * @param name
     */
    public ThreadServer(GenericServer genericServer, Socket sender, List<Socket> clients, String name) {
        this(genericServer, sender, clients);
        this.name = name;
    }

    /**
     * TODO: Definición de {@code ThreadServer}.
     *
     * @param genericServer
     * @param sender
     * @param clients
     * @param name
     * @param processListenersOnOtherThread
     */
    public ThreadServer(GenericServer genericServer, Socket sender, List<Socket> clients, String name, boolean processListenersOnOtherThread) {
        this(genericServer, sender, clients, name);
        this.processListenersOnOtherThread = processListenersOnOtherThread;
    }

    /**
     * TODO: Definición de {@code kill}.
     *
     */
    public void kill() {
        this.nullMessages = 10;
        try {
            this.sender.close();
        } catch (Exception e) {
        }
    }

    /**
     * TODO: Definición de {@code sendMessageToClient}.
     *
     * @param message
     * @param maxTries
     */
    public void sendMessageToClient(String message, int maxTries) {
        OutputStream socketOutputStream;
        for (int i = 1; i <= maxTries; i++) {
            try {
                System.out.println("Sending " + "'" + message + "'" + " to " + "'" + sender.getPort() + "'" + " from server " + "'" + this.name + "'" + " (Trie " + i + ")");
                socketOutputStream = sender.getOutputStream();
                socketOutputStream.write((message + "\n").getBytes());
                System.out.println("Sent " + "'" + message + "'" + " to " + "'" + sender.getPort() + "'" + " from server " + "'" + this.name + "'");
                break;
            } catch (IOException ex) {
                System.out.println("Error sending " + "'" + message + "'" + " to " + "'" + sender.getPort() + "'" + " from server " + "'" + this.name + "'" + " (Trie " + i + ")");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
     * TODO: Definición de {@code sendMessageToAllClients}.
     *
     * @param message
     * @param sendToSender
     * @param maxTries
     */
    public void sendMessageToAllClients(String message, boolean sendToSender, int maxTries) {
        if (sendToSender)
            this.genericServer.sendMessageToAllClients(message, null, maxTries);
        else
            this.genericServer.sendMessageToAllClients(message, Arrays.asList(sender), maxTries);
    }

    /**
     * TODO: Definición de {@code run}.
     *
     */
    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(sender.getInputStream()));
            while (nullMessages <= 10) {

                String request = input.readLine();
                if (request == null) {
                    nullMessages++;
                    if (nullMessages == 10) {
                        nullMessages = 0;
                        System.out.println("Client in port " + this.getSender().getPort() + " is possibly disconnected of server " + "'" + this.name + "'");
                        try {
                            if (onConnectionListener != null) {
                                if (processListenersOnOtherThread)
                                    new Thread(() -> this.onConnectionListener.onDisconnectedClient(this)).start();
                                else
                                    this.onConnectionListener.onDisconnectedClient(this);
                            }
                        } catch (Exception ex) {
                            ListenerException listener = new ListenerException("Error proccessing method onDisconnectedClient", ex);
                            listener.printStackTrace(System.out);
                        }
                        throw new SocketException();
                    }
                } else {
                    nullMessages = 0;
                    try {
                        if (this.onMessageListener != null) {
                            if (processListenersOnOtherThread)
                                new Thread(() -> this.onMessageListener.onRequest(request, this)).start();
                            else
                                this.onMessageListener.onRequest(request, this);
                        }
                    } catch (Exception ex) {
                        ListenerException listener = new ListenerException("Error proccessing method onRequest", ex);
                        listener.printStackTrace(System.out);
                    }
                }
            }
        } catch (SocketException e) {
            clients.remove(sender);
        } catch (Exception e) {
            System.out.println("Unknown error.");
        }
    }
}
