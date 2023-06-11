package com.example.todo

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.todo.databinding.FragmentNewTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class NewTaskFragment(var taskItem: TaskItem?) : BottomSheetDialogFragment(){
    private lateinit var binding: FragmentNewTaskBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueDateTime: LocalDateTime? = null
    private var dueTime: LocalTime? = null
    private var dueDate: LocalDate? = null
    private var selectedFile: Uri? = null
    private var selectedFilePath: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if (taskItem != null)
        {
            val editable = Editable.Factory.getInstance()
            binding.taskNameInput.text = editable.newEditable(taskItem!!.name)
            binding.taskDescriptionInput.text = editable.newEditable(taskItem!!.desc)
            binding.taskCategoryInput.text = editable.newEditable(taskItem!!.category)
            if(taskItem!!.dueDateTime() != null){
                dueDateTime = taskItem!!.dueDateTime()!!
            }
            if(taskItem!!.file != null){
                selectedFilePath = taskItem!!.file
            }
        }

        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)
        binding.saveTaskButton.setOnClickListener {
            saveAction()
        }
        binding.cancelTaskButton.setOnClickListener {
            dismiss()
        }
        binding.setTimeButton.setOnClickListener {
            openTimePicker()
            openDatePicker()

        }
        binding.addFile.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(createChooser(intent, "Select a file"), 111)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            var tempFile = data?.data
            selectedFile = copyFileToPrivateStorage(tempFile!!)
            selectedFilePath = if(selectedFile == null) null else selectedFile.toString()
        }
    }

    private fun copyFileToPrivateStorage(sourceUri: Uri): Uri? {
        return try {
            val inputStream: InputStream = context!!.contentResolver.openInputStream(sourceUri) ?: return null
            val mimeType: String? = context!!.contentResolver.getType(sourceUri)
            val extension: String = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: ""

            val fileName = "attachment_" + System.currentTimeMillis() + "." + extension
            val directory: File? = context!!.getExternalFilesDir(null)
            val file = File(directory, fileName)
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
            }
            inputStream.close()
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun saveAction() {
        if(dueDate != null && dueTime!=null){
            dueDateTime = LocalDateTime.of(dueDate!!.year, dueDate!!.month, dueDate!!.dayOfMonth,dueTime!!.hour, dueTime!!.minute)
        }
        val name = binding.taskNameInput.text.toString()
        val desc = binding.taskDescriptionInput.text.toString()
        val category = binding.taskCategoryInput.text.toString()
        val dueDateTimeString = if(dueDateTime == null) null else TaskItem.dateTimeFormatter.format(dueDateTime)
        println(dueDateTimeString)
        if(taskItem == null) {
            val newTask = TaskItem(name,desc,dueDateTimeString,null, category, selectedFilePath)
            taskViewModel.addTaskItem(newTask)
        }
        else {
            taskItem!!.name = name
            taskItem!!.desc = desc
            taskItem!!.category = category
            taskItem!!.dueDateTime = dueDateTimeString
            taskItem!!.file = selectedFilePath
            taskViewModel.updateTaskItem(taskItem!!)
        }
        binding.taskNameInput.setText("")
        binding.taskDescriptionInput.setText("")
        dismiss()
    }


    private fun openTimePicker() {
        if(dueTime == null)
            dueTime = LocalTime.now()
        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.show()
    }

    private fun openDatePicker() {
        if(dueDate == null)
            dueDate = LocalDate.now()
        val listener = DatePickerDialog.OnDateSetListener{ _, selectedYear, selectedMonth, selectedDay ->
            dueDate = LocalDate.of(selectedYear, selectedMonth, selectedDay)
        }
        val dialog = DatePickerDialog(requireActivity(), listener, dueDate!!.year, dueDate!!.monthValue,dueDate!!.dayOfMonth)
        dialog.show()

    }

}








