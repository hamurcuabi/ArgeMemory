package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.ShareModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShowAllShareSoap {

    private static final String METHODE = "GetAllShare";
    private static final String METHODE_DATE = "GetAllShareByDate";
    private static final String SOAP_ACTION = "http://argememory.com/GetAllShare";
    private static final String SOAP_ACTION_DATE = "http://argememory.com/GetAllShareByDate";
    private static final String URL = "http://www.argememory.com/webservice/Share.asmx";
    public ShareModel shareModel;
    public ArrayList<ShareModel> shareModelArrayList;
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public ArrayList<ShareModel> allShare(String compId) {

        shareModel = new ShareModel();
        shareModelArrayList = new ArrayList<>();
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
                String shareDate = responseTask2.getProperty("shareDate").toString();
                String shareOwner = responseTask2.getProperty("shareOwner").toString();
                String shareTag = responseTask2.getProperty("shareTag").toString();
                String shareDescp = responseTask2.getProperty("shareDescp").toString();
                String shareCountMember = responseTask2.getProperty("shareCountMember").toString();
                String shareID = responseTask2.getProperty("shareID").toString();
                String shareCountComment = responseTask2.getProperty("shareCountComment").toString();
                //Boş gelirse
                if (shareDescp.equals("anyType{}")) {
                    shareDescp = "";
                }
                DateFormat formatter = new SimpleDateFormat("d.M.yyyy HH:mm:ss");
                Date date = formatter.parse(shareDate);

                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dt = formatter.format(date);
                shareModelArrayList.add(new ShareModel(dt, shareOwner, shareTag, shareDescp,
                        shareCountMember, shareID, shareCountComment));

            }

        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return shareModelArrayList;
    }

    public ArrayList<ShareModel> allShareByDate(String compId, String date, String dateSecond) {

        shareModel = new ShareModel();
        shareModelArrayList = new ArrayList<>();
        soapObject = new SoapObject(Utils.NAMESPACE, METHODE_DATE);
        soapObject.addProperty("compId", compId);
        soapObject.addProperty("date", date);
        soapObject.addProperty("dateSecond", dateSecond);
        soapObject.addProperty("api", Utils.API_KEY);

        soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(soapObject);

        httpsTransportSE = new HttpTransportSE(URL);
        httpsTransportSE.debug = true;

        try {

            httpsTransportSE.call(SOAP_ACTION_DATE, soapSerializationEnvelope);

            SoapObject response = (SoapObject) soapSerializationEnvelope.bodyIn;
            SoapObject responseTask = (SoapObject) response.getProperty(0);
            for (int i = 0; i < responseTask.getPropertyCount(); i++) {
                SoapObject responseTask2 = (SoapObject) responseTask.getProperty(i);
                String shareDate = responseTask2.getProperty("shareDate").toString();
                String shareOwner = responseTask2.getProperty("shareOwner").toString();
                String shareTag = responseTask2.getProperty("shareTag").toString();
                String shareDescp = responseTask2.getProperty("shareDescp").toString();
                String shareCountMember = responseTask2.getProperty("shareCountMember").toString();
                String shareID = responseTask2.getProperty("shareID").toString();
                String shareCountComment = responseTask2.getProperty("shareCountComment").toString();
                //Boş gelirse
                if (shareDescp.equals("anyType{}")) {
                    shareDescp = "";
                }
                DateFormat formatter = new SimpleDateFormat("d.M.yyyy HH:mm:ss");
                Date dates = formatter.parse(shareDate);

                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dt = formatter.format(dates);
                shareModelArrayList.add(new ShareModel(dt, shareOwner, shareTag, shareDescp,
                        shareCountMember, shareID, shareCountComment));

            }

        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return shareModelArrayList;
    }


}
