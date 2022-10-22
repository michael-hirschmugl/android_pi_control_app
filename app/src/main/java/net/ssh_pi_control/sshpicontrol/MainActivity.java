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
        t1.setText("");

    }

    @SuppressLint("StaticFieldLeak")
    public void doIt (View view) {

        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    response = executeRemoteCommand("pi", "","192.168.8.105", 22);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                t1.setText(response);
            }

        }.execute(1);
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

        Log.i("response:", responseString);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        session.disconnect();
        channelssh.disconnect();

        return responseString;
    }

}