package gameClient;

import java.util.List;

public class MoveManual implements Runnable {

	private static int prev_id;// the previous robot was chosen
	private static int prev_key;//the previous key was chosen

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
		while(MyGameGUI.arena.getGame().isRunning()) {
			List<String> log = MyGameGUI.arena.getGame().move();
			if(key != -1 && id != -1) {
				if(prev_key != key || prev_id != id) {	// checks if the user chose another id or node 
					MyGameGUI.arena.getGame().chooseNextEdge(id,key);
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

