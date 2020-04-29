package com.app.erl.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.erl.R;
import com.app.erl.adapter.AlbumPhotosAdapter;
import com.app.erl.adapter.AlbumsAdapter;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.ActivityAlbumsBinding;
import com.app.erl.model.entity.info.AlbumInfo;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class AlbumsActivity extends BaseActivity
        implements SelectItemListener, View.OnClickListener {
    private ActivityAlbumsBinding binding;
    private Context mContext;
    private AlbumsAdapter adapterAlbums;
    private AlbumPhotosAdapter adapterAlbumPhotos;
    private final int REQUEST_PERMISSION_KEY = 1;
    private List<AlbumInfo> listAlbums, listAlbumPhotos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_albums);
        binding.imgBack.setOnClickListener(this);
        checkPermission();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    private void setAlbumsAdapter(List<AlbumInfo> list) {
        if (list != null && list.size() > 0) {
            binding.rvAlbums.setVisibility(View.VISIBLE);
            binding.rvAlbumPhotos.setVisibility(View.GONE);
            binding.rvAlbums.setLayoutManager(new GridLayoutManager(this, 2));
            adapterAlbums = new AlbumsAdapter(mContext, list);
            adapterAlbums.setListener(this);
            binding.rvAlbums.setAdapter(adapterAlbums);
        }
    }

    private void setAlbumPhotosAdapter(List<AlbumInfo> list) {
        if (list != null && list.size() > 0) {
            binding.rvAlbumPhotos.setVisibility(View.VISIBLE);
            binding.rvAlbums.setVisibility(View.GONE);
            binding.rvAlbumPhotos.setLayoutManager(new GridLayoutManager(this, 3));
            adapterAlbumPhotos = new AlbumPhotosAdapter(mContext, list);
            adapterAlbumPhotos.setListener(this);
            binding.rvAlbumPhotos.setAdapter(adapterAlbumPhotos);
        }
    }

    private class LoadAlbums extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listAlbums = new ArrayList<>();
        }

        protected String doInBackground(String... args) {
            String xml = "";
            String[] PROJECTION_BUCKET = {
                    MediaStore.Images.ImageColumns.BUCKET_ID,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED};
            String BUCKET_GROUP_BY =
                    "1) GROUP BY 1,(2";
            String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = getContentResolver().query(
                    images, PROJECTION_BUCKET, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);

            while (cursor.moveToNext()) {
                AlbumInfo info = new AlbumInfo();
                info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
                info.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
                info.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)));
                info.setCountPhoto(AppUtils.getAlbumCount(getApplicationContext(), info.getAlbum()));

                if (info.getCountPhoto() > 0)
                    listAlbums.add(info);
            }
            cursor.close();
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            setAlbumsAdapter(listAlbums);
        }
    }

    private class LoadAlbumImages extends AsyncTask<String, Void, String> {
        private String albumName;

        public LoadAlbumImages(String albumName) {
            this.albumName = albumName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listAlbumPhotos = new ArrayList<>();
        }

        protected String doInBackground(String... args) {
            String xml = "";
            Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED};
            String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " " + "DESC";
            Cursor cursorExternal = getContentResolver().query(uriExternal, projection, "bucket_display_name = \"" + albumName + "\"", null, sortOrder);
            Cursor cursorInternal = getContentResolver().query(uriInternal, projection, "bucket_display_name = \"" + albumName + "\"", null, sortOrder);
            Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal, cursorInternal});

            while (cursor.moveToNext()) {
                AlbumInfo info = new AlbumInfo();
                info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
                info.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
                info.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)));

                listAlbumPhotos.add(info);
            }
            cursor.close();
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            if (listAlbumPhotos != null && listAlbumPhotos.size() > 0) {
                binding.imgBack.setImageResource(R.drawable.ic_close);
                binding.txtTitle.setText(albumName);
                binding.txtImageCount.setVisibility(View.VISIBLE);
                binding.txtImageCount.setText(listAlbumPhotos.size()+" Images");
                setAlbumPhotosAdapter(listAlbumPhotos);
            }

        }
    }

    public void checkPermission() {
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!AppUtils.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        } else {
            new LoadAlbums().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_KEY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new LoadAlbums().execute();
                } else {
                    Toast.makeText(mContext, "You must accept permissions.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    @Override
    public void onSelectItem(int position, int action) {
        if (action == AppConstant.Action.SELECT_ALBUM) {
            new LoadAlbumImages(listAlbums.get(position).getAlbum()).execute();
        } else {
//            if (listSelectedImages.size() < totalImage) {
//                binding.rvSelectedImages.setVisibility(View.VISIBLE);
//                listSelectedImages.add(listAlbumPhotos.get(position));
//                adapterSelectedImages.notifyDataSetChanged();
//            } else {
//                Toast.makeText(mContext, String.format(getString(R.string.msg_max_image_error)
//                        , totalImage), Toast.LENGTH_SHORT).show();
//            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        mMenu = menu;
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.done_menu, menu);
//        menu.findItem(R.id.action_done).setVisible(false);
//        AppUtils.setToolbarTextColor(menu.findItem(R.id.action_done), getResources().getString(R.string.done), getResources().getColor(R.color.colorAccent));
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_done:
//
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        if (binding.rvAlbumPhotos.getVisibility() == View.VISIBLE) {
            binding.rvAlbumPhotos.setVisibility(View.GONE);
            binding.rvAlbums.setVisibility(View.VISIBLE);
            binding.txtTitle.setText(getString(R.string.albums));
            binding.txtImageCount.setVisibility(View.GONE);
            binding.imgBack.setImageResource(R.drawable.ic_arrow_back);
        } else {
            finish();
        }
    }
}
