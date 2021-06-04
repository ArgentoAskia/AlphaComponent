package Alpha.IOStream;

import Alpha.AlphaDataStructure.CommentIterator;

import java.io.*;
import java.util.LinkedHashMap;

public class AlphaInputStream{
    private InputStream inputStream;
    private IOException cause;
    private LinkedHashMap<Class<? extends InputStream>,InputStream> streamPackage = new LinkedHashMap<>();
    private LinkedHashMap<Class<? extends Reader>, Reader> readerPackage = new LinkedHashMap<>();

    /**
     * IO流，请提供Socket的InputStream
     * @param inputStream SocketInputStream参数
     */
    public AlphaInputStream(InputStream inputStream){
        this.inputStream = inputStream;

    }

    /**
     * 判断当前流中是否有字节可读。
     * <p>
     *     如果有，则返回可读字节数
     * <br>如果没有，则返回0
     * <p>
     *     此方法建议和{@link AlphaInputStream#read(byte[])}或{@link AlphaInputStream#readNBytes(int)}使用
     *     也可以用来判断服务端或者客户端时候有数据传输到达！
     *
     * @return 可读字节数
     */
    public int available(){
        try {
            return inputStream.available();
        } catch (IOException e) {
            cause = e;
            return 0;

        }
    }

    /**
     * 读取该流的所有可读字节。
     * <p>
     *     需注意该方法只有遇到文件末尾{@code EOF}时，或者抛出了异常，才会停止，否则将一直读下去，也就是说在没有遇到
     *     文件末尾之前，调用该方法会一直阻塞，至于什么时候会读到文件末尾，这个由系统控制，调用者可能无法预测。
     * <p>
     *     因此，<strong>除非你非常明确读取当前的字节输入流{@code inputStream}一定会返回{@link EOFException}，
     *     否则不建议直接调用该方法！</strong>
     *
     * @return 字节数组，读到所有字节
     */
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

    /**
     * 读取指定数量的字节
     * <p>
     *     此方法在读取字节时，最好配合{@link AlphaInputStream#available()}方法使用，参考下面代码：<br>
     *     <pre>
     *         <code>
     *             int readCount = acceptor.available();
     *             if(readCount != 0)
     *                  byte[] data = acceptor.readNBytes(readCount);
     *         </code>
     *     </pre>
     * <p>
     *     在实现客户端或者服务端的事件接口时，重写动作方法获取数据建议使用这种方法.
     * <p>
     *     直接调用{@link AlphaInputStream#readAllBytes()}可能会引起阻塞而无法让进程继续执行，需要注意！
     *
     * @param len 字节数量
     * @return 字节数组，返回指定字节
     */
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

    /**
     * 读取一个字节的数据
     *
     * @deprecated 此方法可能会破坏流的完整性
     * @return 数据
     */
    @Deprecated(since = "dev 1.7")
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

    /**
     * 读取特定字节数据。
     * <p>
     *     此方法建议配合{@link AlphaInputStream#available()}使用，参考代码：
     *     <pre>
     *         <code>
     *             int readCount = acceptor.available();
     *      *             if(readCount != 0)
     *      *                  byte[] data = acceptor.read(new byte[newreadCount]);
     *         </code>
     *     </pre>
     * @param b 字节数组
     * @return 成功读取的字节数量，该返回值小于等于提供的字节数组的数组长度。
     */
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

    /**
     * 读取一行字节转为文本，在遇到'\n'、‘\r’这两个字符其中一个时停止。
     * <p>
     *     该方法与{@link AlphaOutputStream#writeLine(String)}配对，为了保证通信不发生阻塞，请配对使用。
     *
     * @return 字节数据的文本值
     */
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
    public Object readObject(){
        ObjectInputStream objectInputStream;
        objectInputStream = (ObjectInputStream) streamPackage.get(ObjectInputStream.class);
        try {
        if(objectInputStream == null){
            objectInputStream = new ObjectInputStream(inputStream);
            streamPackage.put(ObjectInputStream.class, objectInputStream);
        }
            return objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            cause = e;
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            cause = new IOException(e);
            return null;
        }

    }

    /**
     * 关闭该流。
     *
     * @return 关闭成功返回{@code true}，关闭失败将返回{@code false}，并设置异常，可以通过调用{@link AlphaInputStream#getCause()}获取异常信息
     */
    public boolean close() {
        if(readerPackage.size() > 0){
            CommentIterator<Class<? extends Reader>> keys = new CommentIterator<>(readerPackage.keySet().iterator());
            for(keys.first(); !keys.isDone(); keys.next()){
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
            for(keys.first(); !keys.isDone(); keys.next()){
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

    public boolean skipNBytes(long n) {
        try {
            inputStream.skipNBytes(n);
            return true;
        } catch (IOException e) {
            cause = e;
            return false;
        }
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

    public synchronized void mark(int readlimit) {
        inputStream.mark(readlimit);
    }
    public boolean markSupported() {
        return inputStream.markSupported();
    }
    public synchronized void reset() throws IOException {
        inputStream.reset();
    }


    /**
     * 若存在异常,可以通过该方法查看异常信息
     * @return 异常
     */
    public IOException getCause(){
        return cause;
    }
    InputStream getInputStream(){
        return inputStream;
    }
}
