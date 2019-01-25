package com.sn_g2.greenhouse;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.sn_g2.sensornetwork.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //10.49.1.26
    private PahoMqttClient pahoMqttClient;
    private MqttAndroidClient mqttAndroidClient;
    private String topic = "g4/answer/#";
    private int idRoom = -1;
    private TextView tv;
    private ListView lv1;
    MqttMessage message;



    private Calendar calendar;

    private String clientID;

    private TextView tvHost;
    private ArrayList<String> listItems=new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    Button tempbtn;
    Button humbtn;
    Button presbtn;
    Button batbtn;
    Button accbtn;
    Toolbar toolbar;

    int pirCount=0;
    int ffcount=0;

    private boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Random r = new Random();
        int i1 = r.nextInt(1000 - 1) + 1;
        clientID="client_"+i1;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Gateway");

        tvHost = (TextView) findViewById(R.id.hostET);

        tv= (TextView)findViewById(R.id.tv);

        tempbtn = (Button)findViewById(R.id.tempbtn);
        tempbtn.setOnClickListener(this);
        humbtn = (Button)findViewById(R.id.humbtn);
        humbtn.setOnClickListener(this);
        presbtn = (Button)findViewById(R.id.presbtn);
        presbtn.setOnClickListener(this);
        batbtn = (Button)findViewById(R.id.batbtn);
        batbtn.setOnClickListener(this);
        accbtn = (Button)findViewById(R.id.accbtn);
        accbtn.setOnClickListener(this);

        lv1= (ListView)findViewById(R.id.lv1);
        lv1.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
        //lv1.setOnItemClickListener(this);
        adapter = new ArrayAdapter(this,
                // android.R.layout.simple_list_item_1,
                android.R.layout.simple_list_item_checked,
                // android.R.layout.simple_list_item_single_choice,
                // android.R.layout.simple_list_item_multiple_choice,
                listItems);
        lv1.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.connect) {
            try {
                if (connected){
                    connected=false;
                    mqttAndroidClient.disconnect();
                }else{
                    getHost();
                }
            }catch(MqttSecurityException e){
                e.printStackTrace();
            }catch(MqttException e){
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return getTimeString(value);
        }

        private String getTimeString(float seconds){
            int sec = (int)seconds%60;
            int min= (int)seconds/60;
            int hour = (int)min/60;
            min = (int)min%60;
            return String.format("%02d:%02d:%02d", hour,min, sec);
        }
    }

    private void connectMQTT(){
        pahoMqttClient = new PahoMqttClient();
        mqttAndroidClient = pahoMqttClient.getMqttClient(getApplicationContext(), "tcp://"+ tvHost.getText().toString()+":1883", clientID);

        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                try {
                    mqttAndroidClient.subscribe(topic, 0);
                    toolbar.getMenu().findItem(R.id.connect).setIcon(android.R.drawable.button_onoff_indicator_on);
                    connected=true;
                }catch(MqttSecurityException e){
                    e.printStackTrace();
                }catch(MqttException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                toolbar.getMenu().findItem(R.id.connect).setIcon(android.R.drawable.button_onoff_indicator_off);
                if(connected){
                    connectMQTT();
                }
            }

            @Override
            public void messageArrived(String topicReceived, MqttMessage mqttMessage) throws Exception {
                calendar=Calendar.getInstance();
                String payload = new String(mqttMessage.getPayload());
                String text="";
                tv.setText(" ");

                if(topicReceived.equals("g4/answer/speciesList")){
                    payload = payload.replace("[","");
                    payload = payload.replace("]","");
                    payload = payload.replace("\"","");
                    payload = payload.replace("\r","");
                    payload = payload.replace("\n","");
                    String[] splitted = payload.split(",");
                    for(int i=0; i<splitted.length; i++){
                        listItems.add(splitted[i]);
                        adapter.notifyDataSetChanged();
                        tv.setText(text+"\n"+"-->"+topicReceived+ " - " + splitted[i]+"\n");
                        text = tv.getText().toString();
                    }
                }else if(topicReceived.equals("g4/answer/roomsList")){
                    payload = payload.replace("[","");
                    payload = payload.replace("]","");
                    payload = payload.replace("\"","");
                    payload = payload.replace("\r","");
                    payload = payload.replace("\n","");
                    String[] splitted = payload.split(",");
                    for(int i=0; i<splitted.length; i++){
                        listItems.add(splitted[i]);
                        adapter.notifyDataSetChanged();
                        tv.setText(text+"\n"+"-->"+topicReceived+ " - " + splitted[i]+"\n");
                        text = tv.getText().toString();
                    }
                }else if (topicReceived.equals("g4/answer/test")){

                }



            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tempbtn:
                message = new MqttMessage("{\"command\":\"getSpecies\"}".getBytes());
                message.setQos(0);
                message.setRetained(false);
                try {
                    mqttAndroidClient.publish("g4/commands", message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.humbtn:
                message = new MqttMessage("{\"command\":\"getRooms\"}".getBytes());
                message.setQos(0);
                message.setRetained(false);
                try {
                    mqttAndroidClient.publish("g4/commands", message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.presbtn:
                message = new MqttMessage("{\"command\":\"test\"}".getBytes());
                message.setQos(0);
                message.setRetained(false);
                try {
                    mqttAndroidClient.publish("g4/commands", message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.batbtn:
                String mes="{\"command\":\"changeLimit\", \"value\":"+idRoom+"}";
                message = new MqttMessage(mes.getBytes());
                message.setQos(0);
                message.setRetained(false);
                try {
                    mqttAndroidClient.publish("g4/commands", message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.accbtn:

                break;
        }
    }

    private void getHost(){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.pop_up_host, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        alert.setTitle("Host");
        alert.setView(mView);
        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        userInputDialogEditText.setText(tvHost.getText().toString());
        alert
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String name = userInputDialogEditText.getText().toString();
                        if(name.length()==0){
                            name = tvHost.getText().toString();
                        }
                        tvHost.setText(name);
                        connectMQTT();
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {

                            }
                        });

        AlertDialog alertDialogAndroid = alert.create();
        alertDialogAndroid.show();
    }

}
