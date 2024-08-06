package com.example.taam_project;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
        final boolean[] has_been_changed = {false};
        // Set up the listener for the radio group
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Find the selected radio button
                selectedRadioButton = findViewById(checkedId);
                has_been_changed[0] = true;
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
                if (!has_been_changed[0]) {
                    Toast.makeText(ReportActivity.this, "Must Select Option!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (searchCriteria.equals("All Items")){
                    Toast.makeText(ReportActivity.this, "Please wait a moment, while the pdf is being generated", Toast.LENGTH_SHORT).show();
                    generatePDF("", "Category", descriptionandimage);
                    return;
                }
                generatePDF(query, searchCriteria, descriptionandimage);
            }
        });
    }
    public static String[] splitByNumber(String text, int chunkSize) {
        int textLength = text.length();
        List<String> chunks = new ArrayList<>();
        StringBuilder currentChunk = new StringBuilder();

        String[] words = text.split(" ");

        for (String word : words) {
            if (currentChunk.length() + word.length() + (currentChunk.length() > 0 ? 1 : 0) > chunkSize) {
                chunks.add(currentChunk.toString());
                currentChunk = new StringBuilder();
            }
            if (currentChunk.length() > 0) {
                currentChunk.append(" ");
            }
            currentChunk.append(word);
        }

        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString());
        }
        return chunks.toArray(new String[0]);
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
        Paint content = new Paint();
        Paint descriptionText = new Paint();
        Paint geometry = new Paint();

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

            Bitmap template = BitmapFactory.decodeResource(getResources(), R.drawable.reporttemplate);
            Bitmap scaledTemplate = Bitmap.createScaledBitmap(template, 1020, 1320, false);
            canvas.drawBitmap(scaledTemplate, 0, 0, paint);


            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/customfont.ttf");
            title.setTypeface(typeface);
            title.setTextSize(35);
            title.setColor(ContextCompat.getColor(this, R.color.black));
            title.setTextAlign(Paint.Align.LEFT);

            descriptionText.setTypeface(typeface);
            descriptionText.setTextSize(20);
            descriptionText.setColor(ContextCompat.getColor(this, R.color.black));
            descriptionText.setTextAlign(Paint.Align.LEFT);

            content.setTypeface(typeface);
            content.setTextSize(25);
            content.setColor(ContextCompat.getColor(this, R.color.black));
            content.setTextAlign(Paint.Align.LEFT);

            try {
                bmp = new DownloadImageTask().execute(list.get(i).getMedia()).get(); // Use .get() to wait for the result

                if (bmp != null) {
                    int targetHeight = 500;
                    int targetWidth = 400;

                    int width = bmp.getWidth();
                    int height = bmp.getHeight();
                    double scalar;
                    if (height > width) {
                        scalar = (double) targetHeight / height;
                    } else {
                        scalar = (double) targetWidth / width;
                    }
                    int scaledWidth = (int) (width * scalar);
                    int scaledHeight = (int) (height * scalar);
                    scaledbmp = Bitmap.createScaledBitmap(bmp, scaledWidth, scaledHeight, false);
                    canvas.drawBitmap(scaledbmp, 80, 150, paint);
                } else {
                    canvas.drawText("Image unavailable", 80, 150, content);
                    canvas.drawText("video or no media", 80, 180, content);
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
            String[] lines = splitByNumber(description, 63);
            String[] name_lines = splitByNumber(name, 25);

            //canvas.drawText(name, 530, 200, title);
            float y_1 = 200; // Starting y position for the text
            for (String line : name_lines) {
                canvas.drawText(line, 530, y_1, title);
                y_1 += descriptionText.getTextSize() + 10; // Move y position for the next line
            }

            canvas.drawText(dynasty, 730, 533, content);
            canvas.drawText(category, 730, 440, content);
            canvas.drawText(lotNumber, 730, 625, content);

            float y_2 = 820; // Starting y position for the text
            for (String line : lines) {
                canvas.drawText(line, 80, y_2, descriptionText);
                y_2 += descriptionText.getTextSize() + 4; // Move y position for the next line
            }

            if (descriptionandimage){
                geometry.setColor(Color.WHITE);
                canvas.drawRect(500, 130, 1030, 700, geometry);
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