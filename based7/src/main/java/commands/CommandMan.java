package commands;

import collec_class.CollectionManager;
import collec_class.Route;
import server.Response;
import utils.WrongScriptException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


public  class CommandMan {


    private Map<String, Command> commandMap = new HashMap<>();
    private final static PrintStream originalStream;
    private final Boolean anotherScript;
    ReentrantLock locker = new ReentrantLock();
    static
    {originalStream = System.out;}
    public CommandMan( Boolean anotherScript,Command ... commands){
        this.anotherScript = anotherScript;
        for (Command i: commands
        ) {
            commandMap.put(i.getName(),i);
        }

    }
    public void changeServerMode(String a,boolean s){
        commandMap.get(a).changeservermode(s);
    }
    public Response commandExecution(Command command, CollectionManager<Route> target, BufferedReader buff, Connection c) throws IOException {
        locker.lock();

            try {
                if (command.serverMode){
                    PrintStream dummyStream = new PrintStream(new OutputStream(){
                        public void write(int b) {
                        }
                    });
                    System.setOut(dummyStream);
                }
                if (command instanceof Based && command.serverMode) {
                    ((Based) command).basedCommand(c, target.getCollectionName());
                }
                Response resp = command.execute(target, buff );
                System.setOut(originalStream);
                return (resp);

            } catch (WrongScriptException ex) {
                System.setOut(originalStream);
                throw new WrongScriptException();
            } catch (SQLException exp) {
                if(anotherScript){
                    throw new WrongScriptException();
                }
                return new Response(false, command, exp.getMessage());
            }
            finally {
                System.setOut(originalStream);
                locker.unlock();
            }



    }
    public void upgComMap(Command ... commands){
        for (Command i: commands
        ) {
            commandMap.put(i.getName(),i);
        }

    }
    public Command commandgetter(String a){
        return(commandMap.get(a));
    }


}
