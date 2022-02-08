package assignment3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class FileOP {

	protected static int Number = 1;
	
	//������Ŀ¼
	public static FCB makeRoot(String path) {
		File Root = new File(path);
		if(Root.exists() && Root.isDirectory()) return null;
		else {
			Root.mkdir();
			String foundTime = getFoundTime();
			File file = new File("");
			FCB PreRoot = new FCB(file.getAbsolutePath(), new ArrayList<FCB>());
			FCB RootFcb = new FCB("Root", "folder", foundTime, path, -1, 0, PreRoot, null);
			PreRoot.getSubFils().add(RootFcb);
			return PreRoot;
		}
	}
	
	//�����ļ���
	public static String makeDir(String path, FCB Root) {
		String fileName = "�½��ļ���" + Number;
		StringBuffer filePath = new StringBuffer().append(path).append("\\").append(fileName);
		File Dir = new File(filePath.toString());
		if(!Dir.exists()) {
			Dir.mkdir();
			String foundTime = getFoundTime();
			//�����ļ���FCB
			FCB fcb = new FCB(fileName, "folder", foundTime, filePath.toString(), -1, 0, Root, null);
			FcbOP.addFCB(Root, fcb);
			Number++;
			return fileName;
		}
		return null;
	}
	
	//����txt�ļ�
	public static String makeFile(String path, FCB Root, int[] Fat) throws IOException {
		String FileName = "�½��ı��ļ�" + Number + ".txt";
		StringBuffer FilePath = new StringBuffer().append(path).append("\\").append(FileName);
		File newFile = new File(FilePath.toString());
		if(!newFile.exists()) {			
			newFile.createNewFile();			
			String foundTime = getFoundTime();
			//�ж��Ƿ����ʧ��
			int startFat = FatOP.getStartFat(Fat);
			System.out.println(Arrays.toString(Fat));
			if(startFat == -2) {
				return null;
			}
			FCB fcb = new FCB(FileName, "txt", foundTime, FilePath.toString(), startFat, 0, Root, null);
			FcbOP.addFCB(Root, fcb);
			Number++;
			return FileName;
		}
		return null;
	}
	
	//��ʽ���ļ�
	public static boolean cleanFile(String path, int[] Fat, FCB fcb) {
		File file = new File(path);
		if(file.isDirectory()) return false;
		if(file.exists()) {
			try {
				file.createNewFile();
				FileWriter filewriter = new FileWriter(file);
				filewriter.write("");
				FatOP.cleanFileFat(Fat, fcb.getStartFat());
				System.out.println(Arrays.toString(Fat));
				filewriter.flush();
				filewriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		return true;
	}
	
	//�����ļ�
	public static boolean copyFile(String src, String dest) throws IOException {
		File srcFile = new File(src);
		File destFile = new File(dest);
		return copyDir(srcFile, destFile);
	}
	
	public static boolean copyFile(File srcFile, File destFile) throws IOException {
		if(!srcFile.exists() || destFile.exists()) return false;
		
		InputStream in = new BufferedInputStream(new FileInputStream(srcFile));
		OutputStream out = new BufferedOutputStream(new FileOutputStream(destFile));
		byte[] info = new byte[1024];
		int len = 0;
		while(-1 != (len = in.read(info))) {
			out.write(info, 0, len);
		}
		out.flush();
		in.close();
		out.close();
		return true;				
	}
	
	//�����ļ���
	public static boolean copyDir(String src, String dest) throws IOException {
		File srcFile = new File(src);
		File destFile = new File(dest);
		return copyDir(srcFile, destFile);
	}
	
	public static boolean copyDir(File src, File dest) throws IOException {
		if(src.isDirectory()) {
			dest = new File(dest, src.getName());
			if(dest.getAbsolutePath().contains(src.getAbsolutePath())){
				return false;
			}
		}
		return copyDirDetails(src, dest);
	}
	
	public static boolean copyDirDetails(File src, File dest) throws IOException {
		if(src.isFile()) {
			return copyFile(src, dest);
		} else if(src.isDirectory()){
			dest.mkdirs();
			for(File subFiles : src.listFiles()) {
				return copyDirDetails(subFiles, new File(dest, subFiles.getName()));
			}
		}
		return true;
	}
	
	//ɾ��
	public static boolean deleteFile(String filePath, FCB Root, FCB fcb, int[] Fat) {
		File file = new File(filePath);
		if(!file.exists()) {
			return false;
		}
		return deleteFile(file, Root, fcb, Fat);
	}
	
	//�ݹ�ɾ���ļ��м��ļ�
	private static boolean deleteFile(File file, FCB Root, FCB fcb, int[] Fat) {
		if(file.isDirectory()) {
			for(File subFile : file.listFiles()) {
				FCB s_s_fcb = FcbOP.findFcb(fcb, subFile.getName());
				deleteFile(subFile, fcb, s_s_fcb, Fat);
			}
			FcbOP.deleteFCB(Root, fcb);
			return file.delete();
		} else {
			FatOP.deleteFileFat(Fat, fcb.getStartFat());
			FcbOP.deleteFCB(Root, fcb);
			return file.delete();
		}	
	}
	
	//�����ļ�/�ļ��е�ʱ��
	private static String getFoundTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		Date date = new Date(System.currentTimeMillis());
		String foundTime = sdf.format(date);
		return foundTime;
	}
}