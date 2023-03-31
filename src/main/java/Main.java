import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        JsonOperations jsonOperations = new JsonOperations();
        jsonOperations.createCategoriesFile();
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("./data.bin");

        try (ServerSocket serverSocket = new ServerSocket(8989);) {
            System.out.println("Сервер запущен");

            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    if (file.exists()) {
                        jsonOperations = JsonOperations.loadFromBinFile(file);
                    }

                    String request = in.readLine();
                    JsonFromClient jsonFromClient = mapper.readValue(request, JsonFromClient.class);

                    jsonOperations.createCategoryMap();
                    jsonOperations.sumCalculation(jsonFromClient);

                    Root root = jsonOperations.findingMax();
                    String answer = mapper.writeValueAsString(root);

                    out.println(answer);

                    jsonOperations.saveBin(file);
                }
            }
        } catch (IOException ex) {
            System.out.println("Не могу запустить сервер");
            ex.printStackTrace();
        }
    }
}
