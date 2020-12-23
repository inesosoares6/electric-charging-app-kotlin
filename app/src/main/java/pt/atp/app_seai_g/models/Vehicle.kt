package pt.atp.app_seai_g.models

class Vehicle {
    var type: String? = null
        private set
    var brand: String? = null
        private set
    var model: String? = null
        private set

    constructor() {}
    constructor(type: String?, brand: String?, model: String?) {
        this.type = type
        this.brand = brand
        this.model = model
    }
}