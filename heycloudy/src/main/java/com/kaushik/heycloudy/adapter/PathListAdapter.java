package com.kaushik.heycloudy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaushik.heycloudy.R;
import com.kaushik.heycloudy.dao.PathDAO;
import com.kaushik.heycloudy.model.PathModel;

import java.util.ArrayList;
import java.util.List;

public class PathListAdapter extends BaseAdapter {

    private final Context mContext;
    private final PathDAO pathDao;
    List<PathModel> pathModelList = new ArrayList<PathModel>();

    public PathListAdapter(Context context, List<PathModel> data) {
        super();
        mContext = context;
        pathModelList.addAll(data);
        pathDao = new PathDAO(context);
    }

    @Override
    public int getCount() {
        return pathModelList.size();
    }

    @Override
    public PathModel getItem(int i) {
        return pathModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    public void addItem(PathModel pathModel) {
        pathModelList.add(pathModel);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_path, parent, false);
        }
        TextView pathFullText = (TextView) convertView.findViewById(R.id.textViewPath);
        ImageView pathDelete = (ImageView) convertView.findViewById(R.id.imageViewDeletePath);

        final PathModel item = getItem(position);

        String pathFull = item.getDirPath();
        String pathName = pathFull.substring(pathFull.lastIndexOf("/")+1);

        pathFullText.setText(pathName);

        Log.d("Adapter101",  pathName+ "");

        pathDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("Adapter", item.getId() + "");
                // PathDB Open
                pathDao.open();
                pathDao.deletePath(item.getId());
                pathDao.close();
                // Delete from pathList
                pathModelList.remove(item);

                notifyDataSetChanged();
            }
        });

        return convertView;
    }


}
