import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerTCPThread implements Runnable{
	Socket client;

	public ServerTCPThread(Socket clientSocket) {
		this.client = clientSocket;
	}

	@Override
	public void run() {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter output = new PrintWriter(client.getOutputStream(), true);
	        String line;
	        //listening the incoming message
	        while((line = input.readLine())!= null){
	            System.out.println(client.getInetAddress() + "/" + client.getPort() + " :" + line);
	            //send msg to client
	            output.println("message accepted");
	        }

	        input.close();
	        output.close();
	        client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}

}
