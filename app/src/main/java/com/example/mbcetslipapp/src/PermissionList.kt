package com.example.mbcetslipapp.src

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbcetslipapp.R
import com.example.mbcetslipapp.data.Slip
import com.example.mbcetslipapp.data.slips
import com.example.mbcetslipapp.ui.theme.PrimBlue
import com.example.mbcetslipapp.ui.theme.SlipViewModel

@Composable
fun PermissionScreen(slipViewModel: SlipViewModel = viewModel()) {
    val slipUiState by slipViewModel.uiState.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            if(slipUiState.userType=="Student")
                slipViewModel.updateUserType("HoD")
            else
                slipViewModel.updateUserType("Student")
        },
            modifier = Modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = PrimBlue)){
            Text("Switch")
        }
        Row(modifier = Modifier.padding(8.dp)) {
            Button(onClick = {
                slipViewModel.updateSelection("Requested")
            },
            modifier = Modifier,
                colors = ButtonDefaults.buttonColors(backgroundColor = PrimBlue)){
                Text(stringResource(id = R.string.requested))
            }
            Spacer(Modifier.weight(0.5f))
            Button(onClick = {
                slipViewModel.updateSelection("Approved")
            },
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(backgroundColor = PrimBlue)){
                Text(stringResource(id = R.string.approved))
            }
        }
        Text(text = slipUiState.selection)
        if(slipUiState.selection == "Requested")
            RequestedPermissionScreen()
        else
            ApprovedPermissionScreen()
    }
}

@Composable
fun RequestedPermissionScreen(slipViewModel: SlipViewModel = viewModel()) {
    val slipUiState by slipViewModel.uiState.collectAsState()
    LazyColumn(modifier = Modifier.background(MaterialTheme.colors.background)) {
        items(slips)
        {
                if (!it.status.value && (it.rollNo == slipUiState.studentRoll || slipUiState.userType != "Student"))
                    SlipItem(slip = it, userType = slipUiState.userType)
        }
    }
}

@Composable
fun ApprovedPermissionScreen(slipViewModel: SlipViewModel = viewModel()) {
    val slipUiState by slipViewModel.uiState.collectAsState()
    LazyColumn(modifier = Modifier.background(MaterialTheme.colors.background)) {
        items(slips)
        {
            if (it.status.value && (it.rollNo == slipUiState.studentRoll || slipUiState.userType != "Student"))
                SlipItem(slip = it, userType = slipUiState.userType)
        }
    }
}

@Composable
fun SlipItem(slip: Slip, modifier: Modifier = Modifier, userType: String) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        elevation = 4.dp,
        modifier = modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                SlipData(slip.name, slip.title, stringResource(id = slip.slipType),userType = userType)
                Spacer(Modifier.weight(1f))
                ExpandButton(expanded = expanded, onClick = {expanded = !expanded})
            }
            if (expanded) {
                Column() {
                    SlipInfo(slip, userType)
                    SlipDescription(slip.slipDescribe)
                }
                Spacer(Modifier.weight(1f))
                if((userType == "Advisor" || userType == "HoD") && !slip.status.value)
                {
                    ApproveButton(
                        onClicked = {
                            slip.acceptStatus()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SlipInfo(slip: Slip, userType: String) {
    if(userType == "Student") {
        Column() {
            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = "Advisors: ",
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primaryVariant
                )
                slip.advisors.forEach {
                    Text(
                        text = "$it, ",
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primaryVariant
                    )
                }
            }
            Text(
                text = "HoD: ${stringResource(id = slip.HoD)}",
                modifier = Modifier.padding(
                    8.dp),
                fontSize = 12.sp,
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
    else {
        Column() {
            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = "S${slip.semester} ${slip.className}",
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primaryVariant
                )
                Text(
                    text = ": ${slip.rollNo}",
                    modifier = Modifier.padding(
                        8.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primaryVariant
                )
            }
        }
    }
}

@Composable
fun ApproveButton(
    onClicked: () -> Unit
)
{
    Button(
        onClick = onClicked,
        modifier = Modifier.padding(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = PrimBlue)
    )
    {
        Text(text = stringResource(id = R.string.approve_slip))
    }
}

@Composable
fun SlipDescription(slipDescribe : String)
{
    Text(
        text = "Description: \n $slipDescribe",
        modifier = Modifier.padding(8.dp),
        fontSize = 13.sp,
        color = MaterialTheme.colors.primaryVariant
    )
}

@Composable
fun SlipData (name: String, title: String, type: String, userType: String)
{
    Column() {
        if(userType == "Student")
        {
            Text(
                text = title,
                modifier = Modifier.padding(8.dp)
            )
        }
        else {
            Text(
                text = "$name: $title",
                modifier = Modifier.padding(8.dp)
            )
        }
        Text(
            text = type,
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier.padding(8.dp),
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun ExpandButton(
    expanded: Boolean,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            tint = MaterialTheme.colors.primaryVariant,
            contentDescription = "Check Description",
        )
    }
}