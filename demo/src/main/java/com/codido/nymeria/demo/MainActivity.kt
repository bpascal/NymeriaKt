package com.codido.nymeria.demo

import android.Manifest
import android.widget.Toast
import com.codido.nymeria.NymeriaBaseActivity
import com.codido.nymeria.demo.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.permissionx.guolindev.PermissionX

/**
 * 主入口
 */
class MainActivity : NymeriaBaseActivity<ActivityMainBinding>() {

    /**
     * 初始化视图
     */
    override fun initViews() {
        super.initViews()
        setSupportActionBar(_viewBinding.toolbar)
    }

    /**
     * 初始化数据方法
     */
    override fun initEvents() {
        super.initEvents()
        _viewBinding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            requestPermissionX();
        }
    }

    /**
     * 初始化事件方法
     */
    override fun initDatas() {
        super.initDatas()
    }

    /**
     * 申请权限方法
     */
    fun requestPermissionX() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE
            )
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        this,
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    override lateinit var _viewBinding: ActivityMainBinding
}