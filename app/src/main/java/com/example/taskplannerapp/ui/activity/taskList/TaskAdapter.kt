package com.example.taskplannerapp.ui.activity.taskList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskplannerapp.R
import com.example.taskplannerapp.data.entity.Task

class TaskAdapter(): RecyclerView.Adapter<TaskAdapter.ViewHolder>(){

    private var taskList: List<Task> = emptyList()

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val name: TextView
        val description: TextView
        val status: TextView
        val assignedTo: TextView
        val dueDate: TextView

        init {
            name = view.findViewById(R.id.name)
            description = view.findViewById(R.id.description)
            status = view.findViewById(R.id.status)
            assignedTo = view.findViewById(R.id.assignedTo)
            dueDate = view.findViewById(R.id.dueDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList.get(position)
        holder.name.text = task.name
        holder.description.text = task.description
        holder.status.text = task.status
        holder.assignedTo.text = task.assignedTo
        holder.dueDate.text = task.dueDate.toString()
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun updateTaskAdapter(taskList: List<Task>){
        this.taskList = taskList
        notifyDataSetChanged()
    }
}