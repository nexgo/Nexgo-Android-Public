package com.nexgo.printersample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.nexgo.oaf.apiv3.APIProxy;
import com.nexgo.oaf.apiv3.DeviceEngine;
import com.nexgo.oaf.apiv3.SdkResult;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
import com.nexgo.oaf.apiv3.device.printer.BarcodeFormatEnum;
import com.nexgo.oaf.apiv3.device.printer.OnPrintListener;
import com.nexgo.oaf.apiv3.device.printer.Printer;

public class MainActivity extends AppCompatActivity implements OnPrintListener {

    private final String TAG = "PrinterSample";
    private DeviceEngine deviceEngine;
    private Printer printer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the SDK components
        deviceEngine = APIProxy.getDeviceEngine(MainActivity.this);
        printer = deviceEngine.getPrinter();

        //Initialize the printer
        printer.initPrinter();

        int initResult = printer.getStatus();
        switch (initResult){
            case SdkResult.Success:
                Log.d(TAG, "Printer init success");
                break;
            case SdkResult.Printer_PaperLack:
                Log.w(TAG, "Printer is out of paper");
                Toast.makeText(MainActivity.this, "Out of Paper!", Toast.LENGTH_LONG).show();
                break;
            default:
                Log.w(TAG, "Printer Init Misc Error: " + initResult);
                break;
        }

        //Let's use an external font from the file in our 'assets' directory
        AssetManager am = getApplicationContext().getAssets();
        Typeface platNomor = Typeface.createFromAsset(am, "PlatNomor.ttf");
        printer.setTypeface(platNomor);

        //Add sample text on top of receipt
        printer.appendPrnStr("Panda Karate", 54, AlignEnum.CENTER, true);
        printer.appendPrnStr("2251 Wax On Ave", 30, AlignEnum.CENTER, false);
        printer.appendPrnStr("Laguna Hills, CA", 30, AlignEnum.CENTER, false);

        //Add Panda logo to the receipt
        Bitmap receiptLogo = StringToBitMap(",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");
        if (receiptLogo != null) {
            printer.appendImage(receiptLogo,
                    AlignEnum.CENTER);
        }

        //Add QR Code after Panda Logo
        printer.appendQRcode("Sample QR Code for the PrintSample Application!",
                384,
                7,
                3,
                AlignEnum.CENTER);

        //Add barcode after QR Code (text, height total, spacing, barcode text height, barcode format, alignment)
        printer.appendBarcode("1234567890",
                100,
                0,
                10,
                BarcodeFormatEnum.CODE_128,
                AlignEnum.CENTER);

        //Start the print job
        printer.startPrint(true, MainActivity.this);

    }

    /*
    Utility to get a bitmap image from a String.
     */
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }


    @Override
    public void onPrintResult(int resultCode) {
        switch (resultCode){
            case SdkResult.Success:
                Log.d(TAG, "Printer job finished successfully!");
                break;
            case SdkResult.Printer_Print_Fail:
                Log.e(TAG, "Printer Failed: " + resultCode);
                break;
            case SdkResult.Printer_Busy:
                Log.e(TAG, "Printer is Busy: " + resultCode);
                break;
            case SdkResult.Printer_PaperLack:
                Log.e(TAG, "Printer is out of paper: " + resultCode);
                break;
            case SdkResult.Printer_Fault:
                Log.e(TAG, "Printer fault: " + resultCode);
                break;
            case SdkResult.Printer_TooHot:
                Log.e(TAG, "Printer temperature is too hot: " + resultCode);
                break;
            case SdkResult.Printer_UnFinished:
                Log.w(TAG, "Printer job is unfinished: " + resultCode);
                break;
            case SdkResult.Printer_Other_Error:
                Log.e(TAG, "Printer Other_Error: " + resultCode);
                break;
            default:
                Log.e(TAG, "Generic Fail Error: " + resultCode);
                break;
        }
    }
}
