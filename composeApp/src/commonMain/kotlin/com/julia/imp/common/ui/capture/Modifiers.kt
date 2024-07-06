package com.julia.imp.common.ui.capture

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer

fun Modifier.recordAndDraw(graphicsLayer: GraphicsLayer) =
    this.drawWithCache {
        onDrawWithContent {
            graphicsLayer.record {
                this@onDrawWithContent.drawContent()
            }

            drawLayer(graphicsLayer)
        }
    }

fun Modifier.recordOffscreen(graphicsLayer: GraphicsLayer) =
    this.drawWithCache {
        onDrawWithContent {
            graphicsLayer.record {
                this@onDrawWithContent.drawContent()
            }
        }
    }