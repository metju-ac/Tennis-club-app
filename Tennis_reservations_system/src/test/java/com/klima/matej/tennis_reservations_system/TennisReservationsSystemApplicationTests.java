package com.klima.matej.tennis_reservations_system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klima.matej.tennis_reservations_system.entity.*;
import com.klima.matej.tennis_reservations_system.params.CourtCreateParams;
import com.klima.matej.tennis_reservations_system.params.CourtPatchParams;
import com.klima.matej.tennis_reservations_system.params.ReservationCreateParams;
import com.klima.matej.tennis_reservations_system.params.ReservationPatchParams;
import com.klima.matej.tennis_reservations_system.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TennisReservationsSystemApplication.class)
class TennisReservationsSystemApplicationTests {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@MockBean
	private CustomerService customerService;

	@MockBean
	private SurfaceService surfaceService;

	@MockBean
	private CourtService courtService;

	@MockBean
	private ReservationService reservationService;

	private static Surface SURFACE;
	private static Court COURT_1;
	private static Court COURT_2;
	private static Reservation RESERVATION_1;
	private static Reservation RESERVATION_2;
	private static Reservation RESERVATION_3;
	private static Customer CUSTOMER_1;
	private static Customer CUSTOMER_2;

	@BeforeEach
	public void setUp() {
		SURFACE = new Surface(1, "Test Surface", 10, new HashSet<>(), false);

		COURT_1 = new Court(1, "Test Court 1", SURFACE, new HashSet<>(), false);
		COURT_2 = new Court(2, "Test Court 2", SURFACE, new HashSet<>(), false);

		SURFACE.getCourts().add(COURT_1);
		SURFACE.getCourts().add(COURT_2);

		CUSTOMER_1 = Customer.builder()
						.id(1)
						.phoneNumber("Test Phone Number")
						.name("Test Customr 1")
						.reservations(new HashSet<>())
						.deleted(false)
						.build();

		CUSTOMER_2 = Customer.builder()
				.id(2)
				.phoneNumber("Test Customer 2 Phone Number")
				.name("Test Customer 2")
				.reservations(new HashSet<>())
				.deleted(false)
				.build();

		RESERVATION_1 = Reservation.builder()
				.id(1)
				.doubles(false)
				.createdAt(Timestamp.valueOf("2020-01-01 00:00:00"))
				.startsAt(new Timestamp(System.currentTimeMillis() - 1000000))
				.endsAt(new Timestamp(System.currentTimeMillis() - 1000000))
				.price(10)
				.customer(CUSTOMER_1)
				.court(COURT_1)
				.deleted(false)
				.build();

		RESERVATION_2 = Reservation.builder()
				.id(2)
				.doubles(false)
				.createdAt(Timestamp.valueOf("2019-01-01 00:00:00"))
				.startsAt(new Timestamp(System.currentTimeMillis()))
				.endsAt(new Timestamp(System.currentTimeMillis()))
				.price(20)
				.customer(CUSTOMER_2)
				.court(COURT_1)
				.deleted(false)
				.build();

		RESERVATION_3 = Reservation.builder()
				.id(3)
				.doubles(true)
				.createdAt(Timestamp.valueOf("2018-01-01 00:00:00"))
				.startsAt(new Timestamp(System.currentTimeMillis() + 1000000))
				.endsAt(new Timestamp(System.currentTimeMillis() + 1000000))
				.price(20)
				.customer(CUSTOMER_1)
				.court(COURT_2)
				.deleted(false)
				.build();

		COURT_1.getReservations().add(RESERVATION_1);
		COURT_2.getReservations().add(RESERVATION_2);
		COURT_2.getReservations().add(RESERVATION_3);

		CUSTOMER_1.getReservations().add(RESERVATION_1);
		CUSTOMER_1.getReservations().add(RESERVATION_2);
		CUSTOMER_2.getReservations().add(RESERVATION_3);
	}

	@Test
	public void createCourtTest() throws Exception {
		CourtCreateParams params = CourtCreateParams.builder()
				.name("Test Court Create")
				.surfaceId(SURFACE.getId())
				.build();

		Court newCourt = Court.builder()
						.name("Test Court Create")
						.surface(SURFACE)
						.build();

		doReturn(newCourt).when(courtService).save(isNotNull());
		doReturn(SURFACE).when(surfaceService).findById(SURFACE.getId());

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/courts")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Test Court Create")));
	}

	@Test
	public void createCourtTestSurfaceNotFound() throws Exception {
		CourtCreateParams params = CourtCreateParams.builder()
				.name("Test Court Create")
				.surfaceId(-1)
				.build();

		doReturn(null).when(surfaceService).findById(-1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/courts")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Surface with id -1 not found.")));
	}

	@Test
	public void getAllCourtsTest() throws Exception {
		List<Court> courts = List.of(COURT_1, COURT_2);

		doReturn(courts).when(courtService).findAll();

		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/courts/all")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[1].name", is("Test Court 2")));
	}

	@Test
	public void getCourtByIdTest() throws Exception {
		doReturn(COURT_1).when(courtService).findById(1);

		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/courts/id/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Test Court 1")));
	}

	@Test
	public void getCourtByIdTestNotFound() throws Exception {
		doReturn(null).when(courtService).findById(10);

		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/courts/id/10")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	public void updateCourtByIdTest() throws Exception {
		CourtCreateParams params = CourtCreateParams.builder()
				.name("Test Court Update")
				.surfaceId(SURFACE.getId())
				.build();

		Court updatedCourt = Court.builder()
				.name("Test Court Update")
				.surface(SURFACE)
				.build();

		doReturn(COURT_1).when(courtService).findById(1);
		doReturn(SURFACE).when(surfaceService).findById(SURFACE.getId());
		doReturn(updatedCourt).when(courtService).update(COURT_1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/courts/id/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Test Court Update")));
	}

	@Test
	public void updateCourtByIdTestCourtNotFound() throws Exception {
		CourtCreateParams params = CourtCreateParams.builder()
				.name("Test Court Update")
				.surfaceId(1)
				.build();

		doReturn(null).when(courtService).findById(-1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/courts/id/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Court with id -1 not found.")));
	}

	@Test
	public void updateCourtByIdTestSurfaceNotFound() throws Exception {
		CourtCreateParams params = CourtCreateParams.builder()
				.name("Test Court Update")
				.surfaceId(-1)
				.build();

		doReturn(COURT_1).when(courtService).findById(1);
		doReturn(null).when(surfaceService).findById(-1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/courts/id/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Surface with id -1 not found.")));
	}

	@Test
	public void patchCourtByIdTest() throws Exception {
		CourtPatchParams params = CourtPatchParams.builder()
				.name("Test Court Patch")
				.surfaceId(SURFACE.getId())
				.build();

		Court patchedCourt = Court.builder()
				.name("Test Court Patch")
				.surface(SURFACE)
				.build();

		doReturn(COURT_1).when(courtService).findById(1);
		doReturn(SURFACE).when(surfaceService).findById(SURFACE.getId());
		doReturn(patchedCourt).when(courtService).update(COURT_1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/courts/id/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Test Court Patch")));
	}

	@Test
	public void patchCourtByIdTestCourtNotFound() throws Exception {
		CourtPatchParams params = CourtPatchParams.builder()
				.name("Test Court Patch")
				.surfaceId(SURFACE.getId())
				.build();

		doReturn(null).when(courtService).findById(-1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/courts/id/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Court with id -1 not found.")));
	}

	@Test
	public void patchCourtByIdTestSurfaceNotGiven() throws Exception {
		CourtPatchParams params = CourtPatchParams.builder()
				.name("Test Court Patch")
				.surfaceId(-1)
				.build();

		Court patchedCourt = Court.builder()
				.name("Test Court Patch")
				.surface(SURFACE)
				.build();

		doReturn(COURT_1).when(courtService).findById(1);
		doReturn(null).when(surfaceService).findById(-1);
		doReturn(patchedCourt).when(courtService).update(COURT_1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/courts/id/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Test Court Patch")));
	}

	@Test
	public void patchCourtByIdTestSurfaceNotFound() throws Exception {
		CourtPatchParams params = CourtPatchParams.builder()
				.name("Test Court Patch")
				.surfaceId(10)
				.build();

		doReturn(COURT_1).when(courtService).findById(1);
		doReturn(null).when(surfaceService).findById(10);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/courts/id/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Surface with id 10 not found.")));
	}

	@Test
	public void deleteCourtTest() throws Exception {
		doReturn(true).when(courtService).deleteById(1);

		mockMvc.perform(MockMvcRequestBuilders
						.delete("/api/courts/id/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(true)));
	}

	@Test
	public void getAllReservationTest() throws Exception{
		List<Reservation> reservations = List.of(RESERVATION_1, RESERVATION_2, RESERVATION_3);

		doReturn(reservations).when(reservationService).findAll();

		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/reservations/all")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[1].id", is(2)));
	}

	@Test
	public void getReservationByIdTest() throws Exception {
		doReturn(RESERVATION_1).when(reservationService).findById(1);

		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/reservations/id/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.price", is(RESERVATION_1.getPrice())));
	}

	@Test
	public void getReservationByIdTestNotFound() throws Exception {
		doReturn(null).when(reservationService).findById(10);

		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/reservations/id/10")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	public void updateReservationByIdTest() throws Exception {
		ReservationCreateParams params = ReservationCreateParams.builder()
				.courtId(COURT_2.getId())
				.customerPhoneNumber(CUSTOMER_2.getPhoneNumber())
				.customerName(CUSTOMER_2.getName())
				.isDoubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.build();

		Reservation updatedReservation = Reservation.builder()
				.doubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.customer(CUSTOMER_2)
				.court(COURT_2)
				.build();

		doReturn(RESERVATION_1).when(reservationService).findById(1);
		doReturn(COURT_2).when(courtService).findById(COURT_2.getId());
		doReturn(CUSTOMER_2).when(customerService).findByPhoneNumber(CUSTOMER_2.getPhoneNumber());
		doReturn(updatedReservation).when(reservationService).update(RESERVATION_1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/reservations/id/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.court.name", is("Test Court 2")))
				.andExpect(jsonPath("$.customer.name", is("Test Customer 2")));
	}

	@Test
	public void updateReservationByIdTestReservationNotFound() throws Exception {
		ReservationCreateParams params = ReservationCreateParams.builder()
				.courtId(COURT_2.getId())
				.customerPhoneNumber(CUSTOMER_2.getPhoneNumber())
				.customerName(CUSTOMER_2.getName())
				.isDoubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.build();

		doReturn(null).when(reservationService).findById(-1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/reservations/id/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Reservation with id -1 not found.")));
	}

	@Test
	public void updateReservationByIdTestCourtNotFound() throws Exception {
		ReservationCreateParams params = ReservationCreateParams.builder()
				.courtId(-1)
				.customerPhoneNumber(CUSTOMER_2.getPhoneNumber())
				.customerName(CUSTOMER_2.getName())
				.isDoubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.build();

		doReturn(RESERVATION_1).when(reservationService).findById(1);
		doReturn(null).when(courtService).findById(-1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/reservations/id/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Court with id -1 not found.")));
	}

	@Test
	public void updateReservationByIdTestCreateCustomer() throws Exception {
		ReservationCreateParams params = ReservationCreateParams.builder()
				.courtId(COURT_2.getId())
				.customerPhoneNumber("Created phone number")
				.customerName("Created name")
				.isDoubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.build();

		Reservation updatedReservation = Reservation.builder()
				.doubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.customer(CUSTOMER_2)
				.court(COURT_2)
				.build();

		doReturn(RESERVATION_1).when(reservationService).findById(1);
		doReturn(COURT_2).when(courtService).findById(COURT_2.getId());
		doReturn(null).when(customerService).findByPhoneNumber("Created phone number");
		doReturn(updatedReservation).when(reservationService).update(RESERVATION_1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/reservations/id/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.court.name", is("Test Court 2")));
	}

	@Test
	public void patchReservationByIdTest() throws Exception {
		ReservationPatchParams params = ReservationPatchParams.builder()
				.courtId(COURT_2.getId())
				.customerPhoneNumber(CUSTOMER_2.getPhoneNumber())
				.customerName(CUSTOMER_2.getName())
				.isDoubles(true)
				.startsAt(Timestamp.valueOf("2026-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2026-01-01 11:00:00"))
				.createdAt(Timestamp.valueOf("2026-01-01 09:00:00"))
				.build();

		Reservation patchedReservation = Reservation.builder()
				.doubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.customer(CUSTOMER_2)
				.court(COURT_2)
				.build();

		doReturn(RESERVATION_1).when(reservationService).findById(1);
		doReturn(COURT_2).when(courtService).findById(COURT_2.getId());
		doReturn(CUSTOMER_2).when(customerService).findByPhoneNumber(CUSTOMER_2.getPhoneNumber());
		doReturn(patchedReservation).when(reservationService).update(RESERVATION_1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/reservations/id/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.court.name", is("Test Court 2")))
				.andExpect(jsonPath("$.customer.name", is("Test Customer 2")));
	}

	@Test
	public void patchReservationByIdTestReservationNotFound() throws Exception {
		ReservationCreateParams params = ReservationCreateParams.builder()
				.courtId(COURT_2.getId())
				.customerPhoneNumber(CUSTOMER_2.getPhoneNumber())
				.customerName(CUSTOMER_2.getName())
				.isDoubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.build();

		doReturn(null).when(reservationService).findById(-1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/reservations/id/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Reservation with id -1 not found.")));
	}

	@Test
	public void patchReservationByIdTestCourtNotFound() throws Exception {
		ReservationCreateParams params = ReservationCreateParams.builder()
				.courtId(999)
				.customerPhoneNumber(CUSTOMER_2.getPhoneNumber())
				.customerName(CUSTOMER_2.getName())
				.isDoubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.build();

		doReturn(RESERVATION_1).when(reservationService).findById(1);
		doReturn(null).when(courtService).findById(999);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/reservations/id/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Court with id 999 not found.")));
	}

	@Test
	public void patchReservationByIdTestCreateCustomer() throws Exception {
		ReservationCreateParams params = ReservationCreateParams.builder()
				.courtId(COURT_2.getId())
				.customerPhoneNumber("Created phone number")
				.customerName("Created name")
				.isDoubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.build();

		Reservation updatedReservation = Reservation.builder()
				.doubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.customer(CUSTOMER_2)
				.court(COURT_2)
				.build();

		doReturn(RESERVATION_1).when(reservationService).findById(1);
		doReturn(COURT_2).when(courtService).findById(COURT_2.getId());
		doReturn(null).when(customerService).findByPhoneNumber("Created phone number");
		doReturn(updatedReservation).when(reservationService).update(RESERVATION_1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.patch("/api/reservations/id/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.court.name", is("Test Court 2")));
	}

	@Test
	public void deleteReservationTest() throws Exception {
		doReturn(true).when(reservationService).deleteById(1);

		mockMvc.perform(MockMvcRequestBuilders
						.delete("/api/reservations/id/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(true)));
	}

	@Test
	public void getSortedReservationsByCourtIdTest() throws Exception {
		List<Reservation> reservations = new ArrayList<>();
		reservations.add(RESERVATION_1);
		reservations.add(RESERVATION_2);

		doReturn(reservations).when(reservationService).findAllByCourtId(1);

		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/reservations/court/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(2)));
	}

	@Test
	public void getAllReservationsByPhoneTest() throws Exception {
		List<Reservation> reservations = new ArrayList<>();
		reservations.add(RESERVATION_1);
		reservations.add(RESERVATION_3);

		doReturn(reservations).when(reservationService).findAllByPhoneNumber("123");

		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/reservations/phone/all/123")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1)));
	}

	@Test
	public void getFutureReservationsByPhoneTest() throws Exception {
		List<Reservation> reservations = new ArrayList<>();
		reservations.add(RESERVATION_3);

		doReturn(reservations).when(reservationService).findFutureByPhoneNumber("123");

		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/reservations/phone/future/123")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(3)));
	}

	@Test
	public void createReservationTest() throws Exception {
		ReservationCreateParams params = ReservationCreateParams.builder()
				.courtId(COURT_2.getId())
				.customerPhoneNumber(CUSTOMER_2.getPhoneNumber())
				.customerName(CUSTOMER_2.getName())
				.isDoubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.build();

		Reservation createdReservation = Reservation.builder()
				.doubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.customer(CUSTOMER_2)
				.court(COURT_2)
				.build();

		doReturn(COURT_2).when(courtService).findById(COURT_2.getId());
		doReturn(CUSTOMER_2).when(customerService).findByPhoneNumber(CUSTOMER_2.getPhoneNumber());
		doReturn(createdReservation).when(reservationService).save(isNotNull());

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/reservations")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$", is(createdReservation.calculatePrice())));
	}

	@Test
	public void createReservationTestCourtNotFound() throws Exception {
		ReservationCreateParams params = ReservationCreateParams.builder()
				.courtId(-1)
				.customerPhoneNumber(CUSTOMER_2.getPhoneNumber())
				.customerName(CUSTOMER_2.getName())
				.isDoubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.build();

		doReturn(null).when(courtService).findById(-1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/reservations")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Court with id -1 not found.")));
	}

	@Test
	public void createReservationTestCreateCustomer() throws Exception {
		ReservationCreateParams params = ReservationCreateParams.builder()
				.courtId(COURT_2.getId())
				.customerPhoneNumber("Created phone number")
				.customerName("Created name")
				.isDoubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.build();

		Reservation createdReservation = Reservation.builder()
				.doubles(true)
				.startsAt(Timestamp.valueOf("2025-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2025-01-01 11:00:00"))
				.customer(CUSTOMER_2)
				.court(COURT_2)
				.build();

		doReturn(COURT_2).when(courtService).findById(COURT_2.getId());
		doReturn(null).when(customerService).findByPhoneNumber("Created phone number");
		doReturn(createdReservation).when(reservationService).save(isNotNull());

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/reservations")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$", is(createdReservation.calculatePrice())));
	}

	@Test
	public void createReservationTestCourtOverlapping() throws Exception {
		List<Reservation> reservations = List.of(RESERVATION_1, RESERVATION_2);

		ReservationCreateParams params = ReservationCreateParams.builder()
				.courtId(1)
				.customerPhoneNumber(CUSTOMER_2.getPhoneNumber())
				.customerName(CUSTOMER_2.getName())
				.isDoubles(true)
				.startsAt(Timestamp.valueOf("2010-01-01 10:00:00"))
				.endsAt(Timestamp.valueOf("2030-01-01 11:00:00"))
				.build();

		doReturn(reservations).when(reservationService).findAllByCourtId(1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/reservations")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(params));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Reservation is overlapping with existing reservation.")));
	}

	@Test
	public void testInit() {
		TennisReservationsSystemApplication app = new TennisReservationsSystemApplication();

		assertTrue(app.initData());
	}

}
