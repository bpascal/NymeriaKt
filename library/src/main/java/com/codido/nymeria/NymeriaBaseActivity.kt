package com.codido.nymeria

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.View.inflate
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.progressDialog


/**
 * Activity抽象基类
 */
abstract class NymeriaBaseActivity<T : ViewBinding> : AppCompatActivity() {

    /**
     * 打印日志tag的定义
     */
    val TAG = this::class.simpleName!!

    /**
     * 页面绑定视图的对象
     */
    abstract var _viewBinding: T

    /**
     * 长时间toast显示信号常量
     */
    val MESSAGE_WHAT_SHOW_LONGTOAST = 101;

    /**
     * 短时间toast显示信号常量
     */
    val MESSAGE_WHAT_SHOW_SHORTTOAST = 102;

    /**
     * 显示加载条
     */
    val MESSAGE_WHAT_SHOW_PROGRESSBAR = 103;

    /**
     * 隐藏加载条
     */
    val MESSAGE_WHAT_HIDE_PROGRESSBAR = 104;

    /**
     * 显示加载框
     */
    val MESSAGE_WHAT_SHOW_PROGRESSDIALOG = 105;

    /**
     * 隐藏加载框
     */
    val MESSAGE_WHAT_HIDE_PROGRESSDIALOG = 106;

    /**
     * toast显示的字符串的常量
     */
    val MESSAGE_KEY_TOAST_STR = "MESSAGE_KEY_TOAST_STR";

    /**
     * 加载框显示的字符串常量
     */
    val MESSAGE_KEY_PROGRESS_STR = "MESSAGE_KEY_PROGRESS_STR";

    /**
     * 加载框显示的标题字符串常量
     */
    val MESSAGE_KEY_PROGRESS_TITLE = "MESSAGE_KEY_PROGRESS_STR"

    /**
     * 加载框是否可以手工取消
     */
    val MESSAGE_KEY_PROGRESS_CANCELABLE = "MESSAGE_KEY_PROGRESS_CANCELABLE"

    /**
     * 加载框是否点周围可以取消
     */
    val MESSAGE_KEY_PROGRESS_CANCELABLEOUTSIDE = "MESSAGE_KEY_PROGRESS_CANCELABLEOUTSIDE"

    /**
     * 两次点击返回按钮时间在2秒之内，则退出APP
     */
    private val EXIT_TIME_INTERVAL = 2000

    /**
     * 记录上次点击返回的时间点
     */
    private var mBackPressed: Long = 0

    /**
     * 双击退出的提示语
     */
    abstract var doubleBackExitTips: String

    /**
     * 双击返回按钮是否退出的抽象定义，true表示双击返回按钮后会退出，false表示不会
     */
    abstract var isDoubleBackExit: Boolean;

    /**
     * 加载进度条
     */
    lateinit var progressBar: ProgressBar

    /**
     * 加载进度框
     */
    lateinit var progressDialog: Dialog

    /**
     * 通用处理handler
     */
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MESSAGE_WHAT_SHOW_LONGTOAST -> {
                    //显示长时间toast
                    val msgObj = msg.data
                    var showStr = msgObj.getString(MESSAGE_KEY_TOAST_STR)
                    Toast.makeText(this@NymeriaBaseActivity, showStr.toString(), Toast.LENGTH_LONG)
                        .show();
                }
                MESSAGE_WHAT_SHOW_SHORTTOAST -> {
                    //显示短时间toast
                    val msgObj = msg.data
                    var showStr = msgObj.getString(MESSAGE_KEY_TOAST_STR)
                    Toast.makeText(
                        this@NymeriaBaseActivity,
                        showStr.toString(),
                        Toast.LENGTH_SHORT
                    ).show();
                }
                MESSAGE_WHAT_SHOW_PROGRESSBAR -> {
                    //TODO bpascal 实现 显示加载条
                    val msgObj = msg.data
                    var showStr = msgObj.getString(MESSAGE_KEY_PROGRESS_STR)
                    var showTitle = msgObj.getString(MESSAGE_KEY_PROGRESS_TITLE)
                    var cancelAble = msgObj.getBoolean(MESSAGE_KEY_PROGRESS_CANCELABLE)
                    var cancelOutSide = msgObj.getBoolean(MESSAGE_KEY_PROGRESS_CANCELABLEOUTSIDE)
                }
                MESSAGE_WHAT_HIDE_PROGRESSBAR -> {
                    //TODO bpascal 实现 隐藏加载条
                }
                MESSAGE_WHAT_SHOW_PROGRESSDIALOG -> {
                    //显示加载对话框
                    val msgObj = msg.data
                    var showStr = msgObj.getString(MESSAGE_KEY_PROGRESS_STR)
                    var showTitle = msgObj.getString(MESSAGE_KEY_PROGRESS_TITLE)
                    var cancelAble = msgObj.getBoolean(MESSAGE_KEY_PROGRESS_CANCELABLE)
                    var cancelOutSide = msgObj.getBoolean(MESSAGE_KEY_PROGRESS_CANCELABLEOUTSIDE)
                    progressDialog = indeterminateProgressDialog(showStr, showTitle)
                    progressDialog.setCancelable(cancelAble)
                    progressDialog.setCanceledOnTouchOutside(cancelOutSide)
                    progressDialog.show()
                }
                MESSAGE_WHAT_HIDE_PROGRESSDIALOG -> {
                    //隐藏加载对话框
                    progressDialog.hide()
                }
                else -> {
                    val mBundle = msg.data
                    mBundle?.toString()?.let { Log.e("接收到的数据:", it) }
                }
            }
        }
    }


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
        progressBar = ProgressBar(this, null, 0, R.style.Widget_AppCompat_ProgressBar_Horizontal)
        progressBar.max = 100;
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

    /**
     * 打印长时间toast方法
     */
    open fun showLongToast(toastTxt: String) {
        if (isMainThread()) {
            //如果当前是在主线程，则直接打印
            Toast.makeText(this, toastTxt, Toast.LENGTH_LONG).show();
        } else {
            //当前不在主线程，
            val message: Message = Message();
            message.what = MESSAGE_WHAT_SHOW_LONGTOAST;
            message.data.putString(MESSAGE_KEY_TOAST_STR, toastTxt);
            mHandler.sendMessage(message)
        }
    }

    /**
     * 打印长时间toast方法
     */
    fun showShotToast(toastTxt: String) {
        if (isMainThread()) {
            //如果当前是在主线程，则直接打印
            Toast.makeText(this, toastTxt, Toast.LENGTH_SHORT).show();
        } else {
            //当前不在主线程，
            val message: Message = Message();
            message.what = MESSAGE_WHAT_SHOW_SHORTTOAST;
            message.data.putString(MESSAGE_KEY_TOAST_STR, toastTxt);
            mHandler.sendMessage(message)
        }
    }

    /**
     * 显示加载条
     */
    @Deprecated(message = "方法暂未实现")
    fun showProgressBar(
        showMessageText: String,
        showMessageTitle: String,
        cancelAble: Boolean,
        cancelOutSide: Boolean
    ) {
        //TODO bpascal 方法实现
        if (isMainThread()) {
            progressBar.visibility = View.VISIBLE
        } else {
            //当前不在主线程，
            val message = Message();
            message.what = MESSAGE_WHAT_SHOW_PROGRESSBAR;
            message.data.putString(MESSAGE_KEY_PROGRESS_STR, showMessageText)
            message.data.putString(MESSAGE_KEY_PROGRESS_TITLE, showMessageTitle)
            message.data.putBoolean(MESSAGE_KEY_PROGRESS_STR, cancelAble)
            message.data.putBoolean(MESSAGE_KEY_PROGRESS_TITLE, cancelOutSide)
            mHandler.sendMessage(message)
        }

    }

    /**
     * 隐藏加载条
     */
    @Deprecated(message = "方法暂未实现")
    fun hideProgressBar() {
        if (isMainThread()) {
            progressBar.visibility = View.GONE
        } else {
            //当前不在主线程，
            val message = Message();
            message.what = MESSAGE_WHAT_HIDE_PROGRESSBAR;
            mHandler.sendMessage(message)
        }
    }

    /**
     * 显示默认的加载对话框
     */
    fun showProgressDialog(
        showMessageText: String,
        showMessageTitle: String,
        cancelAble: Boolean,
        cancelOutSide: Boolean
    ) {
        if (isMainThread()) {
            progressDialog = indeterminateProgressDialog(showMessageText, showMessageTitle)
            progressDialog.setCancelable(cancelAble)
            progressDialog.setCanceledOnTouchOutside(cancelOutSide)
            progressDialog.show()
        } else {
            //当前不在主线程，
            val message = Message();
            message.what = MESSAGE_WHAT_SHOW_PROGRESSDIALOG;
            message.data.putString(MESSAGE_KEY_PROGRESS_STR, showMessageText)
            message.data.putString(MESSAGE_KEY_PROGRESS_TITLE, showMessageTitle)
            message.data.putBoolean(MESSAGE_KEY_PROGRESS_STR, cancelAble)
            message.data.putBoolean(MESSAGE_KEY_PROGRESS_TITLE, cancelOutSide)
            mHandler.sendMessage(message)
        }
    }

    /**
     * 隐藏默认的加载对话框
     */
    fun hideProgressDialog() {
        if (isMainThread()) {
            progressDialog.hide()
        } else {
            //当前不在主线程，
            val message = Message();
            message.what = MESSAGE_WHAT_HIDE_PROGRESSDIALOG;
            mHandler.sendMessage(message)
        }
    }

    /**
     * 判断是不是主线程
     */
    fun isMainThread(): Boolean {
        return Looper.getMainLooper() == Looper.myLooper()
    }

//    open fun isMainThread(): Boolean {
//        return Looper.getMainLooper().thread === Thread.currentThread()
//    }
//
//    open fun isMainThread(): Boolean {
//        return Looper.getMainLooper().thread.id == Thread.currentThread().id
//    }

    override fun onBackPressed() {
        if (isDoubleBackExit) {
            //如果这个页面需要双击返回按钮后退出，则进入这段逻辑
            if (mBackPressed + EXIT_TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed()
                return
            } else {
                showShotToast(doubleBackExitTips)
            }
            mBackPressed = System.currentTimeMillis()
        } else {
            super.onBackPressed()
            return
        }
    }


}