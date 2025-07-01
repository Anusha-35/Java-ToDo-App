import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class ToDoApp extends JFrame {
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskField;
    private JButton addButton, deleteButton;
    private final String TASKS_FILE = "tasks.txt";

    public ToDoApp() {
        setTitle("To-Do List App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null); // Center the window

        // Create components
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);

        taskField = new JTextField();
        addButton = new JButton("Add Task");
        deleteButton = new JButton("Delete Task");

        // Layout setup
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(taskField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(deleteButton, BorderLayout.CENTER);

        Container container = getContentPane();
        container.setLayout(new BorderLayout(10, 10));
        container.add(inputPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());

        // Load tasks from file
        loadTasksFromFile();
    }

    private void addTask() {
        String task = taskField.getText().trim();
        if (!task.isEmpty()) {
            taskListModel.addElement(task);
            taskField.setText("");
            saveTasksToFile();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please enter a task!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTask() {
        int selectedIdx = taskList.getSelectedIndex();
        if (selectedIdx != -1) {
            taskListModel.remove(selectedIdx);
            saveTasksToFile();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a task to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))) {
            for (int i = 0; i < taskListModel.getSize(); i++) {
                writer.write(taskListModel.getElementAt(i));
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving tasks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTasksFromFile() {
        File file = new File(TASKS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                taskListModel.addElement(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading tasks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoApp().setVisible(true));
    }
}
