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

    TextView t1, t2;
    String response;
    String user = "pi";
    String pass = "";
    String hostname = "192.168.8.105";
    Integer port = 22;

    String commandProcess1 = "date +%T";
    String commandProcess2 = "cd ~/python_scripts && python3 makefile.py";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = (TextView) findViewById(R.id.TextViewButton1);
        t2 = (TextView) findViewById(R.id.TextViewButton2);

    }

    @SuppressLint("StaticFieldLeak")
    public void processButton1 (View view) {

        String command = commandProcess1;

        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    response = executeRemoteCommand(user, pass,hostname, port, command);
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

    @SuppressLint("StaticFieldLeak")
    public void processButton2 (View view) {

        String command = commandProcess2;

        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    response = executeRemoteCommand(user, pass,hostname, port, command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                t2.setText(response);
            }

        }.execute(1);
    }

    public String executeRemoteCommand(String username,String password,String hostname,int port, String command)
            throws Exception {

        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
        channelssh.setCommand(command);
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