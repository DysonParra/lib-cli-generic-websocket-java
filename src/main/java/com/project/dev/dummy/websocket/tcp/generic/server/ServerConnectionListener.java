/*
 * @fileoverview    {ServerConnectionListener}
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

/**
 * TODO: Definición de {@code ServerConnectionListener}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public interface ServerConnectionListener {

    /**
     * TODO: Definición de {@code onConnectedClient}.
     *
     * @param connected
     */
    public abstract void onConnectedClient(ThreadServer connected);

    /**
     * TODO: Definición de {@code onDisconnectedClient}.
     *
     * @param disconnected
     */
    public abstract void onDisconnectedClient(ThreadServer disconnected);
}
