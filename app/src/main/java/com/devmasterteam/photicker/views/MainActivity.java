package com.devmasterteam.photicker.views;


import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.devmasterteam.photicker.R;
import com.devmasterteam.photicker.utils.ImageUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final ViewHolder mViewHolder = new ViewHolder();

    // Imagem selecionada
    private ImageView mImageSelected;

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

        this.mViewHolder.mLinearControlPanel    = (LinearLayout) this.findViewById(R.id.linear_control_panel);
        this.mViewHolder.mLinearSharePanel      = (LinearLayout) this.findViewById(R.id.linear_share_panel);

        this.mViewHolder.mButtonZoonIn          = (ImageView) this.findViewById(R.id.image_zoom_in);
        this.mViewHolder.mButtonZoonOut         = (ImageView) this.findViewById(R.id.image_zoom_out);
        this.mViewHolder.mButtonRotateLeft      = (ImageView) this.findViewById(R.id.image_rotate_left);
        this.mViewHolder.mButtonRotateRight     = (ImageView) this.findViewById(R.id.image_rotate_right);
        this.mViewHolder.mButtonFinish          = (ImageView) this.findViewById(R.id.image_finish);
        this.mViewHolder.mButtonRemove          = (ImageView) this.findViewById(R.id.image_remove);

        this.setListener();


    }

    private void setListener() {

        this.findViewById(R.id.image_zoom_in).setOnClickListener(this);
        this.findViewById(R.id.image_zoom_out).setOnClickListener(this);
        this.findViewById(R.id.image_rotate_left).setOnClickListener(this);
        this.findViewById(R.id.image_rotate_right).setOnClickListener(this);
        this.findViewById(R.id.image_finish).setOnClickListener(this);
        this.findViewById(R.id.image_remove).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

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

            }
        };

    }

    private void toogleControlPanel(boolean showControls) {

        // caso seja true exibimos Segundo LinearLayout para o controle/manipulacao das fotos
        if (showControls) {

            this.mViewHolder.mLinearControlPanel.setVisibility(View.VISIBLE);
            this.mViewHolder.mLinearSharePanel.setVisibility(View.GONE);

        } else{

            this.mViewHolder.mLinearControlPanel.setVisibility(View.GONE);
            this.mViewHolder.mLinearSharePanel.setVisibility(View.VISIBLE);

        }
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


}
