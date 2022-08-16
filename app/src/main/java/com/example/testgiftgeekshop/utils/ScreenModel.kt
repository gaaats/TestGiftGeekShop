package com.example.testgiftgeekshop.utils

data class ScreenModel(
    val ans1: ModelAnswer = ModelAnswer(),
    val ans2: ModelAnswer = ModelAnswer(),
    val ans3: ModelAnswer = ModelAnswer(),
    val ans4: ModelAnswer = ModelAnswer(),
    val ans5: ModelAnswer = ModelAnswer(),
    val ans6: ModelAnswer = ModelAnswer(),
    val ans7: ModelAnswer = ModelAnswer(),
    val ans8: ModelAnswer = ModelAnswer(),
    val ans9: ModelAnswer = ModelAnswer(),
    val ans10: ModelAnswer = ModelAnswer(),
    var isAnyAnswerActive: Boolean = false
) {
    val listAnswer = listOf(ans1, ans2, ans3, ans4, ans5, ans6, ans7, ans8, ans9, ans10)

    fun returnActiveParams(): List<Int> {
        val listActiveParams = mutableListOf<Int>()
        for (element in listAnswer) {
            if (element.isActive) {
                element.codesForSearch.forEach {
                    listActiveParams.add(it)
                }
            }
        }
        return listActiveParams
    }

    fun returnSingleActiveParams(): String? {
        for (element in listAnswer) {
            if (element.isActive) {
                return element.title
            }
        }
        return null
    }

    fun checkAnsAndChangeState(text: String) {
        when (text) {
            ans1.title -> ans1.changeState()
            ans2.title -> ans2.changeState()
            ans3.title -> ans3.changeState()
            ans4.title -> ans4.changeState()
            ans5.title -> ans5.changeState()
            ans6.title -> ans6.changeState()
            ans7.title -> ans7.changeState()
            ans8.title -> ans8.changeState()
            ans9.title -> ans9.changeState()
            ans10.title -> ans10.changeState()
        }
        checkIsAnyAnswerActive()
    }

    private fun checkIsAnyAnswerActive() {
        for (element in listAnswer) {
            if (element.isActive) {
                isAnyAnswerActive = true
                return
            }
        }
        isAnyAnswerActive = false
        return
    }
}
