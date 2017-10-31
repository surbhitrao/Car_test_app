package com.example.surbhit.assesment_angel;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AsyncSaveTodos extends AsyncTask< DB_Task_Parameters , Void, TextView > {

    // returns string version of current timestamp
    String GetCurrentTimeStamp(){
        long date = System.currentTimeMillis();
        return Long.toString(date);
    }

    public AsyncSaveTodos(TextView tv_start_save , TextView tv_end_save ) {
        super();
        // do stuff
        tv_start_save.setText( "Start Save : " + GetCurrentTimeStamp() );
        tv_end_save.setText("---");
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

    }
    @Override
    protected TextView doInBackground(DB_Task_Parameters... data)
    {
        JSONArray todos_arr = data[0].jsonArray ;
        DB_helper DBH = data[0].dbHepler ;
        TextView tv_end_save = data[0].tv_end_save ;
        final TextView tv_rows_in_db = data[0].tv_rows_in_db ;
        MainActivity myact = data[0].myact ;
        JSONObject todoObj ;
        int id , uid ;
        String title , completed ;

        try {
            for (int l = 0; l < todos_arr.length(); l++) {


                todoObj = todos_arr.getJSONObject(l);
                id = todoObj.getInt("id");
                uid = todoObj.getInt("userId");
                title = todoObj.getString("title");
                completed = todoObj.getString("completed");
                DBH.insertTodo(id, uid, title, completed );

                final int i = DBH.getNoOfRows("todos","title")  ;
                myact.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_rows_in_db.setText("Rows In DB : " + Integer.toString(i));
                    }
                });
                SystemClock.sleep(100);
            }
        }catch (JSONException e) {
            // If an error occurs, this prints the error to the log
            e.printStackTrace();
        }

        //Record method
        return tv_end_save  ;
    }

    @Override
    protected void onPostExecute(TextView tv_end_save )
    {
        super.onPostExecute(tv_end_save);
        tv_end_save.setText("End Save : " + GetCurrentTimeStamp());
        Log.i("Async Task", "Todos Done!");
    }

}

