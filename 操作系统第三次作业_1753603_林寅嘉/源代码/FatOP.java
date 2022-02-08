package assignment3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FatOP {

	//将Fat信息写入文件
	public static void writeFat(int[] Fat) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("Fat.txt")));
		for(int i = 0;i<Fat.length;i++) {
			bw.write(Fat[i]+" ");
		}
		bw.write(FileOP.Number+"");
		bw.flush();
		bw.close();
	}

	//读取Fat表的信息
	public static void readFat(int[] Fat) throws IOException {
		File file = new File("Fat.txt");
		if(file.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String info = br.readLine();
			String[] temps = info.split(" ");
			int len = Fat.length;
			for(int i = 0;i<len;i++) {
				Fat[i] = Integer.parseInt(temps[i]);
			}
			FileOP.Number = Integer.parseInt(temps[len]);
			br.close();
		}
	}
	
	//向txt文件中写入时，分配Fat, -1代表txt文件的结束标志
	public static boolean startFat(int[] Fat, long size, int startFat) {
		int neededBlock = (int) ((size == 0)?1:Math.ceil((double)size/255));
		int len = Fat.length;
		boolean isEnough = false;
		List<Integer> list = new ArrayList<>();
		for(int i = 0;i<len;i++) {
			if(Fat[i] == 0) {
				list.add(i);
			}
			if(list.size() == neededBlock) {
				isEnough = true;
				break;
			}
		}
		if(isEnough) {
			Fat[startFat] = list.get(0);
			int index = neededBlock - 1;
			for(int i = 1;i<index;i++) {
				Fat[list.get(i)] = list.get(i+1);
			}
			Fat[list.get(index)] = -1;
			return true;
		} else {
			return false;
		}
	}
	
	//新建txt时，分配Fat, 返回-2时代表分配失败
	public static int getStartFat(int[] Fat) {
		int len = Fat.length;
		for(int i = 0;i<len;i++) {
			if(Fat[i] == 0) {
				Fat[i] = -1;
				return i;
			}
		}
		return -2;
	}
	
	//格式化txt文件更新Fat
	public static void cleanFileFat(int[] Fat, int startFat) {
		if(Fat[startFat] == -1) return;//如果文件为空，则直接返回
		int nextFat = Fat[startFat];//下一个块的索引
		int copyNextFat = -1;
		while(Fat[nextFat] != -1) {
			copyNextFat = nextFat;
			nextFat = Fat[Fat[nextFat]];
			Fat[copyNextFat] = 0;
		}
		Fat[nextFat] = 0;
		Fat[startFat] = -1;
	}
	
	//删除txt文件时更新Fat
	public static void deleteFileFat(int[] Fat, int startFat) {
		if(Fat[startFat] == -1) {
			Fat[startFat] = 0;//如果文件为空，则直接返回
			return;
		}
		int nextFat = Fat[startFat];//下一个块的索引
		int copyNextFat = -1;
		while(Fat[nextFat] != -1) {
			copyNextFat = nextFat;
			nextFat = Fat[Fat[nextFat]];
			Fat[copyNextFat] = 0;
		}
		Fat[nextFat] = 0;
		Fat[startFat] = 0;
	}	
}