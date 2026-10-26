package com.duocuc.pagoflex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duocuc.pagoflex.data.local.AppDatabase
import com.duocuc.pagoflex.data.local.entity.CompromisoEntity
import com.duocuc.pagoflex.ui.screens.AgentReportScreen
import com.duocuc.pagoflex.ui.screens.ExecutiveDashboard
import com.duocuc.pagoflex.ui.screens.PaymentDetailScreen
import com.duocuc.pagoflex.ui.screens.ReceiptHistoryScreen
import com.duocuc.pagoflex.ui.screens.RecommendationScreen
import com.duocuc.pagoflex.ui.screens.RegisterCompromisoScreen
import com.duocuc.pagoflex.ui.screens.ReportProblemScreen
import com.duocuc.pagoflex.ui.screens.SessionSelectScreen
import com.duocuc.pagoflex.ui.screens.UserHomeScreen
import com.duocuc.pagoflex.viewmodel.AgentSupportViewModel
import com.duocuc.pagoflex.viewmodel.ReportProblemViewModel
import com.duocuc.pagoflex.viewmodel.UserDashboardViewModel
import com.duocuc.pagoflex.viewmodel.ExecutiveCobranzaViewModel
import com.duocuc.pagoflex.viewmodel.PaymentViewModel
import com.duocuc.pagoflex.viewmodel.RecommendationViewModel
import com.duocuc.pagoflex.viewmodel.RegistrarCompromisoViewModel
import com.duocuc.pagoflex.viewmodel.SessionRole
import com.duocuc.pagoflex.viewmodel.SessionViewModel

@Composable
fun AppNavegacion(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val reportProblemViewModel: ReportProblemViewModel = viewModel()
    val agentSupportViewModel: AgentSupportViewModel = viewModel()
    val executiveCobranzaViewModel: ExecutiveCobranzaViewModel = viewModel()
    val sessionViewModel: SessionViewModel = viewModel()
    val registrarCompromisoViewModel: RegistrarCompromisoViewModel = viewModel()
    val usuarioActual by sessionViewModel.session.collectAsStateWithLifecycle()

    val paymentViewModel: PaymentViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PaymentViewModel(
                    db.compromisoDao(),
                    db.pagoDao()
                ) as T
            }
        }
    )
    val recommendationViewModel: RecommendationViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RecommendationViewModel(
                    db.recomendacionDao()
                ) as T
            }
        }
    )

    NavHost(
        navController = navController,
        startDestination = Ruta.SessionSelect.ruta
    ){
        composable(Ruta.SessionSelect.ruta){
            SessionSelectScreen(
                sessionViewModel = sessionViewModel,
                onSesionSeleccionada = {
                    when (sessionViewModel.session.value?.role){
                        SessionRole.USUARIO_FINAL -> {
                            navController.navigate(Ruta.UserHome.ruta)
                        }

                        SessionRole.EJECUTIVO -> {
                            navController.navigate(Ruta.DashboardEjecutivo.ruta)
                        }

                        SessionRole.ADMINISTRADOR -> {
                            navController.navigate(Ruta.ReportesAgente.ruta)
                        }

                        null -> {}
                    }
                }
            )
        }

        // Reportar Problema
        composable(Ruta.ReportarProblema.ruta){
            ReportProblemScreen(
                reportProblemViewModel
            )
        }

        //User Home
        composable(Ruta.UserHome.ruta){
            val userDashboardViewModel: UserDashboardViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return UserDashboardViewModel(
                            db.compromisoDao(),
                            usuarioActual?.userId ?:0
                        ) as T
                    }
                }
            )

            UserHomeScreen(
                dashboardViewModel = userDashboardViewModel,
                sessionViewModel = sessionViewModel,
                onVerCompromisos = { navController.navigate(Ruta.ReceiptHistory.ruta) },
                onVerHistorial = { navController.navigate(Ruta.ReceiptHistory.ruta) },
                onVerRecomendaciones = { navController.navigate(Ruta.Recomendaciones.ruta) },
                onCerrarSesion = {
                    sessionViewModel.cerrarSesion()
                    navController.navigate(Ruta.ReportarProblema.ruta){
                        popUpTo(Ruta.UserHome.ruta){
                            inclusive = true
                        }
                    }
                }
            )
        }

        //Receipt History
        composable(Ruta.ReceiptHistory.ruta){
            ReceiptHistoryScreen(
                paymentViewModel = paymentViewModel,
                onVolver = { navController.navigateUp() }
            )
        }

        //Recomendaciones
        composable(Ruta.Recomendaciones.ruta){
            RecommendationScreen(
                usuarioId = usuarioActual?.userId ?: 0,
                recommendationViewModel = recommendationViewModel,
                onVolver = { navController.navigateUp() }

            )
        }

        // Payment
        composable(
            route = Ruta.PaymentDetail.ruta,
            arguments = listOf(navArgument("id"){ type = NavType.IntType })
            ){ entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            var compromiso by remember {
                mutableStateOf<CompromisoEntity?>(null)
            }

            LaunchedEffect(id) {
                paymentViewModel.obtenerCompromiso(id){
                    compromiso = it
                }
            }

            compromiso?.let {
                PaymentDetailScreen(
                    compromiso = it,
                    paymentViewModel = paymentViewModel,
                    onPagoExitoso = { navController.navigateUp() },
                    onVolver = { navController.navigateUp() }
                )
            }
        }

        //dashboard ejecutivo
        composable(Ruta.DashboardEjecutivo.ruta){
            ExecutiveDashboard(
                executiveCobranzaViewModel
            )
        }

        //Registrar Compromiso
        composable(Ruta.RegistrarCompromiso.ruta){
            RegisterCompromisoScreen(
                registrarCompromisoViewModel,
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.ReportesAgente.ruta){
            AgentReportScreen(
                agentSupportViewModel
            )
        }
    }
}