package com.htu.erhuo.ui.callback;

/**
 * Class  Name: PermissionHandler
 * Description:
 * Created by Mansoul on 17/1/17
 */
public abstract class PermissionHandler {
    /**
     * 权限通过
     */
    public abstract void onGranted();

    /**
     * 权限拒绝
     */
    public void onDenied() {

    }
}
