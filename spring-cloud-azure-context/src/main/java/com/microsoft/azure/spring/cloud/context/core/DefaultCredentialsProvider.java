/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.microsoft.azure.spring.cloud.context.core;

import com.google.common.base.Strings;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * A {@link CredentialsProvider} implementation that provides credentials based on
 * user-provided properties and defaults.
 *
 * @author Warren Zhu
 */
public class DefaultCredentialsProvider implements CredentialsProvider {

    private static final Log LOGGER = LogFactory.getLog(DefaultCredentialsProvider.class);

    private ApplicationTokenCredentials credentials;

    public DefaultCredentialsProvider(CredentialSupplier supplier) {
        initCredentials(supplier);
    }

    private void initCredentials(CredentialSupplier supplier) {
        if (!Strings.isNullOrEmpty(supplier.getCredentialFilePath())) {
            try {
                File credentialFile = new ClassPathResource(supplier.getCredentialFilePath()).getFile();
                this.credentials = ApplicationTokenCredentials.fromFile(credentialFile);
            } catch (IOException e) {
                LOGGER.error("Credential file path not found.", e);
            }
        } else {
            throw new RuntimeException("No credentials provided.");
        }
    }

    @Override
    public ApplicationTokenCredentials getCredentials() {
        return this.credentials;
    }
}
