package io.kikaitech.uvccamera;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.view.ReactViewGroup;
import com.serenegiant.usb.widget.UVCCameraTextureView;

public class UvcCameraView extends ReactViewGroup {
    private UVCCameraTextureView mTextureView;

    public UvcCameraView(ThemedReactContext context) {
        super(context);
        final View view = View.inflate(context, R.layout.texture_view, this);
        setBackgroundColor(Color.BLACK);
        mTextureView = (UVCCameraTextureView) view.findViewById(R.id.uvc_texture_view);
    }

    public UVCCameraTextureView getTextureView() {
        return mTextureView;
    }
}
