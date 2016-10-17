/*
 * Copyright (C) 2016 Krexus ROM
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

package com.android.settings.krexus;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.view.View;

import com.android.internal.logging.MetricsLogger;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QSPanelSettings extends SettingsPreferenceFragment implements 
	OnPreferenceChangeListener, Indexable {

    private static final String PREF_QS_COLUMNS = "sysui_qs_num_columns";

    private ListPreference mNumColumns;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.qs_panel_settings);

        ContentResolver resolver = getActivity().getContentResolver();

        mNumColumns = (ListPreference) findPreference(PREF_QS_COLUMNS);
        int numColumns = Settings.Secure.getIntForUser(resolver,
            Settings.Secure.QS_NUM_TILE_COLUMNS, getDefaultNumColums(),
            UserHandle.USER_CURRENT);
        mNumColumns.setValue(String.valueOf(numColumns));
        updateNumColumnsSummary(numColumns);
        mNumColumns.setOnPreferenceChangeListener(this);
    }
    
    @Override
    protected int getMetricsCategory()
    {
	return MetricsLogger.DONT_TRACK_ME_BRO;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mNumColumns) {
            int numColumns = Integer.valueOf((String) newValue);
            Settings.Secure.putIntForUser(resolver, Settings.Secure.QS_NUM_TILE_COLUMNS,
                numColumns, UserHandle.USER_CURRENT);
            updateNumColumnsSummary(numColumns);
            return true;
        }
        return false;
    }

    private void updateNumColumnsSummary(int numColumns) {
        String prefix = (String) mNumColumns.getEntries()[mNumColumns.findIndexOfValue(String.valueOf(numColumns))];
        mNumColumns.setSummary(getActivity().getResources().getString(R.string.qs_num_columns_showing, prefix));
    }

    private int getDefaultNumColums() {
        try {
            Resources res = getActivity().getPackageManager().getResourcesForApplication("com.android.systemui");
            int val = res.getInteger(res.getIdentifier("quick_settings_num_columns", "integer", "com.android.systemui")); 
            // This better not be larger than 5, that's as high as the list goes atm
            return Math.max(1, val);
        } catch (Exception e) {
            return 3;
        }
    }
}
