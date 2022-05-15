import collec_class.CollectionManager;
import collec_class.Route;
import commands.CommandMan;
import server.PackageMan;
import server.RequestHandler;
import server.Response;
import utils.BasedHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {


    public static void main(String... arg) {
        try {
            BasedHandler base = new BasedHandler();
            Connection c = base.BaseInit("jdbc:postgresql://pg/studs", "s335044", "123");
            CollectionManager<Route> collMan = base.getCollectionFromBase(c, "routes");
            byte[] byt = new byte[10000];
            ByteBuffer byteBuffer;
            DatagramChannel dc = DatagramChannel.open();
            dc.configureBlocking(false);
            SocketAddress addr = new InetSocketAddress(6780);
            dc.bind(addr);



            ExecutorService ex = Executors.newCachedThreadPool();
            CommandMan man = new CommandMan(false);

            while (true) {

                byteBuffer = ByteBuffer.wrap(byt);
                addr = dc.receive(byteBuffer);
                if (addr != null) {
                    ex.execute(new RequestHandler(dc, man, collMan, c, byteBuffer, addr));
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException exp) {
            exp.printStackTrace();
        }


    }


}
