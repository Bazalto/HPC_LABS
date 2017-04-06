package lab_10.client;

import com.googlecode.jsonrpc4j.JsonRpcClient;
import com.googlecode.jsonrpc4j.ProxyUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ExampleClient {
    public static void main(String[] args) {
        try {
            System.out.println("Start client");

            int port = 1420;

            Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), port);
            JsonRpcClient jsonRpcClient = new JsonRpcClient();

            final BankService service1 = ProxyUtil.createClientProxy(
                    ExampleClient.class.getClassLoader(),
                    BankService.class,
                    jsonRpcClient,
                    socket
            );

            service1.deposit("acc-01", 20);
            service1.deposit("acc-02", 50);

            service1.withdraw("acc-01", 15);
            service1.transfer("acc-01", "acc-02", 75);
            service1.transfer("acc-01", "acc-02", 90);

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
