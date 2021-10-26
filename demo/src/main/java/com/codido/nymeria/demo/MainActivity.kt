package com.codido.nymeria.demo

import com.codido.nymeria.NymeriaBaseActivity
import com.codido.nymeria.demo.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

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
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    /**
     * 初始化事件方法
     */
    override fun initDatas() {
        super.initDatas()
    }

    override lateinit var _viewBinding: ActivityMainBinding
}