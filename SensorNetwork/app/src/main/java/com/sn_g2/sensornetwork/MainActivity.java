package com.sn_g2.sensornetwork;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class MainActivity extends AppCompatActivity {

    private PahoMqttClient pahoMqttClient;
    private MqttAndroidClient mqttAndroidClient;
    private LineChart field1;
    private LineChart field2;
    private LineChart field3;
    private LineChart field4;
    private LineChart field5;
    private LineChart field6;
    private LineChart field7;
    private LineChart field8;

    private List<Entry> entries1;
    private LineDataSet dataSet1;
    private LineData lineData1;
    private Calendar calendar;
    private List<Entry> entries2;
    private LineDataSet dataSet2;
    private LineData lineData2;
    private List<Entry> entries3;
    private LineDataSet dataSet3;
    private LineData lineData3;
    private List<Entry> entries4;
    private LineDataSet dataSet4;
    private LineData lineData4;
    private List<Entry> entries5;
    private LineDataSet dataSet5;
    private LineData lineData5;
    private List<Entry> entries6;
    private LineDataSet dataSet6;
    private LineData lineData6;
    private List<Entry> entries7;
    private LineDataSet dataSet7;
    private LineData lineData7;
    private List<Entry> entries8;
    private LineDataSet dataSet8;
    private LineData lineData8;

    private boolean first= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        field1 = (LineChart)findViewById(R.id.field1);
        field2 = (LineChart)findViewById(R.id.field2);
        field3 = (LineChart)findViewById(R.id.field3);
        field4 = (LineChart)findViewById(R.id.field4);
        field5 = (LineChart)findViewById(R.id.field5);

        entries1 = new ArrayList<Entry>();
        entries2 = new ArrayList<Entry>();
        entries3 = new ArrayList<Entry>();
        entries4 = new ArrayList<Entry>();
        entries5 = new ArrayList<Entry>();
        entries6 = new ArrayList<Entry>();
        entries7 = new ArrayList<Entry>();
        //entries.add(new Entry(0,0));
        dataSet1=new LineDataSet(entries1, "Temperature");
        dataSet2=new LineDataSet(entries2, "Humidity");
        dataSet3=new LineDataSet(entries3, "Pressure");
        dataSet4=new LineDataSet(entries4, "Battery");
        dataSet5=new LineDataSet(entries5, "X");
        dataSet5.setColor(Color.RED);
        dataSet6=new LineDataSet(entries6, "Y");
        dataSet6.setColor(Color.GREEN);
        dataSet7=new LineDataSet(entries7, "Z");
        dataSet7.setColor(Color.BLUE);


        field1.getDescription().setEnabled(false);
        field2.getDescription().setEnabled(false);
        field3.getDescription().setEnabled(false);
        field4.getDescription().setEnabled(false);
        field5.getDescription().setEnabled(false);

        dataSet1.setDrawValues(false);
        dataSet2.setDrawValues(false);
        dataSet3.setDrawValues(false);
        dataSet4.setDrawValues(false);
        dataSet5.setDrawValues(false);
        dataSet6.setDrawValues(false);
        dataSet7.setDrawValues(false);

        field1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis1 = field1.getXAxis();
        xAxis1.setValueFormatter(new MyXAxisValueFormatter());
        field2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis2 = field2.getXAxis();
        xAxis2.setValueFormatter(new MyXAxisValueFormatter());
        field3.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis3 = field3.getXAxis();
        xAxis3.setValueFormatter(new MyXAxisValueFormatter());
        field4.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis4 = field4.getXAxis();
        xAxis4.setValueFormatter(new MyXAxisValueFormatter());
        field5.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis5 = field5.getXAxis();
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

        pahoMqttClient = new PahoMqttClient();
        mqttAndroidClient = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);

        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                try {
                    mqttAndroidClient.subscribe("g2/#", 0);
                }catch(MqttSecurityException e){
                    e.printStackTrace();
                }catch(MqttException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                calendar=Calendar.getInstance();
                String payload = new String(mqttMessage.getPayload());
                String[] values = payload.split("&|=");
                for(int i=0; i< values.length; i++){
                    if(values[i].equals("field1")){
                        lineData1.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData1.getIndexOfDataSet(dataSet1));
                        if(first){
                            field1.setData(lineData1);
                        }
                        lineData1.notifyDataChanged();
                        field1.notifyDataSetChanged();
                        field1.invalidate(); // refresh
                    }else if(values[i].equals("field2")){
                        lineData2.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData2.getIndexOfDataSet(dataSet2));
                        if(first){
                            field2.setData(lineData2);
                        }
                        lineData2.notifyDataChanged();
                        field2.notifyDataSetChanged();
                        field2.invalidate(); // refresh
                    }else if(values[i].equals("field3")){
                        lineData3.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData3.getIndexOfDataSet(dataSet3));
                        if(first){
                            field3.setData(lineData3);
                        }
                        lineData3.notifyDataChanged();
                        field3.notifyDataSetChanged();
                        field3.invalidate(); // refresh
                    }else if(values[i].equals("field4")){
                        lineData4.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData4.getIndexOfDataSet(dataSet4));
                        if(first){
                            field4.setData(lineData4);
                        }
                        lineData4.notifyDataChanged();
                        field4.notifyDataSetChanged();
                        field4.invalidate(); // refresh
                    }else if(values[i].equals("field5")){
                        lineData5.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData5.getIndexOfDataSet(dataSet5));
                        if(first){
                            field5.setData(lineData5);
                        }
                        lineData5.notifyDataChanged();
                        field5.notifyDataSetChanged();
                        field5.invalidate(); // refresh
                    }else if(values[i].equals("field6")){
                        lineData5.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData5.getIndexOfDataSet(dataSet6));
                        if(first){
                            field5.setData(lineData5);
                        }
                        lineData5.notifyDataChanged();
                        field5.notifyDataSetChanged();
                        field5.invalidate(); // refresh
                    }else if(values[i].equals("field7")){
                        lineData5.addEntry(new Entry((calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND)) ,Float.parseFloat(values[i+1])), lineData5.getIndexOfDataSet(dataSet7));
                        if(first){
                            field5.setData(lineData5);
                        }
                        lineData5.notifyDataChanged();
                        field5.notifyDataSetChanged();
                        field5.invalidate(); // refresh
                    }else if(values[i].equals("field8")){
                        //field8.setText(values[i+1]);
                    }
                }


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

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

}
