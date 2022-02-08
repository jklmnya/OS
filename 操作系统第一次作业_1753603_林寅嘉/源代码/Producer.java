package assignment1;

import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

/**
 *  生产者
 * @author linyi
 *
 */

public class Producer implements Runnable{
	
	private int PRE_MAX_STORE;//每个缓冲区单元格的最大上限
	private int cur_s_number;//当前的可用仓库数量
	private JProgressBar[] warehouse;//仓库
	private JTextArea textArea;//生产消费信息
	private String name;//该线程的名字
	private JLabel[] ware_count;//每个缓存区内的物品数量
	private boolean[] stop;//是否停止当前线程的标志
	
	//构造函数
	public Producer(int pRE_MAX_STORE, int cur_s_number, JProgressBar[] warehouse, JTextArea textArea, String name, JLabel[] ware_count, boolean[] stop) {
		PRE_MAX_STORE = pRE_MAX_STORE;
		this.cur_s_number = cur_s_number;
		this.warehouse = warehouse;
		this.textArea = textArea;
		this.name = name;
		this.ware_count = ware_count;
		this.stop = stop;
	}

	@Override
	public void run() {
		while(!stop[0]) {
			//给缓存区加锁，防止其他线程对其访问
			synchronized(warehouse) {
				//判断是否需要结束当前线程
				if(stop[0]) return;
				StringBuffer info = new StringBuffer();//打印的信息
				//随机产生一个缓存区的序号
				int store_number = Math.max(new Random().nextInt(cur_s_number + 1) , 1);
				int current_number = warehouse[store_number].getValue();
				//当前缓存区已满，停止生产,通知消费
				if(current_number == PRE_MAX_STORE) {
					info.append("生产消息\n").append("当前生产者 : ").append(this.name);
					info.append(" 仓库").append(store_number).append("已满，无法继续生产\n\n");
					textArea.append(info.toString());
					textArea.setCaretPosition(textArea.getDocument().getLength());
					
					try {
						warehouse.wait();//放弃对缓存区的锁
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					//当前缓存区未满，开始生产
					int count = Math.max((int)(Math.random()*10), 1);
					current_number += count;//随机产生要生产的数量
					
					info.append("生产消息\n").append("当前生产者 : ").append(this.name);
					info.append(" 生产").append(count).append("件商品放入仓库").append(store_number).append("\n\n");
					textArea.append(info.toString());
					textArea.setCaretPosition(textArea.getDocument().getLength());//文本框自动更新到最低端
					
					//判断当前数量是否大于最大上限
					if(current_number >= PRE_MAX_STORE) {
						warehouse[store_number].setValue(PRE_MAX_STORE);
						ware_count[store_number].setText("当前货物数量:100");
					} else {
						warehouse[store_number].setValue(current_number);
						ware_count[store_number].setText("当前货物数量:" + current_number);
					}
				}
				//当前线程持有缓存区的锁，休眠1s
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//生产完毕，通知消费
				warehouse.notifyAll();
			}	
			//当前线程休眠1s
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
