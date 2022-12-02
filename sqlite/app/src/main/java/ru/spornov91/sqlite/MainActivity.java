package ru.spornov91.sqlite;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.database.sqlite.*;
import android.content.*;
import android.util.*;
import android.view.View.*;
import android.view.*;
import android.database.*;

public class MainActivity extends Activity implements OnClickListener
{
	final String LOG_TAG = "spornov91";

	Button btnAdd, btnRead, btnClear;
	EditText etName, etEmail;

	DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		btnAdd = findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);

		Button btnSort = findViewById(R.id.btnSortByName);
		btnSort.setOnClickListener(this);
		
		btnRead = findViewById(R.id.btnRead);
		btnRead.setOnClickListener(this);

		btnClear = findViewById(R.id.btnClear);
		btnClear.setOnClickListener(this);

		etName = findViewById(R.id.etName);
		etEmail = findViewById(R.id.etEmail);

		// создаем объект для создания и управления версиями БД
		dbHelper = new DBHelper(this);
		// dbpath -> /data/user/0/ru.spornov91.sqlite/databases/dbsqlite
		// String dbpath = getDatabasePath("dbsqlite").getAbsolutePath();
		
        mockDb();
		// эмулируем нажатие кнопки
		onClick(btnRead);
		
		String names[] = { "Китай", "США", "Бразилия", "Россия", "Япония",
			"Германия", "Египет", "Италия", "Франция", "Канада" };
		//находим список
		ListView lvMain = findViewById(R.id.listView1);

		// создаем адаптер
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
																android.R.layout.simple_list_item_1, names);

		// присваиваем адаптер списку
		lvMain.setAdapter(adapter);
    };

	@Override
	public void onClick(View v)
	{
		// подключаемся к БД
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		switch (v.getId())
		{
			case R.id.btnAdd:
				Log.d(LOG_TAG, "--- Insert in mytable: ---");
				// подготовим данные для вставки в виде пар: наименование столбца - значение
                // создаем объект для данных
				ContentValues cv = new ContentValues();

				// получаем данные из полей ввода
				String name = etName.getText().toString();
				String email = etEmail.getText().toString();
				
				cv.put("name", name);
				cv.put("email", email);
				// вставляем запись и получаем ее ID
				long rowID = db.insert("mytable", null, cv);
				Log.d(LOG_TAG, "row inserted, ID = " + rowID);
				
				break;
			case R.id.btnRead:
				Log.d(LOG_TAG, "--- Rows in mytable: ---");
				// делаем запрос всех данных из таблицы mytable, получаем Cursor 
				Cursor c = db.query("mytable", null, null, null, null, null, null);
                showAllTable(c);
				
				break;
			case R.id.btnClear:
				Log.d(LOG_TAG, "--- Clear mytable: ---");
				// удаляем все записи
				int clearCount = db.delete("mytable", null, null);
				Log.d(LOG_TAG, "deleted rows count = " + clearCount);
				
				break;
			case R.id.btnSortByName:
				Log.d(LOG_TAG, "--- Сортировка по наименованию ---");
				String orderBy = "name";
				c = db.query("mytable", null, null, null, null, null, orderBy);
				showSortTable(c);
				
				break;
		}
		// закрываем подключение к БД
		dbHelper.close();
	};
	
	
	
	private void showAllTable(Cursor c){
		// ставим позицию курсора на первую строку выборки
		// если в выборке нет строк, вернется false
		if (c.moveToFirst())
		{

			// определяем номера столбцов по имени в выборке
			int idColIndex = c.getColumnIndex("id");
			int nameColIndex = c.getColumnIndex("name");
			int emailColIndex = c.getColumnIndex("email");

			do {
				// получаем значения по номерам столбцов и пишем все в лог
				Log.d(LOG_TAG,
					  "ID = " + c.getInt(idColIndex) + 
					  ", name = " + c.getString(nameColIndex) + 
					  ", email = " + c.getString(emailColIndex));
				// переход на следующую строку 
				// а если следующей нет (текущая - последняя), то false - выходим из цикла
			} while (c.moveToNext());
		}
		else
			Log.d(LOG_TAG, "0 rows");
		c.close();
	};
	
	private void showSortTable(Cursor c)
	{
		if (c != null) {
			if (c.moveToFirst()) {
				String str;
				do {
					str = "";
					for (String cn : c.getColumnNames()) {
						str = str.concat(cn + " = "
										 + c.getString(c.getColumnIndex(cn)) + "; ");
					}
					Log.d(LOG_TAG, str);

				} while (c.moveToNext());
			}
			c.close();
		} else
			Log.d(LOG_TAG, "Cursor is null");
	};
	
	private void mockDb()
	{
		// подключаемся к базе
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// проверка существования записей
		Cursor c = db.query("mytable", null, null, null, null, null, null);
		if (c.getCount() == 0)
		{
			ContentValues cv = new ContentValues();

			// заполним таблицу
			String name[] = { "Китай", "США", "Бразилия", "Россия", "Япония",
				"Германия", "Египет", "Италия", "Франция", "Канада" };
			String region[] = { "Азия", "Америка", "Америка", "Европа", "Азия",
				"Европа", "Африка", "Европа", "Европа", "Америка" };

			for (int i = 0; i < 10; i++)
			{
				cv.put("name", name[i]);
				cv.put("email", region[i]);
				Log.d(LOG_TAG, "id = " + db.insert("mytable", null, cv));
			}
		}
		c.close();
		dbHelper.close();
	};
	
	
};



class DBHelper extends SQLiteOpenHelper 
{

	final String LOG_TAG = "spornov91";

    public DBHelper(Context context)
	{
		// конструктор суперкласса
		super(context, "dbsqlitev1", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
	{
		Log.d(LOG_TAG, "--- onCreate database ---");
		// создаем таблицу с полями
		db.execSQL("create table mytable ("
				   + "id integer primary key autoincrement,"
				   + "name text,"
				   + "email text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

    }
};
