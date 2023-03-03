package com.xxxxls.status

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes

/**
 * 多状态视图
 * @author Max
 * @date 2019-12-14.
 */
open class SuperStatusView : RelativeLayout, IStatusView, View.OnClickListener {

    //当前类型
    protected var statusType: Status = Status.Default

    //状态view集合
    protected val statusViews: HashMap<Class<*>, View> by lazy {
        HashMap<Class<*>, View>()
    }

    //状态-id集合
    protected val statusLayoutIds: HashMap<Class<*>, Int> by lazy {
        HashMap<Class<*>, Int>()
    }

    //布局构造器
    protected val inflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    //状态改变事件
    protected var statusChangeListener: IStatusView.OnStatusChangeListener? = null

    //重试事件
    protected var retryClickListener: IStatusView.OnRetryClickListener? = null

    //默认布局参数
    private val defaultLayoutParams: LayoutParams by lazy {
        LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val array =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.SuperStatusView,
                defStyleAttr,
                defStyleAttr
            )

        statusLayoutIds[Status.Content::class.java] =
            array.getResourceId(
                R.styleable.SuperStatusView_status_content_layout,
                View.NO_ID
            )

        statusLayoutIds[Status.Empty::class.java] =
            array.getResourceId(
                R.styleable.SuperStatusView_status_empty_layout,
                View.NO_ID
            )

        statusLayoutIds[Status.Error::class.java] =
            array.getResourceId(
                R.styleable.SuperStatusView_status_error_layout,
                View.NO_ID
            )

        statusLayoutIds[Status.Loading::class.java] =
            array.getResourceId(
                R.styleable.SuperStatusView_status_loading_layout,
                View.NO_ID
            )

        statusLayoutIds[Status.NoNetwork::class.java] =
            array.getResourceId(
                R.styleable.SuperStatusView_status_no_network_layout,
                View.NO_ID
            )

        array.recycle()
    }

    /**
     * 设置更新状态
     */
    override fun switchStatus(status: Status) {
        if (this.statusType == status) {
            return
        }

        if (status == Status.Content) {
            switchContentView()
            changeStatus(status)
            return
        }

        checkGenerateView(status)?.let {
            switchView(it)
            changeStatus(status)
        } ?: let {
            //该状态无对应视图
        }
    }

    /**
     * 获取当前类型
     */
    override fun getStatus(): Status {
        return statusType
    }

    /**
     * 添加状态视图
     * @param status 状态类型
     * @param statusView 状态视图
     */
    override fun addStatus(status: Status, statusView: View) {
        statusViews[status.javaClass] = statusView
        statusLayoutIds[status.javaClass] = statusView.id
    }

    /**
     * 添加状态视图
     * @param status 状态类型
     * @param statusLayoutId 状态视图布局ID
     */
    override fun addStatus(status: Status, @LayoutRes statusLayoutId: Int) {
        statusLayoutIds[status.javaClass] = statusLayoutId
    }

    /**
     * 移除状态
     */
    override fun removeStatus(status: Status): Boolean {
        if (getStatus().equals(status) || status.equals(Status.Default)) {
            // 无法移除当前状态或默认状态
            return false
        }
        statusViews.remove(status.javaClass)
        statusLayoutIds.remove(status.javaClass)
        return true
    }

    /**
     * 改变状态
     */
    private fun changeStatus(status: Status) {
        if (statusType != Status.Default) {
            statusChangeListener?.onChange(status, statusType)
        }
        this.statusType = status
    }

    /**
     * 检查构造view
     * @param status
     */
    private fun checkGenerateView(status: Status): View? {
        statusViews[status.javaClass]?.let { view ->
            return view
        } ?: let {
            statusLayoutIds[status.javaClass]?.let { layoutId ->
                if (layoutId <= 0) {
                    return null
                }
                val statusView = inflater.inflate(layoutId, null)
                statusViews[status.javaClass] = statusView
                return statusView
            } ?: let {
                return null
            }
        }
    }

    /**
     * 检查添加view
     */
    private fun checkAddView(view: View) {
        if (indexOfChild(view) < 0) {
            view.findViewById<View>(R.id.status_retry_view)?.setOnClickListener(this)
            addView(view, -1, defaultLayoutParams)
            view.visibility = View.GONE
        }
    }

    /**
     * 展示指定View 隐藏其他
     * @param view id
     */
    private fun switchView(view: View) {
        checkAddView(view)
        for (index in 0 until childCount) {
            getChildAt(index).apply {
                visibility = if (this == view) View.VISIBLE else View.GONE
            }
        }
    }

    /**
     * 展示内容View
     */
    private fun switchContentView() {
        checkGenerateView(Status.Content)?.let {
            switchView(it)
        } ?: let {
            for (index in 0 until childCount) {
                getChildAt(index).apply {
                    visibility = if (statusViews.containsValue(this)) View.GONE else View.VISIBLE
                }
            }
        }
    }

    /**
     * 获取某状态视图
     */
    override fun getViewByStatus(status: Status): View? {
        return findStatusView(status)
    }

    /**
     * 获取某状态视图
     */
    override fun findStatusView(status: Status): View? {
        checkGenerateView(status)?.let {
            checkAddView(it)
            return it
        }
        return null
    }

    override fun onClick(v: View?) {
        //重试点击事件
        if (this.statusType != Status.Default) {
            retryClickListener?.onRetry(this, this.statusType)
        }
    }

    override fun setOnStatusChangeListener(listener: IStatusView.OnStatusChangeListener?) {
        this.statusChangeListener = listener
    }

    override fun setOnRetryClickListener(listener: IStatusView.OnRetryClickListener?) {
        this.retryClickListener = listener
    }
}