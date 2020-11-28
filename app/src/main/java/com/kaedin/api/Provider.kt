package com.kaedin.api

import com.kaedin.api.model.Launch
import com.kaedin.api.model.Rocket

class Provider {

    companion object {

        var allLaunches = ArrayList<Launch>()
        var rocket = Rocket()
    }
}