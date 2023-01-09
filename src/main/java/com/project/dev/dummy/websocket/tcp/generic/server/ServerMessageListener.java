/*
 * @fileoverview    {ServerMessageListener} se encarga de realizar tareas específicas.
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
package com.project.dev.dummy.websocket.tcp.generic.server;

/**
 * TODO: Definición de {@code ServerMessageListener}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public interface ServerMessageListener {

    /**
     * TODO: Definición de {@code onRequest}.
     *
     * @param message
     * @param receiver
     */
    public abstract void onRequest(String message, ThreadServer receiver);
}
