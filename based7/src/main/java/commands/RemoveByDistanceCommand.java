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

public class RemoveByDistanceCommand extends Command implements Based {
    boolean another_script;
    private Long distance = 0L;
    private static final long serialVersionUID = 3L;
    private final String userName;
    public RemoveByDistanceCommand(boolean another_script, String userName){
        this.another_script = another_script;
        this.userName=userName;
    }
    @Override
    public Response execute(CollectionManager<Route> collectionMan, BufferedReader buff) throws IOException {

        if (serverMode){
            collectionMan.getStorage().removeIf(route->route.getDistance().equals(this.distance) && route.getUserName().equals(userName) );
        }
        else {
            buff.reset();
            boolean cont = true;
            do {
                try {
                    String c;
                    String[] content = buff.readLine().split(" ");
                    System.out.println(content[0]);
                    if (content.length == 2) {
                        c = content[1];
                    } else {
                        c = content[0];
                    }
                    if (c == null) throw new WrongScriptException();
                    distance = Long.parseLong(c);
                    cont = false;


                } catch (NumberFormatException ex) {
                    System.out.println("число типа Long не найдено или вы забыли его ввести, попробуйте ввести ещё раз");
                    if (another_script) throw new WrongScriptException();
                }
            }while(cont);
        }
        return(new Response(true,this));





    }

    @Override
    public String description() {
        return("удаляю всё че равно такому расстоянию");

    }

    @Override
    public String getName() {
        return("remove_all_by_distance");
    }

    @Override
    public void basedCommand(Connection c, String tableName) throws SQLException {
        String sqlComm = "DELETE FROM " + tableName + " WHERE distance = ?" +  " AND author = ?" ;
        PreparedStatement ps = c.prepareStatement(sqlComm);
        ps.setLong(1,this.distance);
        ps.setString(2,userName);
        ps.execute();


    }
}
