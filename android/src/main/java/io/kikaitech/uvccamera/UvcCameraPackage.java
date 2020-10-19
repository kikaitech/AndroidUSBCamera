package io.kikaitech.uvccamera;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Collections;
import java.util.List;

public class UvcCameraPackage implements ReactPackage {
    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext context) {
        return Collections.<ViewManager>singletonList(new UvcCameraViewManager(context));
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext context) {
        return Collections.emptyList();
    }
}
