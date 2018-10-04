package com.example.solar.metricas;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.io.*;
import java.util.*;


public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final String LOG_TAG = "MainActivity";

    DB_Controller controller;

    Button b_load,b_guardar , b_eliminar , b_mostrar;
    TextView tv_output , tv_n1 , tv_n2 , tv_N1 , tv_N2 , tv_longitud, tv_vocabulario , tv_volumen , tv_dificultad , tv_nivel , tv_esfuerzo , tv_tiempo , tv_numero,tv_prueba;

    Set<String> set_simbolos = new HashSet<String>();
    Set<String> set_palabras_reservadas = new HashSet<String>();
    Set<String> set_variables = new HashSet<String>();
    Set<String> set_numeros = new HashSet<String>();

    float simbolos;

    int n_palabras_r , n_variables , n_numeros;

    //Tokens del programa
    int p;

    //variables o nmeros
    int operandos;

    //Simbolos y palabras reservadas
    int operadores;

    //Unicos o distintos
    int n1, n2;

    //Ocurrencias
    int N1 , N2;

    //Diferentes medidas
    double longitud;
    double vocabulario;
    double volumen;
    double dificultad;
    double nivel;
    double esfuerzo;
    double tiempo;
    double numero;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }

        controller = new DB_Controller(this,"",null,3);


        b_load = findViewById(R.id.load_button);
        b_guardar = findViewById(R.id.save_button);
        b_eliminar = findViewById(R.id.borrar_button);
        b_mostrar = findViewById(R.id.ver_button);

        tv_output = findViewById(R.id.load_text);

        tv_n1 = findViewById(R.id.txt_n1);
        tv_N1 = findViewById(R.id.txt_N1);

        tv_n2 = findViewById(R.id.txt_n2);
        tv_N2 = findViewById(R.id.txt_N2);

        tv_longitud = findViewById(R.id.txt_longitud);
        tv_vocabulario = findViewById(R.id.txt_vocabulario);
        tv_volumen = findViewById(R.id.txt_vocabulario);
        tv_dificultad = findViewById(R.id.txt_dificultad);
        tv_nivel = findViewById(R.id.txt_nivel);
        tv_esfuerzo = findViewById(R.id.txt_esfuerzo);
        tv_tiempo = findViewById(R.id.txt_tiempo);
        tv_numero = findViewById(R.id.txt_numerobugs);

        tv_prueba = findViewById(R.id.tv_prueba);


        b_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();

            }
        });

        b_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.insertar(n1,n2,N1,N2,vocabulario,volumen,dificultad,nivel,esfuerzo,tiempo,numero);
            }
        });

        b_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.eliminar(tv_prueba);
            }
        });

        b_mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.mostrar(tv_prueba);
            }
        });




    }

    //Leyendo contenido del archivo
    private String readText(String input){
        File file = new File(input);
        StringBuilder text = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null){
                text.append(line);
                text.append("\n");
            }
            br.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }


        return text.toString();
    }


    //Seleccionar archivo
    private void performFileSearch(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if (data!= null){
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":")+1);
                Toast.makeText(this,""+path,Toast.LENGTH_SHORT).show();
                String texto = readText(path);
                tv_output.setText(texto);
                busqueda(texto);
                contarSimbolos(texto);

                //Unicos o distintos
                n1 = set_simbolos.size()+set_palabras_reservadas.size();
                n2 = set_numeros.size()+set_variables.size();

                //Ocurrencias
                N1 = (int)simbolos+n_palabras_r;
                N2 =  n_numeros+n_variables;

                //formulas de medidas
                longitud = N1 + N2;
                vocabulario = n1+n2;
                volumen = longitud * Math.log(((double)vocabulario));
                dificultad = (n1/2)*(N2/n2);
                nivel = 1/dificultad;
                esfuerzo = dificultad*volumen;
                tiempo = esfuerzo/18;
                numero = Math.pow(esfuerzo,(2/3))/3000;

                tv_n1.setText(String.valueOf(n1));
                tv_n2.setText(String.valueOf(n2));
                tv_N1.setText(String.valueOf(N1));
                tv_N2.setText(String.valueOf(N2));

                tv_longitud.setText(String.valueOf(longitud));
                tv_vocabulario.setText(String.valueOf(vocabulario));
                tv_volumen.setText(String.valueOf(volumen));
                tv_dificultad.setText(String.valueOf(dificultad));
                tv_nivel.setText(String.valueOf(nivel));
                tv_esfuerzo.setText(String.valueOf(esfuerzo));
                tv_tiempo.setText(String.valueOf(tiempo));
                tv_numero.setText(String.valueOf(numero));
            }
        }
    }




    //permisos de acceso al almacenamiento
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_STORAGE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permiso Concedido",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"Permiso NO Concedido",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    
    //Comprueba si es una palabra reservada
    public boolean esPalabraReservada(String texto){
            //Tipos de dato
            if (texto.contains("int")||texto.contains("double")||texto.contains("float")||texto.contains("char")||texto.contains("long")||texto.contains("short")||texto.contains("unsigned")||texto.contains("void")||texto.contains("constant")||

                    //ciclos
                    texto.contains("for")   ||texto.contains("while")     ||
                    texto.contains("break") ||texto.contains("continue")  ||
                    texto.contains("return")||texto.contains("main")      ||
                    texto.contains("sizeof")||texto.contains("if")        ||texto.contains("else")||
                    texto.contains("main")  ||texto.contains("struct")    ||texto.contains("do")||
                    texto.contains("case")  ||texto.contains("static"))
            {
                return true;

            } else { return false; }
    }




    public void busqueda(String texto){ //Nuevo metodo

        //Recortamos los espacios que pueda tener
        texto.trim();

        Scanner scanner = new Scanner(texto);


        //Leemos linea por linea
        while(scanner.hasNextLine()) {

            String linea = scanner.nextLine();

            //Reccorremos carctaer por caracter
            for (int i = 0 ; i < linea.length() ; i++){
                //Caracter actual
                char t = linea.charAt(i);

                //Comprobando si es digito
                if (Character.isDigit(t)){
                    String numero = "";
                    numero += t;
                    int j = i+1;

                    //Comprobamos si es un digito de mas de un numero
                    while (Character.isDigit(linea.charAt(j))){
                        numero+=linea.charAt(j);
                        j++;
                        //Si el contador llego a final de linea parar
                        if (j == linea.length()) break;;
                    }

                    Log.e(LOG_TAG,"Se hallo un numero; "+numero);
                    //Anadomos el nmero encontrado a la lista de numeros
                    set_numeros.add(numero);
                    n_numeros++;
                    i = j;
                    continue;
                }//Fin es digito


                //Comprobando si es letra o palabra
                else if (Character.isLetter(t)){

                    String letra = "";

                    letra+=t;

                    int j=i+1;

                    while (Character.isLetterOrDigit(linea.charAt(j))){
                        letra+=linea.charAt(j);
                        j++;
                        if (j==linea.length()) break;
                    }

                    i = j;

                    if (esPalabraReservada(letra)){
                        set_palabras_reservadas.add(letra);
                        n_palabras_r++;
                        Log.e(LOG_TAG,"Se encontro palabra reservada: "+letra);
                    }

                    else{
                        set_variables.add(letra);
                        n_variables++;
                        Log.e(LOG_TAG,"Se encontro una variable o identificador: "+letra);
                    }

                    continue;

                }

                else if (!Character.isLetterOrDigit(t)){

                    i++;

                    continue;
                }

            }

        }

        operadores+=set_palabras_reservadas.size();

        Log.e(LOG_TAG,"TOTAL  Variables:"+n_variables+"  Palabras Reservadas:"+n_palabras_r+"  Simbolos:"+simbolos+"   Numeros"+n_numeros);

        scanner.close();
    }


    public void contarSimbolos(String texto) {

        Scanner scannerTexto = new Scanner(texto);



        while (scannerTexto.hasNextLine()) {

            String parrafo = scannerTexto.nextLine();


            //Busqueda de simbolos 
            String[] caracteres = parrafo.split("");

            for (int i = 0; i < caracteres.length; i++) {
                //Operadores basicos
                if (caracteres[i].contains("+") || caracteres[i].contains("-") || caracteres[i].contains("*") || caracteres[i].contains("/") ||
                        //Comparadores
                        caracteres[i].contains("<=") || caracteres[i].contains(">=") || caracteres[i].contains("==") || caracteres[i].contains("!=") ||
                        //Simbolos
                        caracteres[i].contains(",") || caracteres[i].contains(";") || caracteres[i].contains("<") || caracteres[i].contains(">") || caracteres[i].contains("=")) {
                    simbolos++;
                    set_simbolos.add(caracteres[i]);

                    Log.e(LOG_TAG,"Simbolo basico: "+caracteres[i]+"     Cantidad:"+simbolos);

                }
                //Signos de agrupacion , se necita el par para que valgan 1
                else if (caracteres[i].contains("(") || caracteres[i].contains(")") || caracteres[i].contains("{") || caracteres[i].contains("}") || caracteres[i].contains("[") || caracteres[i].contains("]")) {
                    simbolos += 0.5;
                    set_simbolos.add(caracteres[i]);
                    Log.e(LOG_TAG,"Simbolo corchetes"+caracteres[i]+"     Cantidad:"+simbolos);

                }
            }

        }

        Log.e(LOG_TAG,"Simbolos encontrados: "+simbolos);

        scannerTexto.close();


    }
}
