package commands;

import collec_class.CollectionManager;
import collec_class.Route;
import server.Response;
import utils.WrongScriptException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RemoveByIdCommand extends Command{
    private boolean anotherscript;
    private Integer id ;
    private final String userName;
    private static final long serialVersionUID = 4L;
    public RemoveByIdCommand(Boolean anotherscript,String userName){
        this.anotherscript = anotherscript;
        this.userName =userName;

    }
    @Override
    public Response execute(CollectionManager<Route> collectionMan, BufferedReader buff) throws IOException {
        buff.reset();
        boolean cont=true;
        do {
            try {
                String kekw = buff.readLine();
                if (kekw == null) {
                    throw new NullPointerException();
                }
                String[] content = kekw.split(" ");
                if (content.length == 2) {
                    id = Integer.parseInt(content[1]);
                } else {
                    id = Integer.parseInt(content[0]);
                }


                cont = false;
            } catch (NumberFormatException ex) {
                System.out.println("Возможно вы ввели не integer или забыли ввести данные, попробуйте ещё раз");
                if (anotherscript) {
                    throw new WrongScriptException();
                }
            }


        } while (cont);
        return(new Response(true,this));
    }

    @Override
    public String getName() {
        return("remove_by_id");
    }

    @Override
    public String description() {
        return("могу удалять элемент по его id");
    }


}
