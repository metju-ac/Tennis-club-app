package com.klima.matej.tennis_reservations_system.service;

import com.klima.matej.tennis_reservations_system.entity.Court;
import com.klima.matej.tennis_reservations_system.entity.Surface;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourtServiceImplTest {
    private final CourtService courtService = new CourtServiceImpl();
    private static Surface SURFACE;

    @BeforeAll
    public static void setUpSurface() {
        SurfaceService surfaceService = new SurfaceServiceImpl();

        Surface surface = new Surface();
        surface.setName("Court Test Surface");
        surface.setMinutePrice(100);
        surfaceService.save(surface);

        SURFACE = surface;
    }

    @AfterAll
    public static void tearDownSurface() {
        SurfaceService surfaceService = new SurfaceServiceImpl();
        surfaceService.deleteById(SURFACE.getId());
    }

    private Court saveCourt(String name) {
        Court court = new Court();
        court.setName(name);
        court.setSurface(SURFACE);

        courtService.save(court);

        return court;
    }

    @Test
    public void testSaveCourt() {
        Court court = new Court();
        court.setName("Test Court 1");
        court.setSurface(SURFACE);

        Court saved = courtService.save(court);

        assertNotNull(saved);
        assertNotEquals(court.getId(), 0);

        courtService.deleteById(court.getId());
    }

    @Test
    public void testDeleteByIdCourt() {
        Court court = saveCourt("Test Court 2");

        boolean deleted = courtService.deleteById(court.getId());
        assertTrue(deleted);

        Court foundCourt = courtService.findById(court.getId());
        assertNull(foundCourt);
    }

    @Test
    public void testFindAllCourt() {
        Court court1 = saveCourt("Test Court 3");
        Court court2 = saveCourt("Test Court 4");

        assertEquals(2, courtService.findAll().size());

        courtService.deleteById(court1.getId());
        courtService.deleteById(court2.getId());
    }

    @Test
    public void testFindByIdCourt() {
        Court court = saveCourt("Test Court 5");
        Court foundCourt = courtService.findById(court.getId());
        assertEquals(court, foundCourt);

        courtService.deleteById(court.getId());
    }

    @Test
    public void testFindByIdCourtNotFound() {
        Court foundCourt = courtService.findById(-1);
        assertNull(foundCourt);
    }

    @Test
    public void testUpdateCourt() {
        Court court = saveCourt("Test Court 6");
        court.setName("Test Court 7");

        Court updated = courtService.update(court);
        assertNotNull(updated);

        Court foundCourt = courtService.findById(court.getId());
        assertEquals(court, foundCourt);

        courtService.deleteById(court.getId());
    }

    @Test
    public void testCourtEmptyReservations() {
        Court court = saveCourt("Test Court 8");
        assertEquals(0, court.getReservations().size());

        courtService.deleteById(court.getId());
    }
}