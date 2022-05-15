package commands;

import collec_class.CollectionManager;
import collec_class.Route;
import server.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class ExitCommand extends Command {
    private static final long serialVersionUID =1000L;
    @Override
    public Response execute(CollectionManager<Route> collectionMan, BufferedReader buff) throws IOException {
        if (!serverMode) {
            System.out.println("завершение работы");
            System.exit(0);
            return(new Response(true, this));
        }
        else{
            return(new Response(false, this));
        }


    }

    @Override
    public String description() {
        return("ОФАЙ");

    }

    @Override
    public String getName() {
        return("exit");
    }
}
