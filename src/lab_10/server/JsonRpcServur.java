package lab_10.server;


import com.googlecode.jsonrpc4j.JsonRpcServer;
import com.googlecode.jsonrpc4j.StreamServer;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Scanner;

public class JsonRpcServur {
    public static void main(String[] args) {
        try {
            BankServiceImpl bs = new BankServiceImpl();

            JsonRpcServer jsonRpcServer = new JsonRpcServer(bs, BankService.class);

            int maxThreads = 50;
            int port = 1420;

            ServerSocket serverSocket = ServerSocketFactory.getDefault()
                    .createServerSocket(port, 0, InetAddress.getByName("127.0.0.1"));

            StreamServer streamServer = new StreamServer(jsonRpcServer, maxThreads, serverSocket);

            streamServer.start();

            System.out.println("Server start");

            Scanner in = new Scanner(System.in);
            int num = 1;
            System.out.println("Press not 1 to stop");

            while (num == 1) {
                num = in.nextInt();
            }

            streamServer.stop();

            System.out.println("Server stop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
