package Alpha.Exceptions.Solution;

import Alpha.Exceptions.Solution.Solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AlphaFileNotFoundSolution extends Solution<String> {
    private String happened = null;
    private String fixedFileName = null;

    /**
     * 该类提供了对{@link FileNotFoundException}的一个简单的解决方案
     * @param exception 提供一个{@link FileNotFoundException}异常
     */
    public AlphaFileNotFoundSolution(FileNotFoundException exception, String path, String fixedFileName) {
        super(exception, path);
        this.fixedFileName = fixedFileName;

    }
    public AlphaFileNotFoundSolution(FileNotFoundException exception, String path, String fixedFileName, String happenedFunc){
        super(exception, path);
        happened = happenedFunc;
        this.fixedFileName = fixedFileName;
    }


    /**
     * 指定一个解决方案,如果不满足于默认的处理方式,在创建<code>AlphaFileNotFoundSolution</code>时可以重写该方法
     * @param e 参考参数,提供具体的Exception对象共解决方案使用
     * @param keyData 参考参数,提供具体的数据段供给用户解决问题
     */
    @Override
    protected String solution(Throwable e, String keyData) {
        printExceptionMessage(e.getMessage());
        if(Files.isDirectory(Paths.get(keyData))) {
             return directoryCause(keyData);
        }
        if(Files.notExists(Paths.get(keyData))){
            return pathNotExistence(keyData);
        }
        // 超出solution处理能力
        return null;
    }
    protected void printExceptionMessage(String message){
        if(happened == null)
            System.out.println(message);
        else{
            System.out.println("happended in function: " + happened);
            System.out.println(message);
        }
    }
    // 前提是确定路径是目录
    protected String directoryCause(String keyData){
            // 默认文件名
            if (fixedFileName == null) {
                fixedFileName = "FixedFileFor" + happened.substring(0, happened.indexOf("(")) + Math.random() * 10000 + ".obj";
            }
            if (!keyData.endsWith("\\")) {
                keyData +="\\";
            }
            File fullpath = new File(keyData,fixedFileName);
            if(!fullpath.exists()){
                try {
                    fullpath.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return keyData + fixedFileName;
    }
    // 前提是确定已经是文件
    protected String pathNotExistence(String keyData){
        File fullfile = new File(keyData);
        fullfile.mkdirs();
        try {
            fullfile.createNewFile();
            return fullfile.toString();
        } catch (IOException e) {
            // 最一个地方没有文件名,需要自己创建.
            fixedFileName = "FixedFileFor" + happened.substring(0, happened.indexOf("(")) + Math.random() * 10000 + ".obj";
            fullfile = new File(fullfile, fixedFileName);
            try {
                fullfile.createNewFile();
                return fullfile.toString();
            } catch (IOException ex) {
                // 如果还抛出异常,代表超出解决方案解决能力
                ex.printStackTrace();
                return null;
            }
        }
    }
}

