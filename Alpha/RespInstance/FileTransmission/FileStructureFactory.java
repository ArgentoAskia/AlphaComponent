package Alpha.RespInstance.FileTransmission;

import Alpha.Server.BroadcastServer.Message;
import Alpha.Server.HostMessage;

import javax.swing.*;
import java.io.*;

public class FileStructureFactory {
    private String root;
    private String rootName;

    FileStructureFactory(String root, String rootName){
        this.root = root;
        this.rootName = rootName;
    }


    FileStructure filledDownloadStructure(FileStructure emptyDownloadStructure){
        String path = emptyDownloadStructure.getPath();
        String realPath = getRealFilePath(path);
        try(FileInputStream fileInputStream = new FileInputStream(realPath)) {
            byte[] data = fileInputStream.readAllBytes();
            emptyDownloadStructure.setData(data);
            return emptyDownloadStructure;
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在！！！");
            System.out.println("发生在fileStructureFactory.filledDownloadStructure()");
            return null;
        } catch (IOException e) {
            System.out.println("IO错误！！！");
            e.printStackTrace();
            return null;
        }


    }
    boolean executeDeleteStructure(FileStructure deleteStructure){
        String realPath = getRealFilePath(deleteStructure.getPath());
        File file = new File(realPath);
        return file.delete();
    }
    boolean executeRenameStructure(FileStructure RenameStructure){
        String realPath = getRealFilePath(RenameStructure.getPath());

        String newFile = new String(RenameStructure.getData());
        File file = new File(realPath);

        String beginPart = realPath.substring(0, realPath.lastIndexOf("\\"));
        String newFilePath = beginPart + "\\" + newFile;
        File newFilePath1 = new File(newFilePath);
        if(file.renameTo(newFilePath1)){
            return true;
        }else{
            return false;
        }
    }
    void executeUploadStructure(FileStructure uploadStructure){
        String realPath = getRealFilePath(uploadStructure.getPath());
        byte[] datas = uploadStructure.getData();
        File file = new File(realPath);
        try {
            if(file.createNewFile()){
                FileOutputStream fileOutputStream = new FileOutputStream(realPath);
                fileOutputStream.write(datas);
                fileOutputStream.close();
            }else{
                if(JOptionPane.showConfirmDialog(null,
                        "文件已存在，是否覆盖？", "发现同名文件",
                         JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    FileOutputStream fileOutputStream = new FileOutputStream(realPath);
                    fileOutputStream.write(datas);
                    fileOutputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    String getRealFilePath(String path){
        String rootname = path.substring(0, path.indexOf("\\"));
        if(rootname.equals(rootName)){
            String lastPart = path.substring(path.indexOf("\\"));
            return root + lastPart;
        }else{
            return null;
        }
    }

}
