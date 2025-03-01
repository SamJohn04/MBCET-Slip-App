package com.example.mbcetslipapp.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbcetslipapp.R
import com.example.mbcetslipapp.data.Slip
import com.example.mbcetslipapp.data.slips
import com.example.mbcetslipapp.ui.theme.*


@Composable
fun PermissionScreen(listSlipViewModel: ListSlipViewModel = setUserExtrernal("Rick Astley", viewModel())) {
    val slipUiState by listSlipViewModel.uiState.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.background(color = MaterialTheme.colors.background)) {
        Row(modifier = Modifier.padding(8.dp)) {
            Button(onClick = {
                listSlipViewModel.updateSelection("Requested")
            },
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(backgroundColor = SecOrange)){
                Text(stringResource(id = R.string.requested))
            }
            Spacer(Modifier.weight(0.5f))
            Button(onClick = {
                listSlipViewModel.updateSelection("Approved")
            },
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(backgroundColor = PrimGreen)){
                Text(stringResource(id = R.string.approved))
            }
        }
        Text(
            text = slipUiState.selection,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colors.primaryVariant
        )
        Spacer(Modifier.height(5.dp))
        if(slipUiState.selection == "Requested")
            RequestedPermissionScreen()
        else
            ApprovedPermissionScreen()
    }
}

@Composable
fun RequestedPermissionScreen(listSlipViewModel: ListSlipViewModel = viewModel()) {
    val slipUiState by listSlipViewModel.uiState.collectAsState()
    LazyColumn(modifier = Modifier) {
        items(slips)
        {
            if (!it.status.value)
                SlipItem(slip = it, userType = slipUiState.userType)
        }
    }
}

@Composable
fun ApprovedPermissionScreen(listSlipViewModel: ListSlipViewModel = viewModel()) {
    val slipUiState by listSlipViewModel.uiState.collectAsState()
    LazyColumn(modifier = Modifier) {
        items(slips)
        {
            if (it.status.value)
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

            .clip(shape = RoundedCornerShape(25.dp,25.dp,20.dp,20.dp)),
        contentColor = MaterialTheme.colors.onSurface
    ) {
        Column(
            modifier = Modifier.background(MaterialTheme.colors.primary.copy(alpha=0.9f))

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
                SlipData(stringResource(id = slip.name), slip.title, stringResource(id = slip.slipType),userType = userType)
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
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface

                )
                slip.advisors.forEach {
                    Text(
                        text = "${stringResource(id=it)}, ",
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface

                    )
                }
            }
            Text(
                text = "HoD: ${stringResource(id = slip.HoD)}",
                modifier = Modifier.padding(
                    8.dp),
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface

            )
        }
    }
    else {
        Column() {
            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = "S${stringResource(id=slip.semester)} ${stringResource(id = slip.className)}",
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = ": ${stringResource(id = slip.rollNo)}",
                    modifier = Modifier.padding(
                        8.dp),
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface

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
        text = "Description: \n \n $slipDescribe",
        modifier = Modifier.padding(8.dp),
        fontSize = 16.sp,
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
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(8.dp)
            )
        }
        else {
            Text(
                text = "$name: $title",
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(8.dp)
            )
        }
        Text(
            text = type,
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp,
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