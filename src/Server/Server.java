package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int PORT = 8888;

    private Map<String, String> contacts;

    public Server() {
        contacts = new HashMap<>();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;

            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String request = reader.readLine();
                    if (request == null) {
                        break;
                    }

                    String[] parts = request.split(",");
                    String command = parts[0];

                    if (command.equalsIgnoreCase("add")) {
                        String name = parts[1];
                        String phone = parts[2];
                        contacts.put(name, phone);
                        writer.println("Contact added successfully.");
                    } else if (command.equalsIgnoreCase("get")) {
                        String name = parts[1];
                        String phone = contacts.get(name);
                        if (phone != null) {
                            writer.println("Phone: " + phone);
                        } else {
                            writer.println("Contact not found.");
                        }
                    } else if (command.equalsIgnoreCase("quit")) {
                        break;
                    }
                    else if (command.equalsIgnoreCase("delete")) {
                        String name = parts[1];
                        contacts.remove(name);
                        writer.println("Contact deleted successfully!");
                    }
                    else {
                        writer.println("Invalid command.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}