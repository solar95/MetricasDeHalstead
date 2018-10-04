import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

public class BaseHelper extends SQLiteOpenHelper{
    public BaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "TEST.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE REGISTROS(ID INTEGER PRIMARY KEY AUTOINCREMENT , N1 INT , N2 INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS REGISTROS");
        onCreate(db);

    }

    public void insertar(int n1 , int n2){
        ContentValues contentValues = new ContentValues();
        contentValues.put("N1",n1);
        contentValues.put("N2",n2);
        this.getWritableDatabase().insertOrThrow("REGISTROS","",contentValues);
    }

    public void eliminar(int n1){
        this.getWritableDatabase().delete("REGISTROS","N1='"+n1+"'",null);
    }

    public void mostrar(TextView textView){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM REGISTROS",null);

        while (cursor.moveToNext()){
            textView.append(cursor.getString(1)+" "+cursor.getString(2));
        }
    }
}