package com.example.guessthephrase

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var textEdit: EditText
    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var myRv: RecyclerView
    private lateinit var myLayout: ConstraintLayout
    private lateinit var staredStr: CharArray
    private var checkNoStar = false
    private var guessPhraseCount = 9
    private var guessLetterCount = 9
    private val str ="coding dojo is great"
    private var guessedPhraseLetters = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView =findViewById<TextView>(R.id.textView)
        textEdit= findViewById<EditText>(R.id.enteredGuess)
        button = findViewById<Button>(R.id.button)
        myRv = findViewById<RecyclerView>(R.id.rvMain)
        myLayout = findViewById<ConstraintLayout>(R.id.clMain)
        myRv.adapter = RecyclerViewAdapter(guessedPhraseLetters)
        myRv.layoutManager = LinearLayoutManager(this)

        //take a string and convert each letter into a star character
        //replace all characters to *
        textView.text = "${str.replace("[^\\s]".toRegex(), "*")}"
        staredStr = textView.text.toString().toCharArray()
        println(staredStr)
        //- ask the user to guess a predefined phrase
        button.setOnClickListener{

          if(guessPhraseCount>=0) {
              // - change the Entry Text hint to reflect whether the user is guessing the phrase or a lette
              textEdit.setHint("Guess a Phrase")
              textEdit.maxLines=1
              guessPhrase()
          }
          else
          {
              guessedPhraseLetters.add("Guess letter Now")
              // - change the Entry Text hint to reflect whether the user is guessing the phrase or a lette
              textEdit.setHint("Guess a Letter")
              textEdit.filters += InputFilter.LengthFilter(1)
              guessLetter()
          }

        }
    }

    private fun guessLetter() {

        val guess = textEdit.text.toString().toCharArray(0)


        if (textEdit.text.isEmpty()) {
            Snackbar.make(myLayout, "Please enter a Letter", Snackbar.LENGTH_LONG).show()

            textEdit.getText().clear()
        } else if(guessLetterCount==0)  //if we have 0 guess
        {
            showAlertDialog("You Lost")
            textEdit.getText().clear()
        }else if(checkNoStar==true){
            showAlertDialog("You win")
            //then clear the Edit Text field.
            textEdit.getText().clear()
            myRv.adapter!!.notifyDataSetChanged()
        }
        else if(findChar(guess)) {

            //then clear the Edit Text field.
            textEdit.getText().clear()
            //guessedPhraseLetters.add("${guessLetterCount} guesses reamining")
            myRv.adapter!!.notifyDataSetChanged()
        }
        else
        {
            guessedPhraseLetters.add("Wrong guess: ${textEdit.text.toString()}")
            guessedPhraseLetters.add("${guessLetterCount} guesses reamining")

            //then clear the Edit Text field.
            textEdit.getText().clear()
            guessLetterCount--
            myRv.adapter!!.notifyDataSetChanged()

        }
        myRv.scrollToPosition(guessedPhraseLetters.size - 1)

    }
    private fun findChar(guess: CharArray):Boolean
    {
        var count =0

        for(i in 0..str.length-1)
        {
            if(str[i]==guess[0])
            {
                staredStr[i]= str[i]
                count++
                println(staredStr)
            }

        }
        //if no star make checkNoStar true
        checkNoStar = !staredStr.contains('*')
        if(checkNoStar)
        {
            showAlertDialog("You win")
            textEdit.text.clear()
            button.isEnabled = false
            button.isClickable = false
            textEdit.text.clear()
        }


        if (count>0) {
            guessedPhraseLetters.add("Found $count ${guess[0].uppercase()}(s) ")
            textView.text = String(staredStr)
            return true
        }

     return false
    }

    private fun guessPhrase(){
        val guess = textEdit.text.toString()

        if (textEdit.text.isEmpty()) {
            Snackbar.make(myLayout, "Please enter a phrase", Snackbar.LENGTH_LONG).show()
          // Toast.makeText(this, "Please enter a phrase ", Toast.LENGTH_SHORT).show()
            textEdit.getText().clear()
        } else if(guessPhraseCount==0)  //if we have 0 guess
        {
            guessedPhraseLetters.add("Wrong guess: ${textEdit.text.toString()}")
            guessedPhraseLetters.add("$guessPhraseCount guesses reamining")
            //then clear the Edit Text field.
            textEdit.getText().clear()
            myRv.adapter!!.notifyDataSetChanged()
            guessPhraseCount--

            // - change the Entry Text hint to reflect whether the user is guessing the phrase or a lette
            textEdit.setHint("Guess a Letter")
            textEdit.filters += InputFilter.LengthFilter(1)
        }
        else if(guess==str) {
            showAlertDialog("You win")

            textEdit.text.clear()
            button.isEnabled = false
            button.isClickable = false
            guessedPhraseLetters.add("You guessed ${textEdit.text.toString()}\n Correct Guess! You win.")
            //then clear the Edit Text field.
            textEdit.getText().clear()
            myRv.adapter!!.notifyDataSetChanged()
            //set guess to 0 so the dialog box appear and the user will choose to play or not
           }
           else
            {
                guessedPhraseLetters.add("Wrong guess: ${textEdit.text.toString()}")
                guessedPhraseLetters.add("$guessPhraseCount guesses reamining")
                //then clear the Edit Text field.
                textEdit.getText().clear()
                guessPhraseCount--
                myRv.adapter!!.notifyDataSetChanged()

            }
        myRv.scrollToPosition(guessedPhraseLetters.size - 1)
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
