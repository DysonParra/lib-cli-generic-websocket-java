/*
 * @fileoverview {FileName} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {FileName} fue realizada el 31/07/2022.
 * @Dev - La primera version de {FileName} fue escrita por Dyson A. Parra T.
 */
package com.rtc.dummy.websocket.tcp.generic.server;

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
