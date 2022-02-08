package assignment2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainWindow2 {

	private JFrame frame;
	
	//�ж�Ϊ�����㷨
	private int[] isFirst = new int[1];

	private JTextArea textArea;
	
	private Rectangle rect = new Rectangle();
	private Memory memory;
	
	private JComboBox<String> comboBox;
	private JComboBox<String> comboBox_1;
	private JComboBox<Integer> comboBox_2;
	private JComboBox<Integer> comboBox_3;
	private JComboBox<Integer> comboBox_4;
	private JButton start;
	private JButton b_reset;
	
	private boolean isFree;	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow2 window = new MainWindow2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainWindow2() {
		initialize();
		isFirst[0] = 1;
		memory = new Memory(isFirst, rect);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\u6A21\u62DF\u52A8\u6001\u5206\u533A\u5206\u914D");
		frame.setBounds(100, 100, 1043, 570);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		rect.setBounds(323, 427, 640, 20);
		frame.getContentPane().add(rect);
		rect.setBackground(Color.WHITE);
				
		//������
		JScrollPane js = new JScrollPane();
		js.setBounds(323, 124, 645, 216);
		js.setVerticalScrollBarPolicy( 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		frame.getContentPane().add(js);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("����",Font.BOLD, 15));
		js.setViewportView(textArea);
		textArea.setEditable(false);
						
		addButton();
		addJBox();						
	}
	
	private void addButton() {
		//���ð�ť
		b_reset = new JButton("\u91CD\u7F6E");
		b_reset.setBounds(35, 245, 97, 23);
		b_reset.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		frame.getContentPane().add(b_reset);
		
		//�˳���ť
		JButton quit = new JButton("\u9000\u51FA");
		quit.setBounds(35, 363, 97, 23);
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		frame.getContentPane().add(quit);
		
		//��ʼ��ť
		start = new JButton("\u5F00\u59CB\u7533\u8BF7");
		start.setBounds(837, 27, 97, 23);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringBuffer info = new StringBuffer();
				int memorySize = 0;
				int taskNnmber = 0;
				//�ͷ���Դ
				if(isFree) {
					if(comboBox_4.getSelectedItem() != null) {
						start.setEnabled(true);
					} else {
						start.setEnabled(false);
						return;
					}
					taskNnmber = (int) comboBox_4.getSelectedItem();
					info.append("����").append(taskNnmber).append("�����ͷ���Դ\n");
				//������Դ
				} else {
					if(comboBox_2.getSelectedItem() != null) {
						start.setEnabled(true);
					} else {
						start.setEnabled(false);
						return;
					}
					taskNnmber = (int) comboBox_2.getSelectedItem();
					memorySize = (int) comboBox_3.getSelectedItem();					
					info.append("����").append(taskNnmber).append("��������").append(memorySize).append("KB��Դ\n");
				}
							
				textArea.append(info.toString());
				textArea.setCaretPosition(textArea.getDocument().getLength());
				
				boolean success = memory.handle(memorySize, taskNnmber, isFree);
				info.delete(0, info.length());
				if(success) {
					if(isFree) {
						info.append("����").append(taskNnmber).append("�ͷ���Դ�ɹ�\n\n");
						comboBox_4.removeItem(taskNnmber);
						comboBox_2.addItem(taskNnmber);						
						rect.repaint();
					} else {
						info.append("����").append(taskNnmber).append("������Դ�ɹ�\n\n");
						comboBox_2.removeItem(taskNnmber);
						comboBox_4.addItem(taskNnmber);
						rect.repaint();
					}					
				} else {
					if(isFree) {
						info.append("����").append(taskNnmber).append("�ͷ���Դʧ��\n\n");
					} else {
						info.append("����").append(taskNnmber).append("������Դʧ��, �޿����ڴ�\n\n");
					}
				}
				textArea.append(info.toString());
				textArea.setCaretPosition(textArea.getDocument().getLength());
			}
		});
		frame.getContentPane().add(start);
	}
	
	private void addJBox() {
		
		JLabel select = new JLabel("\u8BF7\u9009\u62E9\u9002\u5E94\u7B97\u6CD5\uFF1A");
		select.setBounds(35, 121, 103, 28);
		frame.getContentPane().add(select);
		
		JLabel lblNewLabel = new JLabel("\u7533\u8BF7/\u91CA\u653E");
		lblNewLabel.setBounds(323, 31, 58, 15);
		frame.getContentPane().add(lblNewLabel);
		
		//�㷨ѡ�����
		comboBox = new JComboBox<>();
		comboBox.setBounds(132, 125, 113, 23);
		comboBox.addItem("�״���Ӧ�㷨");
		comboBox.addItem("�����Ӧ�㷨");
		comboBox.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) isFirst[0] *= -1;
			}
		});
		frame.getContentPane().add(comboBox);
		
		//������������
		comboBox_2 = new JComboBox<>();
		comboBox_2.setBounds(547, 27, 58, 23);
		for(int i = 1;i<=10;i++) comboBox_2.addItem(i);
		frame.getContentPane().add(comboBox_2);
		
		//���ͷ�������
		comboBox_4 = new JComboBox<>();
		comboBox_4.setBounds(547, 27, 58, 23);
		comboBox_4.setVisible(false);
		frame.getContentPane().add(comboBox_4);
				
		JLabel label = new JLabel("\u4EFB\u52A1\u7F16\u53F7");
		label.setBounds(489, 31, 58, 15);
		frame.getContentPane().add(label);
		
		//�����ڴ�
		comboBox_3 = new JComboBox<>();
		comboBox_3.setBounds(730, 27, 58, 23);
		for(int i = 1;i<=640;i++) comboBox_3.addItem(i);
		frame.getContentPane().add(comboBox_3);
		
		JLabel lblNewLabel_1 = new JLabel("\u7533\u8BF7\u5185\u5B58(KB)");
		lblNewLabel_1.setBounds(646, 31, 85, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		//��������ͷ�
		comboBox_1 = new JComboBox<>();
		comboBox_1.setBounds(391, 27, 58, 23);
		comboBox_1.addItem("����");
		comboBox_1.addItem("�ͷ�");
		comboBox_1.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(comboBox_1.getSelectedItem() == "����") {
					comboBox_3.setVisible(true);
					comboBox_4.setVisible(false);
					comboBox_2.setVisible(true);
					start.setText("��ʼ����");
					if(comboBox_2.getSelectedItem() == null) {
						start.setEnabled(false);
					} else {
						start.setEnabled(true);
					}
					isFree = false;
				} else {
					comboBox_3.setVisible(false);
					comboBox_4.setVisible(true);
					comboBox_2.setVisible(false);
					start.setText("��ʼ�ͷ�");
					if(comboBox_4.getSelectedItem() == null) {
						start.setEnabled(false);
					} else {
						start.setEnabled(true);
					}
					isFree = true;
				}
			}
		});
		frame.getContentPane().add(comboBox_1);
				
		JLabel lblNewLabel_2 = new JLabel("\u5185\u5B58\u4F7F\u7528\u60C5\u51B5\uFF08\u7070\u8272\u4EE3\u8868\u5DF2\u88AB\u4F7F\u7528\uFF0C\u767D\u8272\u4EE3\u8868\u672A\u88AB\u4F7F\u7528,\u603B\u5185\u5B58\u4E3A640KB\uFF09");
		lblNewLabel_2.setBounds(323, 384, 455, 15);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("\u7533\u8BF7\u91CA\u653E\u60C5\u51B5");
		lblNewLabel_3.setBounds(323, 93, 99, 15);
		frame.getContentPane().add(lblNewLabel_3);
	}
	
	private void reset() {		
		comboBox_4.removeAllItems();
		comboBox_2.removeAllItems();
		for(int i = 1;i<=10;i++) comboBox_2.addItem(i);
		
		rect.clear();
		rect.repaint();
		
		textArea.setText("");
	}
}
