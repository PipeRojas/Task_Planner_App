package com.example.taskplannerapp.ui.activity

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.taskplannerapp.R
import com.example.taskplannerapp.data.AppDatabase
import com.example.taskplannerapp.data.dao.TaskDao
import com.example.taskplannerapp.data.entity.Task
import com.example.taskplannerapp.databinding.ActivityMainBinding
import com.example.taskplannerapp.service.AuthRequest
import com.example.taskplannerapp.service.AuthService
import com.example.taskplannerapp.service.TaskService
import com.example.taskplannerapp.storage.LocalStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var taskService: TaskService

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var localStorage: LocalStorage

    @Inject
    lateinit var taskDao: TaskDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //REST Service usage
        GlobalScope.launch(Dispatchers.IO) {
            //Authentication
            val token = authService.authenticate(AuthRequest("santiago@mail.com", "passw0rd")).body()?.accessToken
            if (token != null) {
                Log.d("Developer", token)
                localStorage.saveToken(token)
            }else{
                Log.d("Developer", "Token Nulo")
            }
            val response = taskService.getAllTasks()
            if(response.isSuccessful){
                val dtoTaskList = response.body()
                //Guardar en base de datos
                dtoTaskList?.map { taskDTO ->
                    Log.d("Developer", "It: $taskDTO")
                    taskDao.insert(Task(taskDTO))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}