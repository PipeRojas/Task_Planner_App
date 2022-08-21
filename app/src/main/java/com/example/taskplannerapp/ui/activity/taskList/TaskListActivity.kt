package com.example.taskplannerapp.ui.activity.taskList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskplannerapp.R
import com.example.taskplannerapp.ui.activity.taskList.TaskListFragment

class TaskListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TaskListFragment.newInstance())
                .commitNow()
        }
    }
}