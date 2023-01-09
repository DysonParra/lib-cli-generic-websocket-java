/*
 * @fileoverview    {ClientMessageListener} se encarga de realizar tareas específicas.
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

import java.net.Socket;

/**
 * TODO: Definición de {@code ClientMessageListener}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public interface ClientMessageListener {

    /**
     * TODO: Definición de {@code onMessage}.
     *
     * @param client
     * @param sentMessage
     * @param server
     */
    public abstract void onMessage(GenericClient client, Socket server, String sentMessage);

    /**
     * TODO: Definición de {@code onResponse}.
     *
     * @param client
     * @param response
     * @param server
     */
    public abstract void onResponse(GenericClient client, Socket server, String response);
}
