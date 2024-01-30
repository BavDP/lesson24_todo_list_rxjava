package com.example.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private val bottomNavigation by lazy {
        findViewById<NavigationBarView>(R.id.bottomNavigate)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showTodoListFragment()
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.todoList -> {
                    showTodoListFragment()
                    true
                }
                R.id.person -> {
                    showPersonalFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun showTodoListFragment() {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragmentContainerView, TabViewPagerFragment())
            .commit()
    }
    private fun showPersonalFragment() {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragmentContainerView, PersonalFragment())
            .commit()
    }
}