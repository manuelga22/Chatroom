import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;


public class Client {
    private static final int PORT = 3050;
    private static final String SERVER_IP = "127.0.0.1";
    public static void main(String[]args) throws IOException, InterruptedException {

      Socket socket = new Socket (SERVER_IP, PORT);//establish connection with the server
      ServerConnection serverConn = new ServerConnection(socket);
   

      BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
      PrintWriter out  = new PrintWriter(socket.getOutputStream(), true);
    
      
     
      new Thread(serverConn).start();// prints the responses to the request from the server

      System.out.println("Type 'LOGOUT' to leave the chat >> ");

      while(true){ 
         System.out.print(">> ");
         String message = keyboard.readLine();
          
         if(message.equals("LOGOUT")){
           System.out.println("you left...");
           out.println("LOGOUT"); 
           break;
         }
         out.println(message); 
      }   
   
      socket.close();
      System.exit(0);
    
    }
}