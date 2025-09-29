package com.example.rebottle.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rebottle.domain.data.Role

@Composable
fun RoleSelector(
    selected: Role?,
    onSelect: (Role) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(Role.USUARIO, Role.RECOLECTOR, Role.EMPRESA_REP).forEach { r ->
            FilterChip(
                selected = selected == r,
                onClick = { onSelect(r) },
                label = { Text(r.displayName) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 2.dp)
            )
        }
    }
}
