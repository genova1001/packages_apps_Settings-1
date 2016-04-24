/*
 * Copyright (C) 2012 The CyanogenMod Project
 * Copyright (C) 2014 SlimRoms Project
 * Copyright (C) 2016 Krexus
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

package com.android.settings.krexus;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.internal.logging.MetricsLogger;
import com.android.settings.krexus.preference.SystemSettingSwitchPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class NotificationLights extends SettingsPreferenceFragment implements OnPreferenceChangeListener {
    private static final String TAG = "NotificationLights";

    private static final String KEY_NOTIFICATION_LIGHT_PULSE = "notification_light_pulse";
    private static final String KEY_CHARGING_BATTERY_LED = "charging_battery_led";
    private static final String KEY_LOW_BATTERY_PULSE = "low_battery_pulse";

    private SystemSettingSwitchPreference mNotificationLightPulse;
    private SystemSettingSwitchPreference mChargingBatteryLed;
    private SystemSettingSwitchPreference mLowBatteryPulse;

    public static final int NOTIFICATION_LIGHT_PULSE_MODE = 1;
    public static final int CHARGING_BATTERY_LED_MODE = 2;
    public static final int LOW_BATTERY_PULSE_MODE = 4;

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.NOTIFICATION;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.notification_lights);

        PreferenceScreen prefSet = getPreferenceScreen();

        mNotificationLightPulse = (SystemSettingSwitchPreference) prefSet.findPreference(KEY_NOTIFICATION_LIGHT_PULSE);
        mChargingBatteryLed = (SystemSettingSwitchPreference) prefSet.findPreference(KEY_CHARGING_BATTERY_LED);
        mLowBatteryPulse = (SystemSettingSwitchPreference) prefSet.findPreference(KEY_LOW_BATTERY_PULSE);

        int mode = Settings.System.getInt(getContentResolver(),
                        Settings.System.NOTIFICATION_LIGHTS_MODE,
                        NOTIFICATION_LIGHT_PULSE_MODE|LOW_BATTERY_PULSE_MODE);

        mNotificationLightPulse.setChecked((mode & NOTIFICATION_LIGHT_PULSE_MODE) != 0);
        mChargingBatteryLed.setChecked((mode & CHARGING_BATTERY_LED_MODE) != 0);
        mLowBatteryPulse.setChecked((mode & LOW_BATTERY_PULSE_MODE) != 0);

        mNotificationLightPulse.setOnPreferenceChangeListener(this);
        mChargingBatteryLed.setOnPreferenceChangeListener(this);
        mLowBatteryPulse.setOnPreferenceChangeListener(this);
   }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mNotificationLightPulse ||
                preference == mChargingBatteryLed ||
                preference == mLowBatteryPulse) {
            int mode = 0;
            if (preference == mNotificationLightPulse && ((Boolean) objValue)
                    || preference != mNotificationLightPulse && mNotificationLightPulse.isChecked()) {
                mode |= NOTIFICATION_LIGHT_PULSE_MODE;
            }
            if (preference == mChargingBatteryLed && ((Boolean) objValue)
                    || preference != mChargingBatteryLed && mChargingBatteryLed.isChecked()) {
                mode |= CHARGING_BATTERY_LED_MODE;
            }
            if (preference == mLowBatteryPulse && ((Boolean) objValue)
                    || preference != mLowBatteryPulse && mLowBatteryPulse.isChecked()) {
                mode |= LOW_BATTERY_PULSE_MODE;
            }
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.NOTIFICATION_LIGHTS_MODE, mode);
            return true;
        }
        return false;
    }

}
