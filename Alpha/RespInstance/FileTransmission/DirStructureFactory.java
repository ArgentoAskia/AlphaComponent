package Alpha.RespInstance.FileTransmission;

import Alpha.Server.BroadcastServer.Message;
import Alpha.Server.HostMessage;

import java.io.File;
import java.util.LinkedList;

public class DirStructureFactory {
    private String root;
    private String rootName;

    private String currentPathLastPart;
    private File[] subFiles;
    private LinkedList<String> emptyDirctoriesList;
    private LinkedList<String> notEmptyDirctoriesList;
    private LinkedList<String> filesList;

    DirStructureFactory(String root, String rootName) {
        if (root.endsWith("\\")) {
            root = root.substring(0, root.length() - 1);
        }
        if (rootName.endsWith("\\")) {
            rootName = rootName.substring(0, rootName.length() - 1);
        }
        this.root = root;
        this.rootName = rootName;
    }

    private void dispatchCurrentPath(String currentPath) {
        if (currentPath == null) {
            currentPath = rootName;
        }
        if (!currentPath.endsWith("\\")) {
            currentPath += "\\";
        }
        // 去掉rootName
        String rootName = currentPath.substring(0, currentPath.indexOf("\\"));
        if (this.rootName.equals(rootName)) {
            this.currentPathLastPart = currentPath.substring(currentPath.indexOf("\\"));
            File currentFile = new File(root + this.currentPathLastPart);
            subFiles = currentFile.listFiles();
        }
    }

    DirStructure createDirStructure(String currentPath) {
        dispatchCurrentPath(currentPath);
        if (currentPathLastPart == null || subFiles == null) {
            return null;
        } else {
            DirStructure dirStructure = new DirStructure();
            getSubDirStructure();
            dirStructure.setCurrentPath(rootName + currentPathLastPart);
            dirStructure.setFiles(filesList.toArray(new String[0]));
            dirStructure.setEmptyDirectories(emptyDirctoriesList.toArray(new String[0]));
            dirStructure.setNotEmptyDirectories(notEmptyDirctoriesList.toArray(new String[0]));
            return dirStructure;
        }
    }

    DirStructure dispatchDirStructure(DirStructure dirStructure) {
        dispatchCurrentPath(dirStructure.getCurrentPath());
        if (currentPathLastPart == null || subFiles == null) {
            return null;
        } else {
            getSubDirStructure();
            dirStructure.setFiles(filesList.toArray(new String[0]));
            dirStructure.setEmptyDirectories(emptyDirctoriesList.toArray(new String[0]));
            dirStructure.setNotEmptyDirectories(notEmptyDirctoriesList.toArray(new String[0]));
            return dirStructure;
        }
    }

    private void getSubDirStructure() {
        notEmptyDirctoriesList = new LinkedList<>();
        emptyDirctoriesList = new LinkedList<>();
        filesList = new LinkedList<>();
        for (File file : subFiles
        ) {
            if (file.isDirectory()) {
                if (file.getName().equals("Config.Msi"))
                    continue;
                if (file.getName().equals("System Volume Information"))
                    continue;
                if (file.getName().equals("$RECYCLE.BIN"))
                    continue;
                File subFile = new File(file.getPath() + "\\");
                File[] files = subFile.listFiles();
                int i = 0;
                int length = files.length;
                for (File f:files
                     ) {
                    if(f.isFile()){
                        i++;
                    }
                }
                if (i < length) {
                    notEmptyDirctoriesList.add(file.getName());
                } else {
                    emptyDirctoriesList.add(file.getName());
                }
            } else if (file.isFile()) {
                filesList.add(file.getName());
            }
        }
    }

    /**
     * 分配真实目录信息
     *
     * @param currentPath 客户端虚拟目录
     * @return
     */
    String getRealDir(String currentPath) {
        if (currentPath == null) {
            currentPath = rootName;
        }
        if (!currentPath.endsWith("\\")) {
            currentPath += "\\";
        }
        // 去掉rootName
        String rootName = currentPath.substring(0, currentPath.indexOf("\\"));
        if (this.rootName.equals(rootName)) {
            this.currentPathLastPart = currentPath.substring(currentPath.indexOf("\\"));
        }
        return root + this.currentPathLastPart;
    }

    /**
     * 判断指定的文件或文件夹删除是否成功
     *
     * @param FileName 文件或文件夹的路径
     * @return true or false 成功返回true，失败返回false
     */
    private boolean deleteAnyone(String FileName) {

        File file = new File(FileName);//根据指定的文件名创建File对象


        if (!file.exists()) {  //要删除的文件不存在
            System.out.println("文件" + FileName + "不存在，删除失败！");
            return false;
        } else { //要删除的文件存在

            if (file.isFile()) { //如果目标文件是文件

                return deleteFile(FileName);

            } else {  //如果目标文件是目录
                return deleteDir(FileName);
            }
        }
    }

    public boolean deleteFile(String fileName) {
        File file = new File(fileName);//根据指定的文件名创建File对象
        if (file.exists() && file.isFile()) { //要删除的文件存在且是文件
            if (file.delete()) {
                System.out.println("文件" + fileName + "删除成功！");
                return true;
            } else {
                System.out.println("文件" + fileName + "删除失败！");
                return false;
            }
        } else {

            System.out.println("文件" + fileName + "不存在，删除失败！");
            return false;
        }


    }


    /**
     * 删除指定的目录以及目录下的所有子文件
     *
     * @param dirName is 目录路径
     * @return true or false 成功返回true，失败返回false
     */
    public boolean deleteDir(String dirName) {

        if (!dirName.endsWith(File.separator))//dirName不以分隔符结尾则自动添加分隔符
            dirName = dirName + File.separator;

        File file = new File(dirName);//根据指定的文件名创建File对象

        if (!file.exists() || (!file.isDirectory())) { //目录不存在或者
            System.out.println("目录删除失败" + dirName + "目录不存在！");
            return false;
        }

        File[] fileArrays = file.listFiles();//列出源文件下所有文件，包括子目录


        for (int i = 0; i < fileArrays.length; i++) {//将源文件下的所有文件逐个删除
            deleteAnyone(fileArrays[i].getAbsolutePath());
        }


        if (file.delete())//删除当前目录
            System.out.println("目录" + dirName + "删除成功！");


        return true;

    }

}
