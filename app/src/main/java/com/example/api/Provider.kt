package com.example.api

import com.example.api.model.Launch
import com.example.api.model.Rocket

class Provider {

    companion object {

        var allLaunches = ArrayList<Launch>()
        var rocket = Rocket()
    }
}