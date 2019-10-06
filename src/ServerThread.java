
import java.io.IOException;
import java.net.*;


public class ServerThread implements Runnable {
    private DatagramSocket server;
    private DatagramPacket packet;


    public ServerThread(int port){
        try {
        	//making udp socket
            server = new DatagramSocket(port);
            server.setBroadcast(true);
        } catch (SocketException e) {
        	System.err.println("Error creating the socket: " + e);
            System.exit(1);
        }
        System.out.println("success creating udp socket at port "+ server.getLocalPort());
    }

    public void run() {
        byte[] buffer = new byte[256];
        packet = new DatagramPacket(buffer, buffer.length);
        try {
            while (true) {
                System.out.println("waiting for packet");
                server.receive(packet);
                printpacket(server, packet);
                //add the sender port into the value of map in Knocking sequence
                //and server that he knocked list as the value
                
                Server.knockingSequence.add(packet.getPort(), server.getLocalPort());
                //if the first message, check whether that sender has been knocked
                //if yes then remove his record to let him try knock again
                
                if (getMessage(packet).equals("first")) {
                    Server.knockingSequence.remove(packet.getPort());
                }
                
                //if it s the last message, match up his knocking sequence 
                //with the right sequence, if right then connect with tcp
                
                if (getMessage(packet).equals("last")) {
                    Server.knockingSequence.equal(packet.getPort());
                    if (Server.knockingSequence.equal(packet.getPort())) {
                        tcpconnection(packet);
                    }
                }
            }
         

            } catch(IOException e){
            	 System.err.println("Error receiving the packet: " + e);
                 System.exit(1);
            }

        server.close();


        }


    public String getMessage(DatagramPacket p){
        return new String(p.getData(),0, p.getLength());
    }

    synchronized void tcpconnection(DatagramPacket packet){

        try {
        	//creating serversocket at port 9999
            ServerSocket socket = new ServerSocket(9999);
            Socket client = null;
            String porttcp = String.valueOf(socket.getLocalPort());
            System.out.println("creating tcp socket with " + porttcp);

            byte buf [] = porttcp.getBytes();
            packet = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
            //send the packet with number of port of tcp server socket
            server.send(packet);
     
            System.out.println("waiting for tcp client");
            //tcp start to listen
            client = socket.accept();
           	InetAddress address = client.getInetAddress();
     		int port = client.getPort();
     		System.out.println(address.toString() + ":" + port +" just joined");
     		//accept and send message in new thread
     		(new Thread(new ServerTCPThread(client))).start();
            
      
     		socket.close();


        }  catch (IOException e) {
            System.err.println("Error creating serversocket: " + e);
            System.exit(1);
        }

    }
    public void printpacket(DatagramSocket socket, DatagramPacket p){
        String data = new String(p.getData(), 0, p.getLength());
        InetAddress address = p.getAddress();
        int port = p.getPort();
        String msg = socket.getLocalPort() +" : message from"+ address.toString() + " " + String.valueOf(port) + " :" +data;
        System.out.println(msg);
        System.out.println("-----");
    }
}
