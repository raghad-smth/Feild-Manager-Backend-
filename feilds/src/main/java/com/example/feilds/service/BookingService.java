package com.example.feilds.service;

import com.example.feilds.model.*;
import com.example.feilds.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {

    private final FieldsRepository fieldsRepository;
    private final FeildSlotsRepository fieldSlotsRepository;
    private final BookingRepository bookingsRepository;
    private final UserRepository userRepository;

    public BookingService(FieldsRepository fieldsRepository,
                          FeildSlotsRepository fieldSlotsRepository,
                          BookingRepository bookingsRepository,
                          UserRepository userRepository) {
        this.fieldsRepository = fieldsRepository;
        this.fieldSlotsRepository = fieldSlotsRepository;
        this.bookingsRepository = bookingsRepository;
        this.userRepository = userRepository;
    }

    public boolean isBookedDuringRequestedTime(Integer fieldId, LocalDate date, LocalTime from, LocalTime to) {
        List<Bookings> bookings = bookingsRepository.findByFieldAndDate(fieldId, date);
        for (Bookings booking : bookings) {
            LocalTime bookedFrom = booking.getFieldSlot().getFrom();
            LocalTime bookedTo = booking.getFieldSlot().getTo();
            if (from.isBefore(bookedTo) && to.isAfter(bookedFrom)) {
                return true; // overlapping booking
            }
        }
        return false;
    }

    public String bookField(Integer fieldId, Integer weekDayId, LocalTime from, LocalTime to,
                            BigDecimal price, Integer adminId, LocalDate date, String role) {

        if (!role.equalsIgnoreCase("ADMIN")) {
            return "Only admins can book fields.";
        }

        Fields field = fieldsRepository.findById(fieldId).orElse(null);
        if (field == null || !field.getIsActive()) {
            return "Field is not active or doesn't exist.";
        }

        if (isBookedDuringRequestedTime(fieldId, date, from, to)) {
            return "Field is already booked during this time.";
        }

        FieldSlots slot = FieldSlots.builder()
                .field(field)
                .weekDay(new WeekDays(weekDayId, null))
                .from(from)
                .to(to)
                .price(price)
                .build();
        fieldSlotsRepository.save(slot);

        Users admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Bookings booking = Bookings.builder()
                .player(admin)
                .fieldSlot(slot)
                .date(date)
                .price(price)
                .status("BOOKED")
                .team(null)
                .build();
        bookingsRepository.save(booking);

        return "Field booked successfully!\n\n" +
                "Field Name: " + field.getName() + "\n" +
                "Date: " + date + "\n" +
                "From: " + from + "\n" +
                "To: " + to + "\n" +
                "Total Price: " + price + " EGP";
    }
}
