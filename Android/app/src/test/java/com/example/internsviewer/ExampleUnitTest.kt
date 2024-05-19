package com.example.internsviewer

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class InternApiServiceTest {

    @Mock
    private lateinit var internApiService: InternApiService

    @Mock
    private lateinit var getInternsCall: Call<List<Intern>>

    @Mock
    private lateinit var createInternCall: Call<Void>

    @Mock
    private lateinit var deleteInternCall: Call<Void>

    @Test
    fun `getInterns should return list of interns`() = runBlocking {
        // Mock response
        val mockInternList = listOf(Intern(1, "John", "Doe", 5000.0, "Jane"))
        `when`(internApiService.getInterns()).thenReturn(getInternsCall)
        `when`(getInternsCall.execute()).thenReturn(Response.success(mockInternList))

        // Call the method
        val response = internApiService.getInterns().execute()
        val interns = response.body()

        // Verify and assert
        verify(internApiService).getInterns()
        assertEquals(1, interns?.size)
        assertEquals(1, interns?.get(0)?.id)
        assertEquals("John", interns?.get(0)?.name)
        assertEquals("Doe", interns?.get(0)?.surname)
        assertEquals(5000.0, interns?.get(0)?.amount)
        assertEquals("Jane", interns?.get(0)?.boss)
    }

    @Test
    fun `createIntern should return success`() = runBlocking {
        val intern = Intern(0, "John", "Doe", 5000.0, "Jane")
        `when`(internApiService.createIntern(intern)).thenReturn(createInternCall)
        `when`(createInternCall.execute()).thenReturn(Response.success(null))

        // Call the method
        val response = internApiService.createIntern(intern).execute()

        // Verify and assert
        verify(internApiService).createIntern(intern)
        assertTrue(response.isSuccessful)
    }

    @Test
    fun `deleteIntern should return success`() = runBlocking {
        `when`(internApiService.deleteIntern(1)).thenReturn(deleteInternCall)
        `when`(deleteInternCall.execute()).thenReturn(Response.success(null))

        // Call the method
        val response = internApiService.deleteIntern(1).execute()

        // Verify and assert
        verify(internApiService).deleteIntern(1)
        assertTrue(response.isSuccessful)
    }
}