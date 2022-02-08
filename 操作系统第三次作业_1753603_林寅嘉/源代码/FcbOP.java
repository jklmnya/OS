package assignment3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FcbOP {
	
	//���л�Root�ļ��е�FCB
	public static void storeRoot(FCB Root) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("Store.txt")));
		oos.writeObject(Root);
		oos.flush();
		oos.close();
	}
	
	//�����л�Root
	public static FCB readRoot() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("Store.txt")));
		FCB Root = (FCB) ois.readObject();
		ois.close();
		return Root;
	}
	
	//����FCB
	public static boolean addFCB(FCB Root, FCB sub) {
		if(Root == null || sub == null) return false;
		if(Root.getSubFils() == null) {
			Root.setSubFils(new ArrayList<FCB>());
			Root.getSubFils().add(sub);			
		} else {
			Root.getSubFils().add(sub);
		}
		return true;
	}
	
	//ɾ��FCB
	public static boolean deleteFCB(FCB Root, FCB sub) {
		if(Root == null || sub == null) return false;
		if(Root.getSubFils()!= null && Root.getSubFils().contains(sub)) {
			return Root.getSubFils().remove(sub);
		}
		return false;
	}
	
	//Ѱ��FCB
	public static FCB findFcb(FCB Root, String subFcbName) {
		if(Root.getSubFils() == null) return null;
		for (FCB sub : Root.getSubFils()) {
			if(sub.getFileName().equals(subFcbName)) return sub;
		}
		return null;
	}
}