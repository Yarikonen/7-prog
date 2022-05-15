package commands;

import collec_class.CollectionManager;
import collec_class.Route;
import server.Response;
import utils.WrongScriptException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveByIdCommand extends Command implements Based{
    private final boolean anotherscript;
    private Integer id=0 ;
    private final String userName;
    private static final long serialVersionUID = 4L;
    public RemoveByIdCommand(Boolean anotherscript,String userName){
        this.anotherscript = anotherscript;
        this.userName =userName;

    }
    @Override
    public Response execute(CollectionManager<Route> collectionMan, BufferedReader buff) throws IOException {
        boolean cont=true;
        if (serverMode){
            try {
                collectionMan.getStorage().removeIf(route->route.get_ID()==(id)&& route.getUserName().equals(userName));

            }
            catch (ArrayIndexOutOfBoundsException ex) {
                if (anotherscript) {
                    throw new WrongScriptException();
                }
                return(new Response(false,this,"выход за границы"));

            }

        }
        else {
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
        }
        return(new Response(true,   this));
    }

    @Override
    public String getName() {
        return("remove_by_id");
    }

    @Override
    public String description() {
        return("могу удалять элемент по его id");
    }

    @Override
    public void basedCommand(Connection c, String tableName) throws SQLException {
        String sqlComm = "DELETE FROM " + tableName + " WHERE id = ?" +  " AND author = ?" ;
        PreparedStatement ps = c.prepareStatement(sqlComm);
        ps.setInt(1, 0);
        ps.setString(2,userName);
        ps.execute();
    }
}
