package commands;

import collec_class.CollectionManager;
import collec_class.Route;
import server.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Stack;

public class ShowCommand extends Command {
    Stack<Route> target = new Stack<>();

    @Override
    public Response execute(CollectionManager<Route> collectionMan, BufferedReader buff) throws IOException {
        if (serverMode) {
            this.target = collectionMan.getStorage();
            return(new Response(true,this,this.infor()));

        }
        else{
            return(new Response(true,this));
        }

    }

    @Override
    public String description() {
        return("вывожу че в коллекции");

    }

    @Override
    public String getName() {
        return("show");
    }
    public String infor(){
        StringBuilder a = new StringBuilder();
        for (Route route: target
             ) {
            a.append(route.toString()).append("\n");
        }
        return(a.toString());
    }

}
