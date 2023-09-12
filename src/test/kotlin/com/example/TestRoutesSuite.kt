package com.example

import com.example.routes.RecentWatchlistRoutesTest
import com.example.routes.UserRoutesTest
import com.example.routes.WatchlistRoutesTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test suite class that includes all the route tests for the application.
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    UserRoutesTest::class,
    RecentWatchlistRoutesTest::class,
    WatchlistRoutesTest::class
)
class TestRoutesSuite
