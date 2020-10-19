package io.kikaitech.uvccamera;

import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.view.Surface;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.common.UVCCameraHandler;
import com.serenegiant.usb.widget.CameraViewInterface;
import com.serenegiant.usb.widget.UVCCameraTextureView;

public class UvcCameraViewManager extends ViewGroupManager<UvcCameraView> {
  public static final String REACT_CLASS = "UvcCamera";
  ReactApplicationContext reactContext;

  private UVCCameraHelper mCameraHelper;


  Bitmap bitmap = Bitmap.createBitmap(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera.DEFAULT_PREVIEW_HEIGHT, Bitmap.Config.RGB_565);
  private boolean isPreviewing;
  private boolean isRequestingPerm;

  public UvcCameraViewManager(ReactApplicationContext context) {
    reactContext = context;
  }

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  protected UvcCameraView createViewInstance(ThemedReactContext reactContext) {
    mCameraHelper = UVCCameraHelper.getInstance();
    mCameraHelper.setDefaultFrameFormat(UVCCameraHelper.FRAME_FORMAT_MJPEG);

    UvcCameraView cameraView = new UvcCameraView(reactContext);
    UVCCameraTextureView textureView = cameraView.getTextureView();
    textureView.setCallback(new CameraViewInterface.Callback() {
      @Override
      public void onSurfaceCreated(CameraViewInterface view, Surface surface) {
        alert("camera surface created!");
        if (!isPreviewing && mCameraHelper.isCameraOpened()) {
          mCameraHelper.startPreview(textureView);
          isPreviewing = true;
        }
      }

      @Override
      public void onSurfaceChanged(CameraViewInterface view, Surface surface, int width, int height) {
        alert("camera surface changed!");
      }

      @Override
      public void onSurfaceDestroy(CameraViewInterface view, Surface surface) {
        alert("camera surface destroyed!");
        if (isPreviewing && mCameraHelper.isCameraOpened()) {
          mCameraHelper.stopPreview();
          isPreviewing = false;
        }
      }
    });

    mCameraHelper.initUSBMonitor(reactContext.getCurrentActivity(), textureView, new UVCCameraHelper.OnMyDevConnectListener() {
      @Override
      public void onAttachDev(UsbDevice device) {
        if (!isRequestingPerm) {
          isRequestingPerm = true;
          mCameraHelper.requestPermission(0);
        }
      }

      @Override
      public void onDettachDev(UsbDevice device) {
        mCameraHelper.closeCamera();
        alert(device.getProductName() + " detached");
      }

      @Override
      public void onConnectDev(UsbDevice device, boolean isConnected) {
        alert(device.getProductName() + (isConnected ? " connected" : " failed to connect"));
        isPreviewing = isConnected;
      }

      @Override
      public void onDisConnectDev(UsbDevice device) {
        alert("disconnected");
      }
    });
    mCameraHelper.setOnPreviewFrameListener(new AbstractUVCCameraHandler.OnPreViewResultListener() {
      @Override
      public void onPreviewResult(byte[] nv21Yuv) {
        alert("new frame: " + nv21Yuv.length);
      }
    });
    mCameraHelper.registerUSB();

    return cameraView;
  }

  private void alert(String msg) {
    Toast.makeText(reactContext, msg, Toast.LENGTH_SHORT).show();
  }
}
