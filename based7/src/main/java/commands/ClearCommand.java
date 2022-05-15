package commands;

import collec_class.CollectionManager;
import collec_class.Route;
import server.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClearCommand extends Command implements Based {
    private static final long serialVersionUID = 2L;
    private final String userName;
    public ClearCommand(Boolean fromAnotherScript, String userName){
        this.userName=userName;

    }
    @Override
    public Response execute(CollectionManager<Route> collectionMan, BufferedReader buff) throws IOException {
        if (!serverMode){
            return(new Response(true,this));
        }
        collectionMan.getStorage().removeIf(route->route.getUserName().equals(userName));
        return(new Response(true, this));

    }

    @Override
    public String description() {
        return("чисти");

    }

    @Override
    public String getName() {
        return("clear");
    }

    @Override
    public void basedCommand(Connection c, String tableName) throws SQLException {
        String sqlComm = "DELETE FROM " + tableName + " WHERE author=?";
        PreparedStatement state = c.prepareStatement(sqlComm);
        state.setString(1,userName);
    }
}
