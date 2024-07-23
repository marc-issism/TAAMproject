package com.example.taam_project;

import android.os.Bundle;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;



public class ReportActivity extends AppCompatActivity {
    // variables for our buttons.
    Button generatePDFbtn;

    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;

    // creating a bitmap variable
    // for storing our images
    //Bitmap /*bmp,*/ scaledbmp;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF);
        //bmp = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        //scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        // below code is used for
        // checking our permissions.
        /*
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
        */

        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to
                // generate our PDF file.
                generatePDF();
            }
        });
    }
    private void generatePDF() {
        // creating an object variable for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();

        /*if (scaledbmp != null) {
            // drawing our image on our PDF file.
            canvas.drawBitmap(scaledbmp, 56, 40, paint);
        } else {
            Log.e("generatePDF", "Bitmap is null. Skipping bitmap drawing.");
        }*/

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextAlign(Paint.Align.LEFT); // Align text to left



// Sample extracted content from Items.pdf (for illustration purposes)
        String pdfContent = "This tricolor (sancai) box has a round shape with shallow straight walls, " +
                "a concave circular mouth, a flat base, and a slightly curved lid. " +
                "The outer surface of the lid is decorated with intricate molded patterns, " +
                "displaying exquisite and varied designs. The exterior of the box is " +
                "covered in tricolor glazes, including green, yellow, white, and blue. " +
                "The interior of the box and the base are covered in yellow glaze. " +
                "The entire piece is adorned with fine crackle patterns.\n\n" +
                "During the mid-Tang Dynasty, the tricolor glazing technique reached its " +
                "peak, resulting in a wide variety of vessel forms and refined " +
                "craftsmanship. The glaze colors during this period were lustrous and the " +
                "coloring appeared natural. Vessels were often fully glazed both on the " +
                "inside and outside, utilizing colors such as green, yellow, white, blue, " +
                "and black, creating a complex and diverse palette. The production " +
                "process involved applying a base layer of slip before adding various " +
                "colored glazes to achieve the desired overall effect in terms of both form " +
                "and decoration. The decoration techniques included carving, stamping, " +
                "appliqué, and modeling. These tricolor artifacts showcased rich content " +
                "and were considered exquisite examples of Tang tricolor ware.";

// Splitting the text into lines to draw on the canvas
        String[] lines = pdfContent.split("\n");

// Drawing the extracted text on the canvas
        float y = 150; // Starting y position for the text
        for (String line : lines) {
            canvas.drawText(line, 50, y, title);
            y += title.getTextSize() + 5; // Move y position for the next line
        }

// Resetting title paint properties if needed for other texts
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(15);
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("This is sample document which we have created.", 396, 560, title);
        pdfDocument.finishPage(myPage);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "test.pdf");

        try {
            // writing our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // printing toast message on completion of PDF generation.
            Toast.makeText(ReportActivity.this, "PDF file generated successfully. Visit device's dowonload folder to print.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // handling error
            e.printStackTrace();
            Toast.makeText(ReportActivity.this, "Failed to generate PDF file.", Toast.LENGTH_SHORT).show();
        }
        pdfDocument.close();
    }


    /*
    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    */



}