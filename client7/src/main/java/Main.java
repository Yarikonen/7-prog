import collec_class.*;
import commands.*;
import server.PackageMan;
import server.Response;
import server.ResponsesHandler;
import server.User;

import java.io.*;
import java.net.*;
import java.sql.SQLOutput;


public class Main {
    public static void main(String ... arg) {
        try {
            DatagramSocket ds;
            InetAddress host;
            int port;
            ds = new DatagramSocket();
            ds.setSoTimeout(5000);
            host = InetAddress.getLocalHost();
            port = 6780;
            CollectionManager<Route> collMan = new CollectionManager<>();
            BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Введите логин и пароль в одной строчке через пробел для авторизации");
            ResponsesHandler<Response> responsesHandler=new ResponsesHandler<>(host,port,ds);
            ResponsesHandler<String> messageHandler=new ResponsesHandler<>(host,port,ds);
            User user = new User(messageHandler,responsesHandler);
            CommandMan man = new CommandMan(
                    new HentaiCommand()
                    ,new ExitCommand()
                    ,new ShowCommand()
                    ,new ClearCommand(false,user.getUserName())
                    ,new RemoveByIdCommand(false, user.getUserName())
                    ,new InfoCommand()
                    ,new AddCommand(false,user.getUserName())
                    ,new HelpCommand()
                    ,new UpdateByIDCommand(false, user.getUserName())
                    ,new ExecuteScriptCommand(false,user.getUserName())
                    ,new RemoveLastCommand()
                    ,new RemoveLoverCommand(false, user.getUserName())
                    ,new ReorderCommand()
                    ,new RemoveByDistanceCommand(false, user.getUserName())
                    ,new SumOfDistanceCommand()
                    ,new FilterNameCommand(false));
            boolean booling = true;



            do {
                try {
                    PrintStream originalStream = System.out;
                    System.setOut(originalStream);
                    buff.mark(8192);
                    String aa = buff.readLine();
                    Response resp = man.command_exec(aa.split(" ")[0], collMan, buff, false);
                    man.changeservermode(aa.split(" ")[0], true);
                    responsesHandler.send(resp);
                    System.out.println(responsesHandler.get().getMessage());
                    man.changeservermode(aa.split(" ")[0], false);

                } catch (NullPointerException excp) {
                    System.out.println("нет такой команды//вы принудительно вышли");

                }   catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            } while (booling);

        }catch (SocketTimeoutException exp) {
            System.out.println("сервер закрыт.....");
            Main.main();

        }
        catch(SocketException | UnknownHostException exp2){
            System.out.println("ТЫ ЛОХ");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    }




