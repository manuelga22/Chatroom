
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.ServerSocket;


public class Server {
    private static final int PORT = 3050;
    private static ArrayList<ClientHandler> clients = new ArrayList<>(); 
    private static ExecutorService pool = Executors.newFixedThreadPool(5);
    public static void main(String[]args) throws IOException{

        ServerSocket listener = new ServerSocket(PORT);

        while(true){
            System.out.println("Waiting for client to connect");
            Socket client = listener.accept();//allows clients to connect to the chatroom
            System.out.println("Client connected");
       
            ClientHandler clientThread = new ClientHandler(client, clients);
            clients.add(clientThread);

            pool.execute(clientThread);
        }
        

    }
} 