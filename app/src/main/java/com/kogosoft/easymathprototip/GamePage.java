package com.kogosoft.easymathprototip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GamePage extends AppCompatActivity {

    Handler handler=new Handler();
    Timer t;
    TimerTask tt;
    private ProgressBar time;
    int ct;
    private ImageView imageViewfalse,imageViewtrue;
    private ImageView iv1,iv2,iv3,iv4,iv5,iv6,iv7,iv8,iv9,iv10,iv11,iv12,iv13,iv14,iv15,iv16,iv17,iv18;
    private TextView textViewOperator;
    private Button buttonA,buttonB,buttonC,buttonD,buttonEquals;
    private TextView textViewQuestion, textViewQuestionCounter, textViewTime, textViewLevelCount, textViewQuestion_part1,textViewQuestion_part2,textViewQuestion_part3;
    private int questionCounter = 0, randomAds;
    private HashSet<Integer> mixedAnswers = new HashSet<>();
    private ArrayList<Integer> answers = new ArrayList<>();
    private int submittedLevel,control=0, allTime;
    private String submittedCategory , buttonText , question;
    private int a,b,c,operator = 0, min,max,result,wrongAnswer, resultCat4, result1, result2, adsCounter=1;
    private String correctAnswerText, resultCat2, resultCat3, resultCat5;
    // operator:  0 sum, 1 subtraction , 2 divide
    private boolean isAdsWatchedOrNot=false;
    private RewardedAd mRewardedAd;
    private final String TAG = "ResultPage";
    private ConstraintLayout constraintLayoutParts,constraintLayout1,constraintLayout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        {
            textViewTime = findViewById(R.id.textViewTime);
            textViewLevelCount = findViewById(R.id.textViewLevelCount);
            time = findViewById(R.id.time);
            buttonA = findViewById(R.id.buttonA);
            buttonB = findViewById(R.id.buttonB);
            buttonC = findViewById(R.id.buttonC);
            buttonD = findViewById(R.id.buttonD);
            textViewQuestion = findViewById(R.id.textViewQuestion);
            textViewOperator = findViewById(R.id.textViewOperator);
            textViewQuestionCounter = findViewById(R.id.textViewQuestionCounter);
            buttonEquals = findViewById(R.id.buttonEquals);
            imageViewtrue = findViewById(R.id.imageViewtrue);
            imageViewfalse = findViewById(R.id.imageViewfalse);
            textViewQuestion_part1 = findViewById(R.id.textViewQuestion_part1);
            textViewQuestion_part2 = findViewById(R.id.textViewQuestion_part2);
            textViewQuestion_part3 = findViewById(R.id.textViewQuestion_part3);
            constraintLayoutParts = findViewById(R.id.constraintLayoutParts);
            constraintLayout1 = findViewById(R.id.constraintLayout1);
            constraintLayout3 = findViewById(R.id.constraintLayout3);

            iv1 = findViewById(R.id.iv1);
            iv2 = findViewById(R.id.iv2);
            iv3 = findViewById(R.id.iv3);
            iv4 = findViewById(R.id.iv4);
            iv5 = findViewById(R.id.iv5);
            iv6 = findViewById(R.id.iv6);
            iv7 = findViewById(R.id.iv7);
            iv8 = findViewById(R.id.iv8);
            iv9 = findViewById(R.id.iv9);
            iv10 = findViewById(R.id.iv10);
            iv11 = findViewById(R.id.iv11);
            iv12 = findViewById(R.id.iv12);
            iv13 = findViewById(R.id.iv13);
            iv14 = findViewById(R.id.iv14);
            iv15 = findViewById(R.id.iv15);
            iv16 = findViewById(R.id.iv16);
            iv17 = findViewById(R.id.iv17);
            iv18 = findViewById(R.id.iv18);

        }       // view ba??lamalar??

        // ??d??l reklam?? y??klenmesi
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //loadRewardedAd();
            }
        });

        submittedLevel = getIntent().getIntExtra("chosenLevel",0);      // Gelen leveli ald??k
        submittedCategory = getIntent().getStringExtra("submittedCategory");      // Gelen kategoriyi ald??k

        Log.e("submittedLevelgame", String.valueOf(submittedLevel));
        //Log.e("submittedCategorygame", submittedCategory);

        getQuestion();      // soru getirsin dedik, sorular?? yukar??daki al??nan kategori ve levele g??re getirecek

        clickImageView(imageViewtrue);
        clickImageView(imageViewfalse);
        clickButton(buttonA);
        clickButton(buttonB);
        clickButton(buttonC);
        clickButton(buttonD);
        clickButton(buttonEquals);

    }

    // imageView se??me fonksiyonu
    public void clickImageView(ImageView imageView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlAnswerImageView(imageView);
                controlCounter();
            }
        });
    }

    // buton se??me fonksiyonu
    public void clickButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlAnswer(button);
                controlCounter();
            }
        });
    }

    public void controlAnswer(Button button){

        // get the text from pushed button
        buttonText = button.getText().toString();

        if (submittedCategory.equals("Cat1")){
            correctAnswerText = String.valueOf(result);
        }
        else if (submittedCategory.equals("Cat2")){
            correctAnswerText = String.valueOf(resultCat2);
        }
        else if (submittedCategory.equals("Cat3")){
            correctAnswerText = String.valueOf(resultCat3);
        }
        else if (submittedCategory.equals("Cat4")){
            correctAnswerText = String.valueOf(resultCat4);
        }
        else{   // Cat5
            correctAnswerText = String.valueOf(resultCat5);
        }

        // control the answer if button text and correctAnswertext equals enter this if
        if (buttonText.equals(correctAnswerText)){
            // if answer is correct,
        }
        // if answer is wrong go to result page
        else {
            control = 1;        // yanl???? ise control?? 1 yap??yoruz ve bu sayede controlCounter fonksiyonunda  if (questionCounter!=20 && control==0){  bu if in i??ine girmiyor e??er girerse tekrar soru getiriyor biz getirmesini istemiyoruz
            t.cancel();
            tt.cancel();

            Random random = new Random();
            randomAds = random.nextInt(3);// 0 not show ads, 1 show ads, 2 show ads     2/3 ihtimalle reklam verecek

            if (questionCounter>0 && adsCounter<3 && randomAds!=0){     // e??er soru say??s?? belli bir soruyu ge??tiyse ve ayn?? levelde g??sterilen reklam say??s?? 3 ten az ise buraya girip reklam g??sterir
                Log.e("randomAds",String.valueOf(randomAds));
                //showRewardedAd();
                Log.e("adsCounter",String.valueOf(adsCounter));
            }else{
                Intent intentt = new Intent(GamePage.this, ResultPage.class); // do??ru cevap yanl???? cevab?? g??nder
                intentt.putExtra("rightAnswer", correctAnswerText);
                intentt.putExtra("chosenAnswer", buttonText);
                intentt.putExtra("lastQuestion", "notFinished");
                intentt.putExtra("question", question);
                intentt.putExtra("submittedCategory", submittedCategory);
                intentt.putExtra("chosenLevel",submittedLevel);
                startActivity(intentt);
                finish();
            }

        }
    }

    public void controlAnswerImageView(ImageView imageView){

        String imageViewName ;
        switch (imageView.getId()) {
            case R.id.imageViewtrue:
                imageViewName = "imageViewtrue";
                break;
            case R.id.imageViewfalse:
                imageViewName = "imageViewfalse";
                break;
            default:
                imageViewName = "null";
                break;
        }


        if (submittedCategory.equals("Cat1")){
            correctAnswerText = String.valueOf(result);
        }
        else if (submittedCategory.equals("Cat2")){
            correctAnswerText = String.valueOf(resultCat2);
        }
        else if (submittedCategory.equals("Cat3")){
            correctAnswerText = String.valueOf(resultCat3);
        }
        else if (submittedCategory.equals("Cat4")){
            correctAnswerText = String.valueOf(resultCat4);
        }
        else{   // Cat5
            correctAnswerText = String.valueOf(resultCat5);
        }


        if (imageViewName.equals("imageViewtrue") && correctAnswerText=="+"){

        }
        else if(imageViewName.equals("imageViewfalse") && correctAnswerText=="-"){

        }
        // if answer is wrong go to result page
        else {
            control = 1;
            t.cancel();
            tt.cancel();

            Intent intentt = new Intent(GamePage.this, ResultPage.class); // do??ru cevap yanl???? cevab?? g??nder
            intentt.putExtra("rightAnswer", correctAnswerText);

            if (imageViewName.equals("imageViewtrue")){
                intentt.putExtra("chosenAnswer", "+");
            }else{
                intentt.putExtra("chosenAnswer", "-");
            }

            intentt.putExtra("submittedCategory", submittedCategory);
            intentt.putExtra("chosenLevel",submittedLevel);
            intentt.putExtra("lastQuestion", "notFinished");
            startActivity(intentt);
            finish();

        }
    }

    public void controlCounter(){
        questionCounter+=1;
        // if not last question enter this if and get question
        if (questionCounter!=20 && control==0){     //  control==0  this is for if wrong answer, dont show any question because controlCounter works and getQuestion if questionCounter = -1 wont work
            getQuestion();
            Log.e("questionnn",question);
        }

        // if last question and true answer enter this if and complete this level open next level, go to result page
        if(questionCounter==20 && control==0) {

            t.cancel();
            tt.cancel();
            // e??er kullan??c?? 20 soruyu da do??ru bildiyse o kategorinin di??er leveli a????lacak ??ekilde veritaban??nda tutuyoruz
            if (submittedCategory.equals("Cat1")){
                if (submittedLevel==1){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat1Lev1Finished", "unlockCat1Lev2");

                    myEdit.commit();
                }
                if (submittedLevel==2){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat1Lev2Finished", "unlockCat1Lev3");
                    myEdit.commit();
                }
                if (submittedLevel==3){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat1Lev3Finished", "unlockCat1Lev4");
                    myEdit.commit();
                }
                if (submittedLevel==4){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat1Lev4Finished", "unlockCat1Lev5");
                    myEdit.commit();
                }

            }
            if (submittedCategory.equals("Cat2")){
                if (submittedLevel==1){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat2Lev1Finished", "unlockCat2Lev2");
                    myEdit.commit();
                }
                if (submittedLevel==2){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat2Lev2Finished", "unlockCat2Lev3");
                    myEdit.commit();
                }
                if (submittedLevel==3){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat2Lev3Finished", "unlockCat2Lev4");
                    myEdit.commit();
                }
                if (submittedLevel==4){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat2Lev4Finished", "unlockCat2Lev5");
                    myEdit.commit();
                }

            }
            if (submittedCategory.equals("Cat3")){
                if (submittedLevel==1){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat3Lev1Finished", "unlockCat3Lev2");
                    myEdit.commit();
                }
                if (submittedLevel==2){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat3Lev2Finished", "unlockCat3Lev3");
                    myEdit.commit();
                }
                if (submittedLevel==3){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat3Lev3Finished", "unlockCat3Lev4");
                    myEdit.commit();
                }
                if (submittedLevel==4){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat3Lev4Finished", "unlockCat3Lev5");
                    myEdit.commit();
                }

            }
            if (submittedCategory.equals("Cat4")){
                if (submittedLevel==1){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat4Lev1Finished", "unlockCat4Lev2");
                    myEdit.commit();
                }
                if (submittedLevel==2){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat4Lev2Finished", "unlockCat4Lev3");
                    myEdit.commit();
                }
                if (submittedLevel==3){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat4Lev3Finished", "unlockCat4Lev4");
                    myEdit.commit();
                }
                if (submittedLevel==4){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat4Lev4Finished", "unlockCat4Lev5");
                    myEdit.commit();
                }

            }
            if (submittedCategory.equals("Cat5")){
                if (submittedLevel==1){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat5Lev1Finished", "unlockCat5Lev2");
                    myEdit.commit();
                }
                if (submittedLevel==2){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat5Lev2Finished", "unlockCat5Lev3");
                    myEdit.commit();
                }
                if (submittedLevel==3){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat5Lev3Finished", "unlockCat5Lev4");
                    myEdit.commit();
                }
                if (submittedLevel==4){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("cat5Lev4Finished", "unlockCat5Lev5");
                    myEdit.commit();
                }

            }

            questionCounter = 0;
            Intent intentt = new Intent(GamePage.this, ResultPage.class); // do??ru cevap yanl???? cevab?? g??nder
            intentt.putExtra("lastQuestion", "finished");
            intentt.putExtra("submittedCategory", submittedCategory);
            intentt.putExtra("chosenLevel",submittedLevel);
            startActivity(intentt);
            finish();

        }
    }

    // if user dont push the button at first question, we must know correct answer so we take correct answer
    // e??er kullan??c?? ilk soruda hi??bir butona basmaz ise butonlar??n fonksiyonlar?? ??al????maz sadece s??re biter ama bizim do??ru cevab?? ????renmemiz laz??m o y??zden bu fonksiyon var do??ru cevap ne onu ????reniyoruz
    public void correctAnswer(){

        {
            if (submittedCategory.equals("Cat1")){
                correctAnswerText = String.valueOf(result);
            }
            else if (submittedCategory.equals("Cat2")){
                correctAnswerText = String.valueOf(resultCat2);
            }
            else if (submittedCategory.equals("Cat3")){
                correctAnswerText = String.valueOf(resultCat3);
            }
            else if (submittedCategory.equals("Cat4")){
                correctAnswerText = String.valueOf(resultCat4);
            }
            else{   // Cat5
                correctAnswerText = String.valueOf(resultCat5);
            }
        }
    }

    public void getQuestion(){
        textViewQuestionCounter.setText("Q: " + (questionCounter+1));
        ct=100;
        if (t==null || tt==null){                                   // ilk ??al????mada ikiside null olaca???? i??in buraya girecek
            timeStart();            // soru gelir gelmez zaman ba??layacak
        }else {
            tt.cancel();                                            // timer task ?? cancel yaparsak ??uan ??al????an i??lemi sonland??r??yor mesela kullan??c?? 4 saniye kala butona basarsa daha 4 saniyelik i??lem var ancak onu sonland??r??yor
            t.cancel();                                             // timer ?? cancel yaparsak b??t??n i??lemleri sonland??r??yor ancak ??uan ??al????an i??lemi sonland??rm??yor o bittikten sonra sonland??r??yor, mesela kullan??c?? 4 saniye kala butona basarsa o 4 saniye bittikten sonra sonland??r??yor
            timeStart();
        }

        // get question from category 1
        if (submittedCategory.equals("Cat1")){
            getQuestionCat1(submittedCategory , submittedLevel);
        }

        // get question from category 2
        if (submittedCategory.equals("Cat2")){
            getQuestionCat2(submittedCategory , submittedLevel);
        }

        // get question from category 3
        if (submittedCategory.equals("Cat3")){
            getQuestionCat3(submittedCategory , submittedLevel);
        }

        // get question from category 4
        if (submittedCategory.equals("Cat4")){
            getQuestionCat4(submittedCategory , submittedLevel);
        }

        // get question from category 5
        if (submittedCategory.equals("Cat5")){
            getQuestionCat5(submittedCategory , submittedLevel);
        }

    }

    // calculate 2 process  example:     1*4=?
    public void getQuestionCat1( String category, int level)
    {
        textViewQuestion.setVisibility(View.INVISIBLE);

        Random r = new Random();    // r.nextInt((max - min) + 1) + min

        operator = r.nextInt(4);
        if(level==1 && category.equals("Cat1")){ min=1; max=5; textViewLevelCount.setText("L: 1"); operator = r.nextInt(2);}  // if chosen level 1 and category 1
        if(level==2 && category.equals("Cat1")){ min=1; max=9; textViewLevelCount.setText("L: 2"); operator = r.nextInt(2);}  // if chosen level 2 and category 1

        // a????klamal?? yorum sat??lar?? operator 1 i??erisinde yer al??yor di??erleri benzer i??lemler zaten, sadece b??lme i??leminde 2 adet farkl?? yorum sat??r?? bulunmaktad??r
        if (operator == 0) {                                                                 // 0 is sum

            if (level == 1 && category.equals("Cat1")) {
                min = 1;
                max = 5;
                textViewLevelCount.setText("L: 1");
                textViewQuestion.setVisibility(View.VISIBLE);
                textViewOperator.setVisibility(View.VISIBLE);
            }  // if chosen level 1 and category 1
            if (level == 2 && category.equals("Cat1")) {
                min = 1;
                max = 9;
                textViewLevelCount.setText("L: 2");

                textViewQuestion.setVisibility(View.VISIBLE);
                textViewOperator.setVisibility(View.VISIBLE);


            }  // if chosen level 2 and category 1
            if (level == 3 && category.equals("Cat1")) {
                min = 1;
                max = 20;
                textViewLevelCount.setText("L: 3");
                textViewQuestion.setVisibility(View.VISIBLE);
                textViewOperator.setVisibility(View.INVISIBLE);
                /*constraintLayoutParts.setVisibility(View.INVISIBLE);
                textViewOperator.setVisibility(View.INVISIBLE);
                textViewQuestion.setVisibility(View.VISIBLE);
                constraintLayout1.setVisibility(View.INVISIBLE);
                constraintLayout3.setVisibility(View.INVISIBLE);*/
            }  // if chosen level 3 and category 1
            if (level == 4 && category.equals("Cat1")) {
                min = 1;
                max = 25;
                textViewLevelCount.setText("L: 4");
                textViewQuestion.setVisibility(View.VISIBLE);
                textViewOperator.setVisibility(View.INVISIBLE);
               /* constraintLayoutParts.setVisibility(View.INVISIBLE);
                textViewOperator.setVisibility(View.INVISIBLE);
                textViewQuestion.setVisibility(View.VISIBLE);
                constraintLayout1.setVisibility(View.INVISIBLE);
                constraintLayout3.setVisibility(View.INVISIBLE);*/
            }  // if chosen level 4 and category 1
            if (level == 5 && category.equals("Cat1")) {
                min = 1;
                max = 30;
                textViewLevelCount.setText("L: 5");
                textViewQuestion.setVisibility(View.VISIBLE);
                textViewOperator.setVisibility(View.INVISIBLE);
             /*   constraintLayoutParts.setVisibility(View.INVISIBLE);
                textViewOperator.setVisibility(View.INVISIBLE);
                textViewQuestion.setVisibility(View.VISIBLE);
                constraintLayout1.setVisibility(View.INVISIBLE);
                constraintLayout3.setVisibility(View.INVISIBLE);*/
            }  // if chosen level 5 and category 1

            int n1 = r.nextInt(max) + min;
            int n2 = r.nextInt(max) + min;

            Log.e("n1", String.valueOf(n1));
            Log.e("n2", String.valueOf(n2));

            textViewQuestion_part1.setText(String.valueOf(n1));
            textViewQuestion_part2.setText("+");
            textViewQuestion_part3.setText(String.valueOf(n2));


            textViewQuestion.setText("" + n1 + " + " + n2 + " = ?");
            textViewOperator.setText("+");
            question = "" + n1 + " + " + n2 + " = ?";

            result = n1 + n2;

            //textViewOperator.setVisibility(View.VISIBLE);

            iv1.setVisibility(View.INVISIBLE);
            iv2.setVisibility(View.INVISIBLE);
            iv3.setVisibility(View.INVISIBLE);
            iv4.setVisibility(View.INVISIBLE);
            iv5.setVisibility(View.INVISIBLE);
            iv6.setVisibility(View.INVISIBLE);
            iv7.setVisibility(View.INVISIBLE);
            iv8.setVisibility(View.INVISIBLE);
            iv9.setVisibility(View.INVISIBLE);
            iv10.setVisibility(View.INVISIBLE);
            iv11.setVisibility(View.INVISIBLE);
            iv12.setVisibility(View.INVISIBLE);
            iv13.setVisibility(View.INVISIBLE);
            iv14.setVisibility(View.INVISIBLE);
            iv15.setVisibility(View.INVISIBLE);
            iv16.setVisibility(View.INVISIBLE);
            iv17.setVisibility(View.INVISIBLE);
            iv18.setVisibility(View.INVISIBLE);

            if((level==1 && category.equals("Cat1")) || ((level==2 && category.equals("Cat1")))){

            if (n1 == 1) {
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.INVISIBLE);
                iv3.setVisibility(View.INVISIBLE);
                iv4.setVisibility(View.INVISIBLE);
                iv5.setVisibility(View.INVISIBLE);
                iv6.setVisibility(View.INVISIBLE);
                iv7.setVisibility(View.INVISIBLE);
                iv8.setVisibility(View.INVISIBLE);
                iv9.setVisibility(View.INVISIBLE);

                Log.e("girdi ", "1");
            }
            if (n1 == 2) {
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.INVISIBLE);
                iv4.setVisibility(View.INVISIBLE);
                iv5.setVisibility(View.INVISIBLE);
                iv6.setVisibility(View.INVISIBLE);
                iv7.setVisibility(View.INVISIBLE);
                iv8.setVisibility(View.INVISIBLE);
                iv9.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "2");
            }
            if (n1 == 3) {
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.VISIBLE);
                iv4.setVisibility(View.INVISIBLE);
                iv5.setVisibility(View.INVISIBLE);
                iv6.setVisibility(View.INVISIBLE);
                iv7.setVisibility(View.INVISIBLE);
                iv8.setVisibility(View.INVISIBLE);
                iv9.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "3");
            }
            if (n1 == 4) {
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.VISIBLE);
                iv4.setVisibility(View.VISIBLE);
                iv5.setVisibility(View.INVISIBLE);
                iv6.setVisibility(View.INVISIBLE);
                iv7.setVisibility(View.INVISIBLE);
                iv8.setVisibility(View.INVISIBLE);
                iv9.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "4");
            }

            if (n1 == 5) {
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.VISIBLE);
                iv4.setVisibility(View.VISIBLE);
                iv5.setVisibility(View.VISIBLE);
                iv6.setVisibility(View.INVISIBLE);
                iv7.setVisibility(View.INVISIBLE);
                iv8.setVisibility(View.INVISIBLE);
                iv9.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "5");
            }
            if (n1 == 6) {
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.VISIBLE);
                iv4.setVisibility(View.VISIBLE);
                iv5.setVisibility(View.VISIBLE);
                iv6.setVisibility(View.VISIBLE);
                iv7.setVisibility(View.INVISIBLE);
                iv8.setVisibility(View.INVISIBLE);
                iv9.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "6");
            }
            if (n1 == 7) {
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.VISIBLE);
                iv4.setVisibility(View.VISIBLE);
                iv5.setVisibility(View.VISIBLE);
                iv6.setVisibility(View.VISIBLE);
                iv7.setVisibility(View.VISIBLE);
                iv8.setVisibility(View.INVISIBLE);
                iv9.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "7");
            }
            if (n1 == 8) {
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.VISIBLE);
                iv4.setVisibility(View.VISIBLE);
                iv5.setVisibility(View.VISIBLE);
                iv6.setVisibility(View.VISIBLE);
                iv7.setVisibility(View.VISIBLE);
                iv8.setVisibility(View.VISIBLE);
                iv9.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "8");
            }
            if (n1 == 9) {
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.VISIBLE);
                iv4.setVisibility(View.VISIBLE);
                iv5.setVisibility(View.VISIBLE);
                iv6.setVisibility(View.VISIBLE);
                iv7.setVisibility(View.VISIBLE);
                iv8.setVisibility(View.VISIBLE);
                iv9.setVisibility(View.VISIBLE);
                Log.e("girdi ", "9");
            }


            if (n2 == 1) {
                iv10.setVisibility(View.VISIBLE);
                Log.e("girdi ", "10");
                iv11.setVisibility(View.INVISIBLE);
                iv12.setVisibility(View.INVISIBLE);
                iv13.setVisibility(View.INVISIBLE);
                iv14.setVisibility(View.INVISIBLE);
                iv15.setVisibility(View.INVISIBLE);
                iv16.setVisibility(View.INVISIBLE);
                iv17.setVisibility(View.INVISIBLE);
                iv18.setVisibility(View.INVISIBLE);
            }
            if (n2 == 2) {
                iv10.setVisibility(View.VISIBLE);
                iv11.setVisibility(View.VISIBLE);
                iv12.setVisibility(View.INVISIBLE);
                iv13.setVisibility(View.INVISIBLE);
                iv14.setVisibility(View.INVISIBLE);
                iv15.setVisibility(View.INVISIBLE);
                iv16.setVisibility(View.INVISIBLE);
                iv17.setVisibility(View.INVISIBLE);
                iv18.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "11");
            }
            if (n2 == 3) {
                iv10.setVisibility(View.VISIBLE);
                iv11.setVisibility(View.VISIBLE);
                iv12.setVisibility(View.VISIBLE);
                iv13.setVisibility(View.INVISIBLE);
                iv14.setVisibility(View.INVISIBLE);
                iv15.setVisibility(View.INVISIBLE);
                iv16.setVisibility(View.INVISIBLE);
                iv17.setVisibility(View.INVISIBLE);
                iv18.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "12");
            }
            if (n2 == 4) {
                iv10.setVisibility(View.VISIBLE);
                iv11.setVisibility(View.VISIBLE);
                iv12.setVisibility(View.VISIBLE);
                iv13.setVisibility(View.VISIBLE);
                iv14.setVisibility(View.INVISIBLE);
                iv15.setVisibility(View.INVISIBLE);
                iv16.setVisibility(View.INVISIBLE);
                iv17.setVisibility(View.INVISIBLE);
                iv18.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "13");
            }

            if (n2 == 5) {
                iv10.setVisibility(View.VISIBLE);
                iv11.setVisibility(View.VISIBLE);
                iv12.setVisibility(View.VISIBLE);
                iv13.setVisibility(View.VISIBLE);
                iv14.setVisibility(View.VISIBLE);
                iv15.setVisibility(View.INVISIBLE);
                iv16.setVisibility(View.INVISIBLE);
                iv17.setVisibility(View.INVISIBLE);
                iv18.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "14");
            }
            if (n2 == 6) {
                iv10.setVisibility(View.VISIBLE);
                iv11.setVisibility(View.VISIBLE);
                iv12.setVisibility(View.VISIBLE);
                iv13.setVisibility(View.VISIBLE);
                iv14.setVisibility(View.VISIBLE);
                iv15.setVisibility(View.VISIBLE);
                iv16.setVisibility(View.INVISIBLE);
                iv17.setVisibility(View.INVISIBLE);
                iv18.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "15");
            }
            if (n2 == 7) {
                iv10.setVisibility(View.VISIBLE);
                iv11.setVisibility(View.VISIBLE);
                iv12.setVisibility(View.VISIBLE);
                iv13.setVisibility(View.VISIBLE);
                iv14.setVisibility(View.VISIBLE);
                iv15.setVisibility(View.VISIBLE);
                iv16.setVisibility(View.VISIBLE);
                iv17.setVisibility(View.INVISIBLE);
                iv18.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "16");
            }
            if (n2 == 8) {
                iv10.setVisibility(View.VISIBLE);
                iv11.setVisibility(View.VISIBLE);
                iv12.setVisibility(View.VISIBLE);
                iv13.setVisibility(View.VISIBLE);
                iv14.setVisibility(View.VISIBLE);
                iv15.setVisibility(View.VISIBLE);
                iv16.setVisibility(View.VISIBLE);
                iv17.setVisibility(View.VISIBLE);
                iv18.setVisibility(View.INVISIBLE);
                Log.e("girdi ", "17");
            }
            if (n2 == 9) {
                iv10.setVisibility(View.VISIBLE);
                iv11.setVisibility(View.VISIBLE);
                iv12.setVisibility(View.VISIBLE);
                iv13.setVisibility(View.VISIBLE);
                iv14.setVisibility(View.VISIBLE);
                iv15.setVisibility(View.VISIBLE);
                iv16.setVisibility(View.VISIBLE);
                iv17.setVisibility(View.VISIBLE);
                iv18.setVisibility(View.VISIBLE);
                Log.e("girdi ", "18");
            }
        }

            // ??retilen say??lar??n toplam?? 5 ten k??????kse burada bir i??lem yap??yoruz, yanl???? 3 se??enek do??ru se??ene??in +-5 uzakl??kta olacak mesela result 3 ise yanl???? se??eneklerden biri -1 ????kabilir
            // bunu engellemek i??in result 5 ten k??????kse yanl???? ??retilen 3 se??enek 0 dan b??y??k olsun diyoruz
            if ((result-5) <= 0){
                a = r.nextInt((result+5)) + 1;      // minimum 1 maximum 10 ??retecek yanl???? se??enekleri
                b = r.nextInt((result+5)) + 1;
                c = r.nextInt((result+5)) + 1;
                while (a==b || a==c || b==c){       // random ??retilen 3 yanl???? se??enek birbiri ile ayn?? olabilir bunu ??nlemek i??in kar????la??t??r??yoruz ve tekrar ??retiyor ne zaman farkl?? olursa o zaman ????k??yor while dan
                    a = r.nextInt((result+5)) + 1;
                    b = r.nextInt((result+5)) + 1;
                    c = r.nextInt((result+5)) + 1;
                }
                // 3 birbirinden fakl?? yanl???? se??enek ??retildi ve bunlar?? answer listemize ekliyoruz
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                // bu yanl???? se??enek listesi i??erisinde result sonucumuzdan varm?? kontrol ediyoruz yani mesela result 3 yanl???? se??enekler 4,6,3 yanl???? se??enekler birbirinden farkl?? ancak resulta e??it bir se??enek daha var o zaman iki tane do??ru sonu?? olmu?? oluyor
                // o zaman tekrar yanl???? se??enek ??retiyoruz ve tekrar birbirinden farkl?? olsun diyoruz ve de resulttanda farkl?? olsun diyoruz
                while (answers.contains(result)){
                    a = r.nextInt((result+5)) + 1;
                    b = r.nextInt((result+5)) + 1;
                    c = r.nextInt((result+5)) + 1;
                    while (a==b || a==c || b==c){
                        a = r.nextInt((result+5)) + 1;
                        b = r.nextInt((result+5)) + 1;
                        c = r.nextInt((result+5)) + 1;
                    }
                    // en sonunda 3 adet yanl???? ve birbirinden ve de resulttan farkl?? se??ene??imiz oldu, yanl???? se??enekleri answer listemize ekliyoruz
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }
            }
            else {  // burada da result 5 ten b??y??kse i??lemi yap??yoruz, resulttan 4 a??a???? 4 yukar?? yanl???? se??enekler ??retecek yani result 8 mesela en d??????k 4 en y??ksek 12 olarak yanl???? se??enekler ??retecek, yukar??daki benzer i??lemler oluyor
                a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                while (a==b || a==c || b==c){
                    a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    c = r.nextInt((result+4)-(result-4)+1) + (result-4);
            }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(result)){
                    a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    while (a==b || a==c || b==c){
                        a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                        b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                        c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }
            }
            // using hashset for mix the answers
            //yanl???? se??enekler ve result??m??z olu??turuldu bunlar?? hashsete at??yoruz ????nk?? hashset i??erisinde kar????t??r??yor, g??nderdi??in s??ra ile kalm??yor ve ayn?? iki de??eri alm??yor mesela 1,2,3,4 g??nderdik s??ra ile hashset onu 3,1,4,2 gibi kar????t??r??yor mesela
            mixedAnswers.clear();
            mixedAnswers.add(result);
            mixedAnswers.add(a);
            mixedAnswers.add(b);
            mixedAnswers.add(c);

            // add from hashset to arraylist
            // answer arraylistimizi temizleyip tekrar hashsetten al??yoruz verileri ancak hangi s??ra ile geliyor bilmiyoruz, neden tekrar arraylist i??ine ald??k ????nk?? hashsetten geri alam??yoruz istedi??imiz gibi bizde arraylistemize ekleyip ondan alaca????z
            answers.clear();
            for (int i:mixedAnswers){
                answers.add(i);
            }

            // butonlara hangi s??ra ile yaz??ld??????n?? bilmeden se??enekleri yazd??r??yoruz
            buttonA.setText(String.valueOf(answers.get(0)));
            buttonB.setText(String.valueOf(answers.get(1)));
            buttonC.setText(String.valueOf(answers.get(2)));
            buttonD.setText(String.valueOf(answers.get(3)));


        }  // 0 is sum operator
        else if (operator == 1){    // 1 is substract

            if(level==1 && category.equals("Cat1"))
            { min=1; max=5; textViewLevelCount.setText("L: 1");
                textViewQuestion.setVisibility(View.VISIBLE);
                textViewOperator.setVisibility(View.VISIBLE);
              /*  textViewQuestion.setVisibility(View.INVISIBLE);
                textViewQuestion_part1.setVisibility(View.VISIBLE);
                textViewQuestion_part2.setVisibility(View.VISIBLE);
                textViewQuestion_part3.setVisibility(View.VISIBLE);*/

            }  // if chosen level 1 and category 1
            if(level==2 && category.equals("Cat1"))
            { min=1; max=9; textViewLevelCount.setText("L: 2");
                textViewQuestion.setVisibility(View.VISIBLE);
                textViewOperator.setVisibility(View.VISIBLE);

            }  // if chosen level 2 and category 1
            if(level==3 && category.equals("Cat1"))
            { min=1; max=20; textViewLevelCount.setText("L: 3");
                textViewQuestion.setVisibility(View.VISIBLE);
                textViewOperator.setVisibility(View.INVISIBLE);
                /*textViewQuestion.setVisibility(View.VISIBLE);
                textViewQuestion_part1.setVisibility(View.INVISIBLE);
                textViewQuestion_part2.setVisibility(View.INVISIBLE);
                textViewQuestion_part3.setVisibility(View.INVISIBLE);*/

            }  // if chosen level 3 and category 1
            if(level==4 && category.equals("Cat1"))
            { min=1; max=25; textViewLevelCount.setText("L: 4");
                textViewQuestion.setVisibility(View.VISIBLE);
                textViewOperator.setVisibility(View.INVISIBLE);
                /*textViewQuestion.setVisibility(View.VISIBLE);
                textViewQuestion_part1.setVisibility(View.INVISIBLE);
                textViewQuestion_part2.setVisibility(View.INVISIBLE);
                textViewQuestion_part3.setVisibility(View.INVISIBLE);*/
            }  // if chosen level 4 and category 1
            if(level==5 && category.equals("Cat1"))
            { min=1; max=30; textViewLevelCount.setText("L: 5");
                textViewQuestion.setVisibility(View.VISIBLE);
                textViewOperator.setVisibility(View.INVISIBLE);
              /*  textViewQuestion.setVisibility(View.VISIBLE);
                textViewQuestion_part1.setVisibility(View.INVISIBLE);
                textViewQuestion_part2.setVisibility(View.INVISIBLE);
                textViewQuestion_part3.setVisibility(View.INVISIBLE);*/
            }  // if chosen level 5 and category 1

            int n1 = r.nextInt(max)+min;
            int n2 = r.nextInt(max)+min;

            if(n2>n1){
                int a = n2;
                n2 = n1;
                n1 = a;
            }

            textViewQuestion.setText("" + n1 + " - " + n2 + " = ?");
            question = "" + n1 + " - " + n2 + " = ?";
            result = n1 - n2;
            textViewOperator.setText("-");

            iv1.setVisibility(View.INVISIBLE);
            iv2.setVisibility(View.INVISIBLE);
            iv3.setVisibility(View.INVISIBLE);
            iv4.setVisibility(View.INVISIBLE);
            iv5.setVisibility(View.INVISIBLE);
            iv6.setVisibility(View.INVISIBLE);
            iv7.setVisibility(View.INVISIBLE);
            iv8.setVisibility(View.INVISIBLE);
            iv9.setVisibility(View.INVISIBLE);
            iv10.setVisibility(View.INVISIBLE);
            iv11.setVisibility(View.INVISIBLE);
            iv12.setVisibility(View.INVISIBLE);
            iv13.setVisibility(View.INVISIBLE);
            iv14.setVisibility(View.INVISIBLE);
            iv15.setVisibility(View.INVISIBLE);
            iv16.setVisibility(View.INVISIBLE);
            iv17.setVisibility(View.INVISIBLE);
            iv18.setVisibility(View.INVISIBLE);

            if((level==1 && category.equals("Cat1")) || ((level==2 && category.equals("Cat1")))){
                if (n1==1){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.INVISIBLE);
                    iv3.setVisibility(View.INVISIBLE);
                    iv4.setVisibility(View.INVISIBLE);
                    iv5.setVisibility(View.INVISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "1");}
                if (n1==2){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.INVISIBLE);
                    iv4.setVisibility(View.INVISIBLE);
                    iv5.setVisibility(View.INVISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "2");}
                if (n1==3){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.INVISIBLE);
                    iv5.setVisibility(View.INVISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "3");}
                if (n1==4){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.INVISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "4");}

                if (n1==5){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "5");}
                if (n1==6){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.VISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "6");}
                if (n1==7){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.VISIBLE);
                    iv7.setVisibility(View.VISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "7");}
                if (n1==8){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.VISIBLE);
                    iv7.setVisibility(View.VISIBLE);
                    iv8.setVisibility(View.VISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "8");}
                if(n1==9) {iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.VISIBLE);
                    iv7.setVisibility(View.VISIBLE);
                    iv8.setVisibility(View.VISIBLE);
                    iv9.setVisibility(View.VISIBLE);
                    Log.e("girdi ", "9");}


                if (n2==1){iv10.setVisibility(View.VISIBLE);
                    Log.e("girdi ", "10");
                    iv11.setVisibility(View.INVISIBLE);
                    iv12.setVisibility(View.INVISIBLE);
                    iv13.setVisibility(View.INVISIBLE);
                    iv14.setVisibility(View.INVISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);}
                if (n2==2){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.INVISIBLE);
                    iv13.setVisibility(View.INVISIBLE);
                    iv14.setVisibility(View.INVISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "11");}
                if (n2 ==3){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.INVISIBLE);
                    iv14.setVisibility(View.INVISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "12");}
                if (n2==4){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.INVISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "13");}

                if (n2 ==5){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "14");}
                if (n2==6){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.VISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "15");}
                if (n2==7){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.VISIBLE);
                    iv16.setVisibility(View.VISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "16");}
                if (n2 ==8){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.VISIBLE);
                    iv16.setVisibility(View.VISIBLE);
                    iv17.setVisibility(View.VISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "17");}
                if (n2 ==9){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.VISIBLE);
                    iv16.setVisibility(View.VISIBLE);
                    iv17.setVisibility(View.VISIBLE);
                    iv18.setVisibility(View.VISIBLE);
                    Log.e("girdi ", "18");}
            }



            if ((result-5) < 0){
                a = r.nextInt((result+5)) + 1;
                b = r.nextInt((result+5)) + 1;
                c = r.nextInt((result+5)) + 1;
                while (a==b || a==c || b==c){
                    a = r.nextInt((result+5)) + 1;
                    b = r.nextInt((result+5)) + 1;
                    c = r.nextInt((result+5)) + 1;
                }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(result)){
                    a = r.nextInt((result+5)) + 1;
                    b = r.nextInt((result+5)) + 1;
                    c = r.nextInt((result+5)) + 1;
                    while (a==b || a==c || b==c){
                        a = r.nextInt((result+5)) + 1;
                        b = r.nextInt((result+5)) + 1;
                        c = r.nextInt((result+5)) + 1;
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }

            }
            else {
                a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                while (a==b || a==c || b==c){
                    a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(result)){
                    a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    while (a==b || a==c || b==c){
                        a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                        b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                        c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }

                }

                mixedAnswers.clear();
                mixedAnswers.add(result);
                mixedAnswers.add(a);
                mixedAnswers.add(b);
                mixedAnswers.add(c);

                answers.clear();
                for (int i:mixedAnswers){
                    answers.add(i);
                }

                buttonA.setText(String.valueOf(answers.get(0)));
                buttonB.setText(String.valueOf(answers.get(1)));
                buttonC.setText(String.valueOf(answers.get(2)));
                buttonD.setText(String.valueOf(answers.get(3)));

        }  // 1 is substract operator
        else if(operator == 2){

            if(level==1 && category.equals("Cat1")){ min=1; max=6; textViewLevelCount.setText("L: 1"); }  // if chosen level 1 and category 1
            if(level==2 && category.equals("Cat1")){ min=1; max=9; textViewLevelCount.setText("L: 2"); }  // if chosen level 2 and category 1
            if(level==3 && category.equals("Cat1")){ min=3; max=10;textViewLevelCount.setText("L: 3"); }  // if chosen level 3 and category 1
            if(level==4 && category.equals("Cat1")){ min=4; max=10; textViewLevelCount.setText("L: 4");  }  // if chosen level 4 and category 1
            if(level==5 && category.equals("Cat1")){ min=4; max=12; textViewLevelCount.setText("L: 5"); }  // if chosen level 5 and category 1

            int n1 = r.nextInt((max-min)+1) + min;
            int n2 = r.nextInt((max-min)+1) + min;
            textViewQuestion.setVisibility(View.VISIBLE);
            textViewOperator.setVisibility(View.INVISIBLE);

            textViewQuestion.setText("" + n1 + " x " + n2 + " = ?");
            question = "" + n1 + " x " + n2 + " = ?";
            result = n1 * n2;

            if ((result-5) <= 0){
                a = r.nextInt((result+5)) + 1;
                b = r.nextInt((result+5)) + 1;
                c = r.nextInt((result+5)) + 1;
                while (a==b || a==c || b==c){
                    a = r.nextInt((result+5)) + 1;
                    b = r.nextInt((result+5)) + 1;
                    c = r.nextInt((result+5)) + 1;
                }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(result)){
                    a = r.nextInt((result+5)) + 1;
                    b = r.nextInt((result+5)) + 1;
                    c = r.nextInt((result+5)) + 1;
                    while (a==b || a==c || b==c){
                        a = r.nextInt((result+5)) + 1;
                        b = r.nextInt((result+5)) + 1;
                        c = r.nextInt((result+5)) + 1;
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }
            }
            else {
                a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                while (a==b || a==c || b==c){
                    a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(result)){
                    a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    while (a==b || a==c || b==c){
                        a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                        b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                        c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }
            }
            // using hashset for mix the answers
            mixedAnswers.clear();
            mixedAnswers.add(result);
            mixedAnswers.add(a);
            mixedAnswers.add(b);
            mixedAnswers.add(c);

            // add from hashset to arraylist
            answers.clear();
            for (int i:mixedAnswers){
                answers.add(i);
            }

            buttonA.setText(String.valueOf(answers.get(0)));
            buttonB.setText(String.valueOf(answers.get(1)));
            buttonC.setText(String.valueOf(answers.get(2)));
            buttonD.setText(String.valueOf(answers.get(3)));
        }   // 2 is multiple operator
        else {
            if(level==1 && category.equals("Cat1")){ min=2; max=50; textViewLevelCount.setText("L: 1"); }  // if chosen level 1 and category 1
            if(level==2 && category.equals("Cat1")){ min=2; max=60;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 1
            if(level==3 && category.equals("Cat1")){ min=3; max=75;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 1
            if(level==4 && category.equals("Cat1")){ min=4; max=100; textViewLevelCount.setText("L: 4"); }  // if chosen level 4 and category 1
            if(level==5 && category.equals("Cat1")){ min=5; max=100; textViewLevelCount.setText("L: 5"); }  // if chosen level 5 and category 1

            int n1 = r.nextInt((max-min)+1) + min;
            int n2 = r.nextInt((max-min)+1) + min;
            int extraNumber = r.nextInt(20);

            textViewQuestion.setVisibility(View.VISIBLE);
            textViewOperator.setVisibility(View.INVISIBLE);

            // burada bi tanesini b??y??k olarak belirliyoruz o da n1 hep b??y??k kals??n istiyoruz
            if(n2>n1){
                int a = n2;
                n2 = n1;
                n1 = a;
            }

            // burada da sonu?? tam say?? ????kmas?? i??in kontrol yap??yoruz mod kullanarak, sonu?? tam say?? ????kana kadar sayi ??retiyoruz
            while (n1%n2!=0){
                n1 = r.nextInt((max-min)+1) + min;
                n2 = r.nextInt((max-min)+1) + min;
            }

            if(level==5 || level == 4){
                textViewQuestion.setText(""+ extraNumber + " + " + n1 + " ?? " + n2 + " = ?");
                question= ""+ extraNumber + " + " + n1 + " ?? " + n2 + " = ?";
                result = extraNumber + n1 / n2;
            }
            else {
                textViewQuestion.setText("" + n1 + " ?? " + n2 + " = ?");
                question="" + n1 + " ?? " + n2 + " = ?";
                result = n1 / n2;
            }

            if ((result-5) <= 0){
                a = r.nextInt((result+5)) + 1;
                b = r.nextInt((result+5)) + 1;
                c = r.nextInt((result+5)) + 1;
                while (a==b || a==c || b==c){
                    a = r.nextInt((result+5)) + 1;
                    b = r.nextInt((result+5)) + 1;
                    c = r.nextInt((result+5)) + 1;
                }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(result)){
                    a = r.nextInt((result+5)) + 1;
                    b = r.nextInt((result+5)) + 1;
                    c = r.nextInt((result+5)) + 1;
                    while (a==b || a==c || b==c){
                        a = r.nextInt((result+5)) + 1;
                        b = r.nextInt((result+5)) + 1;
                        c = r.nextInt((result+5)) + 1;
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }
            }
            else {
                a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                while (a==b || a==c || b==c){
                    a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(result)){
                    a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    while (a==b || a==c || b==c){
                        a = r.nextInt((result+4)-(result-4)+1) + (result-4);
                        b = r.nextInt((result+4)-(result-4)+1) + (result-4);
                        c = r.nextInt((result+4)-(result-4)+1) + (result-4);
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }
            }
            // using hashset for mix the answers
            mixedAnswers.clear();
            mixedAnswers.add(result);
            mixedAnswers.add(a);
            mixedAnswers.add(b);
            mixedAnswers.add(c);

            // add from hashset to arraylist
            answers.clear();
            for (int i:mixedAnswers){
                answers.add(i);
            }

            buttonA.setText(String.valueOf(answers.get(0)));
            buttonB.setText(String.valueOf(answers.get(1)));
            buttonC.setText(String.valueOf(answers.get(2)));
            buttonD.setText(String.valueOf(answers.get(3)));
        }                    // divide operator

    }

    // select operator + - * / example:      7 ? 1 = 8
    public void getQuestionCat2( String category, int level){

        textViewOperator.setVisibility(View.INVISIBLE);
        Random r = new Random();

        operator = r.nextInt(4);

        if(level==1 && category.equals("Cat2")){ min=1; max=5; operator = r.nextInt(2);}  // if chosen level 1 and category 1
        if (operator==0){
            if(level==1 && category.equals("Cat2"))
            { min=1; max=5; textViewLevelCount.setText("L: 1");buttonC.setVisibility(View.INVISIBLE); buttonD.setVisibility(View.INVISIBLE);
                textViewQuestion.setVisibility(View.VISIBLE);
                textViewOperator.setVisibility(View.VISIBLE);
                textViewOperator.setText("?");

            }  // if chosen level 1 and category 2
            if(level==2 && category.equals("Cat2"))
            { min=1; max=15;  textViewLevelCount.setText("L: 2");

            }  // if chosen level 2 and category 2
            if(level==3 && category.equals("Cat2")){ min=1; max=20;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 2
            if(level==4 && category.equals("Cat2")){ min=1; max=25;  textViewLevelCount.setText("L: 4");}  // if chosen level 4 and category 2
            if(level==5 && category.equals("Cat2")){ min=1; max=30;  textViewLevelCount.setText("L: 5");}  // if chosen level 5 and category 2

            int n1 = r.nextInt(max)+min;
            int n2 = r.nextInt(max)+min;
            int resultSum = n1 + n2;
            textViewQuestion.setText("" + n1 + "  ?  " + n2 + "  =  " + resultSum);
            question = "" + n1 + "  ?  " + n2 + "  =  " + resultSum;

            buttonA.setText("+");
            buttonB.setText("-");
            buttonC.setText("??");
            buttonD.setText("x");

            iv1.setVisibility(View.INVISIBLE);
            iv2.setVisibility(View.INVISIBLE);
            iv3.setVisibility(View.INVISIBLE);
            iv4.setVisibility(View.INVISIBLE);
            iv5.setVisibility(View.INVISIBLE);
            iv6.setVisibility(View.INVISIBLE);
            iv7.setVisibility(View.INVISIBLE);
            iv8.setVisibility(View.INVISIBLE);
            iv9.setVisibility(View.INVISIBLE);
            iv10.setVisibility(View.INVISIBLE);
            iv11.setVisibility(View.INVISIBLE);
            iv12.setVisibility(View.INVISIBLE);
            iv13.setVisibility(View.INVISIBLE);
            iv14.setVisibility(View.INVISIBLE);
            iv15.setVisibility(View.INVISIBLE);
            iv16.setVisibility(View.INVISIBLE);
            iv17.setVisibility(View.INVISIBLE);
            iv18.setVisibility(View.INVISIBLE);

            if((level==1 && category.equals("Cat2"))){
                if (n1==1){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.INVISIBLE);
                    iv3.setVisibility(View.INVISIBLE);
                    iv4.setVisibility(View.INVISIBLE);
                    iv5.setVisibility(View.INVISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "1111");}
                if (n1==2){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.INVISIBLE);
                    iv4.setVisibility(View.INVISIBLE);
                    iv5.setVisibility(View.INVISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "2");}
                if (n1==3){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.INVISIBLE);
                    iv5.setVisibility(View.INVISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "3");}
                if (n1==4){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.INVISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "4");}

                if (n1==5){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "5");}
                if (n1==6){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.VISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "6");}
                if (n1==7){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.VISIBLE);
                    iv7.setVisibility(View.VISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "7");}
                if (n1==8){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.VISIBLE);
                    iv7.setVisibility(View.VISIBLE);
                    iv8.setVisibility(View.VISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "8");}
                if(n1==9) {iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.VISIBLE);
                    iv7.setVisibility(View.VISIBLE);
                    iv8.setVisibility(View.VISIBLE);
                    iv9.setVisibility(View.VISIBLE);
                    Log.e("girdi ", "9");}


                if (n2==1){iv10.setVisibility(View.VISIBLE);
                    Log.e("girdi ", "10");
                    iv11.setVisibility(View.INVISIBLE);
                    iv12.setVisibility(View.INVISIBLE);
                    iv13.setVisibility(View.INVISIBLE);
                    iv14.setVisibility(View.INVISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);}
                if (n2==2){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.INVISIBLE);
                    iv13.setVisibility(View.INVISIBLE);
                    iv14.setVisibility(View.INVISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "11");}
                if (n2 ==3){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.INVISIBLE);
                    iv14.setVisibility(View.INVISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "12");}
                if (n2==4){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.INVISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "13");}

                if (n2 ==5){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "14");}
                if (n2==6){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.VISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "15");}
                if (n2==7){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.VISIBLE);
                    iv16.setVisibility(View.VISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "16");}
                if (n2 ==8){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.VISIBLE);
                    iv16.setVisibility(View.VISIBLE);
                    iv17.setVisibility(View.VISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "17");}
                if (n2 ==9){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.VISIBLE);
                    iv16.setVisibility(View.VISIBLE);
                    iv17.setVisibility(View.VISIBLE);
                    iv18.setVisibility(View.VISIBLE);
                    Log.e("girdi ", "18");}
            }


            resultCat2 = "+";
        }       // operator is +
        else if (operator==1){
            if(level==1 && category.equals("Cat2")){ min=1; max=5;  textViewLevelCount.setText("L: 1");  buttonC.setVisibility(View.INVISIBLE); buttonD.setVisibility(View.INVISIBLE);
                textViewQuestion.setVisibility(View.VISIBLE);
                textViewOperator.setVisibility(View.VISIBLE);
                textViewOperator.setText("?");}  // if chosen level 1 and category 2
            if(level==2 && category.equals("Cat2")){ min=1; max=15;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 2
            if(level==3 && category.equals("Cat2")){ min=1; max=20;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 2
            if(level==4 && category.equals("Cat2")){ min=1; max=25;  textViewLevelCount.setText("L: 4");}  // if chosen level 4 and category 2
            if(level==5 && category.equals("Cat2")){ min=1; max=30;  textViewLevelCount.setText("L: 5");}  // if chosen level 5 and category 2

            int n1 = r.nextInt(max)+min;
            int n2 = r.nextInt(max)+min;

            // b??y??kl??k kontrol?? yap??yoruz ve bir de??i??keni kesin b??y??k yap??yoruz o da n1 ????nk?? sonu?? - ????kmas??n diye
            if(n2>n1){
                int a = n2;
                n2 = n1;
                n1 = a;
            }

            int resultSub = n1 - n2;
            textViewQuestion.setText("" + n1 + "  ?  " + n2 + "  =  " + resultSub);
            question = "" + n1 + "  ?  " + n2 + "  =  " + resultSub;

            buttonA.setText("+");
            buttonB.setText("-");
            buttonC.setText("??");
            buttonD.setText("x");

            iv1.setVisibility(View.INVISIBLE);
            iv2.setVisibility(View.INVISIBLE);
            iv3.setVisibility(View.INVISIBLE);
            iv4.setVisibility(View.INVISIBLE);
            iv5.setVisibility(View.INVISIBLE);
            iv6.setVisibility(View.INVISIBLE);
            iv7.setVisibility(View.INVISIBLE);
            iv8.setVisibility(View.INVISIBLE);
            iv9.setVisibility(View.INVISIBLE);
            iv10.setVisibility(View.INVISIBLE);
            iv11.setVisibility(View.INVISIBLE);
            iv12.setVisibility(View.INVISIBLE);
            iv13.setVisibility(View.INVISIBLE);
            iv14.setVisibility(View.INVISIBLE);
            iv15.setVisibility(View.INVISIBLE);
            iv16.setVisibility(View.INVISIBLE);
            iv17.setVisibility(View.INVISIBLE);
            iv18.setVisibility(View.INVISIBLE);

            if((level==1 && category.equals("Cat2"))){
                if (n1==1){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.INVISIBLE);
                    iv3.setVisibility(View.INVISIBLE);
                    iv4.setVisibility(View.INVISIBLE);
                    iv5.setVisibility(View.INVISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "1");}
                if (n1==2){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.INVISIBLE);
                    iv4.setVisibility(View.INVISIBLE);
                    iv5.setVisibility(View.INVISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "2");}
                if (n1==3){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.INVISIBLE);
                    iv5.setVisibility(View.INVISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "3");}
                if (n1==4){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.INVISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "4");}

                if (n1==5){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.INVISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "5");}
                if (n1==6){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.VISIBLE);
                    iv7.setVisibility(View.INVISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "6");}
                if (n1==7){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.VISIBLE);
                    iv7.setVisibility(View.VISIBLE);
                    iv8.setVisibility(View.INVISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "7");}
                if (n1==8){iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.VISIBLE);
                    iv7.setVisibility(View.VISIBLE);
                    iv8.setVisibility(View.VISIBLE);
                    iv9.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "8");}
                if(n1==9) {iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    iv6.setVisibility(View.VISIBLE);
                    iv7.setVisibility(View.VISIBLE);
                    iv8.setVisibility(View.VISIBLE);
                    iv9.setVisibility(View.VISIBLE);
                    Log.e("girdi ", "9");}


                if (n2==1){iv10.setVisibility(View.VISIBLE);
                    Log.e("girdi ", "10");
                    iv11.setVisibility(View.INVISIBLE);
                    iv12.setVisibility(View.INVISIBLE);
                    iv13.setVisibility(View.INVISIBLE);
                    iv14.setVisibility(View.INVISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);}
                if (n2==2){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.INVISIBLE);
                    iv13.setVisibility(View.INVISIBLE);
                    iv14.setVisibility(View.INVISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "11");}
                if (n2 ==3){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.INVISIBLE);
                    iv14.setVisibility(View.INVISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "12");}
                if (n2==4){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.INVISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "13");}

                if (n2 ==5){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.INVISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "14");}
                if (n2==6){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.VISIBLE);
                    iv16.setVisibility(View.INVISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "15");}
                if (n2==7){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.VISIBLE);
                    iv16.setVisibility(View.VISIBLE);
                    iv17.setVisibility(View.INVISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "16");}
                if (n2 ==8){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.VISIBLE);
                    iv16.setVisibility(View.VISIBLE);
                    iv17.setVisibility(View.VISIBLE);
                    iv18.setVisibility(View.INVISIBLE);
                    Log.e("girdi ", "17");}
                if (n2 ==9){iv10.setVisibility(View.VISIBLE);
                    iv11.setVisibility(View.VISIBLE);
                    iv12.setVisibility(View.VISIBLE);
                    iv13.setVisibility(View.VISIBLE);
                    iv14.setVisibility(View.VISIBLE);
                    iv15.setVisibility(View.VISIBLE);
                    iv16.setVisibility(View.VISIBLE);
                    iv17.setVisibility(View.VISIBLE);
                    iv18.setVisibility(View.VISIBLE);
                    Log.e("girdi ", "18");}
            }


            resultCat2 = "-";
        }       // operator is -
        else if (operator==2){
            if(level==1 && category.equals("Cat2")){ min=2; max=6;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 1
            if(level==2 && category.equals("Cat2")){ min=2; max=9;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 1
            if(level==3 && category.equals("Cat2")){ min=3; max=10;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 1
            if(level==4 && category.equals("Cat2")){ min=4; max=10;  textViewLevelCount.setText("L: 4");}  // if chosen level 4 and category 1
            if(level==5 && category.equals("Cat2")){ min=4; max=12;  textViewLevelCount.setText("L: 5");}  // if chosen level 5 and category 1

            int n1 = r.nextInt((max-min)+1) + min;
            int n2 = r.nextInt((max-min)+1) + min;

            int resultMul = n1 * n2;
            textViewQuestion.setText("" + n1 + "  ?  " + n2 + "  =  " + resultMul);
            question = "" + n1 + "  ?  " + n2 + "  =  " + resultMul;

            buttonA.setText("+");
            buttonB.setText("-");
            buttonC.setText("??");
            buttonD.setText("x");

            resultCat2 = "x";
        }       // operator is *
        else{
            if(level==1 && category.equals("Cat2")){ min=2; max=50;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 1
            if(level==2 && category.equals("Cat2")){ min=2; max=60;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 1
            if(level==3 && category.equals("Cat2")){ min=3; max=75;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 1
            if(level==4 && category.equals("Cat2")){ min=4; max=100;  textViewLevelCount.setText("L: 4");}  // if chosen level 4 and category 1
            if(level==5 && category.equals("Cat2")){ min=5; max=100;  textViewLevelCount.setText("L: 5");}  // if chosen level 5 and category 1

            int n1 = r.nextInt((max-min)+1) + min;
            int n2 = r.nextInt((max-min)+1) + min;

            if(n2>n1){
                int a = n2;
                n2 = n1;
                n1 = a;
            }

            while (n1%n2!=0){
                n1 = r.nextInt((max-min)+1) + min;
                n2 = r.nextInt((max-min)+1) + min;
            }

            int resultDiv = n1 / n2;
            textViewQuestion.setText("" + n1 + "  ?  " + n2 + "  =  " + resultDiv);
            question = "" + n1 + "  ?  " + n2 + "  =  " + resultDiv;

            buttonA.setText("+");
            buttonB.setText("-");
            buttonC.setText("??");
            buttonD.setText("x");

            resultCat2 = "??";

        }                   // operator is /

    }

    // true or false   example:  9-5=1  true or false
    public void getQuestionCat3( String category, int level){

        constraintLayoutParts.setVisibility(View.INVISIBLE);
        textViewOperator.setVisibility(View.INVISIBLE);
        textViewQuestion.setVisibility(View.VISIBLE);
        constraintLayout1.setVisibility(View.INVISIBLE);
        constraintLayout3.setVisibility(View.INVISIBLE);

        Random r = new Random();

        operator = r.nextInt(4);

        // a????klamalar operator 0 i??erisinde
        if (operator==0){
            if(level==1 && category.equals("Cat3")){ min=1; max=10;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 2
            if(level==2 && category.equals("Cat3")){ min=1; max=15;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 2
            if(level==3 && category.equals("Cat3")){ min=1; max=20;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 2
            if(level==4 && category.equals("Cat3")){ min=1; max=25;  textViewLevelCount.setText("L: 4");}  // if chosen level 4 and category 2
            if(level==5 && category.equals("Cat3")){ min=1; max=30;  textViewLevelCount.setText("L: 5");}  // if chosen level 5 and category 2

            int n1 = r.nextInt(max)+min;
            int n2 = r.nextInt(max)+min;
            int resultSum = n1 + n2;

            int trueOrFalse = r.nextInt(2);     // burada sorunun cevab??n??n do??ru mu olsun yanl???? m?? olsun onu  belirleyen bir random de??er ??retiyoruz
            if (trueOrFalse == 0){                      // e??er 0 gelirse sonu?? do??ru olacak
                textViewQuestion.setText("" + n1 + "  +  " + n2 + "  =  " + resultSum);
                question = "" + n1 + "  +  " + n2 + "  =  " + resultSum;
                resultCat3 = "+";
            }else{                                      // e??er 1 gelirse sonu?? yanl???? olacak
                int wrongAnswer = r.nextInt((resultSum+4)-(resultSum-4)+1) + (resultSum-4);     // yanl???? de??er ??rettik
                while (wrongAnswer == resultSum){           // bu yanl???? de??er sonu??la ayn?? olmas??n dedik
                    wrongAnswer = r.nextInt((resultSum+4)-(resultSum-4)+1) + (resultSum-4);
                }
                textViewQuestion.setText("" + n1 + "  +  " + n2 + "  =  " + wrongAnswer);
                question = "" + n1 + "  +  " + n2 + "  =  " + wrongAnswer;
                resultCat3 = "-";
            }

            buttonA.setVisibility(View.INVISIBLE);
            buttonB.setVisibility(View.INVISIBLE);
            buttonC.setVisibility(View.INVISIBLE);
            buttonD.setVisibility(View.INVISIBLE);
            imageViewtrue.setVisibility(View.VISIBLE);
            imageViewfalse.setVisibility(View.VISIBLE);



        }       // operator is +
        else if (operator==1){
            if(level==1 && category.equals("Cat3")){ min=1; max=10;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 2
            if(level==2 && category.equals("Cat3")){ min=1; max=15;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 2
            if(level==3 && category.equals("Cat3")){ min=1; max=20;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 2
            if(level==4 && category.equals("Cat3")){ min=1; max=25;  textViewLevelCount.setText("L: 4");}  // if chosen level 4 and category 2
            if(level==5 && category.equals("Cat3")){ min=1; max=30;  textViewLevelCount.setText("L: 5");}  // if chosen level 5 and category 2

            int n1 = r.nextInt(max)+min;
            int n2 = r.nextInt(max)+min;

            if(n2>n1){
                int a = n2;
                n2 = n1;
                n1 = a;
            }

            int resultSub = n1 - n2;

            int trueOrFalse = r.nextInt(2);
            if (trueOrFalse == 0){
                textViewQuestion.setText("" + n1 + "  -  " + n2 + "  =  " + resultSub);
                question = "" + n1 + "  -  " + n2 + "  =  " + resultSub;
                resultCat3 = "+";
            }else{
                if ((resultSub-5) < 0) {
                    wrongAnswer = r.nextInt((resultSub + 5)) + 1;

                    while (wrongAnswer == resultSub) {
                        wrongAnswer = r.nextInt((resultSub + 5)) + 1;
                    }
                    textViewQuestion.setText("" + n1 + "  -  " + n2 + "  =  " + wrongAnswer);
                    question = "" + n1 + "  -  " + n2 + "  =  " + wrongAnswer;
                    resultCat3 = "-";
                }else {
                    wrongAnswer = r.nextInt((resultSub+4)-(resultSub-4)+1) + (resultSub-4);
                    textViewQuestion.setText("" + n1 + "  -  " + n2 + "  =  " + wrongAnswer);
                    question = "" + n1 + "  -  " + n2 + "  =  " + wrongAnswer;
                    resultCat3 = "-";
                }
            }

            buttonA.setVisibility(View.INVISIBLE);
            buttonB.setVisibility(View.INVISIBLE);
            buttonC.setVisibility(View.INVISIBLE);
            buttonD.setVisibility(View.INVISIBLE);
            imageViewtrue.setVisibility(View.VISIBLE);
            imageViewfalse.setVisibility(View.VISIBLE);
        }       // operator is -
        else if (operator==2){
            if(level==1 && category.equals("Cat3")){ min=2; max=6;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 1
            if(level==2 && category.equals("Cat3")){ min=2; max=9;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 1
            if(level==3 && category.equals("Cat3")){ min=3; max=10; textViewLevelCount.setText("L: 3"); }  // if chosen level 3 and category 1
            if(level==4 && category.equals("Cat3")){ min=4; max=10; textViewLevelCount.setText("L: 4"); }  // if chosen level 4 and category 1
            if(level==5 && category.equals("Cat3")){ min=4; max=12; textViewLevelCount.setText("L: 5"); }  // if chosen level 5 and category 1

            int n1 = r.nextInt((max-min)+1) + min;
            int n2 = r.nextInt((max-min)+1) + min;

            int resultMul = n1 * n2;
            int trueOrFalse = r.nextInt(2);
            if (trueOrFalse == 0){
                textViewQuestion.setText("" + n1 + "  x  " + n2 + "  =  " + resultMul);
                question = "" + n1 + "  x  " + n2 + "  =  " + resultMul;
                resultCat3 = "+";
            }else{
                int wrongAnswer = r.nextInt((resultMul+4)-(resultMul-4)+1) + (resultMul-4);
                while (wrongAnswer == resultMul){
                    wrongAnswer = r.nextInt((resultMul+4)-(resultMul-4)+1) + (resultMul-4);
                }
                textViewQuestion.setText("" + n1 + "  x  " + n2 + "  =  " + wrongAnswer);
                question = "" + n1 + "  x  " + n2 + "  =  " + wrongAnswer;
                resultCat3 = "-";
            }

            buttonA.setVisibility(View.INVISIBLE);
            buttonB.setVisibility(View.INVISIBLE);
            buttonC.setVisibility(View.INVISIBLE);
            buttonD.setVisibility(View.INVISIBLE);
            imageViewtrue.setVisibility(View.VISIBLE);
            imageViewfalse.setVisibility(View.VISIBLE);
        }       // operator is *
        else{
            if(level==1 && category.equals("Cat3")){ min=2; max=50;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 1
            if(level==2 && category.equals("Cat3")){ min=2; max=60;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 1
            if(level==3 && category.equals("Cat3")){ min=3; max=75;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 1
            if(level==4 && category.equals("Cat3")){ min=4; max=100; textViewLevelCount.setText("L: 4"); }  // if chosen level 4 and category 1
            if(level==5 && category.equals("Cat3")){ min=5; max=100;  textViewLevelCount.setText("L: 5");}  // if chosen level 5 and category 1

            int n1 = r.nextInt((max-min)+1) + min;
            int n2 = r.nextInt((max-min)+1) + min;

            if(n2>n1){
                int a = n2;
                n2 = n1;
                n1 = a;
            }

            while (n1%n2!=0){
                n1 = r.nextInt((max-min)+1) + min;
                n2 = r.nextInt((max-min)+1) + min;
            }

            int resultDiv = n1 / n2;

            int trueOrFalse = r.nextInt(2);
            if (trueOrFalse == 0){
                textViewQuestion.setText("" + n1 + "  ??  " + n2 + "  =  " + resultDiv);
                question = "" + n1 + "  ??  " + n2 + "  =  " + resultDiv;
                resultCat3 = "+";
            }else{
                if ((resultDiv-5) < 0) {
                    wrongAnswer = r.nextInt((resultDiv + 5)) + 1;

                    while (wrongAnswer == resultDiv) {
                        wrongAnswer = r.nextInt((resultDiv + 5)) + 1;
                    }
                    textViewQuestion.setText("" + n1 + "  ??  " + n2 + "  =  " + wrongAnswer);
                    question = "" + n1 + "  ??  " + n2 + "  =  " + wrongAnswer;
                    resultCat3 = "-";
                }else {
                    wrongAnswer = r.nextInt((resultDiv+4)-(resultDiv-4)+1) + (resultDiv-4);
                    textViewQuestion.setText("" + n1 + "  ??  " + n2 + "  =  " + wrongAnswer);
                    question = "" + n1 + "  ??  " + n2 + "  =  " + wrongAnswer;
                    resultCat3 = "-";
                }
            }

            buttonA.setVisibility(View.INVISIBLE);
            buttonB.setVisibility(View.INVISIBLE);
            buttonC.setVisibility(View.INVISIBLE);
            buttonD.setVisibility(View.INVISIBLE);
            imageViewtrue.setVisibility(View.VISIBLE);
            imageViewfalse.setVisibility(View.VISIBLE);

        }                   // operator is /
    }

    // calculate 3 process      example:  3*6+1=?
    public void getQuestionCat4( String category, int level){

        constraintLayoutParts.setVisibility(View.INVISIBLE);
        textViewOperator.setVisibility(View.INVISIBLE);
        textViewQuestion.setVisibility(View.VISIBLE);
        constraintLayout1.setVisibility(View.INVISIBLE);
        constraintLayout3.setVisibility(View.INVISIBLE);

        Random r = new Random();    // r.nextInt((max - min) + 1) + min
        Log.e("counter: ", String.valueOf(questionCounter));
        operator = r.nextInt(3);

        // getQuestionCat1 e benzer i??lemler burada da bulunmaktad??r
        if (operator == 0){    // 1 is substract and sum

            if(level==1 && category.equals("Cat4")){ min=2; max=50;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 1
            if(level==2 && category.equals("Cat4")){ min=2; max=60;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 1
            if(level==3 && category.equals("Cat4")){ min=3; max=75;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 1
            if(level==4 && category.equals("Cat4")){ min=4; max=100; textViewLevelCount.setText("L: 4"); }  // if chosen level 4 and category 1
            if(level==5 && category.equals("Cat4")){ min=5; max=100; textViewLevelCount.setText("L: 5"); }  // if chosen level 5 and category 1

            int n1 = r.nextInt((max-min)+1) + min;
            int n2 = r.nextInt((max-min)+1) + min;
            int extraNumber = r.nextInt(20);

            if(n2>n1){
                int a = n2;
                n2 = n1;
                n1 = a;
            }


            int leftOrRight = r.nextInt(2);
            if (leftOrRight == 0){
                while ((extraNumber + n1)%n2!=0){
                    n1 = r.nextInt((max-min)+1) + min;
                    n2 = r.nextInt((max-min)+1) + min;
                    extraNumber = r.nextInt(20);
                }
                textViewQuestion.setText("("+ extraNumber + " + " + n1 + ") ?? " + n2 + " = ?");
                question = "("+ extraNumber + " + " + n1 + ") ?? " + n2 + " = ?";
                resultCat4 = (extraNumber + n1) / n2;
            }else{
                while ((n1)%(n2+extraNumber) != 0){
                    n1 = r.nextInt((max-min)+1) + min;
                    n2 = r.nextInt((max-min)+1) + min;
                    extraNumber = r.nextInt(20);
                }
                textViewQuestion.setText("" +  n1 + " ?? (" + n2 + " + " + extraNumber + ") = ?");
                question = "" +  n1 + " ?? (" + n2 + " + " + extraNumber + ") = ?";
                resultCat4 = n1 / (n2 + extraNumber) ;
            }


            if ((resultCat4-5) <= 0){
                a = r.nextInt((resultCat4+5)) + 1;
                b = r.nextInt((resultCat4+5)) + 1;
                c = r.nextInt((resultCat4+5)) + 1;
                while (a==b || a==c || b==c){
                    a = r.nextInt((resultCat4+5)) + 1;
                    b = r.nextInt((resultCat4+5)) + 1;
                    c = r.nextInt((resultCat4+5)) + 1;
                }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(resultCat4)){
                    a = r.nextInt((resultCat4+5)) + 1;
                    b = r.nextInt((resultCat4+5)) + 1;
                    c = r.nextInt((resultCat4+5)) + 1;
                    while (a==b || a==c || b==c){
                        a = r.nextInt((resultCat4+5)) + 1;
                        b = r.nextInt((resultCat4+5)) + 1;
                        c = r.nextInt((resultCat4+5)) + 1;
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }
            }
            else {
                a = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                b = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                c = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                while (a==b || a==c || b==c){
                    a = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    b = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    c = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(resultCat4)){
                    a = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    b = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    c = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    while (a==b || a==c || b==c){
                        a = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                        b = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                        c = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }
            }
            // using hashset for mix the answers
            mixedAnswers.clear();
            mixedAnswers.add(resultCat4);
            mixedAnswers.add(a);
            mixedAnswers.add(b);
            mixedAnswers.add(c);

            // add from hashset to arraylist
            answers.clear();
            for (int i:mixedAnswers){
                answers.add(i);
            }

            buttonA.setText(String.valueOf(answers.get(0)));
            buttonB.setText(String.valueOf(answers.get(1)));
            buttonC.setText(String.valueOf(answers.get(2)));
            buttonD.setText(String.valueOf(answers.get(3)));

        }        // 0 is sum operator   example: resultCat4 = (extraNumber + n1) / n2;
        else if(operator == 1){

            if(level==1 && category.equals("Cat4")){ min=1; max=6;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 1
            if(level==2 && category.equals("Cat4")){ min=1; max=9;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 1
            if(level==3 && category.equals("Cat4")){ min=3; max=10; textViewLevelCount.setText("L: 3"); }  // if chosen level 3 and category 1
            if(level==4 && category.equals("Cat4")){ min=4; max=10; textViewLevelCount.setText("L: 4"); }  // if chosen level 4 and category 1
            if(level==5 && category.equals("Cat4")){ min=4; max=12; textViewLevelCount.setText("L: 5"); }  // if chosen level 5 and category 1

            int n1 = r.nextInt((max-min)+1) + min;
            int n2 = r.nextInt((max-min)+1) + min;
            int extraNumber = r.nextInt(20);

            int sumOrSub = r.nextInt(2);  // 0 = + sum,  1 = - substraction
            sumOrSub = 0;
            int leftOrRight = r.nextInt(2);
            if (sumOrSub == 0 && leftOrRight == 0){
                textViewQuestion.setText(""+ extraNumber + " + " + n1 + " x " + n2 + " = ?");
                question = ""+ extraNumber + " + " + n1 + " x " + n2 + " = ?";
                resultCat4 = extraNumber + n1 * n2;
                Log.e("sumOrSub: 0 ", " leftOrRight: 0");
            }
            if(sumOrSub == 0 && leftOrRight == 1){
                textViewQuestion.setText("" +  n1 + " x " + n2 + " + " + extraNumber + " = ?");
                question = "" +  n1 + " x " + n2 + " + " + extraNumber + " = ?";
                resultCat4 = n1 * n2 + extraNumber ;
                Log.e("sumOrSub: 0 ", " leftOrRight: 1");
            }

            if ((resultCat4-5) <= 0){
                a = r.nextInt((resultCat4+5)) + 1;
                b = r.nextInt((resultCat4+5)) + 1;
                c = r.nextInt((resultCat4+5)) + 1;
                while (a==b || a==c || b==c){
                    a = r.nextInt((resultCat4+5)) + 1;
                    b = r.nextInt((resultCat4+5)) + 1;
                    c = r.nextInt((resultCat4+5)) + 1;
                }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(resultCat4)){
                    a = r.nextInt((resultCat4+5)) + 1;
                    b = r.nextInt((resultCat4+5)) + 1;
                    c = r.nextInt((resultCat4+5)) + 1;
                    while (a==b || a==c || b==c){
                        a = r.nextInt((resultCat4+5)) + 1;
                        b = r.nextInt((resultCat4+5)) + 1;
                        c = r.nextInt((resultCat4+5)) + 1;
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }
            }
            else {
                a = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                b = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                c = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                while (a==b || a==c || b==c){
                    a = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    b = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    c = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(resultCat4)){
                    a = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    b = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    c = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    while (a==b || a==c || b==c){
                        a = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                        b = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                        c = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }
            }
            // using hashset for mix the answers
            mixedAnswers.clear();
            mixedAnswers.add(resultCat4);
            mixedAnswers.add(a);
            mixedAnswers.add(b);
            mixedAnswers.add(c);

            // add from hashset to arraylist
            answers.clear();
            for (int i:mixedAnswers){
                answers.add(i);
            }

            buttonA.setText(String.valueOf(answers.get(0)));
            buttonB.setText(String.valueOf(answers.get(1)));
            buttonC.setText(String.valueOf(answers.get(2)));
            buttonD.setText(String.valueOf(answers.get(3)));
        }   // 1 is multiple operator   example: resultCat4 = extraNumber + n1 * n2;
        else {
            if(level==1 && category.equals("Cat4")){ min=2; max=50;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 1
            if(level==2 && category.equals("Cat4")){ min=2; max=60;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 1
            if(level==3 && category.equals("Cat4")){ min=3; max=75;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 1
            if(level==4 && category.equals("Cat4")){ min=4; max=100; textViewLevelCount.setText("L: 4"); }  // if chosen level 4 and category 1
            if(level==5 && category.equals("Cat4")){ min=5; max=100;  textViewLevelCount.setText("L: 5");}  // if chosen level 5 and category 1

            int n1 = r.nextInt((max-min)+1) + min;
            int n2 = r.nextInt((max-min)+1) + min;
            int extraNumber = r.nextInt(20);

            if(n2>n1){
                int a = n2;
                n2 = n1;
                n1 = a;
            }

            while (n1%n2!=0){
                n1 = r.nextInt((max-min)+1) + min;
                n2 = r.nextInt((max-min)+1) + min;
            }

            int leftOrRight = r.nextInt(2);
            if (leftOrRight == 0){
                textViewQuestion.setText(""+ extraNumber + " + " + n1 + " ?? " + n2 + " = ?");
                question = ""+ extraNumber + " + " + n1 + " ?? " + n2 + " = ?";
                resultCat4 = extraNumber + n1 / n2;
            }else{
                textViewQuestion.setText("" +  n1 + " ?? " + n2 + " + " + extraNumber + " = ?");
                question = "" +  n1 + " ?? " + n2 + " + " + extraNumber + " = ?";
                resultCat4 = n1 / n2 + extraNumber ;
            }


            if ((resultCat4-5) <= 0){
                a = r.nextInt((resultCat4+5)) + 1;
                b = r.nextInt((resultCat4+5)) + 1;
                c = r.nextInt((resultCat4+5)) + 1;
                while (a==b || a==c || b==c){
                    a = r.nextInt((resultCat4+5)) + 1;
                    b = r.nextInt((resultCat4+5)) + 1;
                    c = r.nextInt((resultCat4+5)) + 1;
                }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(resultCat4)){
                    a = r.nextInt((resultCat4+5)) + 1;
                    b = r.nextInt((resultCat4+5)) + 1;
                    c = r.nextInt((resultCat4+5)) + 1;
                    while (a==b || a==c || b==c){
                        a = r.nextInt((resultCat4+5)) + 1;
                        b = r.nextInt((resultCat4+5)) + 1;
                        c = r.nextInt((resultCat4+5)) + 1;
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }
            }
            else {
                a = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                b = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                c = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                while (a==b || a==c || b==c){
                    a = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    b = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    c = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                }
                answers.clear();
                answers.add(c);
                answers.add(a);
                answers.add(b);
                while (answers.contains(resultCat4)){
                    a = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    b = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    c = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    while (a==b || a==c || b==c){
                        a = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                        b = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                        c = r.nextInt((resultCat4+4)-(resultCat4-4)+1) + (resultCat4-4);
                    }
                    answers.clear();
                    answers.add(c);
                    answers.add(a);
                    answers.add(b);
                }
            }
            // using hashset for mix the answers
            mixedAnswers.clear();
            mixedAnswers.add(resultCat4);
            mixedAnswers.add(a);
            mixedAnswers.add(b);
            mixedAnswers.add(c);

            // add from hashset to arraylist
            answers.clear();
            for (int i:mixedAnswers){
                answers.add(i);
            }

            buttonA.setText(String.valueOf(answers.get(0)));
            buttonB.setText(String.valueOf(answers.get(1)));
            buttonC.setText(String.valueOf(answers.get(2)));
            buttonD.setText(String.valueOf(answers.get(3)));
        }                    // divide operator     example: resultCat4 = extraNumber + n1 / n2;

    }

    // compare 2 process equals, bigger or smaller   example:  24/8  ? 44/2
    public void getQuestionCat5( String category, int level){

        constraintLayoutParts.setVisibility(View.INVISIBLE);
        textViewOperator.setVisibility(View.INVISIBLE);
        textViewQuestion.setVisibility(View.VISIBLE);
        constraintLayout1.setVisibility(View.INVISIBLE);
        constraintLayout3.setVisibility(View.INVISIBLE);

        Random r = new Random();    // r.nextInt((max - min) + 1) + min
        buttonEquals.setVisibility(View.VISIBLE);

        question = "Wrong Answer!";

        // burada iki adet i??leme ihtiyac??m??z var o y??zden iki kez d??nderiyoruz bu kodlar?? ilki ilk i??lemi di??eri son i??lemi ??retiyor
        for (int j = 0; j<2; j++){
            operator = r.nextInt(4);

            // a????klamalar operator 0 da
            if (operator == 0){                                                                 // 0 is sum

                if(level==1 && category.equals("Cat5")){ min=1; max=10;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 1
                if(level==2 && category.equals("Cat5")){ min=1; max=15;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 1
                if(level==3 && category.equals("Cat5")){ min=1; max=20;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 1
                if(level==4 && category.equals("Cat5")){ min=1; max=25;  textViewLevelCount.setText("L: 4");}  // if chosen level 4 and category 1
                if(level==5 && category.equals("Cat5")){ min=1; max=30;  textViewLevelCount.setText("L: 5");}  // if chosen level 5 and category 1

                int n1 = r.nextInt(max)+min;
                int n2 = r.nextInt(max)+min;

                // burda kontrol ediyor ilk i??lemde mi yoksa son i??lemde mi
                if (j==0){      // e??er ilk i??lemde ise yaz??yor ilk i??lemi
                    textViewQuestion.setText("" + n1 + " + " + n2 );
                    result1 = n1 + n2;
                }
                if (j==1){      // e??er son i??lemde ise ilk i??lemin 2 sat??r alt??na ? onun da 2 sat??r alt??na son i??lemi yaz??yor
                    textViewQuestion.append("\n\n   ?");
                    textViewQuestion.append("\n\n" + n1 + " + " + n2 );
                    result2 = n1 + n2;
                }

            }  // 0 is sum operator
            else if (operator == 1){    // 1 is substract

                if(level==1 && category.equals("Cat5")){ min=1; max=10;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 1
                if(level==2 && category.equals("Cat5")){ min=1; max=15;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 1
                if(level==3 && category.equals("Cat5")){ min=1; max=20;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 1
                if(level==4 && category.equals("Cat5")){ min=1; max=25;  textViewLevelCount.setText("L: 4");}  // if chosen level 4 and category 1
                if(level==5 && category.equals("Cat5")){ min=1; max=30;  textViewLevelCount.setText("L: 5");}  // if chosen level 5 and category 1

                int n1 = r.nextInt(max)+min;
                int n2 = r.nextInt(max)+min;

                if(n2>n1){
                    int a = n2;
                    n2 = n1;
                    n1 = a;
                }


                if (j==0){
                    textViewQuestion.setText("" + n1 + " - " + n2 );
                    result1 = n1 - n2;
                }
                if (j==1){
                    textViewQuestion.append("\n\n   ?");
                    textViewQuestion.append("\n\n" + n1 + " - " + n2 );
                    result2 = n1 - n2;
                }


            }  // 1 is substract operator
            else if(operator == 2){

                if(level==1 && category.equals("Cat5")){ min=1; max=6;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 1
                if(level==2 && category.equals("Cat5")){ min=1; max=9;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 1
                if(level==3 && category.equals("Cat5")){ min=3; max=10;  textViewLevelCount.setText("L: 3");}  // if chosen level 3 and category 1
                if(level==4 && category.equals("Cat5")){ min=4; max=10;  textViewLevelCount.setText("L: 4");}  // if chosen level 4 and category 1
                if(level==5 && category.equals("Cat5")){ min=4; max=12;  textViewLevelCount.setText("L: 5");}  // if chosen level 5 and category 1

                int n1 = r.nextInt((max-min)+1) + min;
                int n2 = r.nextInt((max-min)+1) + min;


                if (j==0){
                    textViewQuestion.setText("" + n1 + " x " + n2 );
                    result1 = n1 * n2;
                }
                if (j==1){
                    textViewQuestion.append("\n\n   ?");
                    textViewQuestion.append("\n\n" + n1 + " x " + n2 );
                    result2 = n1 * n2;
                }

            }   // 2 is multiple operator
            else {
                if(level==1 && category.equals("Cat5")){ min=2; max=50;  textViewLevelCount.setText("L: 1");}  // if chosen level 1 and category 1
                if(level==2 && category.equals("Cat5")){ min=2; max=60;  textViewLevelCount.setText("L: 2");}  // if chosen level 2 and category 1
                if(level==3 && category.equals("Cat5")){ min=3; max=75; textViewLevelCount.setText("L: 3"); }  // if chosen level 3 and category 1
                if(level==4 && category.equals("Cat5")){ min=4; max=100; textViewLevelCount.setText("L: 4"); }  // if chosen level 4 and category 1
                if(level==5 && category.equals("Cat5")){ min=5; max=100; textViewLevelCount.setText("L: 5"); }  // if chosen level 5 and category 1

                int n1 = r.nextInt((max-min)+1) + min;
                int n2 = r.nextInt((max-min)+1) + min;

                if(n2>n1){
                    int a = n2;
                    n2 = n1;
                    n1 = a;
                }

                while (n1%n2!=0){
                    n1 = r.nextInt((max-min)+1) + min;
                    n2 = r.nextInt((max-min)+1) + min;
                }

                if (j==0){
                    textViewQuestion.setText("" + n1 + " / " + n2 );
                    result1 = n1 / n2;
                }
                if (j==1){
                    textViewQuestion.append("\n\n   ?");
                    textViewQuestion.append("\n\n" + n1 + " ?? " + n2 );
                    result2 = n1 / n2;
                }

            }                    // divide operator
        }

        // olu??turulan iki i??lemi kontrol ediyor, iki i??lemin sonucu da farkl?? de??i??kenlerde tutuldu
        if(result1>result2){
            resultCat5 = ">";
        }
        else if(result1<result2){
            resultCat5 = "<";
        }
        else{
            resultCat5 = "=";
        }
        buttonA.setText(">");
        buttonB.setText("<");
        buttonEquals.setText("=");
        buttonD.setVisibility(View.INVISIBLE);
        buttonC.setVisibility(View.INVISIBLE);

    }

    // Reklam?? y??kleme fonksiyonu
    public void loadRewardedAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-4601944440701403/3865690211",
                adRequest, new RewardedAdLoadCallback() {

                    // internete ba??l?? de??ilken bura ??al??????yor
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.e(TAG, "onAdFailedToLoad");
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.e(TAG, "Ad was loaded.");

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            // reklam g??sterilmeye ba??lay??nca buraya girer
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.e(TAG, "Ad was shown.");
                                adsCounter+=1;      // reklam g??sterildikce adsCounter art??yor 2 yi ge??ince reklam izleyerek ge??me hakk?? bitiyor 2 den fazla reklam olmuyor bir level i??erisinde
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show.");
                            }

                            // Reklam?? kapat??rsa buraya girer ve result pageye gider.
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.e(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                                if (isAdsWatchedOrNot==false){      // e??er reklam izlenmeden kapat??ld??ysa ??d??l?? alamam???? oluyor ve direkt result pageye gidiyor, kald?????? yerden devam etmi?? olmuyor
                                    Intent intentt = new Intent(GamePage.this, ResultPage.class); // do??ru cevap yanl???? cevab?? g??nder
                                    intentt.putExtra("rightAnswer", correctAnswerText);
                                    intentt.putExtra("chosenAnswer", buttonText);
                                    intentt.putExtra("question", question);
                                    intentt.putExtra("lastQuestion", "notFinished");
                                    intentt.putExtra("submittedCategory", submittedCategory);
                                    intentt.putExtra("chosenLevel",submittedLevel);
                                    startActivity(intentt);
                                    finish();
                                }else{      // e??er reklam?? izlediyse timer yeniden ba??l??yor ayn?? sorudan devam ediyor yanm???? olmuyor
                                    Log.e("izlendi","izlendi");
                                    ct=100;         // zaman?? ba??tan ba??latmak i??in ct yi 100 yap??yoruz progress bar?? tam dolu yapacak s??reyi 10 dan tekrar ba??latacak
                                    timeStart();     // s??reyi tekrar ba??lat??yoruz
                                    questionCounter-=1;     // bir say?? d????t??k ????nk?? soruyu kontrol ederken controlCounter fonkdiyonunda 1 art??rd?? ancak tekrar ayn?? sorudan devam edecek onu burda d??????r??yoruz
                                    control=0;      // yanl???? yap??nca kontrol 1 olmu??tu bu y??zden getQuestion ??al????m??yordu tekrar 0 yap??yoruzki controlCounter fonksiyonu i??inde if (questionCounter!=20 && control==0) bu if in i??ine girsin ve getQuestion fonksiyonunu ??al????t??rs??n diye
                                    isAdsWatchedOrNot=false;
                                    //loadRewardedAd();       // burada tekrar reklam y??kl??yoruz ????nk?? gamepage i??erisinde hala ve tekrar yanarsa tekrar reklam haz??r y??kl?? olsun diye
                                }
                                isAdsWatchedOrNot=false;

                            }
                        });
                    }
                });
    }

    // Reklam?? G??sterme fonksiyonu
    public void showRewardedAd(){
        if (mRewardedAd != null) {
            Activity activityContext = GamePage.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                // e??er reklam??n tamam??n?? izlerse kapatmazsa erkenden buraya girer
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.e(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    isAdsWatchedOrNot=true;
                }
            });
        } else {
            Intent intentt = new Intent(GamePage.this, ResultPage.class); // do??ru cevap yanl???? cevab?? g??nder
            intentt.putExtra("rightAnswer", correctAnswerText);
            intentt.putExtra("chosenAnswer", buttonText);
            intentt.putExtra("lastQuestion", "notFinished");
            intentt.putExtra("submittedCategory", submittedCategory);
            intentt.putExtra("question", question);
            intentt.putExtra("chosenLevel",submittedLevel);
            startActivity(intentt);
            finish();
            Log.e(TAG, "The rewarded ad wasn't ready yet.");
        }
    }
    // horizontal progress bar
    public void timeStart(){

        allTime=2000;   // 20 sn ayarlad??k
        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run()
            {
                time.setProgress(ct);       // progress bar??n g??stergesi ct ne ise o

                if(ct == 0) {       // e??er s??re biterse
                    correctAnswer();    // s??renin bitti??i sorunun do??ru cevab??n??n ne oldu??unu ????rendik
                    t.cancel();         // s??releri iptal ettik
                    tt.cancel();
                    Intent intentt = new Intent(GamePage.this, ResultPage.class); // do??ru cevap yanl???? cevab?? g??nder
                    intentt.putExtra("rightAnswer", correctAnswerText);
                    intentt.putExtra("chosenAnswer", "Time is over!");
                    intentt.putExtra("question", question);
                    intentt.putExtra("submittedCategory", submittedCategory);
                    intentt.putExtra("chosenLevel",submittedLevel);
                    intentt.putExtra("lastQuestion", "notFinished");
                    startActivity(intentt);
                    finish();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textViewTime.setText(""+ ((ct+10)/10));
                    }
                });

                ct=ct-10;
            }
        };
        t.schedule(tt,0,allTime);
    }

    // kullan??c?? telefonun orta tu??una home butona basarsa oyunda iken s??reyi durduruyoruz iptal ediyoruz e??er etmezsek orta tu??a bas??p oyunu alta at??nca s??re bitti??i anda otomatik oyun kendini a????yor resultPage ye y??nlenmi?? oluyor ????nk?? s??re bitince intent ile resultPageye y??nlendirme var
    @Override
    protected void onStop() {
        super.onStop();
        tt.cancel();
        t.cancel();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tt.cancel();
        t.cancel();
        startActivity(new Intent(GamePage.this, CategoryPage.class));
        finish();
    }
}