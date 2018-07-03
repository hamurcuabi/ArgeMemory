package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.MainTaskModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainTaskSoap {

    private static final String NAMESPACE = "http://argememory.com/";
    private static final String METHODE = "MainTask";
    private static final String METHODE_CALENDAR = "CalendarTask";
    private static final String SOAP_ACTION = "http://argememory.com/MainTask";
    private static final String SOAP_ACTION_CALENDAR = "http://argememory.com/CalendarTask";
    private static final String URL = "http://www.argememory.com/webservice/task.asmx";
    public MainTaskModel mainTaskModel;
    public ArrayList<MainTaskModel> taskModelArrayList;
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public ArrayList<MainTaskModel> mainTask(String memberId) {

        mainTaskModel = new MainTaskModel();
        taskModelArrayList=new ArrayList<>();
        soapObject = new SoapObject(NAMESPACE, METHODE);
        soapObject.addProperty("memberId", memberId);
        soapObject.addProperty("api", Utils.API_KEY);

        soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(soapObject);

        httpsTransportSE = new HttpTransportSE(URL);
        httpsTransportSE.debug = true;

        try {

            httpsTransportSE.call(SOAP_ACTION, soapSerializationEnvelope);

            SoapObject response = (SoapObject) soapSerializationEnvelope.bodyIn;
            SoapObject responseTask = (SoapObject) response.getProperty(0);
            for (int i = 0; i < responseTask.getPropertyCount(); i++) {

                SoapObject responseTask2 = (SoapObject) responseTask.getProperty(i);
                String taskDate = responseTask2.getProperty("taskDate").toString();
                String taskCreater = responseTask2.getProperty("taskCreater").toString();
                String taskTag = responseTask2.getProperty("taskTag").toString();
                String taskDescription = responseTask2.getProperty("taskDescription").toString();
                String taskCountMan = responseTask2.getProperty("taskCountMan").toString();
                String taskId = responseTask2.getProperty("taskId").toString();
                String subTaskCount = responseTask2.getProperty("taskSubCount").toString();
                //Boş gelirse
                if (taskDescription.equals("anyType{}")) {
                    taskDescription = "";
                }
                DateFormat formatter = new SimpleDateFormat("d.M.yyyy HH:mm:ss");
                Date date = formatter.parse(taskDate);

                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dt=formatter.format(date);
                taskModelArrayList.add(new MainTaskModel(dt, taskCreater,
                        taskTag,
                        taskDescription, taskCountMan, taskId, subTaskCount));

            }

        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return taskModelArrayList;
    }



    public ArrayList<MainTaskModel> calendarTask(String memberId,String dateStart,String dateEnd) {

        mainTaskModel = new MainTaskModel();
        taskModelArrayList=new ArrayList<>();
        soapObject = new SoapObject(NAMESPACE, METHODE_CALENDAR);
        soapObject.addProperty("memberId", memberId);
        soapObject.addProperty("dateStart", dateStart);
        soapObject.addProperty("dateEnd", dateEnd);
        soapObject.addProperty("api", Utils.API_KEY);

        soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(soapObject);

        httpsTransportSE = new HttpTransportSE(URL);
        httpsTransportSE.debug = true;

        try {

            httpsTransportSE.call(SOAP_ACTION_CALENDAR, soapSerializationEnvelope);

            SoapObject response = (SoapObject) soapSerializationEnvelope.bodyIn;
            SoapObject responseTask = (SoapObject) response.getProperty(0);
            for (int i = 0; i < responseTask.getPropertyCount(); i++) {

                SoapObject responseTask2 = (SoapObject) responseTask.getProperty(i);
                String taskDate = responseTask2.getProperty("taskDate").toString();
                String taskCreater = responseTask2.getProperty("taskCreater").toString();
                String taskTag = responseTask2.getProperty("taskTag").toString();
                String taskDescription = responseTask2.getProperty("taskDescription").toString();
                String taskCountMan = responseTask2.getProperty("taskCountMan").toString();
                String taskId = responseTask2.getProperty("taskId").toString();
                String subTaskCount = responseTask2.getProperty("taskSubCount").toString();
                //Boş gelirse
                if (taskDescription.equals("anyType{}")) {
                    taskDescription = "";
                }
                taskModelArrayList.add(new MainTaskModel(taskDate, taskCreater, taskTag,
                        taskDescription, taskCountMan, taskId, subTaskCount));

            }

        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return taskModelArrayList;
    }



}
