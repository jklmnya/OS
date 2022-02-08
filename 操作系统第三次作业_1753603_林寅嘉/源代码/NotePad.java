package assignment3;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class NotePad {

	protected JFrame frame;
	
	private FCB fcb;
	private JTextArea textArea;
	private int[] Fat;
	
	public NotePad(FCB fcb, int[] Fat) {
		this.fcb = fcb;
		this.Fat = Fat;
		initialize();				
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 582, 604);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle(fcb.getFileName());
		
		//为窗口关闭时添加监听器事件
		frame.addWindowListener(new WindowAdapter() {			
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				long size = (textArea.getText().toCharArray().length)*2;//所需的空间
				
				//判断是否有足够的空间
				if(!FatOP.startFat(Fat, size, fcb.getStartFat())) {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {														
								AlertWindow aw = new AlertWindow(4);
								aw.frame.setVisible(true);											
							} catch (Exception e) {}
						}
					});			
				}
				
				File file = new File(fcb.getLocation());
				if(!file.exists()) return;
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(file));
					bw.write("");
					String info = null;
					textArea.append("\0"); 
	                for (int i = 0; i < textArea.getLineCount(); i++) { 
	                    try { 
	                        info = textArea.getText(textArea.getLineStartOffset(i), textArea.getLineEndOffset(i)- textArea.getLineStartOffset(i) - 1);
	                        bw.write(info);
	                        bw.newLine();
	                    } catch (BadLocationException ex) { 
	                        ex.printStackTrace(); 
	                    }
	                }	
	                fcb.setSize(size);//更改fcb的大小
					bw.flush();
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}			
		});
		
		textArea = new JTextArea();
		
		//滚动条
		JScrollPane js = new JScrollPane(textArea);
		js.setBounds(0, 0, 568, 567);
		js.setVerticalScrollBarPolicy( 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		frame.getContentPane().add(js);
		
		try {
			writeTxt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//读文件
	public void writeTxt() throws IOException {
		File file = new File(fcb.getLocation());
		if(!file.exists()) return;
		BufferedReader br = new BufferedReader(new FileReader(file));
		String info = null;
		while((info = br.readLine()) != null) {
			textArea.append(info+"\n");
		}
		br.close();
	}	
}