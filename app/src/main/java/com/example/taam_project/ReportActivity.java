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
    Button generatePDFbtn;
    //private static final int PERMISSION_REQUEST_CODE = 200;
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
        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to
                // generate our PDF file.
                generatePDF();
            }
        });
    }
    public static String[] splitByNumber(String text, int chunkSize) {
        int textLength = text.length();
        int numChunks = textLength / chunkSize;
        int remainder = textLength % chunkSize;

        if (remainder > 0) {
            numChunks++;
        }

        String[] result = new String[numChunks];
        String remainingText = text;

        for (int i = 0; i < numChunks; i++) {
            if (remainingText.length() > chunkSize) {
                result[i] = remainingText.substring(0, chunkSize);
                remainingText = remainingText.substring(chunkSize);
            } else {
                result[i] = remainingText;
            }
        }

        return result;
    }

    private void generatePDF() {
        int pageHeight = 1320;
        int pagewidth = 1020;
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();
        Paint descriptionText = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder( pagewidth,pageHeight,1).create();

        String lotNumber;
        String dynasty;
        String description;
        String category;
        String name;
        Bitmap bmp, scaledbmp;

        for (int i = 0; i < 2; i++){
            //These work below should be repeated per item
            PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
            Canvas canvas = myPage.getCanvas();

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(50);
            title.setColor(ContextCompat.getColor(this, R.color.black));
            title.setTextAlign(Paint.Align.LEFT);

            descriptionText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            descriptionText.setTextSize(25);
            descriptionText.setColor(ContextCompat.getColor(this, R.color.black));
            descriptionText.setTextAlign(Paint.Align.LEFT);

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.replica);

            //manage horizontal image case and vertical image case
            int height = bmp.getHeight();
            double scalar = (double) 400 /height;
            double width =  bmp.getWidth() * scalar;

            scaledbmp = Bitmap.createScaledBitmap(bmp, (int)width, 400, false);

            name = "replica";
            category = "uncategorized";
            lotNumber = "1203";
            dynasty  = "Qing Dynasty";
            description = "This tricolor (sancai) box has a round shape with shallow straight walls, " +
                    "a concave circular mouth, a flat base, and a slightly curved lid. " +
                    "The outer surface of the lid is decorated with intricate molded patterns, " +
                    "displaying exquisite and varied designs. The exterior of the box is " +
                    "covered in tricolor glazes, including green, yellow, white, and blue. " +
                    "The interior of the box and the base are covered in yellow glaze. " +
                    "The entire piece is adorned with fine crackle patterns." +
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
            String[] lines = splitByNumber(description, 85);


            if (scaledbmp != null) {
                canvas.drawBitmap(scaledbmp, 40, 40, paint);
            } else {
                Log.e("generatePDF", "Bitmap is null. Skipping bitmap drawing.");
            }

            canvas.drawText(name, 550, 80, title);
            canvas.drawText(dynasty, 550, 380, title);
            canvas.drawText(category, 550, 430, title);
            canvas.drawText(lotNumber, 900, 80, title);

            float y = 650; // Starting y position for the text
            for (String line : lines) {
                canvas.drawText(line, 40, y, descriptionText);
                y += descriptionText.getTextSize()+ 4; // Move y position for the next line
            }

            pdfDocument.finishPage(myPage);
        }
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
}