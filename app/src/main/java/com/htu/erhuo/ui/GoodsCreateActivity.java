package com.htu.erhuo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.htu.erhuo.R;
import com.htu.erhuo.application.EHApplication;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.ItemInfo;
import com.htu.erhuo.entity.enums.ItemSortEnum;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.ui.adapter.GoodsCreatePictureAdapter;
import com.htu.erhuo.utils.DialogUtil;
import com.htu.erhuo.utils.FileUtils;
import com.htu.erhuo.utils.ImageUtils;
import com.htu.erhuo.utils.Utile;
import com.htu.erhuo.utils.WebPUtil;
import com.htu.erhuo.utils.crop.CropUtil;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class GoodsCreateActivity extends BaseActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private final static int REQUEST_SELECT_PHOTO = 0;

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.activity_goods_create)
    LinearLayout activityGoodsCreate;
    @BindView(R.id.et_goods_description)
    EditText etGoodsDescription;
    @BindView(R.id.gv_goods_picture)
    GridView gvGoodsPicture;
    @BindView(R.id.btn_goods_create)
    Button btnGoodsCreate;
    @BindView(R.id.et_goods_title)
    EditText etGoodsTitle;
    @BindView(R.id.et_goods_price)
    EditText etGoodsPrice;
    @BindView(R.id.tv_choose_sort)
    TextView tvChooseSort;

    private String account;
    private GoodsCreatePictureAdapter goodsCreatePictureAdapter;
    private List<String> picList;
    ItemInfo itemInfo = new ItemInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_create);
        ButterKnife.bind(this);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle(R.string.str_goods_create);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();

            }
        });
        account = getIntent().getStringExtra("account");

        picList = new ArrayList<>();
        picList.add("add");

        goodsCreatePictureAdapter = new GoodsCreatePictureAdapter(this);
        goodsCreatePictureAdapter.setData(picList);
        gvGoodsPicture.setAdapter(goodsCreatePictureAdapter);
        goodsCreatePictureAdapter.notifyDataSetChanged();
        gvGoodsPicture.setOnItemClickListener(this);
        gvGoodsPicture.setOnItemLongClickListener(this);

        setResult(1);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        DialogUtil.showTips(mContext,
                "返回",
                "确定放弃发布吗?",
                "确定",
                "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((BaseActivity) mContext).finish();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    @OnClick(R.id.btn_goods_create)
    void clickGoodsCreate() {
        if (!isCheckInput()) return;
        itemInfo.setItemTitle(etGoodsDescription.getText().toString());
        itemInfo.setCreator(account);
        BigDecimal bigDecimal = new BigDecimal(etGoodsPrice.getText().toString());
        itemInfo.setPrice(bigDecimal);
        itemInfo.setSortId(ItemSortEnum.idOf(tvChooseSort.getText().toString()));
        StringBuilder photoListString = new StringBuilder();
        photoListString.append(EHApplication.getInstance().getUserInfo().getPortrait()).append(",");
        for (String s : picList) {
            if (!s.equals("add"))
                photoListString.append(s).append(",");
        }
        itemInfo.setPhotoList(photoListString.toString());

        Subscriber<EntityResponse> subscriber = new Subscriber<EntityResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(GoodsCreateActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(EntityResponse entityResponse) {
                if (entityResponse.getCode().equals("0")) {
                    Toast.makeText(GoodsCreateActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    setResult(0);
                    finish();
                } else {
                    Toast.makeText(GoodsCreateActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Network.getInstance().createGoods(itemInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @OnClick(R.id.tv_choose_sort)
    void clickChooseSort() {
        final String[] sortIds = new String[]{ItemSortEnum.BOOK.getName(),
                ItemSortEnum.COMPUTER.getName(),
                ItemSortEnum.DIGITAL.getName(),
                ItemSortEnum.FOOD.getName(),
                ItemSortEnum.CLOTHES.getName(),
                ItemSortEnum.SHOES.getName(),
                ItemSortEnum.ELECTRIC.getName(),
                ItemSortEnum.SPORT.getName(),
                ItemSortEnum.BAG.getName(),
                ItemSortEnum.COSMETIC.getName(),
                ItemSortEnum.OTHER.getName()};
        new AlertDialog.Builder(this).setTitle("设置分类").
                setSingleChoiceItems(sortIds, 10, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvChooseSort.setText(sortIds[which]);
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (((parent.getAdapter().getItem(position))).equals("add")) {
            selectPhoto();
        } else {
            // TODO: 2017/3/30 预览 删除图片
        }
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_PHOTO) {
            if (data != null && data.getData() != null) {
                Uri selectedImage = data.getData();
                String avatarPath = selectedImage.toString();
                if (selectedImage.toString().contains("content")) {
                    avatarPath = Utile.getRealPathFromURI(this, selectedImage);
                }
                Log.d("yzw", "upload file path " + avatarPath);
                uploadAvatar(cropImg(avatarPath));
            }
        }
    }

    private String cropImg(String imagePath) {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            Bitmap mImgBmp;
            mImgBmp = CropUtil.rotaingImageView(0,
                    ImageUtils.decodeBitmapFromSDCard(imagePath, 1080, 1920));
            String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1,
                    imagePath.lastIndexOf(".")) + (imagePath.endsWith(".gif") ? ".gif" : ".webp");
            File saveFile = new File(FileUtils.IMG_SAVE_PATH + fileName);

            if (fileName.endsWith(".gif")) {
                if (!FileUtils.getInstance().isExists(FileUtils.IMG_SAVE_PATH + fileName)) {
                    FileUtils.getInstance().copyFile(imagePath, FileUtils.IMG_SAVE_PATH + fileName);
                }
            } else {
                if (!imagePath.endsWith(".webp")) {
                    WebPUtil.with(mContext).imageToWebp(mImgBmp, saveFile);
                } else if (!FileUtils.getInstance().isExists(FileUtils.IMG_SAVE_PATH + fileName)) {
                    ImageUtils.savePhotoToSDCard(mImgBmp, FileUtils.IMG_SAVE_PATH, fileName);
                }
            }
            if (mImgBmp != null && !mImgBmp.isRecycled()) mImgBmp.recycle();
            return FileUtils.IMG_SAVE_PATH + fileName;
        }
        return "";
    }

    private void uploadAvatar(String avatarPath) {
        showLoadingDialog(getString(R.string.uploading));
        final String fileName = FileUtils.getUploadAvatarNameFromPath(avatarPath);
        PutObjectRequest put = new PutObjectRequest(FileUtils.ERHUO_BUCKET, fileName, avatarPath);
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                long percent = currentSize * 100 / totalSize;
                showLoadingText(getString(R.string.uploading) + " " + percent + "%");
            }
        });
        EHApplication.getInstance().getOss().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                hideLoadingDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        picList.add(picList.size() - 1, fileName);
                        goodsCreatePictureAdapter.notifyDataSetChanged();
                        goodsCreatePictureAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingDialog();
                        Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public boolean isCheckInput() {
        if (TextUtils.isEmpty(etGoodsTitle.getText().toString())) {
            Toast.makeText(mContext, "请输入标题", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etGoodsPrice.getText().toString())) {
            Toast.makeText(mContext, "请输入价格", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
