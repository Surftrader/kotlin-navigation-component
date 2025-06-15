package ua.com.poseal.navcomponent.model

class LoadDataException : Exception()

class DuplicateException(
    val duplicatedValue: String,
) : Exception("The list can't contain duplicated items")
