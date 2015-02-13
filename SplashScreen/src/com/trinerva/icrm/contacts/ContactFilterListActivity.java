package com.trinerva.icrm.contacts;

import asia.firstlink.icrm.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ContactFilterListActivity extends Activity {

	int nowSelete;
	TextView mTarget, mNewThisWeek, mModifiedThisWeek, mMyContact;
	int filterPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_filter);

		Intent intent = getIntent();
		nowSelete = intent.getIntExtra("nowSelete", 0);
		filterPage = intent.getIntExtra("filterPage", 0);
		mTarget = (TextView) findViewById(R.id.target);
		mNewThisWeek = (TextView) findViewById(R.id.newThisWeek);
		mModifiedThisWeek = (TextView) findViewById(R.id.modifiedThisWeek);
		mMyContact = (TextView) findViewById(R.id.myContact);

		if (filterPage == 0) {
			mMyContact.setText(getString(R.string.my_contact));

			switch (nowSelete) {
			case 0:
				mTarget.setText(getString(R.string.new_this_week));
				break;
			case 1:

				mTarget.setText(getString(R.string.modified_this_week));
				break;
			case 2:
				mTarget.setText(getString(R.string.my_contact));
				break;
			default:
				break;
			}

		} else if (filterPage == 1) {
			mMyContact.setText(getString(R.string.my_lead));

			switch (nowSelete) {
			case 0:
				mTarget.setText(getString(R.string.new_this_week));
				break;
			case 1:

				mTarget.setText(getString(R.string.modified_this_week));
				break;
			case 2:
				mTarget.setText(getString(R.string.my_lead));
				break;
			default:
				break;
			}
		} else {
			mNewThisWeek.setText(getString(R.string.today_task));
			mModifiedThisWeek.setText(getString(R.string.today_next_7_day));
			mMyContact.setText(getString(R.string.overdue));

			switch (nowSelete) {
			case 0:
				mTarget.setText(getString(R.string.today_task));
				break;
			case 1:

				mTarget.setText(getString(R.string.today_next_7_day));
				break;
			case 2:
				mTarget.setText(getString(R.string.overdue));
				break;
			default:
				break;
			}
		}

		findViewById(R.id.newThisWeek).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent2 = new Intent();
						intent2.putExtra("selete", 0);
						ContactFilterListActivity.this.setResult(RESULT_OK,
								intent2);

						finish();
					}
				});

		findViewById(R.id.modifiedThisWeek).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent2 = new Intent();
						intent2.putExtra("selete", 1);
						ContactFilterListActivity.this.setResult(RESULT_OK,
								intent2);
						finish();
					}
				});

		findViewById(R.id.myContact).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent();
				intent2.putExtra("selete", 2);
				ContactFilterListActivity.this.setResult(RESULT_OK, intent2);
				finish();
			}
		});
	}

}
