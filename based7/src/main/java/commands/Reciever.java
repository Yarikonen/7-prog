package commands;

import collec_class.CollectionManager;
import collec_class.Route;
import server.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;

public class Reciever extends Thread {
    final private Command command;
    private final CommandMan commandMan;
    private CollectionManager<Route> collMan;
    private final BufferedReader buff;
    private final Connection connection;
    private Response resp;


    public Reciever(CommandMan commandMan, CollectionManager<Route> collMan, Command command, Connection c){
        this.buff = new BufferedReader(new InputStreamReader(System.in));
        this.command=command;
        this.commandMan = commandMan;
        this.collMan = collMan;
        this.connection=c;


    }


    @Override
    public void run() {
        try {
            resp = commandMan.commandExecution(command,collMan,buff,connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Response getResp(){
        return(this.resp);
    }
}
