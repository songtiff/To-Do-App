package com.example.todoapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //remove item from list
                listOfTasks.removeAt(position)
                //notify adapter that something has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        loadItems()

        //look up recycler view in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        recyclerView.adapter = adapter //connect recycler view and adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up the button and input field so the user can enter a task

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //get reference to the button and set it to on click
        findViewById<Button>(R.id.button).setOnClickListener {
            //grab the text the user has inputted into the edit text field
            val userInputtedTask = inputTextField.text.toString()

            //add the string to list of tasks
            listOfTasks.add(userInputtedTask)

            //notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size-1)

            //once the user has input a task, we clear the field so they can input a new one
            inputTextField.setText("")

            saveItems()
        }
    }

    //save the data that the user has input by reading and writing a file

    //create a method to get the data file we need
    fun getDataFile() : File {
        //every line is going to represent a specific task
        return File(filesDir, "data.txt")
    }

    //method to load the items every line in the data file
    fun loadItems() {
        try {
        listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    //save items by writing them into our data file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}