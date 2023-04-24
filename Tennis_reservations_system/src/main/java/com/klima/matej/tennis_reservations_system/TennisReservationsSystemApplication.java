package com.klima.matej.tennis_reservations_system;

import com.klima.matej.tennis_reservations_system.entity.*;
import com.klima.matej.tennis_reservations_system.params.*;
import com.klima.matej.tennis_reservations_system.service.*;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * Main class of the application.
 */
@SpringBootApplication
@RestController
public class TennisReservationsSystemApplication {
	@Value("${initialize:false}")
	private boolean initialize;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SurfaceService surfaceService;

	@Autowired
	private CourtService courtService;

	@Autowired
	private ReservationService reservationService;

	/**
	 * Endpoint creating a new customer.
	 * @param params CustomerCreateParams object containing the data of the new customer.
	 * @return ResponseEntity containing the created customer object with 201 Created code in case of success.
	 * 	       ResponseEntity containing the error message with 400 Bad Request code in case of failure.
	 */
	@PostMapping("/api/courts")
	public ResponseEntity<Object> createCourt(@Valid @RequestBody CourtCreateParams params) {
		Surface surface = surfaceService.findById(params.getSurfaceId());
		if (surface == null) {
			String errorMessage = "Surface with id " + params.getSurfaceId() + " not found.";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		Court court = new Court();
		court.setName(params.getName());
		court.setSurface(surface);

		Court saved = courtService.save(court);

		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	/**
	 * Endpoint returning all existing courts.
	 * @return List of all existing courts.
	 */
	@GetMapping("/api/courts/all")
	public List<Court> getAllCourts() {
		return courtService.findAll();
	}

	/**
	 * Endpoint returning a court with the given id.
	 * @param id ID of the court to be returned.
	 * @return Court with the given id or null if there is no court with given ID.
	 */
	@GetMapping("/api/courts/id/{id}")
	public Court getCourtById(@PathVariable long id) {
		return courtService.findById(id);
	}

	/**
	 * Endpoint updating a court with the given id.
	 * @param id ID of the court to be updated.
	 * @param params CourtCreateParams object containing the data of the updated court.
	 * @return ResponseEntity containing the updated court object with 200 OK code in case of success.
	 * 	       ResponseEntity containing the error message with 400 Bad Request code in case of failure.
	 */
	@PutMapping("/api/courts/id/{id}")
	public ResponseEntity<Object> updateCourtById(@PathVariable long id, @Valid @RequestBody CourtCreateParams params) {
		Court court = courtService.findById(id);
		if (court == null) {
			String errorMessage = "Court with id " + id + " not found.";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		Surface surface = surfaceService.findById(params.getSurfaceId());
		if (surface == null) {
			String errorMessage = "Surface with id " + params.getSurfaceId() + " not found.";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		court.setName(params.getName());
		court.setSurface(surface);

		Court updated = courtService.update(court);

		return ResponseEntity.ok(updated);
	}

	/**
	 * Endpoint patching a court with the given id.
	 * @param id ID of the court to be patched.
	 * @param params CourtPatchParams object containing the data of the patched court.
	 * @return ResponseEntity containing the patched court object with 200 OK code in case of success.
	 * 	       ResponseEntity containing the error message with 400 Bad Request code in case of failure.
	 */
	@PatchMapping("/api/courts/id/{id}")
	public ResponseEntity<Object> patchCourtById(@PathVariable long id, @RequestBody CourtPatchParams params) {
		Court court = courtService.findById(id);
		if (court == null) {
			String errorMessage = "Court with id " + id + " not found.";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		if (params.getName() != null) {
			court.setName(params.getName());
		}

		if (params.getSurfaceId() != -1) {
			Surface surface = surfaceService.findById(params.getSurfaceId());
			if (surface == null) {
				String errorMessage = "Surface with id " + params.getSurfaceId() + " not found.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			court.setSurface(surface);
		}

		Court updated = courtService.update(court);

		return ResponseEntity.ok(updated);
	}

	/**
	 * Endpoint deleting a court with the given id.
	 * @param id ID of the court to be deleted.
	 * @return True if the court was deleted.
	 */
	@DeleteMapping("/api/courts/id/{id}")
	public boolean deleteCourtById(@PathVariable long id) {
		return courtService.deleteById(id);
	}

	/**
	 * Endpoint returning all existing reservations.
	 * @return List of all existing reservations.
	 */
	@GetMapping("/api/reservations/all")
	public List<Reservation> getAllReservations() {
		return reservationService.findAll();
	}

	/**
	 * Endpoint returning a reservation with the given id.
	 * @param id ID of the reservation to be returned.
	 * @return Reservation with the given id or null if there is no reservation with given ID.
	 */
	@GetMapping("/api/reservations/id/{id}")
	public Reservation getReservationById(@PathVariable long id) {
		return reservationService.findById(id);
	}

	/**
	 * Endpoint updating a reservation with the given id.
	 * @param id ID of the reservation to be updated.
	 * @param params ReservationCreateParams object containing the data of the updated reservation.
	 * @return ResponseEntity containing the updated reservation object with 200 OK code in case of success.
	 * 	       ResponseEntity containing the error message with 400 Bad Request code in case of failure.
	 */
	@PutMapping("/api/reservations/id/{id}")
	public ResponseEntity<Object> updateReservationById(@PathVariable long id, @Valid @RequestBody ReservationCreateParams params) {
		Reservation reservation = reservationService.findById(id);
		if (reservation == null) {
			String errorMessage = "Reservation with id " + id + " not found.";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		Court court = courtService.findById(params.getCourtId());
		if (court == null) {
			String errorMessage = "Court with id " + params.getCourtId() + " not found.";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		Customer customer = customerService.findByPhoneNumber(params.getCustomerPhoneNumber());
		if (customer == null) {
			customer = new Customer();
			customer.setPhoneNumber(params.getCustomerPhoneNumber());
			customer.setName(params.getCustomerName());
			customer = customerService.save(customer);
		}

		reservation.setCourt(court);
		reservation.setCustomer(customer);
		reservation.setDoubles(params.isDoubles());
		reservation.setStartsAt(params.getStartsAt());
		reservation.setEndsAt(params.getEndsAt());
		reservation.setPrice(reservation.calculatePrice());

		Reservation updated = reservationService.update(reservation);

		return ResponseEntity.ok(updated);
	}

	/**
	 * Endpoint patching a reservation with the given id.
	 * @param id ID of the reservation to be patched.
	 * @param params ReservationPatchParams object containing the data of the patched reservation.
	 * @return ResponseEntity containing the patched reservation object with 200 OK code in case of success.
	 * 	       ResponseEntity containing the error message with 400 Bad Request code in case of failure.
	 */
	@PatchMapping("/api/reservations/id/{id}")
	public ResponseEntity<Object> patchReservationById(@PathVariable long id, @RequestBody ReservationPatchParams params) {
		Reservation reservation = reservationService.findById(id);
		if (reservation == null) {
			String errorMessage = "Reservation with id " + id + " not found.";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		if (params.getCourtId() != -1) {
			Court court = courtService.findById(params.getCourtId());
			if (court == null) {
				String errorMessage = "Court with id " + params.getCourtId() + " not found.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			reservation.setCourt(court);
		}

		if (params.getCustomerPhoneNumber() != null) {
			Customer customer = customerService.findByPhoneNumber(params.getCustomerPhoneNumber());
			if (customer == null) {
				customer = new Customer();
				customer.setPhoneNumber(params.getCustomerPhoneNumber());
				customer.setName(params.getCustomerName());
				customer = customerService.save(customer);
			}
			reservation.setCustomer(customer);
		}

		if (params.getIsDoubles() != null) {
			reservation.setDoubles(params.getIsDoubles());
		}

		if (params.getStartsAt() != null) {
			reservation.setStartsAt(params.getStartsAt());
		}

		if (params.getEndsAt() != null) {
			reservation.setEndsAt(params.getEndsAt());
		}

		if (params.getCreatedAt() != null) {
			reservation.setCreatedAt(params.getCreatedAt());
		}

		reservation.setPrice(reservation.calculatePrice());

		Reservation updated = reservationService.update(reservation);

		return ResponseEntity.ok(updated);
	}

	/**
	 * Endpoint deleting a reservation with the given id.
	 * @param id ID of the reservation to be deleted.
	 * @return True if the reservation was deleted.
	 */
	@DeleteMapping("/api/reservations/id/{id}")
	public boolean deleteReservationById(@PathVariable long id) {
		return reservationService.deleteById(id);
	}

	/**
	 * Endpoint returning all reservations for a court with the given id sorted by creation time.
	 * @param id ID of the court for which the reservations should be returned.
	 * @return List of all reservations for the court with the given id sorted by creation time.
	 */
	@GetMapping("/api/reservations/court/{id}")
	public List<Reservation> getAllReservationsByCourtId(@PathVariable long id) {
		List<Reservation> reservations = reservationService.findAllByCourtId(id);
		reservations.sort(Comparator.comparing(Reservation::getCreatedAt));
		return reservations;
	}

	/**
	 * Endpoint returning all reservations for a customer with the given phone number.
	 * @param phoneNumber Phone number of the customer for which the reservations should be returned.
	 * @return List of all reservations for the customer with the given phone number.
	 */
	@GetMapping("/api/reservations/phone/all/{phoneNumber}")
	public List<Reservation> getAllReservationsByPhoneNumber(@PathVariable String phoneNumber) {
		return reservationService.findAllByPhoneNumber(phoneNumber);
	}

	/**
	 * Endpoint returning all reservations in future for a customer with the given phone number.
	 * @param phoneNumber Phone number of the customer for which the reservations should be returned.
	 * @return List of all reservations in future for the customer with the given phone number.
	 */
	@GetMapping("/api/reservations/phone/future/{phoneNumber}")
	public List<Reservation> getFutureReservationsByPhoneNumber(@PathVariable String phoneNumber) {
		return reservationService.findFutureByPhoneNumber(phoneNumber);
	}

	/**
	 * Endpoint creating a new reservation.
	 * @param params ReservationCreateParams object containing the data of the new reservation.
	 * @return ResponseEntity containing the created reservation object with 200 OK code in case of success.
	 * 	       ResponseEntity containing the error message with 400 Bad Request code in case of failure.
	 */
	@PostMapping("/api/reservations")
	public ResponseEntity<Object> createReservation(@Valid @RequestBody ReservationCreateParams params) {
		List<Reservation> reservations = reservationService.findAllByCourtId(params.getCourtId());
		if (Reservation.isOverlapping(params.getStartsAt(), params.getEndsAt(), reservations)) {
			String errorMessage = "Reservation is overlapping with existing reservation.";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		Court court = courtService.findById(params.getCourtId());
		if (court == null) {
			String errorMessage = "Court with id " + params.getCourtId() + " not found.";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		Customer customer = customerService.findByPhoneNumber(params.getCustomerPhoneNumber());
		if (customer == null) {
			customer = new Customer();
			customer.setPhoneNumber(params.getCustomerPhoneNumber());
			customer.setName(params.getCustomerName());
			customer = customerService.save(customer);
		}

		Reservation reservation = new Reservation();
		reservation.setCourt(court);
		reservation.setCustomer(customer);
		reservation.setDoubles(params.isDoubles());
		reservation.setStartsAt(params.getStartsAt());
		reservation.setEndsAt(params.getEndsAt());
		reservation.setPrice(reservation.calculatePrice());

		Reservation saved = reservationService.save(reservation);

		return ResponseEntity.status(HttpStatus.CREATED).body(saved.calculatePrice());
	}

	/**
	 * Function initializing the database with some data.
	 * @return True if the initialization was successful.
	 */
	public boolean initData() {
		surfaceService = new SurfaceServiceImpl();
		courtService = new CourtServiceImpl();

		Surface claySurface = new Surface();
		claySurface.setName("Clay");
		claySurface.setMinutePrice(10);
		if (surfaceService.save(claySurface) == null) {return false;}

		Court court1 = new Court();
		court1.setName("Court 1");
		court1.setSurface(claySurface);
		if (courtService.save(court1) == null) {return false;}

		Court court2 = new Court();
		court2.setName("Court 2");
		court2.setSurface(claySurface);
		if (courtService.save(court2) == null) {return false;}

		claySurface.getCourts().add(court1);
		claySurface.getCourts().add(court2);
		if (surfaceService.update(claySurface) == null) {return false;}

		Surface grassSurface = new Surface();
		grassSurface.setName("Grass");
		grassSurface.setMinutePrice(15);
		if (surfaceService.save(grassSurface) == null) {return false;}

		Court court3 = new Court();
		court3.setName("Court 3");
		court3.setSurface(grassSurface);
		if (courtService.save(court3) == null) {return false;}

		Court court4 = new Court();
		court4.setName("Court 4");
		court4.setSurface(grassSurface);
		if (courtService.save(court4) == null) {return false;}

		grassSurface.getCourts().add(court3);
		grassSurface.getCourts().add(court4);
		return surfaceService.update(grassSurface) != null;
	}

	public static void main(String[] args) {
		SpringApplication.run(TennisReservationsSystemApplication.class, args);
	}

	@PostConstruct
	public void init() {
		if (initialize) {
			initData();
		}
	}
}
