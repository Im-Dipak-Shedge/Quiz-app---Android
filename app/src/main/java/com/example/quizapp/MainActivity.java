package com.example.quizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;




public class MainActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup answersRadioGroup;
    private Button submitButton;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.questionTextView);
        answersRadioGroup = findViewById(R.id.answersRadioGroup);
        submitButton = findViewById(R.id.submitButton);

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();

        // Add some sample questions if the database is empty
        if (questionList.isEmpty()) {
            dbHelper.addQuestion(new Question("Sample Question 1", "Option 1", "Option 2", "Option 3", "Option 4", 1));
            dbHelper.addQuestion(new Question("Sample Question 2", "Option A", "Option B", "Option C", "Option D", 2));
            dbHelper.addQuestion(new Question(
                    "What is the capital of France?",
                    "London",
                    "Paris",
                    "Berlin",
                    "Madrid",
                    2)); // Answer is option B (Paris)

            dbHelper.addQuestion(new Question(
                    "Who painted the Mona Lisa?",
                    "Leonardo da Vinci",
                    "Pablo Picasso",
                    "Vincent van Gogh",
                    "Michelangelo",
                    1)); // Answer is option A (Leonardo da Vinci)

            dbHelper.addQuestion(new Question(
                    "Which of the following is a primitive data type in Java?",
                    "String",
                    "Integer",
                    "Float",
                    "boolean",
                    4)); // Answer is option D (boolean)

            questionList = dbHelper.getAllQuestions();
        }


        loadQuestion();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = answersRadioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    int selectedAnswerIndex = answersRadioGroup.indexOfChild(selectedRadioButton);
                    checkAnswer(selectedAnswerIndex);
                    currentQuestionIndex++;
                    if (currentQuestionIndex < questionList.size()) {
                        loadQuestion();
                    } else {
                        Toast.makeText(MainActivity.this, "Quiz Finished!", Toast.LENGTH_SHORT).show();
                        currentQuestionIndex = 0;  // Reset for demonstration purposes
                        loadQuestion();  // Reload the first question
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadQuestion() {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        questionTextView.setText(currentQuestion.getQuestion());
        ((RadioButton) answersRadioGroup.getChildAt(0)).setText(currentQuestion.getOption1());
        ((RadioButton) answersRadioGroup.getChildAt(1)).setText(currentQuestion.getOption2());
        ((RadioButton) answersRadioGroup.getChildAt(2)).setText(currentQuestion.getOption3());
        ((RadioButton) answersRadioGroup.getChildAt(3)).setText(currentQuestion.getOption4());
        answersRadioGroup.clearCheck();
    }

    private void checkAnswer(int selectedAnswerIndex) {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        if (selectedAnswerIndex + 1 == currentQuestion.getAnswerNr()) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
        }
    }
}
