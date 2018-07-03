package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.TagModel;
import com.emrehmrc.argememory.model.TaskManModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class TagPopupFillTagSoap {

    private static final String METHODE = "ShowTag";
    private static final String SOAP_ACTION = "http://argememory.com/ShowTag";
    private static final String URL = "http://www.argememory.com/webservice/tag.asmx";
    public TagModel tagModel;
    public ArrayList<TagModel> tagModelArrayList;
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public ArrayList<TagModel> fillTag(String compId) {

        tagModel = new TagModel();
        tagModelArrayList=new ArrayList<>();
        soapObject = new SoapObject(Utils.NAMESPACE, METHODE);
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
                String id = responseTask2.getProperty("id").toString();
                String name = responseTask2.getProperty("name").toString();

                if (name.equals("anyType{}")) {
                    name = "";
                }
                tagModelArrayList.add(new TagModel(name,false,id));
            }

        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return tagModelArrayList;
    }

}
