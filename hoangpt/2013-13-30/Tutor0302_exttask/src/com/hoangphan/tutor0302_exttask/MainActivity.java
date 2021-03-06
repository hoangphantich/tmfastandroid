package com.hoangphan.tutor0302_exttask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

  private static final int GO_TO_SAVE_FROM_MAIN = 0;
  private EditText txtWork;
  private EditText txtHour;
  private EditText txtMinute;
  private ListView list;
  private ArrayList<Work> listWorks;
  private ArrayAdapter<Work> adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    //read from list
    File sdcard = Environment.getExternalStorageDirectory();
    File file = new File(sdcard.getAbsoluteFile()+"/Music/list.txt");
    listWorks = new ArrayList<Work>();
    try {
		ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file));
		listWorks = (ArrayList<Work>)  reader.readObject();
    } catch (Exception e) {
		e.printStackTrace();
	}
    
    txtWork = (EditText) findViewById(R.id.txtWork);
    txtHour = (EditText) findViewById(R.id.txtHour);
    txtMinute = (EditText) findViewById(R.id.txtMinute);
    
    //build list view
    list = (ListView) findViewById(R.id.list);
    
    //need adapter for input data and layout
    adapter = new WorkAdapter(this, R.layout.work_custom, listWorks);
    list.setAdapter(adapter);
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position,
          long id) {
        WorkView wv = (WorkView) view;
        wv.checkBox.setChecked(true);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
  
  @TargetApi(19)
  public void onAddWork(View v){
    //validate
    if(txtWork.getText().toString().equals("")){
      AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
      builder.setTitle("Error info provided");
      builder.setMessage("Not blank for either work name, hour or minute.");
      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        
        @Override
        public void onClick(DialogInterface dialog, int which) {
          System.out.println("OK click");
        }
      });
      builder.show();
    } else {
      Work work = new Work();
      work.setName(txtWork.getText().toString());
      work.setHour(Integer.parseInt(txtHour.getText().toString()));
      work.setMinute(Integer.parseInt(txtMinute.getText().toString()));
      listWorks.add(work);
      Collections.sort(listWorks, new Comparator<Work>() {

        @Override
        public int compare(Work thisWork, Work thatWork) {
          int thisTime = thisWork.getHour()*60 + thisWork.getMinute();
          int thatTime = thatWork.getHour()*60 + thatWork.getMinute();
          return Integer.compare(thisTime, thatTime);
        }
      });
      Log.d("add", work.toString());
      adapter.notifyDataSetChanged();
      
      //reset form
      txtWork.setText("");
      txtHour.setText("");
      txtMinute.setText("");
      
      //write to file
      File sdcard = Environment.getExternalStorageDirectory();
      File file = new File(sdcard.getAbsoluteFile()+"/Music/list.txt");
      ObjectOutputStream writer =  null;
      try {
		writer = new ObjectOutputStream(new FileOutputStream(file));
		writer.writeObject(listWorks);
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
  }

  
  public void onSaveWork(View v){
    Intent infoInt = new Intent(this, InfoActivity.class);
    startActivityForResult(infoInt, GO_TO_SAVE_FROM_MAIN);
  }
}
