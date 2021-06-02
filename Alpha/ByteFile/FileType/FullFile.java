package Alpha.ByteFile.FileType;

import Alpha.Exceptions.FileException.AlphaInvalidFilePathException;
import Alpha.Exceptions.FileException.AlphaMakeFileException;

import java.io.*;

public class FullFile implements File, Serializable {

    private static final long serialVersionUID = 460364234337780771L;
    private String fileName;
    private String fileType;
    private String path;
    private FileContext context;


    // 导出文件到磁盘的方法应该写在工具类中!!!
    @Override
    public boolean toFile() throws AlphaMakeFileException {
        /*
        if(path == null){
            path = "./files/" + fileType + "/";
        }
        String fullPath = path + checkNullFileNameAndType();
        try{
            FileOutputStream fos = new FileOutputStream(fullPath,false);
            context.outputByte(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

         */
        return true;
    }

    public boolean toFile(String pathToWriteOut) throws IOException {
        if(checkPathValid(pathToWriteOut)){
            path = pathToWriteOut;
            return toFile();
        } else {
            throw new AlphaInvalidFilePathException("非法路径,该路径可能不是目录!");
        }
    }

    // file文件不应该处理流
    @Override
    public void makeContext(InputStream stream) throws IOException {
        int length = stream.available();        // 可读字节
        context = new FileContext();
        /*
        context.setPageSize(length);            // 设置块大小,代表文件
        context.inputByte(stream);

         */
    }

    public void makeContext(java.io.File targetFile) throws IOException {
        FileInputStream fis = new FileInputStream(targetFile);
        String file = targetFile.getName();
        int point = file.indexOf(".");
        fileName = file.substring(0,point);
        fileType = file.substring(point + 1);
        System.out.println("fileName" + fileName + "\nflieType" + fileType);
        makeContext(fis);
    }

    public void makeContext(String fileName, String fileType, String... path) throws IOException {
        String dicPath = "";
        for (String part:path
             ) {
            dicPath += (part+"\\");
        }
        String fullPath = dicPath + fileName + "." + fileType;
        FileInputStream fis = new FileInputStream(fullPath);
    }

    @Override
    public void setFile(String fileName, String fileType) throws AlphaInvalidFilePathException {
       setFileName(fileName);
       setFileType(fileType);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getFileType() {
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
        if(checkValidFileNameOrType(name))
            fileName = name;
        else
            throw new AlphaInvalidFilePathException("非法文件名");
    }

    @Override
    public void setFileType(String type) throws AlphaInvalidFilePathException {
        if(checkValidFileNameOrType(type))
            fileType = type;
        else
            throw new AlphaInvalidFilePathException("非法文件类型");
    }

    void toObject() {
        /*
        try{
            ObjectOutputStream oo = new ObjectOutputStream(
                    new FileOutputStream("./" + checkNullFileNameAndType(),false)
            );
            oo.writeObject(this);
            oo.close();
        }catch (IOException e){
            String s = e.getMessage();
            System.out.println(s);
        }

         */
    }


}
