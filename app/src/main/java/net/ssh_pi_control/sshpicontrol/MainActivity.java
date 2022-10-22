package net.ssh_pi_control.sshpicontrol;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    TextView t1;
    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = (TextView) findViewById(R.id.textView);
        t1.setText("asssssssssssssssss");

    }

    @SuppressLint("StaticFieldLeak")
    public void doIt (View view) {

        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    response = executeRemoteCommand("pi", "","192.168.8.105", 22);
                    //t1.setText("output");
                    //try{Thread.sleep(1000);}catch(Exception ee){}
                    //TextView textView1_2 = (TextView)findViewById(R.id.textView);
                    //textView1_2.setText(response);
                    //textView1_2.setText("dead3");
                    //view.invalidate();
                    //t1.setText("done");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                //TextView textView1_2 = (TextView)findViewById(R.id.textView);
                //textView1_2.setText(response);
                //textView1_2.setText("dead2");
                t1.setText(response);
            }

        }.execute(1);

        //t1.setText("dead");

    }

    public String executeRemoteCommand(String username,String password,String hostname,int port)
            throws Exception {

        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
        channelssh.setCommand("date +%T");
        //channelssh.setCommand("cd ~/python_scripts && python3 makefile.py");
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        channelssh.setOutputStream(responseStream);
        channelssh.connect();

        while (channelssh.isConnected()) {
            Thread.sleep(100);
        }

        String responseString = new String(responseStream.toByteArray());

        Log.i("asd", responseString);

        //TextView txtView = findViewById(R.id.textView);
        //txtView.setText(responseString);
        //txtView.invalidate();
        //t1.setText("working...");
        //view.invalidate();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        //channelssh.setCommand("touch /home/pi/hello_moana");
        //channelssh.setCommand("cd ~/python_scripts && python3 makefile.py");
        //channelssh.setCommand("python3 makefile.py");

        //channelssh.connect();
        session.disconnect();
        channelssh.disconnect();

        //TextView txtView = findViewById(R.id.textView);
        //txtView.setText("working done");


        //return baos.toString();
        return responseString;
    }

    //@Override
    //protected void onPostExecute(Void unused) {

    //}
}