package com.example.solar.metricas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

class DB_Controller extends SQLiteOpenHelper {

    public DB_Controller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "TEST.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE REGISTROS(ID INTEGER PRIMARY KEY AUTOINCREMENT , N_OPERADORES INT , N_OPERANDOS INT , T_OPERADORES INT , T_OPERANDOS REAL, n REAL ,V REAL , D REAL , L REAL , E REAL , T REAL , B  REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS REGISTROS");
        onCreate(db);

    }

    public void insertar(int x , int y, int N1 , int N2 , double n , double V , double D, double L, double E, double T, double B ){
        ContentValues contentValues = new ContentValues();
        contentValues.put("N_OPERADORES",x);
        contentValues.put("N_OPERANDOS",y);
        contentValues.put("T_OPERADORES",N1);
        contentValues.put("T_OPERANDOS",N2);
        contentValues.put("n",n);
        contentValues.put("V",V);
        contentValues.put("D",D);
        contentValues.put("L",L);
        contentValues.put("E",E);
        contentValues.put("T",T);
        contentValues.put("B",B);



        this.getWritableDatabase().insertOrThrow("REGISTROS","",contentValues);
    }

    public void eliminar(TextView textView){
        this.getWritableDatabase().delete("REGISTROS","",null);
        textView.setText("");

    }

    public void mostrar(TextView textView){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM REGISTROS",null);

        textView.setText("");

        while (cursor.moveToNext()){
            textView.append("n1: "+cursor.getString(1)+"  n2: "+cursor.getString(2)+"  N1: "+cursor.getString(3)+
                    "  N2: "+ cursor.getString(4)+'\n'+"Vocabulario"+cursor.getString(5)+"   Volumen: "+cursor.getString(6)+'\n'+
                    "Dificultad: "+cursor.getString(7)+"  Nivel: "+cursor.getString(8)+ "  Esfuerzo: "+cursor.getString(9)+'\n'+
                    "Tiempo: "+cursor.getString(10)+"  Numero: "+cursor.getString(11)+'\n'+'\n'+'\n');
        }
    }
}
