package assignment2;

import java.util.ArrayList;
import java.util.Collections;

public class Memory{

	private int[] isFirst;
	
	private static final int memeoy = 640;
	
	private ArrayList<Task> zones = new ArrayList<>();//保存Task的链表
	private Rectangle rect;
	
	public Memory(int[] isFirst, Rectangle rect) {
		this.isFirst = isFirst;	
		this.rect = rect;
		zones.add(new Task(0, memeoy, 0, true));
	}

	//处理请求
	public boolean handle(int memorySize, int taskNumber, boolean isFree) {
		if(isFirst[0] == 1) {
			return firstFit(memorySize, taskNumber, isFree); 			
		} else {
			return BestFit(memorySize, taskNumber, isFree);
		}
	}
	
	//首次适应算法
	private boolean firstFit(int memorySize, int taskNumber, boolean isFree) {
		Collections.sort(zones);
		int length = zones.size();
		//释放资源		
		if(isFree) {
			return freeMemory(memorySize, taskNumber, length);				
		} else {	
			//申请资源
			for(int i = 0;i<length;i++) {
				Task task = zones.get(i);
				int startLoc = task.getStart();
				int endLoc = task.getEnd();
				int Size = endLoc - startLoc;				
				if(task.isFree() && memorySize<=Size) {
					zones.add(new Task(startLoc, startLoc+memorySize, taskNumber, false));
					rect.addLoc(startLoc, startLoc+memorySize);
					if(memorySize == Size) {
						zones.remove(task);
					} else {
						task.setStart(startLoc + memorySize);
					}					
					return true;
				}
			}
		}
		return false;
	}
	
	//最佳适应算法
	private boolean BestFit(int memorySize, int taskNumber, boolean isFree) {
		Collections.sort(zones);
		int length = zones.size();
		
		//释放资源
		if(isFree) {
			return freeMemory(memorySize, taskNumber, length);
		} else {
			//申请资源
			Task task = null;
			Task targetTask = null;
			
			int startLoc = 0;
			int endLoc = 0;
			int Size = 0;			
			int minMemory = Integer.MAX_VALUE;
			//寻找最小且合适的分区
			for(int i = 0;i<length;i++) {
				task = zones.get(i);
				startLoc = task.getStart();
				endLoc = task.getEnd();
				Size = endLoc - startLoc;
				if(task.isFree() && memorySize<=Size) {
					if(minMemory > Size) {
						minMemory = Size;
						targetTask = task;
					}
				}
			}			
			//是否找到合适的分区
			if(targetTask == null) {
				return false;
			} else {
				startLoc = targetTask.getStart();
				endLoc = targetTask.getEnd();
				Size = endLoc - startLoc;
				zones.add(new Task(startLoc, startLoc + memorySize, taskNumber, false));
				rect.addLoc(startLoc, startLoc+memorySize);
				if(Size == memorySize) {
					zones.remove(targetTask);
				} else {
					targetTask.setStart(startLoc + memorySize);
				}
				return true;
			}
		}
	}

	//释放资源
	private boolean freeMemory(int memorySize, int taskNumber, int length) {
		Task task = null;
		Task task_pre = null;
		Task task_next = null;		
		if(length == 1) {
			task = zones.get(0);
			if(task.getNumber() == taskNumber) {
				rect.removeLoc(task.getStart(), task.getEnd());
				task.setFree(true);
				task.setNumber(0);
				return true;
			}
		}
		
		for(int i = 1;i<length - 1;i++) {
			task = zones.get(i);
			if(task.getNumber() == taskNumber) {
				task_pre = zones.get(i-1);					
				task_next = zones.get(i+1);
				if(task_next.isFree() && task.getEnd() == task_next.getStart()) {
					rect.removeLoc(task.getStart(), task.getEnd());
					task.setEnd(task_next.getEnd());
					zones.remove(task_next);
				} else if(task_pre.isFree() && task_pre.getEnd() == task.getStart()){
					rect.removeLoc(task.getStart(), task.getEnd());
					task_pre.setEnd(task.getEnd());
					zones.remove(task);
				} else {
					rect.removeLoc(task.getStart(), task.getEnd());
				}					
				task.setFree(true);
				task.setNumber(0);
				//for(int j = 0;j<zones.size();j++) zones.get(j).print();
				return true;
			}
		}
		
		task = zones.get(length - 1);
		if(task.getNumber() == taskNumber) {
			if(length >= 2) {
				task_pre = zones.get(length - 2);
				if(task_pre.isFree() && task_pre.getEnd() == task.getStart()) {
					rect.removeLoc(task.getStart(), task.getEnd());
					task_pre.setEnd(task.getEnd());
					zones.remove(task);
				} else {
					rect.removeLoc(task.getStart(), task.getEnd());
				}					
			} 
			rect.removeLoc(task.getStart(), task.getEnd());
			//for(int j = 0;j<zones.size();j++) zones.get(j).print();
			return true;
		}
		
		task = zones.get(0);
		if(task.getNumber() == taskNumber) {
			if(length >= 2) {
				task_next = zones.get(1);
				if(task_next.isFree() && task.getEnd() == task_next.getStart()) {
					rect.removeLoc(task.getStart(), task.getEnd());
					task.setEnd(task_next.getEnd());
					zones.remove(task_next);
				} else {
					rect.removeLoc(task.getStart(), task.getEnd());
				}					
			}
			rect.removeLoc(task.getStart(), task.getEnd());
			task.setFree(true);
			task.setNumber(0);
			//for(int j = 0;j<zones.size();j++) zones.get(j).print();
			return true;
		}
		return false;
	}
	
}

//内存区域
class Task implements Comparable<Task>{
	private int start;
	private int end;
	private int number;
	private boolean isFree;
	
	public Task(int start, int end, int number, boolean isFree) {
		this.start = start;
		this.end = end;
		this.isFree = isFree;
		this.number = number;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	@Override
	public int compareTo(Task task) {		
		return this.start>task.start?1:-1;
	}
	
	public void print() {
		System.out.println(start + " " + end + " " + number + " "+ isFree);
	}
}