package vn.com.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import vn.com.model.BaiHat;
import vn.com.mykaraoke.MainActivity;
import vn.com.mykaraoke.R;

public class BaiHatAdapter extends ArrayAdapter {
	Activity context;
	int resource;
	List objects;
	public static SQLiteDatabase database = null;
	public static String DATABASE_NAME = "Arirang.sqlite";
	String DB_PATH_SUFFIX = "/databases/";

	public BaiHatAdapter(Context context, int resource, List objects) {
		super(context, resource, objects);
		this.context = (Activity) context;
		this.resource = resource;
		this.objects = objects;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//xuLySaoChepDuLieu();
		LayoutInflater inflater = this.context.getLayoutInflater();
		View row = inflater.inflate(this.resource, null);
		final ImageButton btnThich = (ImageButton) row.findViewById(R.id.btnThich);
		final ImageButton btnKhongThich = (ImageButton) row.findViewById(R.id.btnKhongThich);
		TextView txtTenBaiHat = (TextView) row.findViewById(R.id.txtTenBaiHat);
		TextView txtCaSi = (TextView) row.findViewById(R.id.txtCaSi);
		TextView txtMaSo = (TextView) row.findViewById(R.id.txtMaSo);
		final BaiHat bh = (BaiHat) objects.get(position);
		txtTenBaiHat.setText(bh.getTenBaiHat().toString());
		txtCaSi.setText(bh.getCaSi().toString());
		txtMaSo.setText(bh.getMaSo() + "");
		if (bh.isThich()) {
			btnThich.setVisibility(View.INVISIBLE);
			btnKhongThich.setVisibility(View.VISIBLE);
		} else {
			btnKhongThich.setVisibility(View.INVISIBLE);
			btnThich.setVisibility(View.VISIBLE);
		}
		btnThich.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// bh.setThich(false);
				btnThich.setVisibility(View.INVISIBLE);
				btnKhongThich.setVisibility(View.VISIBLE);
				// ((BaiHat) objects.get(position)).setThich(true);
				xuLyThich();
			}

			private void xuLyThich() {
				//xuLySaoChepDuLieu();
				SQLiteDatabase db = context.openOrCreateDatabase(MainActivity.DATABASE_NAME, context.MODE_PRIVATE, null);
				
				//db = SQLiteDatabase.openOrCreateDatabase(MainActivity.DATABASE_NAME, null);
				ContentValues row = new ContentValues();
				row.put("YEUTHICH", 1);
				/*Cursor cursor = MainActivity.database.query(
						"ArirangSongList", null, "MABH=?", new String[]{bh.getMaSo()+""},null, null, null);
				*/
				/*Cursor cursor = MainActivity.database.query("ArirangSongList",null,null,null,null,null,null);
				while(cursor.moveToNext()){
					Toast.makeText(context, cursor.getInt(4)+"", Toast.LENGTH_SHORT).show();
				}
				cursor.close();*/
			//	long soDong = MainActivity.database.update("ArirangSongList", row, "MABH = ?",new String[]{bh.getMaSo()+""});
				long soDong = db.update("ArirangSongList", row, "MABH = ?",new String[]{bh.getMaSo()+""});
				
				if (soDong > 0) {
					Toast.makeText(context, "Update successfull", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnKhongThich.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// bh.setThich(true);
				btnKhongThich.setVisibility(View.INVISIBLE);
				btnThich.setVisibility(View.VISIBLE);
				// ((BaiHat) objects.get(position)).setThich(false);
				xuLyKhongThich();
			}
			private void xuLyKhongThich() {
				// database =
				// SQLiteDatabase.openOrCreateDatabase(MainActivity.DATABASE_NAME,
				// null);
				//xuLySaoChepDuLieu();
				SQLiteDatabase db = context.openOrCreateDatabase(MainActivity.DATABASE_NAME, context.MODE_PRIVATE, null);
				ContentValues row = new ContentValues();
				row.put("YEUTHICH", 0);
			//	long soDong = MainActivity.database.update("ArirangSongList", row, "MABH = ?",new String[]{bh.getMaSo()+""});
				long soDong = db.update("ArirangSongList", row, "MABH = ?",new String[]{bh.getMaSo()+""});
				if (soDong > 0) {
					Toast.makeText(context, "Update successfull", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return row;
	}

	private void xuLySaoChepDuLieu() {
		try {
			File file = context.getDatabasePath(DATABASE_NAME);
			if (!file.exists()) {
				saoChepDuLieu();
				Toast.makeText(context, "Sao chep thanh cong", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// Log.e("Error copy data", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	private void saoChepDuLieu() {
		InputStream input;
		try {
			// Becarefull for databases folder ' lack
			File file = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
			if (!file.exists()) {
				file.mkdir();
			}
			input = context.getAssets().open(DATABASE_NAME);
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
		return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
	}
}
