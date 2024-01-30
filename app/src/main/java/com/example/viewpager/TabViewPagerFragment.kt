package com.example.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.viewpager.adapters.TabAdapter
import com.example.viewpager.models.todoList.TodoTaskState
import com.example.viewpager.mvvm.todoList.TodoListViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class TabViewPagerFragment : Fragment() {
    private val _viewModel  by activityViewModels<TodoListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewPager()
    }

    private fun createViewPager() {
        val adapter = TabAdapter(requireActivity())
        val viewPager = requireView().findViewById<ViewPager2>(R.id.todoListPager)

        viewPager.adapter = adapter
        val tabs = requireView().findViewById<TabLayout>(R.id.tabs)

        tabs.addOnTabSelectedListener(object:OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val tagState = it.tag as TodoTaskState
                    _viewModel.activeTodoState = tagState
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        TabLayoutMediator(tabs, viewPager
        ) { tab, position ->
            tab.text = TodoTaskState.values()[position+1].toString()
            tab.tag = TodoTaskState.values()[position+1]
        }.attach()
    }
}