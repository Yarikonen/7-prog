package commands;

import collec_class.CollectionManager;
import collec_class.Route;
import server.Response;
import utils.WrongScriptException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Stack;

public class ExecuteScriptCommand extends Command implements Based {
    private CollectionManager<Route> target;
    private Stack<String> scripts = new Stack<>();
    private Stack<Response> responses = new Stack<>();
    private String script;
    private String scriptNext;
    private transient Connection connection;
    private static final long serialVersionUID = 999L;
    private final String userName;

    public ExecuteScriptCommand(boolean fromAnotherScript, String userName){
        this.userName=userName;

    }
    @Override
    public Response execute(CollectionManager<Route> collectionMan, BufferedReader buff) throws IOException {
        scripts = new Stack<>();
        responses = new Stack<>();
        target = collectionMan;
        if (serverMode){
            try{
                scriptNext = script;
                exec(script);
            }
            catch(FileNotFoundException ex){
                return (new Response(false, this, "файл не найден или вы забыли его ввести, попробуйте ввести другой"));

            }

        }
        else {
                    buff.reset();
                    String[] content = buff.readLine().split(" ");
                    if (content.length == 2) {
                        script = content[1];
                    } else {
                        System.out.println("Введите название скрипта, вы забыли");
                        script = buff.readLine();
                    }
                    return(new Response(true,this));


        }


        Response resp = new Response(true,this );

        responses.stream().forEach(resp::extendResponse);

        return(resp);
    }

    @Override
    public String description() {
        return("я умею запускать скрипт");

    }

    @Override
    public String getName() {
        return("execute_script");
    }
    private void exec(String script) throws IOException {
        scripts.add(scriptNext);
        BufferedReader scriptReader = new BufferedReader(new FileReader(script));
        CommandMan man = new CommandMan(
                true,
                new HentaiCommand()
                ,new ShowCommand()
                ,new RemoveByIdCommand(true,userName)
                ,new InfoCommand()
                ,new ClearCommand(true,this.userName)
                ,new AddCommand(true,this.userName)
                ,new HelpCommand()
                ,new UpdateByIDCommand(true,userName)
                ,new RemoveLastCommand(true,userName)
                ,new RemoveLoverCommand(true,userName)
                ,new ReorderCommand()
                ,new RemoveByDistanceCommand(true,userName)
                ,new SumOfDistanceCommand()
                ,new FilterNameCommand(true));

        while (true) {
            String command;
            String commArgs = scriptReader.readLine();
            if (commArgs == null) {
                break;
            }
            scriptReader.mark(8192);
            String[] content = commArgs.split(" ");
            command=content[0];
            try{
            if (command.equals("execute_script")) {
                if (content.length==1) throw new WrongScriptException();
                scriptNext = content[1];
                if (!scripts.contains(scriptNext)) {
                    try {
                        exec(scriptNext);
                        Response resp = new Response(true, this);
                        responses.add(resp);

                    } catch (FileNotFoundException ex) {
                        Response resp1 = new Response(false, this, "файл " + scriptNext + " не найден или доступ к нему затреднён");
                        responses.add(resp1);
                    }
                } else {
                    Response resp1 = new Response(false, this,"Данный скрипт уже исполнялся - "+ scriptNext);
                    responses.add(resp1);
                }
            } else {
                try {
                    man.changeServerMode(command,false);
                    man.commandExecution(man.commandgetter(command), target, scriptReader, connection );
                    man.changeServerMode(command,true);
                    responses.add(man.commandExecution(man.commandgetter(command), target, scriptReader,  connection));

                } catch (NullPointerException exp) {
                    responses.add(new Response(false,this,"Команды " + command + " не существует"));
                }

            }
            }catch(IOException e){

                try {
                    scriptReader.reset();
                } catch (IOException exp) {
                    exp.printStackTrace();
                    responses.add(new Response(false,this,"sAD"));
                }
                responses.add(new Response(false, man.commandgetter(command), "Ошибка при выполнении " + command + "\nСкрипт " + scripts.peek() + " перешёл к следующей команде"));


            }
        }

    }

    @Override
    public void basedCommand(Connection c, String tableName) {
        this.connection=c;
    }
}