package Alpha.IOStream;

import Alpha.AlphaDataStructure.CommentIterator;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class AlphaInputStream{
    private InputStream inputStream;
    private IOException cause;
    private LinkedHashMap<Class<? extends InputStream>,InputStream> streamPackage = new LinkedHashMap<>();
    private LinkedHashMap<Class<? extends Reader>, Reader> readerPackage = new LinkedHashMap<>();
    public AlphaInputStream(InputStream inputStream){
        this.inputStream = inputStream;

    }
    public int available(){
        try {
            return inputStream.available();
        } catch (IOException e) {
            cause = e;
            return 0;

        }
    }
    public boolean markSupported() {
        return inputStream.markSupported();
    }
    public byte[] readAllBytes()  {
        byte[] ret;
        try{
            ret = inputStream.readAllBytes();
        }catch (IOException e){
            // IOError solution
           cause = e;
            return null;
        }
        return ret;

    }
    public byte[] readNBytes(int len)  {
        byte[] ret;
        try{
            ret = inputStream.readNBytes(len);
        } catch (IOException e) {
            // IOError solution
           cause = e;
            return null;
        }
        return ret;
    }
    public int read() {
        int data;
        try {
            data = inputStream.read();
        } catch (IOException e) {
            // IOError solution
           cause = e;
           return -1;
        }
        return data;
    }
    public int read(byte[] b){
        int ret;
        try {
            ret =  inputStream.read(b);
        } catch (IOException e) {
           cause = e;
            // IOError solution
            return -1;
        }
        return ret;
    }
    public int read(byte[] b, int off, int len) {
        int ret;
        try {
            ret =  inputStream.read(b, off, len);
        } catch (IOException e) {
           cause = e;
            // IOError solution
            return -1;
        }
        return ret;
    }
    public int readNBytes(byte[] b, int off, int len) {
        int ret;
        try {
            ret =   inputStream.readNBytes(b, off, len);
        } catch (IOException e) {
           cause = e;
            // IOError solution
            return -1;
        }
        return ret;
    }
    public long skip(long n) {
        try {
            return inputStream.skip(n);
        } catch (IOException e) {
           cause = e;
            // IOError solution
            return 0;
        }
    }
    public long transferTo(OutputStream out) {
        try {
            return inputStream.transferTo(out);
        } catch (IOException e) {
           cause = e;
            // IOError solution
            return 0;
        }
    }
    public boolean close() {
        if(readerPackage.size() > 0){
            CommentIterator<Class<? extends Reader>> keys = new CommentIterator<>(readerPackage.keySet().iterator());
            for(keys.first(); keys.isDone(); keys.next()){
                try {
                    readerPackage.get(keys.currrentInstance()).close();
                } catch (IOException e) {
                    System.out.println("* In AlphaInputStream Class: ");
                    System.out.println("*   Method: close()");
                    System.out.println("*   Resource: " + keys.currrentInstance());
                    System.out.println("*   Exception msg: " + e.getMessage());
                    System.out.println("*   Exception cause: " + e.getCause().getClass().getName());
                }
            }
        }
        if(streamPackage.size() > 0){
            CommentIterator<Class<? extends InputStream>> keys = new CommentIterator<>(streamPackage.keySet().iterator());
            for(keys.first(); keys.isDone(); keys.next()){
                try {
                    streamPackage.get(keys.currrentInstance()).close();
                } catch (IOException e) {
                    System.out.println("* In AlphaInputStream Class: ");
                    System.out.println("*   Method: close()");
                    System.out.println("*   Resource: " + keys.currrentInstance());
                    System.out.println("*   Exception msg: " + e.getMessage());
                    System.out.println("*   Exception cause: " + e.getCause().getClass().getName());
                }
            }
        }
        try {
            inputStream.close();
            return true;
        } catch (IOException e) {
           cause = e;
            // IOError solution
            return false;
        }
    }
    public synchronized void mark(int readlimit) {
        inputStream.mark(readlimit);
    }
    public synchronized void reset() throws IOException {
        inputStream.reset();
    }
    public boolean skipNBytes(long n) {
        try {
            inputStream.skipNBytes(n);
            return true;
        } catch (IOException e) {
           cause = e;
           return false;
        }
    }
    public IOException getCause(){
        return cause;
    }
    public String readLine(){
        BufferedReader reader;
        if( ( reader = (BufferedReader) readerPackage.get(BufferedReader.class)) == null){
            reader = new BufferedReader(new InputStreamReader(inputStream));
            readerPackage.put(BufferedReader.class, reader);
        }
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            cause = e;
            return null;
        }
    }
}
