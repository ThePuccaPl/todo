@file:Suppress("DEPRECATION")

package com.example.todo

import android.app.Activity.RESULT_OK
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
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
import java.util.Calendar
import java.util.concurrent.TimeUnit


class NewTaskFragment(var taskItem: TaskItem?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNewTaskBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueDateTime: LocalDateTime? = null
    private var dueTime: LocalTime? = null
    private var dueDate: LocalDate? = null
    private var selectedFile: Uri? = null
    private var selectedFilePath: String? = null
    private var name: String? = null
    private var desc: String? = null
    private var category: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if (taskItem != null) {
            val editable = Editable.Factory.getInstance()
            binding.taskNameInput.text = editable.newEditable(taskItem!!.name)
            binding.taskDescriptionInput.text = editable.newEditable(taskItem!!.desc)
            binding.taskCategoryInput.text = editable.newEditable(taskItem!!.category)
            if (taskItem!!.dueDateTime() != null) {
                dueDateTime = taskItem!!.dueDateTime()!!
            }
            if (taskItem!!.file != null) {
                selectedFilePath = taskItem!!.file
            }
        }
        taskViewModel = ViewModelProvider(activity)[TaskViewModel::class.java]
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val tempFile = data?.data
            selectedFile = copyFileToPrivateStorage(tempFile!!)
            selectedFilePath = if (selectedFile == null) null else selectedFile.toString()
        }
    }

    private fun copyFileToPrivateStorage(sourceUri: Uri): Uri? {
        return try {
            val inputStream: InputStream =
                requireContext().contentResolver.openInputStream(sourceUri) ?: return null
            val mimeType: String? = requireContext().contentResolver.getType(sourceUri)
            val extension: String =
                MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: ""

            val fileName = "attachment_" + System.currentTimeMillis() + "." + extension
            val directory: File? = requireContext().getExternalFilesDir(null)
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveAction() {
        if (dueDate != null && dueTime != null) {
            dueDateTime = LocalDateTime.of(
                dueDate!!.year,
                dueDate!!.month,
                dueDate!!.dayOfMonth,
                dueTime!!.hour,
                dueTime!!.minute
            )
        }
        name = binding.taskNameInput.text.toString()
        desc = binding.taskDescriptionInput.text.toString()
        category = binding.taskCategoryInput.text.toString()
        val dueDateTimeString =
            if (dueDateTime == null) null else TaskItem.dateTimeFormatter.format(dueDateTime)
        if (taskItem == null) {
            val newTask =
                TaskItem(name!!, desc!!, dueDateTimeString, null, category, selectedFilePath)
            taskViewModel.addTaskItem(newTask)
        } else {
            taskItem!!.name = name!!
            taskItem!!.desc = desc!!
            taskItem!!.category = category
            taskItem!!.dueDateTime = dueDateTimeString
            taskItem!!.file = selectedFilePath
            taskViewModel.updateTaskItem(taskItem!!)
        }
        binding.taskNameInput.setText("")
        binding.taskDescriptionInput.setText("")
        if (dueDateTime != null) {
            setAlarm1()
        }
        dismiss()
    }


    private fun openTimePicker() {
        if (dueTime == null)
            dueTime = LocalTime.now()
        val listener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.show()
    }

    private fun openDatePicker() {
        if (dueDate == null)
            dueDate = LocalDate.now()
        val listener =
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                dueDate = LocalDate.of(selectedYear, selectedMonth, selectedDay)
            }
        val dialog = DatePickerDialog(
            requireActivity(),
            listener,
            dueDate!!.year,
            dueDate!!.monthValue,
            dueDate!!.dayOfMonth
        )
        dialog.show()

    }

    private fun setAlarm1() {
        val sharedPref = requireContext().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val offset = sharedPref.getInt("notifTimeOffset", 0)
        val offsetMilis = TimeUnit.MINUTES.toMillis(offset.toLong())
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, dueDateTime!!.hour)
        calendar.set(Calendar.MINUTE, dueDateTime!!.minute)
        calendar.set(Calendar.SECOND, 0)
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val thuReq: Long = Calendar.getInstance().timeInMillis + 1
        val reqReqCode = thuReq.toInt()
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, dueDateTime!!.dayOfYear)
        }
        val alarmTimeMilsec = calendar.timeInMillis - offsetMilis
        val intent = Intent(context, Notification::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        intent.putExtra(titleExtra, name)
        intent.putExtra(messageExtra, desc)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reqReqCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTimeMilsec,
            pendingIntent
        )
    }

}








