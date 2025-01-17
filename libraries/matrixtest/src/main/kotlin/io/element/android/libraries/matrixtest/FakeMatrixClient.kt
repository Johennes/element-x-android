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

package io.element.android.libraries.matrixtest

import io.element.android.libraries.matrix.MatrixClient
import io.element.android.libraries.matrix.core.RoomId
import io.element.android.libraries.matrix.core.SessionId
import io.element.android.libraries.matrix.core.UserId
import io.element.android.libraries.matrix.media.MediaResolver
import io.element.android.libraries.matrix.room.MatrixRoom
import io.element.android.libraries.matrix.room.RoomSummaryDataSource
import io.element.android.libraries.matrixtest.media.FakeMediaResolver
import io.element.android.libraries.matrixtest.room.FakeMatrixRoom
import io.element.android.libraries.matrixtest.room.InMemoryRoomSummaryDataSource
import org.matrix.rustcomponents.sdk.MediaSource

class FakeMatrixClient(override val sessionId: SessionId) : MatrixClient {

    override fun getRoom(roomId: RoomId): MatrixRoom? {
        return FakeMatrixRoom(roomId)
    }

    override fun startSync() = Unit

    override fun stopSync() = Unit

    override fun roomSummaryDataSource(): RoomSummaryDataSource {
        return InMemoryRoomSummaryDataSource()
    }

    override fun mediaResolver(): MediaResolver {
        return FakeMediaResolver()
    }

    override suspend fun logout() = Unit

    override fun userId(): UserId = UserId("")

    override suspend fun loadUserDisplayName(): Result<String> {
        return Result.success("")
    }

    override suspend fun loadUserAvatarURLString(): Result<String> {
        return Result.success("")
    }

    override suspend fun loadMediaContentForSource(source: MediaSource): Result<ByteArray> {
        return Result.success(ByteArray(0))
    }

    override suspend fun loadMediaThumbnailForSource(source: MediaSource, width: Long, height: Long): Result<ByteArray> {
        return Result.success(ByteArray(0))
    }

    override fun close() = Unit
}
