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
 * TODO: Description of {@code ServerConnectionListener}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public interface ServerConnectionListener {

    /**
     * TODO: Description of method {@code onConnectedClient}.
     *
     * @param connected
     */
    public abstract void onConnectedClient(ThreadServer connected);

    /**
     * TODO: Description of method {@code onDisconnectedClient}.
     *
     * @param disconnected
     */
    public abstract void onDisconnectedClient(ThreadServer disconnected);
}
