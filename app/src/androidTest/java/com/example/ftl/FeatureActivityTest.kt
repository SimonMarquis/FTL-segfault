package com.example.ftl

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import java.util.*


@HiltAndroidTest
class FeatureActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityScenario = activityScenarioRule<FeatureActivity>()

    private val state = MutableLiveData<State>()

    @BindValue
    @JvmField
    val viewModel: FeatureViewModel = mockk(relaxed = true) {
        every { state } returns this@FeatureActivityTest.state
    }

    @Test fun test0() = test()
    @Test fun test1() = test()
    @Test fun test2() = test()
    @Test fun test3() = test()
    @Test fun test4() = test()
    @Test fun test5() = test()
    @Test fun test6() = test()
    @Test fun test7() = test()
    @Test fun test8() = test()
    @Test fun test9() = test()

    private fun test() {
        onView(withId(android.R.id.text1)).check(matches(isDisplayed()))
        val id = UUID.randomUUID()
        state.postValue(State(id))
        onView(withId(android.R.id.text1)).check(matches(withText(id.toString())))
    }
}
