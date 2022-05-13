/*
 * Copyright (C) 2021 The Pixel Experience Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.pixelexperience.systemui.dagger;

import com.android.systemui.dagger.DefaultComponentBinder;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.SystemUIModule;

import dagger.Subcomponent;

import com.google.android.systemui.smartspace.KeyguardSmartspaceController;

@SysUISingleton
@Subcomponent(modules = {
        DefaultComponentBinder.class,
        DependencyProvider.class,
        SystemUIGoogleDependencyProvider.class,
        SystemUIGoogleBinder.class,
        SystemUIModule.class,
        SystemUIGoogleModule.class})
public interface SysUIGoogleSysUIComponent extends SysUIComponent {
    @Subcomponent.Builder
    interface Builder extends SysUIComponent.Builder {
        SysUIGoogleSysUIComponent build();
    }

    @SysUISingleton
    KeyguardSmartspaceController createKeyguardSmartspaceController();
}
