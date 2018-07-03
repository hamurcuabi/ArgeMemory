package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.PersonelModel;
import com.emrehmrc.argememory.model.TaskManModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class PersonelPopupFillSoap {

    private static final String NAMESPACE = "http://argememory.com/";
    private static final String METHODE = "MemberByDepid";
    private static final String METHODE_COMP = "MemberByCompId";
    private static final String SOAP_ACTION = "http://argememory.com/MemberByDepid";
    private static final String SOAP_ACTION_COMP = "http://argememory.com/MemberByCompId";
    private static final String URL = "http://www.argememory.com/webservice/Member.asmx";
    public PersonelModel personelModel;
    public ArrayList<PersonelModel> personelModelArrayList;
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public ArrayList<PersonelModel> getMemberByDepId(String depId) {

        personelModel = new PersonelModel();
        personelModelArrayList=new ArrayList<>();
        soapObject = new SoapObject(NAMESPACE, METHODE);
        soapObject.addProperty("depId", depId);
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
                personelModelArrayList.add(new PersonelModel(id,name,false));
            }

        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return personelModelArrayList;
    }
    public ArrayList<PersonelModel> getMemberByCompId(String compId) {

        personelModel = new PersonelModel();
        personelModelArrayList=new ArrayList<>();
        soapObject = new SoapObject(NAMESPACE, METHODE_COMP);
        soapObject.addProperty("compId", compId);
        soapObject.addProperty("api", Utils.API_KEY);

        soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(soapObject);

        httpsTransportSE = new HttpTransportSE(URL);
        httpsTransportSE.debug = true;

        try {
            httpsTransportSE.call(SOAP_ACTION_COMP, soapSerializationEnvelope);
            SoapObject response = (SoapObject) soapSerializationEnvelope.bodyIn;
            SoapObject responseTask = (SoapObject) response.getProperty(0);
            for (int i = 0; i < responseTask.getPropertyCount(); i++) {
                SoapObject responseTask2 = (SoapObject) responseTask.getProperty(i);
                String name = responseTask2.getProperty("name").toString();
                String id = responseTask2.getProperty("id").toString();

                if (name.equals("anyType{}")) {
                    name = "";
                }
                personelModelArrayList.add(new PersonelModel(id,name,false));
            }

        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return personelModelArrayList;
    }

}
