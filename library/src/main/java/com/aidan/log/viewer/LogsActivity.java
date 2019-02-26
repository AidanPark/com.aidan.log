package com.aidan.log.viewer;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidan.log.LogBean;
import com.aidan.log.R;

import java.util.List;

public class LogsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<LogBean>> {
    
    private String TAG = "LogsActivity";

    private LogAdapter adapter;

    private ListView lv;

    String search = "";
    private int level = 1;
    private boolean desc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "#### onCreate() ####");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "#### onResume() ####");
        adapter = new LogAdapter(getApplicationContext());

        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setFastScrollEnabled(true);

        getLoaderManager().initLoader(LogsLoader.ID, null, LogsActivity.this);

        EditText et_search = (EditText) findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String input = s.toString().trim();
                    Log.v(TAG, "--input------------------" + input);

                    //갤럭시 노트 등에서 이 메서드가 두번 호출된다. 한번만 수행하기 위해 입력데이타 체크.
                    if (search.equals(input)) {
                        return;
                    }
                    search = input;

                    Log.i(TAG, "+++ onSearchChanged +++" + "    search : [" + search + "] level : [" + level + "] desc : [" + desc + "]");
                    adapter.setSearch(search);
                    Bundle b = new Bundle();
                    b.putString("search", search);
                    b.putInt("level", level);
                    b.putBoolean("desc", desc);
                    getLoaderManager().restartLoader(LogsLoader.ID, b, LogsActivity.this);

                } catch (Exception e) {
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        findViewById(R.id.btn_level).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                level++;
                if (level > Log.ASSERT) {
                    level = 1;
                }

                Log.i(TAG, "+++ onLevelChanged +++ level : " + level);

                ((TextView) v).setText(getLevel(level));

                adapter.setLevel(level);
                Bundle b = new Bundle();
                b.putString("search", search);
                b.putInt("level", level);
                b.putBoolean("desc", desc);
                getLoaderManager().restartLoader(LogsLoader.ID, b, LogsActivity.this);
            }
        });

        findViewById(R.id.btn_time).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                desc = !desc;

                Log.i(TAG, "+++ onDescChanged +++ desc : " + desc);

                ((TextView) v).setText(desc ? "Time Desc" : "Time ASC");

                adapter.setDesc(desc);
                Bundle b = new Bundle();
                b.putString("search", search);
                b.putInt("level", level);
                b.putBoolean("desc", desc);
                getLoaderManager().restartLoader(LogsLoader.ID, b, LogsActivity.this);
            }
        });

        adapter.notifyDataSetChanged();
    }

    private String getLevel(int level) {
        if (level == Log.VERBOSE) {
            return "VERBOSE";
        }

        if (level == Log.DEBUG) {
            return "DEBUG";
        }

        if (level == Log.INFO) {
            return "INFO";
        }

        if (level == Log.WARN) {
            return "WARN";
        }

        if (level == Log.ERROR) {
            return "ERROR";
        }

        if (level == Log.ASSERT) {
            return "ASSERT";
        }

        return "ALL";
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogsLoader.release();
        finish();
    }

    /*****************************************************************/
    /** for implements LoaderManager.LoaderCallbacks<List<LogBean>> **/
    /*****************************************************************/

    @Override
    public Loader<List<LogBean>> onCreateLoader(int id, Bundle b) {
        Log.i(TAG, "+++ onCreateLoader() called! +++ id : " + id + ", b : " + b);
        return new LogsLoader(getApplicationContext(), b);
    }

    @Override
    public void onLoadFinished(Loader<List<LogBean>> loader, List<LogBean> data) {
        Log.i(TAG, "+++ onLoadFinished() called! +++" + data.size());
        adapter.setData(data);

        if (data == null || data.size() == 0) {
            findViewById(R.id.tv_info).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_title)).setText("AVD Logs " + 0);
        } else {
            findViewById(R.id.tv_info).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_title)).setText("AVD Logs " + data.size());
            Toast.makeText(getApplicationContext(), data.size() + " logs retrieved.", Toast.LENGTH_LONG).show();
        }

        if (desc) {
            lv.setSelection(0);
        } else {
            lv.setSelection(adapter.getCount() - 1);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<LogBean>> loader) {
        Log.i(TAG, "+++ onLoadReset() called! +++");
        adapter.setData(null);
    }

}









