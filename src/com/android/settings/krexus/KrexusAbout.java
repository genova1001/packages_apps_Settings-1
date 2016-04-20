package com.android.settings.krexus;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;

import com.android.internal.logging.MetricsLogger;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class KrexusAbout extends SettingsPreferenceFragment {

    private static final String PROPERTY_KREXUS_MAINTAINER = "ro.krexus.maintainer";
    private static final String PROPERTY_KREXUS_MAINTAINER_INFO = "ro.krexus.maintainer.info";
    private static final String SCREEN_KREXUS_ABOUT = "screen_krexus_about";
    private static final String CATEGORY_KREXUS_MAINTAINER = "category_krexus_about_maintainer";
    private static final String KEY_KREXUS_MAINTAINER = "krexus_about_maintainer";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.krexus_about);
	getActivity().getActionBar().setTitle("About Krexus");

        final PreferenceScreen krexusAboutScreen = (PreferenceScreen)
                findPreference(SCREEN_KREXUS_ABOUT);
        final PreferenceGroup krexusMaintainerCategory = (PreferenceGroup)
                findPreference(CATEGORY_KREXUS_MAINTAINER);
        final Preference krexusMaintainerPref = (Preference)
                findPreference(KEY_KREXUS_MAINTAINER);

        if ((SystemProperties.get(PROPERTY_KREXUS_MAINTAINER).equals(""))
                || (SystemProperties.get(PROPERTY_KREXUS_MAINTAINER_INFO).equals(""))) {
            krexusAboutScreen.removePreference(krexusMaintainerCategory);
        } else {
            krexusMaintainerPref.setTitle(SystemProperties
                    .get(PROPERTY_KREXUS_MAINTAINER).replaceAll("_"," "));
            krexusMaintainerPref.setSummary(SystemProperties
                    .get(PROPERTY_KREXUS_MAINTAINER_INFO).replaceAll("_"," "));
        }
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.APPLICATION;
    }
}
