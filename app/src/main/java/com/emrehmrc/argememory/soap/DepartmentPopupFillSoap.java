package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.DepartmentModel;
import com.emrehmrc.argememory.model.TaskManModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class DepartmentPopupFillSoap {

    private static final String NAMESPACE = "http://argememory.com/";
    private static final String METHODE = "DepartmentByCompid";
    private static final String SOAP_ACTION = "http://argememory.com/DepartmentByCompid";
    private static final String URL = "http://www.argememory.com/webservice/Department.asmx";
    public DepartmentModel departmentModel;
    public ArrayList<DepartmentModel> departmentModelArrayList;
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public ArrayList<DepartmentModel> memberByCompId(String compId) {

        departmentModel = new DepartmentModel();
        departmentModelArrayList=new ArrayList<>();
        soapObject = new SoapObject(NAMESPACE, METHODE);
        soapObject.addProperty("compId", compId);
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
                String name = responseTask2.getProperty("name").toString();
                String id = responseTask2.getProperty("id").toString();

                if (name.equals("anyType{}")) {
                    name = "";
                }
                departmentModelArrayList.add(new DepartmentModel(false,name,id));
            }

        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return departmentModelArrayList;
    }

}
