package es.adri.generadorwallpapersjetpackcompose.ui.optionselector.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import es.adri.generadorwallpapersjetpackcompose.ui.optionselector.ui.OptionSelectorViewModel

class OptionSelectorViewModelProvider : PreviewParameterProvider<OptionSelectorViewModel> {
    override val values = sequenceOf(
        OptionSelectorViewModel().apply {
        }
    )
}