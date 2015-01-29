package lad.eclipse.mysqlview.templates;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class SerializeUtil {

    /** 序列化对象
     * @throws IOException 
     * */
    public static byte[] serializeObject(Object object) throws IOException{
       ByteArrayOutputStream saos=new ByteArrayOutputStream();
       ObjectOutputStream oos=new ObjectOutputStream(saos);
       oos.writeObject(object);
       byte [] bytes = saos.toByteArray();
       oos.flush();
       return bytes;
    }

    /** 反序列化对象
     * @throws IOException
     * @throws ClassNotFoundException 
     * */
    public static Object deserializeObject(byte[] buf) throws IOException, ClassNotFoundException{
       ByteArrayInputStream sais=new ByteArrayInputStream(buf);
       ObjectInputStream ois = new ObjectInputStream(sais);
       return ois.readObject();
    }

}