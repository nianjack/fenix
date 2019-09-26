/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Before
import org.junit.After
import org.junit.Ignore
import org.junit.Test
import org.mozilla.fenix.helpers.AndroidAssetDispatcher
import org.mozilla.fenix.helpers.HomeActivityTestRule
import org.mozilla.fenix.helpers.TestAssetHelper
import org.mozilla.fenix.ui.robots.dismissTrackingOnboarding
import org.mozilla.fenix.ui.robots.homeScreen
import org.mozilla.fenix.ui.robots.navigationToolbar

/**
 *  Tests for verifying basic functionality of history
 *
 */

class CollectionTest {
    /* ktlint-disable no-blank-line-before-rbrace */ // This imposes unreadable grouping.
    private val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private lateinit var mockWebServer: MockWebServer

    @get:Rule
    val activityTestRule = HomeActivityTestRule()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            setDispatcher(AndroidAssetDispatcher())
            start()
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }


    @Ignore
    @Test
    // open a webpage, and add currently opened tab to existing collection
    fun AddTabToCollectionTest() {
        // Open a webpage and save to collection "testcollection_1"

        // On homeview, open another webpage

        // Save the current page to the testcollection_1

        // On homeview, open the first saved page

        // On homeview, open the second saved page

    }
    /*
        @Ignore
        @Test
        // Rename Collection from the Homescreen
        fun RenameCollectionTest() {
            // Open a webpage and save to collection "testcollection_1"

            // On homeview, tap the 3-dot button to expand, select rename

            // Rename collection, save

            // Verify the new name is displayed on homeview

        }

        @Ignore
        @Test
        // Delete Collection from the Homescreen
        fun DeleteCollectionTest() {

        }
    */
    // Open 2 webpages, and save each of them to a single collection
    @Test
    fun createCollectionTest() {
        val firstWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 1)
        val secondWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 2)

        // Open a webpage and save to collection "testcollection_1"
        navigationToolbar {
        }.enterURLAndEnterToBrowser(firstWebPage.url) {
            verifyPageContent(firstWebPage.content)
        }
        navigationToolbar {
        }.openThreeDotMenu {
            // click save to collection menu item, type collection name
            clickSaveCollection()
        }.typeCollectionName("testcollection_1") {
            waitForCollectionSavedPopup()
            mDevice.pressBack();    // go to main page
        }

        // Open a different webpage and save to collection "testcollection_2"
        navigationToolbar {
        }.enterURLAndEnterToBrowser(secondWebPage.url) {
            verifyPageContent(secondWebPage.content)
        }
        navigationToolbar {
        }.openThreeDotMenu {
            // click save to collection menu item, type collection name
            clickSaveCollection()
            clickAddNewCollection()
        }.typeCollectionName("testcollection_2") {
            waitForCollectionSavedPopup()
            mDevice.pressBack();    // go to main page
        }

        // On the main screen, swipe to bottom until the collections are shown
        homeScreen {
            // swipe to bottom until the collections are shown
            verifyHomeScreen()
            scrollToElementByText("testcollection_1")
            Espresso.onView(ViewMatchers.withText("testcollection_2"))
                .check(ViewAssertions
                    .matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
            Espresso.onView(ViewMatchers.withText("testcollection_1"))
                .check(ViewAssertions
                    .matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        }
    }
}
