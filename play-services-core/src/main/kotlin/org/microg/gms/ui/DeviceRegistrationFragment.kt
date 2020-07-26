/*
 * SPDX-FileCopyrightText: 2020, microG Project Team
 * SPDX-License-Identifier: Apache-2.0
 */

package org.microg.gms.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.google.android.gms.R
import com.google.android.gms.databinding.DeviceRegistrationFragmentBinding
import org.microg.gms.checkin.CheckinPrefs
import org.microg.gms.checkin.CheckinPrefs.PREF_ENABLE_CHECKIN
import org.microg.gms.checkin.TriggerReceiver

class DeviceRegistrationFragment : Fragment(R.layout.device_registration_fragment) {
    private lateinit var binding: DeviceRegistrationFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DeviceRegistrationFragmentBinding.inflate(inflater, container, false)
        binding.switchBarCallback = object : PreferenceSwitchBarCallback {
            override fun onChecked(newStatus: Boolean) {
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_ENABLE_CHECKIN, newStatus).commit()
                if (newStatus) {
                    requireContext().sendOrderedBroadcast(Intent(requireContext(), TriggerReceiver::class.java), null)
                }
                binding.checkinEnabled = newStatus
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            binding.checkinEnabled = CheckinPrefs.get(context).isEnabled
        }
    }
}