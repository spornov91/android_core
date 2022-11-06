package ru.spornov91.navfrag;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity implements View.OnClickListener 
{

	@Override
	public void onClick(View p1)
	{
		switch (p1.getId()) {
            case R.id.bfrag1:
				Toast.makeText(getApplicationContext(),"click bfrag1",7);
				ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.sframe, new Frag1());
				ft.commit();
                break;
            case R.id.bfrag2:
				Toast.makeText(getApplicationContext(),"click bfrag2",7);
				ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.sframe, new Frag3());
				ft.commit();
                break;
			case R.id.bfrag3:
				Toast.makeText(getApplicationContext(),"click bfrag2",7);
				Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
				startActivity(intent);
				break;
			default : break;
		}
	}
	
	FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		Button bfrag1 = findViewById(R.id.bfrag1);
		bfrag1.setOnClickListener(this);
		Button bfrag2 = findViewById(R.id.bfrag2);
		bfrag2.setOnClickListener(this);
		Button bfrag3 = findViewById(R.id.bfrag3);
		bfrag3.setOnClickListener(this);
	};
};
