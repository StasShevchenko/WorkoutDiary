package com.example.workoutdiary.presentation.home_screen

import com.example.workoutdiary.R
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.chart.segment.SegmentProperties
import com.patrykandpatrick.vico.core.component.OverlayingComponent
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.views.dimensions.dimensionsOf

fun getMarker() : MarkerComponent{
    val labelBackgroundShape = MarkerCorneredShape(all = Corner.FullyRounded)
    val label = textComponent {
        this.color = -1
        this.padding = dimensionsOf(horizontalDp = 8f, verticalDp = 4f)
        this.background = ShapeComponent(
            shape = labelBackgroundShape,
            color = -1
        )
            .setShadow(
                radius = 4f,
                dy = 2f,
                applyElevationOverlay = true
            )
    }
    val indicatorInner =
        ShapeComponent(shape = Shapes.pillShape, color = R.color.purple_500)
    val indicatorCenter =
        ShapeComponent(shape = Shapes.pillShape, color = R.color.white)
    val indicatorOuter = ShapeComponent(shape = Shapes.pillShape, color = R.color.white)

    val indicator = OverlayingComponent(
        outer = indicatorOuter,
        innerPaddingAllDp = 10f,
        inner = OverlayingComponent(
            outer = indicatorCenter,
            inner = indicatorInner,
            innerPaddingAllDp = 5f
        ),
    )
    val guideline = LineComponent(
        color = R.color.white,
        thicknessDp = 2f,
        shape = DashedShape(
            shape = Shapes.pillShape,
            dashLengthDp = 8f,
            gapLengthDp = 4f
        )
    )
    val marker = object : MarkerComponent(label, indicator, guideline){
        override fun getInsets(
            context: MeasureContext,
            outInsets: Insets,
            segmentProperties: SegmentProperties
        ) = with(context) {
            outInsets.top =
                label.getHeight(this) + labelBackgroundShape.tickSizeDp.pixels +
                        4f.pixels * 1.3f - 2f.pixels
        }
    }
    marker.indicatorSizeDp = 30f
    marker.onApplyEntryColor = {
            entryColor ->
        indicatorOuter.color = entryColor.copyColor(alpha = 32)
        with(indicatorCenter) {
            color = entryColor
            setShadow(radius = 12f, color = entryColor)
        }
    }
    return marker
}