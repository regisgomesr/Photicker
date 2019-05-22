package com.devmasterteam.photicker.views;


import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.devmasterteam.photicker.R;
import com.devmasterteam.photicker.utils.ImageUtil;
import com.devmasterteam.photicker.utils.LongEventType;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    private final ViewHolder mViewHolder = new ViewHolder();

    // Imagem selecionada
    private ImageView mImageSelected;
    private boolean mAutoIncrement;
    private LongEventType mLongEventType;
    private Handler mRepeatUpdateHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        List<Integer> mListImages = ImageUtil.getImageList();

        final LinearLayout content = (LinearLayout) this.findViewById(R.id.linear_horizontal_scroll_content);

        this.mViewHolder.mRelativeLPhotoContent = (RelativeLayout) this.findViewById(R.id.relative_photo_content_dram);

        for (Integer imageId : mListImages) {

            ImageView image = new ImageView(this);
            image.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(getResources(), imageId, 70, 70));
            image.setPadding(20, 10, 20, 10);

            BitmapFactory.Options dimensions = new BitmapFactory.Options();
            dimensions.inJustDecodeBounds = false;
            BitmapFactory.decodeResource(getResources(), imageId, dimensions);

            final int width = dimensions.outWidth;
            final int height = dimensions.outHeight;

            image.setOnClickListener(onClickImageOption(this.mViewHolder.mRelativeLPhotoContent, imageId, width, height));

            content.addView(image);
        }

        this.mViewHolder.mLinearControlPanel = (LinearLayout) this.findViewById(R.id.linear_control_panel);
        this.mViewHolder.mLinearSharePanel = (LinearLayout) this.findViewById(R.id.linear_share_panel);

        this.mViewHolder.mButtonZoonIn = (ImageView) this.findViewById(R.id.image_zoom_in);
        this.mViewHolder.mButtonZoonOut = (ImageView) this.findViewById(R.id.image_zoom_out);
        this.mViewHolder.mButtonRotateLeft = (ImageView) this.findViewById(R.id.image_rotate_left);
        this.mViewHolder.mButtonRotateRight = (ImageView) this.findViewById(R.id.image_rotate_right);
        this.mViewHolder.mButtonFinish = (ImageView) this.findViewById(R.id.image_finish);
        this.mViewHolder.mButtonRemove = (ImageView) this.findViewById(R.id.image_remove);

        this.setListener();


    }

    private void setListener() {

        this.findViewById(R.id.image_zoom_in).setOnClickListener(this);
        this.findViewById(R.id.image_zoom_out).setOnClickListener(this);
        this.findViewById(R.id.image_rotate_left).setOnClickListener(this);
        this.findViewById(R.id.image_rotate_right).setOnClickListener(this);
        this.findViewById(R.id.image_finish).setOnClickListener(this);
        this.findViewById(R.id.image_remove).setOnClickListener(this);


        this.findViewById(R.id.image_zoom_in).setOnLongClickListener(this);
        this.findViewById(R.id.image_zoom_out).setOnLongClickListener(this);
        this.findViewById(R.id.image_rotate_left).setOnLongClickListener(this);
        this.findViewById(R.id.image_rotate_right).setOnLongClickListener(this);

        this.findViewById(R.id.image_zoom_in).setOnTouchListener(this);
        this.findViewById(R.id.image_zoom_out).setOnTouchListener(this);
        this.findViewById(R.id.image_rotate_left).setOnTouchListener(this);
        this.findViewById(R.id.image_rotate_right).setOnTouchListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.image_zoom_in:
                ImageUtil.handleZoonIn(this.mImageSelected);
                break;
            case R.id.image_zoom_out:
                ImageUtil.handleZoonOut(this.mImageSelected);
                break;
            case R.id.image_rotate_left:
                ImageUtil.handleRotateLeft(this.mImageSelected);
                break;
            case R.id.image_rotate_right:
                ImageUtil.handleRotateRight(this.mImageSelected);
                break;
            case R.id.image_finish:
                toogleControlPanel(false);
                break;
            case R.id.image_remove:
                this.mViewHolder.mRelativeLPhotoContent.removeView(this.mImageSelected);
                break;
        }

    }


    private View.OnClickListener onClickImageOption(final RelativeLayout relativeLayoutPhoto, final Integer imageId, int width, int height) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ImageView image = new ImageView(MainActivity.this);
                image.setBackgroundResource(imageId);
                relativeLayoutPhoto.addView(image);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) image.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

                mImageSelected = image;

                toogleControlPanel(true);

                image.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        float x, y;
                        switch (motionEvent.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                mImageSelected = image;
                                toogleControlPanel(true);
                                break;
                            case MotionEvent.ACTION_MOVE:
                                int coords[] = {0, 0};
                                relativeLayoutPhoto.getLocationOnScreen(coords);

                                x = (motionEvent.getRawX() - (image.getWidth() / 2));
                                y = motionEvent.getRawY() - ((coords[1] + 100) + (image.getHeight() / 2));
                                image.setX(x);
                                image.setY(y);
                                break;
                            case MotionEvent.ACTION_UP:
                                break;
                        }
                        return true;
                    }
                });


            }
        };

    }

    private void toogleControlPanel(boolean showControls) {

        // caso seja true exibimos Segundo LinearLayout para o controle/manipulacao das fotos
        if (showControls) {

            this.mViewHolder.mLinearControlPanel.setVisibility(View.VISIBLE);
            this.mViewHolder.mLinearSharePanel.setVisibility(View.GONE);

        } else {

            this.mViewHolder.mLinearControlPanel.setVisibility(View.GONE);
            this.mViewHolder.mLinearSharePanel.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public boolean onLongClick(View view) {

        if (view.getId() == R.id.image_zoom_in)
            this.mLongEventType = LongEventType.ZoomIn;

        if (view.getId() == R.id.image_zoom_out)
            this.mLongEventType = LongEventType.ZoomOut;

        if (view.getId() == R.id.image_rotate_left)
            this.mLongEventType = LongEventType.RotateLeft;

        if (view.getId() == R.id.image_rotate_right)
            this.mLongEventType = LongEventType.RotateRight;

        mAutoIncrement = true;

        new RptUpdater().run();

        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int id = view.getId();

        if (id == R.id.image_zoom_in || id == R.id.image_zoom_out || id == R.id.image_rotate_right || id == R.id.image_rotate_left) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP && mAutoIncrement) {

                mAutoIncrement = false;
                this.mLongEventType = null;
            }
        }

        return false;
    }


    private static class ViewHolder {

        LinearLayout mLinearSharePanel;
        LinearLayout mLinearControlPanel;

        ImageView mButtonZoonIn;
        ImageView mButtonZoonOut;
        ImageView mButtonRotateLeft;
        ImageView mButtonRotateRight;
        ImageView mButtonFinish;
        ImageView mButtonRemove;

        RelativeLayout mRelativeLPhotoContent;

    }

    // Thread
    private class RptUpdater implements Runnable {


        @Override
        public void run() {

            if (mAutoIncrement)
                mRepeatUpdateHandler.postDelayed(new RptUpdater(), 50);

            if (mLongEventType != null) {

                switch (mLongEventType) {
                    case ZoomIn:
                        ImageUtil.handleZoonIn(mImageSelected);
                        break;
                    case ZoomOut:
                        ImageUtil.handleZoonOut(mImageSelected);
                        break;
                    case RotateLeft:
                        ImageUtil.handleRotateLeft(mImageSelected);
                        break;
                    case RotateRight:
                        ImageUtil.handleRotateRight(mImageSelected);
                        break;
                }
            }
        }
    }
}
