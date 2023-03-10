package com.kogosoft.easymathprototip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class CategoryPage extends AppCompatActivity {

    private LottieAnimationView animationViewM1,animationViewM2,animationViewM4,animationViewM5,animationViewM3_1,animationViewM3_2;
    private ConstraintLayout constraintLayoutCat1, constraintLayoutCat5, constraintLayoutCat3, constraintLayoutCat2, constraintLayoutCat4,constraintLayoutCatEk1,constraintLayoutEk2;
    private String chosenCategory = "";
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);

       /* animationViewM3_1=findViewById(R.id.animationViewM3_1);
        animationViewM3_2=findViewById(R.id.animationViewM3_2);
        animationViewM1=findViewById(R.id.animationViewM1);
        animationViewM4=findViewById(R.id.animationViewM4);
        animationViewM5=findViewById(R.id.animationViewM5);
        animationViewM2=findViewById(R.id.animationViewM2);*/

        constraintLayoutCat1 =findViewById(R.id.constraintLayoutCat1);
        constraintLayoutCat2 =findViewById(R.id.constraintLayoutCat2);
        constraintLayoutCat3 =findViewById(R.id.constraintLayoutCat3);
        constraintLayoutCat4 =findViewById(R.id.constraintLayoutCat4);
        constraintLayoutCat5 =findViewById(R.id.constraintLayoutCat5);
        constraintLayoutCatEk1 =findViewById(R.id.constraintLayoutCatEk1);
        constraintLayoutEk2 =findViewById(R.id.constraintLayoutEk2);


        // Banner reklam ayarlar?? ba??lang????
        AdView adView = new AdView(this);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Reklam??n??z??n davran??????n?? daha da ??zelle??tirmek i??in, reklam??n ya??am d??ng??s??ndeki bir dizi olaya ba??lanabilirsiniz: y??kleme, a??ma, kapatma vb. Bu olaylar?? AdListener s??n??f?? ??zerinden dinleyebilirsiniz.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {  // onAdLoaded() y??ntemi, bir reklam??n y??klenmesi tamamland??????nda y??r??t??l??r. ??rne??in, bir reklam??n y??klenece??inden emin olana kadar etkinli??inize veya AdView eklemeyi geciktirmek istiyorsan??z, bunu burada yapabilirsiniz.
                Log.e("onAdLoaded", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {     // onAdFailedToLoad() y??ntemi, parametre i??eren tek y??ntemdir. LoadAdError error parametresi, hangi hatan??n olu??tu??unu a????klar.
                Log.e("onAdFailedToLoad", "onAdFailedToLoad");
                Log.e("onAdFailedToLoad", String.valueOf(adError));
            }

            @Override
            public void onAdOpened() {      // Bu y??ntem, kullan??c?? bir reklama dokundu??unda ??a??r??l??r.
                Log.e("onAdOpened", "onAdOpened");

            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {      // Bir kullan??c??, bir reklam??n hedef URL'sini g??r??nt??ledikten sonra uygulamaya d??nd??????nde, bu y??ntem ??a??r??l??r.
                Log.e("onAdClosed", "onAdClosed");
            }
        });
        // Banner reklam ayarlar?? son


        clickCategory(constraintLayoutCat1,"Cat1");
        clickCategory(constraintLayoutCat2,"Cat2");
        clickCategory(constraintLayoutCat3,"Cat3");
        clickCategory(constraintLayoutCat4,"Cat4");
        clickCategory(constraintLayoutCat5,"Cat5");
        clickCategory(constraintLayoutCatEk1,"CatEk1");
        clickCategory(constraintLayoutEk2,"CatEk2");


    }

    // Kategori se??me fonksiyonu
    public void clickCategory(ConstraintLayout constraintLayout, String category)
    {
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chosenCategory = category;

                if(category.equals("CatEk1") || category.equals("CatEk2"))
                {

                    Intent intent=new Intent(CategoryPage.this, EkLevelPage.class);
                    intent.putExtra("chosenCategory", chosenCategory);
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(CategoryPage.this, LevelPage.class);
                    intent.putExtra("chosenCategory", chosenCategory);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();   // exit the game, close the game
    }
}