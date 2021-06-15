package fr.uge.todo;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import javax.swing.JList;

public class ToDo extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1523453941890958827L;
	private JTextField txtTask;
	


	/**
	 * Create the frame.
	 */
	public ToDo() {
		txtTask = new JTextField();
		
	}
	
	private void cleanAndAdd(String words, DefaultListModel<String> task, DefaultListModel<String> taskJson) {
		String[] Tabwords = words.split(",");
		for (String word : Tabwords) {
        	if (word.substring(0,1).equals("[")) {
        	//	word.(0);
        		word = word.substring(1,word.length());
        	}
        	if (word.substring(word.length()-1,word.length()).equals("]")) {
            	//	word.(0);
            		word = word.substring(0,word.length()-1);
            	}
        	task.addElement(word);
        	taskJson.addElement(word);
        }
		
	}
	
	public void launch() {
		DefaultListModel<String> task = new DefaultListModel<>();
		DefaultListModel<String> taskJson = new DefaultListModel<>(); 
		String words = readJson();
		this.cleanAndAdd(words, task, taskJson);
		this.configureWindow();
		this.createFrame(task, taskJson, words);
	}
	
	private void createFrame(DefaultListModel<String> task, DefaultListModel<String> taskJson, String words) {	
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		//menuBar.add(btnSave);
		
		JButton btnAddList = new JButton("Add List");
		btnAddList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//taskJson.addElement("false |"+txtTask.getText());
				taskJson.addElement(txtTask.getText());
				task.addElement(txtTask.getText());
				addList(taskJson);
				txtTask.setText("");
			}
		});
		menuBar.add(btnAddList);
		
		txtTask = new JTextField();
		txtTask.setText("Task");
		menuBar.add(txtTask);
		txtTask.setColumns(10);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		menuBar.add(btnClose);
		getContentPane().setLayout(new BorderLayout(5, 0));
		
		JList<String> list = new JList<>(task);
		getContentPane().add(list);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				taskJson.removeElementAt(list.getSelectedIndex());
				task.remove(list.getSelectedIndex());
				addList(taskJson);
			}
		});
		getContentPane().add(btnDelete, BorderLayout.SOUTH);

	}
	
	private void configureWindow() {
		this.setTitle("ToDo List");
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	void addList(DefaultListModel<String> taskJson)
	{
		try (FileWriter fileWriter = new FileWriter("list.json"))
		{
			JsonObject jsonObj = Json.createObjectBuilder()
						.add("tasks", taskJson.toString())
					.build();
			JsonWriter writer = Json.createWriter(fileWriter);
			writer.writeObject(jsonObj);
		
		}
		catch(Exception e) {
			return;
		}
	}
	
	
	String readJson() {
		FileReader fileReader = null;
		
		try {
			fileReader = new FileReader("list.json");
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Il n'existe pas de fichier de tâche.\n Un nouveau fichier va �tre cr�er");
		}
		
		try{
			JsonReader reader = Json.createReader(fileReader);
			JsonObject jsonObj = reader.readObject();
			//String truc = jsonObj.getString("Task");//pb
			String truc = jsonObj.getString("tasks");
			DefaultListModel<String> listTask = new DefaultListModel<>();
			listTask.addElement(truc);
			
			dispose();
			return listTask.getElementAt(0);

		}
		catch(Exception e) 
		{
			JOptionPane.showMessageDialog(null, "Probleme de lecture");
			dispose();
		}
		return null;
	}
}
