package Alpha.RespInstance.FileTransmission;

import java.io.Serializable;

public class DirStructure implements Serializable {
    private static final long serialVersionUID = 254215462512545L;
    private String currentPath;
    private String[] notEmptyDirectories;
    private String[] emptyDirectories;
    private String[] files;
    private Operation op;

    public enum Operation{
        getDir,newDir, deleteDir
    }
    void setNotEmptyDirectories(String[] notEmptyDirectories){
        this.notEmptyDirectories = notEmptyDirectories;
    }
    void setEmptyDirectories(String[] emptyDirectories){
        this.emptyDirectories = emptyDirectories;
    }
    void setFiles(String[] files){
        this.files = files;
    }
    private void setOperation(Operation op){
        this.op = op;
    }

    Operation getOperation(){
        return op;
    }

    public void setCurrentPath(String currentPath) {
        if(!currentPath.endsWith("\\"))
            currentPath = currentPath + "\\";
        this.currentPath = currentPath;
        this.op = Operation.getDir;
    }

    public void setCurrentPath(String currentPath, Operation op){
        setCurrentPath(currentPath);
        setOperation(op);
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public String[] getEmptyDirectories() {
        return emptyDirectories;
    }

    public String[] getFiles() {
        return files;
    }

    public String[] getNotEmptyDirectories() {
        return notEmptyDirectories;
    }

    public String[] getPartofCurrentPath(){
        return currentPath.split("\\\\");
    }

    public void list(){
        if(currentPath == null){
            System.out.println("error");
            return;
        }
        if(notEmptyDirectories == null || emptyDirectories == null || files == null){
            String opstr;
            if (op == Operation.newDir){
                opstr = "create dir";
            }else{
                opstr = "delete dir";
            }
            System.out.println("This is a command for dir，" + opstr + ": " + getCurrentPath());
            return;
        }
        System.out.println("path:" + getCurrentPath());
        System.out.println("------------- Not Empty Dir -------------");
        for (String ned: notEmptyDirectories
             ) {
            System.out.println(ned);
        }
        System.out.println("------------- Not Empty Dir -------------");
        System.out.println();
        System.out.println("-------------  Empty Dir -------------");
        for (String ed: emptyDirectories
        ) {
            System.out.println(ed);
        }
        System.out.println("-------------  Empty Dir -------------");
        System.out.println();
        System.out.println("-------------  Files -------------");
        for (String f: files
        ) {
            System.out.println(f);
        }
        System.out.println("-------------  Files -------------");
    }
}
