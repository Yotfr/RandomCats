package com.yotfr.randomcats.presentation.screens.random_cat_screen.model

import com.yotfr.randomcats.domain.model.Cat

data class RandomCatState(

  val isLoading:Boolean = false,
  val cat:Cat? = null,
  val error:String = ""

)