import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
import java.util.List;

public class QuizGUI extends JFrame {
    private JButton adminButton;
    private JButton studentButton;
    private JButton newStudentButton;

    private JPanel panel;

    public QuizGUI() {
        setTitle("QUIZ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("WELCOME TO QUIZ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        adminButton = new JButton("Admin");
        studentButton = new JButton("Student");
        newStudentButton = new JButton("New Student");

        buttonPanel.add(adminButton);
        buttonPanel.add(studentButton);
        buttonPanel.add(newStudentButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Button listeners
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAdminLogin();
            }
        });

        studentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showStudentLogin();
            }
        });

        newStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showNewStudentSignup();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void showAdminLogin() {
        String adminUsername = "admin";
        String adminPassword = "admin123";

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 1));
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(null, loginPanel, "Admin Login", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals(adminUsername) && password.equals(adminPassword)) {
                showAdminOptions();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
            }
        }
    }

    private void showAdminOptions() {
        // Code to display admin options and handle question papers (creation/deletion)
        // goes here
        JButton createPaperButton = new JButton("Create Paper");

        createPaperButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createQuestionPaper();
            }
        });

        JButton deletePaperButton = new JButton("Delete Paper");

        deletePaperButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteQuestionPaper();
            }
        });

        JPanel adminOptionsPanel = new JPanel();
        adminOptionsPanel.add(createPaperButton);
        adminOptionsPanel.add(deletePaperButton);

        JOptionPane.showMessageDialog(null, adminOptionsPanel, "Admin Options", JOptionPane.PLAIN_MESSAGE);
    }

    private void createQuestionPaper() {
        int numQuestions = 10;

        JTextField paperNameField = new JTextField(50);

        JPanel paperNamePanel = new JPanel();
        paperNamePanel.add(new JLabel("Question Paper Name:"));
        paperNamePanel.add(paperNameField);

        int option = JOptionPane.showConfirmDialog(null, paperNamePanel, "Create Question Paper",
                JOptionPane.OK_CANCEL_OPTION);

        if (option != JOptionPane.OK_OPTION) {
            // User canceled, handle accordingly (e.g., exit the creation process)
            return;
        }

        String paperName = paperNameField.getText();

        String[] questionList = new String[numQuestions];
        String[] answerList = new String[numQuestions];
        String[] optionsList = new String[numQuestions];

        for (int i = 0; i < numQuestions; i++) {
            JTextField questionField = new JTextField(50);
            JTextField option1Field = new JTextField(50);
            JTextField option2Field = new JTextField(50);
            JTextField option3Field = new JTextField(50);
            JTextField option4Field = new JTextField(50);

            JPanel questionPanel = new JPanel();
            questionPanel.setLayout(new GridLayout(7, 1));
            questionPanel.add(new JLabel("Question " + (i + 1) + ":"));
            questionPanel.add(questionField);
            questionPanel.add(new JLabel("Option 1:"));
            questionPanel.add(option1Field);
            questionPanel.add(new JLabel("Option 2:"));
            questionPanel.add(option2Field);
            questionPanel.add(new JLabel("Option 3:"));
            questionPanel.add(option3Field);
            questionPanel.add(new JLabel("Option 4:"));
            questionPanel.add(option4Field);

            int result = JOptionPane.showConfirmDialog(null, questionPanel, "Create Question",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result != JOptionPane.OK_OPTION) {
                // User canceled, handle accordingly (e.g., exit the creation process)
                return;
            }

            String question = questionField.getText();
            String option1 = option1Field.getText();
            String option2 = option2Field.getText();
            String option3 = option3Field.getText();
            String option4 = option4Field.getText();

            String options = option1 + ";" + option2 + ";" + option3 + ";" + option4;

            questionList[i] = question;
            optionsList[i] = options;

            JComboBox<String> answerComboBox = new JComboBox<>(
                    new String[] { "Option 1", "Option 2", "Option 3", "Option 4" });

            JPanel answerPanel = new JPanel();
            answerPanel.add(new JLabel("Select the Correct Answer:"));
            answerPanel.add(answerComboBox);

            JOptionPane.showConfirmDialog(null, answerPanel, "Select Answer", JOptionPane.DEFAULT_OPTION);

            String answer = (String) answerComboBox.getSelectedItem();
            answerList[i] = answer;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Questions.txt", true))) {
            writer.write("Question Paper Name: " + paperName + "\n");

            for (int i = 0; i < numQuestions; i++) {
                writer.write("Question " + (i + 1) + ": " + questionList[i] + "\n");
                writer.write("Options: " + optionsList[i] + "\n");
                writer.write("Answer: " + answerList[i] + "\n");
            }

            writer.write("\n");

            JOptionPane.showMessageDialog(null, "Question Paper created successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while saving the Question Paper.");
        }
    }

    private void deleteQuestionPaper() {
        String paperName = JOptionPane.showInputDialog(null, "Enter the name of the question paper to delete:");

        try (BufferedReader reader = new BufferedReader(new FileReader("Questions.txt"))) {
            ArrayList<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Question Paper Name:")) {
                    String existingPaperName = line.substring(line.indexOf(":") + 1).trim();

                    if (existingPaperName.equals(paperName)) {
                        // Skip the lines related to the paper being deleted
                        for (int i = 0; i < 12; i++) {
                            reader.readLine();
                        }
                    } else {
                        // Add the lines of other papers to the list
                        lines.add(line);

                        for (int i = 0; i < 12; i++) {
                            lines.add(reader.readLine());
                        }
                    }
                } else {
                    // Add the lines that are not related to any question paper
                    lines.add(line);
                }
            }

            reader.close();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Questions.txt"))) {
                for (String updatedLine : lines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            }

            JOptionPane.showMessageDialog(null, "Question Paper deleted successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while deleting the Question Paper.");
        }
    }

    private void showStudentLogin() {
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 1));
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(null, loginPanel, "Student Login", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (checkStudentCredentials(username, password)) {
                showStudentOptions();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
            }
        }
    }

    private boolean checkStudentCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("StuData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String existingUsername = parts[0];
                String existingPassword = parts[1];
                if (existingUsername.equals(username) && existingPassword.equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while checking student credentials.");
        }
        return false;
    }

    private void showStudentOptions() {
        JButton takeQuizButton = new JButton("Take Quiz");
        JButton viewScoreButton = new JButton("View Score");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.add(takeQuizButton);
        panel.add(viewScoreButton);

        JFrame frame = new JFrame("Student Options");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 100);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.setVisible(true);

        takeQuizButton.addActionListener(e -> showAvailableQuestionPapers());
        viewScoreButton.addActionListener(e -> viewScore());
    }

    private void showAvailableQuestionPapers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Questions.txt"))) {
            String line;
            ArrayList<String> papers = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Question Paper Name:")) {
                    String paperName = line.substring(line.indexOf(":") + 1).trim();
                    papers.add(paperName);
                }
            }

            if (papers.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No question papers available.");
            } else {
                Object[] options = papers.toArray();
                String selectedPaper = (String) JOptionPane.showInputDialog(
                        null,
                        "Select a question paper:",
                        "Question Papers",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (selectedPaper != null) {
                    // Start the quiz for the selected paper
                    startQuiz(selectedPaper); // Start from the first question
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading the question papers.");
        }
    }

    private void startQuiz(String paperName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Questions.txt"))) {
            JFrame frame = new JFrame("Quiz: " + paperName);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JScrollPane scrollPane = new JScrollPane(panel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            frame.add(scrollPane);

            String line;
            boolean isPaperFound = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Question Paper Name:")
                        && line.substring(line.indexOf(":") + 1).trim().equals(paperName)) {
                    isPaperFound = true;
                    continue;
                }

                if (isPaperFound && line.startsWith("Question:")) {
                    String question = line.substring(line.indexOf(":") + 1).trim();
                    JLabel questionLabel = new JLabel(question);
                    questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    panel.add(questionLabel);

                    String[] options = new String[4];
                    line = reader.readLine(); // Read the "Options:" line
                    line = line.substring(line.indexOf(":") + 1).trim(); // Extract the options string
                    options = line.split(",");

                    ButtonGroup buttonGroup = new ButtonGroup();
                    for (String option : options) {
                        JRadioButton optionButton = new JRadioButton(option);
                        optionButton.setActionCommand(option);
                        buttonGroup.add(optionButton);
                        panel.add(optionButton);
                    }
                }
            }

            if (!isPaperFound) {
                JOptionPane.showMessageDialog(null, "Question paper not found.");
            } else {
                JButton submitButton = new JButton("Submit");
                panel.add(submitButton);

                submitButton.addActionListener(e -> {
                    int score = 0;
                    Component[] components = panel.getComponents();
                    for (Component component : components) {
                        if (component instanceof JRadioButton) {
                            JRadioButton optionButton = (JRadioButton) component;
                            if (optionButton.isSelected()
                                    && optionButton.getActionCommand().equals(getCorrectAnswer(paperName))) {
                                score++;
                            }
                        }
                    }

                    saveResult(paperName, score);

                    frame.dispose();
                    JOptionPane.showMessageDialog(null, "Quiz completed! Score: " + score);
                });

                frame.setVisible(true);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading the questions.");
        }
    }

    private String getCorrectAnswer(String paperName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Questions.txt"))) {
            String line;
            boolean isPaperFound = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Question Paper Name:")
                        && line.substring(line.indexOf(":") + 1).trim().equals(paperName)) {
                    isPaperFound = true;
                    continue;
                }

                if (isPaperFound && line.startsWith("Answer:")) {
                    return line.substring(line.indexOf(":") + 1).trim();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading the questions.");
        }
        return "";
    }

    private void saveResult(String paperName, int score) {
        try {
            // Check if the quiz name already exists in the results file
            boolean quizExists = false;
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader("Results.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(paperName)) {
                        // Update the score for the existing quiz
                        int existingScore = Integer.parseInt(parts[1]);
                        existingScore += score;
                        sb.append(paperName).append(",").append(existingScore).append(",").append(parts[2])
                                .append("\n");
                        quizExists = true;
                    } else {
                        sb.append(line).append("\n");
                    }
                }
            }

            // If the quiz does not exist, add it to the results file
            if (!quizExists) {
                sb.append(paperName).append(",").append(score).append(",").append(System.currentTimeMillis())
                        .append("\n");
            }

            // Write the updated results to the file
            Files.write(Paths.get("Results.txt"), sb.toString().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while saving the result.");
        }
    }

    private void viewScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Results.txt"))) {
            JFrame frame = new JFrame("Quiz Scores");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String quizName = data[0].trim();
                int score = Integer.parseInt(data[1].trim());
                String completionTime = data[2].trim();

                JButton quizButton = new JButton(quizName);
                quizButton.addActionListener(e -> {
                    JOptionPane.showMessageDialog(null,
                            "Quiz: " + quizName + "\nBest Score: " + score + "\nCompletion Time: " + completionTime);
                });
                panel.add(quizButton);
            }

            JScrollPane scrollPane = new JScrollPane(panel);
            frame.add(scrollPane);

            frame.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading the scores.");
        }
    }

    private void showNewStudentSignup() {
        JTextField nameField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
                "Name:", nameField,
                "Username:", usernameField,
                "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "New Student Signup", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("StuData.txt", true))) {
                writer.write(username + "," + password + "," + name);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "An error occurred while saving the student data.");
            }

            JOptionPane.showMessageDialog(null, "New student signup successful!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                QuizGUI quizGUI = new QuizGUI();
                quizGUI.setVisible(true);
            }
        });
    }
}
