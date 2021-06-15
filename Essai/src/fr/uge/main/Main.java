package fr.uge.main;
import java.awt.EventQueue;

import fr.uge.todo.ToDo;

public class Main {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ToDo frame = new ToDo();
					frame.setVisible(true);
					frame.launch();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
