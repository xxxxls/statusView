package com.xxxxls.status

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * 多状态构建
 * @author Max
 * @date 2019-12-16.
 */
object StatusFactory {
    /**
     * 构建多状态view
     */
    fun newStatusView(
        contentView: View,
        @LayoutRes loadingRes: Int = View.NO_ID,
        @LayoutRes emptyRes: Int = View.NO_ID,
        @LayoutRes errorRes: Int = View.NO_ID,
        @LayoutRes noNetworkRes: Int = View.NO_ID,
        onRetry: IStatusView.OnRetryClickListener? = null
    ): SuperStatusView? {
        (contentView.parent as? ViewGroup)?.apply {
            val layoutParams = contentView.layoutParams
            val index = this.indexOfChild(contentView)
            this.removeView(contentView)
            val statusView = SuperStatusView(context)
            this.addView(statusView, index, layoutParams)
            statusView.addStatus(Status.Content, contentView)
            statusView.addStatus(Status.Error, errorRes)
            statusView.addStatus(Status.NoNetwork, noNetworkRes)
            statusView.addStatus(Status.Empty, emptyRes)
            statusView.addStatus(Status.Loading, loadingRes)
            statusView.switchStatus(Status.Content)
            statusView.setOnRetryClickListener(onRetry)
            return statusView
        }
        return null
    }
}