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

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //10.49.1.26
    private PahoMqttClient pahoMqttClient;
    private MqttAndroidClient mqttAndroidClient;
    private String topic = "g4/answer/#";
    private int idRoom = -1;
    private String idSpecie="";
    private TextView tv;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tv9;
    private TextView tv10;
    private TextView tv11;
    private TextView tv12;
    private TextView tv13;
    private TextView tv14;
    private TextView tv15;
    //private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;
    private EditText et7;
    private EditText et8;
    private EditText et9;
    private EditText et10;
    private EditText et11;
    private EditText et12;
    private EditText et14;
    private EditText et15;
    private ListView lv1;
    private ListView lv2;
    MqttMessage message;
    private int mode;
    Toast toast;
    private static final int ROOMS=0;
    private static final int SPECIES=1;
    private static final int TEST=2;
    private static final int CHANGE=3;
    private static final int LIMITS=4;
    private static final int ADDSPECIES=5;
    private static final int SHELF=6;
    private static final int ADDSHELF=7;



    private Calendar calendar;

    private String clientID;

    private TextView tvHost;
    private ArrayList<String> selectableItems =new ArrayList<String>();
    private ArrayAdapter<String> adapter1;
    private ArrayList<String> listItems=new ArrayList<String>();
    private ArrayAdapter<String> adapter2;
    Button getSpecies;
    Button getRooms;
    Button test;
    Button changeLimit;
    Button getLimits;
    Button addSpecies;
    Button shelf;
    Button addShelf;
    Toolbar toolbar;
    Spinner speciespinner;
    Spinner shelfspinner;
    ArrayList<String> speciesList = new ArrayList<String>();
    ArrayList<String> shelfList = new ArrayList<String>();
    ArrayAdapter adapterspecies;
    ArrayAdapter adaptershelf;

    int pirCount=0;
    int ffcount=0;

    private boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_main);

        Random r = new Random();
        int i1 = r.nextInt(1000 - 1) + 1;
        clientID="client_"+i1;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Gateway");

        speciespinner = (Spinner) findViewById(R.id.speciespinner);
        shelfspinner = (Spinner) findViewById(R.id.shelfspinner);
        speciespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        shelfspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapterspecies = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, speciesList);
        adaptershelf = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, shelfList);
        speciespinner.setAdapter(adapterspecies);
        shelfspinner.setAdapter(adaptershelf);
        tvHost = (TextView) findViewById(R.id.hostET);

        tv= (TextView)findViewById(R.id.tv);
        tv1= (TextView)findViewById(R.id.tv1);
        tv2= (TextView)findViewById(R.id.tv2);
        tv3= (TextView)findViewById(R.id.tv3);
        tv4= (TextView)findViewById(R.id.tv4);
        tv5= (TextView)findViewById(R.id.tv5);
        tv6= (TextView)findViewById(R.id.tv6);
        tv7= (TextView)findViewById(R.id.tv7);
        tv8= (TextView)findViewById(R.id.tv8);
        tv9= (TextView)findViewById(R.id.tv9);
        tv10= (TextView)findViewById(R.id.tv10);
        tv11= (TextView)findViewById(R.id.tv11);
        tv12= (TextView)findViewById(R.id.tv12);
        tv13= (TextView)findViewById(R.id.tv13);
        tv14= (TextView)findViewById(R.id.tv14);
        tv15= (TextView)findViewById(R.id.tv15);
        //et1= (EditText)findViewById(R.id.et1);
        et2= (EditText)findViewById(R.id.et2);
        et3= (EditText)findViewById(R.id.et3);
        et4= (EditText)findViewById(R.id.et4);
        et5= (EditText)findViewById(R.id.et5);
        et6= (EditText)findViewById(R.id.et6);
        et7= (EditText)findViewById(R.id.et7);
        et8= (EditText)findViewById(R.id.et8);
        et9= (EditText)findViewById(R.id.et9);
        et10= (EditText)findViewById(R.id.et10);
        et11= (EditText)findViewById(R.id.et11);
        et12= (EditText)findViewById(R.id.et12);
        et14= (EditText)findViewById(R.id.et14);
        et15= (EditText)findViewById(R.id.et15);

        getSpecies = (Button)findViewById(R.id.getSpecies);
        getSpecies.setOnClickListener(this);
        addSpecies = (Button)findViewById(R.id.addSpecies);
        addSpecies.setOnClickListener(this);
        getRooms = (Button)findViewById(R.id.getRooms);
        getRooms.setOnClickListener(this);
        test = (Button)findViewById(R.id.test);
        test.setOnClickListener(this);
        changeLimit = (Button)findViewById(R.id.changeLimit);
        changeLimit.setOnClickListener(this);
        getLimits = (Button)findViewById(R.id.getLimits);
        getLimits.setOnClickListener(this);
        shelf = (Button)findViewById(R.id.shelf);
        shelf.setOnClickListener(this);
        addShelf = (Button)findViewById(R.id.addShelf);
        addShelf.setOnClickListener(this);


        lv1= (ListView)findViewById(R.id.lv1);
        lv1.setOnItemClickListener(this);
        adapter1 = new ArrayAdapter(this,
                 //android.R.layout.simple_list_item_1,
                android.R.layout.simple_list_item_single_choice,
                // android.R.layout.simple_list_item_single_choice,
                // android.R.layout.simple_list_item_multiple_choice,
                selectableItems);
        lv1.setAdapter(adapter1);
        lv1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv2= (ListView)findViewById(R.id.lv2);
        lv2.setChoiceMode( ListView.CHOICE_MODE_NONE );
        lv2.setOnItemClickListener(this);
        adapter2 = new ArrayAdapter(this,
                //android.R.layout.simple_list_item_1,
                android.R.layout.simple_list_item_1,
                // android.R.layout.simple_list_item_single_choice,
                // android.R.layout.simple_list_item_multiple_choice,
                listItems);
        lv2.setAdapter(adapter2);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String  itemValue    = (String) lv1.getItemAtPosition(position);
        switch(mode){
            case ROOMS:
                idRoom = Integer.parseInt(itemValue);
                break;
        }

        tv.setText( itemValue );
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
                    mqttAndroidClient.subscribe("g4/test", 0);
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

                if((topicReceived.equals("g4/answer/speciesList"))&&(mode == SPECIES)){
                    hideTextViews();
                    payload = payload.replace("[","");
                    payload = payload.replace("]","");
                    payload = payload.replace("\"","");
                    payload = payload.replace("\r","");
                    payload = payload.replace("\n","");
                    String[] splitted = payload.split(",");
                    for(int i=0; i<splitted.length; i++){
                        selectableItems.add(splitted[i]);
                        tv.setText(text+"\n"+"-->"+topicReceived+ " - " + splitted[i]+"\n");
                        text = tv.getText().toString();
                    }
                    //tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    tv4.setVisibility(View.VISIBLE);
                    tv5.setVisibility(View.VISIBLE);
                    tv6.setVisibility(View.VISIBLE);
                    tv7.setVisibility(View.VISIBLE);
                    tv8.setVisibility(View.VISIBLE);
                    tv9.setVisibility(View.VISIBLE);
                    tv10.setVisibility(View.VISIBLE);
                    tv11.setVisibility(View.VISIBLE);
                    tv12.setVisibility(View.VISIBLE);
                    tv14.setVisibility(View.VISIBLE);
                    tv15.setVisibility(View.VISIBLE);
                    //tv13.setVisibility(View.VISIBLE);
                    //et1.setVisibility(View.VISIBLE);
                    et2.setVisibility(View.VISIBLE);
                    et3.setVisibility(View.VISIBLE);
                    et4.setVisibility(View.VISIBLE);
                    et5.setVisibility(View.VISIBLE);
                    et6.setVisibility(View.VISIBLE);
                    et7.setVisibility(View.VISIBLE);
                    et8.setVisibility(View.VISIBLE);
                    et9.setVisibility(View.VISIBLE);
                    et10.setVisibility(View.VISIBLE);
                    et11.setVisibility(View.VISIBLE);
                    et12.setVisibility(View.VISIBLE);
                    et14.setVisibility(View.VISIBLE);
                    et15.setVisibility(View.VISIBLE);
                    //tv1.setText("idRoom: ");
                    tv2.setText("idSpecie: ");
                    tv3.setText("maxTemp: ");
                    tv4.setText("minTemp: ");
                    tv5.setText("maxHum: ");
                    tv6.setText("minHum:");
                    tv7.setText("maxPH: ");
                    tv8.setText("minPH: ");
                    tv9.setText("maxOxyg: ");
                    tv10.setText("minOxyg: ");
                    tv11.setText("maxCond: ");
                    tv12.setText("minCond: ");
                    tv14.setText("maxWL: ");
                    tv15.setText("minWL: ");
                    addSpecies.setVisibility(View.VISIBLE);
                }else if((topicReceived.equals("g4/answer/roomsList"))&&(mode==ROOMS)){
                    hideTextViews();
                    payload = payload.replace("[","");
                    payload = payload.replace("]","");
                    payload = payload.replace("\"","");
                    payload = payload.replace("\r","");
                    payload = payload.replace("\n","");
                    String[] splitted = payload.split(",");
                    for(int i=0; i<splitted.length; i++){
                        selectableItems.add(splitted[i]);
                        tv.setText(text+"\n"+"-->"+topicReceived+ " - " + splitted[i]+"\n");
                        text = tv.getText().toString();
                    }
                    adapter1.notifyDataSetChanged();
                    lv1.setVisibility(View.VISIBLE);
                    test.setVisibility(View.VISIBLE);
                }else if ((topicReceived.equals("g4/answer/test")&&(mode==TEST))){
                    hideTextViews();
                    listItems.clear();
                    tv.setText(payload);
                    lv2.setVisibility(View.VISIBLE);

                    Gson gson = new Gson();
                    Measures measure = gson.fromJson(payload, Measures.class);
                    listItems.add("idRoom");
                    listItems.add(""+measure.getIdRoom());
                    listItems.add("Temperature [ºC]");
                    listItems.add(""+measure.getTemp());
                    listItems.add("Humidity [%HR]");
                    listItems.add(""+measure.getHum());
                    listItems.add("Pressure [pa]");
                    listItems.add(""+measure.getPres());
                    adapter2.notifyDataSetChanged();
                }else if((topicReceived.equals("g4/answer/ambientLimits"))&&(mode==LIMITS)){
                    hideTextViews();
                    payload = payload.replace("[","");
                    payload = payload.replace("]","");
                    payload = payload.replace("\r","");
                    payload = payload.replace("\n","");
                    tv.setText(payload);
                    Gson gson = new Gson();
                    AmbientModuleLimits ambientModuleLimits = gson.fromJson(payload, AmbientModuleLimits.class);
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    tv4.setVisibility(View.VISIBLE);
                    tv5.setVisibility(View.VISIBLE);
                    tv6.setVisibility(View.VISIBLE);
                    tv7.setVisibility(View.VISIBLE);
                    tv13.setVisibility(View.VISIBLE);
                    //et1.setVisibility(View.VISIBLE);
                    et2.setVisibility(View.VISIBLE);
                    et3.setVisibility(View.VISIBLE);
                    et4.setVisibility(View.VISIBLE);
                    et5.setVisibility(View.VISIBLE);
                    et6.setVisibility(View.VISIBLE);
                    et7.setVisibility(View.VISIBLE);
                    tv1.setText("idRoom: ");
                    tv2.setText("maxTemp: ");
                    tv3.setText("minTemp: ");
                    tv4.setText("maxHum: ");
                    tv5.setText("minHum:");
                    tv6.setText("maxPres: ");
                    tv7.setText("minPres: ");
                    tv13.setText(""+ambientModuleLimits.getIdRoom());
                    et2.setText(""+ambientModuleLimits.getMaxTemp());
                    et3.setText(""+ambientModuleLimits.getMinTemp());
                    et4.setText(""+ambientModuleLimits.getMaxHum());
                    et5.setText(""+ambientModuleLimits.getMinHum());
                    et6.setText(""+ambientModuleLimits.getMaxPres());
                    et7.setText(""+ambientModuleLimits.getMinPres());
                    changeLimit.setVisibility(View.VISIBLE);
                }else if((topicReceived.equals("g4/answer/shelfsList")||(topicReceived.equals("g4/answer/speciesList")))&&(mode==SHELF)){
                    if(topicReceived.equals("g4/answer/shelfsList")){
                        payload = payload.replace("[","");
                        payload = payload.replace("]","");
                        payload = payload.replace("\"","");
                        payload = payload.replace("\r","");
                        payload = payload.replace("\n","");
                        String[] splitted = payload.split(",");
                        for(int i=0; i<splitted.length; i++){
                            shelfList.add(splitted[i]);
                            tv.setText(text+"\n"+"-->"+topicReceived+ " - " + splitted[i]+"\n");
                            text = tv.getText().toString();
                        }
                        shelfspinner.setVisibility(View.VISIBLE);
                        adaptershelf.notifyDataSetChanged();
                    }
                    if(topicReceived.equals("g4/answer/speciesList")){
                        payload = payload.replace("[","");
                        payload = payload.replace("]","");
                        payload = payload.replace("\"","");
                        payload = payload.replace("\r","");
                        payload = payload.replace("\n","");
                        String[] splitted = payload.split(",");
                        for(int i=0; i<splitted.length; i++){
                            speciesList.add(splitted[i]);
                            tv.setText(text+"\n"+"-->"+topicReceived+ " - " + splitted[i]+"\n");
                            text = tv.getText().toString();
                        }
                        speciespinner.setVisibility(View.VISIBLE);
                        adapterspecies.notifyDataSetChanged();
                    }
                    addShelf.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        String mes="";
        if(connected) {
            switch (view.getId()) {
                case R.id.getSpecies:
                    message = new MqttMessage("{\"command\":\"getSpecies\"}".getBytes());
                    message.setQos(2);
                    message.setRetained(false);
                    mode = SPECIES;
                    try {
                        mqttAndroidClient.publish("g4/commands", message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.getRooms:
                    hideTextViews();
                    message = new MqttMessage("{\"command\":\"getRooms\"}".getBytes());
                    message.setQos(2);
                    message.setRetained(false);
                    mode = ROOMS;
                    try {
                        mqttAndroidClient.publish("g4/commands", message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.test:
                    mes = "{\"command\":\"test\", \"idRoom\":" + idRoom + "}";
                    message = new MqttMessage(mes.getBytes());
                    message.setQos(2);
                    message.setRetained(false);
                    mode = TEST;
                    try {
                        mqttAndroidClient.publish("g4/commands", message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.changeLimit:
                    mes = "{\"command\":\"changeLimit\", \"idRoom\":" + tv13.getText().toString() +
                            ",\"maxTemp\":" + et2.getText().toString() +
                            ",\"minTemp\":" + et3.getText().toString() +
                            ",\"maxHum\":" + et4.getText().toString() +
                            ",\"minHum\":" + et5.getText().toString() +
                            ",\"maxPres\":" + et6.getText().toString() +
                            ",\"minPres\":" + et7.getText().toString() +
                            "}";
                    message = new MqttMessage(mes.getBytes());
                    message.setQos(2);
                    message.setRetained(false);
                    try {
                        mqttAndroidClient.publish("g4/commands", message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    mode = CHANGE;
                    break;
                case R.id.getLimits:
                    hideTextViews();
                    message = new MqttMessage("{\"command\":\"getLimits\"}".getBytes());
                    message.setQos(2);
                    message.setRetained(false);
                    mode = ROOMS;
                    try {
                        mqttAndroidClient.publish("g4/commands", message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    mode =LIMITS;
                    break;
                case R.id.addSpecies:
                    if(selectableItems.contains(et2.getText().toString())){
                        Toast.makeText(getBaseContext(), "idSpecie already exists", Toast.LENGTH_SHORT).show();
                    }else {
                        mes = "{\"idSpecie\":\"" + et2.getText().toString() +
                                "\",\"maxTemp\":" + et3.getText().toString() +
                                ",\"minTemp\":" + et4.getText().toString() +
                                ",\"maxHum\":" + et5.getText().toString() +
                                ",\"minHum\":" + et6.getText().toString() +
                                ",\"maxPH\":" + et7.getText().toString() +
                                ",\"minPH\":" + et8.getText().toString() +
                                ",\"maxOxyg\":" + et9.getText().toString() +
                                ",\"minOxyg\":" + et10.getText().toString() +
                                ",\"maxCond\":" + et11.getText().toString() +
                                ",\"minCond\":" + et12.getText().toString() +
                                ",\"maxWL\":" + et14.getText().toString() +
                                ",\"minWL\":" + et15.getText().toString() +
                                "}";
                        message = new MqttMessage(mes.getBytes());
                        message.setQos(2);
                        mode = ROOMS;
                        try {
                            mqttAndroidClient.publish("g4/config/addSpecies", message);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        mode = ADDSPECIES;
                    }
                    break;
                case R.id.shelf:
                    hideTextViews();
                    message = new MqttMessage("{\"command\":\"getSpecies\"}".getBytes());
                    message.setQos(2);
                    message.setRetained(false);
                    try {
                        mqttAndroidClient.publish("g4/commands", message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    message = new MqttMessage("{\"command\":\"getShelfs\"}".getBytes());
                    message.setQos(2);
                    message.setRetained(false);
                    try {
                        mqttAndroidClient.publish("g4/commands", message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    mode=SHELF;
                    break;
                case R.id.addShelf:
                    tv.setText(speciespinner.getSelectedItem().toString());
                    mes = "{\"idSpecie\":\""+ speciespinner.getSelectedItem().toString()+"\", \"idShelf\":" + Integer.parseInt(shelfspinner.getSelectedItem().toString()) + "}";
                    message = new MqttMessage(mes.getBytes());
                    message.setQos(2);
                    message.setRetained(false);
                    try {
                        mqttAndroidClient.publish("g4/config/addShelf", message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    mode=ADDSHELF;
                    break;
            }
        }else{
            Toast.makeText(getBaseContext(), "MQTT not connected, tap the button in toolbar", Toast.LENGTH_SHORT).show();
        }
    }

    private void getHost(){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.pop_up_host, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        alert.setTitle("Host");
        alert.setView(mView);
        String[] hosts = {"138.100.48.251", "10.49.1.26"};
        final Spinner userInputDialogSpinner = (Spinner) mView.findViewById(R.id.userInputDialog);
        ArrayAdapter userInputDialogAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hosts);
        userInputDialogSpinner.setAdapter(userInputDialogAdapter);
        alert
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String name = userInputDialogSpinner.getSelectedItem().toString();
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

    private void hideTextViews(){
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et6.setText("");
        et7.setText("");
        selectableItems.clear();
        adapter1.notifyDataSetChanged();
        listItems.clear();
        adapter2.notifyDataSetChanged();
        shelfList.clear();
        speciesList.clear();
        adaptershelf.notifyDataSetChanged();
        adapterspecies.notifyDataSetChanged();
        lv1.setVisibility(View.GONE);
        lv2.setVisibility(View.GONE);
        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        tv3.setVisibility(View.GONE);
        tv4.setVisibility(View.GONE);
        tv5.setVisibility(View.GONE);
        tv6.setVisibility(View.GONE);
        tv7.setVisibility(View.GONE);
        tv8.setVisibility(View.GONE);
        tv9.setVisibility(View.GONE);
        tv10.setVisibility(View.GONE);
        tv11.setVisibility(View.GONE);
        tv12.setVisibility(View.GONE);
        tv13.setVisibility(View.GONE);
        tv14.setVisibility(View.GONE);
        tv15.setVisibility(View.GONE);
        et2.setVisibility(View.GONE);
        et3.setVisibility(View.GONE);
        et4.setVisibility(View.GONE);
        et5.setVisibility(View.GONE);
        et6.setVisibility(View.GONE);
        et7.setVisibility(View.GONE);
        et8.setVisibility(View.GONE);
        et9.setVisibility(View.GONE);
        et10.setVisibility(View.GONE);
        et11.setVisibility(View.GONE);
        et12.setVisibility(View.GONE);
        et14.setVisibility(View.GONE);
        et15.setVisibility(View.GONE);
        shelfspinner.setVisibility(View.GONE);
        speciespinner.setVisibility(View.GONE);
        addShelf.setVisibility(View.GONE);
        addSpecies.setVisibility(View.GONE);
        test.setVisibility(View.GONE);
        changeLimit.setVisibility(View.GONE);
    }

}
