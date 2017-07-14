package com.kaushik.heycloudy.fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.kaushik.heycloudy.R;
import com.kaushik.heycloudy.adapter.PathListAdapter;
import com.kaushik.heycloudy.dao.PathDAO;
import com.kaushik.heycloudy.model.PathModel;
import com.kaushik.heycloudy.util.PermissionUtils;

import java.util.List;

import ir.sohreco.androidfilechooser.ExternalStorageNotAvailableException;
import ir.sohreco.androidfilechooser.FileChooserDialog;

public class HomeFragment extends Fragment implements FileChooserDialog.ChooserListener {
    private TextView noPathTextView;
    private ListView pathListView;
    private Context mContext;
    private PathDAO pathDAO;
    private List<PathModel> pathList;
    private PathListAdapter pathAdapter;
    private FileChooserDialog.Builder fileChooser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_home_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getActivity().setTitle("Home");
        noPathTextView = view.findViewById(R.id.textViewNoPath);
        //ListView Setup
        pathListView = view.findViewById(R.id.listViewPaths);

        //Db instantiate and open Connection
        pathDAO = new PathDAO(mContext);
        //initialize paths from db
        refreshPathList();

        if (pathList.size() < 1) {
            noPathTextView.setVisibility(View.VISIBLE);
            pathListView.setVisibility(View.GONE);
        } else {
            noPathTextView.setVisibility(View.GONE);
            pathListView.setVisibility(View.VISIBLE);
        }

        pathAdapter = new PathListAdapter(mContext, pathList);

        //Setup the Directory Chooser Dialog
        FileChooserDialog n = new FileChooserDialog();
        n.setStyle(R.style.Theme_AppCompat_Light_NoActionBar, R.style.AppTheme);
        fileChooser = new FileChooserDialog.Builder(FileChooserDialog.ChooserType.DIRECTORY_CHOOSER, this);
        fileChooser.setSelectDirectoryButtonText("Select");
        fileChooser.setDirectoryIcon(R.drawable.ic_folder_white_36dp);

        PermissionUtils.doPermissionCheck(mContext);

        pathListView.setAdapter(pathAdapter);

        pathAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                refreshPathList();

                if (pathList.size() > 0) {
                    noPathTextView.setVisibility(View.GONE);
                    if (pathListView.getVisibility() != View.VISIBLE) {
                        pathListView.setVisibility(View.VISIBLE);
                    }
                } else {
                    noPathTextView.setVisibility(View.VISIBLE);
                    pathListView.setVisibility(View.GONE);
                }
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.btnAddPath);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PermissionUtils.doPermissionCheck(mContext) == 1) {
                    try {
                        FileChooserDialog m = fileChooser.build();
                        m.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
                        m.show(getFragmentManager(), null);
                    } catch (ExternalStorageNotAvailableException e) {
                        e.printStackTrace();
                        Snackbar.make(view, "Error, something messed up. Try Again", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(view, "We need permission to sdCard to proceed.", Snackbar.LENGTH_LONG)
                            .setAction("Grant", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PermissionUtils.doPermissionCheck(mContext);
                                }
                            }).show();
                }

            }
        });
    }

    private void refreshPathList() {
        pathDAO.open();
        pathList = pathDAO.getAllPaths();
        pathDAO.close();
    }

    @Override
    public void onSelect(String path) {
        try {
            pathDAO.open();
            PathModel newPath = pathDAO.addNewPath(path);
            pathAdapter.addItem(newPath);
            pathDAO.close();
            refreshPathList();
        } catch (Exception e) {
            Snackbar.make(pathListView, "Folder is already added.", Snackbar.LENGTH_LONG).show();
        }

        noPathTextView.setVisibility(View.GONE);
        if (pathListView.getVisibility() != View.VISIBLE) {
            pathListView.setVisibility(View.VISIBLE);
        }
    }


}
