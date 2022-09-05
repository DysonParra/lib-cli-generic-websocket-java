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

import com.rtc.dummy.websocket.tcp.generic.client.ClientConnectionListener;
import com.rtc.dummy.websocket.tcp.generic.client.ClientMessageListener;
import com.rtc.dummy.websocket.tcp.generic.client.GenericClient;
import java.net.Socket;
import java.util.Scanner;

/**
 * TODO: Definición de {@code DummyClient}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class DummyClient {

    private static int CLIENT_TYPE = 1;
    private static final int TYPE_PLC = 1;
    private static final int TYPE_PLATE = 2;
    private static final int TYPE_GENERIC = 3;
    private static boolean AUTO_RETRY_CONNECTION = false;
    private static int nroCamera = 1;
    private static boolean randomPlate = true;
    private static String plate = "";
    private static boolean getPlateFrame = false;
    private static int DELAY_TIME = 500;

    /**
     * TODO: Definición de {@code startTcpClient}.
     *
     */
    public static void startTcpClient() {
        String reply;
        Scanner sc = new Scanner(System.in);

        // $MSJ313#MSJ%
        // $VEL05%
        // $MSJ313#DIRIJASE A VIA NACIONAL%
        // $MSJ313#EN MANTENIMIENTO%
        String IP;
        int PORT;
        IP = "127.0.0.1";
        PORT = 5000;

        System.out.println("\nWrite client type:");
        System.out.println("1: PLC");
        System.out.println("2: Plate");
        System.out.println("3: Generic");
        while (true) {
            reply = sc.nextLine();
            try {
                CLIENT_TYPE = Integer.parseInt(reply);
                switch (CLIENT_TYPE) {
                    case TYPE_PLC:
                    case TYPE_PLATE:
                    case TYPE_GENERIC:
                        break;
                    default:
                        throw new Exception();
                }
                break;
            } catch (Exception e) {
                System.out.println("Write a valid client type");
            }
        }

        System.out.println("\nWrite server ip");
        while (true) {
            reply = sc.nextLine();
            try {
                IP = reply;
                System.out.println("New server ip is: " + IP);
                break;
            } catch (Exception e) {
                System.out.println("Write a valid ip");
            }
        }

        System.out.println("\nWrite server port");
        while (true) {
            reply = sc.nextLine();
            try {
                int newPort = Integer.parseInt(reply);
                if (newPort < 0)
                    throw new Exception();
                PORT = newPort;
                System.out.println("New server port is: " + PORT);
                break;
            } catch (Exception e) {
                System.out.println("Write a valid port");
            }
        }

        System.out.println("\nAuto retry connection?:");
        System.out.println("1: Yes");
        System.out.println("0: No");
        while (true) {
            reply = sc.nextLine();
            try {
                int retry = Integer.parseInt(reply);
                switch (retry) {
                    case 1:
                        AUTO_RETRY_CONNECTION = true;
                        break;
                    case 0:
                        AUTO_RETRY_CONNECTION = false;
                        break;
                    default:
                        throw new Exception();
                }
                break;
            } catch (Exception e) {
                System.out.println("Write a valid option");
            }
        }

        boolean result = false;
        GenericClient genericClient = new GenericClient(IP, PORT, 3000, -1, AUTO_RETRY_CONNECTION);

        // Comportamiento cuando envia un mensaje a un servidor y cuando recibe respuesta.
        genericClient.setOnMessageListener(new ClientMessageListener() {
            @Override
            public void onMessage(GenericClient client, Socket server, String sentMessage) {
                //System.out.println("onMessage: " + "'" + sentMessage + "'");
                String clientIp = server.getLocalAddress().getHostAddress();
                String serverIp = server.getInetAddress().getHostAddress();
                int serverPort = server.getPort();
                int clientPort = server.getLocalPort();
                System.out.println("onMessage:       "
                        + "'" + client.getName() + "'" + " (" + clientIp + ":" + clientPort + " to " + serverIp + ":" + serverPort
                        + ") " + "'" + sentMessage + "'");
                //int a[] = null; int b = a[-1];
            }

            @Override
            public void onResponse(GenericClient client, Socket server, String response) {
                //System.out.println("onResponse: " + "'" + response + "'");
                String clientIp = server.getLocalAddress().getHostAddress();
                String serverIp = server.getInetAddress().getHostAddress();
                int serverPort = server.getPort();
                int clientPort = server.getLocalPort();
                System.out.println("onResponse:      "
                        + "'" + client.getName() + "'" + " (" + serverIp + ":" + serverPort + " to " + clientIp + ":" + clientPort
                        + ") " + "'" + response + "'");

                if (CLIENT_TYPE == TYPE_PLC) {
                    if (!response.matches(".*?99")) {
                        System.out.println("PLC get command from server '" + response + "'");
                        try {
                            Thread.sleep(DELAY_TIME);
                        } catch (InterruptedException e) {
                        }
                        genericClient.sendMessage(response.substring(0, 1) + "99");
                    } else {
                        System.out.println("PLC get response from server '" + response + "'");
                    }
                    //int a[] = null; int b = a[-1];
                }
            }
        });

        genericClient.setOnConnectionListener(new ClientConnectionListener() {
            @Override
            public void onConnectedClient(GenericClient client, Socket server) {
                System.out.println("onConnectedClient: '" + client.getName() + "'");
                //int a[] = null; int b = a[-1];
            }

            @Override
            public void onDisconnectedClient(GenericClient client, Socket server) {
                System.out.println("onDisconnectedClient: '" + client.getName() + "'");
                //int a[] = null; int b = a[-1];
            }

            @Override
            public void onErrorConnectingToServer(GenericClient client, String serverIpAddress, int serverPort, int tries) {
                System.out.println(" onErrorConnectingToServer: '" + client.getName() + "'" + ", tries: " + tries);
                //int a[] = null; int b = a[-1];
            }
        });

        // All
        String TIME_PATTERN = "time \\d+";
        String IP_PATTERN = "ip .*?";
        String PORT_PATTERN = "port \\d+";
        String NAME_PATTERN = "name .*?";
        String AUTO_RETRY_PATTERN = "auto [0-1]";

        // PLC
        String DELAY_PATTERN = "delay \\d+";

        // Plate
        String SEND_PLATE_PATTERN = "send plate";
        String NRO_CAM_PATTERN = "nroCam \\d";
        String PLATE_RAND_PATTERN = "plate Rand";
        String PLATE_EMPTY_PATTERN = "plate Empty";
        String PLATE_PATTERN = "plate ([A-Za-z]|\\d){6}";
        String FRAME_PATTERN = "frame \\d";

        // Generic
        String SEND_PATTERN = "send .*?";

        System.out.println("Commands:");
        System.out.println("'start': Start client if is stopped");
        System.out.println("'stop': Stop client and stop trying connection (initialize) if aply");
        System.out.println("'exit': End execution.");
        System.out.println("'initialize': If is started try to connect to server.");
        System.out.println("'init': Same that initialize, but in another thread (Recommended).");
        System.out.println("'clear': Clean the messages queue.");
        System.out.println("'" + IP_PATTERN + "': Edit the ip of the server and disconnect from server.");
        System.out.println("'" + PORT_PATTERN + "': Edit the port of the server and disconnect from server.");
        System.out.println("'" + TIME_PATTERN + "': Change the time between each retry connection.");
        System.out.println("'" + NAME_PATTERN + "': Change the name of the client.");
        System.out.println("'" + AUTO_RETRY_PATTERN + "': Change if auto retry connection (1 = yes, 0 = no).");

        switch (CLIENT_TYPE) {
            case TYPE_PLC:
                System.out.println("'" + DELAY_PATTERN + "': Set the delay time in plc response.");
                System.out.println("'" + SEND_PATTERN + "': Send a custom message to server or add to queue if is not connected to one.");
                genericClient.setName("PLC " + genericClient.getServerIpAddress() + ":" + genericClient.getServerPort());
                break;

            case TYPE_GENERIC:
                System.out.println("'" + SEND_PATTERN + "': Send a custom message to server or add to queue if is not connected to one.");
                genericClient.setName("Generic " + genericClient.getServerIpAddress() + ":" + genericClient.getServerPort());
                break;

            case TYPE_PLATE:
                System.out.println("'" + SEND_PLATE_PATTERN + "': Send a message to server with the plate configurarion");
                System.out.println("'" + NRO_CAM_PATTERN + "': Change the type of camera (1 = dinamica, other = estatica)");
                System.out.println("'" + PLATE_RAND_PATTERN + "': Change the plate to send in random.");
                System.out.println("'" + PLATE_EMPTY_PATTERN + "': Change the plate to send a empty plate.");
                System.out.println("'" + PLATE_PATTERN + "': Change a specific plate to send in each message.");
                System.out.println("'" + FRAME_PATTERN + "': Chage if get a frame in base 64 for each plate (1=true, other=false).");
                genericClient.setName("Plate " + genericClient.getServerIpAddress() + ":" + genericClient.getServerPort());
                break;
        }
        System.out.println("");

        new Thread(genericClient).start();
        System.out.println("\nWrite replies...");
        do {
            reply = sc.nextLine();
            switch (reply) {
                case "init":
                    genericClient.init();
                    System.out.println("Result Init: " + null);
                    break;
                case "initialize":
                    result = genericClient.initialize();
                    System.out.println("Result Initialize: " + result);
                    break;
                //case "run":
                //    new Thread(genericClient).start();
                //    System.out.println("Result Run: " + null);
                //    break;
                case "start":
                    result = genericClient.start();
                    System.out.println("Result Start: " + result);
                    break;
                case "stop":
                    result = genericClient.stop();
                    System.out.println("Result Stop: " + result);
                    break;
                case "clear":
                    result = genericClient.cleanQueueMessages();
                    System.out.println("Result Clear: " + result);
                    break;
                case "exit":
                    result = genericClient.stop();
                    genericClient.killClient();
                    System.out.println("Result Exit: " + result);
                    break;
                default:
                    if (reply.matches(TIME_PATTERN)) {
                        long newTime = Long.parseLong(reply.replace("time ", ""));
                        genericClient.setRetryTime(newTime);
                        System.out.println("New time: " + newTime);
                    } else if (reply.matches(PORT_PATTERN)) {
                        PORT = Integer.parseInt(reply.substring(5));
                        genericClient.setServerPort(PORT);
                        System.out.println("New port is: " + PORT);
                        genericClient.start();
                    } else if (reply.matches(IP_PATTERN)) {
                        IP = reply.substring(3);
                        genericClient.setServerIpAddress(IP);
                        System.out.println("New ip is: " + IP);
                        if (!genericClient.isStopped()) {
                            genericClient.stop();
                        }
                        genericClient.start();
                    } else if (reply.matches(NAME_PATTERN)) {
                        String newName = reply.substring(5);
                        genericClient.setName(newName);
                        System.out.println("New name is: " + newName);
                    } else if (reply.matches(AUTO_RETRY_PATTERN)) {
                        AUTO_RETRY_CONNECTION = reply.substring(5).equals("1");
                        genericClient.setAutoRetryConnection(AUTO_RETRY_CONNECTION);
                        System.out.println("New auto retry is: " + AUTO_RETRY_CONNECTION);
                    } else {
                        switch (CLIENT_TYPE) {

                            case TYPE_PLC:
                                if (reply.matches(DELAY_PATTERN)) {
                                    DELAY_TIME = Integer.parseInt(reply.replace("delay ", ""));
                                    System.out.println("New delay: " + DELAY_TIME);
                                } else if (reply.matches(SEND_PATTERN)) {
                                    result = genericClient.sendMessage(reply.substring(5));
                                    System.out.println("Result Msg: " + result);
                                    //if (!result)
                                    //    genericClient.init();
                                } else
                                    System.out.println("Unknown pattern specified.");
                                break;

                            case TYPE_PLATE:
                                if (reply.matches(NRO_CAM_PATTERN)) {
                                    nroCamera = Integer.parseInt(reply.replace("nroCam ", ""));
                                    System.out.println("New nro camera: " + nroCamera);
                                } else if (reply.matches(PLATE_RAND_PATTERN)) {
                                    System.out.println("Plate is random now");
                                    randomPlate = true;
                                } else if (reply.matches(PLATE_EMPTY_PATTERN)) {
                                    randomPlate = false;
                                    plate = "";
                                    System.out.println("Plate is empty now");
                                } else if (reply.matches(PLATE_PATTERN)) {
                                    randomPlate = false;
                                    plate = reply.replace("plate ", "");
                                    System.out.println("New plate is: " + plate);
                                } else if (reply.matches(FRAME_PATTERN)) {
                                    int digit = Integer.parseInt(reply.substring(6));
                                    getPlateFrame = (digit == 1);
                                    System.out.println("Show frame: " + getPlateFrame);
                                } else if (reply.matches(SEND_PLATE_PATTERN)) {
                                    String cameraType = "dinamica";
                                    if (nroCamera != 1)
                                        cameraType = "estatica";
                                    if (randomPlate) {
                                        plate = "";
                                        for (int i = 0; i < 6; i++) {
                                            int digit = (int) ((Math.random() * 35) + 65);
                                            if (digit > 90)
                                                digit -= 43;
                                            char plateChar = (char) digit;
                                            plate += plateChar;
                                            //System.out.print(plateChar);
                                        }
                                    }
                                    String frame = (!getPlateFrame) ? "frame_" + plate : DummyServer.getPlateBase64(plate);
                                    //System.out.println("");
                                    genericClient.sendMessage(frame + "<EOF>" + plate + "<EOF>" + cameraType);
                                } else
                                    System.out.println("Unknown pattern specified.");
                                break;

                            case TYPE_GENERIC:
                                if (reply.matches(SEND_PATTERN)) {
                                    result = genericClient.sendMessage(reply.substring(5));
                                    System.out.println("Result Msg: " + result);
                                    //if (!result)
                                    //    genericClient.init();
                                } else
                                    System.out.println("Unknown pattern specified.");
                                break;
                        }
                    }
                    break;
            }
        } while (!reply.equals("exit"));
    }
}
