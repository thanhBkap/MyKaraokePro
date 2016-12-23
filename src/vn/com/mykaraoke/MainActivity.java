package vn.com.mykaraoke;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;
import vn.com.adapter.BaiHatAdapter;
import vn.com.model.BaiHat;

public class MainActivity extends Activity {
	ListView lvBaiHat, lvBaiHatYeuThich;
	TabHost tabHost;
	ArrayList<BaiHat> arrBaiHat, arrBaiHatYeuThich;
	BaiHatAdapter baiHatAdapter, baiHatYeuThichAdapter;
	public static SQLiteDatabase database = null;
	public static String DATABASE_NAME = "Arirang.sqlite";
	String DB_PATH_SUFFIX = "/databases/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addControls();
		addEvents();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	private void addEvents() {
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equalsIgnoreCase("tab1")) {
					hienThiBaiHatGoc();
				} else if (tabId.equalsIgnoreCase("tab2")) {
					hienThiBaiHatYeuThich();
				}
			//	baiHatAdapter.notifyDataSetChanged();
			//	arrBaiHatYeuThich.clear();
				/*
				 * for (BaiHat bh : arrBaiHat) { if (bh.isThich()) {
				 * arrBaiHatYeuThich.add(bh); } }
				 */
		//		baiHatYeuThichAdapter.notifyDataSetChanged();
			}
		});

	}

	private void addControls() {
		lvBaiHat = (ListView) findViewById(R.id.lvBaiHat);
		lvBaiHatYeuThich = (ListView) findViewById(R.id.lvBaiHatYeuThich);
		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();
		TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
		tab1.setContent(R.id.tab1);
		// tab1.setIndicator("Bai hat");
		tab1.setIndicator("", getResources().getDrawable(R.drawable.music));
		tabHost.addTab(tab1);
		TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2");
		tab2.setContent(R.id.tab2);
		// tab2.setIndicator("Bai hat yeu thich");
		tab2.setIndicator("", getResources().getDrawable(R.drawable.heart));
		tabHost.addTab(tab2);
		xuLySaoChepDuLieu();
		arrBaiHat = new ArrayList<BaiHat>();
		arrBaiHatYeuThich = new ArrayList<BaiHat>();
		/*
		 * arrBaiHat.add(new BaiHat(99382,"Một nhà","Unknown",true));
		 * arrBaiHat.add(new BaiHat(56534,"Đêm tàn","Cherry",false));
		 * arrBaiHat.add(new BaiHat(79472,"Crush","David Archuletta",false));
		 * arrBaiHat.add(new BaiHat(44384,"Đôi mắt","Wanbi",true));
		 * arrBaiHat.add(new BaiHat(67346,"Đường cong","Phương Linh",true));
		 * arrBaiHat.add(new BaiHat(93646,"Radio","Hà Anh Tuấn",true));
		 */
		baiHatAdapter = new BaiHatAdapter(MainActivity.this, R.layout.item, arrBaiHat);
		lvBaiHat.setAdapter(baiHatAdapter);
		/*
		 * for(BaiHat bh:arrBaiHat){ if(bh.isThich()){
		 * arrBaiHatYeuThich.add(bh); } }
		 */
		baiHatYeuThichAdapter = new BaiHatAdapter(MainActivity.this, R.layout.item, arrBaiHatYeuThich);
		lvBaiHatYeuThich.setAdapter(baiHatYeuThichAdapter);
		hienThiBaiHatGoc();
	}

	private void xuLySaoChepDuLieu() {
		try {
			File file = getDatabasePath(DATABASE_NAME);
			if (!file.exists()) {
				saoChepDuLieu();
				Toast.makeText(this, "Sao chep thanh cong", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// Log.e("Error copy data", e.toString());
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	private void saoChepDuLieu() {
		InputStream input;
		try {
			// Becarefull for databases folder ' lack
			File file = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
			if (!file.exists()) {
				file.mkdir();
			}
			input = getAssets().open(DATABASE_NAME);
			OutputStream output = new FileOutputStream(getDatabasePath());
			byte[] buffer = new byte[1024];
			while ((input.read(buffer)) > 0) {
				output.write(buffer);
			}
			output.flush();
			output.close();
			input.close();
		} catch (Exception ex) {
			Log.e("ERROR", ex.toString());
		}

	}

	private String getDatabasePath() {
		return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
	}

	private void hienThiBaiHatYeuThich() {
		// Must run hienThiBaiHatGoc first. If not this method will be error
		/*arrBaiHatYeuThich.clear();
		for (BaiHat bh : arrBaiHat) {
			if (bh.isThich()) {
				arrBaiHatYeuThich.add(bh);
			}
		}
		baiHatYeuThichAdapter.notifyDataSetChanged();*/
		database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
		arrBaiHatYeuThich.clear();
		Cursor cursor = database.rawQuery("SELECT * FROM ArirangSongList WHERE YEUTHICH = ?", new String[]{""+1});
		while (cursor.moveToNext()) {
			BaiHat bh = new BaiHat();
			bh.setMaSo(Integer.parseInt(cursor.getString(0)));
			bh.setTenBaiHat(cursor.getString(1));
			bh.setCaSi(cursor.getString(3));
			if (cursor.getInt(5) == 0) {
				bh.setThich(false);
			} else {
				bh.setThich(true);
			}
			arrBaiHatYeuThich.add(bh);
		}
		cursor.close();
		baiHatYeuThichAdapter.notifyDataSetChanged();
	}

	private void hienThiBaiHatGoc() {
		database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
		arrBaiHat.clear();
		Cursor cursor = database.query("ArirangSongList", null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			BaiHat bh = new BaiHat();
			bh.setMaSo(Integer.parseInt(cursor.getString(0)));
			bh.setTenBaiHat(cursor.getString(1));
			bh.setCaSi(cursor.getString(3));
			if (cursor.getInt(5) == 0) {
				bh.setThich(false);
			} else {
				bh.setThich(true);
			}
			arrBaiHat.add(bh);
		}
		cursor.close();
		baiHatAdapter.notifyDataSetChanged();
	}
}
