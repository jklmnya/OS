package assignment1;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	private static final int MAX_CS_NUMBER = 5;//生产者线程的最大值
	private static final int MAX_S_NUMBER = 9;//仓库数目的最大值
	private int cur_p_number = 3;//当前的生产者数量
	private int cur_c_number = 3;//当前的消费者数量
	private int cur_s_number = 5;//当前的可用仓库数量                                                                                                                                                     
	
	private boolean[] stop = {false};//是否暂停生产，消费线程的标志
	
	//标签组件
	private JLabel producer_number;
	private JLabel consumer_number;
	private JLabel store_number;
	
	//进度条组件数组
	private JProgressBar[] warehouse = new JProgressBar[10];

	//生产消费信息文本框
	private JTextArea textArea;
	
	//存储每个缓存区产品数量的数组
	private JLabel[] ware_count = new JLabel[10];
	
	//按钮组件
	private JButton add_producer;
	private JButton d_producer;
	private JButton add_consumer;
	private JButton d_consumer;
	private JButton add_store;
	private JButton d_store;
	private JButton start_button;
	private JButton stop_button;
	private JButton reinit_button;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//初始化窗口
	public MainWindow() {
		setTitle("\u751F\u4EA7\u8005\u6D88\u8D39\u8005\u95EE\u9898");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 799);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//生产消费信息
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		//滚动条
		JScrollPane js = new JScrollPane(textArea);
		js.setBounds(773, 117, 295, 604);
		js.setVerticalScrollBarPolicy( 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		contentPane.add(js);
		
		add_JButton();		
		add_JProgressBar();		
		add_JLabel();						
	}
	
	//初始化标签组件
	public void add_JLabel() {
		
		JLabel info = new JLabel("\u751F\u4EA7\u6D88\u8D39\u4FE1\u606F");
		info.setBounds(881, 64, 102, 32);
		contentPane.add(info);
		
		producer_number = new JLabel("\u5F53\u524D\u751F\u4EA7\u8005\u6570\u91CF\uFF1A3");
		producer_number.setBounds(179, 30, 130, 32);
		contentPane.add(producer_number);
				
		consumer_number = new JLabel("\u5F53\u524D\u6D88\u8D39\u8005\u6570\u91CF\uFF1A" + cur_c_number);
		consumer_number.setBounds(373, 30, 130, 32);
		contentPane.add(consumer_number);
		
		store_number = new JLabel("\u5F53\u524D\u53EF\u7528\u4ED3\u5E93\u6570\u91CF\uFF1A" + cur_s_number);
		store_number.setBounds(562, 30, 142, 32);
		contentPane.add(store_number);
		
		JLabel s_1 = new JLabel("\u4ED3\u5E93\u4E00");
		s_1.setBounds(179, 193, 42, 15);
		contentPane.add(s_1);
		
		JLabel s_2 = new JLabel("\u4ED3\u5E93\u4E8C");
		s_2.setBounds(373, 193, 42, 15);
		contentPane.add(s_2);
		
		JLabel s_3 = new JLabel("\u4ED3\u5E93\u4E09");
		s_3.setBounds(561, 193, 42, 15);
		contentPane.add(s_3);
		
		JLabel s_4 = new JLabel("\u4ED3\u5E93\u56DB");
		s_4.setBounds(179, 347, 42, 15);
		contentPane.add(s_4);
		
		JLabel s_5 = new JLabel("\u4ED3\u5E93\u4E94");
		s_5.setBounds(373, 347, 42, 15);
		contentPane.add(s_5);
		
		JLabel s_6 = new JLabel("\u4ED3\u5E93\u516D");
		s_6.setBounds(561, 347, 42, 15);
		contentPane.add(s_6);
		
		JLabel s_7 = new JLabel("\u4ED3\u5E93\u4E03");
		s_7.setBounds(179, 498, 42, 15);
		contentPane.add(s_7);
		
		JLabel s_8 = new JLabel("\u4ED3\u5E93\u516B");
		s_8.setBounds(373, 498, 42, 15);
		contentPane.add(s_8);
		
		JLabel s_9 = new JLabel("\u4ED3\u5E93\u4E5D");
		s_9.setBounds(561, 498, 42, 15);
		contentPane.add(s_9);
				
		JLabel fix_p_number = new JLabel("\u4FEE\u6539\u751F\u4EA7\u8005\u6570\u91CF");
		fix_p_number.setBounds(179, 582, 102, 15);
		contentPane.add(fix_p_number);
		
		JLabel fix_c_number = new JLabel("\u4FEE\u6539\u6D88\u8D39\u8005\u6570\u91CF");
		fix_c_number.setBounds(179, 643, 102, 15);
		contentPane.add(fix_c_number);
		
		JLabel fix_s_number = new JLabel("\u4FEE\u6539\u53EF\u7528\u4ED3\u5E93\u6570\u91CF");
		fix_s_number.setBounds(179, 706, 102, 15);
		contentPane.add(fix_s_number);
		
		JLabel max_p = new JLabel("(\u6700\u5927\u4E0A\u9650\u4E3A5)");
		max_p.setBounds(179, 64, 130, 32);
		contentPane.add(max_p);
		
		JLabel max_c = new JLabel("(\u6700\u5927\u4E0A\u9650\u4E3A5)");
		max_c.setBounds(373, 64, 130, 32);
		contentPane.add(max_c);
		
		JLabel max_s = new JLabel("(\u6700\u5927\u4E0A\u9650\u4E3A9)");
		max_s.setBounds(561, 64, 130, 32);
		contentPane.add(max_s);
		
		JLabel s1_number = new JLabel("\u5F53\u524D\u8D27\u7269\u6570\u91CF\uFF1A50");
		s1_number.setBounds(179, 225, 130, 15);
		contentPane.add(s1_number);
		ware_count[1] = s1_number;
		
		JLabel s2_number = new JLabel("\u5F53\u524D\u8D27\u7269\u6570\u91CF\uFF1A50");
		s2_number.setBounds(373, 225, 122, 15);
		contentPane.add(s2_number);
		ware_count[2] = s2_number;
		
		JLabel s3_number = new JLabel("\u5F53\u524D\u8D27\u7269\u6570\u91CF\uFF1A50");
		s3_number.setBounds(561, 225, 122, 15);
		contentPane.add(s3_number);
		ware_count[3] = s3_number;
		
		JLabel s4_number = new JLabel("\u5F53\u524D\u8D27\u7269\u6570\u91CF\uFF1A50");
		s4_number.setBounds(179, 382, 122, 15);
		contentPane.add(s4_number);
		ware_count[4] = s4_number;
		
		JLabel s5_number = new JLabel("\u5F53\u524D\u8D27\u7269\u6570\u91CF\uFF1A50");
		s5_number.setBounds(373, 382, 122, 15);
		contentPane.add(s5_number);
		ware_count[5] = s5_number;
		
		JLabel s6_number = new JLabel("\u672A\u542F\u7528");
		s6_number.setBounds(561, 382, 122, 15);
		contentPane.add(s6_number);
		ware_count[6] = s6_number;
		
		JLabel s7_number = new JLabel("\u672A\u542F\u7528");
		s7_number.setBounds(179, 532, 122, 15);
		contentPane.add(s7_number);
		ware_count[7] = s7_number;
		
		JLabel s8_number = new JLabel("\u672A\u542F\u7528");
		s8_number.setBounds(373, 532, 122, 15);
		contentPane.add(s8_number);
		ware_count[8] = s8_number;
		
		JLabel s9_number = new JLabel("\u672A\u542F\u7528");
		s9_number.setBounds(561, 532, 122, 15);
		contentPane.add(s9_number);
		ware_count[9] = s9_number;
	}
	
	//初始化进度条组件
	public void add_JProgressBar() {
		
		JProgressBar store_1 = new JProgressBar();
		store_1.setValue(50);
		store_1.setBounds(179, 117, 102, 66);
		contentPane.add(store_1);
		warehouse[1] = store_1;
		
		JProgressBar store_2 = new JProgressBar();
		store_2.setValue(50);
		store_2.setBounds(373, 117, 102, 66);
		contentPane.add(store_2);
		warehouse[2] = store_2;
		
		JProgressBar store_3 = new JProgressBar();
		store_3.setValue(50);
		store_3.setBounds(561, 117, 102, 66);
		contentPane.add(store_3);
		warehouse[3] = store_3;
		
		JProgressBar store_4 = new JProgressBar();
		store_4.setValue(50);
		store_4.setBounds(179, 271, 102, 66);
		contentPane.add(store_4);
		warehouse[4] = store_4;
		
		JProgressBar store_5 = new JProgressBar();
		store_5.setValue(50);
		store_5.setBounds(373, 271, 102, 66);
		contentPane.add(store_5);
		warehouse[5] = store_5;
		
		JProgressBar store_6 = new JProgressBar();
		store_6.setBounds(561, 271, 102, 66);
		contentPane.add(store_6);
		warehouse[6] = store_6;
		
		JProgressBar store_7 = new JProgressBar();
		store_7.setBounds(179, 422, 102, 66);
		contentPane.add(store_7);
		warehouse[7] = store_7;
		
		JProgressBar store_8 = new JProgressBar();
		store_8.setBounds(373, 422, 102, 66);
		contentPane.add(store_8);
		warehouse[8] = store_8;
		
		JProgressBar store_9 = new JProgressBar();
		store_9.setBounds(561, 422, 102, 66);
		contentPane.add(store_9);
		warehouse[9] = store_9;
	}
	
	//初始化按钮组件
	public void add_JButton() {
		
		add_producer = new JButton("\u589E\u52A0\u751F\u4EA7\u8005");
		d_producer = new JButton("\u51CF\u5C11\u751F\u4EA7\u8005");
		add_consumer = new JButton("\u589E\u52A0\u6D88\u8D39\u8005");
		d_consumer = new JButton("\u51CF\u5C11\u6D88\u8D39\u8005");
		add_store = new JButton("\u589E\u52A0\u4ED3\u5E93\u6570\u91CF");
		d_store = new JButton("\u51CF\u5C11\u4ED3\u5E93\u6570\u91CF");
		start_button = new JButton("\u5F00\u59CB");
		
		//为按钮添加监听器事件
		add_producer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cur_p_number++;
				producer_number.setText("\u5F53\u524D\u751F\u4EA7\u8005\u6570\u91CF\uFF1A" + cur_p_number);
				if(cur_p_number == MAX_CS_NUMBER) {
					add_producer.setEnabled(false);
				}
				if(cur_p_number > 0) {
					d_producer.setEnabled(true);
				}
			}
		});
		add_producer.setBounds(373, 578, 122, 23);
		contentPane.add(add_producer);
				
		d_producer.setBounds(561, 578, 122, 23);
		d_producer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cur_p_number--;
				producer_number.setText("\u5F53\u524D\u751F\u4EA7\u8005\u6570\u91CF\uFF1A" + cur_p_number);
				if(cur_p_number == 0) {
					d_producer.setEnabled(false);
				}
				if(cur_p_number < MAX_CS_NUMBER) {
					add_producer.setEnabled(true);
				}
			}
		});
		contentPane.add(d_producer);
				
		add_consumer.setBounds(373, 639, 122, 23);
		add_consumer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cur_c_number++;
				consumer_number.setText("\u5F53\u524D\u6D88\u8D39\u8005\u6570\u91CF\uFF1A" + cur_c_number);
				if(cur_c_number == MAX_CS_NUMBER) {
					add_consumer.setEnabled(false);
				}
				if(cur_c_number > 0) {
					d_consumer.setEnabled(true);
				}
			}
		});
		contentPane.add(add_consumer);
				
		d_consumer.setBounds(561, 639, 122, 23);
		d_consumer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cur_c_number--;
				consumer_number.setText("\u5F53\u524D\u6D88\u8D39\u8005\u6570\u91CF\uFF1A" + cur_c_number);
				if(cur_c_number == 0) {
					d_consumer.setEnabled(false);
				}
				if(cur_c_number < MAX_CS_NUMBER) {
					add_consumer.setEnabled(true);
				}
			}
		});
		contentPane.add(d_consumer);
				
		add_store.setBounds(373, 698, 122, 23);
		add_store.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cur_s_number++;
				store_number.setText("\u5F53\u524D\u53EF\u7528\u4ED3\u5E93\u6570\u91CF\uFF1A" + cur_s_number);
				warehouse[cur_s_number].setValue(50);
				ware_count[cur_s_number].setText("当前货物数量50");
				if(cur_s_number == MAX_S_NUMBER) {
					add_store.setEnabled(false);
				}
				if(cur_s_number > 0) {
					d_store.setEnabled(true);
				}
			}
		});
		contentPane.add(add_store);
				
		d_store.setBounds(561, 698, 122, 23);
		d_store.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				warehouse[cur_s_number].setValue(0);
				ware_count[cur_s_number].setText("未启用");
				cur_s_number--;
				store_number.setText("\u5F53\u524D\u53EF\u7528\u4ED3\u5E93\u6570\u91CF\uFF1A" + cur_s_number);				
				if(cur_s_number == 0) {
					d_store.setEnabled(false);
				} 
				if(cur_s_number < MAX_S_NUMBER) {
					add_store.setEnabled(true);
				}
			}
		});
		contentPane.add(d_store);
				
		start_button.setBounds(20, 149, 97, 23);
		start_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if(cur_s_number == 0) {
					JOptionPane.showMessageDialog(null, "仓库数目不能为0，请添加仓库", "错误提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(cur_s_number == 0) {
					JOptionPane.showMessageDialog(null, "生产者数目不能为0，请添加生产者", "错误提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(cur_c_number == 0) {
					JOptionPane.showMessageDialog(null, "消费者数目不能为0，请添加消费者", "错误提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				setButtonsAcc(false);
				
				stop_button.setEnabled(true);
				reinit_button.setEnabled(false);
				
				start_button.setText("继续");
				
				stop[0] = false;
				create_Thread();
			}
		});
		contentPane.add(start_button);
		
		stop_button = new JButton("\u6682\u505C");
		stop_button.setBounds(20, 361, 97, 23);
		stop_button.setEnabled(false);
		stop_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop[0] = true;
				reinit_button.setEnabled(true);
				stop_button.setEnabled(false);
				setButtonsAcc(true);		
			}
		});
		contentPane.add(stop_button);
		
		JButton exit_button = new JButton("\u9000\u51FA");
		exit_button.setBounds(20, 528, 97, 23);
		exit_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		contentPane.add(exit_button);
		
		reinit_button = new JButton("\u91CD\u7F6E");
		reinit_button.setBounds(20, 251, 97, 23);
		reinit_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start_button.setText("开始");
				restart();
			}
		});
		contentPane.add(reinit_button);
	}
	
	//初始化并启动线程线程
	public void create_Thread() {
		for(int i = 1;i<=cur_p_number;i++) {
			Thread t = new Thread(new Producer(100, cur_s_number, warehouse, textArea, "生产者"+i, ware_count, stop));
			t.setDaemon(true);
			t.start();
		}
		for(int i = 1;i<=cur_c_number;i++) {
			Thread t = new Thread(new Consumer(cur_s_number,  warehouse, textArea, "消费者"+i, ware_count, stop));
			t.setDaemon(true);
			t.start();
		}
	}
	
	//重置窗口信息
	public void restart() {
		cur_p_number = 3;
		cur_c_number = 3;
		cur_s_number = 5;
		
		producer_number.setText("\u5F53\u524D\u751F\u4EA7\u8005\u6570\u91CF\uFF1A" + cur_p_number);
		consumer_number.setText("\u5F53\u524D\u6D88\u8D39\u8005\u6570\u91CF\uFF1A" + cur_c_number);
		store_number.setText("\u5F53\u524D\u53EF\u7528\u4ED3\u5E93\u6570\u91CF\uFF1A" + cur_s_number);				

		for(int i = 1;i<10;i++) {
			if(i <= 5) {
				warehouse[i].setValue(50);
				ware_count[i].setText("当前货物数量 : 50");
			}
			else {
				warehouse[i].setValue(0);
				ware_count[i].setText("未启用");
			}
		}
		
		textArea.setText("");
	}
	
	//设置相关按钮是否可用
	public void setButtonsAcc(boolean flag) {
		start_button.setEnabled(flag);
		add_producer.setEnabled(flag);
		d_producer.setEnabled(flag);
		add_consumer.setEnabled(flag);
		d_consumer.setEnabled(flag);
		add_store.setEnabled(flag);
		d_store.setEnabled(flag);
	}
}
