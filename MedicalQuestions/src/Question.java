class Question {
    private final String questionSK;
    private final String questionEN;
    private final String[] optionsSK;
    private final String[] optionsEN;
    private final int correctAnswer;

    public Question(String questionSK, String questionEN,
                    String[] optionsSK, String[] optionsEN,
                    int correctAnswer) {
        this.questionSK = questionSK;
        this.questionEN = questionEN;
        this.optionsSK = optionsSK;
        this.optionsEN = optionsEN;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion(String[] currentLanguage) {
        return (currentLanguage == App.SK) ? questionSK : questionEN;
    }

    public String[] getOptions(String[] currentLanguage) {
        return (currentLanguage == App.SK) ? optionsSK : optionsEN;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}