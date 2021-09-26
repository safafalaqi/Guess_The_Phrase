package com.example.guessthephrase

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var textEdit: EditText
    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var myRv: RecyclerView

    private var guessPhraseCount = 10
    private var guessLetterCount = 10
    private val str ="Coding Dojo is great"
    private var guessedPhrase = arrayListOf<String>()
    private var guessedLetters = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView =findViewById<TextView>(R.id.textView)
        textEdit= findViewById<EditText>(R.id.enteredGuess)
        button = findViewById<Button>(R.id.button)
        myRv = findViewById<RecyclerView>(R.id.rvMain)


        //take a string and convert each letter into a star character
        //replace all characters to *
        textView.text="phrase: ${str.replace("[^\\s]".toRegex(), "*")}"
        //- ask the user to guess a predefined phrase
        button.setOnClickListener{
         // if(guessPhraseCount>0)
            guessPhrase()
            //else
            //guessLetter()
        }
       //  - ask the user to guess a letter from the phrase if they cannot guess the full phrase
       // - check the phrase for the guessed letter and convert stars into correctly guessed letters
       // - track guessed letters and display them to the user
        //- allow the user to guess the full phrase 10 times, the user should be able to enter a phrase during this stage
        //- allow the user to guess 10 letters, the user should only be able to enter single letters during this stage
       // - change the Entry Text hint to reflect whether the user is guessing the phrase or a letter


    }

    private fun guessLetter() {

    }

    private fun guessPhrase(){
        val guess = textEdit.text.toString()

        if (textEdit.text.isEmpty()) {
            Toast.makeText(this, "Please enter a phrase ", Toast.LENGTH_SHORT).show()
            textEdit.getText().clear()
        } else if(guessPhraseCount==0)  //if we have 0 guess
        {
            showAlertDialog("You Lost")
            textEdit.getText().clear()
        }
        else if(guess==str) {
            showAlertDialog("You win")
            textEdit.text.clear()
            guessedPhrase.add("You guessed ${textEdit.text.toString()}\n Correct Guess! You win.")
            //then clear the Edit Text field.
            textEdit.getText().clear()
            myRv.adapter = RecyclerViewAdapter(guessedPhrase)
            myRv.layoutManager = LinearLayoutManager(this)
            //set guess to 0 so the dialog box appear and the user will choose to play or not
           }
           else
            {
                guessedPhrase.add("Wrong guess: ${textEdit.text.toString()}")
                guessedPhrase.add("$guessPhraseCount guesses reamining")

                //then clear the Edit Text field.
                textEdit.getText().clear()
                myRv.adapter = RecyclerViewAdapter(guessedPhrase)
                myRv.layoutManager = LinearLayoutManager(this)
                guessPhraseCount--
            }
        myRv.scrollToPosition(guessedPhrase.size - 1)
    }



    //this function is used to show Alert Dialog for the user
    private fun showAlertDialog(str:String)
    {
        // first we create a variable to hold an AlertDialog builder
        val dialogBuilder = AlertDialog.Builder(this)
        // here we set the message of our alert dialog
        dialogBuilder.setMessage("$str\nWould you like to play again?")
            // positive button text and action
            .setPositiveButton("Play", DialogInterface.OnClickListener {
                    dialog, id ->  this.recreate()

            })
            // negative button text and action
            .setNegativeButton("Stop", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Game Over")
        // show alert dialog
        alert.show()
    }

}
