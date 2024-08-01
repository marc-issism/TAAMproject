package com.example.taam_project;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;



public class ReportActivity extends AppCompatActivity {
    Button generatePDFbtn;
    private EditText editText;
    private RadioGroup radioGroup;
    private RadioButton selectedRadioButton;
    private Switch toggle;
    //private static final int PERMISSION_REQUEST_CODE = 200;
    @SuppressLint("WrongViewCast")
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



        editText = findViewById(R.id.editTextText);
        radioGroup = findViewById(R.id.radioGroup);
        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF);
        toggle = findViewById(R.id.descriptionandpicture);

        // Set up the listener for the radio group
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Find the selected radio button
                selectedRadioButton = findViewById(checkedId);

                if (selectedRadioButton.getText().toString().equals("All Items")) {
                    editText.setVisibility(View.INVISIBLE);
                } else {
                    editText.setVisibility(View.VISIBLE);
                }

                if (selectedRadioButton != null) {
                    editText.setHint(selectedRadioButton.getText().toString());
                    editText.setContentDescription(selectedRadioButton.getText().toString());
                }

            }
        });

        // If you want to set a default value based on the initially checked radio button
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId != -1) {
            selectedRadioButton = findViewById(checkedRadioButtonId);
            editText.setContentDescription(selectedRadioButton.getText().toString());
        }


        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String query = editText.getText().toString().trim();
                String searchCriteria = selectedRadioButton.getText().toString();
                boolean descriptionandimage = toggle.isChecked();

                if (searchCriteria.equals("All Items")){
                    Toast.makeText(ReportActivity.this, "beep boop", Toast.LENGTH_SHORT).show();
                    generatePDF("", "Category", descriptionandimage);
                    return;
                }
                generatePDF(query, searchCriteria, descriptionandimage);
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
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    private void generatePDF(String query, String searchCriteria, boolean descriptionandimage) {
        int pageHeight = 1320;
        int pagewidth = 1020;
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();
        Paint descriptionText = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        String lotNumber;
        String dynasty;
        String description;
        String category;
        String name;
        Bitmap bmp, scaledbmp;
        Datastore ds = Datastore.getInstance();


        List<Item> list = new ArrayList<>();
        if (searchCriteria.equals("Category")) {
            list = ds.filterItems(Datastore.SearchableField.CATEGORY, query);
        } else if (searchCriteria.equals("LotNumber")) {
            list = ds.filterItems(Datastore.SearchableField.LOT, query);
        } else if (searchCriteria.equals("Period")) {
            list = ds.filterItems(Datastore.SearchableField.PERIOD, query);
        } else if (searchCriteria.equals("Name")) {
            list = ds.filterItems(Datastore.SearchableField.NAME, query);
        }
        if (list == null || list.isEmpty()){
            Toast.makeText(ReportActivity.this, "No item that matches the query / criterion", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < list.size(); i++) {
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

            int image_count = list.size();
            try {
                bmp = new DownloadImageTask().execute(list.get(i).getMedia()).get(); // Use .get() to wait for the result

                if (bmp != null) {
                    int height = bmp.getHeight();
                    double scalar = (double) 400 / height;
                    double width = bmp.getWidth() * scalar;
                    scaledbmp = Bitmap.createScaledBitmap(bmp, (int) width, 400, false);
                    canvas.drawBitmap(scaledbmp, 40, 40, paint);
                } else {
                    canvas.drawText("Image unavailable", 60, 60, title);
                    canvas.drawText("video or no media", 60, 200, title);
                    Log.e("generatePDF", "Bitmap is null. Skipping bitmap drawing.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Draw the text
            name = list.get(i).getName();
            category = list.get(i).getCategory();
            lotNumber = list.get(i).getLotNumber();
            dynasty = list.get(i).getPeriod();
            description = list.get(i).getDescription();
            String[] lines = splitByNumber(description, 75);

            if (!descriptionandimage){
                canvas.drawText(name, 550, 80, title);
                canvas.drawText("Period: " + dynasty, 550, 380, title);
                canvas.drawText("Category: " + category, 550, 440, title);
                canvas.drawText(lotNumber, 850, 1250, title);
            }


            float y = 650; // Starting y position for the text
            for (String line : lines) {
                canvas.drawText(line, 60, y, descriptionText);
                y += descriptionText.getTextSize() + 4; // Move y position for the next line
            }

            pdfDocument.finishPage(myPage);
        }

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "report.pdf");

        try {
            // Writing the PDF file to the specified location
            pdfDocument.writeTo(new FileOutputStream(file));
            //Toast.makeText(ReportActivity.this, "PDF file generated successfully. Check the Downloads folder.", Toast.LENGTH_SHORT).show();
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            PrintDocumentAdapter printAdapter = new PdfPrintDocumentAdapter(this, file.getAbsolutePath());
            printManager.print("Document", printAdapter, null);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ReportActivity.this, "Failed to generate PDF file.", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }


}