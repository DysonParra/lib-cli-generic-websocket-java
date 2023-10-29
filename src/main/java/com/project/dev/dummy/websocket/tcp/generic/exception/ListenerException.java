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
 * @version 1.0     Implementation done.
 * @version 2.0     Documentation added.
 */
package com.project.dev.dummy.websocket.tcp.generic.exception;

/**
 * TODO: Definición de {@code ListenerException}.
 *
 * @author Dyson Parra
 * @since 11
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
