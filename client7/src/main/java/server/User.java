package server;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class User {
    private String userName;
    private String password;
    public User(ResponsesHandler<String> rh,ResponsesHandler<Response> rp) throws IOException,ClassNotFoundException, SocketTimeoutException {
        Scanner in =  new Scanner(System.in);

        Boolean logging = true;
        do{
            System.out.println("Введите логин и пароль для авторизации");
            String passwordlogin=in.nextLine();
            if (passwordlogin.split(" ").length!=2){
                System.out.println("что-то неправильно введено");
            }
            else {
                this.userName=passwordlogin.split(" ")[0];
                this.password=passwordlogin.split(" ")[1];
                Response r = null;
                try {
                    r = new Response(true, null, "login " + userName+" " + hashing(password,"aboba"));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                rp.send(r);
                String ans = rh.get();
                if (ans.equals("Успешный вход")) {
                    logging = false;
                }

                System.out.println(ans);
            }




        }while(logging);

    }
    private String hashing(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt.getBytes(StandardCharsets.UTF_8));
        byte[] bytes =md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++){
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
       return(sb.toString());
    }
    public String getUserName(){
        return(this.userName);
    }
}
