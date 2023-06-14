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
    private var tableSize = false
    private var bundle : Bundle = Bundle()
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
        binding.reloadButton!!.setOnClickListener {
            taskViewModel.reloadItems()
            setRecyclerView()
        }
        binding.sortButton.setOnClickListener {
            if(taskViewModel.sortType == "created"){
                taskViewModel.sortType = "time due"
                taskViewModel.reloadItems()
                Toast.makeText(this, "sorted by due date", Toast.LENGTH_SHORT).show()
                binding.sortButton.setImageResource(R.drawable.baseline_sort_24)
            }
            else if(taskViewModel.sortType == "time due"){
                taskViewModel.sortType = "created"
                taskViewModel.reloadItems()
                Toast.makeText(this, "sorted by creation date", Toast.LENGTH_SHORT).show()
                binding.sortButton.setImageResource(R.drawable.baseline_filter_list_24)
            }
            setRecyclerView()
        }
        binding.hideCompletedButton.setOnClickListener {
            if(taskViewModel.hideCompleted == false){
                taskViewModel.hideCompleted = true
                binding.hideCompletedButton.setImageResource(R.drawable.baseline_unchecked_24)
                taskViewModel.reloadItems()
                Toast.makeText(this, "hide completed", Toast.LENGTH_SHORT).show()
            }
            else{
                taskViewModel.hideCompleted = false
                binding.hideCompletedButton.setImageResource(R.drawable.baseline_hide_completed_24)
                taskViewModel.reloadItems()
                Toast.makeText(this, "show completed", Toast.LENGTH_SHORT).show()
            }
            setRecyclerView()
        }
        binding.hideCategoriesButton.setOnClickListener {
            CategoryInputFragment(taskViewModel).show(supportFragmentManager, "categoryTag")
            setRecyclerView()
        }
        binding.setTimeOffsetButton.setOnClickListener {
            SetTimeOffsetFragment().show(supportFragmentManager, "offsetTag")
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText!=null && newText != ""){
                    taskViewModel.searchDatabase(newText)
                }
                else{
                    taskViewModel.reloadItems()
                }
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
        if(!sharedPref.contains("notifTimeOffset")){
            editor.putInt("notifTimeOffset",0)
        }
        editor.apply()
    }

    override fun editTaskItem(taskItem: TaskItem) {
        NewTaskFragment(taskItem).show(supportFragmentManager,"newTaskTag")
    }

    override fun completeTaskItem(taskItem: TaskItem) {
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

    override fun onResume() {
        super.onResume()
        setRecyclerView()
    }
}







