package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DarkGlassCard
import com.example.ui.components.GlassCard
import com.example.ui.theme.DarkGrayText
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.LightGrayBG
import com.example.ui.theme.MustardYellow
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SecondaryTextGray

import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel? = null,
    modifier: Modifier = Modifier
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var glassmorphismEffects by remember { mutableStateOf(true) }
    var biometricAuth by remember { mutableStateOf(true) }

    val loggedInUser by viewModel?.loggedInUser?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(null) }
    val userProfile by viewModel?.userProfile?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(null) }

    val displayName = loggedInUser?.fullName ?: userProfile?.name ?: "Ana Paula B."
    val displayEmail = loggedInUser?.email ?: "anapaula@saudavel.com"
    val initials = displayName.split(" ").take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("").ifEmpty { "AP" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightGrayBG)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Header Title
        item {
            Column {
                Text(
                    text = "Configurações & Perfil",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGrayText
                )
                Text(
                    text = "Personalização de conta, segurança e preferências",
                    fontSize = 14.sp,
                    color = SecondaryTextGray
                )
            }
        }

        // Profile Card (Dark Glass)
        item {
            DarkGlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(DarkGreen, CircleShape)
                                .border(2.dp, MustardYellow, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initials,
                                color = PureWhite,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = displayName,
                                color = PureWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = displayEmail,
                                color = PureWhite.copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .background(MustardYellow, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Conta Autenticada • Room DB",
                                    color = DarkGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Logout Button inside Settings Profile
                    if (viewModel != null) {
                        Button(
                            onClick = { viewModel.logout() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("settings_logout_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PureWhite.copy(alpha = 0.18f),
                                contentColor = PureWhite
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Sair da Conta",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sair / Trocar de Conta",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Preferences Group
        item {
            Text(
                text = "Preferências de Exibição & Segurança",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGrayText
            )
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SettingToggleRow(
                        icon = Icons.Default.Notifications,
                        title = "Notificações de Lançamento",
                        subtitle = "Alertas de vencimento de pendências",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )

                    SettingToggleRow(
                        icon = Icons.Default.Palette,
                        title = "Efeitos Glassmorphism & Blur",
                        subtitle = "Transparência suave de componentes de vidro",
                        checked = glassmorphismEffects,
                        onCheckedChange = { glassmorphismEffects = it }
                    )

                    SettingToggleRow(
                        icon = Icons.Default.Fingerprint,
                        title = "Autenticação Biométrica",
                        subtitle = "Proteger acesso com biometria ou PIN",
                        checked = biometricAuth,
                        onCheckedChange = { biometricAuth = it }
                    )
                }
            }
        }

        // System Settings Links
        item {
            Text(
                text = "Dados & Sistema",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGrayText
            )
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SettingLinkRow(
                        icon = Icons.Default.Language,
                        title = "Idioma & Moeda",
                        value = "Português (BR) • R$"
                    )

                    SettingLinkRow(
                        icon = Icons.Default.FileDownload,
                        title = "Exportar Relatórios Financeiros",
                        value = "Formato CSV / PDF"
                    )

                    SettingLinkRow(
                        icon = Icons.Default.Lock,
                        title = "Política de Privacidade & LGPD",
                        value = "Criptografia Local"
                    )
                }
            }
        }

        // Export Action Button
        item {
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
            ) {
                Icon(imageVector = Icons.Default.FileDownload, contentDescription = "Exportar", tint = MustardYellow)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Exportar Base de Dados em PDF", color = PureWhite, fontWeight = FontWeight.Bold)
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "FinGlass Analytics v1.0.0 • Design System Glassmorphism",
                    fontSize = 12.sp,
                    color = SecondaryTextGray
                )
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
fun SettingToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(DarkGreen.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = DarkGreen, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                Text(text = subtitle, fontSize = 11.sp, color = SecondaryTextGray)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = PureWhite,
                checkedTrackColor = DarkGreen,
                uncheckedThumbColor = PureWhite,
                uncheckedTrackColor = LightGrayBG
            )
        )
    }
}

@Composable
fun SettingLinkRow(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(DarkGreen.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = DarkGreen, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                Text(text = value, fontSize = 11.sp, color = SecondaryTextGray)
            }
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Abrir", tint = SecondaryTextGray)
    }
}
