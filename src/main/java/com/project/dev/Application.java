/*
 * @fileoverview    {Application}
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
package com.project.dev;

import com.project.dev.tester.DummyClient;
import com.project.dev.tester.DummyServer;
import java.util.Scanner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * TODO: Description of {@code Application}.
 *
 * @author Dyson Parra
 * @since 11
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
     * TODO: Description of {@code run}.
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
