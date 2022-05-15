package commands;

import collec_class.CollectionManager;
import collec_class.Route;
import server.Response;
import utils.WrongScriptException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveLoverCommand extends Command implements Based {
    private static final long serialVersionUID = 6L;
    private final boolean another_script;
    private Integer ID;
    private final String userName;
    private Route route;
    public RemoveLoverCommand(Boolean another_script,String userName){
        this.another_script = another_script;
        this.userName=userName;


    }

    @Override
    public Response execute(CollectionManager<Route> collectionMan, BufferedReader buff) throws IOException {
        boolean cont=true;

        if (serverMode){
            if (ID==null){
                collectionMan.remove_lower(route,userName);
            }
            else{
                route= collectionMan.find_by_id(ID);
                if (route == null) {
                    return(new Response(false,this,"такого ID нет"));
                }
            }
            collectionMan.remove_lower(route,userName);
            return(new Response(true,this));
        }
        else {
            do {
                System.out.println("Хотите выбрать элемент из существуюших? да/нет");
                try {
                    buff.mark(8192);
                    String decis = buff.readLine();
                    if (decis.equals("да")) {
                        System.out.println("Введите ID");
                        String c = buff.readLine();
                        ID = Integer.parseInt(c);

                        cont = false;
                    } else if (decis.equals("нет")) {
                        AddCommand add = new AddCommand(another_script,userName);
                        add.addd(route, buff);
                        cont = false;
                    } else {
                        System.out.println("ДА/нЕТ");
                        if (another_script) throw new WrongScriptException();
                    }

                } catch (NumberFormatException exp) {
                    System.out.println("ID должен быть целым положительным");
                    if (another_script) {
                        throw new WrongScriptException();
                    }
                } catch (NullPointerException exp) {
                    System.out.println("Такого элемента нет");
                    if (another_script) {
                        throw new WrongScriptException();
                    }
                }
            } while (cont);
        }

        return(new Response(true,this));

    }

    @Override
    public String description() {
        return("удалю все элементы которые меньше");

    }

    @Override
    public String getName() {
        return("remove_lover");
    }

    @Override
    public void basedCommand(Connection c, String tableName) throws SQLException {
        String sqlDeleterByRoute = "DELETE FROM "+tableName+" WHERE (distance<? OR (distance=? AND name<?)) AND author= ? ";
        String sqlID="SELECT * from "+tableName +" WHERE id=?";
        PreparedStatement ps2 = c.prepareStatement(sqlDeleterByRoute);
        ps2.setString(4,userName);
        if (ID == null){

            ps2.setLong(1,route.getDistance());
            ps2.setLong(2,route.getDistance());
            ps2.setString(3,route.get_Name());
            System.out.println("aboba");
            ps2.execute();
        }
        else{
            PreparedStatement ps = c.prepareStatement(sqlID);
            ps.setInt(1,ID);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                ps2.setLong(1,rs.getLong("distance"));
                ps2.setLong(2,rs.getLong("distance"));
                ps2.setString(3,rs.getString("name"));
                ps2.execute();
            }
            else{
                throw new SQLException("Такого ID не существует");
            }

        }

    }
}
