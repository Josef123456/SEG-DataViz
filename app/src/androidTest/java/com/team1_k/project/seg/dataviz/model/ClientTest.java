package com.team1_k.project.seg.dataviz.model;

import android.test.MoreAsserts;

import junit.framework.TestCase;

/**
 * Created by alexstoick on 11/19/14.
 */
public class ClientTest extends TestCase {
    public void testConstructor() throws Exception {
        Client testClient = new Client(Client.Type.INVESTOR);

        MoreAsserts.assertEquals(testClient.getType().getInterests(),
                                    new String[]{"GDP", "UNEMPLOYMENT"});
    }
}
