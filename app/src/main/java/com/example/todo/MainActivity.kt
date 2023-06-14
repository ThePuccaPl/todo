package com.example.todo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R.string.preference_file_key
import com.example.todo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), TaskItemClickListener
{
    private lateinit var binding: ActivityMainBinding
    var tableSize = false

    private val taskViewModel: TaskViewModel by viewModels {
        TaskItemModelFactory((application as TodoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        createNotificationChannel()
        setContentView(binding.root)
        setUpPreferences()
        val sharedPref = getSharedPreferences(getString(preference_file_key), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        tableSize = resources.getBoolean(R.bool.isTablet)
        binding.searchView.clearFocus()
        binding.newTaskButton.setOnClickListener {
            NewTaskFragment(null).show(supportFragmentManager, "newTaskTag")
        }
        binding.sortButton.setOnClickListener {
            val sort = sharedPref.getString("sortMode","")
            if(sort == "created"){
                editor.putString("sortMode","time due")
                editor.apply()
                taskViewModel.reloadItems()
                Toast.makeText(this, "sorted by due date", Toast.LENGTH_SHORT).show()
            }
            else if(sort == "time due"){
                editor.putString("sortMode","created")
                editor.apply()
                taskViewModel.reloadItems()
                Toast.makeText(this, "sorted by creation date", Toast.LENGTH_SHORT).show()
            }
            setRecyclerView()

        }
        binding.hideCompletedButton.setOnClickListener {
            val hideCompleted = sharedPref.getString("hideCompleted","")
            if(hideCompleted == "false"){
                editor.putString("hideCompleted","true")
                editor.apply()
                taskViewModel.reloadItems()
                Toast.makeText(this, "hide completed", Toast.LENGTH_SHORT).show()
            }
            else{
                editor.putString("hideCompleted","false")
                editor.apply()
                taskViewModel.reloadItems()
                Toast.makeText(this, "show completed", Toast.LENGTH_SHORT).show()
            }
            setRecyclerView()
        }
        binding.hideCategoriesButton.setOnClickListener {
            Toast.makeText(this, "not implemented", Toast.LENGTH_SHORT).show()
            //val categories = sharedPref.getString("categories","")
            //Toast.makeText(this, categories, Toast.LENGTH_SHORT).show()
        }
        binding.setTimeOffsetButton.setOnClickListener {
            SetTimeOffsetFragment().show(supportFragmentManager, "offsetTag")
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                editor.putString("search",newText)
                editor.apply()
                setRecyclerView()
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

        setRecyclerView()
    }

    private fun createNotificationChannel() {
        val name = "Alarmclock Channel"
        val description = " Reminder Alarm manager"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel("111", name, importance)
        notificationChannel.description = description
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun setRecyclerView() {
        val mainActivity = this
        taskViewModel.taskItems.observe(this){
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it, mainActivity, context)
            }
        }
    }


    private fun setUpPreferences(){
        val sharedPref = getSharedPreferences(getString(preference_file_key), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        if(!sharedPref.contains("hideCompleted")){
            editor.putString("hideCompleted","false")
        }
        if(!sharedPref.contains("categories")){
            editor.putString("categories","")
        }
        if(!sharedPref.contains("notifTimeOffset")){
            editor.putInt("notifTimeOffset",0)
        }
        if(!sharedPref.contains("sortMode")){
            editor.putString("sortMode","created")
        }
        if(!sharedPref.contains("search")){
            editor.putString("search","")
        }
        editor.apply()
    }

    override fun editTaskItem(taskItem: TaskItem)
    {
        NewTaskFragment(taskItem).show(supportFragmentManager,"newTaskTag")
    }

    override fun completeTaskItem(taskItem: TaskItem)
    {
        taskViewModel.setCompleted(taskItem)
    }

    override fun deleteTaskItem(taskItem: TaskItem) {
        taskViewModel.deleteTaskItem(taskItem)
    }

    override fun showDetails(taskItem: TaskItem) {
        if(tableSize && this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            val fragment = DetailsFragment(taskItem)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.detailsFragment,fragment)
                commit()
            }
        }
        else{
            DetailsTaskFragment(taskItem).show(supportFragmentManager,"detailsTag")
        }
    }

}







