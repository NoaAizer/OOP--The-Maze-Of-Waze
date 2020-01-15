package utils;

import java.util.List;
import gameClient.MyGameGUI;

public class MoveManual implements Runnable {

	private static int prev_id;
	private static int prev_key;

	private int key;
	private int id;

	public MoveManual(int src, int idR) {
		this.key=src;
		this.id=idR;
	}

	public void setKey(int src) {
		this.key=src;

	}
	public void setId(int idR) {
		this.id=idR;

	}
	@Override
	public void run() {
		while(MyGameGUI.getGame().isRunning()) {
			List<String> log = MyGameGUI.getGame().move();
			if(key != -1 && id != -1) {
				if(prev_key != key || prev_id != id) {
					MyGameGUI.getGame().chooseNextEdge(id,key);
					System.out.println("robot "+ id+ " moved to: " +key +"\n" +log);
					if(prev_key != key) {
						prev_key = key;}
					if(prev_id != id) {
						prev_id = id;}
				}
			}
		}
	}
}

