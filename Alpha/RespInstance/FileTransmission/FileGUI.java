package Alpha.RespInstance.FileTransmission;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;
import javax.swing.tree.*;


import Alpha.Client.AlphaClient;
import Alpha.Client.AlphaClientConstants;
import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.RespInstance.FileTransmission.DirStructure;
import Alpha.RespInstance.FileTransmission.FileStructure;
import Alpha.Server.BroadcastServer.Message;


/**
 * GUI类，界面的组件配置都放在了这个地方，需要更改组件请在这个类中改
 * 组件事件监听代码需要留接口，采用回调方法的方式，具体接口的写法，参考类{@link WidgetEvent}前的注解
 */
public class FileGUI {

	private JFrame JF;
	private JPanel JP;
	private JButton JB;
	private JPanel panel;
	private JLabel LB1;
	private JLabel JL2;
	private JPanel downPanel;
	private JButton JB1;

	/*
		界面使用了BorderLayout布局,下面的组件位于中间(Center)部分和底部(South)
	 */
	// 底边显示当前选择位置的标签
	private JLabel currentPath;

	// 中间部分
	// 分割面板,中间有一个分割条可以控制两边组件的宽度
	private JSplitPane pane;
	// 承载目录树的面板
	private JScrollPane dirscrollPane;
	// 承载文件的面板
	private JScrollPane filescrollPane;
	// 目录树
	private JTree dirTree;
	// 文件框
	private JList<String> fileList;
	//两个弹出菜单
	private JPopupMenu dirMenu;
	private JPopupMenu fileMenu;

	// 接口字段,所有组件的事件响应方法都应放在这个类中,添加事件请跳转到WidgetEvent中添加
	private WidgetEvent event;

	// 客户端事件,详情请看event类
	private AlphaClientEvent clientAlphaClientEvent;

	// 客户端组件
	private AlphaClient client;

	public FileGUI() {

		// 新建事件响应对象
		event = new WidgetEvent();

		UnknownGUICode();

		// 初始化底部(South)的组件
		initDownPanel();
		// 初始化中间(Center)的组件
		initCenterPanel();
		// 初始化客户端组件(AlphaClient)
		initClient();

		JF.setVisible(true);
	}
	private void UnknownGUICode(){
		JF = new JFrame();
		JF.setFont(new Font("Dialog", Font.PLAIN, 16));
		JF.setForeground(Color.WHITE);
		JF.setTitle("管理员");
		JF.getContentPane().setBackground(Color.WHITE);
		JF.setLayout(new BorderLayout());

		JP = new JPanel();
		JB = new JButton();

		JF.setTitle("管理员");
		JF.setSize(700, 600);
		JF.setLocationRelativeTo(null);
		JF.setResizable(true);
		JF.setDefaultCloseOperation(JF.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBackground(SystemColor.activeCaption);
		panel.setToolTipText("");
		// panel.setBounds(0, 0, 696, 107);
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(696, 107));

		LB1 = new JLabel("管理员");
		LB1.setHorizontalAlignment(SwingConstants.CENTER);
		LB1.setBounds(283, 10, 112, 31);
		LB1.setFont(new Font("幼圆", Font.PLAIN, 28));
		panel.add(LB1);

		JL2 = new JLabel("           管理员文件管理");
		JL2.setFont(new Font("幼圆", Font.PLAIN, 14));
		JL2.setBounds(215, 51, 259, 38);
		panel.add(JL2);

		JF.add(panel, BorderLayout.NORTH);
	}
	// 初始化底部(South)的组件
	private void initDownPanel() {

		// downPanel为底部布局(South)所用的顶层容器,该容器采用流式布局.(FlowLayout)
		// 注意区分:窗口JFrame使用的是BorderLayout,downPanel是FlowLayout
		// downPanel最终将会放在JFrame的底部(South)!
		downPanel = new JPanel();
		downPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 3));
		downPanel.setBackground(Color.WHITE);

		JB1 = new JButton("返回");
		JB1.setBackground(Color.WHITE);
		JB1.addActionListener(e -> {
			JF.dispose();
			// Manageract manageract = new Manageract();
		});

		// 底部标签组件初始化
		currentPath = new JLabel();
		currentPath.setText("服务器当前位置:");
		currentPath.setOpaque(true);

		// 底部downPanel添加组件
		downPanel.add(currentPath);
		downPanel.add(JB1);
		// 窗口添加downPanel
		JF.add(downPanel, BorderLayout.SOUTH);
	}
	// 初始化中间(Center)的组件
	private void initCenterPanel(){
		// 目录树弹出菜单初始化
		// 在目录树中右键将显示:新建目录、删除目录两个子菜单！
		// JPopupMenu的构造方法中的dirmenu为组件名称而不是组件显示名称,不会显示的！
		dirMenu = new JPopupMenu("dirmenu");
		JMenuItem newDir = new JMenuItem("新建目录");
		JMenuItem deleteDir = new JMenuItem("删除目录");
		dirMenu.add(newDir);
		dirMenu.add(deleteDir);

		// 下面三个是添加组件事件，请留接口，具体参考类WidgetEvent前的注解
		dirMenu.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				if(dirTree.getSelectionPath() == null){
					event.Event_DirMenuItemDisable(dirMenu.getSubElements(), e);
				}else{
					event.Event_DirMenuItemEnableToSelected( dirMenu.getSubElements(),e);
				}
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {

			}
		});
		newDir.addActionListener(e->{
			String message = JOptionPane.showInputDialog(JF, "请输入目录名:", "新建目录", JOptionPane.QUESTION_MESSAGE);
			if(message == null || message.equals("")){
				JOptionPane.showMessageDialog(JF, "输入信息不能为空!!","输入信息非法!!",JOptionPane.ERROR_MESSAGE);
				return;
			}
			String path = currentPath.getText().substring(currentPath.getText().indexOf(":") + 1);
			DefaultMutableTreeNode selectedDefaultMutableTreeNode = getTreeNodeByStringPath(path);
			if(selectedDefaultMutableTreeNode!=null){
				event.Event_NewDirMenuitemClick(client,getDirTreeSelectedPath(selectedDefaultMutableTreeNode) ,message, clientAlphaClientEvent,e);
			}


		});
		deleteDir.addActionListener(e ->{
			String path = currentPath.getText().substring(currentPath.getText().indexOf(":") + 1);
			DefaultMutableTreeNode selectedDefaultMutableTreeNode = getTreeNodeByStringPath(path);
			if(selectedDefaultMutableTreeNode!=null) {
				event.Event_DeleteDirMenuitemClick(client, getDirTreeSelectedPath(selectedDefaultMutableTreeNode), clientAlphaClientEvent, e);
			}
		});

		dirTree = new JTree();
		dirTree.setComponentPopupMenu(dirMenu);
		dirTree.setInheritsPopupMenu(true);
		FileTreeCellRenderer treeCellRenderer = new FileTreeCellRenderer();
		treeCellRenderer.setLeafIcon(treeCellRenderer.getDefaultClosedIcon());
		dirTree.setCellRenderer(treeCellRenderer);
		dirTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		// 下面是组件事件，请留接口，具体参考类WidgetEvent前的注解
		dirTree.addTreeSelectionListener(e -> {
			// TODO Auto-generated method stub
			DefaultMutableTreeNode selectedDefaultMutableTreeNode = (DefaultMutableTreeNode) dirTree.getLastSelectedPathComponent();
			clientAlphaClientEvent.setCurrentNode(selectedDefaultMutableTreeNode);

			if (selectedDefaultMutableTreeNode != null) {
				System.out.println(selectedDefaultMutableTreeNode);
				String fullPathString = getDirTreeSelectedPath(selectedDefaultMutableTreeNode);
				System.out.println("fullPathString:" + fullPathString);
				currentPath.setText("服务器当前位置:" + fullPathString);
				event.Event_DirTreeSelected(fullPathString, client, e);
			}
		});
		dirTree.addTreeWillExpandListener(new TreeWillExpandListener() {
			@Override
			public void treeWillExpand(TreeExpansionEvent event1) {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode selectedDefaultMutableTreeNode = (DefaultMutableTreeNode) event1.getPath().getLastPathComponent();
				clientAlphaClientEvent.setCurrentNode(selectedDefaultMutableTreeNode);
				if (selectedDefaultMutableTreeNode != null) {
					System.out.println(selectedDefaultMutableTreeNode);
					String fullPathString = getDirTreeSelectedPath(selectedDefaultMutableTreeNode);
					System.out.println("fullPathString:" + fullPathString);
					currentPath.setText("服务器当前位置:" + fullPathString);
					event.Event_TreeExpanding(fullPathString, client,event1);

				}
			}

			@Override
			public void treeWillCollapse(TreeExpansionEvent event2) {
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) event2.getPath().getLastPathComponent();
				DefaultTreeModel model = (DefaultTreeModel) dirTree.getModel();

				// 先添加再删除!因为非叶子节点不允许没有子项!!
				DefaultMutableTreeNode connetionNode = new DefaultMutableTreeNode("正在连接服务器...");
				model.insertNodeInto(connetionNode, treeNode, 0);

				System.out.println("子节点数:" + model.getChildCount(treeNode));
				System.out.println("关闭节点数:" + treeNode.getChildCount());
				int childCount = treeNode.getChildCount();
				for (int i = 1; i < childCount; i++) {
					System.out.println("关闭节点数:" + treeNode.getChildAt(1));
					DefaultMutableTreeNode child = (DefaultMutableTreeNode) treeNode.getChildAt(1);
					model.removeNodeFromParent(child);
				}
				// System.out.println("关闭节点数:" + treeNode.getChildCount());
			}
		});

		fileList = new JList<>();
		fileMenu = new JPopupMenu();
		JMenuItem uploadItem = new JMenuItem("上传文件");
		JMenuItem downloadItem = new JMenuItem("下载文件");
		JMenuItem deleteItem = new JMenuItem("删除文件");
		fileMenu.add(uploadItem);
		fileMenu.add(downloadItem);
		fileMenu.add(deleteItem);
		fileList.setComponentPopupMenu(fileMenu);

		// 弹出菜单事件代码

		uploadItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode selectedDefaultMutableTreeNode = (DefaultMutableTreeNode) dirTree.getLastSelectedPathComponent();
				if(selectedDefaultMutableTreeNode!=null) {
					String abstractPathString = getDirTreeSelectedPath(selectedDefaultMutableTreeNode);

					JFileChooser jchooser1 = new JFileChooser();
					int index = jchooser1.showOpenDialog(JF);
					if(index == JFileChooser.APPROVE_OPTION){
						File file = jchooser1.getSelectedFile();
						String fileNameString = file.getName();
						event.Event_FileUploadItemClick(client, abstractPathString + fileNameString, file);
					}else if (index == JFileChooser.ERROR_OPTION) {
						JOptionPane.showMessageDialog(JF, "无法读取文件");
					}
				}
			}
		});
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selectedDefaultMutableTreeNode = (DefaultMutableTreeNode) dirTree.getLastSelectedPathComponent();
				if(selectedDefaultMutableTreeNode!=null) {
					String abstractPathString = getDirTreeSelectedPath(selectedDefaultMutableTreeNode);
					String fileName = fileList.getSelectedValue();
					event.Event_FileDeleteItemClick(client, abstractPathString + fileName);
				}
			}
		});
		downloadItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selectedDefaultMutableTreeNode = (DefaultMutableTreeNode) dirTree.getLastSelectedPathComponent();
				if(selectedDefaultMutableTreeNode!=null) {
					String abstractPathString = getDirTreeSelectedPath(selectedDefaultMutableTreeNode);
					String fileName = fileList.getSelectedValue();
					event.Event_FileDownloadItemClick(client, abstractPathString + fileName);
				}
			}
		});

		dirscrollPane = new JScrollPane();
		dirscrollPane.setViewportView(dirTree);

		filescrollPane = new JScrollPane();
		filescrollPane.setViewportView(fileList);

		pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dirscrollPane, filescrollPane);

		pane.setDividerLocation(400 + pane.getInsets().left);
		pane.setDividerSize(5);
		pane.setResizeWeight(1);
		pane.setContinuousLayout(true);
		pane.setBackground(Color.WHITE);
		JF.add(pane, BorderLayout.CENTER);

	}
	// 初始化客户端组件(AlphaClient)
	private void initClient(){
		// 注册客户端组件事件！
		clientAlphaClientEvent = new AlphaClientEvent(fileList, dirTree, currentPath);
		// 初始化客户端，绑定事件
		client = new AlphaClient("10.64.103.47", 13251, clientAlphaClientEvent, AlphaClientConstants.BROADCAST_INIT_ACCEPT);
		// 设置客户端接收信息速率
		client.broadcastAccepting(100);

	}

	private DefaultMutableTreeNode getTreeNodeByStringPath(String path) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) dirTree.getModel().getRoot();
		// 去掉末尾的"\"
		if (path.endsWith("\\")) {
			path = path.substring(0, path.length() - 1);
		}
		// 分割目录位置
		String[] pathParts = path.split("\\\\");
		// 判断root
		if (pathParts[0].equals(root.getUserObject())) {
			// 获取根目录子节点
			int childCount = root.getChildCount();
			// 初始化遍历
			DefaultMutableTreeNode travler = root;

			for (int i = 1; i < pathParts.length; i++) {

				// 寻找响应和的分割目录
				for (int j = 0; j < childCount; j++) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) travler.getChildAt(j);
					if (pathParts[i].equals(node.getUserObject())) {
						// 找到匹配的节点,设置遍历节点,打断循环
						travler = node;
						childCount = travler.getChildCount();
						break;
					}
				}
			}
			return travler;
		}
		return null;
	}
	/**
	 * 返回指定节点到根节点的路径
	 * @param defaultMutableTreeNode 目录树中任何一个节点
	 * @return 该节点到根目录的位置，如一个目录结构是根目录下的Code文件夹，则返回“根目录\Code\”
	 */
	public static String getDirTreeSelectedPath(DefaultMutableTreeNode defaultMutableTreeNode) {
		DefaultMutableTreeNode travelerTreeNode = defaultMutableTreeNode;
		StringBuilder fullPathString = new StringBuilder();
		while(travelerTreeNode != null) {
			fullPathString.insert(0, travelerTreeNode.getUserObject() + "\\");
			travelerTreeNode = (DefaultMutableTreeNode) travelerTreeNode.getParent();
		}
		return fullPathString.toString();
	}

	public static void main(String[] args) {
		new FileGUI();
	}
}

/**
 * 客户响应事件层，要改GUI组件代码请回到FileGUI类中：
 *
 * 由于文件传输使用的是转发服务器，因此，需要关注的是名字带Broadcast的方法，这里给出几个方法的简介
 * doBroadcastAccept():用于处理服务端回传的响应信息，该方法会每个一定的时间执行一次，设置时间速率的相关方法
 * 					   在{@code initClient()}中的client.broadcastAccepting()
 *
 * initializationBroadcastAccept()：接收初始化，当连接到服务器时，服务器会将当前的目录结构发送给客户端,客户端就在
 * 									这个方法中进行初始化的目录结构的处理
 *
 *
 * initializationBroadcastSend()：发送初始化，在文件传输中不需要该方法！因此无需修改！
 *
 *
 * 如果不知道我说什么的话，请不要改！直接跳过这个类。
 *
 */
class AlphaClientEvent implements Alpha.Client.AlphaClientEvent {
	private JList<String> fileList;
	private JTree dirTree;
	private JLabel currentDir;
	private DefaultMutableTreeNode currentNode;
	private String operation;
	AlphaClientEvent(JList<String> arg0, JTree arg1, JLabel arg2) {
		// TODO Auto-generated constructor stub
		fileList = arg0;
		dirTree = arg1;
		currentDir = arg2;
	}

	@Override
	public void connectionFailed(Exception arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void doAccept(AlphaInputStream arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void error(Exception arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getMessage());
		arg0.printStackTrace();

	}
	@Override
	public void initializationAccept(AlphaInputStream arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void initializationSend(AlphaOutputStream arg0, byte[] arg1) {
		// TODO Auto-generated method stub

	}



	@Override
	public void initializationBroadcastAccept(AlphaMessageInputStream arg0) {
		// TODO Auto-generated method stub
		Message msg = arg0.readMessage();
		Object obj = msg.readObject();
		if (obj instanceof DirStructure) {
			DirStructure dirStructure = (DirStructure) obj;
			String pathString = dirStructure.getCurrentPath();
			if (pathString.endsWith("\\")) {
				pathString = pathString.substring(0, pathString.length() - 1);
			}
			initDir(pathString, dirStructure.getNotEmptyDirectories(), dirStructure.getEmptyDirectories());
			initFileList(dirStructure.getFiles());
		}

	}
	private void initDir(String root, String[] dir1, String[] dir2) {
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);
		for (String string : dir1) {
			DefaultMutableTreeNode notEmptyNode = new DefaultMutableTreeNode(string);
			DefaultMutableTreeNode notEmptyNodeSub = new DefaultMutableTreeNode("正在连接服务器...");
			notEmptyNode.add(notEmptyNodeSub);
			rootNode.add(notEmptyNode);
		}
		for (String string : dir2) {
			DefaultMutableTreeNode EmptyNode = new DefaultMutableTreeNode(string);
			rootNode.add(EmptyNode);
		}
		DefaultTreeModel defaultTreeModel = new DefaultTreeModel(rootNode);
		dirTree.setModel(defaultTreeModel);
	}
	private void initFileList(String[] file) {
		fileList.setListData(file);
	}

	@Override
	public void doBroadcastAccept(AlphaMessageInputStream arg0) {
		// TODO Auto-generated method stub

		System.out.println("Accept");
		Message msg = arg0.readMessage();
		if (msg != null) {
			Object obj = msg.readObject();
			if (obj instanceof DirStructure) {
				DirStructure dirStructure = (DirStructure) obj;
				if(operation == null){
					Update(dirStructure, currentNode);
				}else if(operation.equals("新建目录")){
					updateDirAndFileWidget(dirStructure,currentNode);
					operation = null;
				}else if(operation.equals("删除目录")){
					updateDirAndFileWidget(dirStructure, (DefaultMutableTreeNode) currentNode.getParent());
					operation = null;
				}else if(operation.equals("上传文件") || operation.equals("删除文件")){
					updateFileWidget(dirStructure);
					operation = null;
				}
			}
			else if(obj instanceof FileStructure){
				FileStructure fileStructure = (FileStructure) obj;
				processFileStructure(fileStructure);
			}
			else if(obj instanceof String){
				String s = msg.readString();
				processWarningMessage(s, null);
			}

		}
	}
	private void Update(DirStructure dirStructure, DefaultMutableTreeNode currentNode){
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) dirTree.getModel().getRoot();
		String currentPath = dirStructure.getCurrentPath();

		// 去掉末尾的"\"
		if(currentPath.endsWith("\\")){
			currentPath = currentPath.substring(0, currentPath.length() - 1);
		}
		// 分割目录位置
		String[] pathParts = currentPath.split("\\\\");
		// 判断root
		if(pathParts[0].equals(root.getUserObject())){
			// 获取根目录子节点
			int childCount = root.getChildCount();
			// 初始化遍历
			DefaultMutableTreeNode travler = root;

			for (int i = 1; i < pathParts.length; i++) {

				// 寻找响应和的分割目录
				for (int j = 0; j < childCount; j++) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) travler.getChildAt(j);
					if(pathParts[i].equals(node.getUserObject())){
						// 找到匹配的节点,设置遍历节点,打断循环
						travler = node;
						childCount = travler.getChildCount();
						break;
					}
				}
			}
			// 更新目录树
			updateDir(dirStructure, travler);

			// 文件框更新
			// 只有当前的节点,也就是文件框显示的文件的父文件夹==travel才能更新文件框
			if(currentNode != null){
				String current = (String) currentNode.getUserObject();
				String web = (String) travler.getUserObject();
				if(web.equals(current)){
					updateFileWidget(dirStructure);
				}
			}
		}
	}
	private void updateDirAndFileWidget(DirStructure dirStructure, DefaultMutableTreeNode treeNode) {

		String selectedPathString = FileGUI.getDirTreeSelectedPath(treeNode);
		currentDir.setText("服务器当前位置:" + selectedPathString);
		String pathString = dirStructure.getCurrentPath();

		if(!pathString.endsWith("\\")) {
			pathString = pathString + "\\";
		}
		if(!selectedPathString.endsWith("\\")) {
			selectedPathString = selectedPathString + "\\";
		}
		if(selectedPathString.equalsIgnoreCase(pathString)) {
			String[] fileStrings = dirStructure.getFiles();
			fileList.setListData(fileStrings);
			// important
			updateDir(dirStructure, treeNode);
		}else{
			treeNode = (DefaultMutableTreeNode) dirTree.getLastSelectedPathComponent();
			updateDir(dirStructure, treeNode);
		}
	}
	private void updateDir(DirStructure dirStructure, DefaultMutableTreeNode treeNode){
		int insertIndex = 0;
		DefaultTreeModel model = (DefaultTreeModel) dirTree.getModel();
		String[] dirNotEmptyStrings = dirStructure.getNotEmptyDirectories();
		String[] dirEmptyStrings = dirStructure.getEmptyDirectories();

		for (String string : dirNotEmptyStrings) {
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(string);
			model.insertNodeInto(node1, treeNode, insertIndex++);
			DefaultMutableTreeNode subNode = new DefaultMutableTreeNode("正在连接服务器...");
			model.insertNodeInto(subNode, node1, 0);
		}

		for (String string : dirEmptyStrings) {
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(string);
			model.insertNodeInto(node1, treeNode, insertIndex++);
		}
		int childCount = treeNode.getChildCount();
		for (int i = insertIndex; i < childCount; i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) treeNode.getChildAt(insertIndex);
			model.removeNodeFromParent(child);
		}
	}
	private void updateFileWidget(DirStructure dirStructure){
		String[] fileStrings = dirStructure.getFiles();
		fileList.setListData(fileStrings);
	}
	private void processFileStructure(FileStructure fileStructure){
		String abstractPath = fileStructure.getPath();
		String fileName = abstractPath.substring(abstractPath.lastIndexOf("\\") + 1);
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if(fileChooser.showOpenDialog(fileList) == JFileChooser.APPROVE_OPTION){
			String dir = fileChooser.getSelectedFile().getPath();
			if(!dir.endsWith("\\")){
				dir = dir + "\\";
			}
			String realPath = dir + fileName;
			File file = new File(realPath);

			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					System.out.println("文件创建失败!");
					e.printStackTrace();
					return;
				}
			}
			FileOutputStream fileOutputStream = null;

			try {
				fileOutputStream = new FileOutputStream(realPath);
				fileOutputStream.write(fileStructure.getData());
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void processWarningMessage(String message, Component parentComponment){
		JOptionPane.showMessageDialog(parentComponment, message, "信息", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void initializationBroadcastSend(AlphaMessageOutputStream arg0, Message arg1) {
		// TODO Auto-generated method stub

	}


	public void setOperation(String Operation){
		this.operation = Operation;
	}

	public void setCurrentNode(DefaultMutableTreeNode node){
		currentNode = node;
	}
}

/**
 * 渲染节点方法，请不要更改该类的任何东西
 */
class FileTreeCellRenderer extends DefaultTreeCellRenderer{
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
												  boolean sel,
												  boolean expanded,
												  boolean leaf, int row,
												  boolean hasFocus) {
		JLabel label= (JLabel) super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocus);

		if(label.getText().equals("正在连接服务器...")) {
			label.setIcon(null);
		}

		label.setOpaque(false);
		return label;

	}

}

/**
 * 事件响应
 * 留接口的方法
 * 所有接口需要以Event_开头
 * 方法签名使用下列规则命名：
 * 		Event_ + 组件类型（去掉J） + 组件名称（参考FileGUI类中的字段值） + 事件类型
 * 		如：目录树组件展开事件：目录树组件类型为： JTree   组件名称为：dirTree   事件类型为：ExpandListener
 * 		那么方法就可以写成：Event_treeDirTreeExpand()
 * 		可以适当简写，如果怕误会的话在方法签名前面写上注释注明该方法对应哪一个组件的哪一个事件（XXXXListener的类）就好！
 * 回调过程：
 * 		最简单的回调这样写：
 * 		deleteDir.addActionListener(e ->{
 * 				// event为WidgetEvent类的对象，deleteDir为组件
 * 				// 直接调用WidgetEvent类中的Event_DeleteDirMenuitemClick()方法
 * 				event.Event_DeleteDirMenuitemClick(client, getDirTreeSelectedPath(selectedDefaultMutableTreeNode), clientAlphaClientEvent, e);
 *      })
 * 回调参数确定：
 * 固定需要XXXXEvent e，来自于事件响应方法本身，
 * 如：public void Event_DirTreeSelected(TreeSelectionEvent event)
 *
 * 如果需要发送一个请求到服务端，或者回传数据到服务端，请加上AlphaClient client,不需要就不用写这个参数！
 * 如：public void Event_DirTreeSelected(AlphaClient client,TreeSelectionEvent event)
 *
 * 理论上回调参数除了上面两点之外可以随便加
 *
 * 不知道我说什么的，就不要写参数！如：Event_XXXXXXXXX(),然后在上面写的调用过程中不传任何参数直接调用方法就好！
 *
 *
 */
class WidgetEvent{
	public void Event_DirTreeSelected(String selectedPath, AlphaClient client, TreeSelectionEvent event) {
		Message msgMessage = new Message(client.getHostMessage());
		DirStructure dirStructure = new DirStructure();
		dirStructure.setCurrentPath(selectedPath);
		msgMessage.writeObject(dirStructure);
		client.sendMessage(msgMessage);
	}
	public void Event_TreeExpanding(String selectedPath, AlphaClient client, TreeExpansionEvent event) {
		Message msgMessage = new Message(client.getHostMessage());
		DirStructure dirStructure = new DirStructure();
		dirStructure.setCurrentPath(selectedPath);
		msgMessage.writeObject(dirStructure);
		client.sendMessage(msgMessage);
	}
	public void Event_DirMenuItemEnableToSelected(MenuElement[] items, PopupMenuEvent e){
		for (MenuElement item:items
		) {
			JMenuItem item1 = (JMenuItem) item;
			item1.setEnabled(true);
		}


	}
	public void Event_DirMenuItemDisable(MenuElement[] items, PopupMenuEvent e){
		for (MenuElement item:items
		) {
			JMenuItem item1 = (JMenuItem) item;
			item1.setEnabled(false);
		}
	}
	public void Event_NewDirMenuitemClick(AlphaClient client, String selectedPath, String newDir, AlphaClientEvent clientAlphaClientEvent, ActionEvent e){
		Message msgMessage = new Message(client.getHostMessage());
		String fullPath;
		if(selectedPath.endsWith("\\")){
			fullPath = selectedPath + newDir;
		}else{
			fullPath = selectedPath + "\\" + newDir;
		}
		clientAlphaClientEvent.setOperation(e.getActionCommand());
		DirStructure dirStructure = new DirStructure();
		dirStructure.setCurrentPath(fullPath, DirStructure.Operation.newDir);
		msgMessage.writeObject(dirStructure);
		client.sendMessage(msgMessage);
	}
	public void Event_DeleteDirMenuitemClick(AlphaClient client, String selectedPath, AlphaClientEvent clientAlphaClientEvent, ActionEvent e){
		Message msgMessage = new Message(client.getHostMessage());
		clientAlphaClientEvent.setOperation(e.getActionCommand());
		DirStructure dirStructure = new DirStructure();
		dirStructure.setCurrentPath(selectedPath, DirStructure.Operation.deleteDir);
		msgMessage.writeObject(dirStructure);
		client.sendMessage(msgMessage);

	}

	public void Event_FileMenuItemEnableToSelected(MenuElement[] items, PopupMenuEvent e){

	}
	public void Event_FileMenuItemDisable(MenuElement[] items, PopupMenuEvent e){

	}

	public void Event_FileUploadItemClick(AlphaClient client, String abstractFilePath, File realFilePath) {
		Message msg = new Message(client.getHostMessage());
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(realFilePath);
			FileStructure fileStructure = new FileStructure(abstractFilePath, fileInputStream.readAllBytes());
			fileInputStream.close();
			msg.writeObject(fileStructure);
			client.sendMessage(msg);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// 显示文件未找到对话框!!
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Event_FileDownloadItemClick(AlphaClient client, String abstractFilePath) {
		Message msg = new Message(client.getHostMessage());
		FileStructure fileStructure = new FileStructure(abstractFilePath);
		msg.writeObject(fileStructure);
		client.sendMessage(msg);
	}
	public void Event_FileDeleteItemClick(AlphaClient client, String abstractFilePath) {
		Message msg = new Message(client.getHostMessage());
		FileStructure fileStructure = new FileStructure(abstractFilePath);
		fileStructure.setDeleteFileOper();
		msg.writeObject(fileStructure);
		client.sendMessage(msg);
	}
}