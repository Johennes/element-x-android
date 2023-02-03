/*
 * Copyright (c) 2023 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.element.android.features.messages.timeline.factories

import io.element.android.features.messages.timeline.model.content.TimelineItemContent
import io.element.android.features.messages.timeline.model.content.TimelineItemRedactedContent
import io.element.android.features.messages.timeline.model.content.TimelineItemUnknownContent
import org.matrix.rustcomponents.sdk.TimelineItemContentKind
import javax.inject.Inject

class TimelineItemContentProfileChangeFactory @Inject constructor() {

    fun create(kind: TimelineItemContentKind.ProfileChange): TimelineItemContent {
        return TimelineItemUnknownContent
    }
}
