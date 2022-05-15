package commands;

import collec_class.CollectionManager;
import collec_class.Route;
import server.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveLastCommand extends Command implements Based {
    private static final long serialVersionUID = 5L;
    private final String userName;
    public RemoveLastCommand(Boolean fromAnotherScript, String userName) {
        this.userName=userName;
    }

    @Override
    public Response execute(CollectionManager<Route> collectionMan, BufferedReader buff) throws IOException {
        if (serverMode) {
            if (collectionMan.getStorage().isEmpty()) {
                return (new Response(false, this, "already pusto"));
            } else {
                collectionMan.getStorage().pop();
            }
            return (new Response(true, this));
        }
        else{
            return(new Response(true,this));
        }
    }

    @Override
    public String description() {
        return("удалю послдений элемент");

    }

    @Override
    public String getName() {
        return("remove_last");
    }

    @Override
    public void basedCommand(Connection c, String tableName) throws SQLException {
        final String sqlremlast = "DELETE FROM " +tableName +" WHERE id in (SELECT id FROM " + tableName + " ORDER BY id DESC LIMIT 1)" + " AND author = ?" ;
        PreparedStatement ps = c.prepareStatement(sqlremlast);
        ps.setString(1, userName);


    }
}
