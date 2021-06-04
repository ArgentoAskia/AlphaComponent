package Alpha.RespInstance.FileTransmission;

import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.Server.BroadcastServer.BroadcasetResponding;
import Alpha.Server.BroadcastServer.LostingMessage;
import Alpha.Server.BroadcastServer.Message;
import Alpha.Server.HostMessage;

import java.io.File;
import java.util.LinkedList;

public class FileTransmissionResp extends BroadcasetResponding {
    private final DirStructureFactory dirStructureFactory;
    private final FileStructureFactory fileStructureFactory;
    private String rootPath;

    private Message rebackMsg;

    public FileTransmissionResp(String root, String rootName) {
        dirStructureFactory = new DirStructureFactory(root, rootName);
        fileStructureFactory = new FileStructureFactory(root, rootName);
        if (!root.endsWith("\\")) {
            root = root + "\\";
        }
        rootPath = root;
    }

    @Override
    public void connectionIn(HostMessage newClient, HostMessage[] allClients) {
        System.out.println("新连接进来了:" + newClient.toString());
        System.out.println("当前连接总数:" + allClients.length);
    }

    @Override
    public void connectionLost(HostMessage hostMessage, HostMessage[] allClients, Exception cause) {
        System.out.println("连接退出了:" + hostMessage.toString());
        System.out.println("退出原因:" + cause.getMessage());
        System.out.println("当前连接数:" + allClients.length);
    }

    @Override
    public void connectionOut(HostMessage outClient) {

    }

    @Override
    public void connectionFailed(Exception cause) {

    }

    @Override
    public void initialization(HostMessage newClient, AlphaMessageInputStream acceptor, AlphaMessageOutputStream rebacker, AlphaMessageOutputStream[] otherSenders) {
        DirStructure dirStructure = dirStructureFactory.createDirStructure(null);
        Message msg = new Message(new HostMessage("服务端", "服务端IP"));
        msg.writeObject(dirStructure);
        rebacker.WriteMessage(msg);
    }

    @Override
    public Message msgInput(AlphaMessageInputStream acceptor) {
        Message msg = acceptor.readMessage();
        if (msg != null) {
            // todo 上传文件、新建目录等等操作
            // 返回新一个新的DirStructure message给各位转发服务器
            Object obj = msg.readObject();
            if (obj instanceof DirStructure) {
                return solveDirStructureObj(obj);
            } else if (obj instanceof FileStructure) {
                return solveFileStructureObj(obj);
            }
        }
        return msg;
    }

    private Message solveDirStructureObj(Object obj) {
        DirStructure dirStructure = (DirStructure) obj;
        switch (dirStructure.getOperation()) {
            case getDir: {
                dirStructure = dirStructureFactory.dispatchDirStructure(dirStructure);
                rebackMsg = new Message(new HostMessage("服务器", "服务器IP"));
                rebackMsg.writeObject(dirStructure);
                Message msg = new Message();
                msg.writeString("noBroadcast");
                return msg;
            }
            case newDir: {
                String full = dirStructure.getCurrentPath();
                if (full.endsWith("\\")) {
                    full = full.substring(0, full.length() - 1);
                }
                String exist = full.substring(0, full.lastIndexOf("\\"));
                String realDir = dirStructureFactory.getRealDir(full);
                File file = new File(realDir);
                Message msg = new Message(new HostMessage("服务器", "服务器IP"));
                rebackMsg = new Message(new HostMessage("服务器", "服务器IP"));
                if (file.mkdir()) {
                    DirStructure dirStructure1 = dirStructureFactory.createDirStructure(exist + "\\");
                    msg.writeObject(dirStructure1);
                    rebackMsg.writeObject(dirStructure1);
                    return msg;
                } else {
                    String cause;
                    if(file.exists()){
                        cause = "目录:" + file.getName() + "已存在";
                    }else{
                        cause = "未知错误!\n无法创建:" + file.getPath();
                    }
                    msg.writeString("noBroadcast");
                    rebackMsg.writeString("目录创建失败，\n原因：" + cause);
                    return msg;
                }
            }
            case deleteDir: {
                String full = dirStructure.getCurrentPath();
                if (full.endsWith("\\")) {
                    full = full.substring(0, full.length() - 1);
                }
                String exist = full.substring(0, full.lastIndexOf("\\"));
                String realDir = dirStructureFactory.getRealDir(full);
                synchronized (fileStructureFactory) {
                    if(dirStructureFactory.deleteDir(realDir)){
                        Message msg = new Message(new HostMessage("服务器", "服务器IP"));
                        rebackMsg = new Message(new HostMessage("服务器", "服务器IP"));
                        DirStructure dirStructure1 = dirStructureFactory.createDirStructure(exist + "\\");
                        msg.writeObject(dirStructure1);
                        rebackMsg.writeObject(dirStructure1);
                        return msg;
                    }else{
                        Message msg = new Message();
                        msg.writeString("noBroadcast");
                        rebackMsg = new Message(new HostMessage("服务器", "服务器IP"));
                        rebackMsg.writeString("删除失败，目录不存在！");
                        return msg;
                    }

                }
            }
        }
        return null;
    }

    private Message solveFileStructureObj(Object obj) {
        FileStructure fileStructure = (FileStructure) obj;
        switch (fileStructure.getOper()) {
            case deleteFile: {
                String path = fileStructure.getPath();
                String dir = path.substring(0, path.lastIndexOf("\\")) + "\\";
                synchronized (fileStructureFactory) {
                    fileStructureFactory.executeDeleteStructure(fileStructure);
                    DirStructure dirStructure = dirStructureFactory.createDirStructure(dir);
                    Message msg = new Message(new HostMessage("服务器", "服务器IP"));
                    msg.writeObject(dirStructure);
                    rebackMsg = new Message(new HostMessage("服务器", "服务器IP"));
                    rebackMsg.writeObject(dirStructure);
                    return msg;
                }
            }
            case downloadFile: {
                fileStructure = fileStructureFactory.filledDownloadStructure(fileStructure);
                if(fileStructure == null){
                    rebackMsg = new Message(new HostMessage("服务器", "服务器IP"));
                    rebackMsg.writeString("文件填充发生错误!!");
                }else{
                    rebackMsg = new Message(new HostMessage("服务器", "服务器IP"));
                    rebackMsg.writeObject(fileStructure);
                }
                Message msg = new Message();
                msg.writeString("noBroadcast");
                return msg;
            }
            case uploadFile: {
                String path = fileStructure.getPath();
                path = path.substring(0, path.lastIndexOf("\\"));
                synchronized (fileStructureFactory) {
                    fileStructureFactory.executeUploadStructure(fileStructure);
                    DirStructure dirStructure = dirStructureFactory.createDirStructure(path);
                    Message msg = new Message(new HostMessage("服务器", "服务器IP"));
                    msg.writeObject(dirStructure);
                    rebackMsg = new Message(new HostMessage("服务器", "服务器IP"));
                    rebackMsg.writeObject(dirStructure);
                    return msg;
                }
            }
            case renameFile: {
                Message msg = new Message(new HostMessage("服务器", "服务器IP"));
                msg.writeObject(null);
                return msg;
            }
        }
        return null;
    }

    @Override
    public boolean broadcastMsg(Message msg, AlphaMessageOutputStream[] otherSenders, LinkedList<LostingMessage> failSendIndex) {
        if(msg.readString() != null && msg.readString().equals("noBroadcast")){
            return true;
        }
       if(msg.readString() == null && rebackMsg == null){
           return false;
       }
        for (AlphaMessageOutputStream other:otherSenders
             ) {
            other.WriteMessage(msg);
        }
        return true;
    }

    @Override
    public boolean resultBack(AlphaMessageOutputStream rebacker, HostMessage[] lostingClientMsgs, Exception[] causes, boolean run) {
        if(rebacker == null){
            return run;
        }
        rebacker.WriteMessage(rebackMsg);
        return run;
    }
}
