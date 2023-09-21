package bo.ae.gameten10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import bo.ae.gameten10.R;
import bo.ae.gameten10.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private int matrix[][], luumatrix[][];
    private int scores = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityMainBinding  = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        hideSystemUI();
        mapping();
        try {
            setUp();
        } catch (Exception e) {
            setUp();
        }
        final GestureDetector gestureDetector = new GestureDetector(this, new Claw());
        activityMainBinding.undoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unDoClick();
            }
        });
        activityMainBinding.playLinear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                int diemCao = getMaxPoint();
                String diemCurrentString= activityMainBinding.textPoint.getText().toString();
                int diemCurrent=Integer.valueOf(diemCurrentString);
                if (diemCurrent>diemCao){
                    setMaxPoint(diemCurrent);
                    Log.d("Test_3", "onFling: "+diemCurrent);
                    Log.d("Test_3", "onFling: "+diemCao);
                    activityMainBinding.maxPointTv.setText(diemCurrent+"");
                }
                return true;
            }
        });
        activityMainBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                String strPoint=activityMainBinding.textPoint.getText().toString();
                if (Integer.parseInt(strPoint)>0){
                    DBHelper dbHelper = new DBHelper(MainActivity.this);
                    dbHelper.addCount(Integer.parseInt(strPoint));}
            }
        });
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }



    public void unDoClick()
    {
        boolean check = true;
        for(int i=1;i<25;i++)
        {
            if( matrix[i/5][i%5] != luumatrix[i/5][i%5])
            {
                check = false; break;
            }
        }
        if (check==true)
        {
            Toast.makeText(this,"Come back 1 time!", Toast.LENGTH_SHORT).show();
            return;
        }
        for(int i=1;i<25;i++) matrix[i/5][i%5] = luumatrix[i/5][i%5];
        setBox();
    }

    private void mapping() {
        matrix = new int[7][6];
        luumatrix = new int[7][6];
    }
    public void setUp() {
        int diemcao = getMaxPoint();
        activityMainBinding.maxPointTv.setText(String.valueOf(diemcao));
        scores = 0;
        for(int i=0;i<25;i++) matrix [i/5][i%5]=0;
        ranDomNumber();
        setSaveMatrix();
        setBox();
    }


    class Claw  extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean check = false;
            setSaveMatrix();
            if(e1.getX() - e2.getX() > 100 && Math.abs(e1.getY() - e2.getY()) <  Math.abs(e1.getX() - e2.getX()) && Math.abs(velocityX) > 100){
                for(int i=1;i<5;i++){
                    for(int j=1;j<4;j++)
                    {
                        if(matrix[i][j]!=0)
                        {
                            for(int k=j+1; k<5;k++)
                            {
                                if(matrix[i][k]==matrix[i][j])
                                {
                                    check = true;
                                    matrix[i][j]+=1;
                                    scores+=matrix[i][j];
                                    matrix[i][k]=0;j=k;break;
                                }else if(matrix[i][k]!=0) break;
                            }
                        }
                    }
                    for(int j=1;j<4;j++)
                    {
                        if(matrix[i][j] == 0)
                        {
                            for(int k=j+1; k<5;k++)
                            {
                                if(matrix[i][k]!=0)
                                {
                                    check = true;
                                    matrix[i][j]=matrix[i][k];
                                    matrix[i][k]=0; break;
                                }
                            }
                        }
                    }
                }
            }else if(e2.getX() - e1.getX() > 100  && Math.abs(e1.getY() - e2.getY()) <   Math.abs(e1.getX() - e2.getX()) && Math.abs(velocityX) > 100)
            {
                for(int i=1;i<5;i++){
                    for(int j=4;j>1;j--){

                        if( matrix[i][j]!=0)
                        {
                            for(int k=j-1; k>0;k--)
                            {
                                if(matrix[i][k]==matrix[i][j])
                                {
                                    check =true;
                                    matrix[i][j]+=1;
                                    scores+=matrix[i][j];
                                    matrix[i][k]=0; j = k; break;
                                }else if(matrix[i][k]!=0) break;
                            }
                        }
                    }
                    for(int j=4;j>1;j--)
                    {
                        if(matrix[i][j] == 0)
                        {
                            for(int k=j-1; k>0;k--)
                            {
                                if(matrix[i][k]!=0)
                                {
                                    check = true;
                                    matrix[i][j]=matrix[i][k];
                                    matrix[i][k]=0;  break;
                                }
                            }
                        }
                    }
                }
            }
            //vuot len
            else if(e1.getY() - e2.getY() > 100 && Math.abs(e1.getX() - e2.getX()) <   Math.abs(e1.getY() - e2.getY()) && Math.abs(velocityY) > 100){
                for(int i=1;i<5;i++)
                {
                    for(int j=1;j<4;j++){
                        if(matrix[j][i]!=0)
                        {
                            for(int k=j+1;k<5;k++)
                            {
                                if(matrix[k][i]==matrix[j][i])
                                {
                                    check=true;
                                    matrix[j][i] += 1;
                                    scores+=matrix[j][i];
                                    matrix[k][i]=0; j=k; break;
                                }if(matrix[k][i]!=0) break;
                            }
                        }
                    }
                    for(int j=1;j<4;j++)
                    {
                        if(matrix[j][i] == 0)
                        {
                            for(int k=j+1;k<5;k++)
                            {
                                if(matrix[k][i]!=0)
                                {
                                    check = true;
                                    matrix[j][i] = matrix[k][i];
                                    matrix[k][i]=0; break;
                                }
                            }
                        }
                    }
                }
            }
            //vuot xuong
            else if(e2.getY() - e1.getY() > 100  && Math.abs(e1.getX() - e2.getX()) <   Math.abs(e1.getY() - e2.getY()) && Math.abs(velocityY) > 100)
            {
                for(int i=1;i<5;i++)
                {
                    for(int j=4;j>1;j--)
                    {
                        if(matrix[j][i] != 0)
                        {
                            for(int k=j-1;k>0;k--)
                            {
                                if(matrix[k][i]==matrix[j][i])
                                {
                                    check=true;
                                    matrix[j][i]+= 1;
                                    scores+=matrix[j][i];
                                    matrix[k][i]=0; j=k; break;
                                }else if(matrix[k][i] != 0) break;
                            }
                        }
                    }
                    for(int j=4;j>1;j--)
                    {
                        if(matrix[j][i] == 0)
                        {
                            for(int k=j-1;k>0;k--)
                            {
                                if(matrix[k][i]!=0)
                                {
                                    check = true;
                                    matrix[j][i] = matrix[k][i];
                                    matrix[k][i]=0; break;
                                }
                            }
                        }
                    }
                }
            }
            if(check==true)ranDomNumber();
            setBox();
            checkForWin();
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
    private void checkForWin() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                if (matrix[i][j] == 10) {
                    showAlertDialog();
                    return;
                }
            }
        }
    }
    private void showAlertDialog() {
        Dialog customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // Remove title
        customDialog.setContentView(R.layout.dialog_2048_win); // Set your layout
        customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        customDialog.setCancelable(false);

        // Set dialog properties (size, position, etc.)
        Window window = customDialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER); // Center the dialog
        }
        ImageView dialogImageView = customDialog.findViewById(R.id.playImg);
        TextView pointTv = customDialog.findViewById(R.id.pointTv);
        pointTv.setText("Point: "+activityMainBinding.textPoint.getText());
        dialogImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUp();
                customDialog.dismiss();
                hideSystemUI();
            }
        });
        // Show the dialog
        customDialog.show();
    }
    public void setBox() {
         activityMainBinding.textPoint.setText(String.valueOf(scores));
         activityMainBinding.box11.setImageResource(getBackgroundColor(matrix[1][1]));
         activityMainBinding.box12.setImageResource(getBackgroundColor(matrix[1][2]));
         activityMainBinding.box13.setImageResource(getBackgroundColor(matrix[1][3]));
         activityMainBinding.box14.setImageResource(getBackgroundColor(matrix[1][4]));

         activityMainBinding.box21.setImageResource(getBackgroundColor(matrix[2][1]));
         activityMainBinding.box22.setImageResource(getBackgroundColor(matrix[2][2]));
         activityMainBinding.box23.setImageResource(getBackgroundColor(matrix[2][3]));
         activityMainBinding.box24.setImageResource(getBackgroundColor(matrix[2][4]));


         activityMainBinding.box31.setImageResource(getBackgroundColor(matrix[3][1]));
         activityMainBinding.box32.setImageResource(getBackgroundColor(matrix[3][2]));
         activityMainBinding.box33.setImageResource(getBackgroundColor(matrix[3][3]));
         activityMainBinding.box34.setImageResource(getBackgroundColor(matrix[3][4]));


         activityMainBinding.box41.setImageResource(getBackgroundColor(matrix[4][1]));
         activityMainBinding.box42.setImageResource(getBackgroundColor(matrix[4][2]));
         activityMainBinding.box43.setImageResource(getBackgroundColor(matrix[4][3]));
         activityMainBinding.box44.setImageResource(getBackgroundColor(matrix[4][4]));


        String matrixS[][] = new String[7][6];
        for(int i=0;i<25;i++)
        {
            if(matrix[i/5][i%5]==0) matrixS[i/5][i%5]="";
            else matrixS[i/5][i%5] = String.valueOf(matrix[i/5][i%5]);
        }
        setSizetext();
         activityMainBinding.text11.setText(matrixS[1][1]);
         activityMainBinding.text12.setText(matrixS[1][2]);
         activityMainBinding.text13.setText(matrixS[1][3]);
         activityMainBinding.text14.setText(matrixS[1][4]);

         activityMainBinding.text21.setText(matrixS[2][1]);
         activityMainBinding.text22.setText(matrixS[2][2]);
         activityMainBinding.text23.setText(matrixS[2][3]);
         activityMainBinding.text24.setText(matrixS[2][4]);
         activityMainBinding.text31.setText(matrixS[3][1]);
         activityMainBinding.text32.setText(matrixS[3][2]);
         activityMainBinding.text33.setText(matrixS[3][3]);
         activityMainBinding.text34.setText(matrixS[3][4]);
         activityMainBinding.text41.setText(matrixS[4][1]);
         activityMainBinding.text42.setText(matrixS[4][2]);
         activityMainBinding.text43.setText(matrixS[4][3]);
         activityMainBinding.text44.setText(matrixS[4][4]);
    }

    public void setSizetext() {
        if (matrix[1][1] <= 512)  activityMainBinding.text11.setTextSize(15);
        else  activityMainBinding.text11.setTextSize(8);
        if (matrix[2][1] <= 512)  activityMainBinding.text21.setTextSize(15);
        else  activityMainBinding.text21.setTextSize(8);
        if (matrix[3][1] <= 512)  activityMainBinding.text31.setTextSize(15);
        else  activityMainBinding.text31.setTextSize(8);
        if (matrix[4][1] <= 512)  activityMainBinding.text41.setTextSize(15);
        else  activityMainBinding.text41.setTextSize(8);

        if (matrix[1][2] <= 512)  activityMainBinding.text12.setTextSize(15);
        else  activityMainBinding.text12.setTextSize(8);
        if (matrix[2][2] <= 512)  activityMainBinding.text22.setTextSize(15);
        else  activityMainBinding.text22.setTextSize(8);
        if (matrix[3][2] <= 512)  activityMainBinding.text32.setTextSize(15);
        else  activityMainBinding.text32.setTextSize(8);
        if (matrix[4][2] <= 512)  activityMainBinding.text42.setTextSize(15);
        else  activityMainBinding.text42.setTextSize(8);


        if (matrix[1][3] <= 512)  activityMainBinding.text13.setTextSize(15);
        else  activityMainBinding.text13.setTextSize(8);
        if (matrix[2][3] <= 512)  activityMainBinding.text23.setTextSize(15);
        else  activityMainBinding.text23.setTextSize(8);
        if (matrix[3][3] <= 512)  activityMainBinding.text33.setTextSize(15);
        else  activityMainBinding.text33.setTextSize(8);
        if (matrix[4][3] <= 512)  activityMainBinding.text43.setTextSize(15);
        else  activityMainBinding.text43.setTextSize(8);



        if (matrix[1][4] <= 512)  activityMainBinding.text14.setTextSize(15);
        else  activityMainBinding.text14.setTextSize(8);
        if (matrix[2][4] <= 512)  activityMainBinding.text24.setTextSize(15);
        else  activityMainBinding.text24.setTextSize(8);
        if (matrix[3][4] <= 512)  activityMainBinding.text34.setTextSize(15);
        else  activityMainBinding.text34.setTextSize(8);
        if (matrix[4][4] <= 512)  activityMainBinding.text44.setTextSize(15);
        else  activityMainBinding.text44.setTextSize(8);



    }

    public int getBackgroundColor(int n) {
        if (n == 0) return R.drawable.img_0;
        switch (n % 2048) {
            case 1:
                return R.drawable.img_4;
            case 2:
                return R.drawable.img_512;
            case 3:
                return R.drawable.img_128;
            case 4:
                return R.drawable.img_64;
            case 5:
                return R.drawable.img_32;
            case 6:
                return R.drawable.img_16;
            case 7:
                return R.drawable.img_8;
            case 8:
                return R.drawable.img_4;
            case 9:
                return R.drawable.img_2;
            case 10:
                return R.drawable.img_64;
            case 0:
                return R.drawable.img_8;

        }
        return 0;
    }
    public void ranDomNumber()
    {
        Random random = new Random();
        while(true)
        {
            int i=random.nextInt(4)+1, j=random.nextInt(4)+1;
            if(matrix[i][j]==0)
            {
                if(random.nextInt(11) < 10
                ) matrix[i][j]=1;
                else matrix[i][j]=1;
                break;
            }
        }
    }
    private void setMaxPoint(int diemcao) {
        SharedPreferences sharedPref = getSharedPreferences("game2048", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("diemcao", diemcao);
        editor.apply();
    }

    private int getMaxPoint() {
        SharedPreferences sharedPref = getSharedPreferences("game2048", Context.MODE_PRIVATE);
        return sharedPref.getInt("diemcao", 0);  // 0 là giá trị mặc định
    }
    public void setSaveMatrix(){
        for(int i=1;i<25;i++) luumatrix[i/5][i%5] = matrix[i/5][i%5];
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }
}