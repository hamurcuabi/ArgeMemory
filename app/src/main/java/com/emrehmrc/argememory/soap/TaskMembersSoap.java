package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.MainTaskModel;
import com.emrehmrc.argememory.model.TaskManModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class TaskMembersSoap {

    private static final String METHODE = "TaskMembers";
    private static final String SOAP_ACTION = "http://argememory.com/TaskMembers";
    private static final String URL = "http://www.argememory.com/webservice/task.asmx";
    public TaskManModel taskManModel;
    public ArrayList<TaskManModel> taskModelArrayList;
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public ArrayList<TaskManModel> taskMembers(String taskId) {

        taskManModel = new TaskManModel();
        taskModelArrayList=new ArrayList<>();
        soapObject = new SoapObject(Utils.NAMESPACE, METHODE);
        soapObject.addProperty("taskId", taskId);
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
                String name = responseTask2.getProperty("memberName").toString();

                if (name.equals("anyType{}")) {
                    name = "";
                }
                taskModelArrayList.add(new TaskManModel(name));
            }

        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return taskModelArrayList;
    }

}
