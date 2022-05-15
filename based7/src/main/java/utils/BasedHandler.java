package utils;

import collec_class.*;

import java.sql.*;

public class BasedHandler {
    public Connection BaseInit(String url, String user, String password) throws ClassNotFoundException, SQLException {
        Connection c=null;
        final String sqlCreateTable ="CREATE TABLE IF NOT EXISTS routes(id int PRIMARY KEY NOT NULL,name text NOT NULL"+
                ",coordinates text [] NOT NULL, creationDate timestamp,locationTO text [] NOT NULL,locationFROM text [] NOT NULL,distance real,author text )";
        final String sqlSeqCreator="CREATE SEQUENCE IF NOT EXISTS serial START 1";
        final String sqlCreateUserTable = "CREATE TABLE IF NOT EXISTS users(login text PRIMARY KEY NOT NULL, password text NOT NULL)";

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(url,
                            user,password);
            Statement st = c.createStatement();
            st.execute(sqlCreateTable);
            st.execute(sqlCreateUserTable);
            st.execute(sqlSeqCreator);
            return(c);



        } catch (SQLException e) {
            System.out.println(e.getMessage());


        }
        return(c);

    }
    public CollectionManager<Route> getCollectionFromBase(Connection c,String tableName) throws SQLException {
        final String getColl = "SELECT * FROM " + tableName;
        CollectionManager<Route> collMan = new CollectionManager<>();
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(getColl);
        while(rs.next()){
            Route r= new Route();
            r.setId(rs.getInt(1));
            r.setName(rs.getString(2));
            String[] Coordinates = (String[])(rs.getArray(3).getArray());
            String[] tos = (String[])(rs.getArray(5).getArray());
            String[] froms = (String[])(rs.getArray(6).getArray());
            Coordinates coord = new Coordinates();
            coord.setX(Long.parseLong(Coordinates[0]));
            coord.setY(Double.parseDouble(Coordinates[1]));
            r.setCoordinates(coord);
            Location from = new Location(Double.parseDouble(tos[1]),Long.parseLong(tos[2]),tos[0]);
            Locationto to = new Locationto(Double.parseDouble(froms[1]),Long.parseLong(froms[2]),froms[0]);
            r.setFrom(from);
            r.setTo(to);
            r.setCreationDate(rs.getDate(4).toLocalDate());
            r.setDistance(rs.getLong(7));
            r.setUserName(rs.getString(8));
            collMan.getStorage().add(r);
        }
        return(collMan);

    }
}
