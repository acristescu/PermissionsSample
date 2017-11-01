package io.zenandroid.permissionssample;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(button -> onRequestButtonClicked());
    }

    private void onRequestButtonClicked() {
        // instead of showCamera()
        MainActivityPermissionsDispatcher.showCameraWithPermissionCheck(this);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        //
        // If we reached this point, we have the permissions. We'll just open and close the camera
        // object to illustrate the point.
        //
        try {
            Camera.open().release();
        } catch (Exception ex) {
            Toast.makeText(this, "Cannot open camera: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Camera opened", Toast.LENGTH_SHORT).show();
    }

    // This is optional!
    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        Toast.makeText(this, "User denied the permission request", Toast.LENGTH_SHORT).show();
    }

    // This is optional!
    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("Use this (optional) dialog to explain to the user why you need the permission")
                .setPositiveButton("OK", (dialog, button) -> request.proceed())
                .setNegativeButton("Cancel", (dialog, button) -> request.cancel())
                .show();
    }

    // This is optional!
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        Toast.makeText(this, "User clicked 'don't ask again'", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
