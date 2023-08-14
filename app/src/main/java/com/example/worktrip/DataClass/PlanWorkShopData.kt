package com.example.worktrip.DataClass

class PlanWorkShopData {

    private var userID:String? = null
    private var now:Boolean = true
    private var tv_plan_date: String? = null
    private var tv_plan_title: String? = null
    private var tv_plan_people: String? = null
    private var tv_plan_filter: String? = null
    private var tv_plan_budget: String? = null

    fun PlanWorkShopData(date: String?, title: String?, people: String?, filter: String?, budget: String?) {
        this.tv_plan_date = date
        this.tv_plan_title = title
        this.tv_plan_people = people
        this.tv_plan_filter = filter
        this.tv_plan_budget = budget
    }
    fun getuesrid(): String? {
        return userID
    }

    fun setuesrid(id: String?) {
        this.userID = id
    }

    fun getnowid(): Boolean {
        return now
    }

    fun setnowid(id: Boolean) {
        this.now = id
    }
    fun gettv_plan_date(): String? {
        return tv_plan_date
    }

    fun settv_plan_date(tv_plan_date: String?) {
        this.tv_plan_date = tv_plan_date
    }

    fun gettv_plan_title(): String? {
        return tv_plan_title
    }

    fun settv_plan_title(tv_plan_title: String?) {
        this.tv_plan_title = tv_plan_title
    }

    fun gettv_plan_people(): String? {
        return tv_plan_people
    }

    fun settv_plan_people(tv_plan_people: String?) {
        this.tv_plan_people = tv_plan_people
    }


    fun gettv_plan_filter(): String? {
        return tv_plan_filter
    }

    fun settv_plan_filter(tv_plan_filter: String?) {
        this.tv_plan_filter = tv_plan_filter
    }


    fun gettv_plan_budget(): String? {
        return tv_plan_budget
    }

    fun settv_plan_budget(tv_plan_budget: String?) {
        this.tv_plan_budget = tv_plan_budget
    }
}