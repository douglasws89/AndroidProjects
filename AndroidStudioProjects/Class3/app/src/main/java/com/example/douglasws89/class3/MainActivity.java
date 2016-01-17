package com.example.douglasws89.class3;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;
import android.widget.TextView;
import android.widget.*;
import android.content.DialogInterface.OnClickListener;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int count = 0;
    char[] A = new char[9];
    int[] index = new int[3];

    public void clickBoard(View v) {
        count++;
        // Get id of button
        int i = v.getId();
        // More useful, let's get the tag.
        TextView tv = (TextView) findViewById(R.id.textView);
        // If you want to put a cross on it.
        ImageButton vv = (ImageButton) v;
        if( count % 2 == 0){
            vv.setImageResource(R.drawable.cross2);
            A[i - 2131492945] = 'x';
        }
        else{
            vv.setImageResource(R.drawable.circle2);
            A[i - 2131492945] = 'o';
        }

        vv.setClickable(false);

        String finalResult = checkWinner();
        if (finalResult == "x"){
            tv.setText("X Won!");
            for(int z = 0; z<3;z++){
                ImageButton highlight = (ImageButton) findViewById(index[z] + 2131492945);
                highlight.setImageResource(R.drawable.cross2_gold);
            }
        }
        else if (finalResult == "o"){
            tv.setText("O Won!");
            for(int z = 0; z<3;z++){
                ImageButton highlight = (ImageButton) findViewById(index[z] + 2131492945);
                highlight.setImageResource(R.drawable.circle2_gold);
            }
        }
        else if (finalResult == "" && count >= 9){
            tv.setText("Tie!");
        }
    }

    public String checkWinner(){

        for (int i = 0; i < 9; i = i+3){
            if(A[i] == 'x' && A[i+1] == 'x' && A[i+2] == 'x'){
                index[0] = i;
                index[1] = i+1;
                index[2] = i+2;
                return "x";
            }

            if(A[i] == 'o' && A[i+1] == 'o' && A[i+2] == 'o'){
                index[0] = i;
                index[1] = i+1;
                index[2] = i+2;
                return "o";
            }
        }

        for (int i = 0; i < 3; i++){
            if(A[i] == 'x' && A[i+3] == 'x' && A[i+6] == 'x'){
                index[0] = i;
                index[1] = i+3;
                index[2] = i+6;
                return "x";
            }
            if(A[i] == 'o' && A[i+3] == 'o' && A[i+6] == 'o'){
                index[0] = i;
                index[1] = i+3;
                index[2] = i+6;
                return "o";
            }
        }

        if((A[0] == 'x' && A[4] == 'x' && A[8] == 'x') || ((A[2] == 'x' && A[4] == 'x' && A[6] == 'x'))){
                if((A[0] == 'x') && (A[8] == 'x')) {
                    index[0] = 0;
                    index[1] = 4;
                    index[2] = 8;
                }
                else if((A[2] == 'x') && (A[6] == 'x')){
                    index[0] = 2;
                    index[1] = 4;
                    index[2] = 6;
                }
            return "x";
        }
        if((A[0] == 'o' && A[4] == 'o' && A[8] == 'o') || (A[2] == 'o' && A[4] == 'o' && A[6] == 'o')){
            if((A[0] == 'o') && (A[8] == 'o')) {
                index[0] = 0;
                index[1] = 4;
                index[2] = 8;
            }
            else if((A[2] == 'o') && (A[6] == 'o')){
                index[0] = 2;
                index[1] = 4;
                index[2] = 6;
            }
            return "o";
        }

        return "";
    }

    public void resetGame(View v){

        for (int a = 0; a<9; a++){
            ImageButton imageButton = (ImageButton) findViewById(a + 2131492945);
            imageButton.setImageResource(android.R.color.transparent);
            imageButton.setClickable(true);
        }

        for(int z = 0; z<=2;z++){
            ImageButton highlight = (ImageButton) findViewById((index[z] + 2131492945));
        }

        for(int i = 0; i<9; i++){
            A[i] = ' ';
        }

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("Tic Tac Toe");
        count = 0;
    }
}
