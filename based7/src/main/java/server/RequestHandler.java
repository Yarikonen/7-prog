package server;

import collec_class.CollectionManager;
import collec_class.Route;
import commands.Command;
import commands.CommandMan;
import commands.Reciever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestHandler implements Runnable {
    private final DatagramChannel dc;
    private final ByteBuffer bf;
    private final PackageMan<Response> pm = new PackageMan<>();
    private Command command;
    private final CommandMan commandMan;
    private CollectionManager<Route> collMan;
    private final BufferedReader buff;
    private final Connection connection;
    private SocketAddress addr;
    private String loginpass;

    public RequestHandler(DatagramChannel dc, CommandMan commandMan, CollectionManager<Route> collMan, Connection c, ByteBuffer bf, SocketAddress addr) {
        this.dc = dc;
        this.bf = bf;
        this.addr = addr;
        this.buff = new BufferedReader(new InputStreamReader(System.in));
        this.commandMan = commandMan;
        this.collMan = collMan;
        this.connection = c;
    }


    @Override
    public void run() {

        try {
            Response resp = pm.setCommand(bf);
            if (resp.getMsg().split(" ")[0].equals("login")) {
                this.login(resp.getMsg());

            } else {

                this.command = resp.getCommand();

                Reciever commThread = new Reciever(commandMan, collMan, command, connection);
                commThread.start();
                commThread.join();
                resp = commThread.getResp();

                dc.send(pm.packResponse(resp), addr);
            }
        } catch (IOException | ClassNotFoundException | InterruptedException | SQLException ignored) {

        }

    }

    private void login(String loginpass) throws SQLException, IOException, ClassNotFoundException {
        this.loginpass = loginpass;
        PackageMan<String> stringpm = new PackageMan<>();
        String sqlChecker = "select * from users where login =?";
        PreparedStatement state = connection.prepareStatement(sqlChecker);

        String login = loginpass.split(" ")[1];
        state.setString(1, login);
        String pass = loginpass.split(" ")[2];

        ResultSet rs = state.executeQuery();
        if (rs.next()) {
            if (pass.equals(rs.getString(2))) {
                dc.send(stringpm.packResponse("Успешный вход"), addr);

            } else {
                dc.send(stringpm.packResponse("Неверный пароль"), addr);
            }
        } else {
            String sqlAdd = "INSERT INTO users(login,password) values(?,?)";
            PreparedStatement statement = connection.prepareStatement(sqlAdd);
            statement.setString(1, login);
            statement.setString(2, pass);
            statement.execute();
            dc.send(stringpm.packResponse("Вы были зарегестрированы, попробуйте войти"), addr);
        }


    }
}

