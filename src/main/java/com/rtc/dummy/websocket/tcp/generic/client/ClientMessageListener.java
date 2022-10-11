/*
 * @fileoverview {ClientMessageListener} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {ClientMessageListener} fue realizada el 31/07/2022.
 * @Dev - La primera version de {ClientMessageListener} fue escrita por Dyson A. Parra T.
 */
package com.rtc.dummy.websocket.tcp.generic.client;

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
