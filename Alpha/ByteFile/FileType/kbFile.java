package Alpha.ByteFile.FileType;

import Alpha.Exceptions.FileException.AlphaInvalidFilePathException;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * 暂时先不写这个分片
 */

public class kbFile implements File{
    private String path;
    private String fileName;
    private String fileType;
    LinkedList<FileContext> context;

    public void makeContext(InputStream stream) throws IOException {
       context = new LinkedList<>();
       int avaliableByte = stream.available();
       int kilo;
       int turn = avaliableByte/1000;
       for(int i = 0; i <= turn; i++){
           kilo = (avaliableByte/1000)>0? 1000:avaliableByte%1000;
           /*
           FileContext file = new FileContext(kilo);
           file.inputByte(stream);


           context.add(file);

            */
       }
    }

    @Override
    public void makeContext(java.io.File targetFile) throws IOException {

    }

    @Override
    public void makeContext(String fileName, String fileType, String... path) throws IOException {

    }

    public boolean toFile(){
        return false;
    }
    public void setFilePath(String path, String fileName, String fileType){
        if(!path.endsWith("\\")){
            path +="\\";
        }
        this.path = path;
        this.fileName = fileName;
        this.fileType = fileType;
    }
    public void setFile(String fileName, String fileType){
        this.fileName = fileName;
        this.fileType = fileType;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getFileType(){
        return fileType;
    }

    @Override
    public String getFullFileName() {
        return fileName + "." + fileType;
    }

    @Override
    public String getFullFilePath() {
        return path + getFullFileName();
    }

    @Override
    public void setFileName(String name) throws AlphaInvalidFilePathException {

    }

    @Override
    public void setFileType(String type) throws AlphaInvalidFilePathException {

    }
}
