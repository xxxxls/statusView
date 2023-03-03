package com.xxxxls.status

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

/**
 *
 * @author Max
 * @date 2019-12-16.
 */
interface IStatusView {

    /**
     * 切换状态
     * @param status 状态
     */
    fun switchStatus(status: Status)

    /**
     * 获取状态
     */
    fun getStatus(): Status

    /**
     * 添加状态视图
     * @param status 状态类型
     * @param statusView 状态视图
     */
    fun addStatus(status: Status, statusView: View)

    /**
     * 添加状态视图
     * @param status 状态类型
     * @param statusLayoutId 状态视图布局ID
     */
    fun addStatus(status: Status, @LayoutRes statusLayoutId: Int)

    /**
     * 移除状态
     * @return 是否移除成功
     */
    fun removeStatus(status: Status): Boolean

    /**
     * 设置状态更新时间
     */
    fun setOnStatusChangeListener(listener: OnStatusChangeListener?)

    /**
     * 设置重试点击事件
     */
    fun setOnRetryClickListener(listener: OnRetryClickListener?)

    /**
     * 获取某状态对应的视图
     * @param status 状态
     */
    @Deprecated("命名不规范，改用findStatusView()")
    fun getViewByStatus(status: Status): View?

    /**
     * 获取状态视图
     * @param status 状态
     */
    fun findStatusView(status: Status): View?

    /**
     * 获取状态视图下的紫子View
     */
    fun <V : View> findStatusChildView(status: Status, @IdRes viewId: Int): V? {
        return findStatusView(status)?.findViewById<V>(viewId)
    }

    fun showContent() {
        switchStatus(Status.Content)
    }

    fun showError() {
        switchStatus(Status.Error)
    }

    fun showLoading() {
        switchStatus(Status.Loading)
    }

    fun showEmpty() {
        switchStatus(Status.Empty)
    }

    fun showNoNetwork() {
        switchStatus(Status.NoNetwork)
    }

    //状态改变状态
    interface OnStatusChangeListener {

        /**
         * 改变状态
         * @param newStatus 新状态
         * @param oldStatus 旧状态
         */
        fun onChange(newStatus: Status, oldStatus: Status)
    }

    //重试事件
    interface OnRetryClickListener {

        /**
         * 重试操作
         * @param statusView
         * @param status 触发时的状态
         */
        fun onRetry(statusView: IStatusView, status: Status)
    }
}