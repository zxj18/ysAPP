package com.vodbyte.movie.utils;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class PermissionsUtil {
    /**
     * 权限申请
     */
    public static String[] PERMISSION = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.REQUEST_INSTALL_PACKAGES
    };
    public static String[] PERMISSION_ALL = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission_group.LOCATION,
            Manifest.permission_group.PHONE,
            Manifest.permission_group.MICROPHONE,
            Manifest.permission_group.CAMERA};

    public static String[] PERMISSION_CAMERA = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission_group.CAMERA,
            Manifest.permission_group.MICROPHONE};

    @SuppressLint("CheckResult") //多个申请暂时有问题
    public static void requestAll(FragmentActivity context, Consumer<Permission> accept) {
        RxPermissions permissions = new RxPermissions(context);
        permissions.setLogging(true);
        permissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission_group.CAMERA,
                Manifest.permission_group.MICROPHONE,
                Manifest.permission_group.LOCATION,
                Manifest.permission_group.PHONE,
                Manifest.permission.REQUEST_INSTALL_PACKAGES)
                .subscribe(accept);
    }

    @SuppressLint("CheckResult") //多个申请有问题
    public static void requestCamera(FragmentActivity context, Consumer<Boolean> accept) {
        RxPermissions permissions = new RxPermissions(context);
        permissions.setLogging(true);
        permissions.request(//Manifest.permission.REQUEST_INSTALL_PACKAGES,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(accept);
    }

    @SuppressLint("CheckResult") //多个申请有问题
    public static void requestStorage(FragmentActivity context, Consumer<Boolean> accept) {
        RxPermissions permissions = new RxPermissions(context);
        permissions.setLogging(true);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(accept);
    }

    @SuppressLint("CheckResult")
    public static void requestLocation(FragmentActivity context, Consumer<Boolean> accept) {
        RxPermissions permissions = new RxPermissions(context);
        permissions.setLogging(true);
        permissions.request(Manifest.permission_group.LOCATION)
                .subscribe(accept);
    }

    @SuppressLint("CheckResult")
    public static void requestPhone(FragmentActivity context, Consumer<Boolean> accept) {
        RxPermissions permissions = new RxPermissions(context);
        permissions.setLogging(true);
        permissions.request(Manifest.permission_group.PHONE)
                .subscribe(accept);
    }

    @SuppressLint("CheckResult")
    public static void requestPackages(FragmentActivity context, Consumer<Boolean> accept) {
        RxPermissions permissions = new RxPermissions(context);
        permissions.setLogging(true);
        permissions.request(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                .subscribe(accept);
    }
}
