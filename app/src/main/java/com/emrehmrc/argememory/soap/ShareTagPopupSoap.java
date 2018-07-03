package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.NotifCountModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class ShareTagPopupSoap {

    private static final String METHODE = "GetTagName";
    private static final String SOAP_ACTION = "http://argememory.com/GetTagName";
    private static final String URL = "http://www.argememory.com/webservice/TaskAndShare.asmx";
    private ArrayList<String> tagList;
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public ArrayList<String> getTagNames(String shareId) {

        tagList = new ArrayList<>();
        soapObject = new SoapObject(Utils.NAMESPACE, METHODE);
        soapObject.addProperty("shareId", shareId);
        soapObject.addProperty("api", Utils.API_KEY);

        soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(soapObject);

        httpsTransportSE = new HttpTransportSE(URL);
        httpsTransportSE.debug = true;

        try {

            httpsTransportSE.call(SOAP_ACTION, soapSerializationEnvelope);

            SoapObject response = (SoapObject) soapSerializationEnvelope.bodyIn;
            SoapObject responseMember2 = (SoapObject) response.getProperty(0);
            for (int i = 0; i < responseMember2.getPropertyCount(); i++) {
                SoapObject responseMember = (SoapObject) responseMember2.getProperty(i);
                String tag = responseMember.getProperty("name").toString();
                tagList.add(tag);

            }
        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return tagList;
    }


}
