package com.hoangphan.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {

  private static final String TAG = "Sudoku";

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // Set up click listeners for all the buttons
    View continueButton = findViewById(R.id.continue_button);
    continueButton.setOnClickListener(this);
    View newButton = findViewById(R.id.new_button);
    newButton.setOnClickListener(this);
    View aboutButton = findViewById(R.id.about_button);
    aboutButton.setOnClickListener(this);
    View exitButton = findViewById(R.id.exit_button);
    exitButton.setOnClickListener(this);
  }

  // ...

  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.about_button:
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
        break;
      // More buttons go here (if any) ...
      case R.id.new_button:
        openNewGameDialog();
        break;
      case R.id.exit_button:
        finish();
        break;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.settings:
        startActivity(new Intent(this, PrefsActivity.class));
        return true;
      // More items go here (if any) ...
    }
    return false;
  }

  /** Ask the user what difficulty level they want */
  private void openNewGameDialog() {
    new AlertDialog.Builder(this)
      .setTitle(R.string.new_game_title)
      .setItems(R.array.difficulty,
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface,
                              int i) {
            startGame(i);
          }
        })
      .show();
  }

  /** Start a new game with the given difficulty level */
  private void startGame(int gameDifficulty) {
    Log.d(TAG, "clicked on " + gameDifficulty);

    // Start game here...
    Intent i = new Intent(this, GameActivity.class);
    i.putExtra(GameActivity.DIFFICULTY, gameDifficulty);
    startActivity(i);

  }
}
