package commands;

import collec_class.CollectionManager;
import collec_class.Route;
import server.Response;
import utils.WrongScriptException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.*;

public class SaveCommand extends Command {
    private boolean another_script;
    private String file = "";

    public SaveCommand(Boolean another_script){
        this.another_script = another_script;
    }


    @Override
    public Response execute(CollectionManager<Route> collectionMan, BufferedReader buff) throws IOException {


        if(!serverMode){
            try {
                System.out.println("куда хотите сохранить файл?");
                file = buff.readLine();
                if (file == null) throw new NullPointerException();
            }catch(NullPointerException exp){
                throw new WrongScriptException();
            }
        }
        else {
            try {
                PrintWriter write = new PrintWriter(file);
                JAXBContext context = JAXBContext.newInstance(CollectionManager.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(collectionMan, write);
                return (new Response(true, this));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Нет доступа");
            } catch (JAXBException e) {
                e.printStackTrace();
            } catch (NullPointerException exp) {
                if (another_script) {
                    throw new WrongScriptException();
                }
            }
        }
        return(new Response(true,this));

    }








    @Override
    public String description() {
        return("сохраняю коллекцию в файл");

    }

    @Override
    public String getName() {
        return("save");
    }
}