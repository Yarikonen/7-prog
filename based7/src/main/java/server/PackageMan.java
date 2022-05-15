package server;

import java.io.*;
import java.nio.ByteBuffer;

public class PackageMan<V> {
    //десериализация
    public Response setCommand(ByteBuffer buf) throws IOException, ClassNotFoundException{
            ObjectInputStream obj = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
            Response v = (Response) obj.readObject();
            return(v);



    }
    //сериализация
    public ByteBuffer packResponse(V v) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        ObjectOutputStream obj = new ObjectOutputStream(bos);
        obj.writeObject(v);
        return(ByteBuffer.wrap(bos.toByteArray()));
    }

}
