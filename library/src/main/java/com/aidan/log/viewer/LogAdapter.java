package com.aidan.log.viewer;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aidan.log.LogBean;
import com.aidan.log.R;
import com.aidan.log.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015-12-10.
 */
public class LogAdapter extends BaseAdapter {

    @NonNull
    private final List<LogBean> lBeans;

    private Context context;

    public LogAdapter(@NonNull Context context) {
        this(context, null);
    }

    protected LogAdapter(Context context, @Nullable final List<LogBean> _lBeans) {
        this.context = context;

        if (_lBeans != null) {
            lBeans = _lBeans;
        } else {
            lBeans = new ArrayList<>();
        }
    }

    public void setData(@Nullable List<LogBean> _lBeans) {
        lBeans.clear();

        if(_lBeans != null) {
            lBeans.addAll(_lBeans);
        }

        notifyDataSetChanged();
    }

    public @NonNull List<LogBean> getData() {
        return lBeans;
    }

    @Override
    public int getCount() {
        return lBeans.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @NonNull
    public LogBean getItem(final int position) {
        return lBeans.get(position);
    }

    // ######################################################
    // for search
    // ######################################################

    private String search;
    private int level;
    private boolean desc;

    public void setSearch(String search) {
        this.search = search;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_log, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final LogBean lBean = getItem(position);

        int color = getTextColor(lBean.level);
        holder.getLevel().setTextColor(color);
        holder.getTime().setTextColor(color);
        holder.getTag().setTextColor(color);
        holder.getText().setTextColor(color);

        holder.getLevel().setText(getLevel(lBean.level));
        holder.getTime().setText(getTimeFormat(lBean.time));

        if (TextUtils.isEmpty(search)) {
            holder.getTag().setText(lBean.tag);
            holder.getText().setText(lBean.text);
        } else {
            try {
                String searchString = StringUtil.getMatchString(lBean.tag, search);
                int start = lBean.tag.indexOf(searchString);
                int end = start + searchString.length();
                String htmlString = "<font>"+lBean.tag.substring(0, start) + "<b>"+searchString +"</b>"+ lBean.tag.substring(end)+"</font>";
                if(TextUtils.isEmpty(htmlString)) {
                    holder.getTag().setText(lBean.tag);
                } else {
                    holder.getTag().setText(Html.fromHtml(htmlString));
                }
            } catch (Exception e) {
                holder.getTag().setText(lBean.tag);
            }

            try {
                String searchString = StringUtil.getMatchString(lBean.text, search);
                int start = lBean.text.indexOf(searchString);
                int end = start + searchString.length();
                String htmlString = "<font>"+lBean.text.substring(0, start) + "<b>"+searchString +"</b>"+ lBean.text.substring(end)+"</font>";
                if(TextUtils.isEmpty(htmlString)) {
                    holder.getText().setText(lBean.text);
                } else {
                    holder.getText().setText(Html.fromHtml(htmlString));
                }
            } catch (Exception e) {
                holder.getText().setText(lBean.text);
            }
        }

        return convertView;
    }

    private static class Holder {

        View view;
        TextView tv_level;
        TextView tv_time;
        TextView tv_tag;
        TextView tv_text;

        Holder(View view) {
            this.view = view;
        }

        TextView getLevel() {
            if (tv_level == null) {
                tv_level = (TextView) view.findViewById(R.id.tv_level);
            }
            return tv_level;
        }

        TextView getTime() {
            if (tv_time == null) {
                tv_time = (TextView) view.findViewById(R.id.tv_time);
            }
            return tv_time;
        }

        TextView getTag() {
            if (tv_tag == null) {
                tv_tag = (TextView) view.findViewById(R.id.tv_tag);
            }
            return tv_tag;
        }

        TextView getText() {
            if (tv_text == null) {
                tv_text = (TextView) view.findViewById(R.id.tv_text);
            }
            return tv_text;
        }

    }

    private String getLevel(int level) {
        if(level == Log.VERBOSE) {
            return "V";
        }

        if(level == Log.DEBUG) {
            return "D";
        }

        if(level == Log.INFO) {
            return "I";
        }

        if(level == Log.WARN) {
            return "W";
        }

        if(level == Log.ERROR) {
            return "E";
        }

        if(level == Log.ASSERT) {
            return "A";
        }

        return "?";
    }

    private int getTextColor(int level) {
        if(level == Log.VERBOSE) {
            return Color.parseColor("#000000");
        }

        if(level == Log.DEBUG) {
            return Color.parseColor("#00007f");
        }

        if(level == Log.INFO) {
            return Color.parseColor("#007f00");
        }

        if(level == Log.WARN) {
            return Color.parseColor("#ff7f00");
        }

        if(level == Log.ERROR) {
            return Color.parseColor("#ff0000");
        }

        if(level == Log.ASSERT) {
            return Color.parseColor("#000000");
        }

        return Color.parseColor("#000000");
    }

    private String getTimeFormat(long time) {
        return DateFormat.format("MM-dd\nHH:mm:ss.sss", new Date(time)).toString();
    }


}
