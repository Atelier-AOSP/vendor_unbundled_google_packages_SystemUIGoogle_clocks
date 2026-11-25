/*
 * Copyright (C) 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.clocks

import android.view.View
import android.view.ViewGroup
import com.android.systemui.clocks.controller.ClockLayerController
import com.android.systemui.clocks.view.DigitalFaceViewGroup
import com.android.systemui.customization.clocks.AnimationState
import com.android.systemui.customization.clocks.ClockContext
import com.android.systemui.customization.clocks.ClockContextImpl
import com.android.systemui.customization.clocks.TypefaceCache
import com.android.systemui.log.core.MessageBuffer
import com.android.systemui.plugins.keyguard.ui.clocks.ClockAssets
import com.android.systemui.plugins.keyguard.ui.clocks.ClockFaceLayout

interface TypeFactory {
    fun createViewGroup(
        clockCtx: CustomClockContext,
        face: ClockFace,
        isLargeClock: Boolean,
    ): ViewGroup

    fun createComposedView(
        clockCtx: CustomClockContext,
        layer: ComposedDigitalHandLayer,
        dozeState: AnimationState,
    ): DigitalFaceViewGroup<*>

    fun createLayerController(
        clockCtx: CustomClockContext,
        layer: ClockLayer,
        isLargeClock: Boolean,
    ): ClockLayerController

    fun createLayout(clockCtx: CustomClockContext, face: ClockFace, view: View): ClockFaceLayout
}

// Stock also keeps a listener list here; R8 stripped everything that used it, so its element
// type can't be recovered and it is left out.
class ClockAssetSupplier(private var assets: ClockAssets)

class CustomClockContext(
    val factory: TypeFactory,
    val assetLoader: AssetLoader,
    val typefaceCache: TypefaceCache<FontSpec>,
    private val assetSupplier: ClockAssetSupplier,
    private val innerCtx: ClockContextImpl,
) : ClockContext by innerCtx {
    fun copy(messageBuffer: MessageBuffer): CustomClockContext {
        return CustomClockContext(
            factory,
            assetLoader.copy(messageBuffer = messageBuffer),
            typefaceCache,
            assetSupplier,
            innerCtx.copy(messageBuffer = messageBuffer),
        )
    }
}
