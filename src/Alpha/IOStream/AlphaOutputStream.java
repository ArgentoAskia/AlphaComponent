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

    /**
     * 提供Socket通信的输出流
     *
     * @param stream Socket输出流,通常由{@code socket.getOutputStream()}获取
     */
    public AlphaOutputStream(OutputStream stream) {
        outputStream = stream;
    }

    /**
     * 刷新输出流
     *
     * @return 刷新成功返回{@code true},失败返回{@code false}
     */
    public boolean flush() {
        try {
            outputStream.flush();
            return true;
        } catch (IOException e) {
            currentException = e;
            return false;
        }
    }

    /**
     * 写出数据
     *
     * @param b 待写出的4个字节数据
     * @return 写出成功或者失败
     * @deprecated 该方法会破坏流的完整性
     */
    @Deprecated(since = "dev 1.7")
    public boolean write(int b) {
        try {
            outputStream.write(b);
            return true;
        } catch (IOException e) {
            currentException = e;
            return false;
        }
    }

    /**
     * 写出指定的字节数据
     *
     * @param b 代写数据块
     * @return 成功与否
     */
    public boolean write(byte[] b) {
        try {
            outputStream.write(b);
            return true;
        } catch (IOException e) {
            currentException = e;
            return false;
        }
    }

    public boolean write(byte[] b, int off, int len) {
        try {
            outputStream.write(b, off, len);
            return true;
        } catch (IOException e) {
            currentException = e;
            return false;
        }
    }

    /**
     * 写出一行文本
     *
     * @param str 一行文本,如果该文本不以'\n'结尾则会自动补上'\n'
     * @return 写出成功与否
     */
    public boolean writeLine(String str) {
        PrintWriter printWriter = (PrintWriter) writerPackage.get(PrintWriter.class);
        if (printWriter == null) {
            printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
            writerPackage.put(PrintWriter.class, printWriter);
        }
        if (str.endsWith("\n")) {
            printWriter.print(str);
        } else {
            printWriter.println(str);
        }
        if (!printWriter.checkError()) {
            return true;
        } else {
            currentException = new AlphaServerException("目标客户端已断开连接!");
            return false;
        }
    }

    public boolean writeObject(Object obj) {
        ObjectOutputStream objectOutputStream = (ObjectOutputStream) streamPackage.get(ObjectOutputStream.class);
        try {
            if (objectOutputStream == null) {
                objectOutputStream = new ObjectOutputStream(outputStream);
                streamPackage.put(ObjectOutputStream.class, objectOutputStream);
            }
            objectOutputStream.writeObject(obj);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            currentException = e;
            return false;
        }
    }

    private void streamClose() {
        CommentIterator<Class<? extends OutputStream>> keys = new CommentIterator<>(streamPackage.keySet().iterator());
        for (keys.first(); !keys.isDone(); keys.next()) {
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

    private void writerClose() {
        CommentIterator<Class<? extends Writer>> keys = new CommentIterator<>(writerPackage.keySet().iterator());
        for (keys.first(); !keys.isDone(); keys.next()) {
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

    /**
     * 关闭流
     *
     * @return 关闭成功与否
     */
    public boolean close() {
        BufferedWriter tmpWriter = (BufferedWriter) writerPackage.get(BufferedWriter.class);
        if (tmpWriter != null) {
            try {
                tmpWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writerPackage.remove(BufferedWriter.class);
        }
        writerClose();

        BufferedOutputStream tmpBuffOut = (BufferedOutputStream) streamPackage.get(BufferedOutputStream.class);
        if (tmpBuffOut != null) {
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

    public IOException getCause() {
        return currentException;
    }

    OutputStream getOutputStream() {
        return outputStream;
    }

}
