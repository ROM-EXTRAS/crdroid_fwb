/*
 * Copyright (C) 2015 The Resurrection Remix Project
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

package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.UserHandle;
import android.service.quicksettings.Tile;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;



import com.android.systemui.plugins.qs.QSTile.BooleanState;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.lineageos.internal.logging.LineageMetricsLogger;
import lineageos.providers.LineageSettings;

import javax.inject.Inject;


public class GestureAnywhereTile extends QSTileImpl<BooleanState> {
    private boolean mListening;
    private GestureObserver mObserver;

    private static final Intent GESTURE_ANYWHERE_SETTINGS =
	    new Intent("org.lineageos.lineageparts.GESTURE_ANYWHERE_SETTINGS");

    @Inject
    public GestureAnywhereTile (QSHost host) {
        super(host);
        mObserver = new GestureObserver(mHandler);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }


    @Override
    public int getMetricsCategory() {
        return LineageMetricsLogger.GESTURE_ANYWHERE;
    }

    @Override
    protected void handleClick() {
        toggleState();
        refreshState();
    }

/*     @Override
    protected void handleSecondaryClick() {
       // mHost.startActivityDismissingKeyguard(GESTURE_SETTINGS);
    }

    @Override
    public void handleLongClick() {
       // mHost.startActivityDismissingKeyguard(GESTURE_SETTINGS);
    }
*/
    protected void toggleState() {
        LineageSettings.System.putInt(mContext.getContentResolver(),
            LineageSettings.System.GESTURE_ANYWHERE_ENABLED, !isGestureEnabled() ? 1 : 0);
    }

    @Override
    public CharSequence getTileLabel() {
        return mContext.getString(R.string.quick_settings_gesture_anywhere_label);
    }

    @Override
    public Intent getLongClickIntent() {
        // TODO Auto-generated method stub
        return GESTURE_ANYWHERE_SETTINGS;
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.icon = ResourceIcon.get(R.drawable.ic_qs_gestureanywhere);
        if (isGestureEnabled()) {
            state.label = mContext.getString(R.string.quick_settings_gesture_anywhere_on);
            state.state = Tile.STATE_ACTIVE;
        } else {
            state.label = mContext.getString(R.string.quick_settings_gesture_anywhere_off);
            state.state = Tile.STATE_INACTIVE;
        }
    }

    private boolean isGestureEnabled() {
        return LineageSettings.System.getIntForUser(mContext.getContentResolver(),
                LineageSettings.System.GESTURE_ANYWHERE_ENABLED, 0,
                UserHandle.USER_CURRENT) == 1;
    }

    @Override
    public void handleSetListening(boolean listening) {
        if (mListening == listening) return;
            mListening = listening;
        if (listening) {
            mObserver.startObserving();
        } else {
            mObserver.endObserving();
        }
    }

    private class GestureObserver extends ContentObserver {
        public GestureObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            refreshState();
        }

        public void startObserving() {
            mContext.getContentResolver().registerContentObserver(
                    LineageSettings.System.getUriFor(LineageSettings.System.GESTURE_ANYWHERE_ENABLED),
                    false, this, UserHandle.USER_ALL);
        }

        public void endObserving() {
            mContext.getContentResolver().unregisterContentObserver(this);
        }
    }
}

