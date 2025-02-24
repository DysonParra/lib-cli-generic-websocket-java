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
 * TODO: Description of {@code ListenerException}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public class ListenerException extends Exception {

    /**
     * TODO: Description of {@code ListenerException}.
     *
     * @param errorMessage
     * @param err
     */
    public ListenerException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
