package com.example

import RecentWatchlistDAOImplTest
import ValidationTests
import com.example.repository.UserDAOImplTest
import com.example.repository.WatchlistDAOImplTest
import com.example.services.RecentWatchlistServiceTest
import com.example.services.UserServiceTest
import com.example.services.WatchlistServiceTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test suite class that includes all the unit tests for the application.
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    WatchlistDAOImplTest::class,
    RecentWatchlistDAOImplTest::class,
    UserDAOImplTest::class,
    UserServiceTest::class,
    RecentWatchlistServiceTest::class,
    WatchlistServiceTest::class,
    ValidationTests::class
)
class TestSuite
