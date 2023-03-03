package com.xxxxls.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xxxxls.status.IStatusView
import com.xxxxls.status.Status
import com.xxxxls.status.SuperStatusView

/**
 * 多状态视图 XML
 * @author Max
 * @date 2019-12-16.
 */
class StatusXmlFragment : Fragment() {

    private lateinit var statusView: SuperStatusView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_status_xml, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    private fun initView() {
        statusView = requireView().findViewById<SuperStatusView>(R.id.statusView)
        // 添加自定义状态
        statusView.addStatus(CustomStatus, R.layout.layout_status_custom)
    }

    private fun initEvent() {
        statusView.setOnRetryClickListener(object : IStatusView.OnRetryClickListener {
            override fun onRetry(statusView: IStatusView, status: Status) {
                Log.e("STATUS", "状态重试：status -> $status")
                statusView.switchStatus(Status.Loading)
                retry()
            }
        })

        statusView.setOnStatusChangeListener(object : IStatusView.OnStatusChangeListener {
            override fun onChange(newStatus: Status, oldStatus: Status) {
                Log.e("STATUS", "状态改变：$newStatus -#- $oldStatus")
            }
        })

        requireView().findViewById<View>(R.id.tv_content).setOnClickListener {
            statusView.showContent()
        }

        requireView().findViewById<View>(R.id.tv_loading).setOnClickListener {
            statusView.showLoading()
        }

        requireView().findViewById<View>(R.id.tv_empty).setOnClickListener {
            statusView.showEmpty()
        }

        requireView().findViewById<View>(R.id.tv_error).setOnClickListener {
            statusView.showError()
        }

        requireView().findViewById<View>(R.id.tv_no_work).setOnClickListener {
            statusView.showNoNetwork()
        }

        requireView().findViewById<View>(R.id.tv_custom).setOnClickListener {
            statusView.switchStatus(CustomStatus)
        }
    }

    private fun retry() {
        statusView.showLoading()
        val status = when ((0..3).random()) {
            0 -> {
                Status.Empty
            }
            1 -> {
                Status.Error
            }
            2 -> {
                Status.NoNetwork
            }
            else -> {
                Status.Content
            }
        }
        view?.postDelayed({
            statusView.switchStatus(status)
        }, 3000)
    }
}