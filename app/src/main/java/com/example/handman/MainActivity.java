package com.example.handman;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView wordTextView;
    private TextView livesTextView;
    private GridLayout lettersGridLayout;
    private String[] words = {"ANDROID", "JAVA", "KOTLIN", "STUDIO", "HANGMAN"};
    private String currentWord;
    private StringBuilder displayedWord;
    private int lives;
    private HashSet<Character> guessedLetters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordTextView = findViewById(R.id.wordTextView);
        livesTextView = findViewById(R.id.livesTextView);
        lettersGridLayout = findViewById(R.id.lettersGridLayout);

        startNewGame();
        createLetterButtons();
    }

    private void startNewGame() {
        Random random = new Random();
        currentWord = words[random.nextInt(words.length)];
        displayedWord = new StringBuilder();
        for (int i = 0; i < currentWord.length(); i++) {
            displayedWord.append("_");
        }
        lives = 6;
        guessedLetters = new HashSet<>();
        updateUI();
        enableLetterButtons();
    }

    private void createLetterButtons() {
        lettersGridLayout.removeAllViews();
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            Button button = new Button(this);
            button.setText(String.valueOf(letter));
            char finalLetter = letter;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLetterClicked(button, finalLetter);
                }
            });
            lettersGridLayout.addView(button);
        }
    }

    private void onLetterClicked(Button button, char letter) {
        if (guessedLetters.contains(letter)) return;
        guessedLetters.add(letter);
        button.setEnabled(false);

        boolean correctGuess = false;
        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == letter) {
                displayedWord.setCharAt(i, letter);
                correctGuess = true;
            }
        }

        if (correctGuess) {
            if (displayedWord.toString().equals(currentWord)) {
                wordTextView.setText("Wygrałeś!");
                disableLetterButtons();
                showPlayAgainDialog();
            } else {
                updateUI();
            }
        } else {
            lives--;
            if (lives <= 0) {
                wordTextView.setText("Przegrałeś! Poprawny wyraz to: " + currentWord);
                disableLetterButtons();
                showPlayAgainDialog();
            } else {
                updateUI();
            }
        }
    }

    private void updateUI() {
        wordTextView.setText(displayedWord.toString());
        livesTextView.setText("Lives: " + lives);
    }

    private void disableLetterButtons() {
        for (int i = 0; i < lettersGridLayout.getChildCount(); i++) {
            lettersGridLayout.getChildAt(i).setEnabled(false);
        }
    }

    private void enableLetterButtons() {
        for (int i = 0; i < lettersGridLayout.getChildCount(); i++) {
            lettersGridLayout.getChildAt(i).setEnabled(true);
        }
    }

    private void showPlayAgainDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Kolejna gra?")
                .setMessage("Chciałbyś zagrać jeszcze raz?")
                .setPositiveButton("Tak", (dialog, which) -> startNewGame())
                .setNegativeButton("Nie", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
}
