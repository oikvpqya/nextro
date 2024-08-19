package me.oikvpqya.apps.music.feature.main

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.FloatingWindow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDestination
import androidx.navigation.NavDestinationBuilder
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.NavigatorState
import androidx.navigation.compose.LocalOwnersProvider
import androidx.navigation.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass
import kotlin.reflect.KType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberModalBottomSheetNavigator(
    sheetState: SheetState = rememberModalBottomSheetState()
): ModalBottomSheetNavigator {
    return remember(sheetState) { ModalBottomSheetNavigator(sheetState) }
}

inline fun <reified T : Any> NavGraphBuilder.modalBottomSheet(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable ColumnScope.(backstackEntry: NavBackStackEntry) -> Unit
) {
    destination(
        ModalBottomSheetNavigatorDestinationBuilder(
            navigator = provider[ModalBottomSheetNavigator::class],
            route = T::class,
            typeMap = typeMap,
            content = content,
        )
            .apply { deepLinks.forEach { deepLink -> deepLink(deepLink) } }
    )
}

class ModalBottomSheetNavigatorDestinationBuilder(
    navigator: ModalBottomSheetNavigator,
    route: KClass<*>,
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>>,
    private val content: @Composable ColumnScope.(NavBackStackEntry) -> Unit,
) : NavDestinationBuilder<ModalBottomSheetNavigator.Destination>(navigator, route, typeMap) {

    private val modalBottomSheetNavigator = navigator

    override fun instantiateDestination(): ModalBottomSheetNavigator.Destination {
        return ModalBottomSheetNavigator.Destination(
            modalBottomSheetNavigator,
            content,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Navigator.Name("modalBottomSheet")
class ModalBottomSheetNavigator(
    private val sheetState: SheetState,
) : Navigator<ModalBottomSheetNavigator.Destination>() {

    var attached by mutableStateOf(false)

    val backStack: StateFlow<List<NavBackStackEntry>>
        get() = if (attached) {
            state.backStack
        } else {
            MutableStateFlow(emptyList())
        }

    val transitionsInProgress: StateFlow<Set<NavBackStackEntry>>
        get() = if (attached) {
            state.transitionsInProgress
        } else {
            MutableStateFlow(emptySet())
        }

    fun dismiss(backStackEntry: NavBackStackEntry) {
        popBackStack(backStackEntry, false)
    }

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ) {
        entries.forEach { entry -> state.push(entry) }
    }

    override fun onAttach(state: NavigatorState) {
        super.onAttach(state)
        attached = true
    }

    override fun createDestination(): Destination = Destination(
        navigator = this,
        content = {},
    )

    override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
        state.popWithTransition(popUpTo, savedState)
        // When popping, the incoming dialog is marked transitioning to hold it in
        // STARTED. With pop complete, we can remove it from transition so it can move to RESUMED.
        val popIndex = state.transitionsInProgress.value.indexOf(popUpTo)
        // do not mark complete for entries up to and including popUpTo
        state.transitionsInProgress.value.forEachIndexed { index, entry ->
            if (index > popIndex) onTransitionComplete(entry)
        }
    }

    fun onTransitionComplete(entry: NavBackStackEntry) {
        state.markTransitionComplete(entry)
    }

    /**
     * [NavDestination] specific to [ModalBottomSheetNavigator]
     */
    @NavDestination.ClassType(Composable::class)
    class Destination(
        navigator: ModalBottomSheetNavigator,
        val content: @Composable ColumnScope.(NavBackStackEntry) -> Unit,
    ) : NavDestination(navigator), FloatingWindow

    companion object {
        const val NAME = "modalBottomSheet"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetHost(
    modalBottomSheetNavigator: ModalBottomSheetNavigator,
    sheetState: SheetState,
) {
    val saveableStateHolder = rememberSaveableStateHolder()
    val modalBottomSheetBackStack by modalBottomSheetNavigator.backStack.collectAsState()
    val visibleBackStack = rememberVisibleList(modalBottomSheetBackStack)
    visibleBackStack.PopulateVisibleList(modalBottomSheetBackStack)

    val transitionsInProgress by modalBottomSheetNavigator.transitionsInProgress.collectAsState()
    val sheetsToDispose = remember { mutableStateListOf<NavBackStackEntry>() }

    visibleBackStack.forEach { backStackEntry ->
        val destination = backStackEntry.destination as ModalBottomSheetNavigator.Destination
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                modalBottomSheetNavigator.dismiss(backStackEntry)
            },
        ) {
            DisposableEffect(backStackEntry) {
                sheetsToDispose.add(backStackEntry)
                onDispose {
                    modalBottomSheetNavigator.onTransitionComplete(backStackEntry)
                    sheetsToDispose.remove(backStackEntry)
                }
            }

            backStackEntry.LocalOwnersProvider(saveableStateHolder) {
                with(destination) {
                    this@ModalBottomSheet.content(backStackEntry)
                }
            }
        }
    }

    LaunchedEffect(transitionsInProgress, sheetsToDispose) {
        transitionsInProgress.forEach { entry ->
            if (
                !modalBottomSheetNavigator.backStack.value.contains(entry) &&
                !sheetsToDispose.contains(entry)
            ) {
                modalBottomSheetNavigator.onTransitionComplete(entry)
            }
        }
    }
}

@Composable
private fun MutableList<NavBackStackEntry>.PopulateVisibleList(
    backStack: Collection<NavBackStackEntry>
) {
    val isInspecting = LocalInspectionMode.current
    backStack.forEach { entry ->
        DisposableEffect(entry.lifecycle) {
            val observer = LifecycleEventObserver { _, event ->
                // show dialog in preview
                if (isInspecting && !contains(entry)) {
                    add(entry)
                }
                // ON_START -> add to visibleBackStack, ON_STOP -> remove from visibleBackStack
                if (event == Lifecycle.Event.ON_START) {
                    // We want to treat the visible lists as Sets but we want to keep
                    // the functionality of mutableStateListOf() so that we recompose in response
                    // to adds and removes.
                    if (!contains(entry)) {
                        add(entry)
                    }
                }
                if (event == Lifecycle.Event.ON_STOP) {
                    remove(entry)
                }
            }
            entry.lifecycle.addObserver(observer)
            onDispose { entry.lifecycle.removeObserver(observer) }
        }
    }
}

@Composable
private fun rememberVisibleList(
    backStack: Collection<NavBackStackEntry>
): SnapshotStateList<NavBackStackEntry> {
    // show dialog in preview
    val isInspecting = LocalInspectionMode.current
    return remember(backStack) {
        mutableStateListOf<NavBackStackEntry>().also {
            it.addAll(
                backStack.filter { entry ->
                    if (isInspecting) {
                        true
                    } else {
                        entry.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
                    }
                }
            )
        }
    }
}

