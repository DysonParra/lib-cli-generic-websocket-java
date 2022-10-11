/*
 * @fileoverview {ListenerException} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {ListenerException} fue realizada el 31/07/2022.
 * @Dev - La primera version de {ListenerException} fue escrita por Dyson A. Parra T.
 */
package com.rtc.dummy.websocket.tcp.generic.exception;

/**
 * TODO: Definición de {@code ListenerException}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class ListenerException extends Exception {

    /**
     * TODO: Definición de {@code ListenerException}.
     *
     * @param errorMessage
     * @param err
     */
    public ListenerException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
