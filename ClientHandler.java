import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


import sun.misc.Lock;

public class ClientHandler implements Runnable {
    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;
    private final ArrayList<ClientHandler> clients;
    private String clientID;
    private final Lock lock;

    public ClientHandler(final Socket newClient, final ArrayList<ClientHandler> clients) throws IOException {
        this.client = newClient;
        this.clients = clients;
        this.clientID = "";
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
        lock = new Lock();
    }

    @Override
    public void run() {
        try {
            lock.lock();
            askForClientID(); // ask what's your name and define client ID
            broadcast(this.clientID + " has joined the chatroom", true);//notification that someone joined the chat
            lock.unlock();
        } catch (InterruptedException | IOException e1) {
            e1.printStackTrace();
        } finally {
            //the user is in the chat
            try {
                while (true) {
                    String request = in.readLine();
                    if(request.contains("LOGOUT")){
                        broadcast(this.clientID+" has left the chat", true);
                        break;
                    }
                    broadcast(request, false); // show user message in all the clients
                }
            } catch (final IOException e) {
                System.err.println("IO exception ");
                e.printStackTrace();
            } finally {       
                out.close();
                try {
                    in.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * ASKS for the name and sets that as the new client ID
     * @throws IOException
     */
    private void askForClientID() throws IOException {
        out.println("What's your name?");
        String newID = in.readLine();
        setClientID(newID);
    }

    /**
     * setter for the new client ID
     * @param newID
     */
    private void setClientID(String newID) {
        this.clientID = newID;
    }

    /**
     * prints messages in all the clients
     * @param isNotification set to false if msg is a message that an user typed 
     * or true if it's a general notification
     * @param msg the message that is being broadcasted
     */
    private void broadcast( String msg,  boolean isNotification) {
        for (final ClientHandler aClient : clients) {
          if(isNotification){
            aClient.out.println(msg);
          }else{
            aClient.out.println(clientID+":  "+msg); 
          }
         
      }
    }

    
}