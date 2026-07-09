package com.devflow.app.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppCard(

    modifier: Modifier = Modifier,

    content: @Composable () -> Unit

) {

    Card(

        modifier = modifier,

        elevation = CardDefaults.cardElevation(4.dp)

    ) {

        Column(

            modifier = Modifier.padding(16.dp)

        ) {

            content()

        }

    }

}