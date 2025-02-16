import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    // Modern color scheme
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);     // Blue
    private static final Color BUTTON_HOVER_COLOR = new Color(25, 118, 210); // Darker blue
    private static final Color TEXT_COLOR = new Color(33, 33, 33);          // Dark gray
    private static final Color CORRECT_COLOR = new Color(46, 204, 113);     // Green
    private static final Color INCORRECT_COLOR = new Color(231, 76, 60);    // Red

    // Modern font
    private static final Font TITLE_FONT = new Font("Roboto", Font.BOLD, 24);
    private static final Font QUESTION_FONT = new Font("Roboto", Font.BOLD, 18);
    private static final Font BUTTON_FONT = new Font("Roboto", Font.PLAIN, 16);
    private static final Font SCORE_FONT = new Font("Roboto", Font.BOLD, 16);

    // Slovak and English language constants
    public static final String[] SK = {
            "Medicínsky vedomostný test",      // 0: Title
            "Otázka",                          // 1: Question
            "Skóre",                           // 2: Score
            "z",                               // 3: of
            "Správne!",                        // 4: Correct
            "Nesprávna odpoveď.",              // 5: Wrong answer
            "Správna odpoveď je:",             // 6: The correct answer is
            "Test dokončený",                  // 7: Quiz Completed
            "Blahoželáme!",                    // 8: Congratulations
            "Konečné skóre:",                  // 9: Final Score
            "Percentá:",                       // 10: Percentage
            "Zavrieť",                         // 11: Close
            "Výsledok"                         // 12: Result
    };

    public static final String[] EN = {
            "Medical Knowledge Test",           // 0: Title
            "Question",                         // 1: Question
            "Score",                           // 2: Score
            "of",                              // 3: of
            "Correct!",                        // 4: Correct
            "Wrong answer.",                   // 5: Wrong answer
            "The correct answer is:",          // 6: The correct answer is
            "Quiz Completed",                  // 7: Quiz Completed
            "Congratulations!",                // 8: Congratulations
            "Final Score:",                    // 9: Final Score
            "Percentage:",                     // 10: Percentage
            "Close",                           // 11: Close
            "Result"                           // 12: Result
    };

    private String[] currentLanguage = SK;
    private int currentQuestion = 0;
    private int score = 0;
    private JLabel questionLabel;
    private JLabel scoreLabel;
    private List<JButton> optionButtons;
    private List<Question> questions;
    private JProgressBar progressBar;

    public App() {
        setTitle(currentLanguage[0]);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(BACKGROUND_COLOR);

        initializeQuestions();
        setupUI();
        setupLanguageToggle();
        showQuestion();
        setLocationRelativeTo(null);
    }

    private void setupLanguageToggle() {
        JButton languageButton = createStyledButton("EN/SK");
        languageButton.setPreferredSize(new Dimension(100, 30));
        languageButton.addActionListener(e -> toggleLanguage());

        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        langPanel.setBackground(BACKGROUND_COLOR);
        langPanel.add(languageButton);

        add(langPanel, BorderLayout.NORTH);
    }

    private void toggleLanguage() {
        currentLanguage = (currentLanguage == SK) ? EN : SK;
        updateUILanguage();
    }

    private void updateUILanguage() {
        setTitle(currentLanguage[0]);
        showQuestion();
        scoreLabel.setText(currentLanguage[2] + ": " + score);
        progressBar.setString(String.format("%s %d %s %d",
                currentLanguage[1], currentQuestion + 1, currentLanguage[3], questions.size()));
    }

    private void setupUI() {
        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Progress bar
        progressBar = new JProgressBar(0, questions.size() - 1);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString(String.format("%s 1 %s %d",
                currentLanguage[1], currentLanguage[3], questions.size()));
        progressBar.setFont(new Font("Roboto", Font.BOLD, 14));
        progressBar.setForeground(PRIMARY_COLOR);
        progressBar.setBackground(new Color(200, 200, 200)); // Light gray background
        progressBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(progressBar);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Question
        questionLabel = new JLabel();
        questionLabel.setFont(QUESTION_FONT);
        questionLabel.setForeground(TEXT_COLOR);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(questionLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Options
        optionButtons = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            JButton button = createStyledButton("");
            button.setMaximumSize(new Dimension(600, 50));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            final int index = i;
            button.addActionListener(e -> checkAnswer(index));
            optionButtons.add(button);
            mainPanel.add(button);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Score
        scoreLabel = new JLabel(currentLanguage[2] + ": 0");
        scoreLabel.setFont(SCORE_FONT);
        scoreLabel.setForeground(TEXT_COLOR);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(scoreLabel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private void initializeQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question(
                "Aká je normálna srdcová frekvencia u dospelých?",
                "What is the normal heart rate in adults?",
                new String[]{"40-50 úderov/min", "60-100 úderov/min", "100-120 úderov/min", "120-140 úderov/min"},
                new String[]{"40-50 beats/min", "60-100 beats/min", "100-120 beats/min", "120-140 beats/min"},
                1
        ));
        questions.add(new Question(
                "Ktorá z možností NIE JE krvná skupina?",
                "Which of these is NOT a blood type?",
                new String[]{"A+", "B-", "C+", "AB+"},
                new String[]{"A+", "B-", "C+", "AB+"},
                2
        ));
        questions.add(new Question(
                "Ktorý orgán produkuje inzulín?",
                "Which organ produces insulin?",
                new String[]{"Pečeň", "Pankreas", "Obličky", "Slezina"},
                new String[]{"Liver", "Pancreas", "Kidneys", "Spleen"},
                1
        ));
        questions.add(new Question(
                "Koľko kostí má dospelý človek?",
                "How many bones does an adult human have?",
                new String[]{"156", "186", "206", "226"},
                new String[]{"156", "186", "206", "226"},
                2
        ));
        questions.add(new Question(
                "Ktorý vitamín sa tvorí v koži pôsobením slnečného žiarenia?",
                "Which vitamin is produced in the skin when exposed to sunlight?",
                new String[]{"Vitamín A", "Vitamín C", "Vitamín D", "Vitamín E"},
                new String[]{"Vitamin A", "Vitamin C", "Vitamin D", "Vitamin E"},
                2
        ));
        questions.add(new Question(
                "Ako sa nazýva najväčšia tepna v ľudskom tele?",
                "What is the name of the largest artery in the human body?",
                new String[]{"Aorta", "Vena cava", "Pulmonálna artéria", "Karotída"},
                new String[]{"Aorta", "Vena cava", "Pulmonary artery", "Carotid"},
                0
        ));
        questions.add(new Question(
                "Koľko percent vody obsahuje ľudské telo?",
                "What percentage of water is in the human body?",
                new String[]{"40-50%", "50-60%", "60-70%", "70-80%"},
                new String[]{"40-50%", "50-60%", "60-70%", "70-80%"},
                2
        ));
        // Add more questions here...
    }

    private void showQuestion() {
        if (currentQuestion < questions.size()) {
            Question question = questions.get(currentQuestion);
            questionLabel.setText("<html><body style='width: 500px; text-align: center'>" +
                    currentLanguage[1] + " " + (currentQuestion + 1) + ": " +
                    question.getQuestion(currentLanguage) + "</body></html>");

            String[] options = question.getOptions(currentLanguage);
            for (int i = 0; i < options.length; i++) {
                optionButtons.get(i).setText(options[i]);
            }

            progressBar.setValue(currentQuestion);
            progressBar.setString(String.format("%s %d %s %d",
                    currentLanguage[1], currentQuestion + 1,
                    currentLanguage[3], questions.size()));
        } else {
            showFinalScore();
        }
    }

    private void checkAnswer(int selectedOption) {
        Question question = questions.get(currentQuestion);
        JDialog resultDialog = new JDialog(this, currentLanguage[12], true);
        resultDialog.setSize(300, 150);
        resultDialog.setLayout(new BorderLayout());
        resultDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Roboto", Font.BOLD, 16));

        if (selectedOption == question.getCorrectAnswer()) {
            score++;
            resultLabel.setText(currentLanguage[4]);
            resultLabel.setForeground(CORRECT_COLOR);
        } else {
            resultLabel.setText("<html><center>" + currentLanguage[5] + "<br>" +
                    currentLanguage[6] + "<br>" +
                    question.getOptions(currentLanguage)[question.getCorrectAnswer()] +
                    "</center></html>");
            resultLabel.setForeground(INCORRECT_COLOR);
        }

        JButton closeButton = createStyledButton(currentLanguage[11]);
        closeButton.addActionListener(e -> {
            resultDialog.dispose();
            currentQuestion++;
            showQuestion();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(closeButton);

        resultDialog.add(resultLabel, BorderLayout.CENTER);
        resultDialog.add(buttonPanel, BorderLayout.SOUTH);
        resultDialog.setLocationRelativeTo(this);
        resultDialog.setVisible(true);
    }

    private void showFinalScore() {
        JDialog finalDialog = new JDialog(this, currentLanguage[7], true);
        finalDialog.setSize(300, 200);
        finalDialog.setLayout(new BorderLayout());
        finalDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        double percentage = (score * 100.0) / questions.size();
        JLabel finalLabel = new JLabel("<html><center>" +
                currentLanguage[8] + "<br><br>" +
                currentLanguage[9] + " " + score + "<br>" +
                currentLanguage[10] + " " + String.format("%.1f", percentage) + "%</center></html>",
                SwingConstants.CENTER);
        finalLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        finalLabel.setForeground(TEXT_COLOR);

        JButton closeButton = createStyledButton(currentLanguage[11]);
        closeButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(closeButton);

        finalDialog.add(finalLabel, BorderLayout.CENTER);
        finalDialog.add(buttonPanel, BorderLayout.SOUTH);
        finalDialog.setLocationRelativeTo(this);
        finalDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new App().setVisible(true);
        });
    }
}