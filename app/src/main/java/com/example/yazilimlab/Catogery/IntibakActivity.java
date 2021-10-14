package com.example.yazilimlab.Catogery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yazilimlab.R;
import com.example.yazilimlab.RegisterActivity;
import com.example.yazilimlab.StudentHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class IntibakActivity extends AppCompatActivity {


    //EditText
    private EditText editIntibakOldSchool, editIntibakOldFaculty, editIntibakOldBranch;
    private String strEditIntibakOldSchool, strEditIntibakOldFaculty, strEditIntibakOldBranch;

    // Firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;

    private int tableRow = 16;
    private static final int CREATEPDF = 1;

    // daha önce aldığım
    ArrayList<String> oldLessonList;
    private EditText editIntibakOldLesson, editIntibakOldLessonT, editIntibakOldLessonUL, editIntibakOldLessonK, editIntibakOldLessonAKTS;
    private String strOldLessonName, strOldLessonT, strOldLessonUL, strOldLessonK, strOldLessonAKTS;

    // muaf olmak istediğim
    ArrayList<String> exemptLessonList;
    private EditText editIntibakExemptLessonCode, editIntibakExemptLessonName, editIntibakExemptLessonT, editIntibakExemptLessonUL, editIntibakExemptLessonK, editIntibakExemptLessonAKTS;
    private String strExemptLessonCode, strExemptLessonName, strExemptLessonT, strExemptLessonUL, strExemptLessonK, strExemptLessonAKTS;

    private void init() {

        //Firebase
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //EditText
        editIntibakOldSchool = (EditText) findViewById(R.id.editIntibakOldSchool);
        editIntibakOldFaculty = (EditText) findViewById(R.id.editIntibakOldFaculty);
        editIntibakOldBranch = (EditText) findViewById(R.id.editIntibakOldBranch);

        // daha önce aldığım
        oldLessonList = new ArrayList<String>();
        editIntibakOldLesson = (EditText) findViewById(R.id.editIntibakOldLesson);
        editIntibakOldLessonT = (EditText) findViewById(R.id.editIntibakOldLessonT);
        editIntibakOldLessonUL = (EditText) findViewById(R.id.editIntibakOldLessonUL);
        editIntibakOldLessonK = (EditText) findViewById(R.id.editIntibakOldLessonK);
        editIntibakOldLessonAKTS = (EditText) findViewById(R.id.editIntibakOldLessonAKTS);

        // muaf olmak istediğim
        exemptLessonList = new ArrayList<String>();
        editIntibakExemptLessonCode = (EditText) findViewById(R.id.editIntibakExemptLessonCode);
        editIntibakExemptLessonName = (EditText) findViewById(R.id.editIntibakExemptLessonName);
        editIntibakExemptLessonT = (EditText) findViewById(R.id.editIntibakExemptLessonT);
        editIntibakExemptLessonUL = (EditText) findViewById(R.id.editIntibakExemptLessonUL);
        editIntibakExemptLessonK = (EditText) findViewById(R.id.editIntibakExemptLessonK);
        editIntibakExemptLessonAKTS = (EditText) findViewById(R.id.editIntibakExemptLessonAKTS);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intibak);
        init();
    }

    public void initPdf(String title) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        startActivityForResult(intent, CREATEPDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATEPDF) {
            if (data.getData() != null) {

                Uri uri = data.getData();


                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                Paint s = new Paint();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(210, 297, 1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(4);
                paint.setFakeBoldText(true);

                canvas.drawText("T.C.", pageInfo.getPageWidth() / 2, 20, paint);
                canvas.drawText("KOCAELİ ÜNİVERSİTESİ", pageInfo.getPageWidth() / 2, 26, paint);
                canvas.drawText("TEKNOLOJİ FAKÜLTESİ DEKANLIĞINA", pageInfo.getPageWidth() / 2, 32, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(3);
                paint.setFakeBoldText(false);

                //table1 row
                canvas.drawLine(30, 40, 180, 40, paint);
                canvas.drawLine(30, 45, 180, 45, paint);
                canvas.drawLine(30, 50, 180, 50, paint);
                canvas.drawLine(30, 55, 180, 55, paint);
                canvas.drawLine(30, 60, 180, 60, paint);
                canvas.drawLine(30, 65, 180, 65, paint);
                canvas.drawLine(30, 70, 180, 70, paint);
                canvas.drawLine(30, 75, 180, 75, paint);
                //table1 context
                canvas.drawText("KİMLİK BİLGİLERİ ( Tüm alanları doldurunuz )", 33, 44, paint);
                canvas.drawText("Adı ve Soyadı", 33, 49, paint);
                canvas.drawText("Öğrenci No", 33, 54, paint);
                canvas.drawText("Bölümü", 33, 59, paint);
                canvas.drawText("Telefon Numarası", 33, 64, paint);
                canvas.drawText("E-posta Adresi", 33, 69, paint);
                canvas.drawText("Yazışma Adresi", 33, 74, paint);
                //table1 column
                canvas.drawLine(30, 40, 30, 75, paint);
                canvas.drawLine(90, 45, 90, 75, paint);
                canvas.drawLine(180, 40, 180, 75, paint);

                paint.setTextSize(4);
                canvas.drawText("       Daha önce " + strEditIntibakOldSchool + " Üniversitesi " + strEditIntibakOldFaculty + " Fakültesi / Meslek ", 45, 85, paint);
                canvas.drawText("Yüksek Okulu " + strEditIntibakOldBranch + " Bölümünde / Programında aldığım ve ", 45, 90, paint);
                canvas.drawText("aşağıda belirttiğim ders / derslerden muaf olmak istiyorum.", 45, 95, paint);
                canvas.drawText("       Gereğinin yapılmasını arz ederim.", 45, 100, paint);
                canvas.drawText("Tarih:", 150, 107, paint);
                canvas.drawText("İmza:", 150, 115, paint);


                // table arraylist Daha once aldığım ders
                int point = 145;
                for (int i = 0; i < oldLessonList.size(); i++) {
                    String[] temp = oldLessonList.get(i).toString().split(";");
                    point += 5;
                    canvas.drawText(temp[0], 31, point, paint);   // Adı
                    canvas.drawText(temp[1], 81, point, paint);   // T
                    canvas.drawText(temp[2], 86, point, paint);   // U/L
                    canvas.drawText(temp[3], 91, point, paint);   // K
                    canvas.drawText(temp[4], 96, point, paint);   // AKTS
                }

                // table arraylist Daha once aldığım ders
                for (int i = 0; i < exemptLessonList.size(); i++) {
                    String[] temp = exemptLessonList.get(i).toString().split(";");
                    point += 5;
                    canvas.drawText(temp[0], 103, point, paint);   // Kod
                    canvas.drawText(temp[1], 116, point, paint);   // Ad
                    canvas.drawText(temp[2], 161, point, paint);   // T
                    canvas.drawText(temp[3], 165, point, paint);   // U/L
                    canvas.drawText(temp[4], 171, point, paint);   // K
                    canvas.drawText(temp[4], 176, point, paint);   // AKTS
                }

                //table2 row
                canvas.drawLine(30, 120, 180, 120, paint);
                canvas.drawLine(30, 130, 180, 130, paint);
                canvas.drawLine(30, 145, 180, 145, paint);
                //16
                canvas.drawLine(30, 150, 180, 150, paint);
                canvas.drawLine(30, 155, 180, 155, paint);
                canvas.drawLine(30, 160, 180, 160, paint);
                canvas.drawLine(30, 165, 180, 165, paint);
                canvas.drawLine(30, 170, 180, 170, paint);
                canvas.drawLine(30, 175, 180, 175, paint);
                canvas.drawLine(30, 180, 180, 180, paint);
                canvas.drawLine(30, 185, 180, 185, paint);
                canvas.drawLine(30, 190, 180, 190, paint);
                canvas.drawLine(30, 195, 180, 195, paint);
                canvas.drawLine(30, 200, 180, 200, paint);
                canvas.drawLine(30, 205, 180, 205, paint);
                canvas.drawLine(30, 210, 180, 210, paint);
                canvas.drawLine(30, 215, 180, 215, paint);
                canvas.drawLine(30, 220, 180, 220, paint);
                canvas.drawLine(30, 225, 180, 225, paint);

                //table2 context
                canvas.drawText("Daha Önce Aldığım Dersin", 33, 125, paint);
                canvas.drawText("Bilişim Sistemleri Mühendisliği Bölümünde", 103, 124, paint);
                canvas.drawText("Muaf Olmak İstediğim", 103, 129, paint);
                canvas.drawText("ADI", 50, 140, paint);
                paint.setTextSize(3);
                canvas.drawText("T", 81, 138, paint);
                canvas.drawText("U", 86, 138, paint);
                canvas.drawText("L", 86, 142, paint);
                canvas.drawText("K", 91, 138, paint);
                canvas.drawText("A", 96, 132, paint);
                canvas.drawText("K", 96, 136, paint);
                canvas.drawText("T", 96, 140, paint);
                canvas.drawText("S", 96, 144, paint);
                paint.setTextSize(4);
                canvas.drawText("KOD", 103, 140, paint);
                canvas.drawText("ADI", 135, 140, paint);
                paint.setTextSize(3);
                canvas.drawText("T", 161, 138, paint);
                canvas.drawText("U", 166, 138, paint);
                canvas.drawText("L", 166, 142, paint);
                canvas.drawText("K", 171, 138, paint);
                canvas.drawText("A", 176, 132, paint);
                canvas.drawText("K", 176, 136, paint);
                canvas.drawText("T", 176, 140, paint);
                canvas.drawText("S", 176, 144, paint);
                paint.setTextSize(4);
                //table2 column
                canvas.drawLine(30, 120, 30, 225, paint);
                canvas.drawLine(80, 130, 80, 225, paint);
                canvas.drawLine(85, 130, 85, 225, paint);
                canvas.drawLine(90, 130, 90, 225, paint);
                canvas.drawLine(95, 130, 95, 225, paint);
                canvas.drawLine(100, 120, 100, 225, paint);//orta
                canvas.drawLine(115, 130, 115, 225, paint);
                canvas.drawLine(160, 130, 160, 225, paint);
                canvas.drawLine(165, 130, 165, 225, paint);
                canvas.drawLine(170, 130, 170, 225, paint);
                canvas.drawLine(175, 130, 175, 225, paint);
                canvas.drawLine(180, 120, 180, 225, paint);

                canvas.drawText("T: Teorik Ders Saati  U / L : Uygulama / Laboratuvar Saati  K : Kredi", 30, 230, paint);
                paint.setFakeBoldText(true);
                canvas.drawText("Eklenecek Belge/Belgeler:", 50, 240, paint);
                paint.setFakeBoldText(false);
                paint.setTextSize(3);
                canvas.drawText("1-\tTranskript Belgesi (Onaylı)", 55, 245, paint);
                canvas.drawText("2-\tOnaylı Ders İçerikleri", 55, 248, paint);

                pdfDocument.finishPage(page);
                setPdf(uri, pdfDocument);

            }
        }
    }

    // pdf kaydet
    private void setPdf(Uri uri, PdfDocument pdfDocument) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(Objects.requireNonNull(getContentResolver().openOutputStream(uri)));
            pdfDocument.writeTo(stream);
            pdfDocument.close();
            stream.flush();
            Toast.makeText(this, "Pdf oluşturuldu", Toast.LENGTH_LONG).show();
            startActivity(new Intent(IntibakActivity.this, StudentHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Dosya hatası bulunamadı", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Giriş ve çıkış hatası", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Bilinmeyen hata" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    // pdf Start
    public void createPdf(View view) {
        if (isNotEmptyString()) {
            initPdf("intibak");
        } else {
            Toast.makeText(IntibakActivity.this, "Boş alanlar var", Toast.LENGTH_SHORT).show();
        }
    }
    // pdf end

    // editText set str
    private void setTextString() {
        strEditIntibakOldSchool = editIntibakOldSchool.getText().toString();
        strEditIntibakOldFaculty = editIntibakOldFaculty.getText().toString();
        strEditIntibakOldBranch = editIntibakOldBranch.getText().toString();
    }

    // input bos kontrolu editText
    private boolean isNotEmptyString() {
        setTextString();
        boolean result = TextUtils.isEmpty(strEditIntibakOldSchool) || TextUtils.isEmpty(strEditIntibakOldFaculty) || TextUtils.isEmpty(strEditIntibakOldBranch)
                || oldLessonList.size() == 0 || exemptLessonList.size() == 0;
        if (result)
            return false;
        return true;
    }

    // daha önce aldığım start
    private void setTextStringOldLesson() {
        strOldLessonName = editIntibakOldLesson.getText().toString();
        strOldLessonT = editIntibakOldLessonT.getText().toString();
        strOldLessonUL = editIntibakOldLessonUL.getText().toString();
        strOldLessonK = editIntibakOldLessonK.getText().toString();
        strOldLessonAKTS = editIntibakOldLessonAKTS.getText().toString();
    }

    private boolean isNotEmptyOldLesson() {
        setTextStringOldLesson();
        boolean result = TextUtils.isEmpty(strOldLessonName) || TextUtils.isEmpty(strOldLessonT) || TextUtils.isEmpty(strOldLessonUL) || TextUtils.isEmpty(strOldLessonK) || TextUtils.isEmpty(strOldLessonAKTS);

        if (result)
            return false;
        return true;
    }

    private void oldLessonTextDelete() {
        editIntibakOldLesson.setText("");
        editIntibakOldLessonT.setText("");
        editIntibakOldLessonUL.setText("");
        editIntibakOldLessonK.setText("");
        editIntibakOldLessonAKTS.setText("");
    }

    public void IntibakOldLessonAdd(View view) {

        if (oldLessonList.size() < tableRow) {
            if (isNotEmptyOldLesson()) {
                String temp = strOldLessonName + ";" + strOldLessonT + ";" + strOldLessonUL + ";" + strOldLessonK + ";" + strOldLessonAKTS;
                oldLessonList.add(temp);

                for (int i = 0; i < oldLessonList.size(); i++) {
                    System.out.println(oldLessonList.get(i));
                }
                System.out.println("----------------------------------------------");
                oldLessonTextDelete();
            } else {
                Toast.makeText(IntibakActivity.this, "Daha önce aldığım ders kısmında boş alanlar var", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(IntibakActivity.this, String.valueOf(tableRow) + " adet ekliyebilirsin", Toast.LENGTH_SHORT).show();
        }


    }
    // daha önce aldığım end


    // muaf olmak istediğim start
    private void setTextStringExemptLesson() {
        strExemptLessonCode = editIntibakExemptLessonCode.getText().toString();
        strExemptLessonName = editIntibakExemptLessonName.getText().toString();
        strExemptLessonT = editIntibakExemptLessonT.getText().toString();
        strExemptLessonUL = editIntibakExemptLessonUL.getText().toString();
        strExemptLessonK = editIntibakExemptLessonK.getText().toString();
        strExemptLessonAKTS = editIntibakExemptLessonAKTS.getText().toString();
    }

    private boolean isNotEmptyExemptLesson() {
        setTextStringExemptLesson();
        boolean result = TextUtils.isEmpty(strExemptLessonCode) || TextUtils.isEmpty(strExemptLessonName) || TextUtils.isEmpty(strExemptLessonT) || TextUtils.isEmpty(strExemptLessonUL) || TextUtils.isEmpty(strExemptLessonK) || TextUtils.isEmpty(strExemptLessonAKTS);

        if (result)
            return false;
        return true;
    }

    private void exemptLessonTextDelete() {
        editIntibakExemptLessonCode.setText("");
        editIntibakExemptLessonName.setText("");
        editIntibakExemptLessonT.setText("");
        editIntibakExemptLessonUL.setText("");
        editIntibakExemptLessonK.setText("");
        editIntibakExemptLessonAKTS.setText("");
    }

    public void IntibakExemptLessonAdd(View view) {

        if (exemptLessonList.size() < tableRow) {
            if (isNotEmptyExemptLesson()) {
                String temp = strExemptLessonCode + ";" + strExemptLessonName + ";" + strExemptLessonT + ";" + strExemptLessonUL + ";" + strExemptLessonK + ";" + strExemptLessonAKTS;
                exemptLessonList.add(temp);

                for (int i = 0; i < exemptLessonList.size(); i++) {
                    System.out.println(exemptLessonList.get(i));
                }
                System.out.println("----------------------------------------------");
                exemptLessonTextDelete();
            } else {
                Toast.makeText(IntibakActivity.this, "Muaf olmak istediğim kısmında boş alanlar var", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(IntibakActivity.this, String.valueOf(tableRow) + " adet ekliyebilirsin", Toast.LENGTH_SHORT).show();
        }
    }
    // muaf olmak istediğim end

}