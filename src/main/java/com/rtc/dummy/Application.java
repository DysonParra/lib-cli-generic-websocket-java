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
package com.rtc.dummy;

import java.util.Scanner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * TODO: Definición de {@code Application}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
@SpringBootApplication
@EnableScheduling
public class Application implements CommandLineRunner {

    /**
     * Entrada principal del sistema.
     *
     * @param args argumentos de la linea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * TODO: Definición de {@code run}.
     *
     * @param args
     * @throws java.lang.Exception
     */
    @Override
    public void run(String[] args) throws Exception {
        String type;
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nSet type of socket");
            System.out.println("1: Server");
            System.out.println("2: Client");
            System.out.println("Other to exit");
            type = sc.nextLine();
            if (type.equals("1"))
                DummyServer.startTcpServer();
            else if (type.equals("2"))
                DummyClient.startTcpClient();
            else
                break;
        }
        System.exit(0);
    }
}
