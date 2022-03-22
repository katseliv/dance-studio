package com.dataart.dancestudio.service.logic.impl;

import com.dataart.dancestudio.db.repository.impl.BookingRepository;
import com.dataart.dancestudio.service.logic.BookingService;
import com.dataart.dancestudio.service.mapper.BookingMapper;
import com.dataart.dancestudio.service.model.BookingDto;
import com.dataart.dancestudio.service.model.view.BookingViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final BookingMapper mapper;

    @Autowired
    public BookingServiceImpl(BookingRepository repository, BookingMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void createBooking(BookingDto bookingDto) {
        repository.save(mapper.toEntity(bookingDto));
    }

    @Override
    public BookingDto getBookingById(int id) {
        return mapper.fromEntity(repository.findById(id).orElse(null));
    }

    @Override
    public BookingViewDto getBookingViewById(int id) {
        return mapper.fromEntity(repository.findViewById(id).orElse(null));
    }

    @Override
    public void updateBookingById(BookingDto bookingDto, int id) {
        repository.update(mapper.toEntity(bookingDto), id);
    }

    @Override
    public void deleteBookingById(int id) {
        repository.deleteById(id);
    }

    @Override
    public List<BookingViewDto> getAllBookings() {
        return mapper.fromEntities(repository.findAllViews());
    }

}
