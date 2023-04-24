package com.klima.matej.tennis_reservations_system.service;

import com.klima.matej.tennis_reservations_system.entity.Surface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SurfaceServiceImplTest {
    private final SurfaceService surfaceService = new SurfaceServiceImpl();

    private Surface saveSurface(String name, int minutePrice) {
        Surface surface = new Surface();
        surface.setName(name);
        surface.setMinutePrice(minutePrice);
        surfaceService.save(surface);

        return surface;
    }

    @Test
    public void testSaveSurface() {
        Surface surface = new Surface();
        surface.setName("Test Surface 1");
        surface.setMinutePrice(100);

        Surface saved = surfaceService.save(surface);

        assertNotNull(saved);
        assertNotEquals(surface.getId(), 0);

        surfaceService.deleteById(surface.getId());
    }

    @Test
    public void testDeleteByIdSurface() {
        Surface surface = saveSurface("Test Surface 2", 100);
        boolean deleted = surfaceService.deleteById(surface.getId());

        assertTrue(deleted);

        Surface foundSurface = surfaceService.findById(surface.getId());
        assertNull(foundSurface);
    }

    @Test
    public void testSaveSurfaceNullName() {
        Surface surface = new Surface();
        surface.setMinutePrice(100);

        assertThrows(IllegalStateException.class, () -> surfaceService.save(surface));
    }

    @Test
    public void testSaveSurfaceDuplicateName() {
        Surface surface = saveSurface("Test Surface 3", 100);
        assertThrows(IllegalStateException.class, () -> saveSurface("Test Surface 3", 200));

        surfaceService.deleteById(surface.getId());
    }

    @Test
    public void testFindAllSurface() {
        Surface surface1 = saveSurface("Test Surface 4", 100);
        Surface surface2 = saveSurface("Test Surface 5", 200);

        assertEquals(2, surfaceService.findAll().size());

        surfaceService.deleteById(surface1.getId());
        surfaceService.deleteById(surface2.getId());
    }

    @Test
    public void testFindByIdSurface() {
        Surface surface = saveSurface("Test Surface 6", 100);
        Surface foundSurface = surfaceService.findById(surface.getId());
        assertEquals(surface, foundSurface);

        surfaceService.deleteById(surface.getId());
    }

    @Test
    public void testFindByIdSurfaceNotFound() {
        Surface foundSurface = surfaceService.findById(-1);
        assertNull(foundSurface);
    }

    @Test
    public void findByNameSurface() {
        Surface surface = saveSurface("Test Surface 7", 100);
        Surface foundSurface = surfaceService.findByName(surface.getName());
        assertEquals(surface, foundSurface);

        surfaceService.deleteById(surface.getId());
    }

    @Test
    public void findByNameSurfaceNotFound() {
        Surface foundSurface = surfaceService.findByName("Does not exist");
        assertNull(foundSurface);
    }

    @Test
    public void testUpdateSurface() {
        Surface surface = saveSurface("Test Surface 8", 100);
        surface.setName("Test Surface 9");
        surface.setMinutePrice(200);

        Surface updated = surfaceService.update(surface);
        assertNotNull(updated);

        Surface foundSurface = surfaceService.findById(surface.getId());
        assertEquals(surface, foundSurface);

        surfaceService.deleteById(surface.getId());
    }

    @Test
    public void testUpdateSurfaceDuplicateName() {
        Surface surface1 = saveSurface("Test Surface 10", 100);
        Surface surface2 = saveSurface("Test Surface 11", 200);

        surface2.setName("Test Surface 10");
        Surface updated = surfaceService.update(surface2);
        assertNull(updated);

        surfaceService.deleteById(surface1.getId());
        surfaceService.deleteById(surface2.getId());
    }

    @Test
    public void testSurfaceEmptyCourts() {
        Surface surface = saveSurface("Test Surface 12", 100);
        assertEquals(0, surface.getCourts().size());

        surfaceService.deleteById(surface.getId());
    }
}