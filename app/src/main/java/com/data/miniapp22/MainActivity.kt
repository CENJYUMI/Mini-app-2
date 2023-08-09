package com.data.miniapp22

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.data.miniapp22.databinding.ActivityMainBinding
import com.data.miniapp22.databinding.AddDialogLayoutBinding
import com.data.miniapp22.databinding.Update2DialogLayoutBinding
import com.data.miniapp22.databinding.UpdateDialogLayoutBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemList:MutableList<Item>
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //object instantiation
        databaseHelper = DatabaseHelper(this)

        //setup the recyclerview
        recyclerView = binding.recyclerView
        //add layout to recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)

        //declare data
        itemList = getData()

        //initialize adapter object
        adapter = ItemAdapter(itemList)
        adapter.onDeleteClick = {item ->
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Delete Note")
            alertDialogBuilder.setMessage("Are you sure you want to delete this item?")

            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                //delete from database
                delete(item.itemID)
                //delete from list
                itemList.remove(item)
                //notify the adapter that dat has changed
                adapter.notifyDataSetChanged()

            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }
        adapter.onUpdateClick = {item ->
            val alertDialogBuilder = AlertDialog.Builder(this)

            val dialogLayout = layoutInflater.inflate(R.layout.update_dialog_layout, null)
            val dialogBinding = Update2DialogLayoutBinding.bind(dialogLayout)
            alertDialogBuilder.setView(dialogLayout)

            dialogBinding.updateName.setText(item.name)
            dialogBinding.updateDesc.setText(item.description)
            dialogBinding.updateQty.setText(item.quantity)

            alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                val name = dialogBinding.updateName.text.toString()
                val desc = dialogBinding.updateDesc.text.toString()
                val qty = dialogBinding.updateQty.text.toString().toInt()

                val newItem2 = Item(item.itemID, name , desc, qty )
                update(newItem2)

                //find the index of the viewHolder in the recyclerview
                val updateItemPosition = itemList.indexOfFirst { it.itemID == item.itemID }
                if(updateItemPosition != -1) {
                    itemList[updateItemPosition] = newItem2
                    adapter.notifyItemChanged(updateItemPosition)
                }

            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        recyclerView.adapter = adapter

        //add item
        binding.floatingActionButton.setOnClickListener{
            showAddDialog()
        }

    }
    private fun showAddDialog(){
        val alertDialogBuilder = AlertDialog.Builder(this)

        val dialogLayout = layoutInflater.inflate(R.layout.add_dialog_layout, null)
        val dialogBinding = AddDialogLayoutBinding.bind(dialogLayout)
        alertDialogBuilder.setView(dialogLayout)

        alertDialogBuilder.setPositiveButton("Add"){ dialog, _ ->
            val name = dialogBinding.addName.text.toString()
            val desc = dialogBinding.addDesc.text.toString()
            val qty = dialogBinding.addQty.text.toString().toInt()

            var newItem = Item (0, name, desc, qty)
            //add new data to database table
            addData(newItem)
            //add new note to list
            itemList.add(newItem)
            //notify adapter that data has changed
            recyclerView.adapter?.notifyDataSetChanged()
            dialog.dismiss()

        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun getData(): MutableList<Item> {
        return databaseHelper.getAllItems()
    }

    private fun addData(items: Item){
        databaseHelper .insertItems(items)
        Toast.makeText(applicationContext, "New item Added!", Toast.LENGTH_LONG).show()

    }

    private fun update(item: Item){
        databaseHelper.updateItemData(item)
        getData()
        Toast.makeText(applicationContext, "Item Updated!", Toast.LENGTH_LONG).show()

    }
    private fun delete ( id: Int){
        databaseHelper.deleteItemData(id)
        getData()
        Toast.makeText(applicationContext, "Item Deleted!", Toast.LENGTH_LONG).show()
    }
}