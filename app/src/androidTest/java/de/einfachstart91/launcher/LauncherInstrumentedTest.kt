package de.einfachstart91.launcher

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LauncherInstrumentedTest {
    private val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val ruleChain: RuleChain = RuleChain
        .outerRule(
            GrantPermissionRule.grant(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
            ),
        )
        .around(composeRule)

    @Test
    fun startbildschirmZeigtAlleWichtigenElemente() {
        composeRule.waitForIdle()
        composeRule.onNodeWithText("EINFACHSTART 91").assertIsDisplayed()
        composeRule.onNodeWithText("MEINE LIEBSTEN").assertIsDisplayed()
        composeRule.onNodeWithText("TELEFON").assertIsDisplayed()
        composeRule.onNodeWithText("NACHRICHTEN").assertIsDisplayed()
        composeRule.onNodeWithText("NOTFALL – HILFE RUFEN").assertIsDisplayed()
        composeRule.onNodeWithText("KONTAKTE").assertIsDisplayed()
    }

    @Test
    fun kontaktSucheZeigtAlleKontakteAn() {
        composeRule.waitForIdle()
        composeRule.onNodeWithText("KONTAKTE").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("ALLE KONTAKTE DURCHSUCHEN").assertIsDisplayed()
        composeRule.onNodeWithText("KONTAKT ANRUFEN").assertIsDisplayed()
    }
}
