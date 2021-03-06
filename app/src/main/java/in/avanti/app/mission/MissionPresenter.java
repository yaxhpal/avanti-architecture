/*
 * Copyright 2016, The Android Open Source Project
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

package in.avanti.app.mission;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link MissionsFragment}), retrieves the data and updates
 * the UI as required.
 */
public class MissionPresenter implements IMissionContract.Presenter {

    //region Declarations
    @NonNull
    private final IMissionContract.View mMissionView;


    public MissionPresenter(@NonNull IMissionContract.View missionView) {
        mMissionView = checkNotNull(missionView);
    }

    @Override
    public void start() {

    }
}

