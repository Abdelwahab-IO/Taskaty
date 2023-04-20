package com.example.taskaty.app.ui.fragments.abstractFragments

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

abstract class BaseAppBarFragment<VB : ViewBinding>
    (private val bindingInflater: (layoutInflater: LayoutInflater) -> VB)
    : BaseFragment<VB>(bindingInflater){

}
