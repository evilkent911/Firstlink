package com.trinerva.icrm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import asia.firstlink.icrm.R;

public class Agreement extends Activity {
	private Button accept, decline;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agreement);
		accept = (Button) findViewById(R.id.accept);
		decline = (Button) findViewById(R.id.decline);
		accept.setOnClickListener(AcceptAgreement);
		decline.setOnClickListener(DeclineAgreement);
	}

	private OnClickListener AcceptAgreement = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent login = new Intent(Agreement.this, Login.class);
			startActivity(login);
			// Agreement.this.finish();
			// registerForContextMenu(v);
			// openContextMenu(v);
			// unregisterForContextMenu(v);
		}
	};

	private OnClickListener DeclineAgreement = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Agreement.this.finish();
		}
	};

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.online:
			// Toast.makeText(Agreement.this, "Online",
			// Toast.LENGTH_SHORT).show();
			Intent login = new Intent(Agreement.this, Login.class);
			startActivity(login);
			Agreement.this.finish();
			break;
		case R.id.offline:
			Toast.makeText(Agreement.this, "Offline", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.cancel:
			Toast.makeText(Agreement.this, "Cancel", Toast.LENGTH_SHORT).show();
			this.finish();
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(R.string.mode_selection);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.login_mode, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		IntentFilter filter = new IntentFilter();
		filter.addAction("finish");
		registerReceiver(this.broadcastReceiver, filter);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			System.out.println("=== = " + intent.getAction());
			if (intent.getAction().equals("finish")) {
				finish();
			}
		}
	};
}