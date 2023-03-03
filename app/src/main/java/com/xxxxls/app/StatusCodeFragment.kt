package com.xxxxls.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.xxxxls.status.IStatusView
import com.xxxxls.status.Status
import com.xxxxls.status.StatusFactory

/**
 * 多状态视图 CODE
 * @author Max
 * @date 2019-12-16.
 */
class StatusCodeFragment : Fragment() {

    private var statusView: IStatusView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_status_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    private fun initView() {
        //配置多状态
        statusView = StatusFactory.newStatusView(
            requireView().findViewById(R.id.layout_content),
            loadingRes = R.layout.base_status_loading,
            errorRes = R.layout.base_status_error,
            emptyRes = R.layout.base_status_empty,
            noNetworkRes = R.layout.base_status_no_network,
            onRetry = object : IStatusView.OnRetryClickListener {
                override fun onRetry(statusView: IStatusView, status: Status) {
                    Log.e("STATUS", "状态重试：status -> $status")
                    retry()
                }
            }
        )

        // 添加自定义状态
        statusView?.addStatus(CustomStatus, R.layout.layout_status_custom)

        //设置某个状态下的子view文本
        statusView?.findStatusChildView<TextView>(Status.NoNetwork, R.id.base_status_hint_content)
            ?.setText("没有网络吗？")

        //设置某个状态下的子view点击事件
        statusView?.findStatusChildView<View>(Status.Empty, R.id.base_status_hint_content)
            ?.setOnClickListener {
                Toast.makeText(requireContext(), "点我了", Toast.LENGTH_SHORT).show()
            }
    }

    private fun initEvent() {

        statusView?.setOnStatusChangeListener(object : IStatusView.OnStatusChangeListener {
            override fun onChange(newStatus: Status, oldStatus: Status) {
                Log.e("STATUS", "状态改变：$newStatus -#- $oldStatus")
            }
        })

        requireView().findViewById<View>(R.id.tv_content).setOnClickListener {
            statusView?.showContent()
        }

        requireView().findViewById<View>(R.id.tv_loading).setOnClickListener {
            statusView?.showLoading()
        }

        requireView().findViewById<View>(R.id.tv_empty).setOnClickListener {
            statusView?.showEmpty()
        }

        requireView().findViewById<View>(R.id.tv_error).setOnClickListener {
            statusView?.showError()
        }

        requireView().findViewById<View>(R.id.tv_no_work).setOnClickListener {
            statusView?.showNoNetwork()
        }

        requireView().findViewById<View>(R.id.tv_custom).setOnClickListener {
            statusView?.switchStatus(CustomStatus)
        }
    }

    private fun retry() {
        statusView?.showLoading()
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
            statusView?.switchStatus(status)
        }, 3000)
    }
}