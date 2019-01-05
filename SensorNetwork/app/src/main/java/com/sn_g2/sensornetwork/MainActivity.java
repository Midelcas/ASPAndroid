package com.sn_g2.sensornetwork;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String topic= "g2/channels/666894/publish/J8J79SZWTMYLVK09";
    private PahoMqttClient pahoMqttClient;
    private MqttAndroidClient mqttAndroidClient;
    private LineChart tempChart;
    private LineChart humChart;
    private LineChart pressChart;
    private LineChart batChart;
    private LineChart accChart;

    private Calendar calendar;
    private List<Entry> tempEntry1;
    private LineDataSet dataSet1;
    private LineData lineData1;
    private List<Entry> humEntry1;
    private LineDataSet dataSet2;
    private LineData lineData2;
    private List<Entry> pressEntry1;
    private LineDataSet dataSet3;
    private LineData lineData3;
    private List<Entry> batEntry1;
    private LineDataSet dataSet4;
    private LineData lineData4;
    private List<Entry> XEntry1;
    private LineDataSet dataSet5;
    private LineData lineData5;
    private List<Entry> YEntry1;
    private LineDataSet dataSet6;
    private LineData lineData6;
    private List<Entry> ZEntry1;
    private LineDataSet dataSet7;

    private EditText hostET;
    private Spinner topicS;
    Button tempbtn;
    Button humbtn;
    Button presbtn;
    Button batbtn;
    Button accbtn;
    Toolbar toolbar;

    int pirCount=0;
    int ffcount=0;

    private boolean connected = false;
    private final static String[] topics = { "g2/channels/666894/publish/J8J79SZWTMYLVK09", "g2/channels/648459/publish/44GWV2IQ8OU9Z7X3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Gateway");

        hostET = (EditText)findViewById(R.id.hostET);
        topicS = (Spinner) findViewById(R.id.topicS);
        topicS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(topicS.getSelectedItem().toString().equals(topics[0])){
                    toolbar.setTitle("Gateway");
                }else{
                    toolbar.setTitle("Node");
                }
                if(!topic.equals(topicS.getSelectedItem().toString())){
                    tempChart.clear();
                    tempEntry1.clear();
                    humChart.clear();
                    humEntry1.clear();
                    pressChart.clear();
                    pressEntry1.clear();
                    batChart.clear();
                    batEntry1.clear();
                    accChart.clear();
                    XEntry1.clear();
                    YEntry1.clear();
                    ZEntry1.clear();
                    toolbar.getMenu().findItem(R.id.pir).setIcon(R.drawable.pir_off);
                    pirCount=0;
                    ffcount=0;
                    toolbar.getMenu().findItem(R.id.ff).setIcon(R.drawable.ff_off);
                    toolbar.getMenu().findItem(R.id.battery).setIcon(R.drawable.bat_ok);
                    try {
                        if(connected){
                            mqttAndroidClient.disconnect();
                        }
                    }catch(MqttSecurityException e){
                        e.printStackTrace();
                    }catch(MqttException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, topics);
        topicS.setAdapter(adapter);

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

        tempChart = (LineChart)findViewById(R.id.field1);
        humChart = (LineChart)findViewById(R.id.field2);
        pressChart = (LineChart)findViewById(R.id.field3);
        batChart = (LineChart)findViewById(R.id.field4);
        accChart = (LineChart)findViewById(R.id.field5);

        tempEntry1 = new ArrayList<Entry>();
        humEntry1 = new ArrayList<Entry>();
        pressEntry1 = new ArrayList<Entry>();
        batEntry1 = new ArrayList<Entry>();
        XEntry1 = new ArrayList<Entry>();
        YEntry1 = new ArrayList<Entry>();
        ZEntry1 = new ArrayList<Entry>();
        //entries.add(new Entry(0,0));
        dataSet1=new LineDataSet(tempEntry1, "Temperature");
        dataSet2=new LineDataSet(humEntry1, "Humidity");
        dataSet3=new LineDataSet(pressEntry1, "Pressure");
        dataSet4=new LineDataSet(batEntry1, "Battery");
        dataSet5=new LineDataSet(XEntry1, "X");
        dataSet5.setColor(Color.RED);
        dataSet6=new LineDataSet(YEntry1, "Y");
        dataSet6.setColor(Color.GREEN);
        dataSet7=new LineDataSet(ZEntry1, "Z");
        dataSet7.setColor(Color.BLUE);


        tempChart.getDescription().setEnabled(false);
        humChart.getDescription().setEnabled(false);
        pressChart.getDescription().setEnabled(false);
        batChart.getDescription().setEnabled(false);
        accChart.getDescription().setEnabled(false);

        dataSet1.setDrawValues(false);
        dataSet2.setDrawValues(false);
        dataSet3.setDrawValues(false);
        dataSet4.setDrawValues(false);
        dataSet5.setDrawValues(false);
        dataSet6.setDrawValues(false);
        dataSet7.setDrawValues(false);

        tempChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis1 = tempChart.getXAxis();
        xAxis1.setValueFormatter(new MyXAxisValueFormatter());
        humChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis2 = humChart.getXAxis();
        xAxis2.setValueFormatter(new MyXAxisValueFormatter());
        pressChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis3 = pressChart.getXAxis();
        xAxis3.setValueFormatter(new MyXAxisValueFormatter());
        batChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis4 = batChart.getXAxis();
        xAxis4.setValueFormatter(new MyXAxisValueFormatter());
        accChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis5 = accChart.getXAxis();
        xAxis5.setValueFormatter(new MyXAxisValueFormatter());

        lineData1=new LineData(dataSet1);
        lineData2=new LineData(dataSet2);
        lineData3=new LineData(dataSet3);
        lineData4=new LineData(dataSet4);
        lineData5=new LineData();
        lineData5.addDataSet(dataSet5);
        lineData5.addDataSet(dataSet6);
        lineData5.addDataSet(dataSet7);

        //lChart.setData(lineData);

        //lChart.animateX(2000);
        //connectMQTT();

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
                    connectMQTT();
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
        mqttAndroidClient = pahoMqttClient.getMqttClient(getApplicationContext(), "tcp://"+hostET.getText().toString()+":1883", Constants.CLIENT_ID);

        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                try {
                    topic=topicS.getSelectedItem().toString();
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
                if(connected){
                    connectMQTT();
                }else{
                    toolbar.getMenu().findItem(R.id.connect).setIcon(android.R.drawable.button_onoff_indicator_off);
                }
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                calendar=Calendar.getInstance();
                String payload = new String(mqttMessage.getPayload());
                String[] values = payload.split("&|=");
                for(int i=0; i< values.length; i++){
                    if(values[i].equals("field1")){
                        lineData1.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData1.getIndexOfDataSet(dataSet1));
                        if(tempChart.getLineData()==null){
                            tempChart.setData(lineData1);
                        }
                        lineData1.notifyDataChanged();
                        tempChart.notifyDataSetChanged();
                        tempChart.resetViewPortOffsets();
                        tempChart.invalidate(); // refresh
                    }else if(values[i].equals("field2")){
                        lineData2.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData2.getIndexOfDataSet(dataSet2));
                        if(humChart.getLineData()==null){
                            humChart.setData(lineData2);
                        }
                        lineData2.notifyDataChanged();
                        humChart.notifyDataSetChanged();
                        humChart.resetViewPortOffsets();
                        humChart.invalidate(); // refresh
                    }else if(values[i].equals("field3")){
                        lineData3.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData3.getIndexOfDataSet(dataSet3));
                        if(pressChart.getLineData()==null){
                            pressChart.setData(lineData3);
                        }
                        lineData3.notifyDataChanged();
                        pressChart.notifyDataSetChanged();
                        pressChart.resetViewPortOffsets();
                        pressChart.invalidate(); // refresh
                    }else if(values[i].equals("field4")){
                        lineData4.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData4.getIndexOfDataSet(dataSet4));
                        if(batChart.getLineData()==null){
                            batChart.setData(lineData4);
                        }
                        if(Float.parseFloat(values[i+1])>20){
                            toolbar.getMenu().findItem(R.id.battery).setIcon(R.drawable.bat_ok);
                        }
                        lineData4.notifyDataChanged();
                        batChart.notifyDataSetChanged();
                        batChart.resetViewPortOffsets();
                        batChart.invalidate(); // refresh
                    }else if(values[i].equals("field5")){
                        lineData5.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData5.getIndexOfDataSet(dataSet5));
                        if(accChart.getLineData()==null){
                            accChart.setData(lineData5);
                        }
                        lineData5.notifyDataChanged();
                        accChart.notifyDataSetChanged();
                        accChart.resetViewPortOffsets();
                        accChart.invalidate(); // refresh
                    }else if(values[i].equals("field6")){
                        lineData5.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData5.getIndexOfDataSet(dataSet6));
                        if(accChart.getLineData()==null){
                            accChart.setData(lineData5);
                        }
                        lineData5.notifyDataChanged();
                        accChart.notifyDataSetChanged();
                        accChart.resetViewPortOffsets();
                        accChart.invalidate(); // refresh
                    }else if(values[i].equals("field7")){
                        lineData5.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData5.getIndexOfDataSet(dataSet7));
                        if(accChart.getLineData()==null){
                            accChart.setData(lineData5);
                        }
                        lineData5.notifyDataChanged();
                        accChart.notifyDataSetChanged();
                        accChart.resetViewPortOffsets();
                        accChart.invalidate(); // refresh
                    }else if(values[i].equals("field8")){
                        switch(Integer.parseInt(values[i+1])){
                            case 1:
                                toolbar.getMenu().findItem(R.id.pir).setIcon(R.drawable.pir_on);
                                pirCount=3;
                                break;
                            case 2:
                                toolbar.getMenu().findItem(R.id.ff).setIcon(R.drawable.ff_on);
                                ffcount=3;
                                break;
                            case 3:
                                toolbar.getMenu().findItem(R.id.battery).setIcon(R.drawable.bat_nok);
                                break;
                        }
                    }
                }


                if(ffcount > 0){
                    ffcount--;
                }else{
                    toolbar.getMenu().findItem(R.id.ff).setIcon(R.drawable.ff_off);
                }
                if (pirCount > 0) {
                    pirCount--;
                }else{
                    toolbar.getMenu().findItem(R.id.pir).setIcon(R.drawable.pir_off);
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
                if(tempChart.getVisibility()==View.GONE){
                    tempbtn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_up_float,0,0,0);
                    tempChart.setVisibility(View.VISIBLE);
                }else{
                    tempbtn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_down_float,0,0,0);
                    tempChart.setVisibility(View.GONE);
                }

                break;
            case R.id.humbtn:
                if(humChart.getVisibility()==View.GONE){
                    humbtn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_up_float,0,0,0);
                    humChart.setVisibility(View.VISIBLE);
                }else{
                    humbtn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_down_float,0,0,0);
                    humChart.setVisibility(View.GONE);
                }
                break;
            case R.id.presbtn:
                if(pressChart.getVisibility()==View.GONE){
                    presbtn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_up_float,0,0,0);
                    pressChart.setVisibility(View.VISIBLE);
                }else{
                    presbtn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_down_float,0,0,0);
                    pressChart.setVisibility(View.GONE);
                }
                break;
            case R.id.batbtn:
                if(batChart.getVisibility()==View.GONE){
                    batbtn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_up_float,0,0,0);
                    batChart.setVisibility(View.VISIBLE);
                }else{
                    batbtn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_down_float,0,0,0);
                    batChart.setVisibility(View.GONE);
                }
                break;
            case R.id.accbtn:
                if(accChart.getVisibility()==View.GONE){
                    accbtn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_up_float,0,0,0);
                    accChart.setVisibility(View.VISIBLE);
                }else{
                    accbtn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_down_float,0,0,0);
                    accChart.setVisibility(View.GONE);
                }
                break;
        }
    }

}
