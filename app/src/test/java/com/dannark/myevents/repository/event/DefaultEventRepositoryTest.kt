package com.dannark.myevents.repository.event

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dannark.myevents.domain.Event
import com.dannark.myevents.network.CheckInNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class DefaultEventRepositoryTest {
    private val event1 = Event(
        date= 1534784400,
        description= "description1",
        image= "http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png",
        longitude= "-51.2146267",
        latitude= "-30.0392981",
        price= 29.99.toLong(),
        title= "title1",
        id= "1"
    )

    private val event2 = Event(
        date= 1534784400,
        description= "description2",
        image= "https://fm103.com.br/wp-content/uploads/2017/07/campanha-do-agasalho-balneario-camboriu-2016.jpg",
        longitude= "-51.2148497",
        latitude= "-30.037878",
        price= 59.99.toLong(),
        title= "title2",
        id= "2"
    )
    private val event3 = Event(
        date= 1534784400,
        description= "description3",
        image= "http://www.fernaogaivota.com.br/documents/10179/1665610/feira-troca-de-livros.jpg",
        longitude= "-51.2148497",
        latitude= "-30.037878",
        price= 100L,
        title= "title3",
        id= "3"
    )
    private val event4 = Event(
        date= 1534784400,
        description= "description4",
        image= "http://www.fernaogaivota.com.br/documents/10179/1665610/feira-troca-de-livros.jpg",
        longitude= "-51.2148497",
        latitude= "-30.037878",
        price= 100L,
        title= "title4",
        id= "4"
    )
    private val user1Checkin = CheckInNetwork("1","Daniel","dan@mail.com")
    private val user2Checkin = CheckInNetwork("2","simulate_fail_test","null") // should fail

    private val remoteList = listOf(event1, event2).sortedBy { it.id }
    private val localList = listOf(event3).sortedBy { it.id }
    private val newListFromServer = listOf(event4).sortedBy { it.id }

    private lateinit var eventRemoteDataSource: EventsDataSource
    private lateinit var eventLocalDataSource: EventsDataSource
    private lateinit var eventRepository: DefaultEventRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createRepository(){
        eventRemoteDataSource =
            FakeDataSource(remoteList.toMutableList())
        eventLocalDataSource =
            FakeDataSource(localList.toMutableList())

        eventRepository = DefaultEventRepository(
            eventRemoteDataSource, eventLocalDataSource, Dispatchers.Unconfined
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getEvents_requestAllEventsFromLocalDataSource() = runBlockingTest {
        val events = eventRepository.events.value
        assertThat(events, IsEqual(localList))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun refreshEvents_updateAllEventsLocallyFromRemoteDataSource() = runBlockingTest {
        eventRepository.refreshEvents()
        val events = eventRepository.events.value
        assertThat(events, IsEqual(remoteList))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun refreshEvents_removeOldElementsLocallyFromRemoteDataSource() = runBlockingTest {
        //Compare the actual List with the new, ther result should be only items from the new list
        eventRepository.findAndDeleteOldEvents(newListFromServer)
        val events = eventRepository.events.value
        assertThat(events, `is`(listOf()))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun postCheckIn_sendDataToRemoteDataSource_IsSuccessful() = runBlockingTest {
        val isSuccess = eventRepository.postCheckIn(user1Checkin)

        assertThat(isSuccess, `is`(true))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun postCheckIn_sendDataToRemoteDataSource_FailsProperly() = runBlockingTest {
        val isSuccess = eventRepository.postCheckIn(user2Checkin)
        assertThat(isSuccess, `is`(false))
    }
}