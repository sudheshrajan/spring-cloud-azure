/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.spring.cloud.autoconfigure.telemetry;

import com.microsoft.applicationinsights.core.dependencies.apachecommons.codec.digest.DigestUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MacAddressHelper {

    private static final String UNKNOWN_MAC = "Unknown-Mac-Address";

    private static final String hashedMacAddress = computeHashedMacAddress();

    private static String computeHashedMacAddress() {
        byte[] macBytes = getMacAddressByNetworkInterface();

        return DigestUtils.sha256Hex(macBytes);
    }

    public static String getHashedMacAddress() {
        return hashedMacAddress;
    }

    private static byte[] getMacAddressByNetworkInterface() {
        try {
            List<NetworkInterface> nis = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nis) {
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    return macBytes;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00".getBytes();
    }
}
