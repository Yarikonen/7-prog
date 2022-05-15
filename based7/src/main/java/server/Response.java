package server;

import commands.Command;

import java.io.Serializable;

public class Response implements Serializable {
    private Boolean isCommandOk;
    private Command command;
    private String msg = "";

    private String userName="";
    public Response(Boolean isCommandOk, Command command, String msg){
        this.isCommandOk = isCommandOk;
        this.command = command;
        this.msg = msg;
    }
    public Response(Boolean isCommandOk, Command command){
        this.isCommandOk = isCommandOk;
        this.command = command;
        this.userName=command.getUserName();
    }
    public void extendResponse(Response resp){
        this.msg += "\n" + resp.getMsg();
    }
    public String getMsg(){
        return(msg);
    }
    public String getMessage(){
        if(isCommandOk) {
            return ("\n" +command.getName() + " выполнилась корректно, вы большой специалист в этом деле\n" + msg);
        }
        else{
            return("\n" +command.getName() + " выполнилась некорректно, вы в этом не очень большой специалист\n"+msg);
        }

    }
    Command getCommand(){
        return command;
    }
    public String getUserName() {
        return userName;
    }
}
