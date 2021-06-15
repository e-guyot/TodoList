import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.ListModel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.GridLayout;

public class ToDo extends JFrame {
	private JTextField txtTask;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ToDo frame = new ToDo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ToDo() {
		DefaultListModel<String> task = new DefaultListModel<>();
		DefaultListModel<String> taskJson = new DefaultListModel<>(); 
		String words = ReadJson();
		CleanAndAdd(words, task, taskJson);
		
		setTitle("ToDo List");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
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
				AddList(taskJson);
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
				AddList(taskJson);
			}
		});
		getContentPane().add(btnDelete, BorderLayout.SOUTH);

	}
	
	private void CleanAndAdd(String words, DefaultListModel<String> task, DefaultListModel<String> taskJson) {
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

	void AddList(DefaultListModel<String> taskJson)
	{
		FileWriter fileWriter = null;
		try 
		{
			fileWriter = new FileWriter("list.json");
			JsonObject jsonObj = Json.createObjectBuilder()
						.add("tasks", taskJson.toString())
					.build();
			JsonWriter writer = Json.createWriter(fileWriter);
			writer.writeObject(jsonObj);
			writer.close();
		
		}
		catch(Exception e) 
		{
			JsonObject jsonObj = Json.createObjectBuilder()
					.addNull("TaskList")
						.add("tasks", taskJson.toString())
					.build();
			JsonWriter writer = Json.createWriter(fileWriter);
			writer.writeObject(jsonObj);
			writer.close();
		}
	}
	String ReadJson() 
	{
		FileReader fileReader = null;
		
		try { fileReader = new FileReader("list.json"); }
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Il n'existe pas de fichier de sauvegarde.\n Un nouveau fichier va être créer");
		}
		try
		{
			JsonReader reader = Json.createReader(fileReader);
			JsonObject jsonObj = reader.readObject();
			//String truc = jsonObj.getString("Task");//pb
			String truc = jsonObj.getString("tasks");
			DefaultListModel<String> listTask = new DefaultListModel<>();
			listTask.addElement(truc);
			
			dispose();
			return listTask.getElementAt(0);

		}
		catch(Exception e) // Sinon on affiche qu'il y a un probleme car le mot de passe n'est pas équivalent
		{
			JOptionPane.showMessageDialog(null, "Probleme de lecture");
			dispose();
		}
		return null;
	}
}
