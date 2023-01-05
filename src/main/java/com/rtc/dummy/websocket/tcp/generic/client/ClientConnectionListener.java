/*
 * @fileoverview    {ClientConnectionListener} se encarga de realizar tareas específicas.
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
package com.rtc.dummy.websocket.tcp.generic.client;

import java.net.Socket;

/**
 * TODO: Definición de {@code ClientConnectionListener}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public interface ClientConnectionListener {

    /**
     * TODO: Definición de {@code onConnectedClient}.
     *
     * @param client
     * @param server
     */
    public abstract void onConnectedClient(GenericClient client, Socket server);

    /**
     * TODO: Definición de {@code onDisconnectedClient}.
     *
     * @param client
     * @param server
     */
    public abstract void onDisconnectedClient(GenericClient client, Socket server);

    /**
     * TODO: Definición de {@code onErrorConnectingToServer}.
     *
     * @param client
     * @param tries
     * @param serverIpAddress
     * @param serverPort
     */
    public abstract void onErrorConnectingToServer(GenericClient client, String serverIpAddress, int serverPort, int tries);
}
