package assignment3;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

/**
 * 文件属性面板
 * @author linyi
 *
 */
public class FileAttrbutes {

	protected JFrame frame;

	private FCB fcb;
	private JTextField textField;
	
	public FileAttrbutes(FCB fcb) {
		this.fcb = fcb;
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 452);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("文件属性");
		
		textField = new JTextField();
		textField.setBounds(136, 62, 220, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setEditable(false);
		textField.setText(fcb.getFileName());
		
		JLabel type = new JLabel("\u7C7B\u578B\uFF1A");
		type.setBounds(38, 115, 58, 15);
		frame.getContentPane().add(type);
		
		JLabel type_value = new JLabel(fcb.getFileType());
		type_value.setBounds(136, 115, 181, 15);
		frame.getContentPane().add(type_value);
		
		JLabel path = new JLabel("\u4F4D\u7F6E\uFF1A");
		path.setBounds(38, 171, 58, 15);
		frame.getContentPane().add(path);
		
		JLabel path_value = new JLabel(fcb.getLocation());
		path_value.setBounds(136, 171, 252, 15);
		frame.getContentPane().add(path_value);
		
		JLabel size = new JLabel("\u5360\u7528\u7A7A\u95F4\uFF1A");
		size.setBounds(38, 232, 78, 15);
		frame.getContentPane().add(size);
		
		if(fcb.getFileType().equals("txt")) {
			//计算txt文件的大小
			long Filesize = fcb.getSize();
			String Fileinfo = null;
			if(Filesize<1024) {
				Fileinfo = String.valueOf(Filesize)+"B";
			} else if(Filesize<1048576){
				Fileinfo = String.valueOf((double)(Math.round(Filesize/1024*100))/100)+"KB";
			} else {
				Fileinfo = String.valueOf((double)(Math.round(Filesize/1048576*100))/100)+"MB";
			}
			JLabel size_value = new JLabel(Fileinfo);
			frame.getContentPane().add(size_value);
			size_value.setBounds(174, 232, 125, 15);
		} else {
			if(fcb.getSubFils() == null) {
				JLabel size_value = new JLabel("0个文件");
				frame.getContentPane().add(size_value);
				size_value.setBounds(174, 232, 125, 15);
			} else {
				int number = fcb.getSubFils().size();
				JLabel size_value = new JLabel(number+"个文件");
				frame.getContentPane().add(size_value);
				size_value.setBounds(174, 232, 125, 15);
			}
		}		
				
		JLabel time = new JLabel("\u521B\u5EFA\u65F6\u95F4\uFF1A");
		time.setBounds(38, 290, 93, 15);
		frame.getContentPane().add(time);
		
		JLabel time_value = new JLabel(fcb.getFoundTime());
		time_value.setBounds(136, 290, 220, 15);
		frame.getContentPane().add(time_value);
		
		JButton button = new JButton("\u786E\u5B9A");
		button.setBounds(298, 370, 73, 23);
		frame.getContentPane().add(button);
	}
}