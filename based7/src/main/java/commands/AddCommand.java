package commands;

import collec_class.*;
import server.Response;
import utils.WrongScriptException;

import java.beans.Transient;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

public class AddCommand extends Command implements Based {
    private boolean anotherScript =false;
    private Route creation = new Route();
    private static final long serialVersionUID = 1L;
    private final String userName;
    @Override
    public Response execute(CollectionManager<Route> collectionMan, BufferedReader buff) throws IOException {
        if (serverMode) {
            collectionMan.add(creation);
        }
        else{
            this.addd(creation, buff);
        }
        return(new Response(true, this));
    }
    public AddCommand(boolean fromAnotherScript, String userName) {
        this.anotherScript = fromAnotherScript;
        this.userName = userName;
    }

    public void addd(Route creation, BufferedReader in) throws IOException {
        boolean cont = true;
        System.out.println("Загрузка маршрута");
        System.out.println("▨----------");
        System.out.println("▨▨▨-------");
        System.out.println("▨▨▨▨▨▨---");
        System.out.println("▨▨▨▨▨▨▨▨");
        Location from = new Location();
        Locationto to = new Locationto();
        Coordinates coord = new Coordinates();
        System.out.println("Введите ваш город:");

        do{
                String b = in.readLine();
                if (b==null) throw new WrongScriptException();
            from.setName((b.replaceAll(" ", "")));
            if (from.getName().isEmpty()){
                System.out.println("имя должно содержать буквы");
                if (anotherScript) throw new WrongScriptException();
        }
        else{
        cont = false;}}
        while(cont);
        cont = true;
        System.out.println("Введите координаты вашего города:");

        do {
            try {
                System.out.print("    x:");
                String b = in.readLine();
                if (b==null) throw new WrongScriptException();
                from.setX(Long.parseLong(b));
                cont = false;

            } catch (NumberFormatException ex) {
                System.out.println("X должен быть в формате Long");
                if (anotherScript) throw new WrongScriptException();
            }

        } while (cont);
        cont = true;
        do {
            try {
                System.out.print("    y:");
                String b = in.readLine();
                if (b==null) throw new WrongScriptException();
                from.setY(Long.parseLong(b));
                cont = false;

            } catch (NumberFormatException ex) {
                System.out.println("Y должен быть в формате Long");
                if (anotherScript) throw new WrongScriptException();

            }
        } while (cont);
        cont = true;
        creation.setFrom(from);
        System.out.println("Введите ваши координаты:");
        do {
            try {
                System.out.print("    x:");
                String b = in.readLine();
                if (b==null) throw new WrongScriptException();
                coord.setX(Long.parseLong(b));
                cont = false;

            } catch (NumberFormatException ex) {
                System.out.println("X должен быть в формате Long");
                if (anotherScript) throw new WrongScriptException();

            }
        } while (cont);
        cont = true;
        do {
            try {
                System.out.print("    y:");
                String b = in.readLine();
                if (b==null) throw new WrongScriptException();
                coord.setY(Long.parseLong(b));
                cont = false;

            } catch (NumberFormatException ex) {
                System.out.println("Y должен быть в формате Long");
                if (anotherScript) throw new WrongScriptException();

            }
        } while (cont);
        cont = true;
        creation.setCoordinates(coord);
        System.out.println("Введите куда вы хотите:");
        do{
            String b = in.readLine();
            if (b==null) throw new WrongScriptException();
            to.setName((b.replaceAll(" ", "")));
            if (to.getName().isEmpty()){
                System.out.println("имя должно содержать буквы");
                if (anotherScript) throw new WrongScriptException();
            }
            else{
                cont = false;}}
        while(cont);
        cont = true;
        System.out.println("Введите координаты этого места:");
        do {
            try {
                System.out.print("    x:");
                String b = in.readLine();
                if (b==null) throw new WrongScriptException();
                to.setX(Long.parseLong(b));
                cont = false;

            } catch (NumberFormatException ex) {
                System.out.println("X должен быть в формате Long");
                if (anotherScript) throw new WrongScriptException();

            }
        } while (cont);
        cont = true;

        do {
            try {
                System.out.print("    y:");
                String b = in.readLine();
                if (b==null) throw new WrongScriptException();
                to.setY(Long.parseLong(b));
                cont = false;

            } catch (NumberFormatException ex) {
                System.out.println("Y должен быть в формате Long");
                if (anotherScript) throw new WrongScriptException();


            }
        } while (cont);
        cont = true;
        creation.setTo(to);
        System.out.println("Введите сколько вы хотите ехать:");
        do {
            try {
                String b = in.readLine();
                if (b==null) throw new WrongScriptException();
                Long kk = Long.parseLong(b);
                if (kk<=0){throw new NumberFormatException();}
                creation.setDistance(kk);
                cont = false;

            } catch (NumberFormatException ex) {
                System.out.println("Distance должен быть положительным в формате Long");
                if (anotherScript) throw new WrongScriptException();


            }

        } while (cont);
        cont = true;
        System.out.println("Как назовём ваш маршрут?");
        do{
            String b = in.readLine();
            if (b==null) throw new WrongScriptException();
            creation.setName((b.replaceAll(" ", "")));
            if (creation.get_Name().isEmpty()){
                System.out.println("имя должно содержать буквы");
                if (anotherScript) throw new WrongScriptException();
            }
            else{
                creation.setUserName(userName);
                cont = false;}}
        while(cont);



    }
    @Transient
    public void basedCommand(Connection c, String tableName) throws SQLException  {

        final String sqlAddRoute="INSERT INTO "+tableName+"(id,name,coordinates,creationDate,locationTO,locationFROM,distance,author) " +"VALUES(nextval('serial'),?,?,?,?,?,?,?)";
        final String sqlgetID = "SELECT id FROM " + tableName + " ORDER BY id DESC LIMIT 1";

        Statement statement = c.createStatement();
        PreparedStatement st = c.prepareStatement(sqlAddRoute, Statement.RETURN_GENERATED_KEYS);

        st.setString(1, creation.get_Name());
        String[] coord = {((Object) creation.getCoordinates().getX()).toString(), ((Object) creation.getCoordinates().getY()).toString()};
        st.setArray(2, c.createArrayOf("text", coord));
        st.setDate(3, new Date(System.currentTimeMillis()));
        st.setArray(4, c.createArrayOf("text", new String[]{creation.getTo().getName(), ((Object) creation.getTo().getX()).toString(), ((Object) creation.getTo().getY()).toString()}));
        st.setArray(5, c.createArrayOf("text", new String[]{creation.getFrom().getName(), ((Object) creation.getFrom().getX()).toString(), ((Object) creation.getFrom().getY()).toString()}));
        st.setLong(6, creation.getDistance());
        st.setString(7,userName);
        st.execute();
        ResultSet rs = statement.executeQuery(sqlgetID);
        rs.next();
        creation.setId(rs.getInt("id"));

    }
    @Override
    public String description() {
        return("я умею добавить элемент");

    }

    @Override
    public String getName() {
        return("add");
    }
}
