package com.example.taskplannerapp.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskplannerapp.data.dao.TaskDao
import com.example.taskplannerapp.data.entity.Task
import com.example.taskplannerapp.service.AuthRequest
import com.example.taskplannerapp.service.AuthService
import com.example.taskplannerapp.service.TaskService
import com.example.taskplannerapp.storage.LocalStorage
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val taskService: TaskService,
    private val authService: AuthService,
    private val localStorage: LocalStorage,
    private val taskDao: TaskDao
) : ViewModel() {

    //Tasks List LiveData definition
    private val _tasks = MutableLiveData<MutableList<Task>>()
    val tasks: LiveData<MutableList<Task>>
    get() = _tasks

    fun authenticate(){
        GlobalScope.launch(Dispatchers.IO) {
            //Authentication
            val token = authService.authenticate(AuthRequest("santiago@mail.com", "passw0rd")).body()?.accessToken
            if (token != null) {
                Log.d("Developer", token)
                localStorage.saveToken(token)
            }else{
                Log.d("Developer", "Token Nulo")
            }
        }
    }

    fun getTasks(){
        //REST Service usage
        GlobalScope.launch(Dispatchers.IO) {
            val response = taskService.getAllTasks()
            if(response.isSuccessful){
                val dtoTaskList = response.body()
                //Guardar en base de datos
                dtoTaskList?.map { taskDTO ->
                    Log.d("Developer", "It: $taskDTO")
                    val task = Task(taskDTO)
                    taskDao.insert(task)
                    _tasks.value?.add(task)
                }
            }else{
                Log.d("Developer", "Error obtaining tasks")
            }
        }
    }


}