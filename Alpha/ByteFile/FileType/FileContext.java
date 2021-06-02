package Alpha.ByteFile.FileType;

import java.io.Serializable;

public class FileContext implements Serializable {

    private static final long serialVersionUID = 460364234337780772L;
    private byte[] context = null;

    public int getPageSize() {
        return context.length;
    }
    public void writeContext(String s){
        writeContext(s.getBytes());
    }
    public void writeContext(byte[] bytes){
        if(context == null){
            context = new byte[bytes.length];
            int i = 0;
            for (byte b:bytes
            ) {
                context[i++] = b;
            }
        }
    }

    public void addContext(byte[] bytes){
        if(context == null){
            context = new byte[0];
        }
        byte[] oldData = context;
        context = new byte[oldData.length + bytes.length];
        int i = 0;
        for (byte b:oldData
             ) {
            context[i++] = b;
        }
        for (byte b:bytes
             ) {
            context[i++] = b;
        }
    }
    public void addContext(String s){
        addContext(s.getBytes());
    }

    public byte[] readContext(){
        return context;
    }
    public String readStringContext(){
        return new String(context);
    }
}
