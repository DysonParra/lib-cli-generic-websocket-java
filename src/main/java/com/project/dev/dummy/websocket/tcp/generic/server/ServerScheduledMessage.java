/*
 * @fileoverview    {ServerScheduledMessage}
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

import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * TODO: Definición de {@code ServerScheduledMessage}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public abstract class ServerScheduledMessage implements Runnable {

    private GenericServer genericServer;
    private long timeBetweenMessage = 3000;
    @Setter(AccessLevel.NONE)
    private boolean started = true;
    @Setter(AccessLevel.NONE)
    private boolean run = false;
    @Setter(AccessLevel.NONE)
    private boolean killed = false;
    private final Object synchronizer = new Object();

    /**
     * TODO: Definición de {@code ServerScheduledMessage}.
     *
     */
    public ServerScheduledMessage() {

    }

    /**
     * TODO: Definición de {@code ServerScheduledMessage}.
     *
     * @param timeBetweenMessage
     */
    public ServerScheduledMessage(long timeBetweenMessage) {
        this.timeBetweenMessage = timeBetweenMessage;
    }

    /**
     * TODO: Definición de {@code setTimeBetweenMessage}.
     *
     * @param timeBetweenMessage
     */
    public void setTimeBetweenMessage(long timeBetweenMessage) {
        this.timeBetweenMessage = timeBetweenMessage;
        if (started) {
            synchronized (synchronizer) {
                synchronizer.notifyAll();
            }
        }
    }

    /**
     * TODO: Definición de {@code startScheduling}.
     *
     * @return
     */
    public boolean startScheduling() {
        boolean result = false;
        if (!this.started) {
            this.started = true;
            synchronized (synchronizer) {
                synchronizer.notifyAll();
            }
            result = true;
        } else
            System.out.println("Cannot start scheduled message, scheduled is not stopped.");
        return result;
    }

    /**
     * TODO: Definición de {@code stopScheduling}.
     *
     * @return
     */
    public boolean stopScheduling() {
        boolean result = false;
        if (this.started) {
            this.started = false;
            synchronized (synchronizer) {
                synchronizer.notifyAll();
            }
            result = true;
        } else
            System.out.println("Cannot stop scheduled message, scheduled is not started.");
        return result;
    }

    /**
     * TODO: Definición de {@code killScheduling}.
     *
     */
    public void killScheduling() {
        this.killed = true;
        synchronized (synchronizer) {
            synchronizer.notifyAll();
        }
    }

    /**
     * TODO: Definición de {@code calculateMesageToSend}.
     *
     * @return
     */
    public abstract List<String> calculateMesageToSend();

    /**
     * TODO: Definición de {@code run}.
     *
     */
    @Override
    public void run() {
        this.run = true;
        while (!killed) {
            try {
                if (!started) {
                    synchronized (synchronizer) {
                        synchronizer.wait();
                    }
                } else {
                    if (this.genericServer != null) {
                        System.out.println("Sending scheduled message...");
                        calculateMesageToSend().forEach(line -> genericServer.sendMessageToAllClients(line, null, 3));
                    } else {
                        System.out.println("Cannot send scheduled message, server is not specified.");
                    }
                    synchronized (synchronizer) {
                        synchronizer.wait(timeBetweenMessage);
                    }
                }
            } catch (InterruptedException ex) {
                System.out.println("Error waiting to send scheduled message..." + "\n");
            }
        }
        System.out.println("Scheduled message killed...");
    }
}
