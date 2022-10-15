package com.example.mbcetslipapp.data

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import com.example.mbcetslipapp.R

data class Slip(
    val name: String = "",
    @StringRes val slipType: Int,
    val title: String,
    val slipDescribe: String,
    val status: MutableState<Boolean> = mutableStateOf(false),
    val semester: Int,
    val className: String = "",
    val rollNo: String = "",
    val advisors: List<String> = listOf(""),
    @StringRes val HoD: Int
)
{
    fun acceptStatus() {
        status.value = true
    }
}

val slips = mutableListOf(
    Slip(name = "Samuel John", slipType = R.string.slip_misc, title = "Trials", slipDescribe = "Trial And Error", className = "CS 2", semester = 3, advisors = listOf("Ms. Gayathri K. S.","Mr. Praveen J S","Ms. Sreeja S R"), HoD = R.string.hod_cs, rollNo = "B21CS1234"),
    Slip(name = "Ab Cde", slipType = R.string.slip_id, title = "Request for new ID", slipDescribe = "This is to request for a new id. Unfortunately, my ID is lost, and I would like to request a replacement.", semester = 7, className = "CS 1", advisors = listOf("Dun", "Dun Dun", "Duuun"), HoD = R.string.hod_cs, rollNo = "B19CS1111"),
    Slip(name = "Calma Von Feincherstire", slipType = R.string.slip_misc, title = "Trials", slipDescribe = "Trial And Error", semester = 5, className = "CS 1", advisors = listOf("Tana", "Nana"), HoD = R.string.hod_cs, rollNo = "B20CS1100"),
    Slip(name = "Jacob", slipType = R.string.slip_misc, title = "Trials", slipDescribe = "Trial And Error", semester = 1, className = "CS 2", HoD = R.string.hod_cs, rollNo = "B22CS1232"),
    Slip(name = "Frieren", slipType = R.string.slip_gate, title = "Permission to Leave Early", slipDescribe = "I am feeling fever and extreme headaches.\n Please excuse me for the day.", semester = 1, className = "CS 2", advisors = listOf("Never", "Gonna"), HoD = R.string.hod_cs, rollNo = "B22CS1247"),
    Slip(name = "Samuel John", slipType = R.string.slip_misc, title = "Trials", slipDescribe = "Trial And Error",  semester = 3, className = "CS 21", HoD = R.string.hod_cs, rollNo = "B21CS1212")
)