package com.events.planner.service.impl;

import com.events.planner.dto.ReservationDto;
import com.events.planner.dto.ReservationHistoryDto;
import com.events.planner.entity.Event;
import com.events.planner.entity.Hall;
import com.events.planner.entity.Reservation;
import com.events.planner.entity.ReservationHistory;
import com.events.planner.entity.ReservationHistoryAction;
import com.events.planner.entity.ReservationStatus;
import com.events.planner.entity.User;
import com.events.planner.mapper.impl.ReservationDtoEntityMapper;
import com.events.planner.mapper.impl.ReservationHistoryDtoEntityMapper;
import com.events.planner.repository.EventRepository;
import com.events.planner.repository.HallRepository;
import com.events.planner.repository.ReservationHistoryRepository;
import com.events.planner.repository.ReservationRepository;
import com.events.planner.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

public class ReservationServiceImplHistoryTest {

    private static final Long RESERVATION_ID = 100L;
    private static final Long USER_ID = 1L;
    private static final Long HALL_ID = 2L;
    private static final Long EVENT_ID = 3L;
    private static final String USER_EMAIL = "user@example.com";
    private static final String ADMIN_EMAIL = "admin@example.com";

    private ReservationRepository reservationRepository;
    private ReservationHistoryRepository reservationHistoryRepository;
    private UserRepository userRepository;
    private HallRepository hallRepository;
    private EventRepository eventRepository;
    private ReservationServiceImpl reservationService;

    private User user;
    private Hall hall;
    private Event event;

    @BeforeMethod
    public void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        reservationHistoryRepository = mock(ReservationHistoryRepository.class);
        userRepository = mock(UserRepository.class);
        hallRepository = mock(HallRepository.class);
        eventRepository = mock(EventRepository.class);

        reservationService = new ReservationServiceImpl(
                reservationRepository,
                reservationHistoryRepository,
                userRepository,
                hallRepository,
                eventRepository,
                new ReservationDtoEntityMapper(),
                new ReservationHistoryDtoEntityMapper()
        );

        user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);

        hall = new Hall();
        hall.setId(HALL_ID);
        hall.setCapacity(100);

        event = new Event();
        event.setId(EVENT_ID);
        event.setCapacity(50);
    }

    @Test
    public void shouldCreateHistorySnapshotWhenReservationIsCreated() throws Exception {
        ReservationDto dto = validReservationDto();

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(hallRepository.findById(HALL_ID)).thenReturn(Optional.of(hall));
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation reservation = invocation.getArgument(0);
            reservation.setId(RESERVATION_ID);
            return reservation;
        });

        ReservationDto result = reservationService.create(dto, USER_EMAIL);

        ArgumentCaptor<ReservationHistory> historyCaptor = ArgumentCaptor.forClass(ReservationHistory.class);
        verify(reservationHistoryRepository).save(historyCaptor.capture());

        ReservationHistory history = historyCaptor.getValue();

        assertEquals(result.getId(), RESERVATION_ID);
        assertEquals(history.getReservationId(), RESERVATION_ID);
        assertEquals(history.getAction(), ReservationHistoryAction.CREATED);
        assertEquals(history.getChangedBy(), USER_EMAIL);
        assertNotNull(history.getChangedAt());
        assertEquals(history.getStart(), dto.getStart());
        assertEquals(history.getEnd(), dto.getEnd());
        assertEquals(history.getStatus(), ReservationStatus.PENDING);
        assertEquals(history.getDescription(), dto.getDescription());
        assertEquals(history.getUserId(), USER_ID);
        assertEquals(history.getHallId(), HALL_ID);
        assertEquals(history.getEventId(), EVENT_ID);
    }

    @Test
    public void shouldCreateHistorySnapshotWhenReservationIsUpdated() throws Exception {
        Reservation existingReservation = existingReservation(ReservationStatus.PENDING);
        LocalDateTime originalTimestamp = existingReservation.getTimestamp();

        Hall updatedHall = new Hall();
        updatedHall.setId(20L);
        updatedHall.setCapacity(120);

        Event updatedEvent = new Event();
        updatedEvent.setId(30L);
        updatedEvent.setCapacity(60);

        ReservationDto updateDto = new ReservationDto(
                RESERVATION_ID,
                LocalDateTime.of(2026, 9, 22, 12, 0),
                LocalDateTime.of(2026, 9, 22, 14, 0),
                "PENDING",
                "Updated description",
                null,
                USER_ID,
                updatedHall.getId(),
                updatedEvent.getId()
        );

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(existingReservation));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(hallRepository.findById(updatedHall.getId())).thenReturn(Optional.of(updatedHall));
        when(eventRepository.findById(updatedEvent.getId())).thenReturn(Optional.of(updatedEvent));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReservationDto result = reservationService.update(
                RESERVATION_ID,
                updateDto,
                userAuthentication()
        );

        ArgumentCaptor<ReservationHistory> historyCaptor = ArgumentCaptor.forClass(ReservationHistory.class);
        verify(reservationHistoryRepository).save(historyCaptor.capture());
        ReservationHistory history = historyCaptor.getValue();

        assertEquals(result.getStart(), updateDto.getStart());
        assertEquals(result.getEnd(), updateDto.getEnd());
        assertEquals(history.getAction(), ReservationHistoryAction.UPDATED);
        assertEquals(history.getReservationId(), RESERVATION_ID);
        assertEquals(history.getChangedBy(), USER_EMAIL);
        assertEquals(history.getStart(), updateDto.getStart());
        assertEquals(history.getEnd(), updateDto.getEnd());
        assertEquals(history.getDescription(), "Updated description");
        assertEquals(history.getStatus(), ReservationStatus.PENDING);
        assertEquals(history.getUserId(), USER_ID);
        assertEquals(history.getHallId(), updatedHall.getId());
        assertEquals(history.getEventId(), updatedEvent.getId());
        assertEquals(existingReservation.getTimestamp(), originalTimestamp);
    }

    @Test
    public void shouldCreateUpdatedHistoryWhenStatusChanges() throws Exception {
        Reservation reservation = existingReservation(ReservationStatus.PENDING);

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
        when(reservationRepository.existsHallReservationConflict(
                HALL_ID,
                reservation.getStart(),
                reservation.getEnd(),
                ReservationStatus.APPROVED,
                RESERVATION_ID
        )).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReservationDto result = reservationService.updateStatus(
                RESERVATION_ID,
                "APPROVED",
                adminAuthentication()
        );

        ArgumentCaptor<ReservationHistory> historyCaptor = ArgumentCaptor.forClass(ReservationHistory.class);
        verify(reservationHistoryRepository).save(historyCaptor.capture());
        ReservationHistory history = historyCaptor.getValue();

        assertEquals(result.getStatus(), "APPROVED");
        assertEquals(history.getAction(), ReservationHistoryAction.UPDATED);
        assertEquals(history.getStatus(), ReservationStatus.APPROVED);
        assertEquals(history.getChangedBy(), ADMIN_EMAIL);
        assertEquals(history.getReservationId(), RESERVATION_ID);
    }

    @Test
    public void shouldCreateUpdatedHistoryWhenReservationIsCancelledByOwner() throws Exception {
        Reservation reservation = existingReservation(ReservationStatus.APPROVED);

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReservationDto result = reservationService.updateStatus(
                RESERVATION_ID,
                "CANCELLED",
                userAuthentication()
        );

        ArgumentCaptor<ReservationHistory> historyCaptor = ArgumentCaptor.forClass(ReservationHistory.class);
        verify(reservationHistoryRepository).save(historyCaptor.capture());
        ReservationHistory history = historyCaptor.getValue();

        assertEquals(result.getStatus(), "CANCELLED");
        assertEquals(history.getAction(), ReservationHistoryAction.UPDATED);
        assertEquals(history.getStatus(), ReservationStatus.CANCELLED);
        assertEquals(history.getChangedBy(), USER_EMAIL);
    }

    @Test
    public void shouldCreateDeletedHistoryBeforeDeletingReservation() throws Exception {
        Reservation reservation = existingReservation(ReservationStatus.APPROVED);
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        reservationService.delete(RESERVATION_ID, ADMIN_EMAIL);

        ArgumentCaptor<ReservationHistory> historyCaptor = ArgumentCaptor.forClass(ReservationHistory.class);
        InOrder inOrder = inOrder(reservationHistoryRepository, reservationRepository);
        inOrder.verify(reservationHistoryRepository).save(historyCaptor.capture());
        inOrder.verify(reservationRepository).deleteById(RESERVATION_ID);

        ReservationHistory history = historyCaptor.getValue();
        assertEquals(history.getAction(), ReservationHistoryAction.DELETED);
        assertEquals(history.getReservationId(), RESERVATION_ID);
        assertEquals(history.getChangedBy(), ADMIN_EMAIL);
        assertEquals(history.getStatus(), ReservationStatus.APPROVED);
        assertEquals(history.getUserId(), USER_ID);
        assertEquals(history.getHallId(), HALL_ID);
        assertEquals(history.getEventId(), EVENT_ID);
    }

    @Test
    public void shouldReturnReservationHistoryInChronologicalOrder() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 9, 20, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 9, 20, 11, 0);
        LocalDateTime deletedAt = LocalDateTime.of(2026, 9, 20, 12, 0);

        ReservationHistory created = historyEntry(1L, ReservationHistoryAction.CREATED, createdAt);
        ReservationHistory updated = historyEntry(2L, ReservationHistoryAction.UPDATED, updatedAt);
        ReservationHistory deleted = historyEntry(3L, ReservationHistoryAction.DELETED, deletedAt);

        when(reservationHistoryRepository.findByReservationIdOrderByChangedAtAscIdAsc(RESERVATION_ID))
                .thenReturn(List.of(created, updated, deleted));

        List<ReservationHistoryDto> history = reservationService.getHistory(RESERVATION_ID);

        assertEquals(history.size(), 3);
        assertEquals(history.get(0).getAction(), "CREATED");
        assertEquals(history.get(1).getAction(), "UPDATED");
        assertEquals(history.get(2).getAction(), "DELETED");
        assertEquals(history.get(0).getChangedAt(), createdAt);
        assertEquals(history.get(1).getChangedAt(), updatedAt);
        assertEquals(history.get(2).getChangedAt(), deletedAt);
        assertEquals(history.get(0).getReservationId(), RESERVATION_ID);
    }

    // negative tests
    
    @Test
    public void shouldNotCreateHistoryWhenReservationCreationFailsValidation() {
        ReservationDto invalidDto = validReservationDto();
        invalidDto.setEnd(invalidDto.getStart());

        try {
            reservationService.create(invalidDto, USER_EMAIL);
            fail("Expected validation to fail.");
        } catch (Exception exception) {
            assertEquals(exception.getMessage(), "End time must be after start time.");
        }

        verifyNoInteractions(reservationHistoryRepository);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    public void shouldNotCreateHistoryWhenStatusChangeConflictsWithExistingReservation() {
        Reservation reservation = existingReservation(ReservationStatus.PENDING);

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
        when(reservationRepository.existsHallReservationConflict(
                HALL_ID,
                reservation.getStart(),
                reservation.getEnd(),
                ReservationStatus.APPROVED,
                RESERVATION_ID
        )).thenReturn(true);

        try {
            reservationService.updateStatus(
                    RESERVATION_ID,
                    "APPROVED",
                    adminAuthentication()
            );
            fail("Expected approval conflict to fail.");
        } catch (Exception exception) {
            assertEquals(
                    exception.getMessage(),
                    "Cannot approve reservation. Hall is already reserved in that time period."
            );
        }

        verifyNoInteractions(reservationHistoryRepository);
        verify(reservationRepository, never()).save(any(Reservation.class));
        assertFalse(reservation.getStatus() == ReservationStatus.APPROVED);
    }
    
    // helpers

    private ReservationDto validReservationDto() {
        return new ReservationDto(
                null,
                LocalDateTime.of(2026, 9, 21, 10, 0),
                LocalDateTime.of(2026, 9, 21, 12, 0),
                null,
                "Reservation description",
                null,
                null,
                HALL_ID,
                EVENT_ID
        );
    }

    private Reservation existingReservation(ReservationStatus status) {
        return new Reservation(
                RESERVATION_ID,
                LocalDateTime.of(2026, 9, 21, 10, 0),
                LocalDateTime.of(2026, 9, 21, 12, 0),
                status,
                "Reservation description",
                LocalDateTime.of(2026, 9, 15, 9, 0),
                user,
                hall,
                event
        );
    }

    private ReservationHistory historyEntry(Long id,
            ReservationHistoryAction action,
            LocalDateTime changedAt) {
        return new ReservationHistory(
                id,
                RESERVATION_ID,
                action,
                changedAt,
                USER_EMAIL,
                LocalDateTime.of(2026, 9, 21, 10, 0),
                LocalDateTime.of(2026, 9, 21, 12, 0),
                ReservationStatus.PENDING,
                "Reservation description",
                USER_ID,
                HALL_ID,
                EVENT_ID
        );
    }

    private Authentication userAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                USER_EMAIL,
                null,
                AuthorityUtils.createAuthorityList("ROLE_USER")
        );
    }

    private Authentication adminAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                ADMIN_EMAIL,
                null,
                AuthorityUtils.createAuthorityList("ROLE_ADMIN")
        );
    }
}
