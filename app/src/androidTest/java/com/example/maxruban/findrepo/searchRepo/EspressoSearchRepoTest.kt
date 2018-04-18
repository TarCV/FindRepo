package com.example.maxruban.findrepo.searchRepo

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.web.assertion.WebViewAssertions.webContent
import android.support.test.espresso.web.assertion.WebViewAssertions.webMatches
import android.support.test.espresso.web.matcher.DomMatchers.hasElementWithId
import android.support.test.espresso.web.model.Atoms.getCurrentUrl
import android.support.test.espresso.web.sugar.Web.onWebView
import android.support.test.espresso.web.webdriver.DriverAtoms.findElement
import android.support.test.espresso.web.webdriver.DriverAtoms.getText
import android.support.test.espresso.web.webdriver.Locator
import org.junit.Rule
import com.example.maxruban.findrepo.MainActivity
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.example.maxruban.findrepo.R
import com.example.maxruban.findrepo.R.id.activity_search_result
import com.example.maxruban.findrepo.R.id.repoTextView
import com.example.maxruban.findrepo.SearchResultActivity
import org.hamcrest.CoreMatchers.*
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.test.uiautomator.UiSelector
import com.example.maxruban.findrepo.OkHttpProvider
import com.jakewharton.espresso.OkHttp3IdlingResource
import org.junit.Assert
import org.junit.Before
import java.util.regex.Pattern


/**
 * Created by maxruban on 4/1/18.
 */



open class EspressoSearchRepoTest<WebViewActivity : Activity?>
    : BaseTest()
{


    @Rule


    @JvmField


    val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)





    @Test

    fun verifyAppFoundRepoByWord(){
        val okhttpIdlingResource = OkHttp3IdlingResource.create("OkHttp", OkHttpProvider.okHttpClient)

        IdlingRegistry.getInstance().register(okhttpIdlingResource)

        onView(withId(R.id.searchEditText))
                .perform(typeText("code"));
        onView(withId(R.id.searchButton))
                .perform(click())
        onData(anything())
                .inAdapterView(allOf(withId(R.id.repoListView)))
                .atPosition(0)
                .check(matches(isDisplayed()))

        IdlingRegistry.getInstance().unregister(okhttpIdlingResource)
    }

    @Test
    fun verifyAppFoundUsersRepoByName(){

        onView(withId(R.id.userRepoEditText))
                .perform(typeText("maxruban"));
        onView(withId(R.id.userRepoButton))
                .perform(click())
        Thread.sleep(3000)
        onData(anything())
                .inAdapterView(allOf(withId(R.id.repoListView)))
                .atPosition(0)

                .perform(ViewActions.click())
        Thread.sleep(3000)
                val url = uiDevice.findObject(UiSelector().resourceId("com.android.chrome:id/url_bar")).text


        val isMatch = Pattern.matches("https://github.com.*maxruban.*", url)
        if (!isMatch){
            Assert.fail()
        }
    }



}
