import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        JsonOperations jsonOperations = new JsonOperations();
        jsonOperations.createCategoriesFile();
        ObjectMapper mapper = new ObjectMapper();

        try (ServerSocket serverSocket = new ServerSocket(8989);) {
            System.out.println("Сервер запущен");

            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    String request = in.readLine();
                    JsonFromClient jsonFromClient = mapper.readValue(request, JsonFromClient.class);
                    jsonOperations.createCategoryMap(jsonFromClient);
                    jsonOperations.sumCalculation(jsonFromClient);
                    Root root = jsonOperations.findingMax();
                    String answer = mapper.writeValueAsString(root);
                    out.println(answer);
                }
            }
        } catch (IOException ex) {
            System.out.println("Не могу запустить сервер");
            ex.printStackTrace();
        }
    }
}
