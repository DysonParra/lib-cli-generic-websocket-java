/*
 * @fileoverview {ServerMessageListener} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {ServerMessageListener} fue realizada el 31/07/2022.
 * @Dev - La primera version de {ServerMessageListener} fue escrita por Dyson A. Parra T.
 */
package com.rtc.dummy.websocket.tcp.generic.server;

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
