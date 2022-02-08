package assignment3;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;

public class MainWindow {

	private JFrame frame;

	private JPopupMenu blankmenu;
	private JPopupMenu fileopmenu;
	
	private String currentPath;
	
	private JTextArea Path;
	
	private JList<String> fileList;
	
	private JTree fileTree;
	
	private int[] Fat = new int[16];//Fat表
	
	private FCB currentFab;//当前目录
	private FCB Root;//根目录
	
	private DefaultListModel<String> listModel;
	
	private JButton FatherPath;//返回上级目录按钮
	
	private DefaultMutableTreeNode pr_rootNode;
	private DefaultMutableTreeNode rootNode;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {}
			}
		});
	}

	public MainWindow() {
		initialize();

		init_Fcb();
		init_Fat();
		resetPathText();
		init_JTree();
		RightMouseEvent();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\u6587\u4EF6\u7BA1\u7406\u7CFB\u7EDF");
		frame.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().setBackground(UIManager.getColor("Button.background"));
		frame.setBounds(100, 100, 599, 658);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Path = new JTextArea();
		Path.setBounds(208, 35, 347, 24);
		frame.getContentPane().add(Path);
		Path.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		Path.setEditable(false);
		
		JLabel label = new JLabel("\u540D\u79F0");
		label.setBounds(338, 94, 58, 15);
		frame.getContentPane().add(label);
		
		JLabel label_2 = new JLabel("\u6587\u4EF6\u76EE\u5F55");
		label_2.setBounds(45, 94, 58, 15);
		frame.getContentPane().add(label_2);
		
		//为窗口关闭时添加监听器事件
		frame.addWindowListener(new WindowAdapter() {
			
			 @Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				try {
					FcbOP.storeRoot(Root);
					FatOP.writeFat(Fat);
					System.out.println(Arrays.toString(Fat));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}			
		});
	}
	
	//在JList区域添加鼠标右键监听事件
	private void RightMouseEvent() {
		init_JList();
		init_JPopupMenu();
				
		fileList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (fileList.locationToIndex(e.getPoint()) == -1 && !e.isShiftDown()
                        && !isMenuShortcutKeyDown(e)) {
					fileList.clearSelection();
                }				
				String subFileName = fileList.getSelectedValue();
				int subTreeIndex = fileList.getSelectedIndex();
				//判断鼠标点击的位置
				if(subFileName != null) {
					//处理鼠标左键双击事件
					if(e.getClickCount() == 2) {
						FCB sub = FcbOP.findFcb(currentFab, subFileName);
						if(sub != null) {
							//如果是txt类型，则直接打开文件
							if(sub.getFileType().equals("txt")) {
								EventQueue.invokeLater(new Runnable() {
									public void run() {
										try {														
											NotePad notePad = new NotePad(sub, Fat);
											notePad.frame.setVisible(true);											
										} catch (Exception e) {}
									}
								});		
							} else {
								//文件夹类型，显示下级目录
								resetJList(sub.getSubFils());
								currentFab = sub;//更新当前节点所在的位置
								resetPathText();
								FatherPath.setEnabled(true);
								
								pr_rootNode = (DefaultMutableTreeNode) pr_rootNode.getChildAt(subTreeIndex);//更新TreeNode节点	
							}
						}
					}
					if(e.getButton() == MouseEvent.BUTTON3) {
						fileopmenu.show(fileList, e.getX(), e.getY());
					} 					
				} else {
					if(e.getButton() == MouseEvent.BUTTON3) {
						blankmenu.show(fileList, e.getX(), e.getY());
					}
				}
			}
			
			@SuppressWarnings("deprecation")
			private boolean isMenuShortcutKeyDown(InputEvent event) {
                return (event.getModifiers() & Toolkit.getDefaultToolkit()
                        .getMenuShortcutKeyMask()) != 0;
            }			
		});
	}
	
	//初始化JList
	private void init_JList() {
		listModel = new DefaultListModel<>();
        listModel.addElement(currentFab.getSubFils().get(0).getFileName());
        
		fileList = new JList<>(listModel) {
			private static final long serialVersionUID = 1L;

			@Override
             public int locationToIndex(Point location) {
                 int index = super.locationToIndex(location);
                 if (index != -1 && !getCellBounds(index, index).contains(location)) {
                     return -1;
                 }
                 else {
                     return index;
                 }
             }
         };
		
		fileList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				
		JScrollPane listScrollPane = new JScrollPane(fileList);
		listScrollPane.setBounds(208, 135, 347, 462);
		listScrollPane.setBorder(null);
		listScrollPane.setVerticalScrollBarPolicy( 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		frame.getContentPane().add(listScrollPane);		
		
		//返回上级目录
		FatherPath = new JButton("\u8FD4\u56DE");
		FatherPath.setBackground(Color.WHITE);
		FatherPath.setBounds(99, 35, 97, 23);
		FatherPath.setEnabled(false);
		frame.getContentPane().add(FatherPath);
		
		FatherPath.addActionListener(new ActionListener() {	
			//返回上级目录操作
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentFab.getFatherFile() == null) {
					return;
				}
				currentFab = currentFab.getFatherFile();
				if(currentFab.equals(Root)) FatherPath.setEnabled(false);
				resetJList(currentFab.getSubFils());
				resetPathText();
				
				pr_rootNode = (DefaultMutableTreeNode) pr_rootNode.getParent();//更新TreeNode节点
			}
		});
	}
	
	//更新JList
	private void resetJList(ArrayList<FCB> subFileNames) {	
		listModel.removeAllElements();
		if(subFileNames != null) {
			for (FCB fcb : subFileNames) {
				listModel.addElement(fcb.getFileName());
			}
		}
	}
	
	//初始化JTree
	private void init_JTree() {			
		FCB fcb = currentFab.getSubFils().get(0);
		pr_rootNode = new DefaultMutableTreeNode("Pr_Root");//根节点的父亲节点
		rootNode = new DefaultMutableTreeNode(fcb.getFileName());//初始化根节点
		pr_rootNode.add(rootNode);
		detailsJTree(rootNode, fcb);
		
		fileTree = new JTree(rootNode);//使用根节点创建JTree
		
		JScrollPane listScrollPane = new JScrollPane(fileTree);
		listScrollPane.setBounds(25, 135, 159, 462);
		listScrollPane.setBorder(null);
		listScrollPane.setVerticalScrollBarPolicy( 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		frame.getContentPane().add(listScrollPane);	
	}
	
	//递归详细初始化JTree
	private void detailsJTree(DefaultMutableTreeNode rootNode, FCB Root) {
		if(Root.getSubFils() != null) {
			for (FCB sub: Root.getSubFils()) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(sub.getFileName());
				rootNode.add(node);
				detailsJTree(node, sub);
			}
		}
	}
	
	//更新当前路径信息
	private void resetPathText() {
		currentPath = currentFab.getLocation();
		Path.setText(currentPath);
	}
	
	//初始化FCB
	private void init_Fcb() {
		if((Root = FileOP.makeRoot(new File("Root").getAbsolutePath())) == null) {
			try {
				Root = FcbOP.readRoot();//初始化Root
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		currentFab = Root;
	}
	
	//初始化Fat表
	private void init_Fat() {
		try {						
			FatOP.readFat(Fat);//初始化Fat
			System.out.println(Arrays.toString(Fat));
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}	
	}
	
	//初始化JPopupMenu
	private void init_JPopupMenu() {
		//空白处点击显示的JPopupMenu
		blankmenu = new JPopupMenu();
		JMenu newFile = new JMenu("新建");
		
		JMenuItem copy = new JMenuItem("复制");
		
		JMenuItem txt = new JMenuItem("文本文档");
		JMenuItem folder = new JMenuItem("文件夹");
		
		newFile.add(txt);
		newFile.add(folder);
		
		//创建txt文件
		txt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String fileName = FileOP.makeFile(currentPath, currentFab, Fat);
					if(fileName != null) {
						listModel.addElement(fileName);
						pr_rootNode.add(new DefaultMutableTreeNode(fileName));
						fileTree.updateUI();
					}
					else {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {														
									AlertWindow aw = new AlertWindow(0);
									aw.frame.setVisible(true);
								} catch (Exception e) {}
							}
						});		
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}				
		});
		
		//创建文件夹
		folder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String folderName = FileOP.makeDir(currentPath, currentFab);
				if(folderName != null) {
					listModel.addElement(folderName);
					pr_rootNode.add(new DefaultMutableTreeNode(folderName));
					fileTree.updateUI();
				}
				else {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {														
								AlertWindow aw = new AlertWindow(1);
								aw.frame.setVisible(true);
							} catch (Exception e) {}
						}
					});		
				}			
			}				
		});
		
		blankmenu.add(copy);
		blankmenu.add(newFile);
		
		//文件处点击显示的JPopupMenu
		fileopmenu = new JPopupMenu();
		JMenuItem open = new JMenuItem("打开");
		JMenuItem attribute = new JMenuItem("属性");
		JMenuItem delete = new JMenuItem("删除");
		JMenuItem format = new JMenuItem("格式化");
		
		fileopmenu.add(open);
		fileopmenu.add(attribute);
		fileopmenu.add(delete);
		fileopmenu.add(format);
		
		//删除文件
		delete.addActionListener(new ActionListener() {			
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				if((e.getModifiers()&InputEvent.BUTTON1_MASK)!=0) {
					FCB subFile = null;
					String subFcbName = fileList.getSelectedValue();
					int subFileIndex = fileList.getSelectedIndex();
					if((subFile = FcbOP.findFcb(currentFab, subFcbName)) != null) {
						//删除文件，更新Fat，Fcb
						if(FileOP.deleteFile(currentPath+"\\"+subFcbName, currentFab, subFile, Fat)) {							
							listModel.removeElement(subFcbName);//更新JList
							pr_rootNode.remove(subFileIndex);//更新JTree
							fileTree.updateUI();
						} else {
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										AlertWindow aw = new AlertWindow(3);
										aw.frame.setVisible(true);
									} catch (Exception e) {}
								}
							});
						}
						
					}
				}				
			}
		});
		
		//打开属性面板
		attribute.addActionListener(new ActionListener() {			
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				if((e.getModifiers()&InputEvent.BUTTON1_MASK)!=0) {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								FCB subFile = null;
								String subFcbName = fileList.getSelectedValue();
								if((subFile = FcbOP.findFcb(currentFab, subFcbName)) != null) {
									FileAttrbutes attrWindow = new FileAttrbutes(subFile);
									attrWindow.frame.setVisible(true);	
								}
							} catch (Exception e) {}
						}
					});
				}				
			}
		});
		
		//格式化
		format.addActionListener(new ActionListener() {	
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				if((e.getModifiers()&InputEvent.BUTTON1_MASK)!=0) {
					FCB subFile = null;
					String subFcbName = fileList.getSelectedValue();
					if((subFile = FcbOP.findFcb(currentFab, subFcbName)) != null) {
						if(!FileOP.cleanFile(currentPath+"\\"+subFcbName, Fat, subFile)) {
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										AlertWindow aw = new AlertWindow(2);
										aw.frame.setVisible(true);	
									} catch (Exception e) {}									 
								}
							});
						}
					}
				}
			}
		});
		
		//打开
		open.addActionListener(new ActionListener() {	
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				if((e.getModifiers()&InputEvent.BUTTON1_MASK)!=0) {
					String subFcbName = fileList.getSelectedValue();
					int subTreeIndex = fileList.getSelectedIndex();
					final FCB subFile = FcbOP.findFcb(currentFab, subFcbName);					
					if(subFile != null) {
						if(subFile.getFileType().equals("txt")) {
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {														
										NotePad notePad = new NotePad(subFile, Fat);
										notePad.frame.setVisible(true);											
									} catch (Exception e) {}
								}
							});								
						} else {
							//文件夹类型，显示下级目录
							resetJList(subFile.getSubFils());
							currentFab = subFile;//更新当前节点所在的位置
							resetPathText();
							FatherPath.setEnabled(true);
							
							pr_rootNode = (DefaultMutableTreeNode) pr_rootNode.getChildAt(subTreeIndex);//更新TreeNode节点	
						}
					}
				}
			}
		});
	}
}