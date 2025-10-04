package com.example.rebottle.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rebottle.domain.data.Role
import com.example.rebottle.ui.theme.LightGreen

private val RebottleGreen = Color(0xFF1B4332)
private val SelectedBg    = Color(0xFF5C8C5F)

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
            val icon = when (r) {
                Role.USUARIO     -> Icons.Outlined.Person
                Role.RECOLECTOR  -> Icons.Outlined.LocalShipping
                Role.EMPRESA_REP -> Icons.Outlined.Business
            }

            FilterChip(
                selected = selected == r,
                onClick = { onSelect(r) },
                label = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = r.displayName,
                            tint = RebottleGreen,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = r.displayName,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = LightGreen,
                    selectedLabelColor = RebottleGreen,
                    containerColor = Color.White,
                    labelColor = RebottleGreen
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(72.dp)
                    .padding(vertical = 2.dp)
            )
        }
    }
}
