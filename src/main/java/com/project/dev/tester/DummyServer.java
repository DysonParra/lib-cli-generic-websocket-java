/*
 * @fileoverview    {DummyServer}
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
package com.project.dev.tester;

import com.project.dev.dummy.websocket.tcp.generic.server.GenericServer;
import com.project.dev.dummy.websocket.tcp.generic.server.ServerConnectionListener;
import com.project.dev.dummy.websocket.tcp.generic.server.ServerMessageListener;
import com.project.dev.dummy.websocket.tcp.generic.server.ServerScheduledMessage;
import com.project.dev.dummy.websocket.tcp.generic.server.ThreadServer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 * TODO: Description of {@code DummyServer}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public class DummyServer {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); //07/23/2021 21:25:35

    private static int PORT = 10032;
    private static int SEND_RESPONSE = 1;
    private static int SERVER_TYPE = 1;
    private static final int TYPE_DYNAMIC = 1;
    private static final int TYPE_STATIC = 2;
    private static final int TYPE_STATIC_TYPE_TWO = 3;
    private static final int TYPE_DISPLAY = 4;
    private static final int TYPE_PLATE = 5;
    private static final int TYPE_PLC = 6;
    private static final int TYPE_GENERIC = 7;

    private static boolean randomNroClass = true;
    private static Integer nroClass = 2;
    private static final Integer[] classes = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15};
    private static boolean randomAxles = true;
    private static Integer axlesQuantity = 3;
    private static boolean randomTotalWeight = true;
    private static Integer totalWeight = 0;

    private static Integer nroScale = 0;
    private static Integer weight = 0;
    private static boolean randomNroScale = true;
    private static boolean randomWeight = true;

    private static Integer nroCamera = 1;
    private static boolean randomPlate = true;
    private static String plate = "";
    private static boolean getPlateFrame = false;

    /**
     * TODO: Description of {@code startTcpServer}.
     *
     */
    public static void startTcpServer() {
        String reply;
        Scanner sc = new Scanner(System.in);

        System.out.println("\nWrite server type:");
        System.out.println("1: Dynamic scale");
        System.out.println("2: Static scale");
        System.out.println("3: Static scale type two");
        System.out.println("4: Display");
        System.out.println("5: Plate");
        System.out.println("6: Plc");
        System.out.println("7: Generic");
        while (true) {
            reply = sc.nextLine();
            try {
                SERVER_TYPE = Integer.parseInt(reply);
                switch (SERVER_TYPE) {
                    case TYPE_DYNAMIC:
                    case TYPE_STATIC:
                    case TYPE_STATIC_TYPE_TWO:
                    case TYPE_DISPLAY:
                    case TYPE_PLATE:
                    case TYPE_PLC:
                    case TYPE_GENERIC:
                        break;
                    default:
                        throw new Exception();
                }
                break;
            } catch (Exception e) {
                System.out.println("Write a valid server type");
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

        if (SERVER_TYPE == TYPE_GENERIC) {
            System.out.println("\nSend response to clients onMessage:");
            System.out.println("1: Yes");
            System.out.println("0: No");
            while (true) {
                reply = sc.nextLine();
                try {
                    SEND_RESPONSE = Integer.parseInt(reply);
                    switch (SEND_RESPONSE) {
                        case 1:
                        case 0:
                            break;
                        default:
                            throw new Exception();
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Write a valid option");
                }
            }
        }

        GenericServer genericServer = new GenericServer(PORT);
        genericServer.setName("%genericServer%");
        // Comportamiento cuando recibe un mensaje de un cliente.
        genericServer.setOnMessageListener((message, receiver) -> {
            System.out.println("Got '" + message + "' in server '" + receiver.getName()
                    + "' from port " + receiver.getSender().getPort());
            if (SERVER_TYPE == TYPE_PLC) {
                if (!message.matches(".*?99")) {
                    System.out.println(receiver.getName() + " get command from client '" + message + "'");
                    receiver.sendMessageToAllClients(message.substring(0, 1) + "99", true, 3);
                } else {
                    System.out.println(receiver.getName() + " get response from client '" + message + "'");
                }
            } else if (SERVER_TYPE == TYPE_GENERIC && SEND_RESPONSE == 1) {
                receiver.sendMessageToAllClients(message + " received", true, 3);
            }
            //int a[] = null; int b = a[-1];
        });

        genericServer.setOnConnectionListener(new ServerConnectionListener() {
            @Override
            public void onConnectedClient(ThreadServer connected) {
                System.out.println("onConnectedClient: " + connected.getSender().getPort());
                if (SERVER_TYPE == TYPE_PLC) {
                    //connected.sendMessageToAllClients("015", true, 3);
                    //connected.sendMessageToAllClients("013", true, 3);
                    connected.sendMessageToClient("015", 3);
                    connected.sendMessageToClient("013", 3);
                }
                //int a[] = null; int b = a[-1];
            }

            @Override
            public void onDisconnectedClient(ThreadServer disconnected) {
                System.out.println("onDisconnectedClient: " + disconnected.getSender().getPort());
                //int a[] = null; int b = a[-1];
            }
        });

        // All
        String TIME_PATTERN = "time \\d+";
        String PORT_PATTERN = "port \\d+";
        String NAME_PATTERN = "name .*?";

        // Dynamic
        String CATEG_RAND_PATTERN = "categ Rand";
        String CATEG_PATTERN = "categ ([2-9]|1[0-5])";
        String AXLES_RAND_PATTERN = "axles Rand";
        String AXLES_PATTERN = "axles [1-7]";
        String WEIGHT_RAND_PATTERN = "weight Rand";
        String WEIGHT_PATTERN = "weight \\d+";

        // Static
        String SCALE_RAND_PATTERN = "scale Rand";
        String SCALE_PATTERN = "scale [0-3]";
        //WEIGHT_RAND_PATTERN
        //WEIGHT_PATTERN

        // Display
        String SPEED_PATTERN = "\\$VEL\\d\\d%";
        String MESSAGE_PATTERN = "\\$MSJ\\d\\d\\d#.*?%";

        // Plate
        String NRO_CAM_PATTERN = "nroCam \\d";
        String PLATE_RAND_PATTERN = "plate Rand";
        String PLATE_EMPTY_PATTERN = "plate Empty";
        String PLATE_PATTERN = "plate ([A-Za-z]|\\d){6}";
        String FRAME_PATTERN = "frame \\d";

        // Generic
        String SEND_PATTERN = "send .*?";
        String RESPONSE_PATTERN = "resp [0-1]";

        switch (SERVER_TYPE) {
            case TYPE_DYNAMIC:
                genericServer.setScheduledMessage(new ServerScheduledMessage(5000) {
                    @Override
                    public List calculateMesageToSend() {
                        //int a[] = null; int b = a[-1];
                        List<String> info = new ArrayList<>();
                        if (randomNroClass)
                            nroClass = classes[(int) ((Math.random() * classes.length) + 0)];
                        Date date;
                        String currentDate;
                        int axleWeight;
                        float speed;
                        date = new Date();
                        currentDate = DATE_FORMAT.format(date);
                        info.add(currentDate);
                        info.add(currentDate);
                        if (randomAxles)
                            axlesQuantity = (int) ((Math.random() * 5) + 1);

                        if (randomTotalWeight)
                            totalWeight = (int) ((Math.random() * 60000) + 1000);
                        int totalWeightAux = totalWeight;
                        for (int j = 1; j < axlesQuantity; j++) {
                            axleWeight = (int) ((Math.random() * totalWeightAux) + 0);
                            totalWeightAux -= axleWeight;
                            info.add("Axle " + j + ":" + String.format("%7d", axleWeight));
                        }
                        info.add("Axle " + axlesQuantity + ":" + String.format("%7d", totalWeightAux));

                        info.add(String.format("Total:%8d", totalWeight));
                        speed = (float) (Math.random() * 10);
                        info.add("Class:  " + nroClass + "  "
                                + "Num Axles:" + String.format("%3d", axlesQuantity) + "   "
                                + "Speed:" + " " + String.format("%4.1f", speed));
                        info.add("");
                        info.add("");
                        info.add("");
                        return info;
                    }
                });
                break;

            case TYPE_STATIC:
                genericServer.setScheduledMessage(new ServerScheduledMessage(5000) {
                    @Override
                    public List<String> calculateMesageToSend() {
                        List<String> info = new ArrayList<>();
                        String signed = "";
                        if (randomNroScale)
                            nroScale = (int) ((Math.random() * 4) + 0);
                        if (randomWeight) {
                            weight = (int) ((Math.random() * 70000) + 0);
                            signed = ((int) ((Math.random() * 2) + 0) == 1) ? "-" : " ";
                        }
                        info.add("%" + nroScale + signed + String.format("%5d", weight) + "KG");
                        return info;
                    }
                });
                break;

            case TYPE_STATIC_TYPE_TWO:
                genericServer.setScheduledMessage(new ServerScheduledMessage(5000) {
                    @Override
                    public List<String> calculateMesageToSend() {
                        List<String> info = new ArrayList<>();
                        String signed = "";
                        if (randomWeight) {
                            weight = (int) ((Math.random() * 70000) + 0);
                            signed = ((int) ((Math.random() * 2) + 0) == 1) ? "-" : " ";
                        }
                        info.add(signed + String.format("%15d", weight) + "KG");
                        return info;
                    }
                });
                break;

            case TYPE_DISPLAY:
                genericServer.setOnMessageListener(new ServerMessageListener() {

                    String currentMessage = "";
                    int currentSpeed = 100;

                    @Override
                    public void onRequest(String message, ThreadServer receiver) {
                        System.out.println("Got '" + message + "' in server '" + receiver.getName()
                                + "' from port " + receiver.getSender().getPort());

                        if (message.matches(SPEED_PATTERN)) {
                            currentSpeed = Integer.parseInt(message.substring(4, 6));
                            System.out.println("New speed is " + currentSpeed);
                        } else if (message.matches(MESSAGE_PATTERN)) {
                            currentMessage = message.substring(8, message.length() - 1);
                            System.out.println("New message is " + "\"" + currentMessage + "\"");
                        }

                        System.out.println("SPEED: " + currentSpeed);
                        System.out.print("\u250C");
                        for (int i = 0; i < currentMessage.length() + 2; i++)
                            System.out.print("\u2500");
                        System.out.println("\u2510");

                        System.out.print("\u2502 ");
                        System.out.print(currentMessage);
                        System.out.println(" \u2502");

                        System.out.print("\u2514");
                        for (int i = 0; i < currentMessage.length() + 2; i++)
                            System.out.print("\u2500");
                        System.out.println("\u2518");
                    }
                });
                break;

            case TYPE_PLATE:
                genericServer.setScheduledMessage(new ServerScheduledMessage(5000) {
                    @Override
                    public List<String> calculateMesageToSend() {
                        List<String> info = new ArrayList<>();
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
                        String frame = (!getPlateFrame) ? "frame_" + plate : getPlateBase64(plate);
                        //System.out.println("");
                        info.add(frame + "<EOF>" + plate + "<EOF>" + cameraType);
                        return info;
                    }
                });
                break;
        }

        System.out.println("Commands:");
        System.out.println("'exit': End execution.");

        switch (SERVER_TYPE) {
            case TYPE_DYNAMIC:
            case TYPE_STATIC:
            case TYPE_STATIC_TYPE_TWO:
            case TYPE_PLATE:
                System.out.println("'start': Start send messages");
                System.out.println("'stop': Stop send messages");
                System.out.println("'" + TIME_PATTERN + "': Change the time between each message to send.");
                break;
        }
        System.out.println("'" + PORT_PATTERN + "': Edit the port of the server and try to initialize.");
        System.out.println("'" + NAME_PATTERN + "': Change the name of the server.");

        switch (SERVER_TYPE) {
            case TYPE_DYNAMIC:
                System.out.println("'" + CATEG_RAND_PATTERN + "': Change in random the number of the category (class)");
                System.out.println("'" + CATEG_PATTERN + "': Change the number of the category (class)");
                System.out.println("'" + AXLES_RAND_PATTERN + "': Change in random the quantity of axles");
                System.out.println("'" + AXLES_PATTERN + "': Change the quantity of axles");
                System.out.println("'" + WEIGHT_RAND_PATTERN + "': Change the weight to send in random.");
                System.out.println("'" + WEIGHT_PATTERN + "': Change a specific weight to send in each message.");
                genericServer.setName("Dynamic:" + genericServer.getServerPort());
                break;
            case TYPE_STATIC:
                System.out.println("'" + SCALE_RAND_PATTERN + "': Change the scale to send in random.");
                System.out.println("'" + SCALE_PATTERN + "': Change a specific scale to send in each message.");
                System.out.println("'" + WEIGHT_RAND_PATTERN + "': Change the weight to send in random.");
                System.out.println("'" + WEIGHT_PATTERN + "': Change a specific weight to send in each message.");
                genericServer.setName("Static:" + genericServer.getServerPort());
                break;
            case TYPE_STATIC_TYPE_TWO:
                System.out.println("'" + WEIGHT_RAND_PATTERN + "': Change the weight to send in random.");
                System.out.println("'" + WEIGHT_PATTERN + "': Change a specific weight to send in each message.");
                genericServer.setName("Static:" + genericServer.getServerPort());
                break;
            case TYPE_DISPLAY:
                System.out.println("'" + SPEED_PATTERN + "': Receive a message from a client with the pattern and change the speed of the display.");
                System.out.println("'" + MESSAGE_PATTERN + "': Receive a message from a client with the pattern and change the message of the display.");
                genericServer.setName("Display:" + genericServer.getServerPort());
                break;
            case TYPE_PLATE:
                System.out.println("'" + NRO_CAM_PATTERN + "': Change the type of camera (1 = dinamica, other = estatica)");
                System.out.println("'" + PLATE_RAND_PATTERN + "': Change the plate to send in random.");
                System.out.println("'" + PLATE_EMPTY_PATTERN + "': Change the plate to send a empty plate.");
                System.out.println("'" + PLATE_PATTERN + "': Change a specific plate to send in each message.");
                System.out.println("'" + FRAME_PATTERN + "': Chage if get a frame in base 64 for each plate (1 = true, other = false).");
                genericServer.setName("Plate:" + genericServer.getServerPort());
                break;
            case TYPE_PLC:
                System.out.println("'" + SEND_PATTERN + "': Send a custom message to all clients.");
                genericServer.setName("PLC:" + genericServer.getServerPort());
                break;
            case TYPE_GENERIC:
                System.out.println("'" + SEND_PATTERN + "': Send a custom message to all clients.");
                System.out.println("'" + RESPONSE_PATTERN + "': Change if will send response to clients onMessage (1 = yes, 0 = no)");
                genericServer.setName("Generic:" + genericServer.getServerPort());
                break;
        }
        System.out.println("");

        new Thread(genericServer).start();

        System.out.println("Write patterns...");
        boolean result = false;
        do {
            reply = sc.nextLine();
            switch (reply) {
                case "start":
                    switch (SERVER_TYPE) {
                        case TYPE_DYNAMIC:
                        case TYPE_STATIC:
                        case TYPE_STATIC_TYPE_TWO:
                        case TYPE_PLATE:
                            result = genericServer.startScheduledMessages();
                            System.out.println("Start: " + result);
                            break;
                        default:
                            System.out.println("Unknown pattern specified.");
                            break;
                    }
                    break;
                case "stop":
                    switch (SERVER_TYPE) {
                        case TYPE_DYNAMIC:
                        case TYPE_STATIC:
                        case TYPE_STATIC_TYPE_TWO:
                        case TYPE_PLATE:
                            result = genericServer.stopScheduledMessages();
                            System.out.println("Stop: " + result);
                            break;
                        default:
                            System.out.println("Unknown pattern specified.");
                            break;
                    }
                    break;
                case "null":
                    //    genericServer.setScheduledMessage(null);
                    //    System.out.println("Null: " + true);
                    break;
                case "kill":
                    genericServer.killServer();
                    System.out.println("Kill: " + true);
                    break;
                case "exit":
                    System.out.println("Exit: " + true);
                    genericServer.killServer();
                    break;
                default:
                    if (reply.matches(TIME_PATTERN)) {
                        long newTime = Long.parseLong(reply.replace("time ", ""));
                        genericServer.getScheduledMessage().setTimeBetweenMessage(newTime);
                        System.out.println("New time: " + newTime);
                    } else if (reply.matches(PORT_PATTERN)) {
                        PORT = Integer.parseInt(reply.substring(5));
                        genericServer.setServerPort(PORT);
                        System.out.println("New port is: " + PORT);
                        genericServer.init();
                    } else if (reply.matches(NAME_PATTERN)) {
                        String newName = reply.substring(5);
                        genericServer.setName(newName);
                        System.out.println("New name is: " + newName);
                    } else {
                        switch (SERVER_TYPE) {
                            case TYPE_DYNAMIC:
                                if (reply.matches(CATEG_RAND_PATTERN)) {
                                    System.out.println("Category is random now");
                                    randomNroClass = true;
                                } else if (reply.matches(CATEG_PATTERN)) {
                                    randomNroClass = false;
                                    nroClass = Integer.valueOf(reply.replace("categ ", ""));
                                    System.out.println("New category: " + nroClass);
                                } else if (reply.matches(AXLES_RAND_PATTERN)) {
                                    System.out.println("Axles are random now");
                                    randomAxles = true;
                                } else if (reply.matches(AXLES_PATTERN)) {
                                    randomAxles = false;
                                    axlesQuantity = Integer.valueOf(reply.replace("axles ", ""));
                                    System.out.println("New Axles quantity: " + axlesQuantity);
                                } else if (reply.matches(WEIGHT_RAND_PATTERN)) {
                                    System.out.println("Weight is random now");
                                    randomTotalWeight = true;
                                } else if (reply.matches(WEIGHT_PATTERN)) {
                                    randomTotalWeight = false;
                                    totalWeight = Integer.valueOf(reply.replace("weight ", ""));
                                    System.out.println("New Weight is: " + totalWeight);
                                } else
                                    System.out.println("Unknown pattern specified.");
                                break;

                            case TYPE_STATIC:
                                if (reply.matches(SCALE_RAND_PATTERN)) {
                                    System.out.println("Scale is random now");
                                    randomNroScale = true;
                                } else if (reply.matches(SCALE_PATTERN)) {
                                    randomNroScale = false;
                                    nroScale = Integer.valueOf(reply.replace("scale ", ""));
                                    System.out.println("New Scale is: " + nroScale);
                                } else if (reply.matches(WEIGHT_RAND_PATTERN)) {
                                    System.out.println("Weight is random now");
                                    randomWeight = true;
                                } else if (reply.matches(WEIGHT_PATTERN)) {
                                    randomWeight = false;
                                    weight = Integer.valueOf(reply.replace("weight ", ""));
                                    System.out.println("New Weight is: " + weight);
                                } else
                                    System.out.println("Unknown pattern specified.");
                                break;

                            case TYPE_STATIC_TYPE_TWO:
                                if (reply.matches(WEIGHT_RAND_PATTERN)) {
                                    System.out.println("Weight is random now");
                                    randomWeight = true;
                                } else if (reply.matches(WEIGHT_PATTERN)) {
                                    randomWeight = false;
                                    weight = Integer.valueOf(reply.replace("weight ", ""));
                                    System.out.println("New Weight is: " + weight);
                                } else
                                    System.out.println("Unknown pattern specified.");
                                break;

                            case TYPE_DISPLAY:
                                break;

                            case TYPE_PLATE:
                                if (reply.matches(NRO_CAM_PATTERN)) {
                                    nroCamera = Integer.valueOf(reply.replace("nroCam ", ""));
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
                                } else
                                    System.out.println("Unknown pattern specified.");
                                break;

                            case TYPE_PLC:
                                if (reply.matches(SEND_PATTERN)) {
                                    genericServer.sendMessageToAllClients(reply.substring(5), null, 3);
                                } else
                                    System.out.println("Unknown pattern specified.");
                                break;

                            case TYPE_GENERIC:
                                if (reply.matches(SEND_PATTERN)) {
                                    genericServer.sendMessageToAllClients(reply.substring(5), null, 3);
                                } else if (reply.matches(RESPONSE_PATTERN)) {
                                    SEND_RESPONSE = Integer.parseInt(reply.substring(5));
                                    System.out.println("New send response is: " + SEND_RESPONSE);
                                } else
                                    System.out.println("Unknown pattern specified.");
                                break;
                        }
                    }
                    break;
            }
        } while (!reply.equals("exit"));
        System.out.println("Process finished...");
    }

    /**
     * TODO: Description of {@code getPlateBase64}.
     *
     * @param plate
     * @return
     */
    public static String getPlateBase64(String plate) {
        BufferedImage image = new BufferedImage(200, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2d = image.createGraphics();
        graphics2d.setColor(Color.yellow);
        graphics2d.fillRect(0, 0, 200, 80);
        graphics2d.setFont(new Font("TimesNewRoman", Font.PLAIN, 40));
        graphics2d.setColor(Color.BLACK);
        graphics2d.drawString(plate, 20, 50);
        String base64 = null;
        try {
            //File homedir = new File(System.getProperty("user.home")); File fileToWrite = new
            //File(homedir, "imageToPrint.png"); ImageIO.write(image, "png", fileToWrite);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            base64 = Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
        return base64;
    }
}
