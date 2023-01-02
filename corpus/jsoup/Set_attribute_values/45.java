package com.schelb.fernando.jsoupexemple;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String url = "http://www.corridasbr.com.br/df/calendario.asp";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button titleButton = (Button) findViewById(R.id.title_button);
        Button descrButton = (Button) findViewById(R.id.descr_button);
        Button logoButton = (Button) findViewById(R.id.logo_button);
        Button tabelaButton = (Button) findViewById(R.id.bt_tabela);

        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Title().execute();

            }
        });

        descrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Description().execute();
            }
        });

        logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Logo().execute();
            }
        });

        tabelaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Tabela().execute();
            }
        });

    }

    private class Title extends AsyncTask<Void,Void,Void>{

        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Title");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document document = Jsoup.connect(url).get();

                title = document.title();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView txtTitle = (TextView) findViewById(R.id.title_txt);
            txtTitle.setText(title);
            progressDialog.dismiss();
        }
    }

    private class Description extends AsyncTask<Void,Void,Void>{

        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Description");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document document = Jsoup.connect(url).get();

                Elements description = document.select("meta[name=description]");
                desc = description.attr("content");

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView txtDescr = (TextView) findViewById(R.id.descr_txt);
            txtDescr.setText(desc);
            progressDialog.dismiss();
        }
    }

    private class Logo extends AsyncTask<Void,Void,Void>{

        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Logo");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document document = Jsoup.connect(url).get();

                Element img = document.select("img").first();

                String scrValue = img.attr("src");
                InputStream input = new URL(scrValue).openStream();
                bitmap = BitmapFactory.decodeStream(input);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            ImageView ivLogo = (ImageView) findViewById(R.id.logo);
            ivLogo.setImageBitmap(bitmap);
            progressDialog.dismiss();

        }
    }

    private class Tabela extends AsyncTask<Void,Void,Void>{

        String tabela;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Tabela");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document document = Jsoup.connect(url).get();

                Element table = document.select("table").get(5);
                Elements rows  = table.select("tr");
                Element row = rows.get(0);
                Elements testes1 = row.select("table");
                Element teste1 = testes1.get(1);
                Elements testes2 = teste1.select("tr");
                Element teste2 = testes2.get(0);
                tabela = "O Primeiro mês é: "+teste2.text();


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView txtDescr = (TextView) findViewById(R.id.tv_tabela);
            txtDescr.setText(tabela);
            progressDialog.dismiss();
        }
    }


}
