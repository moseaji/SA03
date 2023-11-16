package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8888;

    public void start() {
        try {
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Connected to server");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                System.out.print("Enter command (add/get/quit/delete): ");
                String command = reader.readLine();

                if (command.equalsIgnoreCase("quit")) {
                    writer.println("quit");
                    break;
                } else if (command.equalsIgnoreCase("add")) {
                    System.out.print("Enter name: ");
                    String name = reader.readLine();
                    System.out.print("Enter phone: ");
                    String phone = reader.readLine();

                    writer.println("add," + name + "," + phone);
                } else if (command.equalsIgnoreCase("get")) {
                    System.out.print("Enter name: ");
                    String name = reader.readLine();
                    writer.println("get," + name);

                    String response = serverReader.readLine();
                    System.out.println(response);
                } else if (command.equalsIgnoreCase("delete")){
                    System.out.println("Enter name: ");
                    String name = reader.readLine();
                    writer.println("delete," + name);
                }

                else {
                    System.out.println("Invalid command.");
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}