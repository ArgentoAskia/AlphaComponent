package Alpha.ByteFile.FileType;

import Alpha.Exceptions.FileException.AlphaInvalidFilePathException;

import java.io.*;
import java.util.regex.Pattern;

public interface File {
    /**
     * 将文件写出到磁盘
     * @return
     * @throws IOException
     */
    boolean toFile() throws IOException;
    void makeContext(InputStream stream) throws IOException;
    void makeContext(java.io.File targetFile) throws IOException;
    public void makeContext(String fileName, String fileType, String... path) throws IOException;
    void setFile(String fileName, String fileType) throws AlphaInvalidFilePathException;
    String getFileName();
    String getFileType();
    String getFullFileName();
    String getFullFilePath();
    void setFileName(String name) throws AlphaInvalidFilePathException;
    void setFileType(String type) throws AlphaInvalidFilePathException;
    default boolean checkPathValid(String s){
        Pattern pattern = Pattern.compile("^[C-Z]:\\\\([^\\<\\>\\/\\\\\\|\\:\\\"\\*\\?]+\\\\)*$");
        return pattern.matcher(s).matches();
    }
    default boolean checkValidFileNameOrType(String s){
        Pattern pattern = Pattern.compile("^([^\\<\\>\\/\\\\\\|\\:\\\"\\*\\?])+$");
        return pattern.matcher(s).matches();
    }
}
