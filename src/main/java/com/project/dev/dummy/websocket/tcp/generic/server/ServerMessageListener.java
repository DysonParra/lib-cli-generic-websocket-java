/*
 * @overview        {ServerMessageListener}
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
 * TODO: Description of {@code ServerMessageListener}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public interface ServerMessageListener {

    /**
     * TODO: Description of method {@code onRequest}.
     *
     * @param message
     * @param receiver
     */
    public abstract void onRequest(String message, ThreadServer receiver);
}
