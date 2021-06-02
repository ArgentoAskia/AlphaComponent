package Alpha.IOStream;

import Alpha.AlphaDataStructure.CommentIterator;
import Alpha.Exceptions.ServerException.AlphaServerException;

import java.io.*;
import java.util.LinkedHashMap;

public class AlphaOutputStream {
    private OutputStream outputStream;
    private IOException currentException = null;

    private LinkedHashMap<Class<? extends Writer>, Writer> writerPackage = new LinkedHashMap<>();
    private LinkedHashMap<Class<? extends OutputStream>, OutputStream> streamPackage = new LinkedHashMap<>();
    public AlphaOutputStream(OutputStream stream){
        outputStream = stream;
    }
    public boolean flush(){
        try {
            outputStream.flush();
            return true;
        } catch (IOException e) {
            currentException = e;
            return false;
        }
    }

    public boolean write(int b){
        try {
            outputStream.write(b);
            return true;
        } catch (IOException e) {
            currentException = e;
            return false;
        }
    }
    public boolean write(byte[] b){
        try {
            outputStream.write(b);
            return true;
        } catch (IOException e) {
            currentException = e;
            return false;
        }
    }

    public boolean write(byte[] b, int off, int len){
        try {
            outputStream.write(b, off, len);
            return true;
        } catch (IOException e) {
            currentException = e;
            return false;
        }
    }

    public boolean writeLine(String str){
        PrintWriter  printWriter = (PrintWriter) writerPackage.get(PrintWriter.class);
        if(printWriter == null){
            printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
            writerPackage.put(PrintWriter.class, printWriter);
        }
        if(str.endsWith("\n")){
            printWriter.print(str);
        }else{
            printWriter.println(str);
        }
        if(!printWriter.checkError()){
            return true;
        }else{
            currentException = new AlphaServerException("目标客户端已断开连接!");
            return false;
        }
    }
    private void streamClose(){
        CommentIterator<Class<? extends OutputStream>> keys = new CommentIterator<>(streamPackage.keySet().iterator());
        for(keys.first(); !keys.isDone(); keys.next()){
            try {
                streamPackage.get(keys.currrentInstance()).close();
            } catch (IOException e) {
                System.out.println("* In AlphaOutputStream Class: ");
                System.out.println("*   Method: close()");
                System.out.println("*   Resource: " + keys.currrentInstance());
                System.out.println("*   Exception msg: " + e.getMessage());
                System.out.println("*   Exception cause: " + e.getCause().getClass().getName());
            }
        }
    }
    private void writerClose(){
        CommentIterator<Class<? extends Writer>> keys = new CommentIterator<>(writerPackage.keySet().iterator());
        for(keys.first(); !keys.isDone(); keys.next()){
            try {
                writerPackage.get(keys.currrentInstance()).close();
            } catch (IOException e) {
                System.out.println("* In AlphaOutputStream Class: ");
                System.out.println("*   Method: close()");
                System.out.println("*   Resource: " + keys.currrentInstance());
                System.out.println("*   Exception msg: " + e.getMessage());
                System.out.println("*   Exception cause: " + e.getCause().getClass().getName());
            }
        }
    }
    public boolean close(){
        BufferedWriter tmpWriter = (BufferedWriter) writerPackage.get(BufferedWriter.class);
        if(tmpWriter!= null){
            try {
                tmpWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writerPackage.remove(BufferedWriter.class);
        }
        writerClose();

        BufferedOutputStream tmpBuffOut = (BufferedOutputStream) streamPackage.get(BufferedOutputStream.class);
        if(tmpBuffOut != null){
            try {
                tmpBuffOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            streamPackage.remove(BufferedOutputStream.class);
        }

        streamClose();
        try {
             outputStream.close();
            return true;
        } catch (IOException e) {
            currentException = e;
            return false;
        }
    }

    public IOException getCause(){
        return currentException;
    }


}
