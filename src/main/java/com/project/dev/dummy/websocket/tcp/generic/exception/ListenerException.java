/*
 * @fileoverview    {ListenerException}
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
package com.project.dev.dummy.websocket.tcp.generic.exception;

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
