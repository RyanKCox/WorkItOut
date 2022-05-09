package com.revature.workitout.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.revature.workitout.ui.theme.WorkItOutTheme
import com.revature.workitout.view.nav.StartNav
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test

class MainMenuComposeTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun MainMenuLogoTest(){
        
        composeTestRule.setContent { 
            WorkItOutTheme {

                val navController = rememberNavController()

                StartNav(navController)
            }
        }

        composeTestRule.onNodeWithContentDescription("Logo").assertIsDisplayed()


    }
    @Test
    fun MainMenuButtonTest(){

        composeTestRule.setContent {
            WorkItOutTheme {

                val navController = rememberNavController()

                StartNav(navController)
            }
        }
        composeTestRule.onNodeWithText("Enter").assertIsDisplayed()

    }
    @Test
    fun MainMenuNavTest(){

        composeTestRule.setContent {
            WorkItOutTheme {

                val navController = rememberNavController()

                StartNav(navController)
            }
        }
        composeTestRule.onNodeWithText("Enter").performClick()
        composeTestRule.onNodeWithText("Workouts").assertIsDisplayed()

    }
}