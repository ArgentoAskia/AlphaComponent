package Alpha.RespInstance.FileTransmission;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileStructure implements Serializable {
    enum Operation{
        downloadFile, deleteFile,
        uploadFile, renameFile
    }
    private static final long serialVersionUID = 562245215425214L;
    private String path;
    private Operation oper;
    private byte[] data;

    FileStructure(String path, Operation op, byte[] data){
        this.path = path;
        oper = op;
        this.data = data;
    }

    public FileStructure(String path, byte[] data){
        this(path, Operation.uploadFile, data);
    }

    public FileStructure(String path, String oldFileName, String newFileName){
        this(path + "\\" + oldFileName, Operation.renameFile, newFileName.getBytes());
    }
    public FileStructure(String path){
        this(path, Operation.downloadFile, null);
    }
    public FileStructure(String path, String resourceLocalPath){
        this.path = path;
        oper = Operation.uploadFile;
        try(FileInputStream inputStream = new FileInputStream(resourceLocalPath)){
            this.data = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setDeleteFileOper(){
        oper = Operation.deleteFile;
    }
    public void setDownloadFileOper(){
        oper = Operation.downloadFile;
    }

    void setData(byte[] data) {
        this.data = data;
    }

    void setOper(Operation oper) {
        this.oper = oper;
    }

    void setPath(String path) {
        this.path = path;
    }

    Operation getOper() {
        return oper;
    }

    String getPath() {
        return path;
    }

    public byte[] getData() {
        return data;
    }

    public static void toDiskFile(String directoryPath, FileStructure structure) throws IOException {
        String abstractPath = structure.getPath();
        String fileName = abstractPath.substring(abstractPath.lastIndexOf("\\"));
        String realFullPath= directoryPath + fileName;
        // todo write file
        File file = new File(realFullPath);
        if(!file.exists()){
            if(!file.createNewFile()){
                throw new RuntimeException("无法创建不存在的文件!!");
            }
        }
        try(FileOutputStream fileOutputStream = new FileOutputStream(realFullPath)){
            fileOutputStream.write(structure.getData());
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
