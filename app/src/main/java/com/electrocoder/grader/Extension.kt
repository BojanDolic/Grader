package com.electrocoder.grader

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment



fun View.animateAndHide(action: Int) {

    if(action == 1) {
        animate().translationY(200f).alpha(0f).setDuration(500).setListener(object : Animator.AnimatorListener {

            override fun onAnimationEnd(animation: Animator?) {
                visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
    } else if(action == 0){

        animate().translationY(0f).alpha(100f).setDuration(500).setListener(object : Animator.AnimatorListener {

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                visibility = View.VISIBLE
            }

        })

    }

}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}