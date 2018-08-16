/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.example;

public class UserMessage {

    private String body;

    private String username;


    public UserMessage() {
    }

    public UserMessage(String body, String username) {
        this.body = body;
        this.username = username;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserMessage{" + "body='" + body + '\'' + ", username='" + username + '\'' + '}';
    }
}
