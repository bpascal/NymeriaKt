package com.codido.nymeria

import android.os.Bundle
import android.view.*
import android.view.View.inflate
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType


/**
 * Activity抽象基类
 */
abstract class NymeriaBaseActivity<T: ViewBinding> : AppCompatActivity() {

    /**
     * 页面绑定视图的对象
     */
    abstract var _viewBinding : T


    /**
     * 构造方法
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //初始化页面元素
        initViews()
        //初始化数据
        initDatas()
        //初始化事件
        initEvents()
    }


    /**
     * 初始化页面元素方法
     */
    open fun initViews() {
        val type = javaClass.genericSuperclass as ParameterizedType
        val cls = type.actualTypeArguments[0] as Class<*>
        try {
            val inflate = cls.getDeclaredMethod("inflate", LayoutInflater::class.java)
            _viewBinding = inflate.invoke(null, layoutInflater) as T
            setContentView(_viewBinding.root)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    /**
     * 初始化数据方法
     */
    open fun initEvents() {
    }

    /**
     * 初始化事件方法
     */
    open fun initDatas() {

    }


    /**
     * 点击空白位置 隐藏软键盘
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (null != this.currentFocus) {
            val mInputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.onTouchEvent(event)
    }
}