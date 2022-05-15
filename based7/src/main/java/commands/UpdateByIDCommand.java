package commands;

import collec_class.CollectionManager;
import collec_class.Route;
import server.Response;
import utils.WrongScriptException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

public class UpdateByIDCommand extends Command  implements Based{
    private final boolean another_script;
    private int id;
    private Route podmena;
    private final String userName;
    private static final long serialVersionUID = 9L;
    public UpdateByIDCommand(Boolean another_script,String userName){
        this.another_script = another_script;
        this.userName = userName;

    }


    @Override
    public Response execute(CollectionManager<Route> collectionMan, BufferedReader in) throws IOException {


        if(!serverMode) {
            AddCommand add;
            in.reset();
            add = new AddCommand(another_script,userName);
            podmena= new Route();
            boolean cont = true;
            System.out.println("введите номер элемента, который хотите обновить");
            do {
                try {
                    String c;
                    String kekw = in.readLine();
                    if (kekw == null) {
                        throw new NullPointerException();
                    }
                    String[] content = kekw.split(" ");
                    if (content.length == 2) {
                        c = content[1];
                    } else {
                        c = content[0];
                    }
                    id = Integer.parseInt(c);
                    add.addd(podmena,in);
                    podmena.setId(id);
                    return(new Response(true,this));
                } catch (NumberFormatException ex) {
                    System.out.println("Возможно вы ввели не integer или забыли ввести данные, попробуйте ещё раз");
                    if (another_script) {
                        throw new WrongScriptException();
                    }
                }

            } while (cont);
        }
        else {
            try {
                Route intik = collectionMan.find_by_id(podmena.get_ID());
                if (intik == null || !intik.getUserName().equals(userName)) {
                    return(new Response(false,this,"элемента с таким айди нет или у вас к нему нет доступа"));
                }
                collectionMan.getStorage().remove(intik);
                collectionMan.add(podmena);
                return(new Response(true,this));
            }catch (ArrayIndexOutOfBoundsException ex) {
                if (another_script) {
                    throw new WrongScriptException();
                }
                return(new Response(false,this,"выход за грань"));
            } catch (NullPointerException exp) {
                if (another_script) {
                    throw new WrongScriptException();
                }
                return(new Response(false,this,"Заменять нечего, либо вы пытаетесь заменить неподвластный вам элемент"));
            }

        }
        return(null);



    }

    @Override
    public String description() {
        return("изменим элемент по ID");

    }

    @Override
    public String getName() {
        return("updatebyID");
    }

    @Override
    public void basedCommand(Connection c, String tableName) throws SQLException {
        String sqlID="SELECT * from "+tableName +" WHERE id=? AND author= ?";
        PreparedStatement ps = c.prepareStatement(sqlID);
        ps.setString(2,userName);
        ps.setInt(1,id);
        ResultSet rs=ps.executeQuery();
        if(rs.next()) {
            rs.updateString(1, podmena.get_Name());
            String[] coord = {((Object) podmena.getCoordinates().getX()).toString(), ((Object) podmena.getCoordinates().getY()).toString()};
            rs.updateArray(2, c.createArrayOf("text", coord));
            rs.updateDate(3, Date.valueOf(podmena.getCreationDate()));
            rs.updateArray(4, c.createArrayOf("text", new String[]{podmena.getTo().getName(), ((Object) podmena.getTo().getX()).toString(), ((Object) podmena.getTo().getY()).toString()}));
            rs.updateArray(5, c.createArrayOf("text", new String[]{podmena.getFrom().getName(), ((Object) podmena.getFrom().getX()).toString(), ((Object) podmena.getFrom().getY()).toString()}));
            rs.updateLong(6, podmena.getDistance());
            rs.updateString(7,userName);
        }
        else{
            throw new SQLException("Такого айди нет или у вас к нему нет доступа лол");
        }

    }
}
