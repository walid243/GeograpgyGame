package com.example.geograpgygame.logic

import com.example.geograpgygame.interfaces.IRepository

object ServiceLocator {
    val repository : IRepository = Repository()
}