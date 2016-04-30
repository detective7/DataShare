package com.example.ys.datashare.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ys.datashare.R;

import java.io.File;
import java.util.ArrayList;

public class FileFinderActivity extends ListActivity {

    private ArrayList<String> items = null;
    private ArrayList<String> paths = null;
    private String rootPath = "/";
    private TextView mPath;
    private File currentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_finder);

        mPath = (TextView)findViewById(R.id.mPath);
        mPath.setTextColor(Color.WHITE);
        getFileDir(rootPath);
    }

    private void getFileDir(String filePath) {
        mPath.setText("当前目录："+filePath);

        items = new ArrayList<String>();
        paths = new ArrayList<String>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        if(!filePath.equals(rootPath)) {
            items.add("返回主目录");
            paths.add(rootPath);
            items.add("返回上层目录");
            paths.add(file.getParent());
        }
        for(File fileTemp :files) {
            items.add(fileTemp.getName());
            paths.add(fileTemp.getPath());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FileFinderActivity.this,R.layout.file_now,items);
        setListAdapter(adapter);


    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        currentFile = new File(paths.get(position));
        if(currentFile.canRead()) {
            if(currentFile.isDirectory()) {
                getFileDir(paths.get(position));
            }else {
                Intent intent = new Intent();
                intent.putExtra("filename",currentFile.getName());
                intent.putExtra("filepath",paths.get(position));
                setResult(RESULT_OK,intent);
                finish();
            }
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Message")
                    .setMessage("權限不足~")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

        }
    }
}
