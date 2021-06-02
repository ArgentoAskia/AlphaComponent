package Alpha.ByteFile.FileFactory;

import Alpha.ByteFile.FileType.File;
import Alpha.Exceptions.ServerException.AlphaFileTransException;


import java.io.*;

public class FileObjTransmission {
    public static void sendFileToClient(OutputStream sender, File file) throws AlphaFileTransException {
        try{
            ObjectOutputStream oo = new ObjectOutputStream(sender);
            oo.writeObject(file);
        } catch (IOException e) {
            AlphaFileTransException exception = new AlphaFileTransException(e.getMessage());
            exception.initCause(exception);
            throw exception;
        }
    }
    public static File toFileClass(InputStream data){
        try {
            ObjectInputStream ois = new ObjectInputStream(data);
            File file = (File) ois.readObject();
            return file;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
